package com.app.ugaid.data.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.ugaid.data.api.ApiClient;
import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.db.CoronaCountryDao;
import com.app.ugaid.data.db.CovidDatabase;
import com.app.ugaid.data.db.GlobalDao;
import com.app.ugaid.model.CoronaCountry;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CovidWorker extends Worker {
    private static final String TAG = "CovidWorker";
    private CovidDatabase database;
    private CoronaCountryDao countryDao;
    private GlobalDao globalDao;

    public CovidWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = CovidDatabase.getDatabase(context);
        countryDao = database.countryDao();
        globalDao = database.globalDao();
        Log.d(TAG, "CovidWorker called: ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Log.d(TAG, "Fetching Countries Data from Novelcovid API");

        try {
            ApiService service = ApiClient.getApiService(ApiService.class);
            Call<List<CoronaCountry>> call = service.getAllCountries();
            Response<List<CoronaCountry>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                List<CoronaCountry> countryList = response.body();
                //delete all from the database
                countryDao.deleteAllCountries();
                //Populate new data
                countryDao.insertCoronaByCountry(countryList);

                Log.d(TAG, "Countries Json string from the API " + countryList);
                return Result.success();
            } else {
                return Result.retry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error fetching data", e);
            return Result.failure();
        }


    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }

}
