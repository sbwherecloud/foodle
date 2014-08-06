package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.datastructures.FList;
import com.google.gson.annotations.SerializedName;

public class FSearchResult {
	@SerializedName("reference_number")
	public String referenceNumber;
    @SerializedName("recipe_name")
    public String recipeName;
	@SerializedName("cooking_time")
	public int cookingTime;
	@SerializedName("ingredients")
	public FList<FIngredient> ingredients;
	

	public transient FRecipe recipe;

	@Override
	public String toString() {
		return recipeName;
	}
}