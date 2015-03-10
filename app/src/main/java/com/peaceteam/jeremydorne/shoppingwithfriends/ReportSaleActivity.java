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


public class ReportSaleActivity extends Activity {

    EditText mItemName;
    EditText mLocation;
    EditText mPrice;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sale);
        mItemName = (EditText) findViewById(R.id.item_name_edit_text);
        mLocation = (EditText) findViewById(R.id.location_edit_text);
        mPrice = (EditText) findViewById(R.id.price_edit_text);
        String userEmail = getIntent().getStringExtra("email");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_sale, menu);
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

    public void reportSale(View v) {
        String itemName = mItemName.getText().toString();
        String location = mLocation.getText().toString();
        Double price = Double.parseDouble(mPrice.getText().toString());
        ReportSaleTask task = new ReportSaleTask(userEmail, itemName, location, price);
        task.execute((Void) null);
    }

    public void finishReportSale() {
        finish();
    }

    public class ReportSaleTask extends AsyncTask<Void, Void, Boolean> {
        private String email;
        private String itemName;
        private String location;
        private Double price;

        public ReportSaleTask(String email, String itemName, String location, Double price) {
            this.email = email;
            this.itemName = itemName;
            this.location = location;
            this.price = price;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String requestString = "{ \"email\": \"" + email + "\", \"itemName\": \""
                        + itemName + "\"," + "\"price\": \"" + price.toString() + "\"," +
                        "\"place\": \"" + location + "\" }";
                URL url = new URL("http://10.0.2.2:3000/reportsale");
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
                Boolean didReportSale = responseObject.getBoolean("didReportSale");
                return didReportSale;
            } catch (Exception e) {
                Log.d("info", "Problem reporting sale");
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finishReportSale();
            }
        }

    }
}
