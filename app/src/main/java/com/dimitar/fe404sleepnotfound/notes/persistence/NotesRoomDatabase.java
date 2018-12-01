package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dimitar.fe404sleepnotfound.notes.data.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {
    public abstract NotesDAO notesDAO();
    private static volatile NotesRoomDatabase INSTANCE;

    static NotesRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (NotesRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesRoomDatabase.class, "notes_table")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
