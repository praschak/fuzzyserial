package arduino;

import java.io.IOException;

import model.Spike;
import model.Vibrator;

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

	private final Spike spike;
	private final Vibrator vibrator;
	
	public ServoArduino(String portname, final Spike spike, final Vibrator vibrator) {
		super(portname);
		this.spike = spike;
		this.vibrator = vibrator;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!Thread.interrupted()) {
						synchronized (spike) {
							spike.wait();
						}
						
						synchronized (ServoArduino.this) {
							ServoArduino.this.notifyAll();
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
						synchronized (vibrator) {
							vibrator.wait();
						}
						
						synchronized (ServoArduino.this) {
							ServoArduino.this.notifyAll();
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
				synchronized (this) {
					wait();
				}
				
				String sendValue = String.format("%03d%d", spike.getValue(), vibrator.getState().getMappedValue());
				log.debug("Sending " + sendValue);
				writer.write(sendValue);
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
