package com.app.ugaid.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;

public class PreventionActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention);
        setTitle(R.string.corona_prevention);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
