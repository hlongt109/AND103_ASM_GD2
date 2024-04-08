package com.ph30891.asm_ph30891_gd2.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayoutMediator;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.adapter.AdapterViewPage;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityOrderManageBinding;

public class OrderManageActivity extends AppCompatActivity {
    private ActivityOrderManageBinding binding;
    private AdapterViewPage adapterViewPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainAdminActivity.class));
            finish();
        });
        adapterViewPage = new AdapterViewPage(this);
        binding.viewpage2.setAdapter(adapterViewPage);
        new TabLayoutMediator(binding.tablayout,binding.viewpage2,(tab, i) -> {
            switch (i){
                case 0: tab.setText("Order"); break;
                case 1: tab.setText("History"); break;
            }
        }).attach();
    }
}