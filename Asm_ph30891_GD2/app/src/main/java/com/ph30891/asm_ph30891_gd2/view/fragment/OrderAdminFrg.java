package com.ph30891.asm_ph30891_gd2.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.ph30891.asm_ph30891_gd2.adapter.OderAdminAdapter;
import com.ph30891.asm_ph30891_gd2.adapter.OrderUserAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.FragmentOrderAdminFrgBinding;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickOrder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderAdminFrg extends Fragment {
    private FragmentOrderAdminFrgBinding binding;
    public OrderAdminFrg() {}
    private OderAdminAdapter adapter;
    private HttpRequest httpRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String currentLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderAdminFrgBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        httpRequest = new HttpRequest();
        httpRequest.calAPI().getListOrderAdmin().enqueue(getListOrder);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocationAndNavigate();
        }
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
        binding.rcvOrderConfirm.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvOrderConfirm.setAdapter(adapter);
        adapter.showChooseHandle(new HandleOnClickOrder() {
            @Override
            public void onUpdate(Order order) {
                handleUpdateOrderStatus(order);
                if (order.getStatus() == 2){
                    String local = "cao đẳng fpt polytechnic";
                    String destination = order.getLocation();
                    openMap(local,destination);
                }
            }

            @Override
            public void onCancelOrder(Order order) {
                handleCancelOrderStatus(order);
            }
        });
    }

    private void handleUpdateOrderStatus(Order order) {
        int status = order.getStatus() + 1;
        order.setStatus(status);
        httpRequest.calAPI().updateOrderStatus(order.getId(),order).enqueue(updateOrderAPI);
    }
    private void handleCancelOrderStatus(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to cancel the order?");
        builder.setNegativeButton("No",null);
        builder.setPositiveButton("Yes",(dialog, which) -> {
            int status = -1;
            order.setStatus(status);
            httpRequest.calAPI().updateOrderStatus(order.getId(),order).enqueue(updateOrderAPI);
            dialog.dismiss();
        });
        builder.create().show();
    }
    Callback<Response<Order>> updateOrderAPI = new Callback<Response<Order>>() {
        @Override
        public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(getContext(), "Update order status successfully", Toast.LENGTH_SHORT).show();
                    httpRequest.calAPI().getListOrderAdmin().enqueue(getListOrder);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Order>> call, Throwable t) {
            Log.e("Update order status fail", "onFailure: "+ t.getMessage());
        }
    };
    private void openMap(String currentLocation, String destination) {
        Uri uri = Uri.parse("https://www.google.com/maps/dir/" + currentLocation + "/" + destination);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void getCurrentLocationAndNavigate() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, lấy vị trí hiện tại
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            // Lấy vị trí hiện tại thành công
                            double currentLatitude = location.getLatitude();
                            double currentLongitude = location.getLongitude();

                            currentLocation = currentLatitude + "," + currentLongitude;


                        } else {


                        }
                    });
        }
    }

}