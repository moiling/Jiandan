package me.lingxiao.exam.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class SavePicUtils {
    public static void saveGirlsPic(int id, Bitmap bitmap, View v) {

        String path;
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/MYJANDAN");
        if (!file.exists())
            file.mkdirs();
        try {
            path = file.getPath() + "/image" + id + ".png";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(v.getContext(), "图片保存路径：" + path, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFile(int id, Bitmap bitmap) {
        String path = null;
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/MYJANDAN");
        if (!file.exists())
            file.mkdirs();
        try {
            path = file.getPath() + "/image" + id + ".png";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(path);
    }

    /**
     * 喂猫这个方法在小米上可以，在别的机子上都不行！！！！！！！！！！！
     * 神了个奇！喂猫把路径分开写就可以了啊！！！！！！！！
     */
    /*File outputImage = new File(Environment.getExternalStorageDirectory(), "MYJANDAN/image" + id + ".png");
    try {
        if (outputImage.exists()) {
            outputImage.delete();
        }
        outputImage.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        FileOutputStream out = new FileOutputStream(outputImage);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }*/
}
