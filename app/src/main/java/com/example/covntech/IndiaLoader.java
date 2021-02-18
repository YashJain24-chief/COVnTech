package com.example.covntech;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class IndiaLoader extends AsyncTaskLoader<List<dataFormat>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = IndiaLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public IndiaLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<dataFormat> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<dataFormat> Indiadata = Query.fetchIndiaData(mUrl);
        return Indiadata;
    }
}

