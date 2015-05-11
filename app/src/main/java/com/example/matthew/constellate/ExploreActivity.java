package com.example.matthew.constellate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExploreActivity extends AndroidApplication {

    private ArrayList<Constellation> constellations;
    private ConstellateGlobals global;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        boolean useOpenGLES2 = false;
        Vector3 startDir = new Vector3(0f, 0f, 5f);
        Intent intent = getIntent();
        int cons_ID = intent.getIntExtra("ID", -1);

        Log.d("ID", "ID: " + cons_ID);

        Context context = getApplicationContext();
        global = ((ConstellateGlobals) this.getApplication());

        super.onCreate(savedInstanceState);
        loadConstellations();
        initialize(new Stargazer(global, context, constellations, cons_ID));
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
                    Constellation constellation;

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
                        constellation = new Constellation(name, id);
                        constellations.add(constellation);
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
                        }

                        constellation.addPairs(pairs);
                    }
                }
                catch(Exception e){  Log.d("EXCEPTION", e.toString()) ;}
            }
        }, global.authenticatedUser.getToken());

        constellationTask.execute(global.API_URL, global.CONSTELLATION_ENDPOINT, "GET", "", "");
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
