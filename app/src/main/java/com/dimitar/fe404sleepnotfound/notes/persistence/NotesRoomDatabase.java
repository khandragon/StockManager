package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NotesDAO mDao;

        PopulateDbAsync(NotesRoomDatabase db) {
            mDao = db.notesDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

            Note word = new Note("Hello");
            mDao.insert(word);
            word = new Note("World");
            mDao.insert(word);
            return null;
        }
    }

}
