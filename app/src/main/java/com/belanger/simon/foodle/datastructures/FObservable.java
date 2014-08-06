package com.belanger.simon.foodle.datastructures;

import java.util.Observable;

public class FObservable extends Observable {

	private boolean	silent	= false;

	/***
	 * Silences this observable, preventing it from notifying observers.
	 */
	public void silence() {
		silent = true;
	}

	/***
	 * Removes silence spell from this observable and notifies observers.
	 */
	public void unsilence() {
		silent = false;
		setChanged();
		notifyObservers();
	}
	
	public void silentlyUnsilence(){
		silent = false;
	}

	@Override
	public void notifyObservers() {
		if (!silent) {
			super.notifyObservers();
		}
	}

	@Override
	public void notifyObservers(Object data) {
		if (!silent) {
			super.notifyObservers(data);
		}
	}
}