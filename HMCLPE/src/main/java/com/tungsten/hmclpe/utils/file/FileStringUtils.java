package com.tungsten.hmclpe.utils.file;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStringUtils {
    public static String getStringFromFile(String path){
        StringBuilder content = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = reader.readLine()) != null) content.append(line).append("\n");
        }catch (IOException e){
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void writeFile(String path,String string){
        try {
            String parent = new File(path).getParent();
            FileUtils.createDirectory(parent);
            FileUtils.createFile(path);
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(string);
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("writeFile",e.toString());
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
