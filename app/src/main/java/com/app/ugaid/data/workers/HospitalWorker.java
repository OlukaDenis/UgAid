package com.app.ugaid.data.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.api.StatesApiClient;
import com.app.ugaid.data.db.CovidDatabase;
import com.app.ugaid.data.db.HospitalDao;
import com.app.ugaid.data.local.LocalDataSource;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.model.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HospitalWorker extends Worker {
    private static final String TAG = "HospitalWorker";
    private CovidDatabase database;
    private HospitalDao hospitalDao;

    public HospitalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = CovidDatabase.getDatabase(context);
        hospitalDao = database.hospitalDao();
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            hospitalDao.deleteHospitals();

            List<Hospital> hospitalList = hospitals();
            hospitalDao.insertHospitals(hospitalList);
            Log.d(TAG, "Populating hospitals to the database: ");

//            //Fetching states
//            ApiService service = StatesApiClient.getApiService(ApiService.class);
//            Call<List<State>> call = service.getAllStates();
//            Response<List<State>> response = call.execute();
//
//            if (response.isSuccessful() && response.body() != null) {
////                LocalDataSource.states = response.body();
//            }


            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error while adding hospitals to the database", e);
            return Result.failure();
        }
    }


    private ArrayList<Hospital> hospitals(){

        ArrayList<Hospital> hList = new ArrayList<>();

        hList.add(new Hospital(1, "48th Armed Forces Hospital", "Lagos"));
        hList.add(new Hospital(2, "68th Armed Forces Hospital", "Lagos"));
        hList.add(new Hospital(3, "Abia State University Teaching Hospital", "Aba"));
        hList.add(new Hospital(4, "Abubakar Tafawa Balewa University Teaching Hospital", "Bauchi"));
        hList.add(new Hospital(5, "Abuja Clinics", "Abuja"));
        hList.add(new Hospital(6, "Ahmadu Bello University Teaching Hospital", "Zaria"));
        hList.add(new Hospital(7, "Aminu Kano Teaching Hospital", "Kano"));
        hList.add(new Hospital(8, "Anambra State University Teaching Hospital", "Amaku Awka"));
        hList.add(new Hospital(9, "Armed Forces Hospital 12", "No city"));
        hList.add(new Hospital(10, "Babcock University Teaching Hospital 24", "No city"));
        hList.add(new Hospital(11, "Baptist Medical Centre", "Saki"));

        return hList;
    }
}
