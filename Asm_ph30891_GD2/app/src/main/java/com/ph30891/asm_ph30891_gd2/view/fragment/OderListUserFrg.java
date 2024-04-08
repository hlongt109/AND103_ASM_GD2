package com.ph30891.asm_ph30891_gd2.view.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ph30891.asm_ph30891_gd2.adapter.OderAdminAdapter;
import com.ph30891.asm_ph30891_gd2.adapter.OrderUserAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.FragmentOderListUserFrgBinding;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickOrder;
import com.ph30891.asm_ph30891_gd2.view.MainAdminActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OderListUserFrg extends Fragment {

    public OderListUserFrg() {
    }

    private FragmentOderListUserFrgBinding binding;
    private OrderUserAdapter orderUserAdapter;
    private SharedPreferences sharedPreferences;
    private HttpRequest httpRequest;
    private ArrayList<Order> listOrder = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOderListUserFrgBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        sharedPreferences = getContext().getSharedPreferences("INFO", MainAdminActivity.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        httpRequest = new HttpRequest();
        httpRequest.calAPI().getListOrderUser(userId).enqueue(new Callback<Response<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Order>>> call, retrofit2.Response<Response<ArrayList<Order>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listOrder = response.body().getData();
                        getData(listOrder);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Order>>> call, Throwable t) {

            }
        });

        return view;
    }

    private void getData(ArrayList<Order> list) {
        orderUserAdapter = new OrderUserAdapter(getContext(), list, httpRequest);
        binding.rcvOrderUser.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvOrderUser.setAdapter(orderUserAdapter);
        orderUserAdapter.showChooseHandle(new HandleOnClickOrder() {
            @Override
            public void onUpdate(Order order) {
            }

            @Override
            public void onCancelOrder(Order order) {
                handleCancelOrderStatus(order);
            }
        });
    }

    private void handleCancelOrderStatus(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to cancel the order?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            int status = -1;
            order.setStatus(status);
            httpRequest.calAPI().updateOrderStatus(order.getId(), order).enqueue(new Callback<Response<Order>>() {
                @Override
                public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            removeItemFromList(order.getId());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<Order>> call, Throwable t) {

                }
            });

            dialog.dismiss();
        });
        builder.create().show();
    }

    private void removeItemFromList(String id) {
        for (int i = 0; i < listOrder.size(); i++) {
            if (listOrder.get(i).getId().equals(id)) {
                listOrder.remove(i);
                break;
            }
        }
        orderUserAdapter.notifyDataSetChanged();
    }
}