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

public class DebitFragment extends Fragment {

    public DebitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View debitView =  inflater.inflate(R.layout.fragment_debit, container, false);
        List<String> selectPeriod = Arrays.asList("All", "Today", "Week", "Month", "Year");

        ListView debitSMS = debitView.findViewById(R.id.debit_sms);
        MessageReader messageReader = MessageReader.getMessageReader(MessageHelper.getHelper(getContext(), getContext().getContentResolver()));
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectPeriod);

        Spinner spinnerPeriod = debitView.findViewById(R.id.d_period);
        spinnerPeriod.setAdapter(periodAdapter);

        final String[] selection = {selectPeriod.get(0)};

        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                ArrayAdapter<String> debitAdapter;
                List<String> debitSmsList;
                selection[0] = adapterView.getItemAtPosition(pos).toString();
                switch(selection[0]){
                    case "Today":
                        debitSmsList = messageReader.toStringList(messageReader.getTodayDebitSmsList());
                        break;
                    case "Week":
                        debitSmsList = messageReader.toStringList(messageReader.getWeekDebitSmsList());
                        break;
                    case "Month":
                        debitSmsList = messageReader.toStringList(messageReader.getMonthDebitSmsList());
                        break;
                    case "Year":
                        debitSmsList = messageReader.toStringList(messageReader.getYearDebitSmsList());
                        break;
                    default:
                        debitSmsList = messageReader.getDebitSmsList();
                }
                debitAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1,
                        debitSmsList.size() == 0 ? Arrays.asList("No Transactions found") : debitSmsList);
                debitSMS.setAdapter(debitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
        return debitView;
    }
}