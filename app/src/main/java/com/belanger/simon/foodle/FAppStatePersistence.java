package com.belanger.simon.foodle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.belanger.simon.foodle.models.FRecipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FAppStatePersistence {

	private static final String	APP_STATE_VISITOR_ID_KEY					= "visitor_id";
    private static final String	APP_STATE_FAVORITES_RECIPES_IDS_FILENAME	= "foodle.favorites";
	private static final String	APP_STATE_CUSTOM_RECIPES					= "foodle.custom_recipes";

	// Convenience static constructor
	public static FAppStatePersistence create() {
		return new FAppStatePersistence();
	}

	public void save(FAppState appState, Context context) throws Exception {
		SharedPreferences settings = context.getSharedPreferences(FApplication.F_PREFS, 0);
		Editor settingsEditor = settings.edit();

//		settingsEditor.putString(APP_STATE_VISITOR_ID_KEY, appState.getVisitorId().get());

		Gson gson = getGson();

        if (appState.getFavoriteRecipeIds().size() > 0) {
            String json = gson.toJson(appState.getFavoriteRecipeIds());
            saveContentToFile(context, json, APP_STATE_FAVORITES_RECIPES_IDS_FILENAME);
        }

        String jsonCustomRecipe = gson.toJson(appState.getCustomRecipes());
        saveContentToFile(context, jsonCustomRecipe, APP_STATE_CUSTOM_RECIPES);

		settingsEditor.commit();
	}

	public void load(FAppState appState, Context context) throws Exception {
		SharedPreferences settings = context.getSharedPreferences(FApplication.F_PREFS, 0);

//		appState.getVisitorId().set(settings.getString(APP_STATE_VISITOR_ID_KEY, null));

		Gson gson = getGson();

        String json = loadFileContent(context, APP_STATE_FAVORITES_RECIPES_IDS_FILENAME);
        if (json != null) {
            String[] vehicleIds = gson.fromJson(json, String[].class);
            appState.getFavoriteRecipeIds().clear();
            for (String vid : vehicleIds) {
                appState.addToFavoriteRecipeIds(vid);
            }
        }

		json = loadFileContent(context, APP_STATE_CUSTOM_RECIPES);
		if (json != null) {
			FRecipe[] recipes = gson.fromJson(json, FRecipe[].class);
            appState.getCustomRecipes().clear();
            for (FRecipe recipe : recipes){
                appState.addToCustomRecipes(recipe);
            }
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
