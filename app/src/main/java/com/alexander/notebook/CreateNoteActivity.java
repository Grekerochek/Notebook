package com.alexander.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CreateNoteActivity extends AppCompatActivity {

    private static final String VALUE = "value";
    private static final String EMPTY = "";

    private EditText title;
    private EditText text;
    private Note note;
    private FloatingActionButton button;
    private ProgressBar load;

    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        initDBManager();
        initListeners();
    }

    private void initViews(){
        title = findViewById(R.id.titleCreate);
        text = findViewById(R.id.noteCreate);
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

                if (title.getText().toString().equals(EMPTY) || text.getText().toString().equals(EMPTY))
                    showMessage();
                else {
                    note = new Note();
                    note.setId(getIntent().getIntExtra(VALUE, 0));
                    note.setTitle(title.getText().toString());
                    note.setText(text.getText().toString());
                    new MyAsync().execute();
                }
            }
        });
    }

    private void showMessage(){
        Toast.makeText(this, R.string.message, Toast.LENGTH_SHORT).show();
    }

    public static final Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, CreateNoteActivity.class);
        intent.putExtra(VALUE, id);
        return intent;
    }

    private class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbManager.addNote(note);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load.setVisibility(View.INVISIBLE);
            startActivity(ListNotesActivity.newIntent(CreateNoteActivity.this, true));
        }
    }
}
