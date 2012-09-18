package arduino;

import java.io.IOException;

import model.Spike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServoArduino reads values from a datatype when these are available, and prints these to the serial port.
 * 
 * @author markus
 *
 */
public class ServoArduino extends Arduino implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ServoArduino.class);

	private Spike spike;

	public ServoArduino(String portname, Spike spike) {
		super(portname);
		this.spike = spike;
	}

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {
				synchronized (spike) {
					spike.wait();
				}
				
				String spikeValue = String.format("%03d", spike.getSpikeValue());
				log.debug("Sending " + spikeValue);
				writer.write(spikeValue);
				writer.flush();
			}
		} catch (InterruptedException e) {
			// Ignore
		} catch (IOException e) {
			log.error("Lost connection", e);
		}
		
		log.debug("Interrupted, exiting.");
		disconnect();
	}

}
