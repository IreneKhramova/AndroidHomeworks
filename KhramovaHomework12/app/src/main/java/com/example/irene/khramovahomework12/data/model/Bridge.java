package com.example.irene.khramovahomework12.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Bridge {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("name_eng")
    private String nameEng;

    @SerializedName("divorces")
    private List<DivorceInfo> divorces = new ArrayList<>();

    @SerializedName("description")
    private String description;

    @SerializedName("description_eng")
    private String descriptionEng;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("photo_close")
    private String photoClose;

    @SerializedName("photo_open")
    private String photoOpen;

    @SerializedName("public")
    private boolean isPublic;

    @SerializedName("resource_uri")
    private String resourceUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DivorceInfo> getDivorces() {
        return divorces;
    }

    public void setDivorces(List<DivorceInfo> divorces) {
        this.divorces = divorces;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhotoClose() {
        return photoClose;
    }

    public void setPhotoClose(String photoClose) {
        this.photoClose = photoClose;
    }

    public String getPhotoOpen() {
        return photoOpen;
    }

    public void setPhotoOpen(String photoOpen) {
        this.photoOpen = photoOpen;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
