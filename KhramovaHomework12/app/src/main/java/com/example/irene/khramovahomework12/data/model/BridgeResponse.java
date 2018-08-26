package com.example.irene.khramovahomework12.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BridgeResponse {

    @SerializedName("objects")
    private List<Bridge> bridges = new ArrayList<>();

    public List<Bridge> getBridges() {
        return bridges;
    }
}
