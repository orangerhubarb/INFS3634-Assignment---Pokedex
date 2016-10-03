package com.example.pokedex.View;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.pokedex.R;
import com.example.pokedex.model.Evolution;
import com.example.pokedex.model.Moves;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.model.Stats;
import com.example.pokedex.storage.DBOpenHelper;
import com.example.pokedex.storage.PokemonContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Collections;

public class PokemonActivity extends AppCompatActivity {

    private TextView pokemonName;
    private TextView flavourText;
    private ImageView bigImage;
    private ImageView sprite;
    private TextView abilitiesText;
    private TextView type1;
    private TextView heightText;
    private TextView weightText;

    private TextView hpText;
    private TextView atkText;
    private TextView defText;
    private TextView spAtkText;
    private TextView spDefText;
    private TextView speedText;
    private ProgressBar hpValue;
    private ProgressBar atkValue;
    private ProgressBar defValue;
    private ProgressBar spAtkValue;
    private ProgressBar spDefValue;
    private ProgressBar speedValue;

    private TableLayout evolutionsTable;
    private TextView noEvolutions;

    private TableLayout movesTable;
    private RelativeLayout typesLayout;

    private TextView basicProfile;
    private TextView baseStats;
    private TextView spriteText;
    private TextView evolutionsTitle;
    private TextView learnedMovesTitle;






    private int selectedPokemonID;
    private String pokemonJSON;
    private PokemonContract pokemonContract;
    Pokemon pokemon;
    RequestQueue requestQueue;
    Typeface tf;
    Typeface tf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        tf = Typeface.createFromAsset(this.getAssets(), "Sansation_Regular.ttf");
        tf1 = Typeface.createFromAsset(this.getAssets(), "Sansation_Bold.ttf");

        requestQueue = Volley.newRequestQueue(this);

        bigImage = (ImageView) findViewById(R.id.big_image);
        sprite = (ImageView) findViewById(R.id.pokemon_sprite);
        pokemonName = (TextView) findViewById(R.id.pokemon_name_full);
        typesLayout = (RelativeLayout) findViewById(R.id.typesLayout);
        this.pokemonName.setTypeface(tf);

        flavourText = (TextView) findViewById(R.id.flavour_text);
        this.flavourText.setTypeface(tf);

        abilitiesText = (TextView) findViewById(R.id.abilitiesText);
        this.abilitiesText.setTypeface(tf);

        type1 = (TextView) findViewById(R.id.type1);
        this.type1.setTypeface(tf);

        heightText = (TextView) findViewById(R.id.heightText);
        this.heightText.setTypeface(tf);
        weightText = (TextView) findViewById(R.id.weightText);
        this.weightText.setTypeface(tf);

        hpText = (TextView) findViewById(R.id.hpText);
        this.hpText.setTypeface(tf);
        atkText = (TextView) findViewById(R.id.atkText);
        this.atkText.setTypeface(tf);
        defText = (TextView) findViewById(R.id.defText);
        this.defText.setTypeface(tf);
        spAtkText = (TextView) findViewById(R.id.spAtkText);
        this.spAtkText.setTypeface(tf);
        spDefText = (TextView) findViewById(R.id.spDefText);
        this.spDefText.setTypeface(tf);
        speedText = (TextView) findViewById(R.id.speedText);
        this.speedText.setTypeface(tf);
        hpValue = (ProgressBar) findViewById(R.id.hpValueBar);
        atkValue = (ProgressBar) findViewById(R.id.atkValueBar);
        defValue = (ProgressBar) findViewById(R.id.defValueBar);
        spAtkValue = (ProgressBar) findViewById(R.id.spAtkValueBar);
        spDefValue = (ProgressBar) findViewById(R.id.spDefValueBar);
        speedValue = (ProgressBar) findViewById(R.id.speedValueBar);

        evolutionsTable = (TableLayout) findViewById(R.id.evolutionsTable);
        noEvolutions = (TextView) findViewById(R.id.noEvolutions);

        movesTable = (TableLayout) findViewById(R.id.movesTable);

        basicProfile = (TextView) findViewById(R.id.basicprofile);
        basicProfile.setTypeface(tf1);
        basicProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

