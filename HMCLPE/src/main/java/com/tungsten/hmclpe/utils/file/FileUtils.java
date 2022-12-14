package com.tungsten.hmclpe.utils.file;

import android.app.Activity;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

public class FileUtils {
    public static void createDirectory(String path){
        if (!new File(path).exists()){
            new File(path).mkdirs();
        }
    }

    public static void createFile(String path) throws IOException {
        if (!new File(path).exists()){
            new File(path).createNewFile();
        }
    }

    public static boolean rename(String path,String newName){
        File file = new File(path);
        String newPath = path.substring(0,path.lastIndexOf("/") + 1) + newName;
        File newFile = new File(newPath);
        return file.renameTo(newFile);
    }

    public static boolean copyDirectory(String srcPath, String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        if (!src.isDirectory()) {
            return false;
        }
        if (!dest.isDirectory() && !dest.mkdirs()) {
            return false;
        }
        File[] files = src.listFiles();
        for (File file : files) {
            File destFile = new File(dest, file.getName());
            if (file.isFile()) {
                if (!copyFile(file.getAbsolutePath(), destFile.getAbsolutePath())) {
                    return false;
                }
            } else if (file.isDirectory()) {
                if (!copyDirectory(file.getAbsolutePath(), destFile.getAbsolutePath())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean copyFile(String srcPath,String destPath){
        File src = new File(srcPath);
        File dest = new File(destPath);
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(src));
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(flush)) != -1){
                outputStream.write(flush,0,len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void copyFileWithUri(Uri uri, String destPath, Activity activity) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        OutputStream outputStream = new FileOutputStream(new File(destPath));
        byte[] flush = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(flush)) != -1) {
            outputStream.write(flush, 0, len);
        }
    }

    public static boolean deleteDirectory(String path){
        try{
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                return true;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return true;
            }
            File[] files = dirFile.listFiles();
            if(files == null){
                return false;
            }
            for (File file : files) {
                deleteDirectory(file.toString());
            }
            dirFile.delete();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public static ArrayList<File> getAllFiles(String path) {
        ArrayList<File> list = new ArrayList<>();
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return list;
        }
        if (dirFile.isFile()) {
            list.add(dirFile);
            return list;
        }
        File[] files = dirFile.listFiles();
        if(files == null){
            return list;
        }
        for (File file : files) {
            list.addAll(getAllFiles(file.toString()));
        }
        return list;
    }

    public static String getFileSha1(String path) {
        try {
            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            MessageDigest messagedigest;
            messagedigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) >0) {
                //????????????????????? update()??????????????????
                messagedigest.update(buffer, 0, len);
            }
            in.close();
            //????????????????????????????????????digest ??????????????????????????????????????? digest ?????????MessageDigest ??????????????????????????????????????????
            return bytesToHex(messagedigest.digest()).toLowerCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * ???????????????Hex
     * @param bytes ????????????
     * @return Hex
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < bytes.length; i++) {
                String hex = byteToHex(bytes[i]);
                sb.append(hex);
            }
        }
        return sb.toString();
    }
    /**
     * Byte?????????Hex
     * @param b ??????
     * @return Hex
     */
    private static String byteToHex(byte b) {
        String hexString = Integer.toHexString(b & 0xFF);
        //????????????????????????0~9???A~F?????????1~16???????????????Byte?????????Hex????????????<16,??????????????????????????????A=10??????????????????????????????????????????16????????????,
        //??????????????????????????????????????????11???????????????1??????????????????1???1??????????????????????????????????????????0?????????1???1???????????????0101???11??????????????????11
        if (hexString.length() < 2) {
            hexString = new StringBuilder(String.valueOf(0)).append(hexString).toString();
        }
        return hexString.toUpperCase();
    }
}
