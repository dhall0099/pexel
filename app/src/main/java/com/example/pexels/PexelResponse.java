package com.example.pexels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PexelResponse {

    @SerializedName("photos")
    private List<Pexel> photosList;

    public PexelResponse(List<Pexel> photosList) {
        this.photosList = photosList;
    }

    public List<Pexel> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Pexel> photosList) {
        this.photosList = photosList;
    }
}
