package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.datastructures.FList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FRecipe {
	@SerializedName("reference_number")
	public String referenceNumber;
    @SerializedName("recipe_name")
    public String recipeName;
	@SerializedName("cooking_time")
	public int cookingTime;
    @SerializedName("cooking_temperature")
    public int cookingTemperature;
    @SerializedName("portions")
    public int portions;
	@SerializedName("ingredients")
	public FList<FIngredient> ingredients;

	public boolean					fetchedInfo	= false;

	@Override
	public boolean equals(Object o) {
		if (o != null && o.getClass().equals(FRecipe.class)) {
			FRecipe other = (FRecipe) o;
			return this.referenceNumber.equals(other.referenceNumber);
		}
		return false;
	}
}
