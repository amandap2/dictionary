package com.example.dictionary;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.dictionary.adapter.WordsAdapter;
import com.example.dictionary.api.DataDictionary;
import com.example.dictionary.database.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class MainActivity extends AppCompatActivity implements WordsAdapter.ItemSelectedListener {

    public SQLiteDatabase db;
    private WordsAdapter adapter;
    Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRecycler();
        database.onCreate(getBaseContext(), db, adapter);
    }

    private void startRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView rvWords = (RecyclerView) findViewById(R.id.dictionary_recycler);
        rvWords.setLayoutManager(layoutManager);
        rvWords.setHasFixedSize(true);
        adapter = new WordsAdapter(this);
        rvWords.setAdapter(adapter);
    }

    public void showData(SQLiteDatabase databaseReady, WordsAdapter adapterFromDB){
        ArrayList<String> response = new ArrayList<>();
        Cursor cursor = databaseReady.rawQuery("SELECT word FROM words", null);

        int wordIndex = cursor.getColumnIndex("word");
        cursor.moveToFirst();
        while(!cursor.isLast()) {
            response.add(cursor.getString(wordIndex));
            cursor.moveToNext();
        }
        adapterFromDB.addAll(response);
    }

    @Override
    public void onItemSelected(String selected) {
        Intent intent = new Intent(this, WordMeaning.class);
        intent.putExtra(WordMeaning.word, selected);
        startActivity(intent);
    }
}