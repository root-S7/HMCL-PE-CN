package com.tungsten.hmclpe.launcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.setting.*;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.utils.*;
import com.tungsten.hmclpe.utils.io.FileUtils;
import org.json.JSONObject;
import java.io.File;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private AlertDialog dialog;

    public ProgressBar loadingProgress;
    public TextView loadingText;
    public TextView loadingProgressText;

    public TextView titleTextFirst;
    public TextView titleTextSecond;
    public TextView titleTextThird;
    public ConstraintLayout background;

    public LauncherSetting launcherSetting;

    private PermissionUtils permissionUtils;
    private final String[] legacyAccessFilesPermission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingProgress = findViewById(R.id.loading_progress_bar);
        loadingText = findViewById(R.id.loading_text);
        loadingProgressText = findViewById(R.id.loading_progress_text);

        titleTextFirst = findViewById(R.id.title_text_first);
        titleTextSecond = findViewById(R.id.title_text_second);
        titleTextThird = findViewById(R.id.title_text_third);
        background = findViewById(R.id.background);

        permissionUtils = new PermissionUtils(this);

        initTheme();
        requestPermission();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtils.setLanguage(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.setLanguage(this);
    }

    private void initTheme() {
        File themePath = getExternalFilesDir("Theme");
        if (!themePath.exists()) {
            return;
        }
        changeIcon(background, themePath, "splashBackground");
        if (new File(themePath, "text.json").exists()) {
            try {
                JSONObject jsonObject = new JSONObject(FileUtils.readText(new File(themePath, "text.json")));
                String s1 = jsonObject.getString("titleTextFirst");
                String s2 = jsonObject.getString("titleTextSecond");
                String s3 = jsonObject.getString("titleTextThird");
                String s5 = jsonObject.getString("textColor");
                if (!s1.equals("")) {
                    titleTextFirst.setText(s1);
                }
                if (!s2.equals("")) {
                    titleTextSecond.setText(s2);
                }
                if (!s3.equals("")) {
                    titleTextThird.setText(s3);
                }
                if (!s5.equals("")) {
                    titleTextFirst.setTextColor(Color.parseColor(s5));
                    titleTextSecond.setTextColor(Color.parseColor(s5));
                    titleTextThird.setTextColor(Color.parseColor(s5));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeIcon(View view, File themePath, String iconName) {
        File path = new File(themePath, iconName + ".png");
        if (path.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path.getAbsolutePath());
            view.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if(Environment.isExternalStorageManager()) {
                init();
            }else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }else {
            // 先判断有没有权限
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                init();
            }else {
                ActivityCompat.requestPermissions(this, legacyAccessFilesPermission, 1);
            }
        }
    }

    private void init() {
        if(dialog != null) dialog.dismiss();

        new Thread(() -> {
            AppManifest.initializeManifest(SplashActivity.this);
            launcherSetting = InitializeSetting.initializeLauncherSetting();

            runOnUiThread(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (launcherSetting.fullscreen) {
                        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    } else {
                        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
                    }
                }
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            });

            InstallLauncherFile.checkLauncherFiles(SplashActivity.this);
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // 检查是否授予了所有必要权限
            if(!hasAllPermissionsGranted()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setCancelable(false).setMessage(R.string.storage_permissions_remind).setPositiveButton("授予权限", (dialog1, which) -> {
                    dialog1.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", permissionUtils.getActivity().getPackageName(),null));
                    startActivityForResult(intent,2);
                }).setNegativeButton("关闭应用", (dialog2, which) -> System.exit(0));
                dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }else init();
        }
    }

    private boolean hasAllPermissionsGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()) {
                init();
            }else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setCancelable(false).setMessage(R.string.storage_permissions_remind).setPositiveButton("授予权限", (dialog1, which) -> {
                    dialog1.dismiss();
                    requestPermission();
                }).setNegativeButton("关闭应用", (dialog2, which) -> System.exit(0));
                dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }else if(requestCode == 2) requestPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
