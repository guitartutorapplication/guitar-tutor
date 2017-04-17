package com.example.jlo19.guitartutor.models.retrofit.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores details of a chord
 */
public class Chord implements Parcelable {
    @SerializedName("DIAGRAM_FILENAME")
    private final String diagramFilename;
    @SerializedName("CHORD_ID")
    private final int id;
    @SerializedName("NAME")
    private final String name;
    @SerializedName("TYPE")
    private final String type;
    @SerializedName("VIDEO_FILENAME")
    private final String videoFilename;
    @SerializedName("AUDIO_FILENAME")
    private final String audioFilename;
    @SerializedName("LEVEL_REQUIRED")
    private int levelRequired;
    @SerializedName("NUM_TIMES_PRACT")
    private int numTimesPractised;

    public Chord(int id, String name, String type, String diagramFilename, String videoFilename,
                 String audioFilename, int levelRequired) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.diagramFilename = diagramFilename;
        this.videoFilename = videoFilename;
        this.audioFilename = audioFilename;
        this.levelRequired = levelRequired;
    }

    public Chord(int id, String name, String type, String diagramFilename, String videoFilename,
                 String audioFilename, int levelRequired, int numTimesPractised) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.diagramFilename = diagramFilename;
        this.videoFilename = videoFilename;
        this.audioFilename = audioFilename;
        this.levelRequired = levelRequired;
        this.numTimesPractised = numTimesPractised;
    }

    private Chord(Parcel in) {
        diagramFilename = in.readString();
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        videoFilename = in.readString();
        audioFilename = in.readString();
    }

    public String getDiagramFilename() { return diagramFilename; }

    public int getId() { return id; }

    private String formatType() {
        // taking type from database and retrieving the
        // correct string format
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(diagramFilename);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(videoFilename);
        dest.writeString(audioFilename);
    }

    public static final Creator<Chord> CREATOR = new Creator<Chord>() {
        @Override
        public Chord createFromParcel(Parcel in) {
            return new Chord(in);
        }

        @Override
        public Chord[] newArray(int size) {
            return new Chord[size];
        }
    };

    public String getVideoFilename() {
        return videoFilename;
    }

    public int getLevelRequired() {return levelRequired;}

    public String getAudioFilename() {
        return audioFilename;
    }

    public int getNumTimesPractised() {
        return numTimesPractised;
    }
}
