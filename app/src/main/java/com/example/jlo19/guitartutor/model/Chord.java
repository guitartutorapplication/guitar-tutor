package com.example.jlo19.guitartutor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Chord object
 */

public class Chord {
    @SerializedName("video_filename")
    private String videoFilename;
    @SerializedName("diagram_filename")
    private String diagramFilename;
    @SerializedName("chord_id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    public Chord(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Chord(int id, String name, String type, String diagramFilename, String videoFilename) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.diagramFilename = diagramFilename;
        this.videoFilename = videoFilename;
    }

    public String getDiagramFilename() {
        return diagramFilename;
    }

    private String formatType() {
        switch (type) {
            case "MAJOR":
                return "";
            case "MINOR":
                return "m";
            case "SEVEN":
                return "7";
            case "SHARP":
                return "#";
            case "SHARP_MINOR":
                return "#m";
            case "FLAT":
                return "♭";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return name + formatType();
    }

}
