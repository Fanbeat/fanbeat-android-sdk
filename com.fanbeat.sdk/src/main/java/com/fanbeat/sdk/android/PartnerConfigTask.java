package com.fanbeat.sdk.android;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tony on 6/8/16.
 */
class PartnerConfigTask extends AsyncTask<String, Void, String> {
    static final String BASE_URL = "https://s3.amazonaws.com/ingame-prod/partners/";

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 1) {
            String dataUrl = BASE_URL + params[0] + ".json";

            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(dataUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-length", "0");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(500);
                connection.setReadTimeout(500);
                connection.connect();
                int status = connection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();

                }

            } catch (MalformedURLException e) {
                Log.e("FanBeat SDK", e.toString());
            } catch (IOException e) {
                Log.e("FanBeat SDK", e.toString());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String data) {
        PartnerConfig config = null;
        if (data != null) {
            config = new Gson().fromJson(data, PartnerConfig.class);
        }

        FanBeat.getInstance().setPartnerConfig(config);
    }
}
