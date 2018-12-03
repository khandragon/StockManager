package com.dimitar.fe404sleepnotfound.notes.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.List;

public class NotesRepository {
    private NotesDAO mNotesDao;
    private LiveData<List<Note>> mAllNotes;

    public NotesRepository(Application application) {
        NotesRoomDatabase db = NotesRoomDatabase.getDatabase(application);
        mNotesDao = db.notesDAO();
        mAllNotes = mNotesDao.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Note note) {
        new insertAsyncTask(mNotesDao).execute(note);
    }

    public void delete(int noteId) {
        new deleteAsyncTask(mNotesDao).execute(noteId);
    }

    public void update(Note note) {
        new updateAsyncTask(mNotesDao).execute(note);
    }

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
