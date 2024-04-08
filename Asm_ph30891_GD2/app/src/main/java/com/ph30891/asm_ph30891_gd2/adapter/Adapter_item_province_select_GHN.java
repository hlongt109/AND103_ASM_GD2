package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.model.Province;


import java.util.ArrayList;

public class Adapter_item_province_select_GHN extends BaseAdapter {
    private Context context;
    private ArrayList<Province> list;

    public Adapter_item_province_select_GHN(Context context, ArrayList<Province> list) {
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
        convertView = inflater.inflate(R.layout.item_province,parent,false);
        int stt = position+1;
        TextView numberLocation = convertView.findViewById(R.id.tvNumberProvince);
        TextView nameLocation = convertView.findViewById(R.id.tvNameProvince);
        numberLocation.setText(String.valueOf(stt));
        nameLocation.setText(list.get(position).getProvinceName());
        return convertView;
    }
}
