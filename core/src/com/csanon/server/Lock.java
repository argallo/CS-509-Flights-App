package com.csanon.server;

import java.util.function.Consumer;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Lock {

	private static boolean LOCKED = false;
	private static Timer timer = new Timer();

	public void lock(Consumer<String> callback) {
		if (!LOCKED) {
			LOCKED = true;
			timer.scheduleTask(new Task() {

				@Override
				public void run() {
					callback.accept("Server Lock Has Timed Out");
					LOCKED = false;
				}
			}, 120.0f);
		}
	}

	public void unlock() {
		if (LOCKED) {
			timer.clear();
			
		}
	}
	
	public boolean isLocked(){
		return LOCKED;
	}
}
