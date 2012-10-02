package model;

public class TouchValue {
	
	public static final int MAX = 1023;
	
	private static final int LIMIT = 100;
	
	private int touchValue;
	private boolean touch;
	
	public synchronized int getTouchValue() {
		return touchValue;
	}

	public synchronized boolean isTouched() {
		return touch;
	}

	public synchronized void addMeasurement(int touch) {
		this.touchValue = touch;
		if (LIMIT <= touch && !this.touch) {
			this.touch = true;
		} else if (touch < LIMIT && this.touch) {
			this.touch = false;
		}
		notifyAll();
	}
}
