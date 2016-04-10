package com.csanon.server;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Lock {

	private static boolean LOCKED = false;
	private static Timer timer = new Timer();

	public void lock(Consumer<String> callback) {
		if (!LOCKED) {
			LOCKED = true;
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					callback.accept("Server Lock Has Timed Out");
					LOCKED = false;
				}
			}, 120000);
		}
	}

	public void unlock() {
		if (LOCKED) {
			timer.cancel();
		}
	}

	public boolean isLocked() {
		return LOCKED;
	}
}
