package com.example.matthew.constellate;

import java.util.ArrayList;

/**
 * Created by Matthew on 5/7/2015.
 *
 * ID of -1 indicates it has not yet been POSTed
 *
 */
public class Constellation
{
    public String name;
    public int ID;
    public ArrayList<StarPair> pairs;

    public Constellation()
    {

    }

    public Constellation(String n, int i)
    {
        name = n;
        int ID = i;
        pairs = new ArrayList<StarPair>();
    }

    public void addPairs(ArrayList<StarPair> list)
    {
        pairs.addAll(list);
    }

    public void addPair(StarPair pair)
    {
        pairs.add(pair);
    }
}
