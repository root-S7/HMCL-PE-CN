package com.tungsten.hmclpe.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtils{
    private List<String> permissionList;
    private Activity activity;

    public PermissionUtils(Activity activity){
        this.activity = activity;
        getPackagePermissionList();
    }

    /**
     * 当前应用权限列表
    **/
    private void getPackagePermissionList(){
        PackageManager packageManager = activity.getPackageManager();
        try{
            permissionList = Arrays.asList(packageManager.getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前应用没有的权限列表
     * @return 没有的权限列表集
    **/
    public List<String> getPackageNoPermissionList(){
        List<String> noPermissionList = new ArrayList<>();
        for(String permission : permissionList) if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) noPermissionList.add(permission);
        return noPermissionList;
    }

    /**
     * 指定权限列表中查找没有获得的权限是什么
     * @param permissionList 权限列表
     * @return 没有的权限列表集
    **/
    public List<String> getNoPermissionList(List<String> permissionList){
        List<String> noPermissionList = new ArrayList<>();
        for(String permission : permissionList) if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) noPermissionList.add(permission);
        return noPermissionList;
    }
    public String[] getNoPermissionList(String[] permissionList){
        List<String> noPermissionList = new ArrayList<>();
        for(String permission : permissionList) if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) noPermissionList.add(permission);
        return noPermissionList.toArray(new String[0]);
    }

    /**
     * 判断列表中的权限是否全部都有
     * @param permissionList 权限列表
     * @return 只要一个没有就返回false
    **/
    public boolean listPermissionHaveAll(String[] permissionList){
        for (String s : permissionList) if(ContextCompat.checkSelfPermission(activity,s) == PackageManager.PERMISSION_DENIED) return false;
        return true;
    }

    /**
     * 若用户勾选了始终拒绝授予权限则可以用该方法判断是否依旧有权限
     * @param permissionsList 需要判断的权限列表
     * @return 若全部权限都有则true否则false
    **/
    public boolean isAlwaysNoPermission(String[] permissionsList){
        for(String S : permissionsList) return !(activity.shouldShowRequestPermissionRationale(S));
        return false;
    }

    public AlertDialog.Builder showAlertDialogPermission(String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener... event){
        return new AlertDialogUtils(activity).createAlertDialog(title,message,event[0],event[1],positiveButtonText,negativeButtonText);
    }

    public List<String> getPermissionList() {
        return permissionList;
    }
    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}