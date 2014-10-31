package com.belanger.simon.foodle.adapters;

/**
 * Created by SimonPro on 14-10-30.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FVote;

import java.util.Date;

/**
 * Created by SimonPro on 2014-08-16.
 */
public class FPendingVotesListViewAdapter extends BaseAdapter implements ListAdapter {

    private final FList<FVote> pendingVotes;
    private final LayoutInflater inflater;

    public FPendingVotesListViewAdapter(Context context, FList<FVote> pendingVotes) {
        this.pendingVotes = pendingVotes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FVote vote = pendingVotes.get(position);
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.f_view_pending_votes_listitem, parent, false);
            holder = Holder.fromView(convertView);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        updateView(position, vote, holder);

        return convertView;
    }

    private static class Holder {

        TextView pendingVoteCreatorName;
        TextView timeRemaining;

        public static Holder fromView(View view) {
            Holder holder = new Holder();

            holder.pendingVoteCreatorName = (TextView) view.findViewById(R.id.pendingVoteCreatorName);
            holder.timeRemaining = (TextView) view.findViewById(R.id.pendingVoteRemainingTime);

            return holder;
        }
    }

    // TODO: only update visible views
    private void updateView(final int position, final FVote vote, final Holder holder) {

        if(vote == null)
            return;

        holder.pendingVoteCreatorName.setText(vote.voteCreatorInformation.firstName + " " +
            vote.voteCreatorInformation.lastName);
        Date date = new Date(vote.endTimeInMillis);
        holder.timeRemaining.setText(date.toString());
    }

    @Override
    public int getCount() {
        return pendingVotes.size();
    }

    @Override
    public Object getItem(int position) {
        return pendingVotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

