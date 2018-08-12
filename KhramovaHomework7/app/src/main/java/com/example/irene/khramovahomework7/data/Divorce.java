package com.example.irene.khramovahomework7.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Divorce implements Parcelable {

    @Expose
    private int id;

    @Expose
    private Date start;

    @Expose
    private Date end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.start != null ? this.start.getTime() : -1);
        dest.writeLong(this.end != null ? this.end.getTime() : -1);
    }

    public Divorce() {
    }

    protected Divorce(Parcel in) {
        this.id = in.readInt();
        long tmpStart = in.readLong();
        this.start = tmpStart == -1 ? null : new Date(tmpStart);
        long tmpEnd = in.readLong();
        this.end = tmpEnd == -1 ? null : new Date(tmpEnd);
    }

    public static final Creator<Divorce> CREATOR = new Creator<Divorce>() {
        @Override
        public Divorce createFromParcel(Parcel source) {
            return new Divorce(source);
        }

        @Override
        public Divorce[] newArray(int size) {
            return new Divorce[size];
        }
    };
}
