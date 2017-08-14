package com.kokaihop.network;

import com.altaworks.kokaihop.ui.BuildConfig;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kokaihop.database.RealmString;
import com.kokaihop.database.RealmStringDeserializer;

import java.util.concurrent.TimeUnit;

import io.realm.RealmList;
import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final long REQUEST_TIMEOUT = 15000;

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }

                })
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                }.getType(), new RealmStringDeserializer())
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHttpClient())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
        return okHttpClient;
    }
}
