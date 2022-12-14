package com.tungsten.hmclpe.utils.file;

import android.content.Context;
import android.os.*;
import android.util.Log;
import java.io.*;

public class AssetsUtils {

    private static AssetsUtils instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private ProgressCallback progressCallback;
    private volatile boolean isSuccess;
    private String errorStr;

    public static AssetsUtils getInstance(Context context) {
        if (instance == null)
            instance = new AssetsUtils(context);
        return instance;
    }

    private AssetsUtils(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public static String readAssetsTxt(Context context,String fileName){
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");
            Log.e("latest",text);
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AssetsUtils copyAssetsToSD(final String srcPath, final String sdPath) {
        currentPosition = 0;
        try {
            totalSize = getTotalSize(context,srcPath);
            Log.e("assets文件大小",Long.toString(totalSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            copyAssetsFilesToPhone(context, srcPath, sdPath);
            if (isSuccess)
                handler.obtainMessage(SUCCESS).sendToTarget();
            else
                handler.obtainMessage(FAILED, errorStr).sendToTarget();
        }).start();
        return this;
    }

    public AssetsUtils copyOnMainThread(final String srcPath, final String sdPath) {
        currentPosition = 0;
        try {
            totalSize = getTotalSize(context,srcPath);
            Log.e("assets文件大小",Long.toString(totalSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
        copyAssetsFilesToPhone(context, srcPath, sdPath);
        if (isSuccess) {
            handler.obtainMessage(SUCCESS).sendToTarget();
        }
        else {
            handler.obtainMessage(FAILED, errorStr).sendToTarget();
        }
        return this;
    }

    public AssetsUtils setProgressCallback(ProgressCallback callback) {
        this.progressCallback = callback;
        return instance;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    long currentPosition = 0;
    long totalSize = 0;

    int currentProgress = 0;

    private long getTotalSize(Context context,String srcPath) throws IOException {
        String fileNames[] = context.getAssets().list(srcPath);
        long size = 0;
        if (fileNames.length > 0) {
            for (String fileName : fileNames) {
                if (!srcPath.equals("")) { // assets 文件夹下的目录
                    size += getTotalSize(context, srcPath + File.separator + fileName);
                } else { // assets 文件夹
                    size += getTotalSize(context, fileName);
                }
            }
        } else {
            InputStream is = context.getAssets().open(srcPath);
            size += is.available();
        }
        return size;
    }

    private void copyAssetsFilesToPhone(Context context, String assetsPath, String savePath){
        try {
            String[] fileNames = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyAssetsFilesToPhone(context, assetsPath + "/" + fileName, savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    currentPosition += byteCount;
                    fos.write(buffer, 0, byteCount);
                    if (progressCallback != null) {
                        long cur = 100L * currentPosition;
                        int progress = (int) (cur / totalSize);
                        if (progress != currentProgress) {
                            currentProgress = progress;
                            handler.post(() -> {
                                progressCallback.onProgress(progress);
                            });
                        }
                    }
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface FileOperateCallback {
        void onSuccess();
        void onFailed(String error);
    }

    public interface ProgressCallback{
        void onProgress(int progress);
    }

}
