package com.belanger.simon.foodle.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;
import com.belanger.simon.foodle.models.FRecipe;

/**
 * Created by SimonPro on 2014-08-12.
 */
@Layout(R.layout.f_activity_addrecipe)
public class FAddRecipeActivity extends FActivity {

    @ViewOutlet(R.id.addRecipeConfirm) Button addRecipe;
    @ViewOutlet(R.id.addRecipeName) EditText recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.action_add_recipe));

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAppState.getInstance().addToCustomRecipes(new FRecipe(recipeName.getText().toString()));
                recipeName.getText().clear();
            }
        });
    }
}
