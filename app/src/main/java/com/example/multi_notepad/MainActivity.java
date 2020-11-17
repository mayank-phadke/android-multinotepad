package com.example.multi_notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Note> noteList;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private final int NOTE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList = loadDataFromFile();
        recyclerView = findViewById(R.id.recycler_view);
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Multi Notes (" + noteList.size() + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_note:
                Intent i = new Intent(this, EditActivity.class);
                startActivityForResult(i, NOTE_REQUEST);
                return true;
            case R.id.info:
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                return true;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, EditActivity.class);
        int pos = recyclerView.getChildLayoutPosition(view);
        Note note = noteList.get(pos);
        i.putExtra("position", pos);
        i.putExtra("noteObject", note);
        startActivityForResult(i, NOTE_REQUEST);
    }

    @Override
    public boolean onLongClick(View view) {

        final int pos = recyclerView.getChildLayoutPosition(view);
        Note note = noteList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(pos);
                noteAdapter.notifyDataSetChanged();
                setTitle("Multi Notes (" + noteList.size() + ")");
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .setTitle("Delete Note '" + note.getTitle() + "'?");

        builder.create().show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NOTE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            int pos = data.getIntExtra("position", -1);
            Note note = (Note) data.getSerializableExtra("noteObject");
            if(pos == -1) {
                noteList.add(0, note);
            } else {
                noteList.set(pos, note);
            }
            noteAdapter.notifyDataSetChanged();
            setTitle("Multi Notes (" + noteList.size() + ")");
        }
    }

    private ArrayList<Note> loadDataFromFile() {
        ArrayList<Note> list = new ArrayList<>();
        try {
            InputStream is = getApplicationContext()
                    .openFileInput(getString(R.string.notes_file_name));

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Note note = new Note(
                        jsonObject.getString("note_title"),
                        jsonObject.getString("note_text"),
                        jsonObject.getString("note_date"));

                list.add(note);
            }

        } catch (FileNotFoundException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

    @Override
    protected void onPause() {
        saveDataToFile();
        super.onPause();
    }

    private void saveDataToFile() {
        try {
            FileOutputStream fos = getApplicationContext()
                    .openFileOutput(getString(R.string.notes_file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("    ");
            writer.beginArray();
            for (Note note : noteList) {
                writer.beginObject();
                writer.name("note_title").value(note.getTitle());
                writer.name("note_text").value(note.getText());
                writer.name("note_date").value(note.getDateAsString());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}