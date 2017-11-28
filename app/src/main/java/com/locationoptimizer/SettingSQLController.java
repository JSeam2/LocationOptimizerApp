package com.locationoptimizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingSQLController {
    public static DBHelper dbHelper;
    public Context myContext;
    public static SQLiteDatabase database;

    public SettingSQLController(Context c) {
        myContext = c;
    }

    public SettingSQLController open() throws SQLException {
        dbHelper = new DBHelper(myContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(boolean isFast, boolean isBrute, double budget){
        ContentValues contentValues = new ContentValues();

        // Convert to int as SQLLITE doesn't accept Boolean
        int addFast = isFast ? 1 : 0;
        int addBrute = isBrute ? 1 : 0;

        contentValues.put(DBHelper.fast, addFast);
        contentValues.put(DBHelper.brute, addBrute);
        contentValues.put(DBHelper.budget, budget);

        // insert contentValues
        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public Cursor fetch(){
        String[] columns = new String[] {DBHelper._ID, DBHelper.fast, DBHelper.brute, DBHelper.budget};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    public int update(long _id, boolean isFast, boolean isBrute, double budget){
        ContentValues contentValues = new ContentValues();

        // Convert to int as SQLLITE doesn't accept Boolean
        int addFast = isFast ? 1 : 0;
        int addBrute = isBrute ? 1 : 0;

        contentValues.put(DBHelper.fast, addFast);
        contentValues.put(DBHelper.brute, addBrute);
        contentValues.put(DBHelper.budget, budget);

        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);

        return i;
    }

    public void delete(long _id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper._ID + "=" + _id, null);
    }

}



class DBHelper extends SQLiteOpenHelper {
    // table name
    public static final String TABLE_NAME = "Settings";

    // DB Name
    public static final String DB_NAME = "LocationOptimizer.DB";

    // columns
    public static final String _ID = "id";
    public static final String fast = "isFast";
    public static final String brute = "isBrute";
    public static final String budget = "budget";

    // version
    static final int DB_VERSION = 1;

    // create table query
    private static final String CREATE_TABLE =
            "create table " + TABLE_NAME + "("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + fast + " INTEGER NOT NULL," // 1 or 0
                    + brute + " INTEGER NOT NULL," // 1 or 0
                    + budget + " REAL NOT NULL);";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
