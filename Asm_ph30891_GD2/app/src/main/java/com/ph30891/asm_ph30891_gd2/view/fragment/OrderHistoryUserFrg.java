package com.ph30891.asm_ph30891_gd2.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ph30891.asm_ph30891_gd2.adapter.OrderUserAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.FragmentOrderHistoryUserFrgBinding;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.view.MainAdminActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderHistoryUserFrg extends Fragment {
    private FragmentOrderHistoryUserFrgBinding binding;
    private OrderUserAdapter adapter;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    public OrderHistoryUserFrg() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryUserFrgBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        sharedPreferences = getContext().getSharedPreferences("INFO", MainAdminActivity.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        httpRequest = new HttpRequest();
        httpRequest.calAPI().getListOrderHistoryUser(userId).enqueue(new Callback<Response<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Order>>> call, retrofit2.Response<Response<ArrayList<Order>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        ArrayList<Order> orderList = response.body().getData();
                        getData(orderList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Order>>> call, Throwable t) {

            }
        });
        return view;
    }
    private void getData(ArrayList<Order> list){
        adapter = new OrderUserAdapter(getContext(),list,httpRequest);
        binding.rcvOrderHistoryUser.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvOrderHistoryUser.setAdapter(adapter);
    }
}