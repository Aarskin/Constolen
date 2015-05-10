package com.example.matthew.constellate;

import android.app.Application;
import android.content.Context;

public class ConstellateGlobals extends Application {
    public  String API_URL;
    public  String AUTHENTIACATION_ENDPOINT;
    public  String USER_ENDPOINT;
    public  String CONSTELLATION_ENDPOINT;
    public  String CONSTELLATION_BY_STAR_ENDPOINT;

    public User authenticatedUser = null;
    public Context context = null;
}
