package com.ph30891.asm_ph30891_gd2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.adapter.Adapter_item_district_select_GHN;
import com.ph30891.asm_ph30891_gd2.adapter.Adapter_item_province_select_GHN;
import com.ph30891.asm_ph30891_gd2.adapter.Adapter_item_ward_select_GHN;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityOrderUserBinding;
import com.ph30891.asm_ph30891_gd2.model.District;
import com.ph30891.asm_ph30891_gd2.model.DistrictRequest;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Order;
import com.ph30891.asm_ph30891_gd2.model.Province;
import com.ph30891.asm_ph30891_gd2.model.ResponseGHN;
import com.ph30891.asm_ph30891_gd2.model.Ward;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.networking.LocationRequest;
import com.ph30891.asm_ph30891_gd2.utilities.CurrencyUtils;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderUserActivity extends AppCompatActivity {
    private ActivityOrderUserBinding binding;
    private HttpRequest httpRequest;
    private LocationRequest locationRequest;
    private String WardCode;
    private int DistrictId;
    private int ProvinceId;
    private Adapter_item_ward_select_GHN adapterItemWardSelectGhn;
    private Adapter_item_district_select_GHN adapterItemDistrictSelectGhn;
    private Adapter_item_province_select_GHN adapterItemProvinceSelectGhn;
    private SharedPreferences sharedPreferences;
    private String id_product ="";
    private double totalPriceOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("INFO",MainAdminActivity.MODE_PRIVATE);
        httpRequest = new HttpRequest();
        locationRequest = new LocationRequest();
        setDataOnViewOrder();
        init();
    }
    private void init() {
        locationRequest.callAPI().getListProvince().enqueue(responseProvince);
        binding.spnProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spnDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spnWard.setOnItemSelectedListener(onItemSelectedListener);
        binding.spnProvince.setSelection(0);

        binding.btnOrder.setOnClickListener(v -> {
            String id_user = sharedPreferences.getString("id","");
            int quantity = Integer.parseInt(binding.edQuantity.getText().toString());
            String name_customer = binding.edTenNguoiNhan.getText().toString().trim();
            String phone_customer = binding.edPhoneNumber.getText().toString().trim();
            String location = getLocation();
            String pay_method = binding.tvPaymentMethod.getText().toString();
//            Toast.makeText(this, "+"+totalPriceOrder, Toast.LENGTH_SHORT).show();
            if (validate()){
                Order order = new Order(id_user,id_product,quantity,totalPriceOrder,name_customer,phone_customer,location,pay_method,0);
                httpRequest.calAPI().addOrder(order).enqueue(addOderApi);
            }
        });
        binding.btnBack.setOnClickListener(v -> {
           startActivity(new Intent(this, MainUserActivity.class));
           finish();
        });

    }
    private void setDataOnViewOrder(){
        String name = sharedPreferences.getString("name","");
        Bundle bundle = getIntent().getExtras();
        Fruit fruit = (Fruit) bundle.getSerializable("itemOrder");
        id_product = fruit.get_id();
        String price = CurrencyUtils.formatCurrency(fruit.getPrice(),new Locale("vi","VN"));
        binding.tvNameProduct.setText(fruit.getName());
        binding.tvPrice.setText(price);
        binding.edTenNguoiNhan.setText(name);
        binding.edQuantity.setText(String.valueOf(1));
        Glide.with(this).load(fruit.getImages().get(0)).centerCrop().error(R.drawable.image).into(binding.imgProduct);
        totalPrice(fruit.getPrice());
        binding.edQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                totalPrice(fruit.getPrice());
            }
        });
    }
    private void totalPrice(Double price){
        String quantityText = binding.edQuantity.getText().toString().trim();
        if(quantityText.isEmpty()){
            binding.tilQuantity.setError("Quantity must be a number");
            binding.tvTotalPrice.setText("");
        }else {
            binding.tilQuantity.setError(null);
            int quantity = Integer.parseInt( binding.edQuantity.getText().toString().replaceAll("\\s+", ""));
            totalPriceOrder = price * quantity;
            String totalP = CurrencyUtils.formatCurrency(totalPriceOrder,new Locale("vi","VN"));
            binding.tvTotalPrice.setText(totalP);
        }

    }
    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {

        }
    };
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.spnProvince) {
                ProvinceId = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceId);
                locationRequest.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            }
            if(parent.getId() == R.id.spnDistrict){
                DistrictId = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                locationRequest.callAPI().getListWard(DistrictId).enqueue(responseWard);
            }
            if(parent.getId() == R.id.spnWard){
                WardCode = ((Ward)parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> list = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(list);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };
    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<Ward> list = new ArrayList<>();
                    if(response.body().getData() == null) return;
                    list.addAll(response.body().getData());
                    SetDataSpinWard(list);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {

        }
    };
    private void SetDataSpinProvince(ArrayList<Province> list) {
        adapterItemProvinceSelectGhn = new Adapter_item_province_select_GHN(this,list);
        binding.spnProvince.setAdapter(adapterItemProvinceSelectGhn);
    }
    private void SetDataSpinDistrict(ArrayList<District> list) {
        adapterItemDistrictSelectGhn = new Adapter_item_district_select_GHN(this,list);
        binding.spnDistrict.setAdapter(adapterItemDistrictSelectGhn);
    }
    private void SetDataSpinWard(ArrayList<Ward> list) {
        adapterItemWardSelectGhn = new Adapter_item_ward_select_GHN(this,list);
        binding.spnWard.setAdapter(adapterItemWardSelectGhn);
    }
    private boolean validate(){
        if(TextUtils.isEmpty(binding.edQuantity.getText().toString().trim())){
            binding.tilQuantity.setError("Quantity must be a number");
            return false;
        }else {
            binding.tilQuantity.setError(null);
        }
        if(TextUtils.isEmpty(binding.edTenNguoiNhan.getText().toString().trim())){
            binding.tilTenNguoiNhan.setError("Enter your name");
            return false;
        }else {
            binding.tilTenNguoiNhan.setError(null);
        }
        if (TextUtils.isEmpty(binding.edPhoneNumber.getText().toString().trim())){
            binding.tilPhoneNumber.setError("Enter your phone number");
            return false;
        }else {
            binding.tilPhoneNumber.setError(null);
        }
        if (TextUtils.isEmpty(binding.edLocation.getText().toString().trim())){
            binding.tilLocation.setError("Enter your location");
            return false;
        }else {
            binding.tilLocation.setError(null);
        }
        return true;
    }
    private String getLocation() {
        String detailAddress =  binding.edLocation.getText().toString().trim();
        String ward = ((Ward) binding.spnWard.getSelectedItem()).getWardName();
        String district = ((District) binding.spnDistrict.getSelectedItem()).getDistrictName();
        String province = ((Province) binding.spnProvince.getSelectedItem()).getProvinceName();
        return detailAddress + ", "+ ward + ", " + district + ", " + province;
    }
    Callback<com.ph30891.asm_ph30891_gd2.model.Response<Order>> addOderApi = new Callback<com.ph30891.asm_ph30891_gd2.model.Response<Order>>() {
        @Override
        public void onResponse(Call<com.ph30891.asm_ph30891_gd2.model.Response<Order>> call, Response<com.ph30891.asm_ph30891_gd2.model.Response<Order>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(OrderUserActivity.this, "Order successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrderUserActivity.this, MainUserActivity.class));
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<com.ph30891.asm_ph30891_gd2.model.Response<Order>> call, Throwable t) {
            Log.e("Order failure", "onFailure: "+t.getMessage());
        }
    };
}