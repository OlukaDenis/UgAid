package com.app.ugaid.view.ui.donate;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.model.Donation;
import com.app.ugaid.utils.Config;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialDonationFragment extends Fragment {
    private TextInputEditText mType, mQty, mName, mContact, mDescription;
    private Donation donation;
    private DonateViewModel viewModel;
    private AlertDialog.Builder builder;
    private MaterialButton btnDonate;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        if (Config.isNetworkAvailable(getContext())) {
            root = inflater.inflate(R.layout.fragment_material_donation, container, false);

            //Init firebase analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());


            builder = new AlertDialog.Builder(getContext());

            mContact = root.findViewById(R.id.donatio_contact);
            mType = root.findViewById(R.id.donation_type);
            mQty = root.findViewById(R.id.donation_quantity);
            mName = root.findViewById(R.id.donation_name);
            mDescription = root.findViewById(R.id.donation_desc);
            btnDonate = root.findViewById(R.id.btn_donate_material);

            DonateViewModelFactory factory = new DonateViewModelFactory(getActivity().getApplication());
            viewModel = new ViewModelProvider(this, factory).get(DonateViewModel.class);

            btnDonate.setOnClickListener(v -> submitForm());
        } else {
            root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        }

        return root;
    }

    private void submitForm() {
        String name, type, quantity, description, contact;

        //Donate
        name = Objects.requireNonNull(mName.getText()).toString();
        type = Objects.requireNonNull(mType.getText()).toString();
        quantity = Objects.requireNonNull(mQty.getText()).toString();
        description = Objects.requireNonNull(mDescription.getText()).toString();
        contact = Objects.requireNonNull(mContact.getText()).toString();

        donation = new Donation(type, quantity, name, contact, description);


        if(type.isEmpty()){
            mType.setError("Please provide donation type");
        } else if(quantity.isEmpty()){
            mQty.setError("Please provide donation quantity");
        } else if(name.isEmpty()){
            mName.setError("Please provide your name");
        } else if(contact.isEmpty()){
            mContact.setError("Please provide your contact");
        } else if(description.isEmpty()){
            mDescription.setError("Please provide a short description of your donation");
        } else {

            viewModel.sendDonation(donation);

            builder.setTitle("Thank you for your donation!")
                    .setMessage("Our team will contact you on how to deliver the donation.")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_donate);
                        reset();
                    });
            builder.setCancelable(false);
            builder.show();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Submit_donation");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }

    }

    private void reset(){
        mDescription.setText("");
        mName.setText("");
        mContact.setText("");
        mQty.setText("");
        mType.setText("");
    }
}
