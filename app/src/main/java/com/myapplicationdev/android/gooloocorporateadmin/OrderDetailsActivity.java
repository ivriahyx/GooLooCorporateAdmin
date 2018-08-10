package com.myapplicationdev.android.gooloocorporateadmin;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView tvLabel,tvTotal;
    ListView lv_order_detail;
    ArrayList<String> al_order_detail = new ArrayList<String>();
    ArrayAdapter<String> aa_order_detail;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");
        mToolbar.setTitleTextColor(Color.WHITE);



        tvLabel = (TextView)findViewById(R.id.tvDetail);
        tvTotal = (TextView)findViewById(R.id.tvTotal);

        lv_order_detail = (ListView)findViewById(R.id.lv_orderDetail);

        aa_order_detail = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,al_order_detail);
        lv_order_detail.setAdapter(aa_order_detail);
        //upon clicking the order ref, system intent to this page
        //get orderId/order ref to get order detail in this page

        final int orderId = getIntent().getIntExtra("OrderId",0);
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        tvLabel.setText("Ordered by: "+firstName+" "+lastName);

        Log.d("OrderDetailId",orderId+"");
//getOrderDetails
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/getOrderDetails.php?orderId=" + orderId;

        // Request a json response from the provided URL.
        StringRequest orderDetailsRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            double total = 0.0;
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("OrderDetails","JSONObj response : "+response);

                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String orderDetailId = jsonObject.getString("id");
                                String itemName = jsonObject.getString("item_name");
                                String unitPrice = jsonObject.getString("price");
                                String item_image = jsonObject.getString("item_image");
                                String qnty = jsonObject.getString("count");

                                aa_order_detail.add("Order Id: "+orderDetailId+"\nItem: "+itemName+"\nUnit Price: $"+unitPrice+"\n"+"Quantity: "+qnty);
                                double price = Double.parseDouble(unitPrice) * Double.parseDouble(qnty);
                                total = total + price;
                            }
                            tvTotal.setText("Total: $"+total);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderDetailsActivity.this, "No Order Details found.", Toast.LENGTH_LONG).show();
                            Log.d("OrderDetails","Unsuccessful");
                        }
                        aa_order_detail.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OrderDetails", error.toString()+"");
                Toast.makeText(OrderDetailsActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(orderDetailsRequest);




    }

}
