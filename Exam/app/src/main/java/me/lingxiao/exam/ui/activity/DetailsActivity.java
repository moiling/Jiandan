package me.lingxiao.exam.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.lingxiao.exam.R;
import me.lingxiao.exam.app.ExitApplication;
import me.lingxiao.exam.ui.listener.MyPopupOnTouchListner;
import me.lingxiao.exam.ui.widget.CircleImageView;
import me.lingxiao.exam.util.EditKeyboardUtils;
import me.lingxiao.exam.util.GetImageUtils;
import me.lingxiao.exam.util.MIUIV6;
import me.lingxiao.exam.util.PicPopupWindowUtils;


public class DetailsActivity extends MyActivity implements View.OnClickListener, View.OnTouchListener {

    private Toolbar mToolbar;
    private CardView logout;
    private CircleImageView userImage;
    private Uri imageUri;
    private String userName;
    private TextView talkText;
    private TextView userNameText;
    private EditText talkEdit;
    /**
     * popupwindow
     */
    private PopupWindow mPopupWindow;
    private TextView gallery;
    private TextView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deltails);
        userName = getIntent().getStringExtra("user_name");
        MIUIV6.setStatusBarTextColor(this, 1);
        initToolbar();
        init();
        initPopupWindow();
    }

    private void initPopupWindow() {
        mPopupWindow = PicPopupWindowUtils.getPopouWiondow(this);
        gallery = (TextView) mPopupWindow.getContentView().findViewById(R.id.tv_gallery);
        gallery.setOnTouchListener(new MyPopupOnTouchListner());
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = GetImageUtils.getImageUri(userName + "'s image");
                //保存uri
                SharedPreferences.Editor editor = getSharedPreferences("data" + userName, MODE_PRIVATE).edit();
                editor.putString("image_uri", imageUri.toString());
                editor.apply();
                GetImageUtils.chooseFromAlbum((android.app.Activity) v.getContext(), imageUri);
            }
        });
        photo = (TextView) mPopupWindow.getContentView().findViewById(R.id.tv_photo);
        photo.setOnTouchListener(new MyPopupOnTouchListner());
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = GetImageUtils.getImageUri(userName + "'s image");
                //保存uri
                SharedPreferences.Editor editor = getSharedPreferences("data" + userName, MODE_PRIVATE).edit();
                editor.putString("image_uri", imageUri.toString());
                editor.apply();
                GetImageUtils.takePhoto((android.app.Activity) v.getContext(), imageUri);
            }
        });
    }

    private void init() {
        logout = (CardView) findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);
        logout.setOnTouchListener(this);

        //获取uri地址
        SharedPreferences sp = this.getSharedPreferences("data" + userName, Context.MODE_APPEND);
        imageUri = Uri.parse(sp.getString("image_uri", ""));
        userImage = (CircleImageView) findViewById(R.id.iv_user_image);
        userImage.setOnClickListener(this);
        //将保存的头像显示出来
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(imageUri), null, options);

            if (bitmap != null) {
                userImage.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userNameText = (TextView) findViewById(R.id.tv_user_name);
        userNameText.setText(userName);
        talkText = (TextView) findViewById(R.id.tv_talk);
        String talk = sp.getString("talk", "Type something here");
        talkText.setText(talk);
        talkText.setOnClickListener(this);
        talkEdit = (EditText) findViewById(R.id.ed_talk);
        talkEdit.setOnKeyListener(onEditKey);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("  " + getResources().getString(R.string.details));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        if (mToolbar.getNavigationIcon() != null) {
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.primary_text), PorterDuff.Mode.SRC_IN);
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_APPEND);
                sp.edit().putBoolean("AUTOISCHECK", false).apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ExitApplication.getInstance().exit();
                break;
            case R.id.tv_talk:
                String talk = talkText.getText().toString();
                talkText.setVisibility(View.GONE);
                talkEdit.setText(talk);
                talkEdit.setSelection(talk.length());
                talkEdit.setVisibility(View.VISIBLE);
                EditKeyboardUtils.showSoftInput(talkEdit);
                break;
            case R.id.iv_user_image:
                mPopupWindow.showAsDropDown(v);
                break;
        }
    }

    private View.OnKeyListener onEditKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                editTalkFinish();
            }
            return false;
        }
    };

    private void editTalkFinish() {
        String talk = talkEdit.getText().toString();
        //保存信息
        SharedPreferences.Editor editor = getSharedPreferences("data" + userName, MODE_PRIVATE).edit();
        editor.putString("talk", talk);
        editor.apply();

        talkEdit.setVisibility(View.GONE);
        EditKeyboardUtils.hideSoftInput(talkEdit);
        talkText.setText(talk);
        talkText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    logout.setCardBackgroundColor(getResources().getColor(R.color.my_dark_red));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    logout.setCardBackgroundColor(getResources().getColor(R.color.my_red));
                }
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2) {
            GetImageUtils.cropPhoto(this, requestCode, resultCode, userImage, imageUri, data);
        }
    }
}
