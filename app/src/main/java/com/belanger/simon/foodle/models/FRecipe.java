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
    @SerializedName("recipe_category")
    public String recipeCategory;
	@SerializedName("cooking_time")
	public int cookingTime;
    @SerializedName("cooking_temperature")
    public int cookingTemperature;
    @SerializedName("portions")
    public int portions;
	@SerializedName("ingredients")
	public FList<FIngredient> ingredients;

    public FRecipe(String recipeName){
        this.recipeName = recipeName;
    }

	@Override
	public boolean equals(Object o) {
		if (o != null && o.getClass().equals(FRecipe.class)) {
			FRecipe other = (FRecipe) o;
			return this.recipeName.equals(other.recipeName);
		}
		return false;
	}
}
