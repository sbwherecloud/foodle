package com.belanger.simon.foodle.models;

import android.util.Log;

import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.datastructures.FValue;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FSearchConstraints {

	@SerializedName("ingredients")
	public FList<FIngredient> ingredients	= new FList<FIngredient>();
	@SerializedName("cooking_time_max")
	public FValue<Integer> cookingTimeMax = new FValue<Integer>(FSearch.DEFAULT_MAX_COOKING_TIME);

	public static transient final FValue<Integer> absoluteCookingTimeMax = new FValue<Integer>();

	/**
	 * Updates the receiver with another object. Triggers observers.
	 * 
	 * @param sc
	 *            The search constraints containing new data.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void update(FSearchConstraints sc) {
		if (sc == null) {
			Log.d("Error", "Search Constraints were null - deserialization problem?");
			return;
		}

		Field[] fields = FSearchConstraints.class.getFields();
		for (Field f : fields) {
			// Skip transient and static fields
			if (Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			try {
				if (FValue.class.isInstance(f.get(sc))) {
					FValue mine = (FValue) f.get(this);
					FValue other = (FValue) f.get(sc);
					mine.set(other.get());
				} else if (FList.class.isInstance(f.get(sc))) {
					FList mine = (FList) f.get(this);
					FList other = (FList) f.get(sc);
					mine.silently().clear();
					mine.addAll(other);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * This will setup and store all possible constraint values in static
	 * fields.
	 */
//	public static void updateAbsoluteConstraints() {
//		final Callback<CRSearchCountResponse> cb = new Callback<CRSearchCountResponse>() {
//			@Override
//			public void failure(RetrofitError error) {
//				if (error != null) {
//					error.printStackTrace();
//				}
//			}
//
//			@Override
//			public void success(CRSearchCountResponse sc, Response res) {
//				FSearchConstraints.absoluteCookingTimeMax.set(sc.searchConstraints.priceMax.get());
//			}
//		};
//	}
}