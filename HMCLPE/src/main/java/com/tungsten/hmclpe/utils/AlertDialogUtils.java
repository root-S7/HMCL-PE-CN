package com.tungsten.hmclpe.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogUtils {
    private final Activity activity;

    public AlertDialogUtils(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog.Builder createAlertDialog(String message, DialogInterface.OnClickListener negativeButtonEvent, String negativeButton){
        return createAlertDialog(null,message,null,negativeButtonEvent,null,negativeButton);
    }

    public AlertDialog.Builder createAlertDialog(String title, String message, DialogInterface.OnClickListener positiveButtonEvent, DialogInterface.OnClickListener negativeButtonEvent, String positiveButton, String negativeButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);
        if(title != null && !title.isEmpty()) builder.setTitle(title);
        if(message != null && !message.isEmpty()) builder.setMessage(message);
        if(positiveButtonEvent != null && positiveButton != null) builder.setPositiveButton(positiveButton,positiveButtonEvent);
        if(negativeButtonEvent != null && negativeButton != null) builder.setNegativeButton(negativeButton,negativeButtonEvent);
        builder.create();
        builder.show();

        return builder;
    }
}
