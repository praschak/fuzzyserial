package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arduino.SensorArduino;

/**
 * Class for bootstrapping.
 * @author markus
 *
 */
public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		log.info("Starting up...");
		
		SensorArduino sensorArduino = new SensorArduino("/dev/tty.usbmodemfd121");
		Thread sensorThread = new Thread(sensorArduino);
		sensorThread.start();

		
		// Wait for newline, then interrupt threads and exit
		log.info("Press enter to exit program.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		
		sensorThread.interrupt();
		
		// Wait for everything to clean up before exiting
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Interrupted.", e);
		}
		
		System.exit(0);
	}

}
