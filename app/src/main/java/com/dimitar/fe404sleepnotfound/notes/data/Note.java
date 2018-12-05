package com.dimitar.fe404sleepnotfound.notes.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * class representing a row in the table with a note object
 */
@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    private int noteId;

    @NonNull
    @ColumnInfo(name = "noteText")
    private String text;

    public Note(String text) {
        this.text = text;

    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public int getNoteId() {
        return this.noteId;
    }

    public String getText() {
        return this.text;
    }

}
