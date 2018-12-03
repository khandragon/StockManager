package com.dimitar.fe404sleepnotfound.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dimitar.fe404sleepnotfound.R;

/**
 * NewNoteActivity class to handle all functions of creating a new note or editing a previous one
 * @@Author Saad Khan
 */
public class NewNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.notelistsql.REPLY";
    private EditText mEditnoteView;
    private int noteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        mEditnoteView = findViewById(R.id.edit_word);
        final Button button = findViewById(R.id.button_save);

        //check to see if we are updating
        final String type = getIntent().getStringExtra("type");
        if (type != null) {
            mEditnoteView.setText(getIntent().getStringExtra("text"));
            noteId = getIntent().getIntExtra("noteId", 0);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditnoteView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    //if we are updating then supply the id
                    if (type != null) {
                        replyIntent.putExtra("noteId", noteId);
                    }
                    String note = mEditnoteView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, note);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
