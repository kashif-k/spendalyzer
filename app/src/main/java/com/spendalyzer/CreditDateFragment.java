package com.spendalyzer;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;
import com.spendalyzer.util.MessageHelper;
import com.spendalyzer.util.MessageReader;
import com.spendalyzer.util.SmsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CreditDateFragment extends Fragment {

    public CreditDateFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cDateView = inflater.inflate(R.layout.fragment_credit_date, container, false);

        MessageReader messageReader = MessageReader.getMessageReader(MessageHelper.getHelper(getContext(), getContext().getContentResolver()));

        MaterialTextView dateSelector = cDateView.findViewById(R.id.credit_date);
        ListView smsListView = cDateView.findViewById(R.id.credit_date_sms);
        MaterialTextView creditTotal = cDateView.findViewById(R.id.date_credit_info);

        dateSelector.setOnClickListener(v -> {

            MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setSelection(new Pair<>(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                    .setTitleText("Select a date range").build();

            datePicker.show(getActivity().getSupportFragmentManager(), "DATE");

            datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
                ArrayAdapter<String> adapter;
                Long startDate = selection.first;
                Long endDate = selection.second;

                String startDateString = DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString();
                String endDateString = DateFormat.format("dd/MM/yyyy", new Date(endDate)).toString();
                String date = startDateString + " - " + endDateString;
                dateSelector.setText(getString(R.string.select_period) + ": " + date);
                List<Object> sms = messageReader.getCreditSmsFromDate(startDate, endDate);
                List<String> smsList = messageReader.toStringList((ArrayList<SmsUtil>)sms.get(1));
                smsList.add("");

                adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1,
                        smsList.size() == 0 ? Collections.singletonList("No Transactions found") : smsList);
                smsListView.setAdapter(adapter);
                creditTotal.setText("Total: " + (String) sms.get(0));

            });

        });

        return cDateView;
    }
}