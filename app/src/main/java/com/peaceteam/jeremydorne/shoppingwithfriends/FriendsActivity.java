package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class FriendsActivity extends Activity {

    public ListView mListView;
    private String userEmail;
    protected ArrayList<User> friendsArrayList;
    public EditText mAddFriendField;



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
    }

    public void addFriend(View v) {
        String friendEmail = mAddFriendField.getText().toString();
        AddFriendTask addFriendTask = new AddFriendTask(userEmail, friendEmail);
        addFriendTask.execute((Void) null);
    }

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

    public class AddFriendTask extends AsyncTask<Void, Void, JSONObject> {
        private final String email;
        private final String friendEmail;

        public AddFriendTask(String email, String friendEmail) {
            this.email = email;
            this.friendEmail = friendEmail;
        }

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

        @Override
        protected void onPostExecute(final JSONObject result) {
            if (result != null) {
                finishAddFriend(result);
            } else {
                Log.d("info", "Result of HTTP request to add friend was null");
            }
        }
    }

    public class GetFriendsTask extends AsyncTask<Void, Void, JSONObject> {
        private final String email;

        public GetFriendsTask(String email) {
            this.email = email;
        }

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
