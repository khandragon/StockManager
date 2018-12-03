package com.dimitar.fe404sleepnotfound.notes;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import java.util.List;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.notes.data.Note;
import com.dimitar.fe404sleepnotfound.notes.viewModel.NotesListAdapter;
import com.dimitar.fe404sleepnotfound.notes.viewModel.NotesViewModel;

public class NotesActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private NotesViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mNoteViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.notesList);
        final NotesListAdapter adapter = new NotesListAdapter(this, mNoteViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                adapter.setNotes(notes);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, NewNoteActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY));
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}

