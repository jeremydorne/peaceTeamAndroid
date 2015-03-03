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

    public void registerInterest(View v) {
        String itemName = interestTextField.getText().toString();
        Double price = Double.parseDouble(priceTextField.getText().toString());
        RegisterInterestTask registerInterestTask = new RegisterInterestTask(currentUserEmail,
                itemName, price);
        registerInterestTask.execute((Void) null);
    }

    public void finishRegisterInterest() {
        finish();
    }

    public class RegisterInterestTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String itemName;
        private final Double price;

        public RegisterInterestTask(String email, String itemName, Double price) {
            this.email = email;
            this.itemName = itemName;
            this.price = price;
        }

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
