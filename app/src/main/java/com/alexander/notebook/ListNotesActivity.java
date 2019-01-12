package com.alexander.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    private static final String VALUE = "value";
    private Adapter adapter;
    private List<Note> notes;

    private FloatingActionButton button;
    private ProgressBar load;

    private DBManager dbManager;

    private boolean isAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        initListeners();
        initDBManager();
        /*isAdded = getIntent().getBooleanExtra(VALUE, false);
        if (isAdded){
            new MyAsync().execute();
        }*/
        new MyAsync().execute();
    }

    private void initViews(){
        button = findViewById(R.id.button);
        load = findViewById(R.id.load);
    }

    private void initDBManager(){
        dbManager = new DBManager(this);
    }

    private void initListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateNoteActivity.newIntent(ListNotesActivity.this, notes.size()));
            }
        });
    }

    private void initRecyclerView(){

        notes = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);

    }

    public static final Intent newIntent(Context context, boolean isAdded){
        Intent intent = new Intent(context, ListNotesActivity.class);
        intent.putExtra(VALUE, isAdded);
        return intent;
    }

    private class MyAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
           notes = dbManager.getNotes();
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setData(notes);
            load.setVisibility(View.INVISIBLE);
            Log.v("PROVERKA", notes.toString());
        }
    }
}
