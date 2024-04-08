package com.ph30891.asm_ph30891_gd2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.adapter.FruitAdminAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityMainBinding;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Page;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;
import com.ph30891.asm_ph30891_gd2.utilities.HandleOnClickFruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainAdminActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String token;
    private int page = 1;
    private int totalPages = 0;
    private ArrayList<Fruit> listFruit = new ArrayList<>();
    private String sort;
    private FruitAdminAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("INFO",MainAdminActivity.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        httpRequest = new HttpRequest(token);

        init();
    }
    private void init(){
        binding.btnOrderr.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderManageActivity.class));
        });
        binding.btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddUpdateActivity.class));
        });
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(totalPages == page) return;
                if(binding.progressBar.getVisibility() == View.INVISIBLE ||binding.progressBar.getVisibility() == View.GONE){
                    binding.progressBar.setVisibility(View.VISIBLE);
                    page++;
                    FilterFruit();
                }
            }
        });
        binding.btnFilter.setOnClickListener(v -> {
            page = 1;
            listFruit.clear();
            adapter.notifyDataSetChanged();
            FilterFruit();
        });

        ArrayAdapter<CharSequence> spinerAdapter = ArrayAdapter.createFromResource(this,R.array.spinner_price, android.R.layout.simple_spinner_item);

        binding.spnFilter.setAdapter(spinerAdapter);
        binding.spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence value = (CharSequence) parent.getAdapter().getItem(position);
                if (value.toString().equals("Ascending")){
                    sort = "1";
                } else if (value.toString().equals("Decrease")) {
                    sort = "-1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spnFilter.setSelection(1);
    }

    private void getData(ArrayList<Fruit> list){
        if (binding.progressBar.getVisibility() == View.VISIBLE){
            new Handler().postDelayed(() -> {
                adapter.notifyItemChanged(listFruit.size() -1);
                binding.progressBar.setVisibility(View.GONE);
                listFruit.addAll(list);
                adapter.notifyDataSetChanged();
            },500);
            return;
        }
        listFruit.addAll(list);
        adapter = new FruitAdminAdapter(this,listFruit,httpRequest);
        binding.rcvProduct.setLayoutManager(new GridLayoutManager(this,2));
        binding.rcvProduct.setAdapter(adapter);
        adapter.showHandleClick(new HandleOnClickFruit() {
            @Override
            public void details(String id) {

            }

            @Override
            public void delete(String id) {
                handleDelete(id);
            }

            @Override
            public void update(Fruit fruit) {
               Intent intent = new Intent(MainAdminActivity.this, AddUpdateActivity.class);
               intent.putExtra("fruit",fruit);
               startActivity(intent);
            }
        });
    }

    Callback<Response<Page<ArrayList<Fruit>>>> getlistFruitResponse = new Callback<Response<Page<ArrayList<Fruit>>>>() {
        @Override
        public void onResponse(Call<Response<Page<ArrayList<Fruit>>>> call, retrofit2.Response<Response<Page<ArrayList<Fruit>>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200){
                    totalPages = response.body().getData().getTotalPage();
                    ArrayList<Fruit> _list = response.body().getData().getData();
                    getData(_list);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Page<ArrayList<Fruit>>>> call, Throwable t) {
            Log.e("Load more fail", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> map = getMapFilter(page,"", "-1");
        httpRequest.calAPI().getPageFruit(map).enqueue(getlistFruitResponse);
    }
    private Map<String, String> getMapFilter(int _page, String _name, String _sort) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(_page));
        map.put("name", _name);
        map.put("sort", _sort);
        return map;
    }
    private void FilterFruit(){
        String _name = binding.edSearch.getText().toString().equals("") ? "" :binding.edSearch.getText().toString();
        String _sort = sort.equals("") ? "-1" : sort;
        Map<String , String> map = getMapFilter(page,_name,_sort);
        httpRequest.calAPI().getPageFruit(map).enqueue(getlistFruitResponse);
    }
    private void handleDelete(String id) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this fruit?");
        builder.setNegativeButton("No",null);
        builder.setPositiveButton("Yes",(dialog, which) -> {
            httpRequest.calAPI().removeFruitById(id).enqueue(new Callback<Response<Fruit>>() {
                @Override
                public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainAdminActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        removeItemFromList(id);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Response<Fruit>> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        });
        builder.create().show();
    }
    private void removeItemFromList(String id) {
        for (int i = 0; i < listFruit.size(); i++) {
            if (listFruit.get(i).get_id().equals(id)) {
                listFruit.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }
}