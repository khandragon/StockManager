package com.dimitar.fe404sleepnotfound.notes.viewModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.Collections;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private final TextView NotesItemView;

        private NotesViewHolder(View itemView) {
            super(itemView);
            NotesItemView = itemView.findViewById(R.id.noteItem);
        }
    }

    private final LayoutInflater mInflater;
    private List<Note> mNotes = Collections.emptyList(); // Cached copy of Notes

    public NotesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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


