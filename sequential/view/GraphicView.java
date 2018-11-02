package view;

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

	// set width x height to default of 720 x 480
	public int FRAME_WIDTH = 720;
	public int FRAME_HEIGHT = 480;
	
	private int frameXIndex = 0;
	private int frameYIndex = 0;
	
	
	public GraphicView(Gravity universe, int width, int height) {
		this.universe = universe;
		this.FRAME_WIDTH = width;
		this.FRAME_HEIGHT = height;
		repaint();
	}
	
	public GraphicView(Gravity universe){
		this.universe = universe;
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
	}
	
	public void updateFramePosition(int x, int y){
		System.out.println(x + " " + y);
		this.frameXIndex += x;
		this.frameYIndex += y;
	}
	
	/*
	 * draw a circle of given radius and center on the frame
	 * 
	 * Input: 	Center of the Circle (x, y) in reference to the ultimate width and height
	 * 			Radius of the Circle r
	 * 
	 * fillOval needs a corner coordinate and two diameters.
	 * use the center of the circle, subtract the frameIndex to bring it to the frame,
	 * then subtract the radius of the circle to get to the exact location. do the same for y
	 * multiply the radius by 2 to make it the diameter, being the radius, d = 2*r
	 * 
	 */
	public void drawCenteredCircle(int x, int y, int r) {
		g2.fillOval(x-r - frameXIndex, y-r - frameYIndex, 2*r, 2*r);
		repaint();
	}
	
	private boolean withinBounds(int index){
		double radius = universe.getRadius(index);
		double x = universe.getXPosition(index);
		double y = universe.getYPosition(index);
		if((x + radius) < frameXIndex || (x - radius) > (FRAME_WIDTH+frameXIndex) || 
				(y + radius) < frameYIndex || (y - radius) > (FRAME_HEIGHT + frameYIndex))
			return false;
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {
		g2 = (Graphics2D) g;

		for(int i=0; i< universe.getCount(); i++){
			
			if(withinBounds(i))
				drawCenteredCircle((int)universe.getXPosition(i), (int)universe.getYPosition(i), 
									(int)universe.getRadius(i));
		}
		
	}
}