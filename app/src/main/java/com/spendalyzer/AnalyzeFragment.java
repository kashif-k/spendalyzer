package com.spendalyzer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.spendalyzer.util.MessageHelper;
import com.spendalyzer.util.MessageReader;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AnalyzeFragment extends Fragment {

    public AnalyzeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View analyticsView = inflater.inflate(R.layout.fragment_analyze, container, false);

        MessageReader msgReader = MessageReader.getMessageReader(MessageHelper.getHelper(getContext(), getContext().getContentResolver()));
        List<String> selectPeriod = Arrays.asList("Today", "Week", "Month", "Year");
        String[] transactions = new String[5];

        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectPeriod);

        Spinner spinnerPeriod = analyticsView.findViewById(R.id.period);
        spinnerPeriod.setAdapter(periodAdapter);

        MaterialTextView upi = analyticsView.findViewById(R.id.upi);
        MaterialTextView atm = analyticsView.findViewById(R.id.atm);
        MaterialTextView other = analyticsView.findViewById(R.id.other_trx);
        MaterialTextView upiDebit = analyticsView.findViewById(R.id.upi_debit);
        MaterialTextView upiCredit = analyticsView.findViewById(R.id.upi_credit);
        MaterialTextView othDebit = analyticsView.findViewById(R.id.oth_debit);
        MaterialTextView othCredit = analyticsView.findViewById(R.id.oth_credit);
        MaterialTextView atmDebit = analyticsView.findViewById(R.id.atm_debit);

        final String[] selection = {selectPeriod.get(0)};
        transactions[0] = msgReader.getTodayUPIDebit();
        transactions[1] = msgReader.getTodayUPICredit();
        transactions[2] = msgReader.getTodayOtherDebit();
        transactions[3] = msgReader.getTodayOtherCredit();
        transactions[4] = msgReader.getTodayATMDebit();

        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selection[0] = adapterView.getItemAtPosition(pos).toString();
                switch(selection[0]){
                    case "Week":
                        transactions[0] = msgReader.getWeekUPIDebit();
                        transactions[1] = msgReader.getWeekUPICredit();
                        transactions[2] = msgReader.getWeekOtherDebit();
                        transactions[3] = msgReader.getWeekOtherCredit();
                        transactions[4] = msgReader.getWeekATMDebit();
                        break;
                    case "Month":
                        transactions[0] = msgReader.getMonthUPIDebit();
                        transactions[1] = msgReader.getMonthUPICredit();
                        transactions[2] = msgReader.getMonthOtherDebit();
                        transactions[3] = msgReader.getMonthOtherCredit();
                        transactions[4] = msgReader.getMonthATMDebit();
                        break;
                    case "Year":
                        transactions[0] = msgReader.getYearUPIDebit();
                        transactions[1] = msgReader.getYearUPICredit();
                        transactions[2] = msgReader.getYearOtherDebit();
                        transactions[3] = msgReader.getYearOtherCredit();
                        transactions[4] = msgReader.getYearATMDebit();
                        break;
                    default:
                        transactions[0] = msgReader.getTodayUPIDebit();
                        transactions[1] = msgReader.getTodayUPICredit();
                        transactions[2] = msgReader.getTodayOtherDebit();
                        transactions[3] = msgReader.getTodayOtherCredit();
                        transactions[4] = msgReader.getTodayATMDebit();
                        break;
                }
                BigDecimal upiTotal = new BigDecimal(transactions[1]).subtract(new BigDecimal(transactions[0]));
                BigDecimal othTotal = new BigDecimal(transactions[3]).subtract(new BigDecimal(transactions[2]));
                upi.setText("UPI transactions " + selection[0] + ": " + upiTotal);
                atm.setText("ATM transactions " + selection[0]);
                other.setText("Other transactions " + selection[0] + ": " + othTotal);

                upiDebit.setText("Debit - " + transactions[0]);
                upiCredit.setText("Credit - " + transactions[1]);
                othDebit.setText("Debit - " + transactions[2]);
                othCredit.setText("Credit - " + transactions[3]);
                atmDebit.setText("Debit - " + transactions[4]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });


        return analyticsView;
    }
}
