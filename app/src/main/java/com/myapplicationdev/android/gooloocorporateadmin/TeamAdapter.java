package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team>{

    private ArrayList<Team> teamsArrayList;
    private Context context;

    TextView tvName;
    Button imageButtonDelete;

    public TeamAdapter(Context context, int resource, ArrayList<Team> teamsArrayList){
        super(context, resource, teamsArrayList);
        this.teamsArrayList = teamsArrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_team, parent, false);

        tvName = (TextView)rowView.findViewById(R.id.tvTeamName);
        imageButtonDelete = (Button) rowView.findViewById(R.id.imageButtonDelete);

        Team currentItem = teamsArrayList.get(position);

        tvName.setText(currentItem.getFirstName()+" "+currentItem.getLastName());

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.rgb(254,245,245));
        } else {
            rowView.setBackgroundColor(Color.rgb(235,74,74));
        }

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete button", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
