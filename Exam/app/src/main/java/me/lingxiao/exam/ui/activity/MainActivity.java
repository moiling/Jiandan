package me.lingxiao.exam.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lingxiao.exam.R;
import me.lingxiao.exam.app.ExitApplication;
import me.lingxiao.exam.model.CyxbsInfo;
import me.lingxiao.exam.model.DrawerListItem;
import me.lingxiao.exam.ui.adapter.DrawerListAdapter;
import me.lingxiao.exam.ui.adapter.MyFragmentPagerAdapter;
import me.lingxiao.exam.ui.fragment.GirlsFragment;
import me.lingxiao.exam.ui.fragment.LotsFragment;
import me.lingxiao.exam.ui.fragment.StoriesFragment;
import me.lingxiao.exam.ui.listener.MyPopupOnTouchListner;
import me.lingxiao.exam.ui.widget.CircleImageView;
import me.lingxiao.exam.util.API;
import me.lingxiao.exam.util.EditKeyboardUtils;
import me.lingxiao.exam.util.GetImageUtils;
import me.lingxiao.exam.util.HttpCallbackListener;
import me.lingxiao.exam.util.HttpUtils;
import me.lingxiao.exam.util.MyXMLCallbackListener;
import me.lingxiao.exam.util.MyXMLUtils;
import me.lingxiao.exam.util.PicPopupWindowUtils;
import me.lingxiao.exam.util.UpdateAPKUtils;


