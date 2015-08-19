package com.sharebuttons.grocery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Monkey D Luffy on 7/15/2015.
 */
public class GroceryDataSource {
    private GroceryHelper mGroceryHelper;
    SQLiteDatabase database;

    public GroceryDataSource(Context context) {
        mGroceryHelper = new GroceryHelper(context);
    }

    public void open() {
        database = mGroceryHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long insert(String item, String total, String color) {
        database.beginTransaction();
        long statusId;
        try {
            ContentValues values = new ContentValues();
            values.put(GroceryHelper.COLUMN_GROCERY, item);
            values.put(GroceryHelper.COLUMN_RATE, total);
            values.put(GroceryHelper.COLUMN_COLOR, color);

            statusId = database.insert(GroceryHelper.TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return statusId;
    }

    public Cursor read() {
        return database.query(
                GroceryHelper.TABLE_NAME,//table
                new String[]{GroceryHelper.COLUMN_ID, GroceryHelper.COLUMN_GROCERY, GroceryHelper.COLUMN_RATE, GroceryHelper.COLUMN_COLOR},//columns names
                null,//where
                null,//params
                null,//group by
                null,
                null
        );
    }

    public void delete(String item) {
        String[] args = {String.valueOf(item)};
        database.delete(
                GroceryHelper.TABLE_NAME,
                GroceryHelper.COLUMN_GROCERY + " =? ",
                args
        );
    }

    public void insertDataAccordingToCat(String cat, String item, String rate, String color) {
        database.beginTransaction();
        cat = cat.toLowerCase();
        GroceryHelper.CUSTOM_TABLE = cat;
        try {
            ContentValues values = new ContentValues();

            values.put(GroceryHelper.COLUMN_GROCERY, item);
            values.put(GroceryHelper.COLUMN_RATE, rate);
            values.put(GroceryHelper.COLUMN_COLOR, color);

            database.insert(GroceryHelper.TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void createDatabaseTable(String tableName) {
        //drop is exists
        String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
        database.execSQL(DROP_TABLE);
        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        GroceryHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GroceryHelper.COLUMN_GROCERY + " TEXT, " +
                        GroceryHelper.COLUMN_COLOR + " TEXT, " +
                        GroceryHelper.COLUMN_RATE + " TEXT)";
        database.execSQL(CREATE_TABLE);
    }
    /*public Cursor select(String item) {
        String whereClause = GroceryHelper.COLUMN_GROCERY + " = ?";
        Cursor cursor = database.query(
                GroceryHelper.TABLE_NAME,//table
                new String[]{GroceryHelper.COLUMN_ID, GroceryHelper.COLUMN_GROCERY},//columns names
                whereClause,//where
                new String[]{item},//params
                null,//group by
                null,
                null
        );

        return cursor;
    }*/
    /*private SQLiteDatabase open() {
        return mGroceryHelper.getWritableDatabase();
    }

    private SQLiteDatabase readDatabase() {
        return mGroceryHelper.getReadableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public long create(String item) {
        SQLiteDatabase database = open();
        database.beginTransaction();
        //implement for details
        ContentValues values = new ContentValues();
        values.put(GroceryHelper.COLUMN_GROCERY, item);

        long groceryId = database.insert(GroceryHelper.TABLE_NAME, null, values);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
        return groceryId;
    }

    public String readData() {
        String item = null;
        SQLiteDatabase database = readDatabase();
        Cursor cursor = database.query(
                GroceryHelper.TABLE_NAME,
                new String[]{GroceryHelper.COLUMN_ID, GroceryHelper.COLUMN_GROCERY},
                null,//GroceryHelper.COLUMN_GROCERY + " = " + GroceryHelper.COLUMN_GROCERY,//Selection
                null,//selection args
                null,//group by
                null,//having
                null);//;order

        while (cursor.moveToNext()) {
            item = getString(cursor, GroceryHelper.COLUMN_GROCERY);
        }
        cursor.close();
        close(database);
        return item;
    }

    public String read() {
        String item = null;
        SQLiteDatabase database = open();
        Cursor cursor = database.query(
                GroceryHelper.TABLE_NAME,
                new String[]{GroceryHelper.COLUMN_ID, GroceryHelper.COLUMN_GROCERY},
                null,//Selection
                null,//selection args
                null,//group by
                null,//having
                null);//;order


        while (cursor.moveToNext()) {
            item = getString(cursor, GroceryHelper.COLUMN_GROCERY);
        }
        cursor.close();
        close(database);
        return item;
    }


    private String getString(Cursor cursor, String ColumnName) {
        int columnIndex = cursor.getColumnIndex(ColumnName);
        return cursor.getString(columnIndex);
    }
*/
}
