package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robertguthrie on 2/9/15.
 */
public class FriendsDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShoppingWithFriends.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + FriendsContract.FriendsEntry.TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FriendsContract.FriendsEntry.COLUMN_USER1 + " INTEGER FOREIGN KEY REFERENCES LOGIN(_id),"
            + FriendsContract.FriendsEntry.COLUMN_USER2 + " INTEGER FOREIGN KEY REFERENCES LOGIN(_id)";

    public FriendsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
