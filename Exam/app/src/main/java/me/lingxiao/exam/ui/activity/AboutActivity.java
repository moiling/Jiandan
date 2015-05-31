package me.lingxiao.exam.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import me.lingxiao.exam.R;
import me.lingxiao.exam.util.MIUIV6;


public class AboutActivity extends MyActivity {

    private Toolbar mToolbar;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        MIUIV6.setStatusBarTextColor(this, 1);

        initToolbar();
        init();
    }

    private void init() {
        version = (TextView) findViewById(R.id.tv_version);

        //得到版本信息
        PackageManager pm = getApplicationContext().getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getApplicationContext().getPackageName(), 0);
            version.setText("Version " + pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("  " + getResources().getString(R.string.about));
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
}
