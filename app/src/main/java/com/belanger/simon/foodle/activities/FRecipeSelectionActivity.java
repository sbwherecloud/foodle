package com.belanger.simon.foodle.activities;

import android.content.Context;
import android.os.Bundle;
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
import com.belanger.simon.foodle.views.FUserInfoDialog;
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

    Context context;

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

        FUserInfo userInfo = FAppState.getInstance().getUserInfo();

        //Obligatory registration
        if(userInfo == null || (!userInfo.isValid())){
            FUserInfoDialog registrationDialog = new FUserInfoDialog(this);
            registrationDialog.show();
        }

        currentGroup.setText(message);

        context = getApplicationContext();

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
                        FAppState.getInstance().addToPendingVotes(object.key.id);
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
}
