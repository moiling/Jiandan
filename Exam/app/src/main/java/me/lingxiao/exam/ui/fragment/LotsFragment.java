package me.lingxiao.exam.ui.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import me.lingxiao.exam.R;
import me.lingxiao.exam.db.DatabaseHelper;
import me.lingxiao.exam.ui.activity.LotsDetailsActivity;
import me.lingxiao.exam.ui.adapter.MyArrayAdapter;
import me.lingxiao.exam.util.EditKeyboardUtils;


public class LotsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    private ListView mListView;
    private List<String> items = new ArrayList<String>();
    private MyArrayAdapter adapter;
    private TextView textViewHint;
    private String lotName;
    private LinearLayout mLayout;
    private EditText mEditText;
    private CardView createButton;
    private CardView cancelButton;
    private ImageButton buttonFloat;
    private View click;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private List<Integer> idList = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建数据库
        dbHelper = new DatabaseHelper(getActivity(), "LotsStore.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();

        //绑定list
        Cursor cursor = db.query("lots", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("lots_name"));
                items.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lots, container, false);


        init(view);
        return view;
    }

    private void init(View v) {
        mListView = (ListView) v.findViewById(R.id.lv_lots);
        adapter = new MyArrayAdapter(v.getContext(), R.layout.item_lots_list, items);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        textViewHint = (TextView) v.findViewById(R.id.tv_hint_add_table);
        showHint();

        buttonFloat = (ImageButton) v.findViewById(R.id.btn_float);
        buttonFloat.setOnClickListener(this);

        mLayout = (LinearLayout) v.findViewById(R.id.layout_add_lots);
        mLayout.setVisibility(View.GONE);
        mEditText = (EditText) v.findViewById(R.id.ed_add_lots);
        createButton = (CardView) v.findViewById(R.id.btn_create);
        cancelButton = (CardView) v.findViewById(R.id.btn_cancel);
        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        click = v.findViewById(R.id.click);
        click.setOnClickListener(this);
    }

    private void showHint() {
        if (items.isEmpty()) {
            textViewHint.setVisibility(View.VISIBLE);
        } else {
            textViewHint.setVisibility(View.GONE);
        }
    }
    private void clearTables() {
        items.clear();
        db.execSQL("DELETE FROM lotsDetail");
        db.execSQL("DELETE FROM lots");
        showHint();
        adapter.notifyDataSetChanged();
    }

    private void createTable() {
        lotName = mEditText.getText().toString();

        //数据库
        values.clear();
        values.put("lots_name", lotName);
        db.insert("lots", null, values);

        items.add(lotName);
        adapter.notifyDataSetChanged();
        mLayout.setVisibility(View.GONE);
        mEditText.setText("");
        EditKeyboardUtils.hideSoftInput(mEditText);
        showHint();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                createTable();
                break;
            case R.id.btn_cancel:
                mLayout.setVisibility(View.GONE);
                showHint();
                EditKeyboardUtils.hideSoftInput(mEditText);
                break;
            case R.id.btn_float:
                mLayout.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                EditKeyboardUtils.showSoftInput(mEditText);
                break;
            case R.id.click:
                mLayout.setVisibility(View.GONE);
                EditKeyboardUtils.hideSoftInput(mEditText);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = items.get(position);
        Cursor cursor = db.query("lots", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int tableId = cursor.getInt(cursor.getColumnIndex("id"));
                idList.add(tableId);
            } while (cursor.moveToNext());
        }
        LotsDetailsActivity.actionStart(view.getContext(), name, idList.get(position));
        // 不清空数据全乱了
        idList.clear();
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

                        Cursor cursor = db.query("lots", null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                int id = cursor.getInt(cursor.getColumnIndex("id"));
                                idList.add(id);
                            } while (cursor.moveToNext());
                        }

                        db.delete("lots", "id = ?", new String[] { idList.get(position) + "" });

                        db.delete("lotsDetail", "tableId = ?", new String[] { idList.get(position) + "" });
                        idList.clear();

                    }
                }).start();
                items.remove(position);
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
}
