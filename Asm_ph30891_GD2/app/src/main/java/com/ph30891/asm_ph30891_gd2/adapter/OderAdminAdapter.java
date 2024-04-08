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
import com.ph30891.asm_ph30891_gd2.databinding.ItemOrderAdminBinding;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.CurrencyUtils;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class OderAdminAdapter extends RecyclerView.Adapter<OderAdminAdapter.myViewHolder>{
    private Context context;
    private ArrayList<Order> list;
    private HttpRequest httpRequest;
    private HandleOnClickOrder handleOnClickOrder;

    public void showChooseHandle(HandleOnClickOrder handleOnClickOrder){
        this.handleOnClickOrder = handleOnClickOrder;
    }

    public OderAdminAdapter(Context context, ArrayList<Order> list, HttpRequest httpRequest) {
        this.context = context;
        this.list = list;
        this.httpRequest = httpRequest;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderAdminBinding binding = ItemOrderAdminBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
         Order order = list.get(position);
         holder.setDataOrder(order);
         holder.setDataProduct(order.getId_product());

         holder.binding.btnHuyOrder.setOnClickListener(v -> {
             handleOnClickOrder.onCancelOrder(order);
         });
         holder.binding.btnXacNhanOrder.setOnClickListener(v -> {
             handleOnClickOrder.onUpdate(order);
         });
         if(order.getStatus() == -1 || order.getStatus() == 3){
             holder.binding.btnXacNhanOrder.setVisibility(View.GONE);
             holder.binding.btnHuyOrder.setVisibility(View.GONE);
             holder.binding.btnCall.setVisibility(View.GONE);
         }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemOrderAdminBinding binding;
        myViewHolder(ItemOrderAdminBinding itemOrderAdminBinding){
            super(itemOrderAdminBinding.getRoot());
            binding = itemOrderAdminBinding;
        }
        void setDataOrder(Order order){
           binding.tvTotalQuantity.setText(order.getQuantity() + " kg");
           binding.tvTotalPrice.setText(CurrencyUtils.formatCurrency(order.getTotal(),new Locale("vi","VN")));
           binding.tvTenKh.setText(order.getName_customer());
           binding.tvPhone.setText(order.getPhone_customer());
           binding.tvDiaChi.setText(order.getLocation());
           if(order.getStatus() == 0){
               binding.tvTrangThaiOrder.setText("Pending confirmation order");
               binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(context,R.color.orange_bold));
               binding.btnXacNhanOrder.setText("Confirm order");
           } else if (order.getStatus() == 1) {
               binding.tvTrangThaiOrder.setText("Preparing order");
               binding.btnXacNhanOrder.setText("Deliver order");
               binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(context,R.color.orange));
           } else if (order.getStatus() == 2) {
               binding.tvTrangThaiOrder.setText("Delivering");
               binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(context,R.color.green));
               binding.btnXacNhanOrder.setText("Delivered successfully");
               binding.btnHuyOrder.setEnabled(false);
           } else if (order.getStatus() == -1) {
               binding.tvTrangThaiOrder.setText("Order canceled");
           } else if (order.getStatus() == 3) {
               binding.tvTrangThaiOrder.setText("Delivered successfully");
               binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(context,R.color.green));
           }

        }

        Map<String,String> fruitCache = new HashMap<>();
        void setDataProduct(String id_product) {
            if (fruitCache.containsKey(id_product)) {
                String image = fruitCache.get(id_product);
                String name = fruitCache.get(id_product);
                Double price = Double.parseDouble(fruitCache.get(id_product));
                Glide.with(context).load(image).error(R.drawable.image).centerCrop().into(binding.imgProduct);
                binding.tvNameProduct.setText(name);
                binding.Price.setText(String.valueOf(price)); // Convert Double to String
            } else {
                httpRequest.calAPI().getFruitById(id_product).enqueue(new Callback<Response<Fruit>>() {
                    @Override
                    public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                String image = response.body().getData().getImages().get(0);
                                String name = response.body().getData().getName();
                                Double price = response.body().getData().getPrice();
                                fruitCache.put(id_product, image); // Put each key-value pair separately
                                fruitCache.put(id_product, name);
                                fruitCache.put(id_product, String.valueOf(price)); // Convert Double to String
                                Glide.with(context).load(image).error(R.drawable.image).centerCrop().into(binding.imgProduct);
                                binding.tvNameProduct.setText(name);
                                binding.Price.setText(String.valueOf(price)); // Convert Double to String
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
