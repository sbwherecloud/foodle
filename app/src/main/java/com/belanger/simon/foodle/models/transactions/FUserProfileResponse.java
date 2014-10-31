package com.belanger.simon.foodle.models.transactions;

import com.belanger.simon.foodle.models.FUserInfo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SimonPro on 14-10-30.
 */
public class FUserProfileResponse {
    @SerializedName("contactInformation")
    public FUserInfo contactInformation;
}
