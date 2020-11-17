package com.example.multi_notepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView title, text, date;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.note_title);
        text = itemView.findViewById(R.id.note_text);
        date = itemView.findViewById(R.id.note_date);
    }
}
