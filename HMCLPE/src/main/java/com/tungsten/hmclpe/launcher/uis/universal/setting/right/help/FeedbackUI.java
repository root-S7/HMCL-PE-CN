package com.tungsten.hmclpe.launcher.uis.universal.setting.right.help;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class FeedbackUI extends BaseUI implements View.OnClickListener {

    public LinearLayout feedbackUI;

    private ImageButton joinDiscord;
    private ImageButton joinQQChannel;
    private ImageButton joinQQ;
    private ImageButton jumpToGit;

    public FeedbackUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        feedbackUI = activity.findViewById(R.id.ui_feedback);

        joinDiscord = activity.findViewById(R.id.join_discord);
        joinQQChannel = activity.findViewById(R.id.join_qq_channel);
        joinQQ = activity.findViewById(R.id.join_qq_group);
        jumpToGit = activity.findViewById(R.id.jump_to_git_issues);

        joinDiscord.setOnClickListener(this);
        joinQQChannel.setOnClickListener(this);
        joinQQ.setOnClickListener(this);
        jumpToGit.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(feedbackUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startFeedbackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(feedbackUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startFeedbackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    /****************
     *
     * ?????????????????????????????????Boat???|HMCLPE??????????????????(965085729) ??? key ?????? ZrtZkHED9ad1edu-TKYvVYz6iGzAa-nS
     * ?????? joinQQGroup(ZrtZkHED9ad1edu-TKYvVYz6iGzAa-nS) ???????????????Q????????????????????? Boat???|HMCLPE??????????????????(965085729)
     *
     * @param key ??????????????????key
     * @return ??????true???????????????Q???????????????false??????????????????
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // ???Flag??????????????????????????????????????????????????????????????????????????????????????????Q???????????????????????????????????????????????????????????????    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // ????????????Q???????????????????????????
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == joinDiscord) {
            Uri uri = Uri.parse("https://icraft.ren:90/titles/Discord");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (v == joinQQChannel) {
            Uri uri = Uri.parse("https://icraft.ren:90/titles/QQGroup");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (v == joinQQ){
            //7rX0cr37hu_jNPaGIlqAEf4Ndv1BG-WU
            joinQQGroup(context, "ZrtZkHED9ad1edu-TKYvVYz6iGzAa-nS");
        }
        if (v == jumpToGit){
            Uri uri = Uri.parse("https://github.com/root-S7/HMCL-PE_Server-CN/issues");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}
