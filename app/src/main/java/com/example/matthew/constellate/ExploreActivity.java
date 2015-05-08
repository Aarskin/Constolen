package com.example.matthew.constellate;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.example.matthew.constellate.Stargazer;


public class ExploreActivity extends AndroidApplication {

    private Constellate global;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        boolean useOpenGLES2 = false;

        Context context = getApplicationContext();
        global = ((Constellate) this.getApplication());

        super.onCreate(savedInstanceState);
        loadConstellations();
        initialize(new Stargazer(global, context));
    }

    private void loadConstellations()
    {
        CallAPI constellationTask = new CallAPI(new CallAPI.ResponseListener()
        {
            @Override
            public void responseReceived(String response)
            {
                System.out.println(response);
            }
        }, global.authenticatedUser.getToken());

        constellationTask.execute(
                getString(R.string.api_url),
                getString(R.string.const_endpoint),
                "GET", "", "");
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
