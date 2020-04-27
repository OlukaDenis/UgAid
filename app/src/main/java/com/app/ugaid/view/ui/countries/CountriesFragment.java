package com.app.ugaid.view.ui.countries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.view.adapters.CoronaCountryAdapter;
import com.app.ugaid.view.ui.HomeActivity;

import java.util.List;

public class CountriesFragment extends Fragment {

    private CountriesViewModel countriesViewModel;
    private RecyclerView recyclerView;
    private CoronaCountryAdapter adapter;
    private List<CoronaCountry> countryList;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ShimmerFrameLayout mShimmerViewContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CountriesViewModelFactory factory = new CountriesViewModelFactory(this.getActivity().getApplication());
        countriesViewModel = new ViewModelProvider(this, factory).get(CountriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_countries, container, false);

        mShimmerViewContainer = root.findViewById(R.id.shimmer_container);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

        recyclerView = root.findViewById(R.id.country_recyclerview);

        countriesViewModel.getStatsByCountry().observe(getViewLifecycleOwner(), coronaCountries -> {
            countryList = coronaCountries;
            showRecycleview();
        });
        setHasOptionsMenu(true);
        return root;
    }

    private void showRecycleview() {
        adapter = new CoronaCountryAdapter(getContext(), countryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        mShimmerViewContainer.setVisibility(View.GONE);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((HomeActivity) getContext()).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search Country...");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null){
                    getItemsFromDb(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null){
                    getItemsFromDb(newText);
                }
                return true;
            }
        });
    }

    private void getItemsFromDb(String query) {
        query = "%"+ query+"%";
        countriesViewModel.searchCountries(query).observe(this, coronaCountries -> {
            adapter = new CoronaCountryAdapter(getContext(), coronaCountries);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}