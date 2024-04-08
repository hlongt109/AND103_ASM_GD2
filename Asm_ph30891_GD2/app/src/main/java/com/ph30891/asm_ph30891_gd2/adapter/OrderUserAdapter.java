package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.databinding.ItemOrderUserBinding;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderUserAdapter extends RecyclerView.Adapter<OrderUserAdapter.myViewHolder>{
    private Context context;
    private ArrayList<Order> list;
    private HttpRequest httpRequest;
    private HandleOnClickOrder handleOnClickOrder;

    public void showChooseHandle(HandleOnClickOrder handleOnClickOrder){
        this.handleOnClickOrder = handleOnClickOrder;
    }

    public OrderUserAdapter(Context context, ArrayList<Order> list, HttpRequest httpRequest) {
        this.context = context;
        this.list = list;
        this.httpRequest = httpRequest;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderUserBinding binding = ItemOrderUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
         Order order = list.get(position);
         holder.setDataOrder(order);
         holder.setDataProduct(order.getId_product());

         holder.binding.btnCancelOrder.setOnClickListener(v -> {
             handleOnClickOrder.onCancelOrder(order);
         });
         if(order.getStatus() == -1 || order.getStatus() == 2 ||order.getStatus() == 3){
             holder.binding.btnCancelOrder.setVisibility(View.GONE);
         }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemOrderUserBinding binding;
        myViewHolder(ItemOrderUserBinding itemOrderAdminBinding){
            super(itemOrderAdminBinding.getRoot());
            binding = itemOrderAdminBinding;
        }
        void setDataOrder(Order order){
           binding.tvQuantity.setText(String.valueOf(order.getQuantity()));
           binding.tvPrice.setText(String.valueOf(order.getTotal()));
           if(order.getStatus() == 0){
               binding.tvStatus.setText("Pending confirmation order");
               binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.orange_bold));
           } else if (order.getStatus() == 1) {
               binding.tvStatus.setText("Preparing order");
               binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.orange));
           } else if (order.getStatus() == 2) {
               binding.tvStatus.setText("Delivering");
               binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
           } else if (order.getStatus() == -1) {
               binding.tvStatus.setText("Order canceled");
           } else if (order.getStatus() == 3) {
               binding.tvStatus.setText("Delivered successfully");
               binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
           }

        }

        Map<String,String> fruitCacheUser = new HashMap<>();
        void setDataProduct(String id_product) {
            if (fruitCacheUser.containsKey(id_product)) {
                String image = fruitCacheUser.get(id_product);
                String name = fruitCacheUser.get(id_product);
                Glide.with(context).load(image).error(R.drawable.image).centerCrop().into(binding.imgProduct);
                binding.tvNameProduct.setText(name);
            } else {
                httpRequest.calAPI().getFruitById(id_product).enqueue(new Callback<Response<Fruit>>() {
                    @Override
                    public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                String image = response.body().getData().getImages().get(0);
                                String name = response.body().getData().getName();
                                fruitCacheUser.put(id_product, image);
                                fruitCacheUser.put(id_product, name);
                                Glide.with(context).load(image).error(R.drawable.image).centerCrop().into(binding.imgProduct);
                                binding.tvNameProduct.setText(name);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Fruit>> call, Throwable t) {
                        Log.e("Data loading failed", t.getMessage());
                    }
                });
            }
        }
    }
}
