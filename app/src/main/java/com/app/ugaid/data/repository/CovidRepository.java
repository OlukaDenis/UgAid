package com.app.ugaid.data.repository;


import android.app.Application;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.api.StatesApiClient;
import com.app.ugaid.data.db.CoronaCountryDao;
import com.app.ugaid.data.db.CovidDatabase;
import com.app.ugaid.data.db.GlobalDao;
import com.app.ugaid.data.db.HospitalDao;
import com.app.ugaid.data.local.LocalDataSource;
import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.model.Covid;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.model.State;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidRepository {
    private CovidDatabase database;
    private CoronaCountryDao mDao;
    private GlobalDao globalDao;
    private HospitalDao hospitalDao;
    private static final String TAG = "CovidRepository";

    public CovidRepository(Application application){
        database = CovidDatabase.getDatabase(application);
        mDao = database.countryDao();
        globalDao = database.globalDao();
        hospitalDao = database.hospitalDao();
        getStates();
    }

    public LiveData<List<CoronaCountry>> getAllCountries(){
        return mDao.getAllCountries();
    }

    public Covid getGlobalStat(String orderBy){
        return globalDao.globalStats(orderBy);
    }


    public CoronaCountry ugandaStatus(String country){
        return mDao.getOneCountry(country);
    }

    public LiveData<List<Hospital>> getAllHospitals(){
        return hospitalDao.getAllHospitals();
    }

    @WorkerThread
    public LiveData<List<CoronaCountry>> getSearchResults(String country){
        return mDao.getSearchResults(country);
    }

    private void getStates(){
        //Fetching states
        ApiService service = StatesApiClient.getApiService(ApiService.class);
        Call<List<State>> call = service.getAllStates();

        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {

                if (response.isSuccessful()){
                    List<State> stateList = response.body();
                    List<String> strings = new ArrayList<>();

                    for (int i = 0; i < stateList.size(); i++) {
                        strings.add(stateList.get(i).getName());
//                            Log.i(TAG, "State at "+ i +":" +stateList.get(i).getName());
                    }

                    LocalDataSource.ALL_STATES = strings;
//                        Log.d(TAG, "States List: "+strings);
                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Log.e(TAG, "States onFailure: ",t );

            }
        });

    }
}
