package com.dimitar.fe404sleepnotfound.notes.viewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.notes.NewNoteActivity;
import com.dimitar.fe404sleepnotfound.notes.NotesActivity;
import com.dimitar.fe404sleepnotfound.notes.data.Note;

import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * class to handle the recycler view and the update and delete methods in the recycler items
 * @Author: Saad Khan
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    private static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;


    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView NotesItemView;
        private final Button removeBtn;
        public int noteId;

        private NotesViewHolder(View itemView) {
            super(itemView);
            NotesItemView = itemView.findViewById(R.id.noteItem);
            NotesItemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * handles the update of the notes
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NewNoteActivity.class);
                    intent.putExtra("type","update");
                    intent.putExtra("noteId",noteId);
                    intent.putExtra("text", NotesItemView.getText().toString());
                    ((Activity)mContext).startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });
            removeBtn = itemView.findViewById(R.id.removeNoteBtn);
            //attach the remove event
            removeBtn.setOnClickListener(this);

        }

        /**
         * gets the id of the specific note
         * @return
         */

        public int getNoteId(){
            return noteId;
        }

        /**
         * event to remove from the notes list the note that was clicked
         * @param view
         */
        @Override
        public void onClick(View view) {
            mNoteViewModel.delete(getNoteId());
        }
    }

    /**
     * secoundary onActivityResult method handles both update and new note creation
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.i("hello there", "new");
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY));
            mNoteViewModel.insert(note);
        }else if(requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY));
            note.setNoteId(data.getIntExtra("noteId",0));
            Log.i("hello there", note.getText());
            mNoteViewModel.update(note);
        }
    }

    private final LayoutInflater mInflater;
    private List<Note> mNotes = Collections.emptyList(); // Cached copy of Notes
    private final NotesViewModel mNoteViewModel;
    private final Context mContext;

    public NotesListAdapter(Context context, NotesViewModel mNoteViewModel) {
        this.mContext = context;
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
        holder.noteId = current.getNoteId();
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


