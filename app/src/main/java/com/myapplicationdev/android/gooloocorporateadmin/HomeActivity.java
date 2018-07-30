package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class HomeActivity extends AppCompatActivity {

    TextView tvFirstName, tvLastName,tvCompanyName;
    ImageView ivProfile;

    String email="";
    String firstName="";
    String lastName="";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvFirstName = (TextView)findViewById(R.id.tvFirstName);
        tvLastName = (TextView)findViewById(R.id.tvLastName);
        tvCompanyName = (TextView)findViewById(R.id.tvCompanyName);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        // Get intents
        Intent intent = getIntent();
        final String user[] = intent.getStringArrayExtra("user");

        //Extract data from user
        firstName = user[3];
        lastName = user[2];
        email = user[1];
        tvFirstName.setText(" "+firstName);
        tvLastName.setText(" "+lastName);

//getCompany
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        //String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/getCompanyname.php?email="+email;
        String urlcompany ="http://10.0.2.2/gooloo/getCompanyname.php?email="+email;

        // Request a json response from the provided URL.
        StringRequest stringcompanyRequest = new StringRequest(Request.Method.GET, urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("HomeActivitygetCompany", response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            //Log.d("ActiveFragment","JSONObject response : "+ jsonArray.getJSONObject(0));
                            String id = jsonObject.getString("id");
                            Log.d("item", jsonObject.getString("id"));
                            String company_name = jsonObject.getString("company_name");
                            Log.d("item", jsonObject.getString("company_name"));

                            String[] company = {id, company_name};

                            //test array
                            for (int x = 0; x < company.length; x++) {
                                Log.d("itemarray", company[x]);
                            }
                            tvCompanyName.setText(" "+company_name);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivityLogIn", error.toString()+"");
                Toast toast = Toast.makeText(HomeActivity.this, ""+error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringcompanyRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ViewTeam) {
            Intent j = new Intent(HomeActivity.this,ViewTeamActivity.class);
            j.putExtra("email",""+email);
            j.putExtra("companyname",""+tvCompanyName.getText().toString());
            startActivity(j);
            return true;
        }else   if (id == R.id.action_Orders) {
            Intent j = new Intent(HomeActivity.this,ViewOrdersActivity.class);
            j.putExtra("email",""+email);
            j.putExtra("companyname",""+tvCompanyName.getText().toString());
            startActivity(j);
            return true;
        }else{
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
