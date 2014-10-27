package com.belanger.simon.foodle.network;

import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.datastructures.FValue;
import com.belanger.simon.foodle.models.FRecipe;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

public class FWebService {

	private static FWebServiceInterface instance;

	protected FWebService() {
	};

	public static FWebServiceInterface getInstance() {
		if (instance == null) {

//			if (FEndpoint.USE_LOCAL_MOCK) {
//				instance = new FWebServiceMock();
//				return instance;
//			}

			String server = FEndpoint.USE_MOCK ? FEndpoint.BASE_URL_MOCK : FEndpoint.BASE_URL;

			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(server)
					.setConverter(new GsonConverter(getGson()))
					.setLogLevel(LogLevel.FULL).build();

			instance = restAdapter.create(FWebServiceInterface.class);
		}

		return instance;
	}

	/*
	 * JSON SERIALIZATION / DESERIALIZATION SUPPORT
	 */

	private static Gson getGson() {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				// TODO: remove, only for debugging
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(FValue.class, new FValue.FJsonSerializer())
				.registerTypeAdapter(FValueOfInteger, new FValue.FJsonDeserializer<Integer>(Integer.class))
				.registerTypeAdapter(FValueOfString, new FValue.FJsonDeserializer<String>(String.class))
				.registerTypeAdapter(FValueOfFloat, new FValue.FJsonDeserializer<Float>(Float.class))
				.registerTypeAdapter(FList.<String>getType(), new FList.FJsonDeserializer<String>(String.class))
				.registerTypeAdapter(FList.<FRecipe> getType(),
						new FList.FJsonDeserializer<FRecipe>(FRecipe.class))
				.create();
		return gson;
	}

	private static final Type FValueOfInteger = new TypeToken<FValue<Integer>>() {
													}.getType();
	private static final Type FValueOfString = new TypeToken<FValue<String>>() {
													}.getType();
	private static final Type FValueOfFloat = new TypeToken<FValue<Float>>() {
													}.getType();
}