package com.myapplicationdev.android.gooloocorporateadmin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order>{

    private ArrayList<Order> orders;
    private Context context;

    TextView tvOrderRef;

    public OrderAdapter(Context context, int resource, ArrayList<Order> orders){
        super(context, resource, orders);
        this.orders = orders;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_active_orders, parent, false);

        tvOrderRef = (TextView)rowView.findViewById(R.id.textViewOrderRef1);

        Order currentItem = orders.get(position);

        tvOrderRef.setText(currentItem.getOrderRef()+"\nBooking time: "+currentItem.getDatetime());

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.rgb(254,245,245));
        } else {
            rowView.setBackgroundColor(Color.rgb(235,74,74));
        }
        return rowView;
    }
}
