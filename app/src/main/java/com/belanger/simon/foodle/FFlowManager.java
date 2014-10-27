package com.belanger.simon.foodle;

import android.app.Activity;
import android.content.Intent;

import com.belanger.simon.foodle.activities.FAddRecipeActivity;
import com.belanger.simon.foodle.activities.FRecipeSelectionActivity;

import java.util.Stack;

public class FFlowManager {
	private static FFlowManager	instance;
	private static Stack<Activity>	previousActivities	= new Stack<Activity>();

	private FFlowManager() {
	};

	public static FFlowManager getInstance() {
		if (instance == null) {
			instance = new FFlowManager();
		}
		return instance;
	}

	/*
	 * UI FLOW
	 */

	public void launchAddRecipeActivity(Activity from) {
		Intent intent = new Intent(from, FAddRecipeActivity.class);
		from.startActivity(intent);
		from.overridePendingTransition(R.anim.f_anim_enter_slideleft, R.anim.f_anim_exit_slideleft);
	}

	public void launchRecipeSelectionActivity(Activity from, String message) {
		Intent intent = new Intent(from, FRecipeSelectionActivity.class);
        paramRecipeSelectionMessage = message;
		from.startActivity(intent);
		from.overridePendingTransition(R.anim.f_anim_enter_slideleft, R.anim.f_anim_exit_slideleft);
	}

	/*
	 * PARAMS FOR ACTIVITIES
	 */

    private String paramRecipeSelectionMessage;

    public String getRecipeSelectionMessage(){
        return paramRecipeSelectionMessage;
    }

	/*
	 * Stack consistency
	 */

	public void removeParentActivity() {
		if (previousActivities.size() != 0) previousActivities.pop();
	}

	public void launchParentActivity(Activity from) {
		Class<?> parentActivityClass = FRecipeSelectionActivity.class;
        Activity parentActivity = null;
		if (!previousActivities.isEmpty()){
            parentActivity = previousActivities.pop();
            parentActivity.finish();
            parentActivityClass = parentActivity.getClass();
        }
		while (parentActivityClass.getSimpleName().equals(from.getClass().getSimpleName())
				&& previousActivities.size() != 0) {
            parentActivity = previousActivities.pop();
            parentActivity.finish();
			parentActivityClass = parentActivity.getClass();
		}

		Intent intent = new Intent(from, parentActivityClass);
		from.startActivity(intent);
        from.finish();
		from.overridePendingTransition(R.anim.f_anim_enter_slideleft, R.anim.f_anim_exit_slideleft);
	}
	
	public Class<?> getParentActivity(Activity currentActivity){
		if(!previousActivities.isEmpty()){
			Class<?> parentActivityClass = currentActivity.getClass();
			Stack<Activity> temp = new Stack<Activity>();
			while(!previousActivities.isEmpty()){
				Activity popped = previousActivities.pop();
				temp.push(popped);
				if(parentActivityClass != popped.getClass()){
					parentActivityClass = popped.getClass();
					break;
				}
			}
			while(!temp.isEmpty()){
				previousActivities.push(temp.pop());
			}
			return parentActivityClass != null ? parentActivityClass : FRecipeSelectionActivity.class;
		}else{
			return FRecipeSelectionActivity.class;
		}
	}
}