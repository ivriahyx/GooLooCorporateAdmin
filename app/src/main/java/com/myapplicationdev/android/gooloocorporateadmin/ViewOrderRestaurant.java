package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ViewOrderRestaurant extends AppCompatActivity {

    ListView lv_restaurant;
    ArrayList<Restaurant> al = new ArrayList<Restaurant>();
    RestaurantAdapter aa;

    private Toolbar toolbar;

    String email="";
    String companyname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");
        toolbar.setTitleTextColor(Color.WHITE);

        email = getIntent().getStringExtra("email");
        companyname = getIntent().getStringExtra("companyname");

        lv_restaurant = (ListView)findViewById(R.id.restaurantList);

        aa = new RestaurantAdapter(getApplicationContext(), R.layout.row_restaurant_list, al);
        lv_restaurant.setAdapter(aa);


        //getRestaurant
        RequestQueue queue = Volley.newRequestQueue(ViewOrderRestaurant.this);
        String urlcompany ="https://ivriah.000webhostapp.com/gooloo/gooloo/getRestaurantForOrders.php";
        // Request a json response from the provided URL.
        StringRequest restaurantRequest = new StringRequest(Request.Method.GET, urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ViewTeamActivty", "Response: "+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                Restaurant restaurant = new Restaurant(id,name);
                                al.add(restaurant);
                                aa.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            aa.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewTeamActivtyError", error.toString()+"");
                Toast toast = Toast.makeText(ViewOrderRestaurant.this, "No Restaurant found.", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(restaurantRequest);
//



        lv_restaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String restaurantname = al.get(position).getRestaurantName();
                Log.d("restaurantname",restaurantname);
                //intent restaurantname to ViewOrdersActivity (volley in frag activity)
                Intent i = new Intent(ViewOrderRestaurant.this,ViewOrdersActivity.class);
                i.putExtra("restaurantId",al.get(position).getRestaurantId());
                i.putExtra("restaurantname",restaurantname);
                i.putExtra("companyname",companyname);
                i.putExtra("email",email);
                startActivity(i);

            }
        });

    }
}
