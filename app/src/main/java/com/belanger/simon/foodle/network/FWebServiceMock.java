//package com.belanger.simon.foodle.network;
//
//import com.coderougemedia.models.CRBodyType;
//import com.coderougemedia.models.CRCity;
//import com.coderougemedia.models.CRCoupon;
//import com.coderougemedia.models.CRDeal;
//import com.coderougemedia.models.CRDealer;
//import com.coderougemedia.models.CRDealerOpenHours;
//import com.coderougemedia.models.CRMake;
//import com.coderougemedia.models.CRModelTrim;
//import com.coderougemedia.models.CRSearchConstraints;
//import com.coderougemedia.models.CRSearchCriteria;
//import com.coderougemedia.models.CRSearchResult;
//import com.coderougemedia.models.CRVehicle;
//import com.coderougemedia.models.CRVisit;
//import com.coderougemedia.models.CRVisitor;
//import com.coderougemedia.models.transactions.CRAuthRequest;
//import com.coderougemedia.models.transactions.CRAuthResponse;
//import com.coderougemedia.models.transactions.CRCheckinResponse;
//import com.coderougemedia.models.transactions.CRCitiesResponse;
//import com.coderougemedia.models.transactions.CRCouponsResponse;
//import com.coderougemedia.models.transactions.CRDealersResponse;
//import com.coderougemedia.models.transactions.CRHotDealsResponse;
//import com.coderougemedia.models.transactions.CRIncrementalSearchResponse;
//import com.coderougemedia.models.transactions.CRRegisterRequest;
//import com.coderougemedia.models.transactions.CRSearchCountRequestRoot;
//import com.coderougemedia.models.transactions.CRSearchCountResponse;
//import com.coderougemedia.models.transactions.CRSearchResultsRequest;
//import com.coderougemedia.models.transactions.CRSearchResultsResponse;
//import com.coderougemedia.models.transactions.CRVehicleInventoryResponse;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Random;
//
//import retrofit.Callback;
//import retrofit.client.Response;
//
//public class FWebServiceMock implements FWebServiceInterface {
//
//	private Response	r	= null;
//
//	@Deprecated
//	@Override
//	public void performRegister(CRRegisterRequest registerRequest, Callback<CRAuthResponse> cb) {
//		CRAuthResponse ar = new CRAuthResponse();
//		ar.firstName = registerRequest.firstName;
//		ar.lastName = registerRequest.lastName;
//		ar.email = registerRequest.email;
//		ar.leadId = "123456789";
//		cb.success(ar, r);
//	}
//
//	@Deprecated
//	@Override
//	public void performAuthenticate(CRAuthRequest authRequest, Callback<CRAuthResponse> cb) {
//		if (authRequest.email.equals("test@codered.com")) {
//			CRAuthResponse ar = new CRAuthResponse();
//			ar.firstName = "Car";
//			ar.lastName = "Lover";
//			ar.email = authRequest.email;
//			ar.leadId = "123456789";
//			cb.success(ar, r);
//		} else {
//			CRAuthResponse ar = new CRAuthResponse();
//			ar.firstName = "";
//			ar.lastName = "";
//			ar.email = "";
//			ar.leadId = "";
//			cb.failure(null);
//		}
//	}
//
//	@Override
//	public void performVisitorRequest(Callback<CRVisitor> cb) {
//		CRVisitor v = new CRVisitor();
//		v.visitorId = "1111111";
//		cb.success(v, r);
//	}
//
//	@Override
//	public void performVisitRequest(Callback<CRVisit> cb) {
//		CRVisit v = new CRVisit();
//		v.visitId = "VISIT_ID";
//		cb.success(v, r);
//	}
//
//	@Override
//	public void performSearchCountResquest(CRSearchCountRequestRoot search, float latitude, float longitude,
//			Callback<CRSearchCountResponse> cb) {
//		CRSearchCountResponse scr = new CRSearchCountResponse();
//		scr.searchConstraints = new CRSearchConstraints();
//
//		// Body types
//
//		for (int i = 0; i < 16; i++) {
//			CRBodyType bodyType = new CRBodyType();
//			bodyType.displayName = "Category " + i;
//			bodyType.count = i;
//			bodyType.id = "BODYTYPE" + i;
//			bodyType.imageUrl = "http://www.iconsdb.com/icons/preview/gray/car-7-xxl.png";
//			scr.searchConstraints.bodyTypes.add(bodyType);
//		}
//
//		// Make Model Trim
//
//		Random rnd = new Random();
//
//		String[] makes = {"Honda", "Chrysler", "Dodge", "Jeep"};
//		String[] modeltrims = {"Civic", "Accord", "200", "300", "Grand Cherokee"};
//
//		for (int i = 0; i < 5; i++) {
//			CRMake make = new CRMake();
//			make.makeId = "MAKE" + i;
//			make.makeName = makes[rnd.nextInt(makes.length)];
//			make.count = i;
//
//			for (int k = 0; k < i + 2; k++) {
//				CRModelTrim modelTrim = new CRModelTrim();
//				modelTrim.count = i;
//				modelTrim.makeId = make.makeId;
//				modelTrim.makeName = make.makeName;
//				modelTrim.modelTrimId = "modeltrim" + k;
//				modelTrim.modelTrimName = modeltrims[rnd.nextInt(modeltrims.length)];
//				make.trimModels.add(modelTrim);
//			}
//
//			scr.searchConstraints.makeModelTrims.add(make);
//		}
//
//		CRSearchCriteria sc = null;
//
//		// Conditions
//
//		sc = new CRSearchCriteria();
//		sc.count = 20;
//		sc.displayName = "New";
//		sc.id = "CONDITION1";
//		scr.searchConstraints.conditions.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 30;
//		sc.displayName = "Used";
//		sc.id = "CONDITION2";
//		scr.searchConstraints.conditions.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 30;
//		sc.displayName = "Certified";
//		sc.id = "CONDITION3";
//		scr.searchConstraints.conditions.add(sc);
//
//		scr.searchConstraints.priceMin.set(10000);
//		scr.searchConstraints.priceMax.set(150000);
//
//		scr.searchConstraints.yearMin.set(2005);
//		scr.searchConstraints.yearMax.set(2014);
//
//		sc = new CRSearchCriteria();
//		sc.count = 14;
//		sc.displayName = "Any";
//		sc.id = "TRANSMISSION0";
//		scr.searchConstraints.transmissions.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 14;
//		sc.displayName = "Manual";
//		sc.id = "TRANSMISSION1";
//		scr.searchConstraints.transmissions.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 14;
//		sc.displayName = "Auto";
//		sc.id = "TRANSMISSION2";
//		scr.searchConstraints.transmissions.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 22;
//		sc.displayName = "Any";
//		sc.id = "DRIVETRAIN0";
//		scr.searchConstraints.driveTrains.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 22;
//		sc.displayName = "4WD";
//		sc.id = "DRIVETRAIN1";
//		scr.searchConstraints.driveTrains.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 22;
//		sc.displayName = "FWD";
//		sc.id = "DRIVETRAIN2";
//		scr.searchConstraints.driveTrains.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Any";
//		sc.id = "FUEL0";
//		scr.searchConstraints.fuelTypes.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Petrol";
//		sc.id = "FUEL1";
//		scr.searchConstraints.fuelTypes.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Diesel";
//		sc.id = "FUEL2";
//		scr.searchConstraints.fuelTypes.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Any";
//		sc.id = "COLOR0";
//		scr.searchConstraints.exteriorColors.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "White";
//		sc.id = "COLOR1";
//		scr.searchConstraints.exteriorColors.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Black";
//		sc.id = "COLOR2";
//		scr.searchConstraints.exteriorColors.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 25;
//		sc.displayName = "Crimson";
//		sc.id = "COLOR3";
//		scr.searchConstraints.exteriorColors.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 839;
//		sc.displayName = "30 days";
//		sc.id = "LISTAGE0";
//		scr.searchConstraints.maxListingAges.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 839;
//		sc.displayName = "15 days";
//		sc.id = "LISTAGE1";
//		scr.searchConstraints.maxListingAges.add(sc);
//
//		sc = new CRSearchCriteria();
//		sc.count = 839;
//		sc.displayName = "5 days";
//		sc.id = "LISTAGE2";
//		scr.searchConstraints.maxListingAges.add(sc);
//
//		scr.searchConstraints.mileageMin.set(0);
//		scr.searchConstraints.mileageMax.set(10000);
//
//		scr.searchConstraints._liquidations.set(1);
//
//		scr.searchConstraints.totalCount.set((int) (150 * Math.random()) + 1);
//
//		cb.success(scr, r);
//	}
//
//	@Override
//	public void performSearchResultsRequest(CRSearchResultsRequest search, int from, int to, float latitude,
//			float longitude, boolean store, Callback<CRSearchResultsResponse> cb) {
//
//		CRSearchResultsResponse srr = new CRSearchResultsResponse();
//
//		String[] trims = new String[]{"SE", "Sedan", "GX", "Type R", "VTI"};
//
//		GregorianCalendar calendar = new GregorianCalendar();
//		calendar.setTime(new Date());
//
//		List<CRSearchResult> newResults = new ArrayList<CRSearchResult>();
//		for (int i = from; i <= to; i++) {
//			CRSearchResult newResult = new CRSearchResult();
//			newResult.referenceNumber = "VEHICLE_ID_" + i;
//			newResult.vin = "HHOPWAPN";
//			CRModelTrim makeModelTrim = new CRModelTrim();
//			makeModelTrim.makeName = "Honda";
//			makeModelTrim.modelTrimName = "Civic " + trims[i % trims.length];
//			newResult.makeModelTrim = makeModelTrim;
//			newResult.year = 2000 + i;
//			newResult.price = 20000 + i * 1000;
//			newResult.transmission = new CRSearchCriteria();
//			newResult.transmission.displayName = "manual";
//			newResult.odometer = (int) (15092 + 200.0f * Math.sin(i));
//			newResult.dealer = new CRDealer();
//			int dealer_i = i % 3;
//			if (i % 3 == 0) {
//				newResult.dealer.latitude = (float) (45.5000f + dealer_i / 200.0f * Math.sin(dealer_i));
//				newResult.dealer.longitude = (float) (-73.5667f + dealer_i / 200.0f * Math.cos(dealer_i));
//				newResult.dealer.dealerId = "DEALER" + dealer_i;
//				newResult.dealer.dealerName = "Dealer " + dealer_i;
//			} else {
//				// Wherecloud
//				newResult.dealer.latitude = 45.519548f;
//				newResult.dealer.longitude = -73.585374f;
//				newResult.dealer.dealerId = "DEALER_WHERECLOUD";
//				newResult.dealer.dealerName = "Wherecars";
//			}
//
//			if (i % 3 == 0) {
//				newResult.deal = new CRDeal();
//				newResult.deal.dealPrice = newResult.price - 1000;
//				calendar.add(Calendar.DATE, 1);
//				calendar.add(Calendar.HOUR, i);
//				newResult.deal.dealEndDate = calendar.getTime();
//			}
//			if (i % 9 == 0) {
//				newResult.image = "http://prlmotorsports.com/assets/2012-honda-civic-si-300x300.jpg";
//			} else if (i % 4 == 0) {
//				newResult.image = "http://upload.wikimedia.org/wikipedia/commons/5/57/2007_Honda_Civic_TypeR_01.JPG";
//			} else if (i % 5 == 0) {
//				newResult.image = "http://www.frontierhonda.ab.ca/images/landing/honda-civic.jpg";
//			} else if (i % 7 == 0) {
//				newResult.image = "http://www.autos.ca/testdrives/images/01civic_si_rear2.jpg";
//			} else if (i % 3 == 0) {
//				newResult.image = "http://d3img3do1wj44f.cloudfront.net/wp-content/uploads/2013/09/2012-Honda-Civic-Coupe-1280x854-300x200.jpg";
//			} else {
//				newResult.image = "http://www.autoviva.com/img/photos/322/honda_civic_1_8_executive_navi_small_19322.jpg";
//			}
//
//			newResults.add(newResult);
//		}
//
//		srr.searchResults = newResults;
//
//		cb.success(srr, r);
//	}
//
//	@Override
//	public void performVehicleInventoryRequest(String vehicleInventoryId, Callback<CRVehicleInventoryResponse> cb) {
//		CRVehicle v = new CRVehicle();
//
//		v.condition = new CRSearchCriteria();
//		v.condition.displayName = "New";
//
//		v.genericColorExt = new CRSearchCriteria();
//		v.genericColorExt.displayName = "White";
//
//		v.specificColorExt = new CRSearchCriteria();
//		v.specificColorExt.displayName = "Deep White";
//
//		v.specificColorInt = new CRSearchCriteria();
//		v.specificColorInt.displayName = "Deep Black";
//
//		v.odometer = 48124;
//
//		v.drivetrain = new CRSearchCriteria();
//		v.drivetrain.displayName = "FWD";
//
//		v.fuelType = new CRSearchCriteria();
//		v.fuelType.displayName = "Regular Unleaded";
//
//		v.dealer = new CRDealer();
//		v.dealer.dealerId = "HB091";
//		v.dealer.dealerName = "Honda Brossard";
//		v.dealer.phone = "514-555-5555";
//		v.dealer.email = "honda@brossard.com";
//		v.dealer.city = " Montreal";
//		v.dealer.latitude = 45.5000f;
//		v.dealer.longitude = 73.5667f;
//		v.dealer.postalCode = "H2H 2H2";
//		v.dealer.website = "http://www.hondabrossard.com";
//		v.dealer.province = "Quebec";
//		v.dealer.hours = new CRDealerOpenHours();
//		v.dealer.hours.mondayOpen = "closed";
//		v.dealer.hours.mondayClose = "";
//		v.dealer.hours.tuesdayOpen = "1PM";
//		v.dealer.hours.tuesdayClose = "6PM";
//		v.dealer.hours.wednesdayOpen = "9AM";
//		v.dealer.hours.wednesdayClose = "6AM";
//		v.dealer.hours.thursdayOpen = "9AM";
//		v.dealer.hours.thursdayOpen = "6AM";
//		v.dealer.hours.fridayOpen = "9AM";
//		v.dealer.hours.fridayOpen = "6AM";
//		v.dealer.hours.saturdayOpen = "10AM";
//		v.dealer.hours.saturdayClose = "5PM";
//		v.dealer.hours.sundayOpen = "10AM";
//		v.dealer.hours.sundayClose = "4PM";
//
//		CRVehicleInventoryResponse vir = new CRVehicleInventoryResponse();
//		vir.vehicle = v;
//
//		cb.success(vir, r);
//	}
//
//	@Override
//	public void performCouponRequest(String referenceNumber, String firstName, String lastName, String email,
//			String phoneNumber, Callback<CRCoupon> cb) {
//		CRCoupon coupon = new CRCoupon();
//		coupon.couponId = "ABX12";
//		CRSearchResult newResult = new CRSearchResult();
//		newResult.referenceNumber = referenceNumber;
//		newResult.vin = "HHOPWAPN";
//		CRModelTrim makeModelTrim = new CRModelTrim();
//		makeModelTrim.makeName = "Honda";
//		makeModelTrim.modelTrimName = "Civic SE";
//		newResult.makeModelTrim = makeModelTrim;
//		newResult.year = 2010;
//		newResult.price = 22000;
//		newResult.transmission = new CRSearchCriteria();
//		newResult.transmission.displayName = "manual";
//		newResult.odometer = 15092;
//		newResult.dealer = new CRDealer();
//		newResult.dealer.dealerName = "Dealer 1";
//		newResult.dealer.latitude = 45.5000f;
//		newResult.dealer.longitude = 73.5667f;
//		newResult.deal = new CRDeal();
//		newResult.deal.dealPrice = newResult.price - 1000;
//		newResult.image = "http://prlmotorsports.com/assets/2012-honda-civic-si-300x300.jpg";
//
//		newResult.hasCoupon = true;
//		coupon.vehicle = newResult;
//		coupon.meetingDate = null;
//		cb.success(coupon, r);
//	}
//
//	@Override
//	public void performCouponUpdate(CRCoupon coupon, Callback<CRCoupon> cb) {
//	}
//
//	@Override
//	public void performCouponsRequest(Callback<CRCouponsResponse> cb) {
//
//		CRCouponsResponse cr = new CRCouponsResponse();
//		cr.coupons = new ArrayList<CRCoupon>();
//
//		String[] trims = new String[]{"SE", "Sedan", "GX", "Type R", "VTI"};
//
//		for (int i = 0; i <= 5; i++) {
//			CRCoupon cert = new CRCoupon();
//			CRSearchResult newResult = new CRSearchResult();
//			newResult.referenceNumber = "VEHICLE_ID_" + i;
//			newResult.vin = "HHOPWAPN";
//			CRModelTrim makeModelTrim = new CRModelTrim();
//			makeModelTrim.makeName = "Honda";
//			makeModelTrim.modelTrimName = "Civic " + trims[i % trims.length];
//			newResult.makeModelTrim = makeModelTrim;
//			newResult.year = 2000 + i;
//			newResult.price = 20000 + i * 1000;
//			newResult.transmission = new CRSearchCriteria();
//			newResult.transmission.displayName = "manual";
//			newResult.odometer = 15092;
//			newResult.dealer = new CRDealer();
//			newResult.dealer.dealerName = "Dealer " + i;
//			newResult.dealer.latitude = (float) (-33.867f + i / 200.0f * Math.sin(i));
//			newResult.dealer.longitude = (float) (151.206f + i / 200.0f * Math.cos(i));
//			if (i % 3 == 0) {
//				newResult.deal = new CRDeal();
//				newResult.deal.dealPrice = newResult.price - 1000;
//			}
//			if (i % 9 == 0) {
//				newResult.image = "http://prlmotorsports.com/assets/2012-honda-civic-si-300x300.jpg";
//			} else if (i % 4 == 0) {
//				newResult.image = "http://upload.wikimedia.org/wikipedia/commons/5/57/2007_Honda_Civic_TypeR_01.JPG";
//			} else if (i % 5 == 0) {
//				newResult.image = "http://www.frontierhonda.ab.ca/images/landing/honda-civic.jpg";
//			} else if (i % 7 == 0) {
//				newResult.image = "http://www.autos.ca/testdrives/images/01civic_si_rear2.jpg";
//			} else if (i % 3 == 0) {
//				newResult.image = "http://d3img3do1wj44f.cloudfront.net/wp-content/uploads/2013/09/2012-Honda-Civic-Coupe-1280x854-300x200.jpg";
//			} else {
//				newResult.image = "http://www.autoviva.com/img/photos/322/honda_civic_1_8_executive_navi_small_19322.jpg";
//			}
//
//			newResult.hasCoupon = true;
//			cert.vehicle = newResult;
//			if (i % 2 != 0) {
//				cert.meetingDate = null;
//			} else {
//				cert.meetingDate = new Date();
//			}
//			cr.coupons.add(cert);
//		}
//
//		cb.success(cr, r);
//	}
//
//	@Override
//	public void performDealerRequest(String dealerId, Callback<CRDealer> cb) {
//		CRDealer d = new CRDealer();
//		d.city = "Montreal";
//		d.dealerId = dealerId;
//		d.dealerName = "Honda Dealer";
//		cb.success(d, r);
//	}
//
//	@Override
//	public void performDealersRequest(int from, int to, float latitude, float longitude, int radius,
//			Callback<CRDealersResponse> cb) {
//		CRDealersResponse dr = new CRDealersResponse();
//		dr.dealers = new ArrayList<CRDealer>();
//		for (int i = from; i <= to; i++) {
//			CRDealer d = new CRDealer();
//			d.city = "Montreal";
//			d.province = "Quebec";
//			d.postalAddress = "1 Biscotti avenue";
//			d.dealerId = i + "";
//			d.dealerName = "Honda Dealer " + i;
//			d.latitude = 45.5000f;
//			d.longitude = -73.5667f;
//			d.hours = new CRDealerOpenHours();
//			d.hours.mondayOpen = "closed";
//			d.hours.mondayClose = "";
//			d.hours.tuesdayOpen = "1PM";
//			d.hours.tuesdayClose = "6PM";
//			d.hours.wednesdayOpen = "9AM";
//			d.hours.wednesdayClose = "6AM";
//			d.hours.thursdayOpen = "9AM";
//			d.hours.thursdayOpen = "6AM";
//			d.hours.fridayOpen = "9AM";
//			d.hours.fridayOpen = "6AM";
//			d.hours.saturdayOpen = "10AM";
//			d.hours.saturdayClose = "5PM";
//			d.hours.sundayOpen = "10AM";
//			d.hours.sundayClose = "4PM";
//			dr.dealers.add(d);
//		}
//		cb.success(dr, r);
//	}
//
//	@Override
//	public void performIncrementalSearchRequest(int from, int to, float latitude, float longitude, String text,
//			Callback<CRIncrementalSearchResponse> cb) {
//
//		CRIncrementalSearchResponse isr = new CRIncrementalSearchResponse();
//		isr.makeModelTrims = new ArrayList<CRModelTrim>();
//
//		if (text != null && !text.isEmpty()) {
//			Random rnd = new Random();
//
//			String[] modelTrims = {"Accord", "Civic"};
//
//			int limit = 30;
//
//			int min = from <= limit ? from : limit;
//			int max = to <= limit ? to : limit;
//			for (int i = min; i <= max; i++) {
//				CRModelTrim modelTrim = new CRModelTrim();
//				modelTrim.count = rnd.nextInt(200);
//				modelTrim.makeId = "MAKE" + i;
//				modelTrim.makeName = "Honda";
//				modelTrim.modelTrimId = "modeltrim" + i;
//				if (i % 3 != 0) {
//					modelTrim.modelTrimName = modelTrims[rnd.nextInt(modelTrims.length)];
//				}
//				isr.makeModelTrims.add(modelTrim);
//			}
//		}
//
//		cb.success(isr, r);
//	}
//
//	@Override
//	public void performCitiesRequest(int from, int to, Callback<CRCitiesResponse> cb) {
//
//		CRCity[] cities = new CRCity[6];
//
//		int k = 0;
//
//		CRCity city = new CRCity();
//		city.name = "Montreal";
//		city.region = "Quebec";
//		city.latitude = 45.5f;
//		city.longitude = 73.5667f;
//		cities[k++] = city;
//
//		city = new CRCity();
//		city.name = "Quebec City";
//		city.region = "Quebec";
//		city.latitude = 46.8167f;
//		city.longitude = 71.2167f;
//		cities[k++] = city;
//
//		city = new CRCity();
//		city.name = "Toronto";
//		city.region = "Ontario";
//		city.latitude = 43.7000f;
//		city.longitude = 79.4000f;
//		cities[k++] = city;
//
//		city = new CRCity();
//		city.name = "Ottawa";
//		city.region = "Ontario";
//		city.latitude = 45.4214f;
//		city.longitude = 75.6919f;
//		cities[k++] = city;
//
//		city = new CRCity();
//		city.name = "Saskatoon";
//		city.region = "Saskatchewan";
//		city.latitude = 55.0000f;
//		city.longitude = 106.0000f;
//		cities[k++] = city;
//
//		city = new CRCity();
//		city.name = "Edmonton";
//		city.region = "Alberta";
//		city.latitude = 53.5333f;
//		city.longitude = 113.5000f;
//		cities[k++] = city;
//
//		CRCitiesResponse cr = new CRCitiesResponse();
//		cr.cities = new ArrayList<CRCity>();
//		for (int i = from - 1; i < to; i++) {
//			if (i >= 0 && i < cities.length) {
//				cr.cities.add(cities[i]);
//			}
//		}
//
//		cb.success(cr, r);
//	}
//
//	@Override
//	public void performCheckinRequest(String couponId, float latitude, float longitude, Callback<CRCheckinResponse> cb) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void performSimilarDealsRequest(float latitude, float longitude, String referenceNumber,
//			Callback<CRSearchResultsResponse> cb) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void performRequestForHotDeals(Callback<CRHotDealsResponse> cb) {
//		// TODO Auto-generated method stub
//
//	}
//
//}