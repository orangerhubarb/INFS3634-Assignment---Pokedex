package com.example.pokedex.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.pokedex.model.Pokemon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Davian on 17/09/16.
 */
public class PokemonContract {
    public static final String TABLE_NAME = "pokemon";
    private final SQLiteOpenHelper dbHelper;

    public PokemonContract(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PokemonEntry._ID + " INTEGER PRIMARY KEY," +
                    PokemonEntry.COLUMN_NAME + " TEXT," +
                    PokemonEntry.COLUMN_ID + " INT,"  +
                    PokemonEntry.COLUMN_SPRITE + " BLOB," +
                    PokemonEntry.COLUMN_BIGIMAGE + " BLOB," +
                    PokemonEntry.COLUMN_FLAVOURTEXT + " TEXT," +
                    PokemonEntry.COLUMN_JSONOBJECT + " TEXT" + " )";

    public abstract class PokemonEntry implements BaseColumns {
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SPRITE = "sprite";
        public static final String COLUMN_JSONOBJECT = "jsonobject";
        public static final String COLUMN_BIGIMAGE = "bigimage";
        public static final String COLUMN_FLAVOURTEXT = "flavourtext";

    }

    public long insertWhole(Pokemon pokemon, String JSONObject) {
        Log.d("Debug", "Attempting to getWritable");
        SQLiteDatabase db  = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PokemonEntry.COLUMN_NAME, pokemon.getName());
        values.put(PokemonEntry.COLUMN_ID, pokemon.getId());
        values.put(PokemonEntry.COLUMN_JSONOBJECT, JSONObject);

        long newRowId;
        newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public long insertNew(Pokemon pokemon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(PokemonEntry.COLUMN_NAME, pokemon.getName());
        values.put(PokemonEntry.COLUMN_ID, pokemon.getId());

        byte[] img = getBitmapAsByteArray(pokemon.getSprite());
        values.put(PokemonEntry.COLUMN_SPRITE, img);

        long newRowId;
        newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }


    public ArrayList<Pokemon> getPokemon() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                PokemonEntry._ID,
                PokemonEntry.COLUMN_NAME,
                PokemonEntry.COLUMN_ID,
                PokemonEntry.COLUMN_SPRITE,
                PokemonEntry.COLUMN_JSONOBJECT
        };

        String sortOrder = PokemonEntry.COLUMN_ID;

        Cursor cur = db.query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        while (cur.moveToNext()){
            Pokemon pokemon = new Pokemon();
            pokemon.setName(cur.getString(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)));
            pokemon.setId(cur.getInt(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_ID)));

            
            pokemonList.add(pokemon);
        }

        cur.close();
        db.close();
        return pokemonList;
    }

    public ArrayList<Pokemon> getPokemonForRecycler() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                PokemonEntry._ID,
                PokemonEntry.COLUMN_NAME,
                PokemonEntry.COLUMN_ID,
                PokemonEntry.COLUMN_SPRITE,

        };

        String sortOrder = PokemonEntry.COLUMN_ID;

        Cursor cur = db.query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        while (cur.moveToNext()){
            Pokemon pokemon = new Pokemon();
            pokemon.setName(cur.getString(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)));
            pokemon.setId(cur.getInt(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_ID)));

            byte[] img = cur.getBlob(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_SPRITE));
            pokemon.setSprite(BitmapFactory.decodeByteArray(img, 0, img.length));

            pokemonList.add(pokemon);
        }

        cur.close();
        db.close();
        return pokemonList;
    }

    public Pokemon getPokemon(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = PokemonEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        String[] columns = {
                PokemonEntry._ID,
                PokemonEntry.COLUMN_NAME,
                PokemonEntry.COLUMN_ID,
                PokemonEntry.COLUMN_SPRITE,
                PokemonEntry.COLUMN_JSONOBJECT,
                PokemonEntry.COLUMN_BIGIMAGE,
                PokemonEntry.COLUMN_FLAVOURTEXT,

        };

        Cursor cur = db.query(
                TABLE_NAME,  // The table to query
                columns,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Pokemon pokemon = null;
        if(cur.moveToNext()) {
            pokemon = new Pokemon();
            pokemon.setName(cur.getString(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)));
            pokemon.setId(cur.getInt(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_ID)));

            byte[] img = cur.getBlob(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_SPRITE));
            pokemon.setSprite(BitmapFactory.decodeByteArray(img, 0, img.length));
            Log.d("Debug", pokemon.getSprite().toString());

            pokemon.setJSONObject(cur.getString(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_JSONOBJECT)));
            pokemon.setFlavourText(cur.getString(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_FLAVOURTEXT)));

            if (cur.getBlob(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_BIGIMAGE)) != null) {
                byte[] bigImg = cur.getBlob(cur.getColumnIndexOrThrow(PokemonEntry.COLUMN_BIGIMAGE));
                pokemon.setBigImage(BitmapFactory.decodeByteArray(bigImg, 0, bigImg.length));
                Log.d("Debug", pokemon.getBigImage().toString());
            }

        }

        cur.close();
        db.close();
        return pokemon;



    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
