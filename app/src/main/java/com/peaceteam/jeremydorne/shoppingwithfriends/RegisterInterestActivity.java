package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Activity class for registering interest for an item
 * @author Robert Guthrie
 * @version 1.0
 */
public class RegisterInterestActivity extends Activity {

    String currentUserEmail;
    EditText interestTextField;
    EditText priceTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interest);
        currentUserEmail = getIntent().getStringExtra("email");
        interestTextField = (EditText) findViewById(R.id.interest_text_box);
        priceTextField = (EditText) findViewById(R.id.desired_price_text_box);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_interest, menu);
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
     * Takes the values from the text field
     * and registers an interest with the user's account
     * @param v view that triggers the event (submit button)
     */
    public void registerInterest(View v) {
        String itemName = interestTextField.getText().toString();
        Double price = Double.parseDouble(priceTextField.getText().toString());
        RegisterInterestTask registerInterestTask = new RegisterInterestTask(currentUserEmail,
                itemName, price);
        registerInterestTask.execute((Void) null);
    }

    /**
     * Once the interest is registered, return to
     * the list of interests
     */
    public void finishRegisterInterest() {
        finish();
    }

    /**
     * Registers the user's interest and stores in the database
     * with the web service
     * @author Robert Guthrie
     * @version 1.0
     */
    public class RegisterInterestTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String itemName;
        private final Double price;

        /**
         * @param email of the user to add the interest to
         * @param itemName of the interest
         * @param price max price desired
         */
        public RegisterInterestTask(String email, String itemName, Double price) {
            this.email = email;
            this.itemName = itemName;
            this.price = price;
        }

        /**
         * Sends an HTTP POST request to store the desired interest in the database
         * @param params
         * @return whether it was successful
         */
        @Override
        public Boolean doInBackground(Void... params) {
            try {
                String requestString = "{ \"email\": \"" + email + "\", \"itemName\": \""
                        + itemName + "\"," + "\"price\": \"" + price.toString() + "\" }";
                URL url = new URL("http://10.0.2.2:3000/registerinterest");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Content-Type", "application/json");
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(requestString);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d("Info:", response.toString());
                JSONObject responseObject = new JSONObject(response.toString());
                Boolean didRegisterInterest = responseObject.getBoolean("didAddInterest");
                return didRegisterInterest;
            } catch (Exception e) {
                Log.d("info", "Problem adding interest");
            }
            return false;
        }

        /**
         * If it was successful, then finish the process
         * @param didRegisterInterest whether the process was successful
         */
        @Override
        protected void onPostExecute(final Boolean didRegisterInterest) {
            if (didRegisterInterest) {
                finishRegisterInterest();
            } else {
                Log.d("info", "interest not registered");
            }
        }
    }
}
