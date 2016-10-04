package com.example.pokedex.AsynchTasks;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.R;
import com.example.pokedex.View.RecyclerAdapter;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.storage.DBOpenHelper;
import com.example.pokedex.storage.PokemonContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Davian on 25/09/16.
 */
public class InitialListLoader extends AsyncTask<Void, Void, ArrayList<Pokemon>> implements InitialListLoaderCallback {
    String apiURL = "http://pokeapi.co/api/v2/pokemon-species/?limit=151";
    RequestQueue requestQueue;
    ArrayList<Pokemon> pokemonToReturn = new ArrayList<>();
    private Context mContext;
    InitialListLoaderCallback mCallback = null;
    private Activity myActivity;


    public void setmListener(InitialListLoaderCallback callback) {
        mCallback = callback;
    }


    public InitialListLoader(Context context, Activity myActivity) {
        mContext = context;
        this.myActivity = myActivity;
    }


    @Override
    protected ArrayList<Pokemon> doInBackground(Void... voids) {
        Log.d("Debug", "AsyncTask Executed");
        requestQueue = Volley.newRequestQueue(mContext);

        JsonObjectRequest listObject = new JsonObjectRequest(Request.Method.GET, apiURL,

                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray returnedList = response.getJSONArray("results");
                            Log.d("Debug", "onResponse worked");


                            for (int i = 0; i < 151; i++) {


                                JSONObject currentPokemon = returnedList.getJSONObject(i);
                                String name = currentPokemon.getString("name");
                                String storedName = name.substring(0, 1).toUpperCase() + name.substring(1);

                                int id = i + 1;


                                Pokemon pokemon = new Pokemon();
                                pokemon.setName(storedName);
                                pokemon.setId(id);


                                pokemonToReturn.add(pokemon);


                                SpriteLoader spriteLoader = new SpriteLoader(mContext, myActivity);
                                spriteLoader.setmListener(InitialListLoader.this);
                                spriteLoader.execute(pokemon);


                            }
                            Log.d("Debug", "For loop exited");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        error.printStackTrace();
                    }
                }

        );
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        listObject.setRetryPolicy(policy);


        requestQueue.add(listObject);
        Log.d("Debug", "Request added");
        return pokemonToReturn;

    }


    @Override
    public void onTaskCompleted() {
        mCallback.onTaskCompleted();
    }
}

