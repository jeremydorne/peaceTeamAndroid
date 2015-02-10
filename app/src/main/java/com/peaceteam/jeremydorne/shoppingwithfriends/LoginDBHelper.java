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

    /* variables that holder information about the db */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShoppingWithFriends.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + LoginContract.LoginEntry.TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LoginContract.LoginEntry.COLUMN_EMAIL + " TEXT,"
            + LoginContract.LoginEntry.COLUMN_PASSWORD + " TEXT)";

    /**
     * Create an instance of the DB helper in the given context
     * @param context
     */
    public LoginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * If the table does not already exist, create it
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * NOT IMPLEMENTED
     * Recreates the table if the DB is updated
     * @param db the db to be updated
     * @param oldVersion the old version number
     * @param newVersion the new version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
