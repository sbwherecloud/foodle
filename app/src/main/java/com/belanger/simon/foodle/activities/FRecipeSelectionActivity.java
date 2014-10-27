package com.belanger.simon.foodle.activities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.FFlowManager;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.adapters.FCandidateListViewAdapter;
import com.belanger.simon.foodle.adapters.FRecipeListViewAdapter;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FRecipe;
import com.belanger.simon.foodle.models.FUserInfo;
import com.belanger.simon.foodle.models.FVote;
import com.belanger.simon.foodle.models.transactions.FVoteRequest;
import com.belanger.simon.foodle.network.FCallback;
import com.belanger.simon.foodle.network.FWebService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.RetrofitError;
import retrofit.client.Response;

@Layout(R.layout.f_activity_recipe_selection)
public class FRecipeSelectionActivity extends FActivity implements FRecipeListViewAdapter.OnAddRecipe {

    @ViewOutlet(R.id.recipeSelectionRecipes) public ListView recipesListView;
    @ViewOutlet(R.id.recipeSelectionCandidateRecipes) public ListView candidateRecipes;
    @ViewOutlet(R.id.recipeSelectionSendCandidatesButton) public Button sendCandidatesButton;
    @ViewOutlet(R.id.recipeSelectionGetCandidatesButton) public Button getCandidatesButton;
    @ViewOutlet(R.id.recipeSelectionCurrentGroup) public TextView currentGroup;

    public static final String EXTRA_MESSAGE = "message";
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
    Context context;
    GCMRegistrationTask task;
    String message = FFlowManager.getInstance().getRecipeSelectionMessage();

    private MenuItem addRecipe;
    private FList<FRecipe> recipes = FAppState.getInstance().getCustomRecipes();
    private FList<FRecipe> candidates = new FList<FRecipe>();
    private FCandidateListViewAdapter candidatesAdapter;
    private FRecipeListViewAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.recipe_selection));

        currentGroup.setText(message);

        context = getApplicationContext();

        //if(checkPlayServices()) {
        //    gcm = GoogleCloudMessaging.getInstance(this);
        //    regid = getRegistrationId(context);

        //    if (regid.isEmpty()) {
        //        task = new GCMRegistrationTask();
        //        task.execute();
        //    }
        //} else {
        //    Log.i(TAG, "No valid Google Play Services APK found.");
        //}

        recipesAdapter = new FRecipeListViewAdapter(this, recipes);
        recipesAdapter.setOnAddRecipeListener(this);
        recipesListView.setAdapter(recipesAdapter);

        candidatesAdapter = new FCandidateListViewAdapter(this, candidates);
        candidateRecipes.setAdapter(candidatesAdapter);

        sendCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> candidatesName = new ArrayList<String>();
                for(FRecipe recipe : candidates){
                    candidatesName.add(recipe.recipeName);
                }
                List<String> votersEmail = new ArrayList<String>();
                votersEmail.add("simbeliric@gmail.com");
                FVoteRequest vote = new FVoteRequest();
                vote.recipes = candidatesName;
                vote.votersEmail = votersEmail;
                FWebService.getInstance().insertVote(vote, new FCallback<FVote>() {
                    @Override
                    public void success(FVote object, Response response) {
                        super.success(object, response);
                        currentGroup.setText(Long.toString(object.key.id));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                    }
                });
            }
        });

        getCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> votes = new ArrayList<Integer>();
                votes.add(new Integer(0));
                votes.add(new Integer(1));
                votes.add(new Integer(0));
                votes.add(new Integer(0));
                votes.add(new Integer(0));
                votes.add(new Integer(1));

                FUserInfo userInfo = new FUserInfo();
                userInfo.firstName = "Simon";
                userInfo.lastName = "Belanger";
                userInfo.email = "simbeliric@gmail.com";

                FWebService.getInstance().updateGcmRegistrationId("simbeliric@gmail.com", "1232fg23", new FCallback<FUserInfo>() {
                    @Override
                    public void success(FUserInfo object, Response response) {
                        super.success(object, response);
                        currentGroup.setText(object.firstName);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                    }
                });
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frecipe_selection, menu);
        addRecipe = menu.findItem(R.id.action_add_recipe);
        addRecipe.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onAddRecipeMenuClicked();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        checkPlayServices();
        currentGroup.setText(message);
        recipesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onAddRecipeMenuClicked(){
        FFlowManager.getInstance().launchAddRecipeActivity(this);
    }

    @Override
    public void addRecipe(FRecipe recipe) {
        if(!candidates.contains(recipe)){
            candidates.add(recipe);
            candidatesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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
                currentGroup.append(msg + "\n");
            }


    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String regid) {
        FWebService.getInstance().updateGcmRegistrationId(FAppState.getInstance().getUserInfo().email, regid, new FCallback<FUserInfo>() {
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
}
