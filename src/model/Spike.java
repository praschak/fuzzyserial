package model;

/**
 * This active datatype models a spike. It can go out quickly, but then goes back after a while.
 * 
 * @author markus
 *
 */
public class Spike implements Runnable {
	
	private static final long
			SPIKE_OUT_TIME = 3000,
			SLEEP_TIME = 20;
	
	private static final int
			MAX = 100,
			MIN = 0,
			STEP = 1;
	
	
	private int spikeValue = MIN;

	private long lastSpikeTime = 0;
	
	public synchronized int getSpikeValue() {
		return spikeValue;
	}
	
	public synchronized void spike() {
		setSpikeValue(MAX);
		lastSpikeTime = System.currentTimeMillis();
	}
	
	private synchronized void setSpikeValue(int spikeValue) {
		if (spikeValue != this.spikeValue) {
			this.spikeValue = spikeValue > MAX ? MAX : spikeValue;
			notifyAll();
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			if (spikeValue > MIN && System.currentTimeMillis() - lastSpikeTime > SPIKE_OUT_TIME) {
				setSpikeValue(spikeValue - STEP);
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
	}

}
