package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Spike;

/**
 * A GUI part to show the spike.
 * 
 * @author markus
 *
 */
@SuppressWarnings("serial")
public class SpikeMeter extends JPanel {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 300;
	
	private final Spike spike;
	
	public SpikeMeter(Spike spike) {
		super();
		this.spike = spike;
		
		setBackground(Color.WHITE);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int height = HEIGHT - spike.getSpikeValue();
		
		g.setColor(Color.RED);
		g.fillPolygon(new int[] {0, WIDTH / 2, WIDTH}, new int[] {HEIGHT, height, HEIGHT}, 3);
		
		g.setColor(Color.BLACK);
		g.drawString("Spike: " + (HEIGHT - height), 10, HEIGHT - 10);
	}
}
