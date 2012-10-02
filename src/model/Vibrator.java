package model;

public class Vibrator implements Runnable {
	
	private static final long
			ACTIVITY_TIMEOUT = 4000,
			SLEEP_TIME = 100;
	
	private long lastActivityTime = 0;
	
	private VibratorState state = VibratorState.HEARTBEAT_LOW;

	public synchronized VibratorState getState() {
		return state;
	}
	
	public synchronized void purr() {
		if (state != VibratorState.HEARTBEAT_HIGH) {
			lastActivityTime = System.currentTimeMillis();
			setState(VibratorState.PURRING);
		}
	}

	public synchronized void agitated() {
		lastActivityTime = System.currentTimeMillis();
		setState(VibratorState.HEARTBEAT_HIGH);
	}

	private synchronized void setState(VibratorState state) {
		if (state != this.state) {
			this.state = state;
			notifyAll();
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			synchronized (this) {
				if (state != VibratorState.HEARTBEAT_LOW && System.currentTimeMillis() - lastActivityTime > ACTIVITY_TIMEOUT) {
					if (state == VibratorState.HEARTBEAT_HIGH) {
						lastActivityTime = System.currentTimeMillis();
						setState(VibratorState.PURRING);
					} else if (state == VibratorState.PURRING) {
						setState(VibratorState.HEARTBEAT_LOW);
					}
				}
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	}
	
	public enum VibratorState {
		OFF(0), PURRING(1), HEARTBEAT_LOW(2), HEARTBEAT_HIGH(3);
		
		private int mapping;

		VibratorState(int mapping) {
			this.mapping = mapping;
		}
		
		public int getMappedValue() {
			return mapping;
		}
	}

}
