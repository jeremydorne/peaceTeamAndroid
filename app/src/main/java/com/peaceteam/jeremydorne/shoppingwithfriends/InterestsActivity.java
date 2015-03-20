package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peaceteam.robertguthrie.model.Interest;
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
 * Activity class for viewing a person's registered
 * interests
 * @author Robert Guthrie
 * @version 1.0
 */
public class InterestsActivity extends Activity {

    public ListView mListView;
    private String userEmail;
    protected ArrayList<Interest> interestsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interestsArrayList = new ArrayList<>();
        userEmail = getIntent().getStringExtra("email");
        mListView = (ListView) findViewById(R.id.interests_list_view);
    }

    /**
     * Builds or updates the table of interests when data
     * might have changed
     */
    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<Interest> adapter = (ArrayAdapter<Interest>) mListView.getAdapter();
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        GetInterestsTask getInterests = new GetInterestsTask(userEmail);
        getInterests.execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interests, menu);
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
     * Builds the table of interests from the
     * data from HTTP requests
     * @param interests received from HTTP request
     */
    public void populateTable(JSONArray interests) {
        try {
            for (int i = 0; i < interests.length(); i++) {
                JSONObject jsonUser = new JSONObject(interests.get(i).toString());
                String itemName = jsonUser.getString("itemName");
                double price = jsonUser.getDouble("price");
                Interest interest = new Interest(itemName, price);
                interestsArrayList.add(interest);
            }
        } catch (JSONException e) {
            Log.d("info", e.getMessage());
        }
        ArrayAdapter<Interest> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, interestsArrayList);
        mListView.setAdapter(adapter);
    }

    /**
     * Moves the user to the register interest view
     * @param v that fires the event
     */
    public void goToRegisterInterest(View v) {
        Intent intent = new Intent(this, RegisterInterestActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    /**
     * Class to make HTTP requests to get a user's interests
     * @author Robert Guthrie
     * @version 1.0
     */
    public class GetInterestsTask extends AsyncTask<Void, Void, JSONArray> {

        private final String userEmail;

        /**
         * Creates a GetInterestsTask
         * @param email of the user to get the interests of
         */
        public GetInterestsTask(String email) {
            userEmail = email;
        }

        /**
         * Makes an HTTP request and returns a JSON array
         * of the user's interests
         * @param params
         * @return a JSON array of the users interests
         */
        protected JSONArray doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getinterests/" + userEmail);
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
         * Populates the list view with the retrieved data
         * if the HTTP request was successful
         * @param result of the HTTP request
         */
        @Override
        protected void onPostExecute(final JSONArray result) {
            if (result != null) {
                populateTable(result);
            } else {
                Log.d("info", "response was null");
            }
        }

    }

}
