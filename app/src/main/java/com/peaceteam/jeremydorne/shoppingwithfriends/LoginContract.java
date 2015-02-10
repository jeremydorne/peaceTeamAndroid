package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.provider.BaseColumns;

/**
 * A class that represents a login item
 * in the data base
 * @author Robert Guthrie
 * @version 1.0
 */
public final class LoginContract {

    public LoginContract() {}

    /**
     * Class that represents the columns and table name of the Login table
     * as static variables
     */
    public static abstract class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "LOGIN";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }
}
