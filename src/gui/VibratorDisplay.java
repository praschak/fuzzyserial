package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Vibrator;

@SuppressWarnings("serial")
public class VibratorDisplay extends JPanel {
	
	private static final int WIDTH = 200;
	private static final int HEIGHT = 300;
	
	private final Vibrator vibrator;
	
	public VibratorDisplay(Vibrator vibrator) {
		super();
		this.vibrator = vibrator;
		
		setBackground(Color.WHITE);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		String value = vibrator.getState().name();
		
		g.setColor(Color.BLACK);
		g.drawString("Vibrator: " + value, 10, getHeight() - 10);
	}

}
