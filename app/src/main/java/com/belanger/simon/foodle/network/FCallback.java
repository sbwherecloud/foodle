package com.belanger.simon.foodle.network;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

// For logging, use setLogLevel on RestAdapter in FWebService

public class FCallback<T> implements Callback<T> {

	@Override
	public void failure(RetrofitError error) {

	}

	@Override
	public void success(T object, Response response) {

	}
}