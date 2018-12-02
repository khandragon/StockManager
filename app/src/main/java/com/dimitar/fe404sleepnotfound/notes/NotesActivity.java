package com.dimitar.fe404sleepnotfound.notes;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.List;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;
import com.dimitar.fe404sleepnotfound.notes.data.Note;
import com.dimitar.fe404sleepnotfound.notes.viewModel.NotesListAdapter;
import com.dimitar.fe404sleepnotfound.notes.viewModel.NotesViewModel;




public class NotesActivity extends AppCompatActivity {

    private NotesViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        RecyclerView recyclerView = findViewById(R.id.notesList);
        final NotesListAdapter adapter = new NotesListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                adapter.setNotes(notes);
            }
        });
    }
}

