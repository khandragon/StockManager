package com.dimitar.fe404sleepnotfound.notes;


import android.os.Bundle;


import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;


public class NotesActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        RecyclerView recyclerView = findViewById(R.id.notesList);
        final NotesListAdapter adapter = new NotesList(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

