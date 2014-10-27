package com.belanger.simon.foodle.models.transactions;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SimonPro on 14-10-25.
 */
public class FVoteUpdate {
    @SerializedName("integersList")
    public List<Integer> integersList	= new ArrayList<Integer>();
}
