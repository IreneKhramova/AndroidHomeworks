package com.example.irene.khramovahomework7.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bridge implements Parcelable {

    @Expose
    private String description;

    @SerializedName("description_eng")
    @Expose
    private String descriptionEng;

    @Expose
    private List<Divorce> divorces;

    @Expose
    private int id;

    @Expose
    private double lat;

    @Expose
    private double lng;

    @Expose
    private String name;

    @SerializedName("name_eng")
    @Expose
    private String nameEng;

    @SerializedName("photo_close")
    @Expose
    private String photoClose;

    @SerializedName("photo_open")
    private String photoOpen;

    @SerializedName("public")
    private boolean isPublic;

    @SerializedName("resource_uri")
    private String resourceUri;

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

    public List<Divorce> getDivorces() {
        return divorces;
    }

    public void setDivorces(List<Divorce> divorces) {
        this.divorces = divorces;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.descriptionEng);
        dest.writeTypedList(this.divorces);
        dest.writeInt(this.id);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.name);
        dest.writeString(this.nameEng);
        dest.writeString(this.photoClose);
        dest.writeString(this.photoOpen);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeString(this.resourceUri);
    }

    public Bridge() {
    }

    protected Bridge(Parcel in) {
        this.description = in.readString();
        this.descriptionEng = in.readString();
        this.divorces = in.createTypedArrayList(Divorce.CREATOR);
        this.id = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.name = in.readString();
        this.nameEng = in.readString();
        this.photoClose = in.readString();
        this.photoOpen = in.readString();
        this.isPublic = in.readByte() != 0;
        this.resourceUri = in.readString();
    }

    public static final Creator<Bridge> CREATOR = new Creator<Bridge>() {
        @Override
        public Bridge createFromParcel(Parcel source) {
            return new Bridge(source);
        }

        @Override
        public Bridge[] newArray(int size) {
            return new Bridge[size];
        }
    };
}
