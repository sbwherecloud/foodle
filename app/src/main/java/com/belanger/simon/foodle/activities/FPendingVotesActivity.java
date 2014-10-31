package com.belanger.simon.foodle.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.FFlowManager;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.adapters.FPendingVotesListViewAdapter;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FVote;
import com.belanger.simon.foodle.network.FCallback;
import com.belanger.simon.foodle.network.FWebService;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SimonPro on 14-10-30.
 */
@Layout(R.layout.f_activity_pending_votes)
public class FPendingVotesActivity extends FActivity{

    @ViewOutlet(R.id.pendingVotesListView) public ListView pendingVotesListView;
    @ViewOutlet(R.id.pendingVotesSkipButton) public Button skipButton;

    private FList<FVote> pendingVotesList = new FList<FVote>();

    private FPendingVotesListViewAdapter pendingVotesListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.pending_votes));

        if(FAppState.getInstance().getPendingVotesList().isEmpty()){
            FFlowManager.getInstance().launchRecipeSelectionActivity(this, null);
        }

        for(Long pendingVoteId : FAppState.getInstance().getPendingVotesList()){
            FWebService.getInstance().getVote(pendingVoteId, new FCallback<FVote>(){
                @Override
                public void success(FVote object, Response response) {
                    super.success(object, response);
                    pendingVotesList.add(object);
                }

                @Override
                public void failure(RetrofitError error) {
                    super.failure(error);
                }
            });
        }

        pendingVotesListViewAdapter = new FPendingVotesListViewAdapter(this, pendingVotesList);
        pendingVotesListView.setAdapter(pendingVotesListViewAdapter);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FFlowManager.getInstance().launchRecipeSelectionActivity(FPendingVotesActivity.this, null);
            }
        });
    }
}
