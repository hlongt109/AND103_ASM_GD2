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
import com.ph30891.asm_ph30891_gd2.adapter.AdapterViewPageUser;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityOrderUserManageBinding;

public class OrderUserManageActivity extends AppCompatActivity {
    private ActivityOrderUserManageBinding binding;
    private AdapterViewPageUser adapterViewPageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderUserManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainUserActivity.class));
            finish();
        });
        adapterViewPageUser = new AdapterViewPageUser(this);
        binding.viewpage2.setAdapter(adapterViewPageUser);
        new TabLayoutMediator(binding.tablayout,binding.viewpage2,(tab, i) -> {
            switch (i){
                case 0: tab.setText("Order"); break;
                case 1: tab.setText("History"); break;
            }
        }).attach();
    }
}