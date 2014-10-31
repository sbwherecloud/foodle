package com.belanger.simon.foodle.views;

/**
 * Created by SimonPro on 14-10-30.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.models.FUserInfo;
import com.belanger.simon.foodle.models.transactions.FUserProfileResponse;
import com.belanger.simon.foodle.network.FCallback;
import com.belanger.simon.foodle.network.FWebService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class FUserInfoDialog extends Dialog {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "573624315414";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    public static final String EXTRA_MESSAGE = "message";
    GCMRegistrationTask task;
    final Context context;

    public FUserInfoDialog(final Context context) {
        super(context);

        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.f_dialog_userinfo);

        final ViewGroup container = (ViewGroup) findViewById(R.id.userDialogContainer);
        container.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm =
                        (InputMethodManager) FUserInfoDialog.this.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                return false;
            }
        });

        final EditText firstName = (EditText) findViewById(R.id.firstNameUserInfoDialog);
        final EditText lastName = (EditText) findViewById(R.id.lastNameUserInfoDialog);
        final EditText email = (EditText) findViewById(R.id.emailUserInfoDialog);

        Button registerButton = (Button) findViewById(R.id.userInfoRegisterButton);

        registerButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FUserInfo newUserInfo = new FUserInfo();
                newUserInfo.firstName = firstName.getText().toString();
                newUserInfo.lastName = lastName.getText().toString();
                newUserInfo.email = email.getText().toString();

                if (newUserInfo.isValid()) {
//                    String eventName = getContext().getString(R.string.analyticsEventRegistration);

//                    CRApplication.getInstance().sendGoogleAnalyticsEvents(eventName);

                    FWebService.getInstance().insertUserProfile(newUserInfo, new FCallback<FUserInfo>(){
                        @Override
                        public void success(FUserInfo object, Response response) {
                            super.success(object, response);
                            FAppState.getInstance().setUserInfo(object);
                            if(checkPlayServices()) {
                                gcm = GoogleCloudMessaging.getInstance(context);
                                regid = getRegistrationId(context);

                                if (regid.isEmpty()) {
                                    task = new GCMRegistrationTask();
                                    task.execute();
                                }
                            } else {
                                Log.i(TAG, "No valid Google Play Services APK found.");
                            }
                            dismiss();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            super.failure(error);
                            Toast t = Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_LONG);
                            t.setGravity(Gravity.TOP, 0, 0);
                            t.show();
                        }
                    });
                } else {
                    Toast t = Toast.makeText(getContext(), R.string.enter_required_fields, Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP, 0, 0);
                    t.show();
                }
            }
        });
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = FAppState.getInstance().getGcmRegistrationId().get();
        if (registrationId == null) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = FAppState.getInstance().getAppVersion().get();
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend(regid);

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
        }


    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String regid) {
        FWebService.getInstance().updateGcmRegistrationId(FAppState.getInstance().getUserInfo().email,
                regid, new FCallback<FUserInfo>() {
                    @Override
                    public void success(FUserInfo object, Response response) {
                        super.success(object, response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                    }
                });
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        FAppState.getInstance().getGcmRegistrationId().set(regId);
        FAppState.getInstance().getAppVersion().set(appVersion);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                ((Activity)context).finish();
            }
            return false;
        }
        return true;
    }
}

