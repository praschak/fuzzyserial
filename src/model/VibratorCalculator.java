package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VibratorCalculator implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(VibratorCalculator.class);
	
	private static final int
			LOWER_LIMIT = 100,
			HIGHER_LIMIT = 300;

	private final Spike spike;
	
	private final TouchValue touchValue;
	private final Vibrator vibrator;

	public VibratorCalculator(final Spike spike, final TouchValue touchValue, Vibrator vibrator) {
		this.spike = spike;
		this.touchValue = touchValue;
		this.vibrator = vibrator;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						synchronized (spike) {
							while (!spike.isSpiked()) {
								spike.wait();
							}
						}
						
						synchronized (VibratorCalculator.this) {
							VibratorCalculator.this.notifyAll();
						}
					}
				} catch (InterruptedException e) {
					// Ignore
				}
				log.debug("Interrupted, exiting.");
			}
			
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						synchronized (touchValue) {
							while(touchValue.getValue() < LOWER_LIMIT) {
								touchValue.wait();
							}
						}
						
						synchronized (VibratorCalculator.this) {
							VibratorCalculator.this.notifyAll();
						}
					}
				} catch (InterruptedException e) {
					// Ignore
				}
				log.debug("Interrupted, exiting.");
			}
			
		}).start();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					wait();
				}
				
				int value = touchValue.getValue();
				
				if (spike.isSpiked()) {
					vibrator.agitated();
				} else {
					if (LOWER_LIMIT <= value && value < HIGHER_LIMIT) {
						vibrator.purr();
					} else if (HIGHER_LIMIT <= value) {
						vibrator.agitated();
					}
				}
				
			}
		} catch (InterruptedException e) {
			// Ignore
		}
		
		log.debug("Interrupted, exiting.");
	}

}
