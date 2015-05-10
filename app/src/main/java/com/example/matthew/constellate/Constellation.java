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

    @Override
    // Convert to JSON
    public String toString()
    {
        StringBuilder json = new StringBuilder();

        json.append("{\"name\":\""+name+"\",\"vectors\":[");

        for(int i = 0; i < pairs.size(); i++)
        {
            StarPair pair = pairs.get(i);
            json.append("[" + pair.star1 + "," + pair.star2 + "]");

            if(i != pairs.size()-1)
                json.append((","));
        }

        json.append("]}");

        return json.toString();
    }
}
