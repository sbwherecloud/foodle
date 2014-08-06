package com.belanger.simon.foodle.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.belanger.simon.foodle.FApplication;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.annotations.FragmentOutlet;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;

import com.google.android.gms.analytics.GoogleAnalytics;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class FActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (((Object) this).getClass().isAnnotationPresent(Layout.class)) {
			Layout layout = ((Object) this).getClass().getAnnotation(Layout.class);
			setContentView(layout.value());
		}

		Field[] fields = ((Object) this).getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ViewOutlet.class)) {
				ViewOutlet v = field.getAnnotation(ViewOutlet.class);
				try {
					field.set(this, findViewById(v.value()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			}

			if (field.isAnnotationPresent(FragmentOutlet.class)) {
				FragmentOutlet f = field.getAnnotation(FragmentOutlet.class);
				try {
					field.set(this, getFragmentManager().findFragmentById(f.value()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public void onClickOutsideModal(View v) {
//	}
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		CRFlowManager.getInstance().removeParentActivity();
//		this.overridePendingTransition(R.anim.cr_anim_enter_slideright, R.anim.cr_anim_exit_slideright);
//	}

	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		CRAppState.getInstance().save(getApplicationContext());
//	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(FApplication.CR_PREFS, 0);
		settings.edit().putBoolean(FApplication.CR_PREFS_IS_FIRST_LAUNCH, false).commit();
	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
	public boolean isFirstLaunch() {
		SharedPreferences settings = getSharedPreferences(FApplication.CR_PREFS, 0);
		return settings.getBoolean(FApplication.CR_PREFS_IS_FIRST_LAUNCH, true);
	}
//
//	public boolean hasUpNavigation() {
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case android.R.id.home :
//				CRFlowManager.getInstance().launchParentActivity(this);
//				overridePendingTransition(R.anim.cr_anim_enter_slideright, R.anim.cr_anim_exit_slideright);
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

//	protected ViewGroup getActionBarView() {
//		View v = getWindow().getDecorView();
//		int resId = getResources().getIdentifier("action_bar_container", "id", "android");
//		return (ViewGroup) v.findViewById(resId);
//	}
//
//	protected int getUpButtonResource() {
//		return R.drawable.logo_white;
//	}
//
//	protected View getCustomTitleView() {
//		return null;
//	}

//    private static class AutoChangeObservable extends Observable {
//		@Override
//		public void notifyObservers() {
//			this.setChanged();
//			super.notifyObservers();
//		}
//
//		@Override
//		public void notifyObservers(Object data) {
//			this.setChanged();
//			super.notifyObservers(data);
//		}
//	}
}