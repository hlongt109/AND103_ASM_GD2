package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.model.District;

import java.util.ArrayList;

public class Adapter_item_district_select_GHN extends BaseAdapter {
    private Context context;
    private ArrayList<District>list;

    public Adapter_item_district_select_GHN(Context context, ArrayList<District> list) {
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
        convertView = inflater.inflate(R.layout.item_district,parent,false);
        TextView numberLocation = convertView.findViewById(R.id.tvNumberDistrict);
        TextView nameLocation = convertView.findViewById(R.id.tvNameDistrict);
        numberLocation.setText(String.valueOf(position + 1));
        nameLocation.setText(list.get(position).getDistrictName());
        return convertView;
    }
}
