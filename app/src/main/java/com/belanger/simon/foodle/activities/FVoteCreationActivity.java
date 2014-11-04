package com.belanger.simon.foodle.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.FFlowManager;
import com.belanger.simon.foodle.R;

import com.belanger.simon.foodle.adapters.FSelectedRecipeListViewAdapter;
import com.belanger.simon.foodle.adapters.FContactListViewAdapter;
import com.belanger.simon.foodle.adapters.FSelectedContactListViewAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SimonPro on 14-11-03.
 */
@Layout(R.layout.f_activity_votecreation)
public class FVoteCreationActivity extends FActivity implements ActionBar.TabListener, FRecipeListViewAdapter.OnAddRecipe,
        FContactListViewAdapter.OnAddContact {

    @ViewOutlet(R.id.tabFragmentContainer)                   public ViewGroup  voteCreationActivityContentView;
    @ViewOutlet(R.id.voteCreationCreateVoteButton)           public Button	   createVoteButton;
    @ViewOutlet(R.id.voteCreationRecipesListView)            public ListView   recipesListView;
    @ViewOutlet(R.id.voteCreationCandidatesListView)         public ListView   selectedRecipesListView;
    @ViewOutlet(R.id.voteCreationContactsListView)           public ListView   contactsListView;
    @ViewOutlet(R.id.voteCreationSelectedContactsListView)   public ListView   selectedContactsListView;
    @ViewOutlet(R.id.voteCreationDatePicker)                 public DatePicker datePicker;
    @ViewOutlet(R.id.voteCreationTimePicker)                 public TimePicker timePicker;

    public final static long MILLIS_IN_AN_HOUR = 3600000;
    public final static long MILLIS_IN_A_MINUTE = 60000;

    private ActionBar.Tab tabRecipe;
    private ActionBar.Tab tabContacts;
    private ActionBar.Tab tabEndTime;

    private FSelectedRecipeListViewAdapter selectedRecipeListViewAdapter;
    private FRecipeListViewAdapter recipeListViewAdapter;
    private FContactListViewAdapter contactListViewAdapter;
    private FSelectedContactListViewAdapter selectedContactListViewAdapter;

    private FList<FRecipe> recipeList = FAppState.getInstance().getCustomRecipes();
    private FList<FRecipe> selectedRecipeList = new FList<FRecipe>();
    private FList<FUserInfo> contactList = FAppState.getInstance().getFriendsList();
    private FList<FUserInfo> selectedContactList = new FList<FUserInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FUserInfo userInfo = FAppState.getInstance().getUserInfo();

        //Obligatory registration
        if(userInfo == null || (!userInfo.isValid())){
            FUserInfoDialog registrationDialog = new FUserInfoDialog(this);
            registrationDialog.show();
        }

        ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        tabRecipe = actionBar.newTab().setText(getString(R.string.recipes)).setTabListener(this);
        actionBar.addTab(tabRecipe);
        tabContacts = actionBar.newTab().setText(getString(R.string.contacts)).setTabListener(this);
        actionBar.addTab(tabContacts);
        tabEndTime = actionBar.newTab().setText(getString(R.string.vote_end_time)).setTabListener(this);
        actionBar.addTab(tabEndTime);

        recipeListViewAdapter = new FRecipeListViewAdapter(this, recipeList);
        recipeListViewAdapter.setOnAddRecipeListener(this);
        recipesListView.setAdapter(recipeListViewAdapter);

        selectedRecipeListViewAdapter = new FSelectedRecipeListViewAdapter(this, selectedRecipeList);
        selectedRecipesListView.setAdapter(selectedRecipeListViewAdapter);

        contactListViewAdapter = new FContactListViewAdapter(this, contactList);
        contactListViewAdapter.setOnAddContactListener(this);
        contactsListView.setAdapter(contactListViewAdapter);

        selectedContactListViewAdapter = new FSelectedContactListViewAdapter(this, selectedContactList);
        selectedContactsListView.setAdapter(selectedContactListViewAdapter);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePicker(getApplicationContext());
        datePicker.init(year, month, day, null);

        timePicker = new TimePicker(getApplicationContext());

        createVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedContactList.isEmpty() || selectedRecipeList.isEmpty())
                    return;
                List<String> candidatesName = new ArrayList<String>();
                for(FRecipe recipe : selectedRecipeList){
                    candidatesName.add(recipe.recipeName);
                }

                List<String> votersEmail = new ArrayList<String>();
                for(FUserInfo contact : selectedContactList){
                    votersEmail.add(contact.email);
                }

                Long dateInMillis = datePicker.getCalendarView().getDate();
                Long timeInMillis = timePicker.getCurrentHour() * MILLIS_IN_AN_HOUR +
                        timePicker.getCurrentMinute() * MILLIS_IN_A_MINUTE;

                FVoteRequest vote = new FVoteRequest();
                vote.recipes = candidatesName;
                vote.votersEmail = votersEmail;
                vote.endTimeInMillis = dateInMillis + timeInMillis;
                vote.voteCreatorInformation = FAppState.getInstance().getUserInfo();
                FWebService.getInstance().insertVote(vote, new FCallback<FVote>() {
                    @Override
                    public void success(FVote object, Response response) {
                        super.success(object, response);
                        FAppState.getInstance().addToPendingVotes(object.key.id);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.f_menu_votecreation, menu);

        MenuItem addRecipe = menu.findItem(R.id.action_add_recipe);
        MenuItem goToContacts = menu.findItem(R.id.action_contacts);
        MenuItem goToPendingVotes = menu.findItem(R.id.action_votes);

        addRecipe.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onAddRecipeMenuClicked();
                return true;
            }
        });

        goToPendingVotes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onVotesMenuClicked();
                return true;
            }
        });

        goToContacts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContactsMenuClicked();
                return true;
            }
        });
        return true;
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

	/*
	 * TAB MANAGMENT
	 */

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabSelected(final ActionBar.Tab tab, FragmentTransaction ft) {
        if (tab == tabRecipe) {
            voteCreationActivityContentView.findViewById(R.id.recipeSelectionTabView).setVisibility(View.VISIBLE);
        } else if (tab == tabContacts) {
            voteCreationActivityContentView.findViewById(R.id.contactSelectionTabView).setVisibility(View.VISIBLE);
        } else if (tab == tabEndTime) {
            voteCreationActivityContentView.findViewById(R.id.voteEndTimeTabView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabUnselected(final ActionBar.Tab tab, FragmentTransaction ft) {
        if (tab == tabRecipe) {
            voteCreationActivityContentView.findViewById(R.id.recipeSelectionTabView).setVisibility(View.GONE);
        } else if (tab == tabContacts) {
            voteCreationActivityContentView.findViewById(R.id.contactSelectionTabView).setVisibility(View.GONE);
        } else if (tab == tabEndTime) {
            voteCreationActivityContentView.findViewById(R.id.voteEndTimeTabView).setVisibility(View.GONE);
        }
    }

    @Override
    public void addContact(FUserInfo contact) {
        if(!selectedContactList.contains(contact)){
            selectedContactList.add(contact);
            selectedContactListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addRecipe(FRecipe recipe) {
        if(!selectedRecipeList.contains(recipe)){
            selectedRecipeList.add(recipe);
            selectedRecipeListViewAdapter.notifyDataSetChanged();
        }
    }
}