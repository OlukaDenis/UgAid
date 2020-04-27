package com.app.ugaid.view.ui.facts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.view.ui.self_test.SelfTestFragment;

import java.util.Objects;

public class FactsFragment extends Fragment {
    private FactsViewModel factsViewModel;
    private CardView symptomCard, treatmentCard, faqCard, preventionCard;
    private SelfTestFragment selfTestFragment;
    private FragmentManager fragmentManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        factsViewModel =
                ViewModelProviders.of(this).get(FactsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_facts, container, false);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

        symptomCard = root.findViewById(R.id.symptom_card);
        treatmentCard = root.findViewById(R.id.treatment_card);
        faqCard = root.findViewById(R.id.faq_card);
        preventionCard = root.findViewById(R.id.prevention_card);

        faqCard.setOnClickListener(v -> openTestFragment() );
        symptomCard.setOnClickListener(v -> openSymptomFragment());
        treatmentCard.setOnClickListener(v -> openTreatmentFragment());
        preventionCard.setOnClickListener(v -> openPreventionFragment());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        return root;
    }

    private void openTestFragment() {
        navController.navigate(R.id.nav_faq);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open Self-Test");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openSymptomFragment() {
        navController.navigate(R.id.nav_symptoms);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open symptoms");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openTreatmentFragment() {
        navController.navigate(R.id.nav_treatment);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open treatment");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openPreventionFragment() {
        navController.navigate(R.id.nav_prevention);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open prevention");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
