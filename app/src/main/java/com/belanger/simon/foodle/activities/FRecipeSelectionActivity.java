package com.belanger.simon.foodle.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.FFlowManager;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.adapters.FRecipeListViewAdapter;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FRecipe;

@Layout(R.layout.f_activity_recipe_selection)
public class FRecipeSelectionActivity extends FActivity implements FRecipeListViewAdapter.OnAddRecipe {

    @ViewOutlet(R.id.recipeSelectionRecipes) public ListView recipesListView;
    @ViewOutlet(R.id.recipeSelectionCandidateRecipes) public ListView candidateRecipes;

    private MenuItem addRecipe;
    private FList<FRecipe> recipes = FAppState.getInstance().getCustomRecipes();
    private FList<FRecipe> candidates = new FList<FRecipe>();
    private FRecipeListViewAdapter candidatesAdapter;
    private FRecipeListViewAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.recipe_selection));

        recipesAdapter = new FRecipeListViewAdapter(this, recipes);
        recipesAdapter.setOnAddRecipeListener(this);
        recipesListView.setAdapter(recipesAdapter);

        candidatesAdapter = new FRecipeListViewAdapter(this, candidates);
        candidateRecipes.setAdapter(candidatesAdapter);
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
        candidates.add(recipe);
        candidatesAdapter.notifyDataSetChanged();
    }
}
