package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {

    EditText mEmail;
    EditText mPassword;
    EditText mConfirmPassword;
    Button mCreateAccount;
    LoginDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //make a database adaptor
        db = new LoginDBHelper(getApplicationContext());

        //UI variables
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mCreateAccount = (Button) findViewById(R.id.createAccount);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Upon submitting, adds the entered
     * email and password to the database to later
     * be authenticated against
     * @param v
     */
    public void registerUser(View v) {
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        String confirmPass = mConfirmPassword.getText().toString();
        if (!pass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_LONG).show();
        } else {
            SQLiteDatabase database = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(LoginContract.LoginEntry.COLUMN_EMAIL, email);
            values.put(LoginContract.LoginEntry.COLUMN_PASSWORD, pass);
            database.insert(LoginContract.LoginEntry.TABLE_NAME,
                    null,
                    values);
            Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Goes back to the previous screen
     * if the user chooses to cancel registration
     */
    public void cancelRegister(View v) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
