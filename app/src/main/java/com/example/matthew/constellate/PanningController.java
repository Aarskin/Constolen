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
    public PerspectiveCamera cam;
    public float fingX, fingY;
    public Star closest;
    ModelInstance starModel;
    Ray ray;
    Intersector intersector;

    public PanningController(Stargazer g)
    {
        fingX = 0;
        fingY = 0;

        gazer = g;
        cam = gazer.cam;
        intersector = new Intersector();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        Vector3 cur;
        Ray ray = cam.getPickRay(x, y);
        Star star;
        float mag;
        Material yellow = new Material(ColorAttribute.createDiffuse(Color.WHITE));

        for(int i = 0; i < gazer.locs.length; i++)
        {
            cur = gazer.locs[i];
            star = gazer.stars.get(i);
            mag = (float)star.getMag();

            Log.d("touch", "check" + i);

            if(Intersector.intersectRaySphere(ray, cur, mag, null)) {
                Log.d("intersection", "Location: " + cur);
                gazer.instances.get(i).materials.clear();
                gazer.instances.get(i).materials.add(yellow);
                break; // Cur is the loc of the star we touched
            }
        }

        Log.d("touch", "LOOP FINISH");

        /*
        // Unproject onto the far plane (z = 1f)
        inWorld = cam.unproject(new Vector3(x, y, 1f));
        closest = findStar(inWorld);

        gazer.selected.add(closest);
        */

        return true;
    }

    private Vector3 findStar(Vector3 inWorld)
    {
        /*
        Vector v;
        Vector3 starVec;
        Vector3 retVec = new Vector3();
        float minDistance = 1000f;

        for(Star star : gazer.stars)
        {
            v = star.getHat();

            starVec = new Vector3((float)v.getX(), (float)v.getY(), (float)v.getZ()).scl(255f);

            if(inWorld.dst(starVec) < minDistance)
            {
                retVec = starVec;
            }
        }
        */

        return null;
    }

    @Override
    public boolean tap(float v, float v2, int i, int i2)
    {
        return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {

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
