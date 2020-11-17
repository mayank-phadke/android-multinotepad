package com.example.multi_notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText title_edit_text, text_edit_text;
    int position;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title_edit_text = findViewById(R.id.note_title_edit_text);
        text_edit_text = findViewById(R.id.note_text_edit_text);

        position = -1;

        note = (Note) getIntent().getSerializableExtra("noteObject");
        if(note != null) {
            position = getIntent().getIntExtra("position", -1);

            title_edit_text.setText(note.getTitle());
            text_edit_text.setText(note.getText());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save_note) {
            saveNote();
        }

        return true;
    }

    private void saveNote() {
        String note_title = title_edit_text.getText().toString().trim();
        String note_text = text_edit_text.getText().toString().trim();
        if(note_title.isEmpty()) {
            Toast.makeText(this, "The un-titled note was not saved", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
        } else {
            Intent i = new Intent();
            Note note = new Note(note_title, note_text);
            i.putExtra("position", position);
            i.putExtra("noteObject", note);
            setResult(Activity.RESULT_OK, i);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        String note_text = text_edit_text.getText().toString().trim();
        String note_title = title_edit_text.getText().toString().trim();
        if(note == null && note_title.isEmpty()) {
            Toast.makeText(this, "The un-titled note was not saved", Toast.LENGTH_SHORT).show();
            finish();
        } else if(note != null && note.getText().equals(note_text) && note.getTitle().equals(note_title)) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveNote();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setTitle("Your note is not saved!");
            builder.setMessage("Save note '" + note_title + "'?");

            builder.create().show();
        }
    }
}