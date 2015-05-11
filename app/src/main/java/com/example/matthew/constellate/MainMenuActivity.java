package com.example.matthew.constellate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuActivity extends Activity {

    // Global singleton
    ConstellateGlobals global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Assign global singleton
        global = ((ConstellateGlobals) this.getApplication());
        global.context = this;
        global.API_URL = getString(R.string.api_url);
        global.AUTHENTIACATION_ENDPOINT = getString(R.string.auth_endpoint);
        global.USER_ENDPOINT = getString(R.string.user_endpoint);
        global.CONSTELLATION_ENDPOINT = getString(R.string.const_endpoint);
        global.CONSTELLATION_BY_STAR_ENDPOINT = getString(R.string.const_by_star_endpoint);
        global.WAITING = false;


        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openExplore(View view)
    {
        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
    }

    public void openCollection(View view)
    {
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        // Clear authenticated user
        global.authenticatedUser = null;

        // Open up main activity
        Intent intent = new Intent(this, Constellate.class);
        startActivity(intent);
    }
}
