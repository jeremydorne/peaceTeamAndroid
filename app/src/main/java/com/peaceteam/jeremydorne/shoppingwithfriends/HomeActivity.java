package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peaceteam.robertguthrie.model.Sale;
import com.peaceteam.robertguthrie.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Activity class for the Home page of the application
 * @author Robert Guthrie
 * @version 1.0
 */
public class HomeActivity extends Activity {

    private String userEmail;
    private ArrayList<Sale> salesArrayList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userEmail = getIntent().getStringExtra("email");
        salesArrayList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.sales_list_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<Sale> adapter = (ArrayAdapter<Sale>) mListView.getAdapter();
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        GetSalesTask getSales = new GetSalesTask(userEmail);
        getSales.execute((Void) null);
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

    public void populateTable(JSONArray data) {
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonSale = new JSONObject(data.get(i).toString());
                String itemName = jsonSale.getString("itemName");
                String location = jsonSale.getString("place");
                Double price = jsonSale.getDouble("price");
                Sale sale = new Sale(itemName, price, location);
                salesArrayList.add(sale);
            }
        } catch (JSONException e) {
            Log.d("info", e.getMessage());
        }
        ArrayAdapter<Sale> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, salesArrayList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sale sale = (Sale) parent.getItemAtPosition(position);
                Log.d("info", sale.getItemName());
                itemClick(sale);
            }
        });
    }

    public void itemClick(Sale sale) {

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

    /**
     * Sends the user to the interests screen
     * @param v that sends the event
     */
    public void goToInterests(View v) {
        Intent intent = new Intent(this, InterestsActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    /**
     * Sends the user to the screen to report a sale
     * @param v that sends the event
     */
    public void goToReportSale(View v) {
        Intent intent = new Intent(this, ReportSaleActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    /**
     * Gets the sales that the user is interested in and below their threshold price
     * @author Robert Guthrie
     * @version 1.0
     */
    public class GetSalesTask extends AsyncTask<Void, Void, JSONArray> {
        private String email;

        public GetSalesTask(String email) {
            this.email = email;
        }

        /**
         * Gets a JSON array of sales that the user is interested in that have been reported
         * @param params
         * @return the sales the user is interested in
         */
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getsales/" + email);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setChunkedStreamingMode(0);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d("Info:", response.toString());
                JSONArray responseObject = new JSONArray(response.toString());
                return responseObject;
            } catch (Exception e) {
                Log.d("info", e.getMessage());
            }
            return null;
        }

        /**
         * If it was successful, populate the table with the results
         * @param result
         */
        @Override
        protected void onPostExecute(final JSONArray result) {
            if (result != null) {
                populateTable(result);
            } else {
                Log.d("info", "Problem getting sales");
            }
        }

    }
}
