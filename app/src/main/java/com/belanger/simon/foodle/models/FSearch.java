package com.belanger.simon.foodle.models;

import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.datastructures.FObservable;
import com.belanger.simon.foodle.datastructures.FValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents a search constructed by the user. The FSearch object contains
 * several observable fields and registers to those fields to propagate events
 * to other objects.
 * 
 * Fields marked as transient do not propagate events; object must register
 * directly to those fields. This is to prevents observance cycles.
 * 
 */
public class FSearch extends FObservable implements Observer {

	public static final String	SORT_BY_DISCOUNT_ABSOLUTE	= "discount_dollars";
	public static final String	SORT_BY_DISCOUNT_RELATIVE	= "discount_percent";
	public static final String	SORT_BY_DISTANCE			= "distance";
	public static final String	SORT_BY_PRICE				= "liquidation_price";
	public static final String	SORT_BY_MILEAGE				= "mileage";
	public static final String	SORT_BY_YEAR				= "model_year";
	public static final String	SORT_BY_PUBLICATION_DATE	= "listingage";

	public static final String	DEFAULT_SORT				= SORT_BY_DISCOUNT_RELATIVE;

	public static final String	ORDER_ASCENDING				= "asc";
	public static final String	ORDER_DESCENDING			= "desc";

	public static final String	DEFAULT_ORDER				= ORDER_DESCENDING;

	public static final int	DEFAULT_MAX_COOKING_TIME = 600;

	public FValue<String> sortBy = new FValue<String>(DEFAULT_SORT);
	public FValue<String> order	= new FValue<String>(DEFAULT_ORDER);
	public FValue<Integer> cookingTimeMax = new FValue<Integer>();
	public FList<FIngredient> ingredients = new FList<FIngredient>();

//	private transient FList<FToken> tokens = null;
	public transient final FSearchConstraints constraints = new FSearchConstraints();
	private transient final FList<FSearchResult> results = new FList<FSearchResult>();

	public FSearch() {
		try {
			Field[] fields = FSearch.class.getFields();
			for (Field f : fields) {
				if (Observable.class.isInstance(f.get(this))) {
					if (!Modifier.isTransient(f.getModifiers())) {
						Observable o = (Observable) f.get(this);
						o.addObserver(this);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Observing internal changes and forwarding them outside
	@Override
	public void update(Observable observable, Object data) {
		// When the search is modified,
											// reset this flag
		setChanged();
		notifyObservers(data);
	}

	public FList<FSearchResult> getResults() {
		return results;
	}

	public void hardReset() {
        this.silence();
		sortBy.set(DEFAULT_SORT);
		order.set(DEFAULT_ORDER);
		ingredients.clear();
		cookingTimeMax.set(DEFAULT_MAX_COOKING_TIME);
		results.clear();
        this.unsilence();
	}
	
	/***
	 * @see FToken
	 */
//	public FList<FToken> getTokens() {
//		if (tokens == null) {
//			tokens = new CRList<CRToken>();
//			CRToken token = null;
//
//			// Make model trims
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatMakeModelTrim(makeModelTrim, CRFormat.get(R.string.all_makes_models)));
//			token.observe(makeModelTrim);
//			tokens.add(token);
//
//			// Conditions
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatWithSlashes(conditions, CRFormat.get(R.string.condition)));
//			token.observe(conditions);
//			tokens.add(token);
//
//			// Year
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.year)));
//			token.observe(yearMin);
//			token.observe(yearMax);
//			tokens.add(token);
//
//			// Price
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.price)));
//			token.observe(priceMin);
//			token.observe(priceMax);
//			tokens.add(token);
//
//			// Mileage
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.mileage)));
//			token.observe(mileageMin);
//			token.observe(mileageMax);
//			tokens.add(token);
//
//			// Fuel Type
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.fuel_type)));
//			token.observe(fuelType);
//			tokens.add(token);
//
//			// Drive Train
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.drive_train)));
//			token.observe(driveTrain);
//			tokens.add(token);
//
//			// Transmission
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.transmission)));
//			token.observe(transmission);
//			tokens.add(token);
//
//			// Max Listing Age
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.listing_age)));
//			token.observe(maxListingAge);
//			tokens.add(token);
//
//			// Ext color
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.ext_colors)));
//			token.observe(exteriorColor);
//			tokens.add(token);
//
//			// Distance
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.distance)));
//			token.observe(radius);
//			tokens.add(token);
//
//			// Liquidation
//			token = new CRToken();
//			token.setFormatStrategy(new CRTokenFormatReturnTitle(CRFormat.get(R.string.liquidation)));
//			token.observe(isLiquidation);
//			tokens.add(token);
//		}
//		return tokens;
//	}
}