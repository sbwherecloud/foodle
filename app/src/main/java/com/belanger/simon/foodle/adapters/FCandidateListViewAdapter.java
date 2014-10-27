package com.belanger.simon.foodle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FRecipe;

/**
 * Created by SimonPro on 2014-08-16.
 */
public class FCandidateListViewAdapter extends BaseAdapter implements ListAdapter {

    private final FList<FRecipe> candidates;
    private final LayoutInflater inflater;

    public FCandidateListViewAdapter(Context context, FList<FRecipe> candidates) {
        this.candidates = candidates;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FRecipe recipe = candidates.get(position);
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.f_view_recipe_listitem, parent, false);
            holder = Holder.fromView(convertView);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        updateView(position, recipe, holder);

        return convertView;
    }

    private static class Holder {

        TextView recipeName;
        Button addRecipe;

        public static Holder fromView(View view) {
            Holder holder = new Holder();

            holder.recipeName = (TextView) view.findViewById(R.id.recipeSelectionRecipeName);
            holder.addRecipe = (Button) view.findViewById(R.id.recipeSelectionAddRecipe);

            return holder;
        }
    }

    // TODO: only update visible views
    private void updateView(final int position, final FRecipe recipe, final Holder holder) {

        if(recipe == null)
            return;

        holder.recipeName.setText(recipe.recipeName);
    }

    @Override
    public int getCount() {
        return candidates.size();
    }

    @Override
    public Object getItem(int position) {
        return candidates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
