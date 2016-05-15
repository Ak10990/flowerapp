package com.android.flowerapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by akanksha on 14/5/16.
 */
public class Flower implements Parcelable {

    private long id;
    private String name;
    private String url;
    private boolean isFavorite;

    public Flower() {
    }

    public Flower(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFavorite = false;
    }

    protected Flower(Parcel in) {
        id = in.readLong();
        name = in.readString();
        url = in.readString();
        isFavorite = in.readByte() == 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static final Creator<Flower> CREATOR = new Creator<Flower>() {
        @Override
        public Flower createFromParcel(Parcel in) {
            return new Flower(in);
        }

        @Override
        public Flower[] newArray(int size) {
            return new Flower[size];
        }
    };

    public Flower getFlowerFromDb(FlowerDb flowerDb) {
        id = flowerDb.getId();
        name = flowerDb.getName();
        url = flowerDb.getUrl();
        isFavorite = flowerDb.isFavorite();
        return this;
    }

    public FlowerDb getFlowerDb() {
        return new FlowerDb(this.id, this.name, this.url, this.isFavorite);
    }
}
