package com.spendalyzer.util;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spendalyzer.CreditDateFragment;
import com.spendalyzer.CreditFragment;
import com.spendalyzer.DebitDateFragment;
import com.spendalyzer.DebitFragment;

public class ViewPagerDateAdapter extends FragmentStateAdapter {

    public ViewPagerDateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) return new CreditDateFragment();
        return new DebitDateFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
