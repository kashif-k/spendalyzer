package com.spendalyzer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.spendalyzer.util.MessageHelper;
import com.spendalyzer.util.MessageReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreditFragment extends Fragment {

    public CreditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View creditView = inflater.inflate(R.layout.fragment_credit, container, false);

        ListView creditSMS = creditView.findViewById(R.id.credit_sms);
        MessageReader messageReader = MessageReader.getMessageReader(MessageHelper.getHelper(getContext(), getContext().getContentResolver()));
        List<String> selectPeriod = Arrays.asList("All", "Today", "Week", "Month", "Year");

        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectPeriod);

        Spinner spinnerPeriod = creditView.findViewById(R.id.c_period);
        spinnerPeriod.setAdapter(periodAdapter);

        final String[] selection = {selectPeriod.get(0)};

        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                ArrayAdapter<String> creditAdapter;
                List<String> creditSmsList;
                selection[0] = adapterView.getItemAtPosition(pos).toString();
                switch(selection[0]){
                    case "Today":
                        creditSmsList = messageReader.toStringList(messageReader.getTodayCreditSmsList());
                        break;
                    case "Week":
                        creditSmsList = messageReader.toStringList(messageReader.getWeekCreditSmsList());
                        break;
                    case "Month":
                        creditSmsList = messageReader.toStringList(messageReader.getMonthCreditSmsList());
                        break;
                    case "Year":
                        creditSmsList = messageReader.toStringList(messageReader.getYearCreditSmsList());
                        break;
                    default:
                        creditSmsList = messageReader.getCreditSmsList();
                }
                creditAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1,
                        creditSmsList.size() == 0 ? Arrays.asList("No Transactions found") : creditSmsList);
                creditSMS.setAdapter(creditAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return creditView;
    }
}