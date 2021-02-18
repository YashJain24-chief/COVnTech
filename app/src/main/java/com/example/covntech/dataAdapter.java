package com.example.covntech;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Custom adapter class
public class dataAdapter extends ArrayAdapter<dataFormat> {

    public dataAdapter(Activity context, ArrayList<dataFormat> Word) {

        super(context, 0, Word);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check if the current view is reused else inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //get the object located at position
        dataFormat word_item = getItem(position);

        TextView countryName = listItemView.findViewById(R.id.country_id);
        countryName.setText(word_item.getmCountry());

        TextView confirmed = listItemView.findViewById(R.id.Confirmed_data);
        confirmed.setText(word_item.getmConfirmed() + " ");

        TextView recovered = listItemView.findViewById(R.id.Recovered_data);
        recovered.setText(word_item.getmRecovered() + "");

        TextView death = listItemView.findViewById(R.id.Death_data);
        death.setText(word_item.getmDeath() + "");
        return listItemView;
    }
}