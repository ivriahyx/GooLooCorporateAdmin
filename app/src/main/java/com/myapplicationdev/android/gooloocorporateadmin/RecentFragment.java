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
import com.myapplicationdev.android.gooloocorporateadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class RecentFragment extends Fragment {

    ListView lv_recent;
    ArrayList<Order> al_recent = new ArrayList<Order>();
    OrderAdapter aa_recent;

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
        String email = activity.getEmail();
        Log.d("ActiveFragment","email: "+email);

        lv_recent = (ListView)rootView.findViewById(R.id.lv_recent);

        aa_recent = new OrderAdapter(getActivity(), R.layout.row_active_orders, al_recent);
        aa_recent.clear();
        lv_recent.setAdapter(aa_recent);

        //
        //getCompany
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //String urlcompany ="http://ivriah.000webhostapp.com/gooloo/gooloo/getCompanyname.php?email="+email;
        String urlcompany ="http://10.0.2.2/gooloo/getCompanyname.php?email="+email;

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
                            //String urlOrder ="http://ivriah.000webhostapp.com/gooloo/gooloo/retrieveRecentOrderForCustomer.php?company="+company_name;
                            String urlOrder ="http://10.0.2.2/gooloo/retrieveRecentOrderForCustomer.php?company="+company_name;

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

                                                    Log.d("order","id: "+orderId+" orderRef: "+order_ref+" firstName: "+firstName+" lastName: "+lastName+" finalprice: "+finalPrice);
                                                    Order order = new Order(Integer.parseInt(orderId),order_ref, firstName, lastName, Double.parseDouble(finalPrice));
                                                    al_recent.add(order);

                                                    //test array
                                                    for(int x =0; x < al_recent.size(); x++){
                                                        Log.d("itemarrayActive", ""+al_recent.get(x).getOrderRef());
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



        registerForContextMenu(lv_recent);

        //Upon Clicking on the item in the listview
        lv_recent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                al_recent.get(position).toString();
                Log.d("list",al_recent.get(position).toString());
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
        if (v.getId() == R.id.lv_recent) {
            getActivity().getMenuInflater().inflate(R.menu.menu_order, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_download) {

            return true;
        }else{
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //



}
