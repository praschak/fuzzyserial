package arduino;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Arduino {
	
	private static final Logger log = LoggerFactory.getLogger(Arduino.class);
	
	private static final int BAUD = 115200;
	private static final int TIMEOUT = 2000;
	
	private SerialPort serialPort;
	protected BufferedReader reader;
	protected BufferedWriter writer;

	private String portname;
	
	public Arduino(String portname) {
		this.portname = portname;
	}

	protected void connect() {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portname);
			CommPort commPort = portIdentifier.open(getClass().getName(), TIMEOUT);
			
			log.debug("Opening port");
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("Interrupted!", e);
			}
			
			log.info("Ready!");
			
		} catch (NoSuchPortException e) {
			log.error("No such serial port.", e);
		} catch (PortInUseException e) {
			log.error("Serial port in use.", e);
		} catch (UnsupportedCommOperationException e) {
			log.error("UnsupportedCommOperation.", e);
		} catch (IOException e) {
			log.error("Couldn't get input- and/or outputstream to/from Arduino.", e);
		}
	}
	
	protected void disconnect() {
		serialPort.close();
		log.info("Disconnected.");
	}
}
