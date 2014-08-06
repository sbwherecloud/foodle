package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.util.FFormat;
import com.google.gson.annotations.SerializedName;

public class FUserInfo {

	@SerializedName("first_name")
	public String	firstName;
	@SerializedName("last_name")
	public String	lastName;
	@SerializedName("email")
	public String	email;
	@SerializedName("phone_number")
	public String	phoneNumber;

	public boolean isValid() {
		return isFirstNameValid() && isLastNameValid() && isEmailValid();
	}

	public boolean isFirstNameValid() {
		return firstName != null && !firstName.isEmpty() && firstName.length() > 1;
	}

	public boolean isLastNameValid() {
		return lastName != null && !lastName.isEmpty() && lastName.length() > 1;
	}

	public boolean isEmailValid() {
		return FFormat.isValidEmail(email);
	}
}
