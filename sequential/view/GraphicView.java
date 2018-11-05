package view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import sequentialmodel.Gravity;

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

	private double magnification = 1;
	
	
	public GraphicView(Gravity universe, int width, int height, double magnification) {
		this.universe = universe;
		this.FRAME_WIDTH = width;
		this.FRAME_HEIGHT = height;
		this.magnification = magnification;

		repaint();
	}
	
	public GraphicView(Gravity universe){
		this.universe = universe;
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	// when the plane is magnified, use w,s,a,d to navigate between frames
	// this function does that
	public void updateFramePosition(int x, int y){
		if((frameXIndex == 0 && x < 0) || (frameXIndex == (((double)universe.WIDTH)/magnification)-FRAME_WIDTH && x > 0) ||
			(frameYIndex == 0 && y < 0) || (frameYIndex == (((double)universe.HEIGHT)/magnification)-FRAME_HEIGHT && y > 0))
			return;
		this.frameXIndex += x;
		this.frameYIndex += y;
	}
	
	// you really need comments to get this?
	// seriously you're reading this?
	// wow
	public void changeMagnification(double newMag){
		magnification = newMag;
		frameXIndex = 0;
		frameYIndex = 0;
	}

	public void changeSpeed(double factor){
		universe.updateSpeed(factor);
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
		g2.fillOval((int)(((double)(x-r))/((double)magnification)) - frameXIndex, 
					(int)(((double)(y-r))/((double)magnification)) - frameYIndex, 
					(int)((double)2*r/(double)magnification), 
					(int)((double)2*r/(double)magnification));
		repaint();
	}
	
	// checks if the object is within the bounds of the current frame
	private boolean withinBounds(int index){
		double radius = universe.getRadius(index)/magnification;
		double x = universe.getXPosition(index)/magnification;
		double y = universe.getYPosition(index)/magnification;
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