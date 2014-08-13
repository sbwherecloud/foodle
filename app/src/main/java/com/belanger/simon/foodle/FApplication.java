package com.belanger.simon.foodle;

import android.app.Application;
import android.content.res.AssetManager;

import com.belanger.simon.foodle.util.FFormat;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class FApplication extends Application {

	public static final String	F_PREFS					= "fprefs";
	public static final String	F_PREFS_IS_FIRST_LAUNCH	= "is_first_launch";
	private Tracker mTracker;

	private static FApplication instance;
	
	public static FApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		final AssetManager assets = getAssets();

		FAppState.getInstance().load(getApplicationContext());

		FFormat.init(this.getApplicationContext());

//		mTracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker);

		instance = this;
	}

	public Tracker getTracker() {
		return mTracker;
	}
	
	public void sendGoogleAnalyticsEvents(String eventName) {

		// Set screen name.
		// Where path is a String representing the screen name.
		// Build and send an Event.
		mTracker.send(new HitBuilders.EventBuilder().setCategory(getString(R.string.ui_action)).setAction(eventName)
				.setLabel("").build());

		// Send a screen view.
		mTracker.send(new HitBuilders.AppViewBuilder().build());
	}

	public void sendGoogleAnalyticsScreens(String screenName) {
		// Set screen name.
		// Where path is a String representing the screen name.
		// Build and send an Event.
		mTracker.setScreenName(screenName);

		// Send a screen view.
		mTracker.send(new HitBuilders.AppViewBuilder().build());
		
	}
}
