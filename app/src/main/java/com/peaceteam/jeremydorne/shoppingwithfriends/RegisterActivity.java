package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for the register screen of the application
 * @author Robert Guthrie
 * @version 1.0
 */
public class RegisterActivity extends Activity {

    /* Variables to store references to UI controls */
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mName;

    /**
     * Initialize references to views and the database
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //UI variables
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mName = (EditText) findViewById(R.id.name);
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

    /**
     * Upon submitting, adds the entered
     * email and password to the database to later
     * be authenticated against
     * @param v view
     */
    public void registerUser(View v) {
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        String confirmPass = mConfirmPassword.getText().toString();
        String name = mName.getText().toString();
        if (!pass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_LONG).show();
        } else {
            Log.d("info", "About to register user");
            RegisterUserTask rTask = new RegisterUserTask(email, pass, name);
            rTask.execute((Void) null);
        }
    }

    void finishRegistration() {
        Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", mEmail.getText().toString());
        startActivity(intent);
    }

    /**
     * Goes back to the previous screen
     * if the user chooses to cancel registration
     */
    public void cancelRegister(View v) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPass;
        private final String mName;

        RegisterUserTask(String email, String password, String name) {
            mEmail = email;
            mPass = password;
            mName = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String credentials = "{ \"email\": \"" + mEmail + "\", \"password\": \"" + mPass + "\"," +
                  "\"name\": \"" + mName + "\" }";
            try {
                URL url = new URL("http://10.0.2.2:3000/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Content-Type", "application/json");
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(credentials);
                wr.flush();
                wr.close();

                //TODO Read a response and make sure it was written
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject responseObject = new JSONObject(response.toString());
                return responseObject.getBoolean("isRegistered");

            } catch (Exception e) {
                Log.d("info", "Error occurred");
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finishRegistration();
            }
        }

    }
}
