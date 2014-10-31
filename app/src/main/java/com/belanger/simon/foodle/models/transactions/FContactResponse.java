package com.belanger.simon.foodle.models.transactions;

import com.belanger.simon.foodle.models.FUserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SimonPro on 14-10-30.
 */
public class FContactResponse {
    @SerializedName("contactList")
    public List<FUserInfo> contactList	= new ArrayList<FUserInfo>();
}
