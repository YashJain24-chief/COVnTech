package com.example.covntech;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class worldLoader extends AsyncTaskLoader<List<dataFormat>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = worldLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public worldLoader(Context context, String url) {
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
        List<dataFormat> worlddata = Query.fetchWorldData(mUrl);
        return worlddata;
    }
}

