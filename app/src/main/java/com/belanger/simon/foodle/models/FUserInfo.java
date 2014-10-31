package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.util.FFormat;
import com.google.gson.annotations.SerializedName;

public class FUserInfo {

	@SerializedName("firstName")
	public String	firstName;
	@SerializedName("lastName")
	public String	lastName;
	@SerializedName("email")
	public String	email;

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

    public boolean equals(Object obj){
        if(!(obj instanceof FUserInfo))
            return false;

        return this.email.equals(((FUserInfo) obj).email);
    }

    public int hashCode(){
        return this.email.hashCode();
    }
}
