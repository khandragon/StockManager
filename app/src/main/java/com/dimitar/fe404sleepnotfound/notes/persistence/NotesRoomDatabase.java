package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.dimitar.fe404sleepnotfound.notes.data.Note;

/**
 * Abstract Room database uses the dao to issue queries
 *
 * @Author: Saad Khan
 */
@Database(entities = {Note.class}, version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {
    public abstract NotesDAO notesDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile NotesRoomDatabase INSTANCE;

    static NotesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesRoomDatabase.class, "notes_table")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
