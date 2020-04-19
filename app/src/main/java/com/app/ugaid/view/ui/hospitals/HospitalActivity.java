package com.app.ugaid.view.ui.hospitals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.ugaid.R;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.view.adapters.HospitalAdapter;

import java.util.List;

public class HospitalActivity extends AppCompatActivity {
    private HospitalAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        HospitalViewModelFactory factory = new HospitalViewModelFactory(this.getApplication());
        HospitalViewModel viewModel = new ViewModelProvider(this, factory).get(HospitalViewModel.class);

        recyclerView = findViewById(R.id.hospital_recycler_view);
        viewModel.getAllHospitals().observe(this, hospitals -> {
            adapter = new HospitalAdapter(this, hospitals);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        });
    }
}
