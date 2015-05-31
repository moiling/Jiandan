package me.lingxiao.exam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_LOTS = "create table lots ("
            + "id integer primary key autoincrement, "
            + "lots_name text)";

    public static final String CREATE_LOTS_DETAIL = "create table lotsDetail ("
            + "id integer primary key autoincrement, "
            + "tableId integer, "
            + "lots_detail_name text)";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOTS);
        db.execSQL(CREATE_LOTS_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
