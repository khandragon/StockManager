package com.dimitar.fe404sleepnotfound.notes.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.dimitar.fe404sleepnotfound.notes.data.Note;
import com.dimitar.fe404sleepnotfound.notes.persistence.NotesRepository;

import java.util.List;

/**
 * Notes view model handles the live data and call the methods in the room to handle the db
 * @Author: Saad Khan
 */
public class NotesViewModel extends AndroidViewModel {
    private NotesRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NotesViewModel(Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    /**
     * returns live data of all notes in the db at this current moment
     * @return
     */
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    /**
     * calls the insert in the repository
     * @param note
     */
    public void insert(Note note) {
        mRepository.insert(note);
    }

    /**
     * calls the delete in the repository
     * @param noteId
     */
    public void delete(int noteId) {
        mRepository.delete(noteId);
    }

    /**
     * calls the update in the repository
     * @param note
     */
    public void update(Note note) {
        mRepository.update(note);
    }
}
