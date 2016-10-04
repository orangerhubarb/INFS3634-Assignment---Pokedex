package com.example.pokedex.View;

/**
 * Created by Davian on 26/09/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokedex.R;
import com.example.pokedex.model.Pokemon;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PokemonHolder> {

    private ArrayList<Pokemon> mPokemon;


    public static class PokemonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pokemonSprite;
        private TextView pokemonID;
        private TextView pokemonName;
        private Pokemon pokemon;
        Typeface tf;

        private static final String POKEMON_KEY = "POKEMON";

        public PokemonHolder(View view) {
            super(view);

            pokemonSprite = (ImageView) view.findViewById(R.id.pokemon_sprite);
            pokemonID = (TextView) view.findViewById(R.id.pokemon_id);
            pokemonName = (TextView) view.findViewById(R.id.pokemon_name);

            view.setOnClickListener(this);

            tf = Typeface.createFromAsset(view.getContext().getAssets(), "Sansation_Regular.ttf");
            this.pokemonID.setTypeface(tf);
            this.pokemonName.setTypeface(tf);


        }

        public void bindPokemon (Pokemon pokemon) {
            this.pokemon = pokemon;

            DecimalFormat leadngZeros = new DecimalFormat("000");
            pokemonID.setText("#" + leadngZeros.format(pokemon.getId()));

            pokemonName.setText(pokemon.getName());
            pokemonSprite.setImageBitmap(pokemon.getSprite());
        }


        @Override
        public void onClick(View view) {
            Context context = itemView.getContext();
            Intent showPokemon = new Intent(context, PokemonActivity.class);
            showPokemon.putExtra(POKEMON_KEY, pokemon.getId());

            context.startActivity(showPokemon);
        }
    }

    public RecyclerAdapter(ArrayList<Pokemon> pokemon) {
        mPokemon = pokemon;

    }

    @Override
    public RecyclerAdapter.PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new PokemonHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PokemonHolder holder, int position) {
        Pokemon itemPokemon = mPokemon.get(position);
        holder.bindPokemon(itemPokemon);

    }


    @Override
    public int getItemCount() {
        return mPokemon.size();
    }


}
