package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import model.Gravity;
import view.GraphicView;

public class Controller extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
	private Gravity universe;
	private MyListener actionList;
	private GraphicView view;

	public Controller() {
		setSize(WIDTH, HEIGHT);
		universe = new Gravity(10, WIDTH, HEIGHT);
		view = new GraphicView(universe);
		actionList = new MyListener();
		view.addMouseListener(actionList);
		setLocation(50, 30);
		setTitle("Particles");
		add(view);
	}

	public static void main(String[] args) {
		Controller universeController = new Controller();
		universeController.setVisible(true);
		while(true) {
		    universeController.view.repaint();
		    universeController.universe.updateDynamics();
		}
	}

	private class MyListener implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}
}
