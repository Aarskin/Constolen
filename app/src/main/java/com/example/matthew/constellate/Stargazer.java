package com.example.matthew.constellate;

import android.content.Context;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Matthew on 4/20/2015.
 */
public class Stargazer implements ApplicationListener
{
    public Model model;
    public PerspectiveCamera cam;
    public GestureDetector rotate;
    public ModelInstance inst;
    public Array<ModelInstance> instances;
    public ModelBatch modelBatch;
    public ModelBuilder modelBuilder;
    public ArrayList<Star> stars;
    public Gson gread;
    public Scanner reader;
    public Context context;
    public ShapeRenderer draw;
    public Vector3[] locs;

    private int NUM_STARS = 520;
    private int SCALAR = 255;
    private float VIEW_MIN = 1f;
    private float VIEW_MAX = 300f;

    public Stargazer(Context c) {
        context = c;
    }

    @Override
    public void create()
    {
        // Need a camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 0f, 0f);
        cam.lookAt(0f, 0f, 5f);
        cam.near = VIEW_MIN;
        cam.far = VIEW_MAX;
        cam.update();

        // Want to rotate when panning
        rotate = new GestureDetector((new PanningController(this)));
        Gdx.input.setInputProcessor(rotate);

        // Create obj to test rendering
        modelBuilder = new ModelBuilder();
        modelBatch = new ModelBatch();
        instances = new Array<ModelInstance>();
        stars = new ArrayList<Star>();
        gread = new Gson();
        reader = new Scanner(context.getResources().openRawResource(R.raw.stars));
        draw = new ShapeRenderer();
        locs = new Vector3[NUM_STARS];

        while(reader.hasNextLine())
            stars.add(gread.fromJson(reader.nextLine(), Star.class));

        loadStars();
    }

    public void loadStars()
    {
        Star star;
        Vector nick_vector;
        float x, y, z, mag;

        for(int i = 0; i < NUM_STARS; i++)
        {
            star = stars.get(i);

            mag = (float)star.getMag();
            nick_vector = star.getHat();
            x = SCALAR*(float)nick_vector.getX();
            y = SCALAR*(float)nick_vector.getY();
            z = SCALAR*(float)nick_vector.getZ();

            locs[i] = new Vector3(x, y, z);

            model = modelBuilder.createSphere(mag, mag, mag, 10, 10,
                        new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

            // Create the model scaled by the specified coordinates
            inst = new ModelInstance(model, x, y, z);
            instances.add(inst);
        }
    }

    @Override
    public void render()
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.update();
        draw.setProjectionMatrix(cam.combined);
        draw.begin(ShapeRenderer.ShapeType.Line);
        draw.setColor(Color.GREEN);
        for(int i = 0; i < NUM_STARS-1; i++)
            draw.line(locs[i], locs[i+1]);
        draw.end();

        modelBatch.begin(cam);
        modelBatch.render(instances);
        modelBatch.end();
    }

    @Override
    public void dispose()
    {
        model.dispose();
        modelBatch.dispose();
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }
}
