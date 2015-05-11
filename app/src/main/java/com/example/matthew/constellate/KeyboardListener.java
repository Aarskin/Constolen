package com.example.matthew.constellate;

import android.util.Log;

import com.badlogic.gdx.Input;

import org.json.JSONObject;

import java.security.Key;

/**
 * Created by Matthew on 5/10/2015.
 */
public class KeyboardListener implements Input.TextInputListener
{
    ConstellateGlobals global;
    Constellation constellation;

    public KeyboardListener(ConstellateGlobals cg, Constellation c)
    {
        global = cg;
        constellation = c;
    }

    @Override
    public void input(String s)
    {
        CallAPI postConstTask  = new CallAPI(new CallAPI.ResponseListener()
        {
            @Override
            public void responseReceived(String response)
            {
                // Do something with it
                try
                {
                    JSONObject responseJ = new JSONObject(response);
                    int id = responseJ.getInt("constellation_id");
                    constellation.ID = id;
                }
                catch(Exception e) { Log.d("EXCEPTION ", e.toString()); }
            }
        }, global.authenticatedUser.getToken());

        constellation.name = s;
        String consJ = constellation.toString();
        Log.d("POST", "JSON executed: " + consJ);
        postConstTask.execute(global.API_URL, global.CONSTELLATION_ENDPOINT, "POST", "", consJ);
    }

    @Override
    public void canceled() {

    }
}
