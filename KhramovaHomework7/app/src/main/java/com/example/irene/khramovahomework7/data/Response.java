package com.example.irene.khramovahomework7.data;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Response {

    @Expose
    private Meta meta;

    @Expose
    private List<Bridge> objects;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Bridge> getObjects() {
        return objects;
    }

    public void setObjects(List<Bridge> objects) {
        this.objects = objects;
    }
}
