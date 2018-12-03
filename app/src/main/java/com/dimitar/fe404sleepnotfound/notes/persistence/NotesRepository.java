package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.List;

/**
 * A Repository manages query threads and allows you to use multiple backends.
 * Author: Saad Khan
 */
public class NotesRepository {
    private NotesDAO mNotesDao;
    private LiveData<List<Note>> mAllNotes;

    public NotesRepository(Application application) {
        NotesRoomDatabase db = NotesRoomDatabase.getDatabase(application);
        mNotesDao = db.notesDAO();
        mAllNotes = mNotesDao.getAllNotes();
    }

    /**
     * room getss all the notes in the database and returns them as live data
     *
     * @return
     */
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    /**
     * inserts note in a async task
     *
     * @param note
     */
    public void insert(Note note) {
        new insertAsyncTask(mNotesDao).execute(note);
    }

    /**
     * deletes the note by id using an async task
     *
     * @param noteId
     */
    public void delete(int noteId) {
        new deleteAsyncTask(mNotesDao).execute(noteId);
    }

    /**
     * updates the note using an async task
     *
     * @param note
     */
    public void update(Note note) {
        new updateAsyncTask(mNotesDao).execute(note);
    }

    /**
     * async class to insert note into the db
     */
    public static class insertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NotesDAO mAsyncTaskDao;

        insertAsyncTask(NotesDAO notesDAO) {
            mAsyncTaskDao = notesDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    /**
     * async class that find note by id and then deletes the note from the db
     */
    public static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private NotesDAO mAsyncTaskDao;

        deleteAsyncTask(NotesDAO notesDAO) {
            mAsyncTaskDao = notesDAO;
        }

        @Override
        protected Void doInBackground(Integer... noteId) {
            Note n = mAsyncTaskDao.getNote(noteId[0]);
            mAsyncTaskDao.deleteNote(n);
            return null;
        }
    }

    /**
     * async class that updates note by by old id and setting new text from the db
     */
    public static class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NotesDAO mAsyncTaskDao;

        updateAsyncTask(NotesDAO notesDAO) {
            mAsyncTaskDao = notesDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            String text = notes[0].getText();
            int id = notes[0].getNoteId();
            mAsyncTaskDao.updateNote(id, text);
            return null;
        }
    }
}
