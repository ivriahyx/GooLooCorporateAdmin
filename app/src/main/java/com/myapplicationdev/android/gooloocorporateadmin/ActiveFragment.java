package com.myapplicationdev.android.gooloocorporateadmin;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.myapplicationdev.android.gooloocorporateadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class ActiveFragment extends Fragment {

    ListView lv_active;
    ArrayList<Order> al = new ArrayList<Order>();
    OrderAdapter aa;

    TextView tvOrderRef;
    EditText etSearch;
    ImageButton btnSearchOrder;

    String folderLocation,msg;

    ClipboardManager myClipboard;
    public ActiveFragment(){ }

    public static ActiveFragment newInstance(){
        ActiveFragment fragment = new ActiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_active, container, false);
        ViewOrdersActivity activity = (ViewOrdersActivity) getActivity();


        //file
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";

        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }
        //

        String email = activity.getEmail();
        String restaurantname = activity.getRestaurantName();
        final String restaurantId = activity.getRestaurantId();

        Log.d("ActiveFragment","email: "+email);
        Log.d("ActiveFragment","restaurantname: "+restaurantname);

        tvOrderRef = (TextView)rootView.findViewById(R.id.textViewOrderRef1);
        lv_active = (ListView)rootView.findViewById(R.id.lv_active);
        etSearch = (EditText)rootView.findViewById(R.id.etFilter);
        btnSearchOrder = (ImageButton) rootView.findViewById(R.id.btnSearch);

        aa = new OrderAdapter(getActivity(), R.layout.row_active_orders, al);
        aa.clear();
        lv_active.setAdapter(aa);

        registerForContextMenu(lv_active);


        //getCompany
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/getCompanyname.php?email="+email;

        // Request a json response from the provided URL.
        StringRequest stringcompanyRequest = new StringRequest(Request.Method.GET, urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ActiveFragmentLine112", response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            String id = jsonObject.getString("id");
                            Log.d("item", jsonObject.getString("id"));
                            String company_name = jsonObject.getString("company_name");
                            Log.d("item", jsonObject.getString("company_name"));

                            String[] company = {id, company_name};

                            //test array
                            for (int x = 0; x < company.length; x++) {
                                Log.d("itemarray", company[x]);
                            }

                            //getOrders
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            String urlOrder ="http://ivriah.000webhostapp.com/gooloo/gooloo/retrieveOrderForCustomer.php?company="+company_name+"&m_id="+restaurantId;
                            //String urlOrder ="http://10.0.2.2/gooloo/retrieveOrderForCustomer.php?company="+company_name;

                            // Request a json response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlOrder,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                if(response.toString()!=null){
                                                    Log.d("ActiveFragmentLine149",response.toString());
                                                    JSONArray jsonArray = new JSONArray(response.toString());
                                                    for (int i=0;i<jsonArray.length();i++){
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        String orderId = jsonObject.getString("id");
                                                        String order_ref = jsonObject.getString("order_ref");
                                                        String firstName = jsonObject.getString("first_name");
                                                        String lastName = jsonObject.getString("last_name");
                                                        String finalPrice = jsonObject.getString("final_price");
                                                        String customerId = jsonObject.getString("customer_id");
                                                        String bookingtime = jsonObject.getString("booking_time");

                                                        Log.d("order","id: "+orderId+" orderRef: "+order_ref+" firstName: "+firstName+" lastName: "+lastName+" finalprice: "+finalPrice);
                                                        Order order = new Order(Integer.parseInt(orderId),order_ref, firstName, lastName, Double.parseDouble(finalPrice),customerId,bookingtime);



                                                        al.add(order);


                                                        //test array
                                                        for(int x =0; x < al.size(); x++){
                                                            Log.d("itemarrayActive", ""+al.get(x).getOrderRef());
                                                        }

                                                    }

                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("ActiveFragment","Unsuccessful retriever");
                                            }
                                            aa.notifyDataSetChanged();

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("item", error.toString()+"");
                                    Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });

                            // Add the request to the RequestQueue.
                            queue.add(stringRequest);


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ActiveFragment", error.toString()+"");
                Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringcompanyRequest);


        //lv.setOnClick
        lv_active.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                int OrderId = al.get(position).getOrderId();
                Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
                intent.putExtra("OrderId",OrderId);
                intent.putExtra("firstName",al.get(position).getFirstName());
                intent.putExtra("lastName",al.get(position).getLastName());

                startActivity(intent);
            }
        });

        //btnSearch
        btnSearchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderRefSearch = etSearch.getText().toString();

                //getOrders
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String urlOrder ="https://ivriah.000webhostapp.com/gooloo/gooloo/getOrderByOrderId.php?order_ref="+orderRefSearch;

                // Request a json response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlOrder,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(response.equals("[[]]")) {
                                        al.clear();
                                    }else{
                                        Log.d("Response", response.toString());
                                        JSONArray jsonArray = new JSONArray(response.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String orderId = jsonObject.getString("id");
                                            String order_ref = jsonObject.getString("order_ref");
                                            String firstName = jsonObject.getString("first_name");
                                            String lastName = jsonObject.getString("last_name");
                                            String finalPrice = jsonObject.getString("final_price");
                                            String customerId = jsonObject.getString("customer_id");
                                            String bookingtime = jsonObject.getString("booking_time");

                                            Log.d("order", "id: " + orderId + " orderRef: " + order_ref + " firstName: " + firstName + " lastName: " + lastName + " finalprice: " + finalPrice);
                                            Order order = new Order(Integer.parseInt(orderId), order_ref, firstName, lastName, Double.parseDouble(finalPrice), customerId, bookingtime);

                                            al.clear();
                                            al.add(order);

                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("Unsuccessful","Unsuccessful retriever");
                                }
                                aa.notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("item", error.toString()+"");
                        Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

             return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    //Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_active) {
            getActivity().getMenuInflater().inflate(R.menu.menu_active_order, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_download) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final int index = info.position;
            final int orderId = al.get(index).getOrderId();
            Log.d("aaa",""+al.get(index).getOrderId());
            final String firstName = al.get(index).getFirstName();
            final String lastName = al.get(index).getLastName();
            final double finalPrice = al.get(index).getFinalPrice();
            final String orderRef = al.get(index).getOrderRef();
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/getOrderDetails.php?orderId=" + orderId;

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

                                    msg += "Order Reference Number: "+orderRef+"Order created by: " + firstName + " " + lastName + "\nItems: " + itemName + "\nUnit Price: $" + unitPrice + "\nTotal Price: $" + finalPrice+"\n\n";
                                }
                                createPDF(msg,orderRef+index);
                                //createPDF("Order created by: "+firstName+" "+lastName+"\nItems: "+itemName+"\nUnit Price: $"+unitPrice+"\nTotal Price: $"+finalPrice+"\n",""+orderRef);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast toast = Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG);
                                toast.show();
                                Log.d("ActiveOrderDetails","Unsuccessful");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ActiveOrderDetails", error.toString()+"");
                    Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(orderDetailsRequest);

            Toast.makeText(getActivity(), "Invoice Successfully Downloaded", Toast.LENGTH_SHORT).show();


            return true;
        }else if (id == R.id.action_submit_order){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final int index = info.position;
            final String orderRefNum = al.get(index).getOrderRef();
            final String customerId = al.get(index).getCustomerId();
            //Create the Dialog Builder
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());

            //set the dialog details
            myBuilder.setTitle("Submit Order");
            myBuilder.setMessage("Would you like to submit this order?");

            //User are unable to exit dialog simply by clicking anywhere outside the dialog box when set to false
            myBuilder.setCancelable(false);

            // Configure the 'positive' button for button dialog
            myBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/submitOrder.php?orderRef="+orderRefNum;
                    //String url ="http://10.0.2.2/gooloo/submitOrder.php?orderRef="+orderRefNum+"&customerId="+customerId;

                    // Request a json response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });

// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            });
            // Configure the 'neutral' button
            myBuilder.setNeutralButton("Cancel", null);

            //create and display dialog
            AlertDialog myDialog = myBuilder.create();
            myDialog.show();

            return true;
        }
        else{
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //

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

            Chunk mOrderIdChunk = new Chunk("Order No: ");
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
