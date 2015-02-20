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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Activity class for the friends list screen
 * @author Robert Guthrie
 * @version 1.0
 */
public class FriendsActivity extends Activity {

    public ListView mListView;
    private String userEmail;
    protected ArrayList<User> friendsArrayList;
    public EditText mAddFriendField;


    /**
     * Get data from web service upon opening the friends screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friendsArrayList = new ArrayList<>();
        userEmail = getIntent().getStringExtra("email");
        mListView = (ListView) findViewById(R.id.list_view);
        mAddFriendField = (EditText) findViewById(R.id.add_friend_text_field);
        GetFriendsTask getFriends = new GetFriendsTask(userEmail);
        getFriends.execute((Void) null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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
     * After getting data asynchronously, populates the list view with the data received
     * @param data from HTTP request
     */
    public void populateTable(JSONObject data) {
        try {
            JSONArray friends = data.getJSONArray("friends");
            for (int i = 0; i < friends.length(); i++) {
                JSONObject jsonUser = new JSONObject(friends.get(i).toString());
                String email = jsonUser.getString("email");
                String name = jsonUser.getString("name");
                double rating = jsonUser.getDouble("rating");
                int numSalesReported = jsonUser.getInt("numSalesReported");
                User user = new User(email, name, rating, numSalesReported);
                friendsArrayList.add(user);
            }
        } catch (JSONException e) {
            Log.d("info", e.getMessage());
        }
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsArrayList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                Log.d("info", user.getEmail());
                itemClick(user);
            }
        });
    }

    /**
     * Method that begins the process of adding a friend
     * @param v
     */
    public void addFriend(View v) {
        String friendEmail = mAddFriendField.getText().toString();
        AddFriendTask addFriendTask = new AddFriendTask(userEmail, friendEmail);
        addFriendTask.execute((Void) null);
    }

    /**
     * Used to add the friend to the friends list and display it in the list after
     * completing the HTTP request
     * @param data
     */
    public void finishAddFriend(JSONObject data) {
        try {
            JSONObject friend = data.getJSONObject("friend");
            String email = friend.getString("email");
            String name = friend.getString("name");
            double rating = friend.getDouble("rating");
            int numSalesReported = friend.getInt("numSalesReported");
            User newFriend = new User(email, name, rating, numSalesReported);
            friendsArrayList.add(newFriend);
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsArrayList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d("info", e.getMessage());
        }
    }

    public void itemClick(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("currentUserEmail", userEmail);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("name", user.getName());
        intent.putExtra("rating", user.getRating());
        intent.putExtra("numSalesReported", user.getNumSalesReported());
        startActivity(intent);
    }

    /**
     * Class that permits adding friends
     * @author Robert Guthrie
     * @version 1.0
     */
    public class AddFriendTask extends AsyncTask<Void, Void, JSONObject> {
        private final String email;
        private final String friendEmail;

        /**
         * Constructs an AddFriendTask
         * @param email to add the friend to
         * @param friendEmail email of person being added
         */
        public AddFriendTask(String email, String friendEmail) {
            this.email = email;
            this.friendEmail = friendEmail;
        }

        /**
         * Makes an HTTP request to add the friend to the user
         * @param params
         * @return The JSON whether or not it was successful
         */
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String requestString = "{ \"email\": \"" + email + "\", \"friendEmail\": \""
                        + friendEmail + "\" }";
                URL url = new URL("http://10.0.2.2:3000/addfriend");
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
                return responseObject;
            } catch (Exception e) {
                Log.d("info", "Problem adding friend");
            }
            return null;
        }

        /**
         * Populates the table with the new friend if it was successful
         * @param result
         */
        @Override
        protected void onPostExecute(final JSONObject result) {
            if (result != null) {
                finishAddFriend(result);
            } else {
                Log.d("info", "Result of HTTP request to add friend was null");
            }
        }
    }

    /**
     * Class for getting all friends of a user from the web service
     * @author Robert Guthrie
     * @version 1.0
     */
    public class GetFriendsTask extends AsyncTask<Void, Void, JSONObject> {
        private final String email;

        /**
         * Constructs a GetFriendsTask
         * @param email of the user whose friends to get
         */
        public GetFriendsTask(String email) {
            this.email = email;
        }

        /**
         * Sends an HTTP request to get a JSON array of all friends of a user
         * @param params
         * @return JSONObject containing the users friends and their info
         */
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getfriends/" + email);
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
                JSONObject responseObject = new JSONObject(response.toString());
                return responseObject;
            } catch (Exception e) {
                Log.d("info", e.getMessage());
            }
            return null;
        }

        /**
         * If successful, populate the list view
         * @param result
         */
        @Override
        protected void onPostExecute(final JSONObject result) {
            if (result != null) {
                populateTable(result);
            } else {
                Log.d("info", "result of getfriends was null");
            }
        }
    }
}
