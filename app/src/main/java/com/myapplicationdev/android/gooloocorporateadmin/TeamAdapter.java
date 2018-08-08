package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_team, parent, false);

        tvName = (TextView)rowView.findViewById(R.id.tvTeamName);
        imageButtonDelete = (Button) rowView.findViewById(R.id.imageButtonDelete);

        Team currentItem = teamsArrayList.get(position);

        tvName.setText(currentItem.getFirstName()+" "+currentItem.getLastName()+"\n"+currentItem.getEmail());

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.rgb(254,245,245));
        } else {
            rowView.setBackgroundColor(Color.rgb(235,74,74));
        }

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Delete team member (get staff id)
               Log.d("emailDelete", teamsArrayList.get(position).getEmail());
                RequestQueue queue = Volley.newRequestQueue(context);
                String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/deleteStaff.php?email="+ teamsArrayList.get(position).getEmail();
                //String urlcompany ="http://10.0.2.2/gooloo/getTeamMembers.php?company="+companyname;
                // Request a json response from the provided URL.
                StringRequest teamRequest = new StringRequest(Request.Method.GET, urlcompany,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("ViewTeamActivty", "Response: "+response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean status = jsonObject.getBoolean("success");
                                    String msg = jsonObject.getString("message");
                                    Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ViewTeamActivtyError", error.toString()+"");
                        Toast.makeText(context, "Delete not successful", Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the RequestQueue.

                queue.add(teamRequest);

            }
        });

        return rowView;
    }
}