public class MainActivity extends MyActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    public static void actionStart(Context context, String userName) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("user_name", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private int[][] colors = {{R.color.blue_primary_color, R.color.blue_primary_dark_color, R.color.blue_light_white, R.color.blue_accent_color},
            {R.color.blue_grey_primary_color, R.color.blue_grey_primary_dark_color, R.color.blue_grey_light_white, R.color.blue_grey_accent_color},
            {R.color.red_primary_color, R.color.red_primary_dark_color, R.color.red_light_white, R.color.red_accent_color}};
    private int colorChoose = 0;
    private int pageSelect = 0;
    /**
     * 主页面
     */
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private View drawSlip;
    private LinearLayout drawSlipBG;
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private String userName;
    /**
     * 抽屉
     */
    private DrawerLayout mDrawerLayout;
    private boolean isDrawerOpened;
    private ListView mListView;
    private DrawerListAdapter drawerListAdapter;
    private List<DrawerListItem> items = new ArrayList<DrawerListItem>();
    private CircleImageView userImage;
    private Uri imageUri;
    private TextView userNameTextView;
    private ImageView edit;
    private TextView talkText;
    private EditText talkEdit;
    private LinearLayout drawerBar;

    /**
     * popupwindow
     */
    private PopupWindow mPopupWindow;
    private TextView gallery;
    private TextView photo;

    private CyxbsInfo cyxbsInfo;
    private ProgressDialog pd = null;
    private String UPDATE_SERVERAPK = "Cyxbs.apk";
    private String verName = "";
    private String url;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    cyxbsInfo = (CyxbsInfo) msg.obj;
                    verName = cyxbsInfo.getVersionName();
                    url = cyxbsInfo.getApkURL();
                    UpdateAPKUtils.doNewVersionUpdate(url, verName, MainActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = getIntent().getStringExtra("user_name");

        initToolbar();        //设置toolbar
        init();        //主页面
        initLeftDrawer();        //抽屉
        initDrawToggle();       //toolbar图标
        initColor();
        initUserInfo();
        initPopupWindow();

        //第一页太吓人，还是初始化用第二页挡一挡
        mViewPager.setCurrentItem(1);
        pageSelect = 1;

    }

    private void initUserInfo() {
        //获取uri地址
        SharedPreferences sp = this.getSharedPreferences("data" + userName, Context.MODE_APPEND);
        imageUri = Uri.parse(sp.getString("image_uri", ""));
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
        String talk = sp.getString("talk", getResources().getString(R.string.default_text));
        talkText.setText(talk);
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

    private void initColor() {

        SharedPreferences sp = this.getSharedPreferences("data" + userName, Context.MODE_APPEND);
        colorChoose = sp.getInt("color", 0);
        mToolbar.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
        tab1.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
        tab2.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
        tab3.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
        tab1.setTextColor(getResources().getColor(colors[colorChoose][2]));
        tab2.setTextColor(getResources().getColor(colors[colorChoose][2]));
        tab3.setTextColor(getResources().getColor(colors[colorChoose][2]));
        switch (pageSelect) {
            case 0:
                tab1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                tab2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                tab3.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        drawerBar.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
        talkText.setTextColor(getResources().getColor(colors[colorChoose][2]));
        talkEdit.setTextColor(getResources().getColor(colors[colorChoose][3]));
        drawSlipBG.setBackgroundColor(getResources().getColor(colors[colorChoose][0]));
    }

    private void initLeftDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerBar = (LinearLayout) findViewById(R.id.ll_left_drawer_bar);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }

        });
        mListView = (ListView) findViewById(R.id.lv_drawer);
        items.add(new DrawerListItem(getResources().getString(R.string.details), R.mipmap.ic_account));
        items.add(new DrawerListItem(getResources().getString(R.string.settings), R.mipmap.ic_settings));
        items.add(new DrawerListItem(getResources().getString(R.string.about), R.mipmap.ic_about));
        items.add(new DrawerListItem(getResources().getString(R.string.exit), R.mipmap.ic_power));
        drawerListAdapter = new DrawerListAdapter(this, R.layout.item_drawer_list, items);
        mListView.setAdapter(drawerListAdapter);
        mListView.setOnItemClickListener(this);
        userImage = (CircleImageView) findViewById(R.id.iv_user_image);
        userImage.setOnClickListener(this);
        userNameTextView = (TextView) findViewById(R.id.tv_user_name);
        userNameTextView.setText(userName);

        edit = (ImageView) findViewById(R.id.iv_edit);
        edit.setOnClickListener(this);

        talkText = (TextView) findViewById(R.id.tv_talk);
        talkEdit = (EditText) findViewById(R.id.ed_talk);
        talkEdit.setOnKeyListener(onEditKey);
    }

    private void init() {
        tab1 = (TextView) findViewById(R.id.tv_tab_1);
        tab1.setOnClickListener(this);
        tab2 = (TextView) findViewById(R.id.tv_tab_2);
        tab2.setOnClickListener(this);
        tab3 = (TextView) findViewById(R.id.tv_tab_3);
        tab3.setOnClickListener(this);
        //设置滑条
        drawSlip = findViewById(R.id.draw_slip);
        drawSlipBG = (LinearLayout) findViewById(R.id.draw_slip_bg);
        drawSlip.setLayoutParams(
                new LinearLayout.LayoutParams((getScreenWidth(this) / 3) - 60, 6));
        //设置viewpager
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        fragmentList.add(new GirlsFragment());
        fragmentList.add(new StoriesFragment());
        fragmentList.add(new LotsFragment());
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                drawSlip.setX(((i + v) * getScreenWidth(getApplicationContext()) / 3) + 30);
            }

            @Override
            public void onPageSelected(int position) {
                pageSelect = position;
                switch (position) {
                    case 0:
                        tab1.setTextColor(getResources().getColor(R.color.white));
                        tab2.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        tab3.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        break;
                    case 1:
                        tab1.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        tab2.setTextColor(getResources().getColor(R.color.white));
                        tab3.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        break;
                    case 2:
                        tab1.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        tab2.setTextColor(getResources().getColor(colors[colorChoose][2]));
                        tab3.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setLogo(R.mipmap.ic_title);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void initDrawToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 主页面
             */
            case R.id.tv_tab_1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab_2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_tab_3:
                mViewPager.setCurrentItem(2);
                break;
            /**
             * 左抽屉
             */
            case R.id.iv_user_image:
                mPopupWindow.showAsDropDown(v);
                break;
            case R.id.iv_edit:
                if (talkText.getVisibility() == View.VISIBLE) {
                    String talk = talkText.getText().toString();
                    talkText.setVisibility(View.GONE);
                    talkEdit.setText(talk);
                    talkEdit.setSelection(talk.length());
                    talkEdit.setVisibility(View.VISIBLE);
                    edit.setColorFilter(getResources().getColor(colors[colorChoose][3]), PorterDuff.Mode.SRC_IN);
                    EditKeyboardUtils.showSoftInput(talkEdit);
                } else {
                    editTalkFinish();
                }
                break;
        }
    }

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
        edit.clearColorFilter();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_drawer:
                switch (position) {
                    case 0:
                        //个人信息
                        Intent detailsIntent = new Intent(this, DetailsActivity.class);
                        detailsIntent.putExtra("user_name", userName);
                        startActivityForResult(detailsIntent, 3);
                        break;
                    case 1:
                        //设置
                        Intent settingsIntent = new Intent(this, SettingsActivity.class);
                        settingsIntent.putExtra("user_name", userName);
                        startActivityForResult(settingsIntent, 0);
                        break;
                    case 2:
                        //关于
                        Intent aboutIntent = new Intent(this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case 3:
                        //退出
                        ExitApplication.getInstance().exit();
                        break;
                }
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

    /**
     * toolbar右边按钮的点击事件
     */
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_refresh:
                    HttpUtils.sendHttpRequestNoChange(API.cyxbsUpdate, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            MyXMLUtils.parseXMLWithPull(response, new MyXMLCallbackListener() {
                                @Override
                                public void onFinish(CyxbsInfo cyxbsInfo) {
                                    Message message = new Message();
                                    message.what = 1;
                                    message.obj = cyxbsInfo;
                                    handler.sendMessage(message);
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                        }

                        @Override
                        public void onFinish(Bitmap bitmap) {
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
                    break;
            }
            return true;
        }
    };


    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isDrawerOpened = mDrawerLayout.isDrawerOpen(Gravity.START);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                initColor();
                break;
            case 1:
                GetImageUtils.cropPhoto(this, requestCode, resultCode, userImage, imageUri, data);
                break;
            case 2:
                GetImageUtils.cropPhoto(this, requestCode, resultCode, userImage, imageUri, data);
                break;
            case 3:
                initUserInfo();
                break;
        }
    }
}
