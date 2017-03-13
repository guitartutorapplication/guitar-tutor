package com.example.jlo19.guitartutor.models.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores details of a chord
 */
public class Chord implements Parcelable {
    @SerializedName("diagram_filename")
    private String diagramFilename;
    @SerializedName("chord_id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("video_filename")
    private String videoFilename;

    public Chord(int id, String name, String type, String diagramFilename, String videoFilename) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.diagramFilename = diagramFilename;
        this.videoFilename = videoFilename;
    }

    private Chord(Parcel in) {
        diagramFilename = in.readString();
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        videoFilename = in.readString();
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
}
