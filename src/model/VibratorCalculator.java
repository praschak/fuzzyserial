package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VibratorCalculator implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(VibratorCalculator.class);
	
	private static final int
			LOWER_LIMIT = 100,
			HIGHER_LIMIT = 300;

	private final TouchValue touchValue;
	private final Vibrator vibrator;

	public VibratorCalculator(TouchValue touchValue, Vibrator vibrator) {
		this.touchValue = touchValue;
		this.vibrator = vibrator;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				int value;
				synchronized (touchValue) {
					while(touchValue.getValue() < LOWER_LIMIT) {
						touchValue.wait();
					}
					
					value = touchValue.getValue();
				}
				
				if (LOWER_LIMIT <= value && value < HIGHER_LIMIT) {
					vibrator.purr();
				} else if (HIGHER_LIMIT <= value) {
					vibrator.agitated();
				}
			}
		} catch (InterruptedException e) {
			// Ignore
		}
		
		log.debug("Interrupted, exiting.");
	}

}
