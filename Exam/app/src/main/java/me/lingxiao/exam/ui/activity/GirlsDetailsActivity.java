package me.lingxiao.exam.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;

import me.lingxiao.exam.R;
import me.lingxiao.exam.ui.listener.MyPopupOnTouchListner;
import me.lingxiao.exam.util.HttpCallbackListener;
import me.lingxiao.exam.util.HttpUtils;
import me.lingxiao.exam.util.LogUtils;
import me.lingxiao.exam.util.MIUIV6;
import me.lingxiao.exam.util.PicPopupWindowUtils;
import me.lingxiao.exam.util.SavePicUtils;

public class GirlsDetailsActivity extends MyActivity implements View.OnLongClickListener {

    public static void actionStart(Context context, String url, int id) {
        Intent intent = new Intent(context, GirlsDetailsActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private ImageView mImageView;
    private Bitmap bitmap;
    private String url;
    private int id;
    /**
     * popupwindow
     */
    private PopupWindow mPopupWindow;
    private TextView save;
    private TextView share;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    bitmap = (Bitmap) msg.obj;
                    mImageView.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girls_details);
        url = getIntent().getStringExtra("url");
        id = getIntent().getIntExtra("id", 0);
        MIUIV6.setStatusBarTextColor(this, 1);
        downloadBigPic();
        init();
        initPopupWindow();
    }

    /**
     * 这个时候只加载一个图片就不压缩了！可以保存原图！
     */
    private void downloadBigPic() {
        HttpUtils.sendHttpRequestFotBigPic(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
            }

            @Override
            public void onFinish(Bitmap bitmap) {
                LogUtils.d("TAG", "得到了bitmap");
                Message message = new Message();
                message.what = 1;
                message.obj = bitmap;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void initPopupWindow() {
        mPopupWindow = PicPopupWindowUtils.getPopouWiondow(this);
        save = (TextView) mPopupWindow.getContentView().findViewById(R.id.tv_gallery);
        save.setText(getResources().getString(R.string.save));
        save.setOnTouchListener(new MyPopupOnTouchListner());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePicUtils.saveGirlsPic(id, bitmap, v);
            }
        });

        share = (TextView) mPopupWindow.getContentView().findViewById(R.id.tv_photo);
        share.setText(getResources().getString(R.string.share));
        share.setOnTouchListener(new MyPopupOnTouchListner());
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = SavePicUtils.getFile(id, bitmap);
                Uri u = Uri.fromFile(file);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, u);
                shareIntent.setType("image/png");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        });
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.iv_pic);
        mImageView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        mPopupWindow.showAsDropDown(v, getScreenWidth(v.getContext()) / 2, -getScreenHeight(v.getContext()) / 2);
        return true;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
