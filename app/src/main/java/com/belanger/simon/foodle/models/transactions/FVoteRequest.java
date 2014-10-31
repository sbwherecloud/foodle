package com.belanger.simon.foodle.models.transactions;

import com.belanger.simon.foodle.models.FKey;
import com.belanger.simon.foodle.models.FUserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SimonPro on 2014-08-22.
 */
public class FVoteRequest {
    @SerializedName("key")
    public FKey key;
    @SerializedName("endTimeInMillis")
    public Long endTimeInMillis;
    @SerializedName("voteCreatorInformation")
    public FUserInfo voteCreatorInformation;
    @SerializedName("recipes")
    public List<String> recipes	= new ArrayList<String>();
    @SerializedName("votersEmail")
    public List<String> votersEmail	= new ArrayList<String>();
}
