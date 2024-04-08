package com.ph30891.asm_ph30891_gd2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ph30891.asm_ph30891_gd2.view.fragment.OderListUserFrg;
import com.ph30891.asm_ph30891_gd2.view.fragment.OrderAdminFrg;
import com.ph30891.asm_ph30891_gd2.view.fragment.OrderHistoryAdminFrg;
import com.ph30891.asm_ph30891_gd2.view.fragment.OrderHistoryUserFrg;

public class AdapterViewPageUser extends FragmentStateAdapter {
    public AdapterViewPageUser(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new OderListUserFrg();
            case 1: return new OrderHistoryUserFrg();
        }
        return new OderListUserFrg();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
