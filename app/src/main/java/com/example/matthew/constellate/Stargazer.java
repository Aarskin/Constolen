package com.example.matthew.constellate;

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
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

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

    @Override
    public void create()
    {
        // Need a camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 0f, 0f);
        cam.lookAt(0f, 0f, 5f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        // Want to rotate when panning
        rotate = new GestureDetector((new PanningController(this)));
        Gdx.input.setInputProcessor(rotate);

        // Create obj to test rendering
        modelBuilder = new ModelBuilder();
        modelBatch = new ModelBatch();
        instances = new Array<ModelInstance>();

        /*
        // Sample box model, replace with star population
        model = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
         */



        loadStars();
    }

    public void loadStars()
    {
        for(int i = 0; i < 15; i++)
        {
            model = modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 15, 15,
                    new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

            if(i<3)
                inst = new ModelInstance(model, 0f, 0f, 5f+i);
            else if(i<6)
                inst = new ModelInstance(model, -0.5f, -0.5f, 5f+(i-3));
            else if(i<9)
                inst = new ModelInstance(model, 0.5f, 0.5f, 5f+(i-6));
            else if(i<12)
                inst = new ModelInstance(model, -0.5f, 0.5f, 5f+(i-9));
            else if(i<15)
                inst = new ModelInstance(model, 0.5f, -0.5f, 5f+(i-12));

            instances.add(inst);
        }
    }

    @Override
    public void render()
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
