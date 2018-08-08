package com.myapplicationdev.android.gooloocorporateadmin;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText etEmail;
    Button btnLogin;
    String email = "";
    String id = "";
    String objEmail="";
    String lastName = "";
    String firstName = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");
        mToolbar.setTitleTextColor(Color.WHITE);

        etEmail = (EditText)findViewById(R.id. edtEmail);
        btnLogin = (Button)findViewById(R.id. btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/LoginCorporateAdmin.php?email=" + email;
                //String url ="http://10.0.2.2/gooloo/LoginCorporateAdmin.php?email=" + email;

                // Request a json response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());
                                    Log.d("MainActivity","JSONObj response : "+response);

                                    id = jsonObject.getString("id");
                                    Log.d("item", jsonObject.getString("id"));
                                    objEmail = jsonObject.getString("email");
                                    Log.d("item", jsonObject.getString("email"));
                                    lastName = jsonObject.getString("last_name");
                                    Log.d("item", jsonObject.getString("last_name"));
                                    firstName = jsonObject.getString("first_name");
                                    Log.d("item", jsonObject.getString("first_name"));
                                    String [] user = {id,objEmail, lastName, firstName};

                                    //test array
                                    for(int x =0; x < user.length; x++){
                                        Log.d("item", user[x]);
                                    }

                                    if(objEmail.equals(email)){
                                        Intent j = new Intent(MainActivity.this, HomeActivity.class);
                                        j.putExtra("user", user);
                                        Log.d("user", user.length+"");
                                        startActivity(j);
                                    }else{
                                        Toast toast = Toast.makeText(MainActivity.this, "email is incorrect", Toast.LENGTH_LONG);
                                        toast.show();
                                        Log.d("MainActivity","Unsuccessful");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast toast = Toast.makeText(MainActivity.this, "Incorrect Email", Toast.LENGTH_LONG);
                                    toast.show();
                                    Log.d("MainActivity","Unsuccessful");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivityLogIn", error.toString()+"");
                        Toast toast = Toast.makeText(MainActivity.this, ""+error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etEmail.setText("");
    }
}
