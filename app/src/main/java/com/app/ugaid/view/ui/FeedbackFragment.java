package com.app.ugaid.view.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.ugaid.R;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
    private EditText eSub, eMessage;
    private Button sendBtn;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);
        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

        eMessage = root.findViewById(R.id.txtMsg);
        eSub = root.findViewById(R.id.txtSub);
        sendBtn = root.findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(v -> sendEmail());
        return root;
    }

    private void sendEmail(){
        if (eSub.getText().toString().isEmpty()){
            eSub.setError("Subject must not be empty");
        } else if (eMessage.getText().toString().isEmpty()){
            eMessage.setError("Please enter your feedback message");
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"olukadeno@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, eSub.getText().toString());
            emailIntent.putExtra(Intent.EXTRA_TEXT, eMessage.getText().toString());
            emailIntent.setType("message/rfc82");
            if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Send email via."));
            }
            resetFields();
        }
    }

    private void resetFields() {
        eMessage.setText("");
        eSub.setText("");
    }

}
