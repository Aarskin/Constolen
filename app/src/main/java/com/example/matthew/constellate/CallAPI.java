package com.example.matthew.constellate;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class CallAPI extends AsyncTask<String, String, String> {

    ResponseListener responseListener;
    String token;

    public CallAPI(ResponseListener responseListener, String token) {
        this.responseListener = responseListener;
        this.token = token;
    }

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
            connection.setDoInput(true);
            if (requestMethod.equals("PUT") || requestMethod.equals("POST"))
                connection.setDoOutput(true);

            // If we've got a token, authenticate this request
            if (!token.equals(""))
                connection.setRequestProperty("Authorization", "Bearer " + token);

            // We're gonna send and expect JSON (mostly)
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Set request method
            connection.setRequestMethod(requestMethod);

            // Write any JSON
            if (requestMethod.equals("PUT") || requestMethod.equals("POST")) {
                OutputStream os = connection.getOutputStream();
                os.write(requestJSON.getBytes());
                os.flush();
            }

            // Get response code
            int status = connection.getResponseCode();

            Log.d("STATS", ""+status);

            // Get response
            InputStreamReader responseStream;
            if (status >= 400) {
                responseStream = new InputStreamReader(connection.getErrorStream());
            } else {
                responseStream = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader br = new BufferedReader(responseStream);

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            // Close connection
            connection.disconnect();
        } catch (Exception e ) {
            return e.getMessage();
        }

        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        responseListener.responseReceived(result);
    }

    public interface ResponseListener {
        void responseReceived(String response);
    }
}