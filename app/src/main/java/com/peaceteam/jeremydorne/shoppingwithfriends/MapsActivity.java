package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Activity that displays a map of a given sale location
 * @author Robert Guthrie
 * @version 1.0
 */
public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Adds a map marker based on the result of the HTTP request
     * @param result of the HTTP request
     */
    void finish(JSONObject result) {
        try {
            LatLng res = new LatLng(result.getDouble("latitude"), result.getDouble("longitude"));
            mMap.addMarker(new MarkerOptions()
                    .position(res)
                    .title(result.getString("locationName")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(res));
        } catch (JSONException e) {
            Log.d("info", e.getMessage());
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String locationName = getIntent().getStringExtra("locationName");
        GetLocationDataTask task = new GetLocationDataTask(locationName);
        task.execute((Void) null);
    }

    /**
     * Class to make HTTP requests to get the 
     * latitude and longitude of a given sale location
     * @author Robert Guthrie
     * @version 1.0
     */
    public class GetLocationDataTask extends AsyncTask<Void, Void, JSONObject> {

        private final String locationName;

        /**
         * Creates a GetLocationDataTask
         * @param name of the location to get the data of
         */
        public GetLocationDataTask(String name) {
            locationName = name;
        }

        /**
         * Performs an HTTP request and gets the 
         * latitude and longitude of the name of a location
         * passed into the intent that created the activity
         * @param params void
         * @return the latitude and longitude of a given place
         */
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getlocation/" + locationName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setChunkedStreamingMode(0);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d("Info:", response.toString());
                return new JSONObject(response.toString());
            } catch (Exception e) {
                Log.d("info", e.getMessage());
            }
            return null;
        }

        /**
         * If the HTTP request was successful,
         * put a marker on the map view.
         * @param result of the HTTP request
         */
        @Override
        protected void onPostExecute(final JSONObject result) {
            if (result != null) {
                finish(result);
            } else {
                Log.d("info", "Problem getting location data");
            }
        }

    }
}
