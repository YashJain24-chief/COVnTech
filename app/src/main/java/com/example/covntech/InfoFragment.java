package com.example.covntech;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class InfoFragment extends Fragment {

    View rootView;
    long totalConfirmed;
    long totalRecovered;
    long totalDeath;

    public static final String LOG_TAG = "";

    private static final String world_query_url =
            "https://api.covid19api.com/summary";

    private static final String projectLink = "https://colab.research.google.com/drive/1cqLUb5BuZxl9roSt4WVwXGaG-OjtB45L?usp=sharing";

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        detailedAsyncTask task = new detailedAsyncTask();
        task.execute();

        TextView mforInfo = rootView.findViewById(R.id.forInfo);
        Button visualize = rootView.findViewById(R.id.visualize);
        mforInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.webmd.com/lung/coronavirus#7-13");
                Intent moreInfo = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(moreInfo);
            }
        });
        visualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri driveLink = Uri.parse(projectLink);
                Intent moreInfo = new Intent(Intent.ACTION_VIEW, driveLink);
                startActivity(moreInfo);

            }
        });
        return rootView;
    }

    private class detailedAsyncTask extends AsyncTask<URL, Void, dataFormat> {

        @Override
        protected dataFormat doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(world_query_url);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            dataFormat data = extractFeatureFromJson(jsonResponse);

            return data;
        }

        @Override
        protected void onPostExecute(dataFormat data) {
            if (data == null) {
                return;
            }
            InfoFragmentupdate();
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private dataFormat extractFeatureFromJson(String dataJSON) {
            try {
                JSONObject root = new JSONObject(dataJSON);
                JSONObject worldWide = root.getJSONObject("Global");
                totalConfirmed = worldWide.getLong("TotalConfirmed");
                totalRecovered = worldWide.getLong("TotalRecovered");
                totalDeath = worldWide.getLong("TotalDeaths");

                return new dataFormat(totalConfirmed, totalRecovered, totalDeath);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing JSON results", e);
            }
            return null;
        }
    }

    private void InfoFragmentupdate() {

        TextView confirmedTextView = rootView.findViewById(R.id.confirmed_id);
        confirmedTextView.setText(String.format("%.2fM", totalConfirmed / 1000000.0) + "");

        TextView recoveredTextView = rootView.findViewById(R.id.recovered_id);
        recoveredTextView.setText(String.format("%.2fM", totalRecovered / 1000000.0) + "");

        TextView deathTextView = rootView.findViewById(R.id.death_id);
        deathTextView.setText(String.format("%.2fM", totalDeath / 1000000.0) + "");
    }
}