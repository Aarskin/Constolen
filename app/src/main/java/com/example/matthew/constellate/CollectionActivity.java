package com.example.matthew.constellate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.math.Interpolation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CollectionActivity extends ActionBarActivity
{
    public LinearLayout ll;
    public ScrollView sv;

    // Cache of constellations (needs to be cleared at logout, which this is not doing)
    ArrayList<Constellation> constellations = null;
    ConstellateGlobals global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        global = (ConstellateGlobals)this.getApplication();
        global.context = this;

        if(constellations == null)
            cacheConstellations();
        else
            ConfigureButtons();
    }

    private void ConfigureButtons()
    {
        // Create the containers for this button
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        Log.d("", "Creating Buttons");
        for(Constellation c : constellations)
        {
            final Constellation cons = c;

            Button button = new Button(this);
            button.setText(""+c.ID+"");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialize a Stargazer pointed at this constellation
                    Intent intent = new Intent(global.context, ExploreActivity.class);
                    intent.putExtra("ID", cons.ID);
                    startActivity(intent);
                }
            });
            ll.addView(button);
        }

        setContentView(sv);
    }

    private void cacheConstellations()
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
                        Log.d("CONST", "NAME: " + name + " ID: " + id);

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
                catch(Exception e){ Log.d("EXCEPTION", e.toString()) ;}

                ConfigureButtons();
            }
        }, global.authenticatedUser.getToken());

        constellationTask.execute(global.API_URL, global.CONSTELLATION_ENDPOINT, "GET", "", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collection, menu);
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
