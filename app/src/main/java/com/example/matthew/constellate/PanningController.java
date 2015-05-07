package com.example.matthew.constellate;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector;
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
        fingX = x;
        fingY = y;

        return true;
    }

    @Override
    public boolean tap(float v, float v2, int i, int i2)
    {
        return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
        fingX = x;
        fingY = y;

        Vector3 test = cam.unproject(new Vector3(x, y, 0));

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
