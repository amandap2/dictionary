package com.example.dictionary.database;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.MainActivity;
import com.example.dictionary.R;
import com.example.dictionary.adapter.WordsAdapter;
import com.example.dictionary.api.DataDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Database extends AppCompatActivity {

    private DataDictionary dataDictionary;
    private WordsAdapter adapter;
    private Retrofit retrofit;
    public void onCreate(Context context, SQLiteDatabase database, WordsAdapter adapter){
        this.adapter = adapter;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/meetDeveloper/freeDictionaryAPI/master/meta/wordList/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dataDictionary = retrofit.create(DataDictionary.class);
        createDataBase(context, database);
    }
    public Database() {

    }

    public void createDataBase(Context context, SQLiteDatabase db){
        try{
            db = context.openOrCreateDatabase("dictionary", context.MODE_PRIVATE, null);

            db.execSQL("DROP TABLE IF EXISTS words");

            db.execSQL("CREATE TABLE IF NOT EXISTS words (word VARCHAR)");

            getData(db);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getData(SQLiteDatabase db) throws IOException {
        Call<ResponseBody> call = dataDictionary.getWords();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String responseBody = "";
                    try{
                        responseBody = response.body().string();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    String[] words = responseBody.split("[\r\n]+");
                    for(int i = 0; i <= words.length; i++){
                        String wordResponse = words[i];
                        if(wordResponse.contains("'")){
                            String wordCorrect = wordResponse.replace("'", "");
                            db.execSQL("INSERT INTO words (word) VALUES ('"+wordCorrect+"')");
                        }else{
                            db.execSQL("INSERT INTO words (word) VALUES ('"+wordResponse+"')");
                        }
                    }

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.showData(db, adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Erro", t.getMessage());
            }
        });
    }
}
