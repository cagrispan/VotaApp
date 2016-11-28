package com.example.cagri.votaapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cagri on 27/11/2016.
 */

public class ListCell extends ArrayAdapter<Candidate> {
    private Activity context;

    private final ArrayList<Candidate> candidates;

    public ListCell(Activity context, ArrayList<Candidate> candidates){
        super(context, R.layout.list_cell, candidates);
        this.context = context;
        this.candidates = candidates;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.list_cell, null, true);

        ImageView image = (ImageView) row.findViewById(R.id.image);
        TextView name = (TextView) row.findViewById(R.id.name);
        TextView party = (TextView) row.findViewById(R.id.party);

        image.setImageBitmap(candidates.get(position).getImage());
        name.setText(candidates.get(position).getName());
        party.setText(candidates.get(position).getParty());

        return row;
    }
}