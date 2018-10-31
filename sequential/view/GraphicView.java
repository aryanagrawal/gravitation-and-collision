package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Gravity;

public class GraphicView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Gravity universe;
	private Graphics2D g2;

	public GraphicView(Gravity universe) {
		this.universe = universe;
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
	}
	
	public void drawCenteredCircle(int x, int y, int r, double mass) {
		g2.fillOval(x-r,y-r,2*r,2*r);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g2 = (Graphics2D) g;
		
		for(int i=0; i< universe.getCount(); i++){
			drawCenteredCircle((int)universe.getXPosition(i), (int)universe.getYPosition(i), 
					(int)universe.getRadius(i), universe.getMass(i));
		}
		
	}
}