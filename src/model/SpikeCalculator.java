package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SpikeCalculator is responsible for sending the spike out, based on the "approach value".
 * @author markus
 *
 */
public class SpikeCalculator implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(SpikeCalculator.class);
	
	private static final int LIMIT = 10;

	private final ApproachValue approachValue;
	private final Spike spike;

	public SpikeCalculator(ApproachValue approachValue, Spike spike) {
		this.approachValue = approachValue;
		this.spike = spike;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (approachValue) {
					while(approachValue.getApproachValue() < LIMIT) {
						approachValue.wait();
					}
				}
				
				// Do spiking every time we are over limit, continuously
				spike.spike();
			}
		} catch (InterruptedException e) {
			// Ignore
		}
		
		log.debug("Interrupted, exiting.");
	}

}
