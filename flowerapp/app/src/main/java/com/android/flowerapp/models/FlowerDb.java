package com.android.flowerapp.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by akanksha on 14/5/16.
 */
public class FlowerDb extends RealmObject{

    @PrimaryKey
    private long id;
    private String name;
    private String url;
    private boolean isFavorite;

    public FlowerDb() {
    }

    public FlowerDb(long id, String name, String url, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFavorite = isFavorite;
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

}
