package com.example.matthew.constellate;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Json;
import com.example.matthew.constellate.Stargazer;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExploreActivity extends AndroidApplication {

    ArrayList<Constellation> constellations;
    private Constellate global;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        boolean useOpenGLES2 = false;

        Context context = getApplicationContext();
        global = ((Constellate) this.getApplication());

        super.onCreate(savedInstanceState);
        loadConstellations();
        initialize(new Stargazer(global, context, constellations));
    }

    private void loadConstellations()
    {
        constellations = new ArrayList<Constellation>();

        CallAPI constellationTask = new CallAPI(new CallAPI.ResponseListener()
        {
            @Override
            public void responseReceived(String response)
            {

                try {
                    JSONObject OG_JSON = new JSONObject(response);
                    JSONArray constellationA = OG_JSON.getJSONArray("constellations");
                    JSONObject constellationJ, info;

                    // Intermediates
                    JSONArray vectorsA;
                    JSONArray idA;
                    String constellation_infoJ;
                    ArrayList<StarPair> pairs;

                    // Results!
                    int id;
                    String name;
                    int star1, star2;

                    // Loop over constellations
                    for(int i = 0; i < constellationA.length(); i++)
                    {
                        // Grab the first constellation
                        constellationJ = constellationA.getJSONObject(i);

                        // Break it into the two smaller JSON objects
                        constellation_infoJ = constellationJ.getString("info");
                        vectorsA = constellationJ.getJSONArray("vectors");
                        info = new JSONObject(constellation_infoJ);

                        // Read info
                        id = info.getInt("id");
                        name = info.getString("name");
                        constellations.add(new Constellation(name, id));
                        System.out.println("NAME: " + name + " ID: " + id);

                        pairs = new ArrayList<StarPair>();

                        // Loop and read vectors
                        for(int j = 0; j < vectorsA.length(); j++)
                        {
                            // Because Max is an asshole
                            idA = vectorsA.getJSONArray(j);

                            // Finally start reading out star pairs
                            star1 = idA.getInt(0);
                            star2 = idA.getInt(1);

                            pairs.add(new StarPair(star1, star2));
                            System.out.println("STAR1: " + star1 + " STAR2: " + star2);
                        }
                    }
                }
                catch(Exception e){ System.out.println(e) ;}
            }
        }, global.authenticatedUser.getToken());

        gson = new Gson();

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
