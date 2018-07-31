package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class ViewTeamActivity extends AppCompatActivity {
    private Toolbar toolbar;

    Button btnAdd;
    TextView tvTeamCompanyName;

    ListView lv_team;
    ArrayList<String> al;
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");

        btnAdd = (Button)findViewById(R.id.btnAdd);
        tvTeamCompanyName = (TextView)findViewById(R.id.tvTeamCompanyName);

        lv_team = (ListView)findViewById(R.id.lv_team);
        al = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,al);

        String companyname = getIntent().getStringExtra("companyname");
        tvTeamCompanyName.setText(companyname);
        //getTeam
        RequestQueue queue = Volley.newRequestQueue(ViewTeamActivity.this);
        String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/getTeamMembers.php?company="+companyname;
        //String urlcompany ="http://10.0.2.2/gooloo/getTeamMembers.php?company="+companyname;
        Log.d("URL company name",""+companyname);
        // Request a json response from the provided URL.
        StringRequest stringteamRequest = new StringRequest(Request.Method.GET, urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ViewTeamActivty", "Response: "+response);
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d("","jsonArray: "+jsonArray);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String firstname = jsonObject.getString("first_name");
                                    Log.d("item", jsonObject.getString("first_name"));
                                    String lastname = jsonObject.getString("last_name");
                                    Log.d("item", jsonObject.getString("last_name"));

                                    String[] company = {firstname,lastname};

                                    al.add(company[0]+" "+company[1]);

                                    aa = new ArrayAdapter<String>(ViewTeamActivity.this, android.R.layout.simple_list_item_1, al);
                                    lv_team.setAdapter(aa);

                                    //test array
                                    for (int x = 0; x < company.length; x++) {
                                        Log.d("itemarray", company[x]);
                                    }
                                }

                                Log.d("Arraylist",""+al.size());



                        } catch (JSONException e) {
                            e.printStackTrace();
                            aa.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewTeamActivtyError", error.toString()+"");
                Toast toast = Toast.makeText(ViewTeamActivity.this, ""+error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringteamRequest);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTeamActivity.this,AddTeamMembers.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String companyname = getIntent().getStringExtra("companyname");
        tvTeamCompanyName.setText(companyname);
    }
}
