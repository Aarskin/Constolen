package com.example.matthew.constellate;

import android.content.Context;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import java.util.HashMap;
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
    public HashMap<Integer, Star> hashStars;
    public ArrayList<Vector3> selected;
    public Gson gread;
    public Scanner reader;
    public Context context;
    public ShapeRenderer draw;
    public Vector3[] locs;
    public BitmapFont font;
    public SpriteBatch batch;
    public ArrayList<Constellation> constellations;
    public Star star;
    public ArrayList<StarPair> pairs;

    private int NUM_STARS = 520;
    private float SCALAR = 255f;
    private float VIEW_MIN = 1f;
    private ConstellateGlobals global;

    public Stargazer(ConstellateGlobals act, Context c, ArrayList<Constellation> cs)
    {
        global = act;
        context = c;
        constellations = cs;
    }

    @Override
    public void create()
    {
        // Need a camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 0f, 0f);
        cam.lookAt(0f, 0f, 5f);
        cam.near = VIEW_MIN;
        cam.far = SCALAR;
        cam.update();

        // Instantiate
        modelBuilder = new ModelBuilder();
        modelBatch = new ModelBatch();
        instances = new Array<ModelInstance>();
        stars = new ArrayList<Star>();
        gread = new Gson();
        reader = new Scanner(context.getResources().openRawResource(R.raw.stars));
        draw = new ShapeRenderer();
        locs = new Vector3[NUM_STARS];
        selected = new ArrayList<Vector3>();
        hashStars = new HashMap<Integer, Star>();
        star = new Star();
        pairs = new ArrayList<StarPair>();

        // Want to rotate when panning
        rotate = new GestureDetector((new PanningController(this)));
        Gdx.input.setInputProcessor(rotate);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.YELLOW);

        while(reader.hasNextLine()) {
            star = gread.fromJson(reader.nextLine(), Star.class);
            stars.add(star);
            hashStars.put(star.ID_NUM, star);
        }

        createStarModels();
        //computeConstellations();
    }

    public void createStarModels()
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

    public void computeConstellations()
    {
        Star s1, s2;
        Vector n1, n2;
        float x1, x2;
        float y1, y2;
        float z1, z2;

        // Draw lines between all the pairs!
        for(Constellation con : constellations)
        {
            for(StarPair pair : con.pairs)
            {
                s1 = hashStars.get(pair.star1);
                s2 = hashStars.get(pair.star2);

                if (s1 != null && s2 != null) {
                    n1 = s1.getHat();
                    n2 = s2.getHat();

                    x1 = SCALAR * (float) n1.getX();
                    y1 = SCALAR * (float) n1.getY();
                    z1 = SCALAR * (float) n1.getZ();

                    x2 = SCALAR * (float) n2.getX();
                    y2 = SCALAR * (float) n2.getY();
                    z2 = SCALAR * (float) n2.getZ();

                    pair.v1 = new Vector3(x1, y1, z1);
                    pair.v2 = new Vector3(x2, y2, z2);

                    pairs.add(pair);
                }
            }
        }
    }

    @Override
    public void render()
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render lines
        cam.update();
        draw.setProjectionMatrix(cam.combined);
        draw.begin(ShapeRenderer.ShapeType.Line);
        draw.setColor(Color.GREEN);
        for(StarPair pair : pairs)
            draw.line(pair.v1, pair.v2);
        draw.end();

        // Render coordinates
        //batch.begin();
        //font.draw(batch, "Hello World!", 200, 200);
        //batch.end();

        // Render stars
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
