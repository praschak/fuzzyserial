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
	
	private Object sync = new Object();

	public ServoArduino(String portname, final Spike spike) {
		super(portname);
		this.spike = spike;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						synchronized (spike) {
							spike.wait();
						}
						
						synchronized (sync) {
							sync.notifyAll();
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
		connect();

		try {
			while (!Thread.interrupted()) {
				synchronized (sync) {
					sync.wait();
				}
				
				String spikeValue = String.format("%03d0", spike.getSpikeValue());
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
