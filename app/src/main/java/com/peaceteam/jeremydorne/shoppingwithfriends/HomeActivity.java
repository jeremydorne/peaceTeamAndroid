package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity class for the Home page of the application
 * @author Robert Guthrie
 * @version 1.0
 */
public class HomeActivity extends Activity {

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEmail = getIntent().getStringExtra("email");
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Logs the user out upon hitting the logout button
     * and returns them to the login screen
     * @param v
     */
    public void logoutUser(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    /**
     * Sends the user to the friends screen after hitting the appropriate button
     * @param v that sends the event
     */
    public void goToFriends(View v) {
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }
}
