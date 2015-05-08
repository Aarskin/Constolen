package com.example.matthew.constellate;

import java.util.ArrayList;

/**
 * Created by Matthew on 5/7/2015.
 */
public class Constellation
{
    public String name;
    public int id;
    public ArrayList<StarPair> pairs;

    public Constellation(String n, int i)
    {
        name = n;
        id = i;

        pairs = new ArrayList<StarPair>();
    }

    public void addPairs(ArrayList<StarPair> list)
    {
        pairs.addAll(list);
    }

}
