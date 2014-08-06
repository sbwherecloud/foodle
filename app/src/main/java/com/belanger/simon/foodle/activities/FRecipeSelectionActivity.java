package com.belanger.simon.foodle.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.annotations.Layout;

@Layout(R.layout.f_activity_recipe_selection)
public class FRecipeSelectionActivity extends FActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_recipe_selection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frecipe_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
