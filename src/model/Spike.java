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
			SLEEP_TIME = 40;
	
	private static final int
			MAX = 100,
			MIN = 0,
			STEP = 1;
	
	
	private int spikeValue = MIN;
	private int x = MIN;

	private long lastSpikeTime = 0;
	
	public synchronized int getSpikeValue() {
		return spikeValue;
	}
	
	public synchronized void spike() {
		x = MAX;
		lastSpikeTime = System.currentTimeMillis();
		calculateSpikeValue();
	}
	
	private synchronized void calculateSpikeValue() {
		int newSpikeValue = scaleValue(x);
		if (newSpikeValue != spikeValue) {
			spikeValue = newSpikeValue;
			notifyAll();
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			if (x > MIN && System.currentTimeMillis() - lastSpikeTime > SPIKE_OUT_TIME) {
				x -= STEP;
				calculateSpikeValue();
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private int scaleValue(int linearValue) {
		// 50*(tanh(x/25-2)+1)
		return (int) (50 * ( Math.tanh(linearValue / 25d - 2) + 1) );
	}

}
