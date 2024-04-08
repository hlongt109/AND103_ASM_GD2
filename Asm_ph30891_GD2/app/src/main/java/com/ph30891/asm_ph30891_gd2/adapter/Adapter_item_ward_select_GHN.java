package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.model.Ward;

import java.util.ArrayList;

public class Adapter_item_ward_select_GHN extends BaseAdapter {
    private Context context;
    private ArrayList<Ward> list;

    public Adapter_item_ward_select_GHN(Context context, ArrayList<Ward> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_ward,parent,false);
        TextView numberLocation = convertView.findViewById(R.id.tvNumberLocation);
        TextView nameLocation = convertView.findViewById(R.id.tvNameLocation);
        numberLocation.setText(String.valueOf(position +1));
        nameLocation.setText(list.get(position).getWardName());
        return convertView;
    }
}
