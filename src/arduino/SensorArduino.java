package arduino;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorArduino extends Arduino implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SensorArduino.class);

	public SensorArduino(String portname) {
		super(portname);
	}

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {
				String line = reader.readLine();
				log.trace("Read: " + line);
			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		}
		log.debug("Interrupted, exiting.");
		disconnect();
	}

}
