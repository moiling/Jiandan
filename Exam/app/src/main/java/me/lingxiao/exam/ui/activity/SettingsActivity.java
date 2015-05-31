package me.lingxiao.exam.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import me.lingxiao.exam.R;
import me.lingxiao.exam.util.MIUIV6;

public class SettingsActivity extends MyActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private int color;
    private String userName;
    private CardView colorSelectCardView;
    private TextView colorSelect;
    private CardView blue;
    private CardView blueGrey;
    private CardView red;
    private ImageView blueCheck;
    private ImageView blueGreyCheck;
    private ImageView redCheck;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MIUIV6.setStatusBarTextColor(this, 1);

        userName = getIntent().getStringExtra("user_name");
        SharedPreferences sp = this.getSharedPreferences("data" + userName, Context.MODE_APPEND);
        color = sp.getInt("color", 0);

        initToolbar();

        init();

        initColor();
        Animation shake = AnimationUtils.loadAnimation(colorSelectCardView.getContext(), R.anim.fade_in);
        colorSelectCardView.startAnimation(shake);

    }

    private void initColor() {
        switch (color) {
            case 0:
                colorSelect.setTextColor(getResources().getColor(R.color.blue_primary_color));
                blueCheck.setImageResource(R.mipmap.ic_check);
                blueGreyCheck.setImageDrawable(null);
                redCheck.setImageDrawable(null);
                break;
            case 1:
                colorSelect.setTextColor(getResources().getColor(R.color.blue_grey_primary_color));
                blueCheck.setImageDrawable(null);
                blueGreyCheck.setImageResource((R.mipmap.ic_check));
                redCheck.setImageDrawable(null);
                break;
            case 2:
                colorSelect.setTextColor(getResources().getColor(R.color.red_primary_color));
                blueCheck.setImageDrawable(null);
                blueGreyCheck.setImageDrawable(null);
                redCheck.setImageResource((R.mipmap.ic_check));
                break;
        }
    }

    private void init() {
        colorSelectCardView = (CardView) findViewById(R.id.cv_color_select);
        colorSelect = (TextView) findViewById(R.id.tv_color_select);
        blue = (CardView) findViewById(R.id.btn_color_blue);
        blue.setOnClickListener(this);
        blueGrey = (CardView) findViewById(R.id.btn_blue_grey_blue);
        blueGrey.setOnClickListener(this);
        red = (CardView) findViewById(R.id.btn_color_red);
        red.setOnClickListener(this);
        blueCheck = (ImageView) findViewById(R.id.iv_blue);
        blueGreyCheck = (ImageView) findViewById(R.id.iv_grey_blue);
        redCheck = (ImageView) findViewById(R.id.iv_red);

        editor = getSharedPreferences("data" + userName, MODE_PRIVATE).edit();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("  " + getResources().getString(R.string.settings));
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
            case R.id.btn_color_blue:
                color = 0;
                initColor();
                editor.putInt("color", color);
                editor.apply();
                break;
            case R.id.btn_blue_grey_blue:
                color = 1;
                initColor();
                editor.putInt("color", color);
                editor.apply();
                break;
            case R.id.btn_color_red:
                color = 2;
                initColor();
                editor.putInt("color", color);
                editor.apply();
                break;
        }
    }
}
