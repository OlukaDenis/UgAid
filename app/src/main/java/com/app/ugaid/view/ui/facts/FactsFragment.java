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
import com.app.ugaid.view.ui.PreventionActivity;
import com.app.ugaid.view.ui.SymptomsActivity;
import com.app.ugaid.view.ui.TreatmentActivity;
import com.app.ugaid.view.ui.self_test.SelfTestFragment;

import java.util.Objects;

public class FactsFragment extends Fragment {
    private FactsViewModel factsViewModel;
    private CardView symptomCard, treatmentCard, faqCard, preventionCard;
    private SelfTestFragment selfTestFragment;
    private FragmentManager fragmentManager;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        symptomCard.setOnClickListener(v -> startActivity(new Intent(getActivity(), SymptomsActivity.class)));
        treatmentCard.setOnClickListener(v -> startActivity(new Intent(getActivity(), TreatmentActivity.class)));
        preventionCard.setOnClickListener(v -> startActivity(new Intent(getActivity(), PreventionActivity.class)));

        return root;
    }

    private void openTestFragment() {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_faq);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open Self-Test");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
