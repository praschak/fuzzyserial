package arduino;

import java.io.IOException;

import model.ApproachValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SensorArduino reads distances from the Arduino with the Ping sensor, and puts these values in a datatype.
 * 
 * @author markus
 *
 */
public class SensorArduino extends Arduino implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(SensorArduino.class);

	private final ApproachValue approachValue;

	public SensorArduino(String portname, ApproachValue approachValue) {
		super(portname);
		this.approachValue = approachValue;
	}

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {
				String line = reader.readLine();
				log.trace("Read: " + line);
				
				try {
					int distance = Integer.parseInt(line);
					approachValue.addMeasurement(distance);
				} catch (NumberFormatException e) {
					log.warn("Number format exception, ignoring.");
				}
			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		}
		
		log.debug("Interrupted, exiting.");
		disconnect();
	}

}
