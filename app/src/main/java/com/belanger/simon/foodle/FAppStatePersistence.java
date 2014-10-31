package com.belanger.simon.foodle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.belanger.simon.foodle.models.FRecipe;
import com.belanger.simon.foodle.models.FUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FAppStatePersistence {

	private static final String	APP_STATE_GCM_REGISTRATION_ID				= "gcm_registration_id";
    private static final String	APP_STATE_APP_VERSION				        = "gcm_app_version";
    private static final String	APP_STATE_FAVORITES_RECIPES_IDS_FILENAME	= "foodle.favorites";
	private static final String APP_STATE_CUSTOM_RECIPES_FILENAME           = "foodle.custom_recipes";
    private static final String APP_STATE_PENDING_VOTES_FILENAME            = "foodle.pending_votes";
    private static final String APP_STATE_FRIENDS_LIST_FILENAME             = "foodle.friends_list";
    private static final String	APP_STATE_PROFILE_FILENAME					= "foodle.profile";

	// Convenience static constructor
	public static FAppStatePersistence create() {
		return new FAppStatePersistence();
	}

	public void save(FAppState appState, Context context) throws Exception {
		SharedPreferences settings = context.getSharedPreferences(FApplication.F_PREFS, 0);
		Editor settingsEditor = settings.edit();

		settingsEditor.putString(APP_STATE_GCM_REGISTRATION_ID, appState.getGcmRegistrationId().get());

        settingsEditor.putInt(APP_STATE_APP_VERSION, appState.getAppVersion().get());

		Gson gson = getGson();

        String json = gson.toJson(appState.getFavoriteRecipeIds());
        saveContentToFile(context, json, APP_STATE_FAVORITES_RECIPES_IDS_FILENAME);

        String jsonCustomRecipe = gson.toJson(appState.getCustomRecipes());
        saveContentToFile(context, jsonCustomRecipe, APP_STATE_CUSTOM_RECIPES_FILENAME);

        String jsonPendingVotes = gson.toJson(appState.getPendingVotesList());
        saveContentToFile(context, jsonPendingVotes, APP_STATE_PENDING_VOTES_FILENAME);

        String jsonFriendsList = gson.toJson(appState.getFriendsList());
        saveContentToFile(context, jsonFriendsList, APP_STATE_FRIENDS_LIST_FILENAME);

        String jsonUserInfo = gson.toJson(appState.getUserInfo());
        saveContentToFile(context, jsonUserInfo, APP_STATE_PROFILE_FILENAME);

		settingsEditor.commit();
	}

	public void load(FAppState appState, Context context) throws Exception {
		SharedPreferences settings = context.getSharedPreferences(FApplication.F_PREFS, 0);

		appState.getGcmRegistrationId().set(settings.getString(APP_STATE_GCM_REGISTRATION_ID, null));

        appState.getAppVersion().set(settings.getInt(APP_STATE_APP_VERSION, Integer.MIN_VALUE));

		Gson gson = getGson();

        String json = loadFileContent(context, APP_STATE_FAVORITES_RECIPES_IDS_FILENAME);
        if (json != null) {
            String[] vehicleIds = gson.fromJson(json, String[].class);
            appState.getFavoriteRecipeIds().clear();
            for (String vid : vehicleIds) {
                appState.addToFavoriteRecipeIds(vid);
            }
        }

		json = loadFileContent(context, APP_STATE_CUSTOM_RECIPES_FILENAME);
		if (json != null) {
			FRecipe[] recipes = gson.fromJson(json, FRecipe[].class);
            appState.getCustomRecipes().clear();
            for (FRecipe recipe : recipes){
                appState.addToCustomRecipes(recipe);
            }
		}

        json = loadFileContent(context, APP_STATE_PENDING_VOTES_FILENAME);
        if (json != null) {
            Long[] pendingVotes = gson.fromJson(json, Long[].class);
            appState.getPendingVotesList().clear();
            for (Long pendingVoteId : pendingVotes){
                appState.addToPendingVotes(pendingVoteId);
            }
        }

        json = loadFileContent(context, APP_STATE_FRIENDS_LIST_FILENAME);
        if (json != null) {
            FUserInfo[] friendsUserInfo = gson.fromJson(json, FUserInfo[].class);
            appState.getFriendsList().clear();
            for (FUserInfo friendUserInfo : friendsUserInfo){
                appState.addToFriendsList(friendUserInfo);
            }
        }

        json = loadFileContent(context, APP_STATE_PROFILE_FILENAME);
        if (json != null) {
            FUserInfo userInfo = gson.fromJson(json, FUserInfo.class);
            appState.setUserInfo(userInfo);
        }

	}

	/*
	 * HELPER METHODS
	 */

	private void saveContentToFile(Context context, String content, String filename) throws Exception {
		File file = new File(context.getFilesDir(), filename);

		if (!file.exists()) {
			if (!file.createNewFile()) {
				Log.e("Error", "Unable to create file at [" + file.getAbsolutePath() + "]");
				return;
			}
		}

		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.close();
	}

	private String loadFileContent(Context context, String filename) throws Exception {
		File file = new File(context.getFilesDir(), filename);

		if (!file.exists()) {
			Log.e("Error", "App state file does not exist at [" + file.getAbsolutePath() + "]");
			return null;
		}

		FileReader fr = new FileReader(file);
		StringBuilder content = new StringBuilder();
		char[] buffer = new char[64];
		int charsRead = fr.read(buffer);
		while (charsRead != -1) {
			content.append(buffer, 0, charsRead);
			charsRead = fr.read(buffer);
		}
		Log.e("Error", "Error reading [" + file.getAbsolutePath() + "]");
		fr.close();
		return content.toString();
	}

	private String loadResourceContent(Context context, int id) throws Exception {

		InputStream fr = context.getResources().openRawResource(id);
		BufferedReader r = new BufferedReader(new InputStreamReader(fr));
		StringBuilder content = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			content.append(line);
			content.append("\n");
		}
		fr.close();
		r.close();
		return content.toString();
	}

	private Gson getGson() {
		// TODO: configure gson for app state serialization if necessary
		return new GsonBuilder().create();
	}

}
