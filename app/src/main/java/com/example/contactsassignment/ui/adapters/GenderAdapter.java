package com.example.contactsassignment.ui.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;


public class GenderAdapter extends ArrayAdapter<String> {

    private static final String[] DEFAULT_GENDERS = {"Male", "Female"};


    public GenderAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line, DEFAULT_GENDERS);
    }


}


