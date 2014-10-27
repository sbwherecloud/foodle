package com.belanger.simon.foodle.network;

import com.belanger.simon.foodle.models.FUserInfo;
import com.belanger.simon.foodle.models.FVote;
import com.belanger.simon.foodle.models.transactions.FVoteRequest;
import com.belanger.simon.foodle.models.transactions.FVoteUpdate;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface FWebServiceInterface {

    /**
     * Vote
     */

    @POST("/vote")
    void insertVote(@Body FVoteRequest voteSetup, Callback<FVote> cb);

	@GET("/vote/{voteId}")
	void getVote(@Path("voteId") Long voteId, Callback<FVote> cb);

    @PUT("/vote/{voteId}")
    void updateVote(@Path("voteId") Long voteId, @Body FVoteUpdate voteUpdate, Callback<FVote> cb);

    /**
     * User profile
     */

    @POST("/userprofile")
    void insertUserProfile(@Body FUserInfo userInfo, Callback<FUserInfo> cb);

    @GET("/userprofile/{userEmail}")
    void getUserProfile(@Path("userEmail") String email, Callback<FUserInfo> cb);

    @PUT("/userprofile/{userEmail}/{gcmRegistrationId}")
    void updateGcmRegistrationId(@Path("userEmail") String email, @Path("gcmRegistrationId") String gcmRegistrationId,
                           Callback<FUserInfo> cb);

}