package com.example.cagri.votaapp;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by cagri on 27/11/2016.
 */

public class Candidate implements Serializable {

    private static final long serialVersionUID = 236007149884209464L;
    private String Id;
    private String name;
    private String party;
    private Bitmap image;
    private String imageUrl;


    public Candidate() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
