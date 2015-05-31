package me.lingxiao.exam.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import me.lingxiao.exam.R;


public class UpdateAPKUtils {
    private static ProgressDialog pd = null;
    private static String UPDATE_SERVERAPK = "Cyxbs.apk";
    private static Context mContent;

    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    pd.cancel();
                    update();
                    break;
            }
        }
    };

    /**
     * 更新版本
     */
    public static void doNewVersionUpdate(final String apkUrl, String verName, final Context content) {
        mContent = content;
        StringBuffer sb = new StringBuffer();
        sb.append(mContent.getResources().getString(R.string.find_new_version));
        sb.append(verName);
        sb.append(mContent.getResources().getString(R.string.if_update));
        AlertDialog.Builder builder = new AlertDialog.Builder(content);
        builder.setTitle(mContent.getResources().getString(R.string.update_app));
        builder.setMessage(sb.toString());
        builder.setPositiveButton(mContent.getResources().getString(R.string.update), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(content);
                pd.setTitle(mContent.getResources().getString(R.string.loading));
                pd.setMessage(mContent.getResources().getString(R.string.please_wait));
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                downFile(apkUrl);
            }
        }).setNegativeButton(mContent.getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 下载apk
     */
    public static void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        int count = 0;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public static void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                message.what = 2;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public static void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK))
                , "application/vnd.android.package-archive");
        mContent.startActivity(intent);
    }
}
