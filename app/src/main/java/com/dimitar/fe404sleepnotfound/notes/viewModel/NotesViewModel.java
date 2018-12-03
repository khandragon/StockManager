package com.dimitar.fe404sleepnotfound.notes.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.dimitar.fe404sleepnotfound.notes.data.Note;
import com.dimitar.fe404sleepnotfound.notes.persistence.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    private NotesRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NotesViewModel(Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Note note) {
        mRepository.insert(note);
    }

    public void delete(String note) {
        mRepository.delete(note);
    }

}
