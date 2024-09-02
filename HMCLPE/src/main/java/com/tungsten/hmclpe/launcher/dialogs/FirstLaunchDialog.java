package com.tungsten.hmclpe.launcher.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.HMCLPEApplication;
import java.io.*;
import java.net.*;

public class FirstLaunchDialog extends Dialog implements View.OnClickListener {
    private Button positive;
    private final TextView TV;
    private Handler handler;
    private String allStr = "";

    public FirstLaunchDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_first_start);
        TV = findViewById(R.id.eula);
        setCancelable(false);
        init();
    }

    private void init() {
        positive = findViewById(R.id.positive);
        positive.setOnClickListener(this);
        run();
    }

    public void run(){
        handler = new Handler();
        new Thread(() -> {
            String url = HMCLPEApplication.properties.getProperty("announcement-url","https://icraft.ren:90:90/titles/HMCL-PE/2.0.6/announcement.txt");
            String inputLine;
            try {
                URL oracle = new URL(url);
                URLConnection conn = oracle.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((inputLine = br.readLine()) != null) allStr = allStr + inputLine + "\n";
            }catch (IOException e) {
                allStr = "网络异常[若长期看见此消息说明服务器挂了]\n" + e;
            }
            handler.post(runnableUi);
        }).start();
    }

    Runnable runnableUi = new Runnable(){
        @Override
        public void run() {
            //在这里写更新UI的操作
            TV.setText(allStr);
        }

    };

    @SuppressLint("CommitPrefEdits") @Override
    public void onClick(View view) {
        if (view == positive) {
            dismiss();
        }
    }

}