package com.app.ugaid.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.ugaid.utils.Config.NIGERIAN_STATES_URL;

public class StatesApiClient {
    // Create Logger
    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder initOkHttp = new OkHttpClient.Builder().addInterceptor(logger);

    // Create Retrofit Builder
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(NIGERIAN_STATES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initOkHttp.build());


    // Create Retrofit Instance
    private static Retrofit retrofit = builder.build();

    public static <T> T getApiService(Class<T> type) {
        return retrofit.create(type);
    }
}
