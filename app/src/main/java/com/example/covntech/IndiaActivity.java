package com.example.covntech;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class IndiaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<dataFormat>> {

    public static final String LOG_TAG = IndiaActivity.class.getName();

    private TextView mEmptyStateTextView;
    private static final int data_LOADER_ID = 1;
    private static final String world_query_url =
            "https://api.rootnet.in/covid19-in/stats/latest";
    private dataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.listview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchView search = findViewById(R.id.search_view);

        // Find a reference to the {@link ListView} in the layout
        ListView dataListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        dataListView.setEmptyView(mEmptyStateTextView);

        ArrayList<dataFormat> world_list = new ArrayList<>();
        mAdapter = new dataAdapter(this, world_list);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        dataListView.setAdapter(mAdapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (world_list.contains(query)) {
                    mAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(IndiaActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (world_list.contains(newText)) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager.getInstance(this).initLoader(data_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No internet connection");
        }
    }

    @Override
    public Loader<List<dataFormat>> onCreateLoader(int id, Bundle args) {
        return new IndiaLoader(this, world_query_url);
    }

    @Override
    public void onLoadFinished(Loader<List<dataFormat>> loader, List<dataFormat> Indiainfo) {

        mAdapter.clear();
        ProgressBar hide = findViewById(R.id.loading_spinner);
        hide.setVisibility(View.GONE);

        // data set. This will trigger the ListView to update.
        if (Indiainfo != null && !Indiainfo.isEmpty()) {
            mAdapter.addAll(Indiainfo);
        } else {
            mEmptyStateTextView.setText("No Data Available");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<dataFormat>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}