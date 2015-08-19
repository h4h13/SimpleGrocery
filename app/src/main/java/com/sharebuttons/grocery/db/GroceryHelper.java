package com.sharebuttons.grocery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Monkey D Luffy on 7/15/2015.
 */
public class GroceryHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "grocery.db";

    public static final int DB_VERSION = 8;

    //Grocery Table
    public static String TABLE_NAME = "grocery";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GROCERY = "grocery_items";
    public static String COLUMN_RATE = "rate";
    public static String COLUMN_COLOR = "color";


    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GROCERY + " TEXT, " +
                    COLUMN_COLOR + " TEXT, " +
                    COLUMN_RATE + " TEXT)";


    public static String CUSTOM_TABLE = "custom_table";
    private static final String CREATE_CUSTOM_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CUSTOM_TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GROCERY + " TEXT, " +
                    COLUMN_COLOR + " TEXT, " +
                    COLUMN_RATE + " TEXT)";


    public GroceryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOM_TABLE);
        Log.v("DATABASE", "upgrade");
        onCreate(db);
    }
}
