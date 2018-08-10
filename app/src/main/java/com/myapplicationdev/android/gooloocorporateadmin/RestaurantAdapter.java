package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantAdapter extends ArrayAdapter<Restaurant>{

    private ArrayList<Restaurant> restaurantArrayList;
    private Context context;

    TextView tvName,tvId;

    public RestaurantAdapter(Context context, int resource, ArrayList<Restaurant> restaurantArrayList){
        super(context, resource, restaurantArrayList);
        this.restaurantArrayList = restaurantArrayList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_restaurant_list, parent, false);

        tvName = (TextView)rowView.findViewById(R.id.tvRestaurantName);
        tvId = (TextView)rowView.findViewById(R.id.tvRestaurantId);

        Restaurant currentItem = restaurantArrayList.get(position);

        tvName.setText(currentItem.getRestaurantName());
        tvId.setText(currentItem.getRestaurantId());

        return rowView;
    }
}
