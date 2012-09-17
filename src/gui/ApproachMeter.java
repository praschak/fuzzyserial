package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.ApproachValue;

/**
 * A GUI part to show the "approach value".
 *
 * @author markus
 *
 */
@SuppressWarnings("serial")
public class ApproachMeter extends JPanel {
	
	private static final int WIDTH = 100;
	private static final int HEIGHT = 300;
	
	private ApproachValue approachValue;

	public ApproachMeter(ApproachValue approachValue) {
		super();
		this.approachValue = approachValue;
		
		setBackground(Color.WHITE);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int height = approachValue.getApproachValue() > HEIGHT ? HEIGHT : approachValue.getApproachValue();
		
		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT - height, getWidth(), height);
		
		g.setColor(Color.BLACK);
		g.drawString("Approach: " + height, 10, HEIGHT - 10);
	}

}
