package com.dimitar.fe404sleepnotfound.notes.viewModel;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.notes.NotesActivity;
import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.Collections;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {


    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView NotesItemView;
        private final Button removeBtn;

        private NotesViewHolder(View itemView) {
            super(itemView);
            NotesItemView = itemView.findViewById(R.id.noteItem);
            removeBtn = itemView.findViewById(R.id.removeNoteBtn);
            removeBtn.setOnClickListener(this);

        }
        public String getNoteText() {
            return NotesItemView.getText().toString();
        }

        @Override
        public void onClick(View view) {
            mNoteViewModel.delete(getNoteText());
        }
    }

    private final LayoutInflater mInflater;
    private List<Note> mNotes = Collections.emptyList(); // Cached copy of Notes
    private final NotesViewModel mNoteViewModel;

    public NotesListAdapter(Context context, NotesViewModel mNoteViewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.mNoteViewModel = mNoteViewModel;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.notesview_item, parent, false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Note current = mNotes.get(position);
        holder.NotesItemView.setText(current.getText());
    }

    public void setNotes(List<Note> Notes) {
        mNotes = Notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
}


