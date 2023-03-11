package com.spendalyzer.util;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spendalyzer.CreditFragment;
import com.spendalyzer.DebitFragment;

public class ViewPagerSpendAdapter extends FragmentStateAdapter {

    public ViewPagerSpendAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) return new CreditFragment();
        return new DebitFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
