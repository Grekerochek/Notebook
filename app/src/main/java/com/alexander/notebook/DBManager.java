package com.alexander.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String TEXT = "text";
    private final static String TEXT_SIZE = "textSize";
    private final static String TEXT_COLOR = "textColor";

    private final static String TABLE_NAME = "NOTES";

    private DBHelper dbHelper;

    public DBManager(Context context){
        this.dbHelper = new DBHelper(context);
    }

    public void addNote(Note note){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = getContentValues(note);
            addNoteInternal(db, contentValues);
            db.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction())
                    db.endTransaction();
                db.close();
            }
        }
    }

    public List<Note> getNotes(){
        List<Note> notes = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            notes = parseCursorList(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()){
                    db.endTransaction();
                }
                db.close();
            }
        }
        return notes;
    }

    public Note getNote(int id){
        Note note = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(TABLE_NAME, null, ID + "= ?", new String[]{String.valueOf(id)}, null, null, null);
            note = parseCursorId(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()){
                    db.endTransaction();
                }
                db.close();
            }
        }
        return note;
    }

    public void editNote(Note note){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.update(TABLE_NAME, getContentValues(note), ID + "= ?", new String[]{String.valueOf(note.getId())});
            db.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction())
                    db.endTransaction();
                db.close();
            }
        }
    }


    private ContentValues getContentValues(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, note.getId());
        contentValues.put(TITLE, note.getTitle());
        contentValues.put(TEXT, note.getText());
        contentValues.put(TEXT_SIZE, note.getTextSize());
        contentValues.put(TEXT_COLOR, note.getTextColor());
        return contentValues;
    }

    private void addNoteInternal(SQLiteDatabase db, ContentValues contentValues){
        db.insert(TABLE_NAME, null, contentValues);
    }

    private Note parseCursor(Cursor cursor){
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        note.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
        note.setTextSize(cursor.getInt(cursor.getColumnIndex(TEXT_SIZE)));
        note.setTextColor(cursor.getInt(cursor.getColumnIndex(TEXT_COLOR)));
        return note;
    }

    private Note parseCursorId(Cursor cursor){
        Note note = new Note();
        if (cursor.moveToFirst()){
            note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
            note.setTextSize(cursor.getInt(cursor.getColumnIndex(TEXT_SIZE)));
            note.setTextColor(cursor.getInt(cursor.getColumnIndex(TEXT_COLOR)));
            return note;
        }
        return null;
    }

    private List<Note> parseCursorList(Cursor cursor){
        List<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()){
            Note note = parseCursor(cursor);
            Log.v("PROVERKA", note.toString());
            notes.add(note);
        }
        return notes;
    }

}
