package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_members);
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

                RequestQueue queue = Volley.newRequestQueue(AddTeamMembers.this);
                //String urlTeam = "http://ivriah.000webhostapp.com/gooloo/gooloo/addTeamMember.php";
                String urlTeam = "http://10.0.2.2/gooloo/addTeamMember.php";
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
                        params.put("CompanyName", getIntent().getStringExtra("companyname"));
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

                Intent intent = new Intent(AddTeamMembers.this,ViewTeamActivity.class);
                startActivity(intent);
                Toast.makeText(AddTeamMembers.this, "Successfully Created.", Toast.LENGTH_SHORT).show();
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
