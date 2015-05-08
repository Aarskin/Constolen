package com.example.matthew.constellate;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Matthew on 5/5/2015.
 */
public class PanningController implements GestureListener
{
    public Stargazer gazer;
    public PerspectiveCamera cam;
    public float fingX, fingY;
    public Star closest;

    public PanningController(Stargazer g)
    {
        fingX = 0;
        fingY = 0;

        gazer = g;
        cam = gazer.cam;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        Vector3 inWorld, closest;

        // Unproject onto the far plane (z = 1f)
        inWorld = cam.unproject(new Vector3(x, y, 1f));
        closest = findStar(inWorld);

        gazer.selected.add(closest);

        return true;
    }

    private Vector3 findStar(Vector3 inWorld)
    {
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

        return retVec;
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
