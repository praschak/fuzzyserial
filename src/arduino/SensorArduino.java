package arduino;

import java.io.IOException;

import model.ApproachValue;
import model.TouchValue;

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

	private final TouchValue touchValue;

	public SensorArduino(String portname, ApproachValue approachValue, TouchValue touchValue) {
		super(portname);
		this.approachValue = approachValue;
		this.touchValue = touchValue;
	}

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {
				String line = reader.readLine();
				log.trace("Read: " + line);
				String[] split = line.split(",");
				if (split.length < 2) {
					continue;
				}
				
				try {
					int distance = Integer.parseInt(split[0]);
					int touch =  Integer.parseInt(split[1]);
					
					approachValue.addMeasurement(distance);
					touchValue.addMeasurement(touch);
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
