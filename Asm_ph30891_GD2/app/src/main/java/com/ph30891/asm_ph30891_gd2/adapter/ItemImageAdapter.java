package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.databinding.ItemImageAddUpBinding;


import java.util.ArrayList;

public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.myViewHolder>{
    private Context context;
    private ArrayList<String> list;

    public ItemImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageAddUpBinding binding = ItemImageAddUpBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String url = list.get(position);
        holder.setData(url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemImageAddUpBinding binding;
        myViewHolder(ItemImageAddUpBinding itemImageBinding){
            super(itemImageBinding.getRoot());
            binding = itemImageBinding;
        }
        void setData(String url){
            Glide.with(context).load(url).error(R.drawable.image).into(binding.imageAddUp);
        }
    }
}
