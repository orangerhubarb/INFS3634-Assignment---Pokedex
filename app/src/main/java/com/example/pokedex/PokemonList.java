package com.example.pokedex;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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
    private EditText searchPokemon;
    private ArrayList<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        searchPokemon = (EditText) findViewById(R.id.searchPokemon);
        searchPokemonTextListener();

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        PokemonContract pokemonContract = new PokemonContract(helper);
        pokemonList = pokemonContract.getPokemonForRecycler();
        Log.d("Debug", Integer.toString(pokemonList.size()));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new RecyclerAdapter(pokemonList);
        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

    }

    public void searchPokemonTextListener() {
        searchPokemon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();

                final ArrayList<Pokemon> newList  = new ArrayList<>();

                for(int i = 0 ; i < pokemonList.size() ; i++) {
                    final String pokemonSearch = pokemonList.get(i).getName().toLowerCase();
                    if(pokemonSearch.contains(s)) {
                        newList.add(pokemonList.get(i));
                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(PokemonList.this));
                mAdapter = new RecyclerAdapter(newList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
