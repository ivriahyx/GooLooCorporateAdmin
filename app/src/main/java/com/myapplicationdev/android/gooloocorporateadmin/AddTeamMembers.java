package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class AddTeamMembers extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    EditText etFirstName, etLastName,etEmail,etMobile;
    Button btnAddTeam;
    Spinner spinner;
    String role;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_members);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");
        toolbar.setTitleTextColor(Color.WHITE);


        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etMobile = (EditText)findViewById(R.id.etMobile);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        btnAddTeam = (Button)findViewById(R.id.btnAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();

                //Check email exists as customer
                RequestQueue queue = Volley.newRequestQueue(AddTeamMembers.this);
                String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/checkMember.php?email=" + email;
                //String url ="http://10.0.2.2/gooloo/LoginCorporateAdmin.php?email=" + email;

                // Request a json response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());
                                    Log.d("MainActivity","JSONObj response : "+response);
                                    boolean authorized = jsonObject.getBoolean("authorized");

                                    if(authorized==true){
                                        //add member to company staff table
                                        RequestQueue queue = Volley.newRequestQueue(AddTeamMembers.this);
                                        String urlTeam = "http://ivriah.000webhostapp.com/gooloo/gooloo/addTeamMember.php";
                                        //String urlTeam = "http://10.0.2.2/gooloo/addTeamMember.php";
                                        StringRequest postRequest = new StringRequest(Request.Method.POST, urlTeam,
                                                new Response.Listener<String>()
                                                {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // response
                                                        Log.d("Response", response);
                                                    }
                                                },
                                                new Response.ErrorListener()
                                                {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // error
                                                        Log.d("Error.Response", error.toString());
                                                    }
                                                }
                                        ) {
                                            @Override
                                            protected Map<String, String> getParams()
                                            {
                                                Map<String, String>  params = new HashMap<String, String>();
                                                params.put("FirstName", etFirstName.getText().toString());
                                                params.put("LastName", etLastName.getText().toString());
                                                params.put("CompanyName", getIntent().getStringExtra("teamcompanyname"));
                                                params.put("email",  etEmail.getText().toString());
                                                String mobileNum;
                                                if(etMobile.getText().toString().equals("")){
                                                    mobileNum = "NULL";
                                                }else{
                                                    mobileNum = etMobile.getText().toString();
                                                }
                                                params.put("Mobile", etMobile.getText().toString());

                                                String role = spinner.getSelectedItem().toString().toLowerCase();
                                                Log.d("Spinner",role);
                                                params.put("role",role);

                                                return params;
                                            }
                                        };
                                        queue.add(postRequest);

                                       finish();
                                        Toast.makeText(AddTeamMembers.this, "Successfully Created.", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(AddTeamMembers.this, "Member not added. Please have member create a account first.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Log.d("MainActivity","Unsuccessful");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivityLogIn", error.toString()+"");
                        Toast toast = Toast.makeText(AddTeamMembers.this, ""+error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);



            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String item = arg0.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }




}
