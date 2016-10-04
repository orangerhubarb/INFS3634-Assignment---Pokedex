package com.example.pokedex;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pokedex.AsynchTasks.InitialListLoader;
import com.example.pokedex.AsynchTasks.InitialListLoaderCallback;
import com.example.pokedex.View.RecyclerAdapter;
import com.example.pokedex.View.SimpleDividerItemDecoration;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.storage.DBOpenHelper;
import com.example.pokedex.storage.PokemonContract;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InitialListLoaderCallback {

    private ProgressBar progressBar;
    private Typeface tf1;
    private Typeface tf2;
    private TextView loadingText;
    private TextView mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.loadingText);
        mainTitle = (TextView) findViewById(R.id.mainTitle);



        tf1 = Typeface.createFromAsset(this.getAssets(), "Sansation_Bold.ttf");
        loadingText.setTypeface(tf1);

        tf2 = Typeface.createFromAsset(this.getAssets(), "Sansation_Bold_Italic.ttf");
        mainTitle.setTypeface(tf2);


        if (!doesDBExist()) {
            Log.d("Debug", "Database doesn't exist");
            progressBar.setMax(649);
            InitialListLoader initialListLoader = new InitialListLoader(this, this);
            initialListLoader.setmListener(this);
            initialListLoader.execute();

        } else {
            Log.d("Debug", "Database Exists");
            Intent intent = new Intent(this, PokemonList.class);
            startActivity(intent);
            finish();
        }


    }


    public boolean doesDBExist() {
        File dbFile = this.getDatabasePath("pokedex.db");
        if (dbFile.exists()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onTaskCompleted() {
        Log.d("Debug", "Callback worked");
        Intent intent = new Intent(this, PokemonList.class);
        startActivity(intent);
        finish();
    }

}




