package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Object that allows for reading and writing to
 * the underlying SQLite database
 * @author Robert Guthrie
 * @version 1.0
 */
public class LoginDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Login.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + LoginContract.LoginEntry.TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LoginContract.LoginEntry.COLUMN_EMAIL + " TEXT,"
            + LoginContract.LoginEntry.COLUMN_PASSWORD + " TEXT)";

    public LoginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
