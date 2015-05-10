package com.example.matthew.constellate;

import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by Matthew on 5/5/2015.
 */
public class PanningController implements GestureListener
{
    public Stargazer gazer;
    //public StarPair newPair;
    public PerspectiveCamera cam;
    public float fingX, fingY;
    public Star closest;
    public ConstellateGlobals global;
    ModelInstance starModel;
    Ray ray;
    Intersector intersector;
    Constellation constellation;

    private String postJ;

    CallAPI postConstTask;

    int s1, s2;
    Vector3 v1, v2;

    public PanningController(Stargazer g, ConstellateGlobals glob)
    {
        fingX = 0;
        fingY = 0;

        global = glob;
        gazer = g;
        cam = gazer.cam;
        intersector = new Intersector();

        // Intermediates for creation
        s1 = -1;
        s2 = -1;
        v1 = new Vector3();
        v2 = new Vector3();
        postJ = new String();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        if(!gazer.DRAW) {
            s1 = -1;
            s2 = -1;

            constellation = null;
            return true; // Done with this constellation/do nothing
        }

        Ray ray = cam.getPickRay(x, y);
        int index = findStar(ray);

        if(index != -1) // Hit!
        {
            if (s1 != -1) {

                if(constellation == null) {
                    constellation = new Constellation("NAME_HERE", -1);
                }

                // Flesh out the pair
                s2 = gazer.stars.get(index).ID_NUM;
                v2 = gazer.locs[index];

                // full now, add to pairs
                StarPair newPair = new StarPair(s1, s2, v1, v2);
                gazer.pairs.add(newPair);
                constellation.addPair(newPair);
                Log.d("pairs", "PAIR_ADDED" + v1 + " | " + v2);

                // Rotate
                s1 = s2;
                v1 = v2;
            } else // star1 == -1, so must star2 (enforce)
            {
                // First star hit
                s1 = gazer.stars.get(index).ID_NUM;
                v1 = gazer.locs[index];
            }
        }

        return true;
    }

    // Returns the index of the identified star
    private int findStar(Ray ray)
    {
        int i;
        Vector3 cur;
        Star star;
        float mag;
        Material green = new Material(ColorAttribute.createDiffuse(Color.GREEN));

        for(i = 0; i < gazer.locs.length-1; i++)
        {
            cur = gazer.locs[i];
            star = gazer.stars.get(i);
            mag = (float)star.getMag();

            Log.d("touch", "check" + i);

            // Account for sloppy presses by making the stars bigger
            if(Intersector.intersectRaySphere(ray, cur, 3*mag, null)) {
                Log.d("intersection", "Location: " + cur);
                gazer.instances.get(i).materials.add(green);
                return i; // Cur is the loc of the star we touched
            }
        }

        return -1;
    }

    @Override
    public boolean tap(float v, float v2, int i, int i2)
    {
        return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
        if(gazer.DRAW) // Post the constellation we finished
        {
            postConstTask  = new CallAPI(new CallAPI.ResponseListener() {
                @Override
                public void responseReceived(String response)
                {
                    // Do something with it

                }
            }, global.authenticatedUser.getToken());

            String consJ = constellation.toString();
            Log.d("POST", "JSON executed: " + consJ);
            postConstTask.execute(global.API_URL, global.CONSTELLATION_ENDPOINT, "POST", "", consJ);
        }
        gazer.DRAW = !gazer.DRAW;
        return true;
    }

    @Override
    public boolean fling(float v, float v2, int i)
    {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        // Relative to the camera
        Vector3 tmp = cam.up.cpy();
        Vector3 yAxis = cam.up;
        Vector3 xAxis = tmp.crs(cam.direction);

        cam.rotate(yAxis, 0.1f*deltaX);
        cam.rotate(xAxis, -0.1f*deltaY);
        cam.update();

        return true;
    }

    @Override
    public boolean panStop(float v, float v2, int i, int i2)
    {
        return false;
    }

    @Override
    public boolean zoom(float v, float v2)
    {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24)
    {
        return false;
    }
}
