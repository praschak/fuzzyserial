package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ApproachValue takes distance measurements and converts these to an "approach value".
 * 
 * @author markus
 *
 */
public class ApproachValue {
	
	private static final int QUEUE_LIMIT = 50;

	private Queue<Integer> queue = new LinkedList<Integer>();
	
	private int approachValue = 0;
	
	public synchronized int getApproachValue() {
		return approachValue;
	}
	
	public synchronized void addMeasurement(int distance) {
		// Add to end of queue
		queue.add(distance);
		
		if (queue.size() <= QUEUE_LIMIT) {
			return;
		}
		
		// Remove first element
		queue.poll();
		
		double sum = 0;
		for (int d : queue) {
			sum += d;
		}
		
		double mean = sum / QUEUE_LIMIT;
		
		double variance = 0;
		for (int d : queue) {
			variance += Math.pow(mean - d, 2);
		}
		variance /= QUEUE_LIMIT;
		
		double standardDeviation = Math.sqrt(variance);
		
		if (approachValue != (int) standardDeviation) {
			approachValue = (int) standardDeviation;
			notifyAll();
		}
	}
}
