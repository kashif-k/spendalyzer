package com.spendalyzer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;

public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View helpView =  inflater.inflate(R.layout.fragment_help, container, false);

        MaterialTextView mail = helpView.findViewById(R.id.email);
        MaterialTextView paym = helpView.findViewById(R.id.payment);

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        mail.setOnClickListener(view -> {
            ClipData data = ClipData.newPlainText("email", "khankashif165@gmail.com");
            clipboard.setPrimaryClip(data);
            Toast.makeText(helpView.getContext(), "Email copied", Toast.LENGTH_SHORT).show();
        });

        paym.setOnClickListener(view -> {
            ClipData data = ClipData.newPlainText("UPI", "khankashif98.kk12-1@okicici");
            clipboard.setPrimaryClip(data);
            Toast.makeText(helpView.getContext(), "UPI copied", Toast.LENGTH_SHORT).show();
        });

        return helpView;
    }
}