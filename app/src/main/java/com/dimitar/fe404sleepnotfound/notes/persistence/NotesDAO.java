package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.List;

@Dao
public interface NotesDAO {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM notes_table")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes_table WHERE noteId = :noteId")
    Note getNote(int noteId);

    @Query("SELECT * FROM notes_table WHERE noteText = :text")
    Note getNote(String text);

    @Query("UPDATE notes_table SET noteText = :noteText WHERE noteId = :oldId")
    void updateNote(int oldId, String noteText);

    @Delete
    void deleteNote(Note... notes);

    @Query("DELETE FROM notes_table")
    void deleteAll();

}
