package me.lingxiao.exam.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestNoChange(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestFotPic(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpGet httpget = new HttpGet(address);

                try {
                    HttpResponse resp = httpclient.execute(httpget);
                    //判断是否正确执行
                    if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
                        //将返回内容转换为bitmap
                        HttpEntity entity = resp.getEntity();
                        InputStream in = entity.getContent();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inSampleSize = 3;
                        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                        if (listener != null) {
                            listener.onFinish(bitmap);
                        }
                        in.close();
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    httpclient.getConnectionManager().shutdown();
                }
            }
        }).start();
    }

    public static void sendHttpRequestFotBigPic(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpGet httpget = new HttpGet(address);

                try {
                    HttpResponse resp = httpclient.execute(httpget);
                    //判断是否正确执行
                    if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
                        //将返回内容转换为bitmap
                        HttpEntity entity = resp.getEntity();
                        InputStream in = entity.getContent();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        if (listener != null) {
                            listener.onFinish(bitmap);
                        }
                        in.close();
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    httpclient.getConnectionManager().shutdown();
                }
            }
        }).start();
    }
}
