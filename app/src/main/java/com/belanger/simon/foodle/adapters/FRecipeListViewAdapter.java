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

public class FRecipeListViewAdapter extends BaseAdapter implements ListAdapter {

    private final FList<FRecipe> recipes;
    private final LayoutInflater inflater;
    private OnAddRecipe listener;

    public FRecipeListViewAdapter(Context context, FList<FRecipe> recipes) {
        this.recipes = recipes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FRecipe recipe = recipes.get(position);
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
        holder.addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.addRecipe(recipe);
            }
        });
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnAddRecipeListener(OnAddRecipe l){
        this.listener = l;
    }

    public interface OnAddRecipe{
        public void addRecipe(FRecipe recipe);
    }
}
