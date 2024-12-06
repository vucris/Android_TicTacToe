package com.branternser.pearlsandworkers0906;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PURCHASES = "purchases";
    public static final String COLUMN_RECEIPT_ID = "receipt_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_STATUS = "status";
    private static final String DATABASE_NAME = "receipts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + TABLE_PURCHASES
            + "("
            + COLUMN_RECEIPT_ID
            + " text primary key not null, "
            + COLUMN_USER_ID
            + " text not null, "
            + COLUMN_STATUS
            + " text not null "
            + ");";

    public SQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion
                + " to "
                + newVersion
        );
    }

}