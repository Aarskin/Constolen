package com.example.matthew.constellate;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class CallAPI extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String apiURL           = params[0];
        String apiEndpoint      = params[1];
        String requestMethod    = params[2];
        String requestParameter = params[3];
        String requestJSON      = params[4];

        StringBuilder response = new StringBuilder();

        try {
            // Open connection
            URL url = new URL(apiURL + apiEndpoint + requestParameter);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // We're gonna input and output (mostly)
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // We're gonna send and expect JSON (mostly)
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Set request method
            connection.setRequestMethod(requestMethod);

            // Write any JSON
            OutputStream os = connection.getOutputStream();
            os.write(requestJSON.getBytes());
            os.flush();

            // Get response
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            // Close connection
            connection.disconnect();
        } catch (Exception e ) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return response.toString();
    }

    protected void onPostExecute(String result) {

    }

}