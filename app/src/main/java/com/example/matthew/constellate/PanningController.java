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
    public boolean longPress(float v, float v2)
    {
        return false;
    }

    @Override
    public boolean fling(float v, float v2, int i)
    {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        Vector3 yAxis = new Vector3(0f, 1f, 0f);
        Vector3 xAxis = new Vector3(1f, 0f, 0f);
        Vector3 zAxis = new Vector3(0f, 0f, 1f);

        cam.rotate(xAxis, -0.1f*deltaY);
        cam.rotate(yAxis, -0.1f*deltaX);
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
