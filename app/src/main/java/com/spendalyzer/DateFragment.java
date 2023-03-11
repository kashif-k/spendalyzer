package com.spendalyzer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.spendalyzer.util.ViewPagerDateAdapter;


public class DateFragment extends Fragment {


    public DateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dateView =  inflater.inflate(R.layout.fragment_date, container, false);

        TabLayout dateTab = dateView.findViewById(R.id.date_tab);
        ViewPager2 pagesTab = dateView.findViewById(R.id.date_pages);

        ViewPagerDateAdapter adapter = new ViewPagerDateAdapter(this);

        pagesTab.setAdapter(adapter);

        dateTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                    dateTab.getTabAt(position).select();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Some error in selecting date tabs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return dateView;
    }
}