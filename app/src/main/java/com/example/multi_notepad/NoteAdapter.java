package com.example.multi_notepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> noteList;
    private MainActivity mainActivity;

    public NoteAdapter(List<Note> noteList, MainActivity mainActivity) {
        this.noteList = noteList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        String note_text = note.getText();
        if(note_text.length() > 80) {
            note_text = note_text.substring(0, 80) + "...";
        }
        holder.text.setText(note_text);
        holder.date.setText(note.getDateAsString());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
