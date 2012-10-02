package main;

import gui.SpikeMeter;
import gui.ApproachMeter;
import gui.TouchMeter;
import gui.VibratorDisplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.ApproachValue;
import model.Spike;
import model.SpikeCalculator;
import model.TouchValue;
import model.Vibrator;
import model.VibratorCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arduino.SensorArduino;
import arduino.ServoArduino;

/**
 * Class for bootstrapping.
 * 
 * @author markus
 *
 */
public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		log.info("Starting up...");
		
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final ApproachValue approachValue = new ApproachValue();
		final TouchValue touchValue = new TouchValue();
		final Spike spike = new Spike();
		final Vibrator vibrator = new Vibrator();
		
		SpikeCalculator spikeCalculator = new SpikeCalculator(approachValue, spike);
		VibratorCalculator vibratorCalculator = new VibratorCalculator(spike, touchValue, vibrator);
		
		SensorArduino sensorArduino = new SensorArduino("/dev/tty.usbmodemfa131", approachValue, touchValue);
		ServoArduino servoArduino = new ServoArduino("/dev/tty.usbmodemfd121", spike, vibrator);
		
		threadPool.execute(spike);
		threadPool.execute(vibrator);
		threadPool.execute(sensorArduino);
		threadPool.execute(servoArduino);
		threadPool.execute(spikeCalculator);
		threadPool.execute(vibratorCalculator);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Create the GUI
				final JFrame frame = new JFrame("FuzzySerial");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
				
				final ApproachMeter approachMeter = new ApproachMeter(approachValue);
				final TouchMeter touchMeter = new TouchMeter(touchValue);
				final SpikeMeter spikeMeter = new SpikeMeter(spike);
				final VibratorDisplay vibratorDisplay = new VibratorDisplay(vibrator);
				frame.getContentPane().add(approachMeter);
				frame.getContentPane().add(touchMeter);
				frame.getContentPane().add(spikeMeter);
				frame.getContentPane().add(vibratorDisplay);
				
				frame.pack();
				frame.setVisible(true);
				
				// Create threads to repaint the GUI
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							while (!Thread.interrupted()) {
								synchronized (approachValue) {
									approachValue.wait();
								}
								approachMeter.repaint();
							}
						} catch (InterruptedException e) {
							// Ignore
						}
						log.debug("Interrupted, exiting.");
					}
				});
				
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							while (!Thread.interrupted()) {
								synchronized (touchValue) {
									touchValue.wait();
								}
								touchMeter.repaint();
							}
						} catch (InterruptedException e) {
							// Ignore
						}
						log.debug("Interrupted, exiting.");
					}
				});

				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							while (!Thread.interrupted()) {
								synchronized (spike) {
										spike.wait();
								}
								spikeMeter.repaint();
							}
						} catch (InterruptedException e) {
							// Ignore
						}
						log.debug("Interrupted, exiting.");
					}
				});
				
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							while (!Thread.interrupted()) {
								synchronized (vibrator) {
									vibrator.wait();
								}
								vibratorDisplay.repaint();
							}
						} catch (InterruptedException e) {
							// Ignore
						}
						log.debug("Interrupted, exiting.");
					}
				});
			}
			
		});
		
		// Wait for newline, then interrupt threads and exit
		log.info("Press enter to exit program.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		
		threadPool.shutdownNow();
		
		// Wait for everything to clean up before exiting
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Interrupted.", e);
		}
		
		System.exit(0);
	}

}
