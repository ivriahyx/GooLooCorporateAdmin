package com.myapplicationdev.android.gooloocorporateadmin;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class RecentFragment extends Fragment {

    ListView lv_recent;
    ArrayList<OrderRecent> al_recent = new ArrayList<OrderRecent>();
    OrderRecentAdapter aa_recent;

    String folderLocation;

    ClipboardManager myClipboard;
    public static RecentFragment newInstance(){
        RecentFragment fragment = new RecentFragment();
        return fragment;
    }
    public RecentFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
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
        Log.d("Email","email: "+email);

        String restaurantname = activity.getRestaurantName();
        final String restaurantId = activity.getRestaurantId();

        lv_recent = (ListView)rootView.findViewById(R.id.lv_recent);

        aa_recent = new OrderRecentAdapter(getActivity(), R.layout.row_recent_orders, al_recent);
        aa_recent.clear();
        lv_recent.setAdapter(aa_recent);

        registerForContextMenu(lv_recent);

        //
        //getCompany
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/getCompanyname.php?email="+email;
        //String urlcompany ="http://10.0.2.2/gooloo/getCompanyname.php?email="+email;

        // Request a json response from the provided URL.
        StringRequest stringcompanyRequest = new StringRequest(Request.Method.GET, urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RecentFragment", response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            //Log.d("ActiveFragment","JSONObject response : "+ jsonArray.getJSONObject(0));
                            String id = jsonObject.getString("id");
                            String company_name = jsonObject.getString("company_name");

                            String[] company = {id, company_name};

                            //test array
                            for (int x = 0; x < company.length; x++) {
                                Log.d("itemarray_recent", company[x]);
                            }

                            //getOrders
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            String urlOrder ="http://ivriah.000webhostapp.com/gooloo/gooloo/retrieveRecentOrderForCustomer.php?company="+company_name+"&m_id="+restaurantId;
                            ///String urlOrder ="http://10.0.2.2/gooloo/retrieveRecentOrderForCustomer.php?company="+company_name;

                            // Request a json response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlOrder,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                if(response.toString()!=null){
                                                Log.d("RecentFragment",response.toString());
                                                JSONArray jsonArray = new JSONArray(response.toString());
                                                for (int i=0;i<jsonArray.length();i++){
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    Log.d("RecentFragment","JSONObject response : "+ response.toString());
                                                    String orderId = jsonObject.getString("id");
                                                    String order_ref = jsonObject.getString("order_ref");
                                                    String firstName = jsonObject.getString("first_name");
                                                    String lastName = jsonObject.getString("last_name");
                                                    String finalPrice = jsonObject.getString("final_price");
                                                    String customerId = jsonObject.getString("customer_id");
                                                    String bookingtime = jsonObject.getString("booking_time");

                                                    Log.d("order","id: "+orderId+" orderRef: "+order_ref+" firstName: "+firstName+" lastName: "+lastName+" finalprice: "+finalPrice);
                                                    OrderRecent order_recent = new OrderRecent(Integer.parseInt(orderId),order_ref, firstName, lastName, Double.parseDouble(finalPrice),customerId,bookingtime);
                                                    al_recent.add(order_recent);

                                                    //test array
                                                    for(int x =0; x < al_recent.size(); x++){
                                                        Log.d("itemarrayRecent", ""+al_recent.get(x).getOrderRef());
                                                    }
                                                }
                                            }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            aa_recent.notifyDataSetChanged();
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
                Log.d("RecentFragment", error.toString()+"");
                Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringcompanyRequest);





        /*//Upon Clicking on the item in the listview
        lv_recent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                al_recent.get(position).toString();
                Log.d("list",al_recent.get(position).toString());
            }
        });
*/
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    //Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_recent) {
            getActivity().getMenuInflater().inflate(R.menu.menu_order, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_recent_download) {
            AdapterView.AdapterContextMenuInfo info_recent = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final int index_recent = info_recent.position;
            Log.d("rrr",""+al_recent.get(index_recent).getOrderId());
            final int orderId = al_recent.get(index_recent).getOrderId();
            final String firstName = al_recent.get(index_recent).getFirstName();
            final String lastName = al_recent.get(index_recent).getLastName();
            final double finalPrice = al_recent.get(index_recent).getFinalPrice();
            final String orderRef = al_recent.get(index_recent).getOrderRef();
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url ="http://ivriah.000webhostapp.com/gooloo/gooloo/getOrderDetails.php?orderId=" + orderId;

            // Request a json response from the provided URL.
            StringRequest orderDetailsRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray jsonArray = new JSONArray(response.toString());
                                Log.d("RecentOrderDetails","JSONObj response : "+response);

                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String orderDetailId = jsonObject.getString("id");
                                    String itemName = jsonObject.getString("item_name");
                                    String unitPrice = jsonObject.getString("price");
                                    createPDF("Order created by: "+firstName+" "+lastName+"\nItems: "+itemName+"\nUnit Price: $"+unitPrice+"\nTotal Price: $"+finalPrice+"\n",""+orderRef);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                               /* Toast toast = Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG);
                                toast.show();*/
                                Log.d("RecentOrderDetails","Unsuccessful");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("RecentOrderDetails", error.toString()+"");
                    Toast toast = Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(orderDetailsRequest);


            //openPDF(orderRef);
            Toast.makeText(getActivity(), "Invoice Successfully Downloaded", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //

    public void createPDF(String msg,String filename) {
        //write
        //Code for file writing
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
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

            document.addHeader("ss", "s");

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
