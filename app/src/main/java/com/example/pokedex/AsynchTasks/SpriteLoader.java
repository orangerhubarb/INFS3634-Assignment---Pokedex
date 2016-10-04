package com.example.pokedex.AsynchTasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.pokedex.R;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.storage.DBOpenHelper;
import com.example.pokedex.storage.PokemonContract;

import java.io.InputStream;

/**
 * Created by Davian on 27/09/16.
 */
public class SpriteLoader extends AsyncTask<Pokemon, Void, Pokemon> {

    private Context context;
    private Activity myActivity;
    private ProgressBar progressBar;

    InitialListLoaderCallback mCallback = null;

    public void setmListener(InitialListLoaderCallback callback) {
        mCallback = callback;
    }

    public SpriteLoader(Context context, Activity myActivity) {
        this.context = context;
        this.myActivity = myActivity;
    }



    protected Pokemon doInBackground(Pokemon... pokemons) {
        Pokemon pokemon = pokemons[0];

        int id = pokemon.getId();

        String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        pokemon.setSprite(bitmap);
        Log.d("Debug", pokemon.toString());

        return pokemon;
    }

    protected void onPostExecute(Pokemon pokemon) {
        DBOpenHelper helper = new DBOpenHelper(context);
        PokemonContract pokemonContract = new PokemonContract(helper);

        progressBar = (ProgressBar) myActivity.findViewById(R.id.progressBar);
        progressBar.incrementProgressBy(1);

        if(pokemonContract.insertNew(pokemon) == 151) {
            mCallback.onTaskCompleted();
        }
    }
}