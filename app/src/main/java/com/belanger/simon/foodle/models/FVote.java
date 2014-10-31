package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.datastructures.FList;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SimonPro on 2014-08-22.
 */
public class FVote {

   @SerializedName("key")
    public FKey key;
    @SerializedName("recipes")
    public FList<String> recipes;
    @SerializedName("endTimeInMillis")
    public Long endTimeInMillis;
    @SerializedName("recipeVotes")
    public FList<Integer> recipeVotes;
    @SerializedName("voteCreatorInformation")
    public FUserInfo voteCreatorInformation;

}
