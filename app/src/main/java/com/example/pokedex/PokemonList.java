package com.example.pokedex;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.pokedex.View.RecyclerAdapter;
import com.example.pokedex.View.SimpleDividerItemDecoration;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.storage.DBOpenHelper;
import com.example.pokedex.storage.PokemonContract;

import java.util.ArrayList;

public class PokemonList extends AppCompatActivity {
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private int testToSee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        PokemonContract pokemonContract = new PokemonContract(helper);
        ArrayList<Pokemon> pokemonList = pokemonContract.getPokemonForRecycler();
        Log.d("Debug", Integer.toString(pokemonList.size()));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new RecyclerAdapter(pokemonList);
        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

    }
}
