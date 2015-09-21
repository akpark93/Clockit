package com.example.andrewpark.clockit.model;

/**
 * Created by andrewpark on 9/10/15.
 */
public class Melody {

    private String melody_name;
    private String melody_uri;
    private boolean selected;
    private int position;

    public Melody(String melody_name, String melody_uri) {
        this.melody_name = melody_name;
        this.melody_uri = melody_uri;
    }

    public String getMelody_name() {
        return melody_name;
    }

    public String getMelody_uri() {
        return melody_uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
