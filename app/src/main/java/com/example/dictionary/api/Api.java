package com.example.dictionary.api;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private Retrofit retrofit;

    private Gson gson = new Gson();

    private Context context;

    private DataDictionary dataDictionary;

    public Api(Context context, String url){
        this.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getWordDescription(String word, ResponseListener listener){
        DataDictionary service = retrofit.create(DataDictionary.class);

        Call<ResponseBody> call = service.getMeaning(word);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseBody = "";
                if (response.isSuccessful() ) {
                    try {
                        responseBody = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onSuccess(responseBody);
                }
                 else {
                    listener.onFailure(responseBody + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure(t.getMessage() + t.getStackTrace());
            }
        });
    }
}
