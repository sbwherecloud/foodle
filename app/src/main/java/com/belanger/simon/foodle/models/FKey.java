package com.belanger.simon.foodle.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SimonPro on 14-10-24.
 */
public class FKey {
    @SerializedName("kind")
    public String kind;
    @SerializedName("appId")
    public String appId;
    @SerializedName("id")
    public Long id;
    @SerializedName("complete")
    public boolean complete;
}
