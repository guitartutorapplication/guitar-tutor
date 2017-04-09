package com.example.jlo19.guitartutor.models.retrofit.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Object that stores details of a song
 */
public class Song implements Parcelable {
    @SerializedName("title")
    private final String title;
    @SerializedName("artist")
    private final String artist;
    @SerializedName("contents")
    private final String contents;
    @SerializedName("chords")
    private final List<Chord> chords;

    public Song(String title, String artist, String contents, List<Chord> chords) {
        this.title = title;
        this.artist = artist;
        this.contents = contents;
        this.chords = chords;
    }

    private Song(Parcel in) {
        title = in.readString();
        artist = in.readString();
        contents = in.readString();
        chords = new ArrayList<>();
        in.readList(chords, getClass().getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public List<Chord> getChords() {
        return chords;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(contents);
        dest.writeList(chords);
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getContents() {
        return contents;
    }
}
