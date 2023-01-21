package com.example.dictionary;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.api.Api;
import com.example.dictionary.api.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordMeaning extends AppCompatActivity {
    public static String word = "word";
    private String selectedWord = "";
    private TextView textWord, textWordPhonetic, textGoBack, textViewMeaning;
    private Button buttonBack, buttonFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        initcomponentsScreen();
        loadComponents();
        getWordAPI();
    }
    private void initcomponentsScreen(){

        textWord = findViewById(R.id.textViewWord);
        textWordPhonetic = findViewById(R.id.textViewWordPhonetic);
        textViewMeaning = findViewById(R.id.textViewMeaning);
        textGoBack = findViewById(R.id.textViewGoBack);

        buttonBack = findViewById(R.id.buttonBack);
        buttonFavorite = findViewById(R.id.buttonFavorite);

        textGoBack.setOnClickListener(view -> {
            finish();
        });

        buttonBack.setOnClickListener(view -> {
            finish();
        });
    }
    private void loadComponents(){
        selectedWord = getIntent().getExtras().getString(word);
    }
    private void getJsonResponse(Object o){
        try{
            JSONArray mainObject = new JSONArray(String.valueOf(o));
            String responsePhonetic = "";
            String responseWord = mainObject.getJSONObject(0).getString("word");
            try{
                responsePhonetic = mainObject.getJSONObject(0).getString("phonetic");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            JSONArray meanings = mainObject.getJSONObject(0).getJSONArray("meanings");
            String meaning1 = meanings.getJSONObject(0).getString("partOfSpeech");

            JSONArray definitions = meanings.getJSONObject(0).getJSONArray("definitions");
            String meaning2 = definitions.getJSONObject(0).getString("definition");

            String meaning = meaning1 + ": " + meaning2;

            textWord.setText(responseWord);
            textWordPhonetic.setText(responsePhonetic);
            textViewMeaning.setText(meaning);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getWordAPI(){
        Api api = new Api(this, "https://api.dictionaryapi.dev/api/v2/entries/en/");
        api.getWordDescription(selectedWord, new ResponseListener() {
            @Override
            public void onSuccess(@NonNull Object o) {
                getJsonResponse(o);
            }

            @Override
            public void onFailure(@NonNull String errorMessage) {
                Toast.makeText(WordMeaning.this, "Palavra não encontrada! Retorne para a página inicial!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
