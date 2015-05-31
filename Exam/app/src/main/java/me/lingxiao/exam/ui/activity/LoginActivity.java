package me.lingxiao.exam.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.lingxiao.exam.R;
import me.lingxiao.exam.ui.widget.TopSlidingLayout;
import me.lingxiao.exam.util.EditKeyboardUtils;
import me.lingxiao.exam.util.MIUIV6;

public class LoginActivity extends MyActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    private AppCompatEditText userNameEdit;
    private AppCompatEditText passwordEdit;
    private String userName;
    private String password;
    private CardView login;
    private TextView loginText;
    private ImageButton go;
    private Button mButton;
    private TopSlidingLayout mTopSlidingLayout;
    private CheckBox rememberMe;
    private CheckBox autoLogin;
    private RelativeLayout menuLayout;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MIUIV6.setStatusBarTextColor(this, 1);
        init();

        //用来记住密码
        sp = this.getSharedPreferences("userInfo", Context.MODE_APPEND);
        //判断自动登陆选择框的状态
        if (sp.getBoolean("AUTOISCHECK", false)) {

            autoLogin.setChecked(true);
            rememberMe.setChecked(true);
            String name = sp.getString("USER_NAME", "");
            MainActivity.actionStart(getApplicationContext(), name);
        }
        //判断记住密码选择框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记录密码状态
            rememberMe.setChecked(true);
            userNameEdit.setText(sp.getString("USER_NAME", ""));
            passwordEdit.setText(sp.getString("PASSWORD", ""));
            if (sp.getBoolean("ISCHECK", true)) {
                //让记住密码，再次登陆时变色……
                loginText.setTextColor(getResources().getColor(R.color.white));
            }
        }

        Animation shake = AnimationUtils.loadAnimation(go.getContext(), R.anim.shake);
        go.startAnimation(shake);
    }

    private void init() {
        menuLayout = (RelativeLayout) findViewById(R.id.top_menu);

        rememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
        autoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
        autoLogin.setOnCheckedChangeListener(this);
        rememberMe.setOnCheckedChangeListener(this);

        //Go跳转按钮
        go = (ImageButton) findViewById(R.id.iv_go);
        go.setOnClickListener(this);
        //为了获取到下滑的滑动事件实例化
        mButton = (Button) findViewById(R.id.btn_content);

        userNameEdit = (AppCompatEditText) findViewById(R.id.ed_user_name);

        passwordEdit = (AppCompatEditText) findViewById(R.id.ed_password);
        userNameEdit.addTextChangedListener(new MyTextWatcher());
        passwordEdit.addTextChangedListener(new MyTextWatcher());
        passwordEdit.setOnKeyListener(onPasswordKey);

        login = (CardView) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        login.setOnTouchListener(this);
        loginText = (TextView) findViewById(R.id.tv_login);

        //下滑登陆菜单
        mTopSlidingLayout = (TopSlidingLayout) findViewById(R.id.top_sliding_layout);
        mTopSlidingLayout.setScrollEvent(go);
        mTopSlidingLayout.setScrollEvent(mButton);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    login.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_color));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    login.setCardBackgroundColor(getResources().getColor(R.color.primary_color));
                }
                break;
        }
        return false;
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!userNameEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString()
                    .isEmpty()) {
                loginText.setTextColor(getResources().getColor(R.color.white));
            } else {
                loginText.setTextColor(getResources().getColor(R.color.light_white));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginEvent();
                break;

            case R.id.iv_go:
                if (mTopSlidingLayout.isTopLayoutVisible()) {
                    mTopSlidingLayout.scrollToContentFromTopMenu();
                } else {
                    showTopSlidingMenu();
                }
                break;
        }
    }

    private void loginEvent() {
        userName = userNameEdit.getText().toString();
        password = passwordEdit.getText().toString();
        /**
         *从数据库中获得该学号学生的密码、判断是否相等
         *//*
                int tempPW = 0;
                dbHelper = new MyDataBaseHelper(getApplicationContext(), "StudentManager.db", null, 1);
                dbHelper.getWritableDatabase();
                db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from Student where studentId=?",
                        new String[] {text});
                if (cursor.moveToFirst()) {
                    do {
                        tempPW = cursor.getInt(cursor.getColumnIndex("password"));
                    } while (cursor.moveToNext());
                }
                cursor.close();*/
        if (!password.isEmpty() && !userName.isEmpty()) {
            //登录成功和记住密码框为选中状态才保存用户信息
            if (rememberMe.isChecked()) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", password);
                editor.apply();
            }
            MainActivity.actionStart(getApplicationContext(), userName);
        } else if (userName.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_enter_username), Toast.LENGTH_SHORT)
                    .show();
        } else if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.username_or_password_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void showTopSlidingMenu() {
        mTopSlidingLayout.initShowTopState();
        mTopSlidingLayout.scrollToTopMenu();

        Animation translateAnimation = new TranslateAnimation(0, 0, 800, 0);
        translateAnimation.setDuration(800);
        translateAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        menuLayout.startAnimation(translateAnimation);

        //让其跳转后焦点转移到editText
        if (userNameEdit.getText().toString().equals("")) {
            EditKeyboardUtils.showSoftInput(userNameEdit);
        } else if (passwordEdit.getText().toString().equals("")) {
            EditKeyboardUtils.showSoftInput(passwordEdit);
        } else {
            //我就是要弹输入法！你管我！
            EditKeyboardUtils.showSoftInput(passwordEdit);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_auto_login:
                if (autoLogin.isChecked()) {
                    sp.edit().putBoolean("AUTOISCHECK", true).apply();
                    rememberMe.setChecked(true);
                } else {
                    sp.edit().putBoolean("AUTOISCHECK", false).apply();
                }
                break;
            case R.id.cb_remember_me:
                if (rememberMe.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).apply();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).apply();
                    sp.edit().putBoolean("AUTOISCHECK", false).apply();
                    autoLogin.setChecked(false);
                }
                break;
        }
    }

    private View.OnKeyListener onPasswordKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                loginEvent();
            }
            return false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
