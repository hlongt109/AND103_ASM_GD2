package com.ph30891.asm_ph30891_gd2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.databinding.ItemFruitAdminBinding;
import com.ph30891.asm_ph30891_gd2.databinding.ItemFruitCustomerBinding;
import com.ph30891.asm_ph30891_gd2.model.Distributor;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickFruit;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnclickFruitUser;
import com.ph30891.asm_ph30891_gd2.view.OrderUserActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class FruitUserAdapter extends RecyclerView.Adapter<FruitUserAdapter.myViewHolder>{
    private Context context;
    private ArrayList<Fruit> list;
    private HttpRequest httpRequest;

    HandleOnclickFruitUser onClickFruit;

    public FruitUserAdapter(Context context, ArrayList<Fruit> list, HttpRequest httpRequest) {
        this.context = context;
        this.list = list;
        this.httpRequest = httpRequest;
    }

    public void showHandleClick (HandleOnclickFruitUser onClickFruit){
        this.onClickFruit = onClickFruit;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFruitCustomerBinding binding = ItemFruitCustomerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Fruit fruit = list.get(position);
        holder.setDataOnItem(fruit);
        holder.setNameDistributor(fruit);

        holder.binding.btnBuy.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("itemOrder",list.get(holder.getAdapterPosition()));
            Intent intent = new Intent(context, OrderUserActivity.class);
            intent.putExtras(bundle);
            (context).startActivity(intent);
        });
        holder.itemView.setOnClickListener(v -> {
            onClickFruit.details(fruit);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemFruitCustomerBinding binding;
        myViewHolder(ItemFruitCustomerBinding itemFruitBinding){
            super(itemFruitBinding.getRoot());
            binding = itemFruitBinding;
        }
        void setDataOnItem(Fruit fruit){
            if (fruit != null && fruit.getImages() != null && !fruit.getImages().isEmpty()) {
                String url  = fruit.getImages().get(0);
                if (url != null) {
                    String newUrl = url.replace("localhost", "10.0.2.2");
                    Glide.with(context).load(newUrl).centerCrop().error(R.drawable.image).into(binding.imvFruit);
                }
            }
            binding.tvNameFruit.setText(fruit.getName());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            format.setCurrency(Currency.getInstance("VND"));
            String priceFormatted = format.format(fruit.getPrice());
            binding.tvPrice.setText(priceFormatted);
        }
        Map<String,String> distributorCache = new HashMap<>();
        void setNameDistributor(Fruit fruit){
            String distributorId = fruit.getDistributorId();
            if(distributorCache.containsKey(distributorId)){
                String distributorName = distributorCache.get(distributorId);
                binding.tvNameDistributor.setText(distributorName);
            }else {
                httpRequest.calAPI().getNameDistributor(distributorId).enqueue(new Callback<Response<Distributor>>() {
                    @Override
                    public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                        if(response.isSuccessful()){
                            if (response.body().getStatus() == 200){
                                String distributorName = response.body().getData().getName();
                                distributorCache.put(distributorId, distributorName);
                                binding.tvNameDistributor.setText(distributorName);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Response<Distributor>> call, Throwable t) {
                        Log.e("Error get name distributor", "onFailure: "+t.getMessage());
                    }
                });
            }
        }
    }
}
