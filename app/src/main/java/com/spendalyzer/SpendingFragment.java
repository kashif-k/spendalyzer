package com.spendalyzer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.spendalyzer.util.ViewPagerSpendAdapter;

public class SpendingFragment extends Fragment {

    public SpendingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View spendingView =  inflater.inflate(R.layout.fragment_spending, container, false);

        TabLayout spendTab = spendingView.findViewById(R.id.spend_tab);
        ViewPager2 pagesTab = spendingView.findViewById(R.id.pages);

        ViewPagerSpendAdapter adapter = new ViewPagerSpendAdapter(this);

        pagesTab.setAdapter(adapter);

        spendTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagesTab.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        pagesTab.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                try{
                    spendTab.getTabAt(position).select();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Some error in selecting spend tabs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return spendingView;
    }


}
