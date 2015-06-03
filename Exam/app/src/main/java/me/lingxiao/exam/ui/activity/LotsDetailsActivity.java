package me.lingxiao.exam.ui.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.lingxiao.exam.R;
import me.lingxiao.exam.db.DatabaseHelper;
import me.lingxiao.exam.ui.adapter.MyArrayAdapter;
import me.lingxiao.exam.util.EditKeyboardUtils;

public class LotsDetailsActivity extends MyActivity implements View.OnClickListener,AdapterView.OnItemLongClickListener {

    public static void actionStart(Context context, String tableName, int tableId) {
        Intent intent = new Intent(context, LotsDetailsActivity.class);
        intent.putExtra("table_name", tableName);
        intent.putExtra("table_id", tableId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private Random random = new Random();
    private Toolbar mToolbar;
    private int tableId;
    private String tableName;
    private ListView mListView;
    private MyArrayAdapter adapter;
    private List<String> itemList = new ArrayList<String>();
    private ImageButton addButton;
    private LinearLayout createTableLayout;
    private EditText mEditText;
    private CardView createButton;
    private CardView cancelButton;
    private String memberName;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private List<Integer> idList = new ArrayList<Integer>();
    private TextView textViewHint;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lots_details);

        tableId = getIntent().getIntExtra("table_id", 0);
        tableName = getIntent().getStringExtra("table_name");
        //创建数据库
        dbHelper = new DatabaseHelper(this, "LotsStore.db", null, 1);
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();

        //绑定list
        Cursor cursor = db.rawQuery("select * from lotsDetail where tableId=?",
                new String[]{tableId + ""});
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("lots_detail_name"));
                itemList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        init();
        initToolbar();

        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor s = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(listener, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void init() {
        textViewHint = (TextView) findViewById(R.id.tv_hint_add_table);
        showHint();
        mListView = (ListView) findViewById(R.id.lv_lots);
        adapter = new MyArrayAdapter(this, R.layout.item_lots_list, itemList);
        mListView.setAdapter(adapter);
        mListView.setOnItemLongClickListener(this);

        addButton = (ImageButton) findViewById(R.id.btn_float);
        addButton.setOnClickListener(this);

        createTableLayout = (LinearLayout) findViewById(R.id.layout_add_lots);
        createTableLayout.setVisibility(View.GONE);
        mEditText = (EditText) findViewById(R.id.ed_add_lots);

        createButton = (CardView) findViewById(R.id.btn_create);
        cancelButton = (CardView) findViewById(R.id.btn_cancel);
        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        show = (TextView) findViewById(R.id.tv_show);
        show.setOnClickListener(this);

    }

    private void showHint() {
        if (itemList.isEmpty()) {
            textViewHint.setVisibility(View.VISIBLE);
        } else {
            textViewHint.setVisibility(View.GONE);
        }
    }

    private void createMember() {
        memberName = mEditText.getText().toString();
        db = dbHelper.getWritableDatabase();
        //数据库
        values.clear();
        values.put("lots_detail_name", memberName);
        values.put("tableId", tableId);
        db.insert("lotsDetail", null, values);
        db.close();
        itemList.add(memberName);
        adapter.notifyDataSetChanged();

        createTableLayout.setVisibility(View.GONE);
        mEditText.setText("");
        EditKeyboardUtils.hideSoftInput(mEditText);
        showHint();
    }

    private void clearMembers() {
        itemList.clear();
        // 清空数据库
        db = dbHelper.getWritableDatabase();
        db.delete("lotsDetail", "tableId = ?", new String[] { tableId + "" });
        db.close();
        showHint();
        adapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("  " + tableName);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        if (mToolbar.getNavigationIcon() != null) {
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
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
            case R.id.btn_create:
                createMember();
                break;
            case R.id.btn_cancel:
                createTableLayout.setVisibility(View.GONE);
                showHint();
                EditKeyboardUtils.hideSoftInput(mEditText);
                break;
            case R.id.btn_float:
                createTableLayout.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                EditKeyboardUtils.showSoftInput(mEditText);
                break;
            case R.id.tv_show:
                show.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
        view.startAnimation(shake);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.delete));
        builder.setMessage(getResources().getString(R.string.if_delete));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from lotsDetail where tableId=?",
                                new String[] { tableId + "" });
                        if (cursor.moveToFirst()) {
                            do {
                                int id = cursor.getInt(cursor.getColumnIndex("id"));
                                idList.add(id);
                            } while (cursor.moveToNext());
                        }

                        db.delete("lotsDetail", "id = ?", new String[] { idList.get(position) + "" });
                        db.close();
                        idList.clear();

                    }
                }).start();
                itemList.remove(position);
                adapter.notifyDataSetChanged();
                showHint();
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xValue = Math.abs(event.values[0]);
            float yValue = Math.abs(event.values[1]);
            float zValue = Math.abs(event.values[2]);
            if (xValue > 30 || yValue > 30 || zValue > 30) {
                if (itemList.size() != 0) {
                    if (show.getVisibility() == View.GONE) {
                        int index = random.nextInt(itemList.size());
                        show.setVisibility(View.VISIBLE);
                        show.setText(itemList.get(index));
                        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(new long[] {100, 200, 100, 400}, -1);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
