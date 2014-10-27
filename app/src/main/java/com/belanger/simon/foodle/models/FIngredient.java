package com.belanger.simon.foodle.models;

import com.google.gson.annotations.SerializedName;

public class FIngredient {
	@SerializedName("ingredient_id")
	public String				ingredientId;
	@SerializedName("ingredient_name")
	public String				ingredientName;
	@SerializedName("voteId")
	public String				data;

    @Override
	public boolean equals(Object o) {
		FIngredient other = null;
		if (o != null && o instanceof FIngredient) {
			other = (FIngredient) o;
			if (this.ingredientId != null) {
				return this.ingredientId.equals(other.ingredientId);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}