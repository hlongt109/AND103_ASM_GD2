package com.ph30891.asm_ph30891_gd2.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ph30891.asm_ph30891_gd2.adapter.OderAdminAdapter;
import com.ph30891.asm_ph30891_gd2.adapter.OrderUserAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.FragmentOrderHistoryAdminFrgBinding;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderHistoryAdminFrg extends Fragment {

    public OrderHistoryAdminFrg() {}
    private FragmentOrderHistoryAdminFrgBinding binding;
    private OderAdminAdapter adapter;
    private HttpRequest httpRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryAdminFrgBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        httpRequest = new HttpRequest();
        httpRequest.calAPI().getListOrderHistoryAdmin().enqueue(getListOrder);

        return view;
    }
    Callback<Response<ArrayList<Order>>> getListOrder = new Callback<Response<ArrayList<Order>>>() {
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
    };
    private void getData(ArrayList<Order> list){
        adapter = new OderAdminAdapter(getContext(),list,httpRequest);
        binding.rcvHistoryAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvHistoryAdmin.setAdapter(adapter);
    }
}