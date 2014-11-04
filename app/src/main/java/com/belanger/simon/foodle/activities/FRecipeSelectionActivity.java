package com.belanger.simon.foodle.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.FFlowManager;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.adapters.FSelectedRecipeListViewAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

@Layout(R.layout.f_activity_recipe_selection)
public class FRecipeSelectionActivity extends FActivity implements FRecipeListViewAdapter.OnAddRecipe,
        DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    @ViewOutlet(R.id.recipeSelectionRecipes) public ListView recipesListView;
    @ViewOutlet(R.id.recipeSelectionCandidateRecipes) public ListView candidateRecipes;
    @ViewOutlet(R.id.recipeSelectionSendCandidatesButton) public Button sendCandidatesButton;
    @ViewOutlet(R.id.recipeSelectionGetCandidatesButton) public Button getCandidatesButton;
    @ViewOutlet(R.id.recipeSelectionCurrentGroup) public TextView currentGroup;
    @ViewOutlet(R.id.recipeSelectionDatePicker) public DatePicker datePicker;
    @ViewOutlet(R.id.recipeSelectionTimePicker) public TimePicker timePicker;

    public final static long MILLIS_IN_AN_HOUR = 3600000;
    public final static long MILLIS_IN_A_MINUTE = 60000;

    Context context;

    String message = FFlowManager.getInstance().getRecipeSelectionMessage();

    private MenuItem addRecipe;
    private MenuItem votes;
    private MenuItem contacts;
    private FList<FRecipe> recipes = FAppState.getInstance().getCustomRecipes();
    private FList<FRecipe> candidates = new FList<FRecipe>();
    private Long dateInMillis = 0L;
    private Long timeInMillis = 0L;
    private Long voteEndTimeInMillis = 0L;
    private FSelectedRecipeListViewAdapter candidatesAdapter;
    private FRecipeListViewAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.recipe_selection));

        currentGroup.setText(message);

        context = getApplicationContext();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePicker(context);
        datePicker.init(year, month, day, this);

        timePicker = new TimePicker(context);
        timePicker.setOnTimeChangedListener(this);

        recipesAdapter = new FRecipeListViewAdapter(this, recipes);
        recipesAdapter.setOnAddRecipeListener(this);
        recipesListView.setAdapter(recipesAdapter);

        candidatesAdapter = new FSelectedRecipeListViewAdapter(this, candidates);
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
                dateInMillis = datePicker.getCalendarView().getDate();
                voteEndTimeInMillis = dateInMillis + timeInMillis;
                timeInMillis = timePicker.getCurrentHour() * MILLIS_IN_AN_HOUR + timePicker.getCurrentMinute() * MILLIS_IN_A_MINUTE;
                vote.endTimeInMillis = voteEndTimeInMillis;
                vote.voteCreatorInformation = FAppState.getInstance().getUserInfo();
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
        votes = menu.findItem(R.id.action_votes);
        votes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onVotesMenuClicked();
                return true;
            }
        });
        contacts = menu.findItem(R.id.action_contacts);
        contacts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContactsMenuClicked();
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

    public void onVotesMenuClicked(){
        FFlowManager.getInstance().launchPendingVotesActivity(this);
    }

    public void onContactsMenuClicked(){
        FFlowManager.getInstance().launchContactsActivity(this);
    }

    @Override
    public void addRecipe(FRecipe recipe) {
        if(!candidates.contains(recipe)){
            candidates.add(recipe);
            candidatesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Date chosenDate = new Date(year,monthOfYear,dayOfMonth);
        dateInMillis = chosenDate.getTime();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        timeInMillis = hourOfDay * MILLIS_IN_AN_HOUR + minute * MILLIS_IN_A_MINUTE;
    }
}
