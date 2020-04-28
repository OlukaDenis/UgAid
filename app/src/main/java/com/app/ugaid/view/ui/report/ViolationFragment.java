package com.app.ugaid.view.ui.report;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ugaid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViolationFragment extends Fragment {


    public ViolationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_violation, container, false);

        return root;
    }

}
