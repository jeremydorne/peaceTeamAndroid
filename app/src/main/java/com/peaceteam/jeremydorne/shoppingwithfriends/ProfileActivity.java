package com.peaceteam.jeremydorne.shoppingwithfriends;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ProfileActivity extends Activity {

    String currentUserEmail;
    String email;
    String name;
    Double rating;
    Integer numSalesReported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUserEmail = getIntent().getStringExtra("currentUserEmail");
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        rating = getIntent().getDoubleExtra("rating", 0.0);
        numSalesReported = getIntent().getIntExtra("numSalesReported", 0);
        TextView emailTextField = (TextView) findViewById(R.id.email_text_field);
        emailTextField.setText(email);
        TextView nameTextField = (TextView) findViewById(R.id.name_text_field);
        nameTextField.setText(name);
        TextView ratingTextField = (TextView) findViewById(R.id.rating_text_field);
        ratingTextField.setText(rating.toString());
        TextView numSalesTextField = (TextView) findViewById(R.id.num_sales_reported_text_field);
        numSalesTextField.setText(numSalesReported.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
