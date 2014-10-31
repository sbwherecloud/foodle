package com.belanger.simon.foodle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.datastructures.FValue;
import com.belanger.simon.foodle.models.FRecipe;
import com.belanger.simon.foodle.models.FSearch;
import com.belanger.simon.foodle.models.FSearchConstraints;
import com.belanger.simon.foodle.models.FUserInfo;

public class FAppState {

	private static FAppState	instance;

	private FAppState() {
		// TODO: validate that this is only needed in CRSearchFragment. If so,
		// move this code to CRSearchFragment (remove the observer on destroy)
		// Hook an observer on the search change to always update constraints
		// when the search is changed
//		final FCallback<CRSearchCountResponse> searchCountRequestCallback = new CRCallback<CRSearchCountResponse>() {
//			@Override
//			public void success(CRSearchCountResponse searchCountResponse, Response response) {
//				super.success(searchCountResponse, response);
//				if(currentSearch.constraints != null && searchCountResponse != null){
//					currentSearch.constraints.update(searchCountResponse.searchConstraints);
//				}
//			}
//
//			@Override
//			public void failure(RetrofitError error) {
//				// TODO: handle request failure
//				super.failure(error);
//			}
//		};
//
//		currentSearch.addObserver(new Observer() {
//			@Override
//			public void update(Observable observable, Object data) {
//				FWebService.getInstance().performSearchCountResquest(CRSearchCountRequestRoot.from(currentSearch),
//						CRAppState.getInstance().getUserLat(), CRAppState.getInstance().getUserLong(),
//						searchCountRequestCallback);
//			}
//		});
	}

	public static FAppState getInstance() {
		if (instance == null) {
			instance = new FAppState();
		}
		return instance;
	}

	private final FValue<Integer>	appVersion			= new FValue<Integer>();
	private final FValue<String>	gcmRegistrationId	= new FValue<String>();
	private final FSearch			currentSearch		= new FSearch();
    private FList<String>			favoriteRecipeIds	= new FList<String>();
    private FList<FUserInfo>		friendsList     	= new FList<FUserInfo>();
    private FList<Long>			    pendingVotesList    = new FList<Long>();
    private FList<FRecipe>			customRecipes	    = new FList<FRecipe>();
	private FUserInfo				userInfo			= new FUserInfo();

    public FValue<String> getGcmRegistrationId(){
        return gcmRegistrationId;
    }

    public FValue<Integer> getAppVersion(){
        return appVersion;
    }

    public FSearch getCurrentSearch() {
		return currentSearch;
	}

	public FSearchConstraints getCurrentSearchConstraints() {
		return currentSearch.constraints;
	}

	public FUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(FUserInfo userInfo) {
		this.userInfo = userInfo;
	}

    public void addToCustomRecipes(FRecipe recipe) {
        if (customRecipes.contains(recipe)) {
            customRecipes.remove(recipe);
        }
        customRecipes.add(recipe);
    }

    public void removeFromCustomRecipes(FRecipe recipe) {
        if (customRecipes.contains(recipe)) {
            customRecipes.remove(recipe);
        }
    }

    public FList<FRecipe> getCustomRecipes() {
        return customRecipes;
    }

    public void addToFriendsList(FUserInfo friendInfo) {
        if (friendsList.contains(friendInfo)) {
            friendsList.remove(friendInfo);
        }
        friendsList.add(friendInfo);
    }

    public void removeFromFriendsList(FUserInfo friendInfo) {
        if (friendsList.contains(friendInfo)) {
            friendsList.remove(friendInfo);
        }
    }

    public FList<FUserInfo> getFriendsList() {
        return friendsList;
    }

    public void addToFavoriteRecipeIds(String recipeId) {
        if (favoriteRecipeIds.contains(recipeId)) {
            favoriteRecipeIds.remove(recipeId);
        }
        favoriteRecipeIds.add(recipeId);
    }

    public void removeFromFavoriteRecipeIds(String recipeId) {
        if (favoriteRecipeIds.contains(recipeId)) {
            favoriteRecipeIds.remove(recipeId);
        }
    }

    public FList<Long> getPendingVotesList() {
        return pendingVotesList;
    }

    public void addToPendingVotes(Long pendingVoteId) {
        if (pendingVotesList.contains(pendingVoteId)) {
            pendingVotesList.remove(pendingVoteId);
        }
        pendingVotesList.add(pendingVoteId);
    }

    public void removeFromPendingVotesList(Long pendingVoteId) {
        if (pendingVotesList.contains(pendingVoteId)) {
            pendingVotesList.remove(pendingVoteId);
        }
    }

    public FList<String> getFavoriteRecipeIds() {
        return favoriteRecipeIds;
    }

	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public void save(Context context) {
		try {
			FAppStatePersistence.create().save(this, context);
		} catch (Exception e) {
			Log.e("Error", "Error while saving application state.");
			e.printStackTrace();
		}
	}

	public void load(Context context) {
		try {
			FAppStatePersistence.create().load(this, context);
		} catch (Exception e) {
			Log.e("Error", "Error while loading application state.");
			e.printStackTrace();
		}
	}
}