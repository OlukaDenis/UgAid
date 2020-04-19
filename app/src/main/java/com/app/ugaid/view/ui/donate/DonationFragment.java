package com.app.ugaid.view.ui.donate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonationFragment extends Fragment {
    private MaterialButton btnFunds, btnMaterial;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_donation, container, false);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());


        btnFunds = root.findViewById(R.id.btn_donate_funds);
        btnMaterial = root.findViewById(R.id.btn_donate_material);

        btnFunds.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_funds_donate);
        });

        btnMaterial.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_material_donate);
        });

        return root;
    }

}
