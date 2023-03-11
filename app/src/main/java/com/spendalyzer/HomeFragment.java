package com.spendalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.spendalyzer.util.MessageHelper;
import com.spendalyzer.util.MessageReader;

import java.math.BigDecimal;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView =  inflater.inflate(R.layout.fragment_home, container, false);


        MaterialTextView debitToday = homeView.findViewById(R.id.home_debit);
        MaterialTextView creditToday = homeView.findViewById(R.id.home_credit);
        MaterialTextView debitWeek = homeView.findViewById(R.id.home_debit_week);
        MaterialTextView creditWeek = homeView.findViewById(R.id.home_credit_week);
        MaterialTextView debitMonth = homeView.findViewById(R.id.home_debit_month);
        MaterialTextView creditMonth = homeView.findViewById(R.id.home_credit_month);
        MaterialTextView debitYear = homeView.findViewById(R.id.home_debit_year);
        MaterialTextView creditYear = homeView.findViewById(R.id.home_credit_year);

        MaterialTextView homeWeek = homeView.findViewById(R.id.home_week);
        MaterialTextView homeMonth = homeView.findViewById(R.id.home_month);
        MaterialTextView homeYear = homeView.findViewById(R.id.home_year);

        MaterialTextView totalToday = homeView.findViewById(R.id.home_total);

        MessageReader msgReader = MessageReader.getMessageReader(MessageHelper.getHelper(getContext(), getContext().getContentResolver()));
        String tDebit = msgReader.getTodayDebit();
        String tCredit = msgReader.getTodayCredit();
        String mDebit = msgReader.getMonthDebit();
        String mCredit = msgReader.getMonthCredit();
        String yDebit = msgReader.getYearDebit();
        String yCredit = msgReader.getYearCredit();
        String wDebit = msgReader.getWeekDebit();
        String wCredit = msgReader.getWeekCredit();


        BigDecimal bal = new BigDecimal(tCredit).subtract(new BigDecimal(tDebit));

        totalToday.setText("Today's total: " + bal.toString());
        int balHL = bal.compareTo(BigDecimal.ZERO);
        if(balHL < 0){
            totalToday.setTextColor(Color.RED);
        }else if(balHL > 0){
            totalToday.setTextColor(Color.GREEN);
        }else{
            totalToday.setTextColor(Color.GRAY);
        }
        debitToday.setText("Debit - " + tDebit);
        creditToday.setText("Credit - " + tCredit);

        homeWeek.setText("Transaction this week: " + new BigDecimal(wCredit).subtract(new BigDecimal(wDebit)));
        debitWeek.setText("Debit - " + wDebit);
        creditWeek.setText("Credit - " + wCredit);
        homeMonth.setText("Transaction this month: " + new BigDecimal(mCredit).subtract(new BigDecimal(mDebit)));
        debitMonth.setText("Debit - " + mDebit);
        creditMonth.setText("Credit - " + mCredit);
        homeYear.setText("Transaction this year: " + new BigDecimal(yCredit).subtract(new BigDecimal(yDebit)));
        debitYear.setText("Debit - " + yDebit);
        creditYear.setText("Credit - " + yCredit);


        return homeView;
    }
}