package com.app.ugaid.view.ui.donate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ugaid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailsFragment extends Fragment {


    public PaymentDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

}
