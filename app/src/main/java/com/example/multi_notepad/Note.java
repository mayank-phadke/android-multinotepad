package com.example.multi_notepad;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {

    private String title;
    private String text;
    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a", Locale.US);

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.date = new Date();
    }

    public Note(String title, String text, String date) {
        this.title = title;
        this.text = text;
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            this.date = new Date();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDateAsString() {
        return dateFormat.format(this.date);
    }
}
