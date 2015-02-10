package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.provider.BaseColumns;

/**
 * Created by robertguthrie on 2/9/15.
 */
public class FriendsContract {

    public FriendsContract() {}

    public static abstract class FriendsEntry implements BaseColumns {
        public static final String TABLE_NAME = "FRIENDS";
        public static final String COLUMN_USER1 = "user1";
        public static final String COLUMN_USER2 = "user2";
    }

}