        baseStats = (TextView) findViewById(R.id.baseStats);
        baseStats.setTypeface(tf1);
        baseStats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

        evolutionsTitle = (TextView) findViewById(R.id.evolutionsTitle);
        evolutionsTitle.setTypeface(tf1);
        evolutionsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

        learnedMovesTitle = (TextView) findViewById(R.id.learnedMovesTitle);
        learnedMovesTitle.setTypeface(tf1);
        learnedMovesTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

        spriteText = (TextView) findViewById(R.id.sprite);
        spriteText.setTypeface(tf1);
        spriteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);




        selectedPokemonID = (int) getIntent().getSerializableExtra("POKEMON");

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

        pokemonContract = new PokemonContract(dbOpenHelper);
        pokemon = pokemonContract.getPokemon(selectedPokemonID);

        if (pokemon.getJSONObject() == null) {
            Log.d("Debug", "Time to download a new Pokemon!");
            String url = "http://pokeapi.co/api/v1/pokemon/" + selectedPokemonID;
            JsonObjectRequest pokemonInfo = new JsonObjectRequest(Request.Method.GET, url,

                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pokemonJSON = response.toString();
                            pokemon.setJSONObject(pokemonJSON);


                            DecimalFormat leadngZeros = new DecimalFormat("000");
                            String urlAdder = leadngZeros.format(pokemon.getId()) + pokemon.getName() + ".png";


                            Glide.with(PokemonActivity.this).load("https://raw.githubusercontent.com/fanzeyi/Pokemon-DB/master/img/" + urlAdder).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    pokemon.setBigImage(resource);
                                    Log.d("Debug", "Pokemon Image has been set");

                                    DBOpenHelper dbHelper = new DBOpenHelper(PokemonActivity.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues cv = new ContentValues();
                                    Log.d("Debug", "Content Values for big image has been created");

                                    byte[] img = pokemonContract.getBitmapAsByteArray(pokemon.getBigImage());
                                    cv.put("bigimage", img);
                                    cv.put("jsonobject", pokemonJSON);
                                    Log.d("Debug", "Content Values put method has been used");

                                    db.update("pokemon", cv, "id=" + selectedPokemonID, null);
                                    Log.d("Debug", "Successfully added bigimage");
                                    db.close();

                                    getFlavourText();

                                }
                            });

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
            int socketTimeout = 15000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            pokemonInfo.setRetryPolicy(policy);
            requestQueue.add(pokemonInfo);
        } else {
            setPokemonDetails(pokemon);

        }


    }

    public void getFlavourText() {
        String url = "http://pokeapi.co/api/v2/pokemon-species/" + pokemon.getId();
        JsonObjectRequest flavourRequest = new JsonObjectRequest(Request.Method.GET, url,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray flavourArray = response.getJSONArray("flavor_text_entries");
                            JSONObject flavourObject = flavourArray.getJSONObject(1);
                            String flavourTextRaw = flavourObject.getString("flavor_text");

                            String flavourText = flavourTextRaw.replace("\\n", " ");
                            Log.d("Debug", flavourText);

                            pokemon.setFlavourText(flavourText);
                            DBOpenHelper dbHelper = new DBOpenHelper(PokemonActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("flavourtext", flavourText);


                            db.update("pokemon", cv, "id=" + selectedPokemonID, null);
                            Log.d("Debug", "Flavour Text added");
                            db.close();

                            //Set the remaining values for the Pokemon based off saved JSON
                            setPokemonDetails(pokemon);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        error.printStackTrace();
                    }
                }

        );
        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        flavourRequest.setRetryPolicy(policy);
        requestQueue.add(flavourRequest);
    }


    public void setPokemonDetails(Pokemon pokemon) {
        try {
            JSONObject response = new JSONObject(pokemon.getJSONObject());

            DecimalFormat df = new DecimalFormat("#.##");

            //Height
            double height = (response.getInt("height") * 0.1);
            pokemon.setHeight(height);

            heightText.setText(df.format(height) + "m");


            //Weight
            double weight = (response.getInt("weight") / 10.0);
            pokemon.setWeight(weight);

            weightText.setText(df.format(weight) + "kg");



            //Stats
            int att = response.getInt("attack");
            int def = response.getInt("defense");
            int speed = response.getInt("speed");
            int spatk = response.getInt("sp_atk");
            int spdef = response.getInt("sp_def");
            int hp = response.getInt("hp");

            Stats stats = new Stats(hp, att, def, spatk, spdef, speed);
            pokemon.setStats(stats);


            //Abilities
            ArrayList<String> abilities = new ArrayList<>();
            JSONArray abilitiesArray = response.getJSONArray("abilities");
            for (int i = 0; i < abilitiesArray.length(); i++) {
                JSONObject ability = abilitiesArray.getJSONObject(i);
                String abilityName = ability.getString("name");
                String storedName = abilityName.substring(0, 1).toUpperCase() + abilityName.substring(1);
                abilities.add(storedName);
            }



            pokemon.setAbilities(abilities);


            //Moves
            ArrayList<Moves> movesList = new ArrayList<>();
            JSONArray movesArray = response.getJSONArray("moves");
            for (int i = 0; i < movesArray.length(); i++) {
                JSONObject move = movesArray.getJSONObject(i);
                if (move.getString("learn_type").equals("level up")) {
                    int level = move.getInt("level");
                    String name = move.getString("name");

                    Moves moves = new Moves(name, level);
                    movesList.add(moves);
                }

            }
            pokemon.setMoves(movesList);

            //Evolution
            JSONArray evolutionsArray = response.getJSONArray("evolutions");
            if (evolutionsArray.length() > 0) {
                ArrayList<Evolution> evolutionsList = new ArrayList<>();
                for (int i = 0; i < evolutionsArray.length(); i++) {
                    JSONObject evolutionObject = evolutionsArray.getJSONObject(i);
                    String name = evolutionObject.getString("to");
                    String evolveMethod;
                    Evolution evolution = new Evolution(name);
                    Log.d("Debug", "Evolutions are being created");

                    if(i == 1 && evolutionObject.getString("to").equals(evolutionsArray.getJSONObject(0).getString("to"))) {
                        continue;
                    }
                    else {
                        //The API i'm using has Nidoking evolving to Clefairy, so I've had to make an exception here for it. (Nidoking shouldn't evolve to anything)
                        if (!pokemon.getName().equals("Nidoking") && !pokemon.getName().equals("Arbok") ) {
                            if (evolutionObject.getString("method").equals("level_up")) {
                                Log.d("Debug", pokemon.getName());
                                if(!(pokemon.getName().equals("Eevee"))) {

                                    int evolveLevel = evolutionObject.getInt("level");
                                    evolution.setEvolveMethod("Level up");
                                    evolution.setLevelEvolved(evolveLevel);
                                    evolutionsList.add(evolution);
                                    Log.d("Debug", "Evolutions are being added");
                                }

                                else {
                                    evolution.setEvolveMethod("Level up");
                                    evolutionsList.add(evolution);
                                    Log.d("Debug", "Eevee exception");
                                }

                            } else {
                                evolveMethod = evolutionObject.getString("method");
                                evolution.setEvolveMethod(evolveMethod);
                                evolutionsList.add(evolution);
                                Log.d("Debug", "Evolutions are being added");
                            }
                        }
                    }


                }
                pokemon.setEvolution(evolutionsList);
            }


            //Types
            ArrayList<String> typesList = new ArrayList<>();
            JSONArray typesArray = response.getJSONArray("types");
            for (int i = 0; i < typesArray.length(); i++) {
                JSONObject type = typesArray.getJSONObject(i);
                String name = type.getString("name");
                String storedName = name.substring(0, 1).toUpperCase() + name.substring(1);
                typesList.add(storedName);
                Log.d("Debug", storedName);
            }
            pokemon.setTypes(typesList);
            type1.setText(pokemon.getTypes().get(0));
            switch (pokemon.getTypes().get(0)) {
                case "Normal" : setText1(pokemon.getTypes().get(0), "#A8A77A"); break;
                case "Fire" : setText1(pokemon.getTypes().get(0), "#EE8130"); break;
                case "Water" : setText1(pokemon.getTypes().get(0), "#6390F0"); break;
                case "Electric" : setText1(pokemon.getTypes().get(0), "#F7D02C"); break;
                case "Grass" : setText1(pokemon.getTypes().get(0), "#7AC74C"); break;
                case "Ice" : setText1(pokemon.getTypes().get(0), "#96D9D6"); break;
                case "Fighting" : setText1(pokemon.getTypes().get(0), "#C22E28"); break;
                case "Poison" : setText1(pokemon.getTypes().get(0), "#A33EA1"); break;
                case "Ground" : setText1(pokemon.getTypes().get(0), "#E2BF65"); break;
                case "Flying" : setText1(pokemon.getTypes().get(0), "#A98FF3"); break;
                case "Psychic" : setText1(pokemon.getTypes().get(0), "#F95587"); break;
                case "Bug" : setText1(pokemon.getTypes().get(0), "#A6B91A"); break;
                case "Rock" : setText1(pokemon.getTypes().get(0), "#B6A136"); break;
                case "Ghost" : setText1(pokemon.getTypes().get(0), "#735797"); break;
                case "Dragon" : setText1(pokemon.getTypes().get(0), "#6F35FC"); break;
                case "Dark" : setText1(pokemon.getTypes().get(0), "#705746"); break;
                case "Steel" : setText1(pokemon.getTypes().get(0), "#B7B7CE"); break;
                case "Fairy" : setText1(pokemon.getTypes().get(0), "#D685AD"); break;

            }


            if(pokemon.getTypes().size() > 1) {

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                final float scale = getResources().getDisplayMetrics().density;
                int margin1Set = (int) (5 * scale + 0.5f);

                layoutParams.setMargins(margin1Set, 0, 0, 0);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.type1);
                TextView type2 = new TextView(this);
                type2.setLayoutParams(layoutParams);



                int padding1Set = (int) (10 * scale + 0.5f);
                int padding2Set = (int) (5 * scale + 0.5f);

                type2.setPadding(padding1Set, padding2Set, padding1Set, padding2Set);
                type2.setGravity(View.TEXT_ALIGNMENT_CENTER);



                type2.setText(pokemon.getTypes().get(1));
                switch (pokemon.getTypes().get(1)) {
                    case "Normal" : setText2(pokemon.getTypes().get(1), "#A8A77A", type2); break;
                    case "Fire" : setText2(pokemon.getTypes().get(1), "#EE8130", type2); break;
                    case "Water" : setText2(pokemon.getTypes().get(1), "#6390F0", type2); break;
                    case "Electric" : setText2(pokemon.getTypes().get(1), "#F7D02C", type2); break;
                    case "Grass" : setText2(pokemon.getTypes().get(1), "#7AC74C", type2); break;
                    case "Ice" : setText2(pokemon.getTypes().get(1), "#96D9D6", type2); break;
                    case "Fighting" : setText2(pokemon.getTypes().get(1), "#C22E28", type2); break;
                    case "Poison" : setText2(pokemon.getTypes().get(1), "#A33EA1", type2); break;
                    case "Ground" : setText2(pokemon.getTypes().get(1), "#E2BF65", type2); break;
                    case "Flying" : setText2(pokemon.getTypes().get(1), "#A98FF3", type2); break;
                    case "Psychic" : setText2(pokemon.getTypes().get(1), "#F95587", type2); break;
                    case "Bug" : setText2(pokemon.getTypes().get(1), "#A6B91A", type2); break;
                    case "Rock" : setText2(pokemon.getTypes().get(1), "#B6A136", type2); break;
                    case "Ghost" : setText2(pokemon.getTypes().get(1), "#735797", type2); break;
                    case "Dragon" : setText2(pokemon.getTypes().get(1), "#6F35FC", type2); break;
                    case "Dark" : setText2(pokemon.getTypes().get(1), "#705746", type2); break;
                    case "Steel" : setText2(pokemon.getTypes().get(1), "#B7B7CE", type2); break;
                    case "Fairy" : setText2(pokemon.getTypes().get(1), "#D685AD", type2); break;

                }
                type2.setTypeface(tf);

                typesLayout.addView(type2);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String text = pokemon.getFlavourText();
        flavourText.setText(text.replace("\n", " "));


        DecimalFormat leadngZeros = new DecimalFormat("000");

        pokemonName.setText("#" + leadngZeros.format(pokemon.getId()) + " " + pokemon.getName());
        pokemonName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        pokemonName.setTypeface(tf1);




        //Sprite and Big Image
        sprite.setImageBitmap(pokemon.getSprite());
        bigImage.setImageBitmap(pokemon.getBigImage());


        //Evolutions
        if (!(pokemon.getEvolution() == null)) {

            for (int i = 0; i < pokemon.getEvolution().size(); i++) {
                TableRow row = new TableRow(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(layoutParams);

                TextView evolvesTo = new TextView(this);
                evolvesTo.setText(pokemon.getEvolution().get(i).getName());
                evolvesTo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                evolvesTo.setTypeface(tf);



                row.addView(evolvesTo);
                Log.d("Debug", pokemon.getEvolution().get(i).getName());

                TextView evolveMethod = new TextView(this);
                evolveMethod.setText(pokemon.getEvolution().get(i).getEvolveMethod());
                row.addView(evolveMethod);
                evolveMethod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                Log.d("Debug", pokemon.getEvolution().get(i).getEvolveMethod());
                evolveMethod.setTypeface(tf);

                TextView levelEvolved = new TextView(this);
                levelEvolved.setText(Integer.toString(pokemon.getEvolution().get(i).getLevelEvolved()));
                row.addView(levelEvolved);
                levelEvolved.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                levelEvolved.setTypeface(tf);

                evolutionsTable.addView(row);
            }

        }

        else {
            evolutionsTable.setVisibility(View.GONE);
            noEvolutions.setText(pokemon.getName() + " has no evolutions");
            noEvolutions.setTypeface(tf);

        }


        //Moves
        Collections.sort(pokemon.getMoves());

        for (int i = 0; i < pokemon.getMoves().size(); i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);


            TextView moveName = new TextView(this);
            moveName.setText(pokemon.getMoves().get(i).getName());
            row.addView(moveName);
            moveName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            moveName.setTypeface(tf);

            TextView evolveLevel = new TextView(this);
            evolveLevel.setText(Integer.toString(pokemon.getMoves().get(i).getLevelLearnt()));
            evolveLevel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            TableRow.LayoutParams para =new TableRow.LayoutParams();
            para.gravity = Gravity.CENTER;
            evolveLevel.setLayoutParams(para);
            row.addView(evolveLevel);
            evolveLevel.setTypeface(tf);

            movesTable.addView(row);
        }


        //Stats
        hpText.setText(Integer.toString(pokemon.getStats().getHp()));
        hpValue.setProgress(pokemon.getStats().getHp());


        atkText.setText(Integer.toString(pokemon.getStats().getAtt()));
        atkValue.setProgress(pokemon.getStats().getAtt());

        defText.setText(Integer.toString(pokemon.getStats().getDef()));
        defValue.setProgress(pokemon.getStats().getDef());

        spAtkText.setText(Integer.toString(pokemon.getStats().getSpatk()));
        spAtkValue.setProgress(pokemon.getStats().getSpatk());

        spDefText.setText(Integer.toString(pokemon.getStats().getSpdef()));
        spDefValue.setProgress(pokemon.getStats().getSpdef());

        speedText.setText(Integer.toString(pokemon.getStats().getSpeed()));
        speedValue.setProgress(pokemon.getStats().getSpeed());


        //Abilities
        abilitiesText.setText(pokemon.getAbilities().toString().replace("[", "").replace("]", ""));



        Log.d("Debug", "JSON Parsed already");



    }

    public void setText1(String type, String hex) {
        type1.setBackgroundColor(Color.parseColor(hex));
        int resID = getResources().getIdentifier(type, "string", getPackageName());
        String string = getResources().getString(resID);

        type1.setText(Html.fromHtml(string));

    }

    public void setText2(String type, String hex, TextView type2) {
        type2.setBackgroundColor(Color.parseColor(hex));
        int resID = getResources().getIdentifier(type, "string", getPackageName());
        String string = getResources().getString(resID);

        type2.setText(Html.fromHtml(string));

    }

}

