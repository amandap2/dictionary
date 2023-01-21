package com.example.dictionary.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface DataDictionary {
    @GET("english.txt")
    @Streaming
    Call<ResponseBody> getWords();

    @GET("{word}")
    Call<ResponseBody> getMeaning(@Path("word") String word);
}
