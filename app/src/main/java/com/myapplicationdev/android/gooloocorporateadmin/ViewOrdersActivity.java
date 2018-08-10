package com.myapplicationdev.android.gooloocorporateadmin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String email="";
    String companyname="";
    String restaurantname="";
    String restaurantId="";
    String msg="";
    Double total=0.0;

    ViewPagerAdapter mDemoCollectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        int permissionCheck_Write= ContextCompat.checkSelfPermission(
                ViewOrdersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_Write != PermissionChecker.PERMISSION_GRANTED){
            Toast.makeText(ViewOrdersActivity.this, "Permission not granted.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(ViewOrdersActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            finish();
        }

        mDemoCollectionPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        email = getIntent().getStringExtra("email");
        companyname = getIntent().getStringExtra("companyname");
        restaurantname = getIntent().getStringExtra("restaurantname");
        restaurantId = getIntent().getStringExtra("restaurantId");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(mDemoCollectionPagerAdapter);

        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    public String getEmail() {
        Log.d("ViewOrdersActivity",""+email);
        return email;
    }
    public String getCompanyname(){
        Log.d("ViewOrdersActivity",""+companyname);
        return companyname;
    }
    public String getRestaurantId(){
        Log.d("ViewOrdersActivity",""+restaurantId);
        return restaurantId;
    }
    public String getRestaurantName(){
        Log.d("ViewOrdersActivity",""+restaurantname);
        return restaurantname;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ActiveFragment(), "Active");
        adapter.addFragment(new RecentFragment(), "Recent");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_order, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alldownload) {
            RequestQueue queue = Volley.newRequestQueue(ViewOrdersActivity.this);
            String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/getOrderDetails.php?orderId="+"&m_id="+restaurantId;

            // Request a json response from the provided URL.
            StringRequest orderDetailsRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                Log.d("ActiveOrderDetails", "JSONObj response : " + response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String orderDetailId = jsonObject.getString("id");
                                    String itemName = jsonObject.getString("item_name");
                                    String unitPrice = jsonObject.getString("price");
                                    String create_time = jsonObject.getString("create_time");

                                    msg += "\n"+"Order created by: " + email +" on "+create_time+ "\nItems: " + itemName + "\nUnit Price: $" + unitPrice +"\n\n";
                                    double price = Double.parseDouble(unitPrice);
                                    total = total + price;
                                }
                                createPDF(msg+"\nTotal:$"+total,restaurantname);
                                Toast.makeText(ViewOrdersActivity.this, "Invoice Downloaded Successfully.", Toast.LENGTH_SHORT).show();
                                //createPDF("Order created by: "+firstName+" "+lastName+"\nItems: "+itemName+"\nUnit Price: $"+unitPrice+"\nTotal Price: $"+finalPrice+"\n",""+orderRef);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast toast = Toast.makeText(ViewOrdersActivity.this, "Unsuccessful", Toast.LENGTH_LONG);
                                toast.show();
                                Log.d("ActiveOrderDetails","Unsuccessful");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ActiveOrderDetails", error.toString()+"");
                    Toast toast = Toast.makeText(ViewOrdersActivity.this, ""+error.toString(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(orderDetailsRequest);
            return true;
        }else{
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void createPDF(String msg,String filename){
        //write
        //Code for file writing
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Download";
        File f = new File(path, filename+".pdf");


        try {
            Document document = new Document();
            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(f, false));

            // Open to write
            document.open();
            //Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("GooLoo");
            document.addCreator("GooLoo");

            document.addHeader("ss","s");

            Chunk mOrderDetailsTitleChunk = new Chunk("Order Details");
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);


            document.add(mOrderDetailsTitleParagraph);

            Chunk mOrderIdChunk = new Chunk("Orders from ");
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);
            document.add(new Paragraph(filename));

            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            document.add(new Chunk(lineSeparator));

            document.add(new Paragraph(msg));

            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();
            Log.d("sss", "done");

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

}
