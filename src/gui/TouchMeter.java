package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.TouchValue;

/**
 * A GUI part to show the "approach value".
 *
 * @author markus
 *
 */
@SuppressWarnings("serial")
public class TouchMeter extends JPanel {
	
	private static final int WIDTH = 100;
	private static final int HEIGHT = 300;
	
	private TouchValue touchValue;

	public TouchMeter(TouchValue touchValue) {
		super();
		this.touchValue = touchValue;
		
		setBackground(Color.WHITE);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int value = touchValue.getTouchValue();
		
		int height = getHeight() * value / TouchValue.MAX;
		
		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT - height, getWidth(), height);
		
		g.setColor(Color.BLACK);
		g.drawString("Touch: " + value, 10, getHeight() - 10);
	}

}
