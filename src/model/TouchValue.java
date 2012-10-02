package model;

public class TouchValue {
	
	public static final int MAX = 1023;
	
	private int touchValue;
	
	public synchronized int getValue() {
		return touchValue;
	}

	public synchronized void addMeasurement(int touch) {
		touch = (int) (Math.floor(touch / 10d) * 10);
		if (touch != touchValue) {
			this.touchValue = touch;
			notifyAll();
		}
	}
}
