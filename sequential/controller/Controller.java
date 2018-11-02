package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	
	
	
	
	/*
	 * So the object fly up on the universal width by height,
	 * but we only show the frame.
	 * the magnification is calculated as (FRAME_WIDTH/UNIVERSAL_WIDTH)*100
	 * 
	 * 
	 */
	
	public static final int FRAME_WIDTH = 720;
	public static final int FRAME_HEIGHT = 480;
	
	public static final int UNIVERSE_WIDTH = 720*2;//98304;
	public static final int UNIVERSE_HEIGHT = 480*2;//65536;
	
	public static final int CELESTIAL_OBJECT_COUNT = 10;
	
	private Gravity universe;
	private MyMouseListener myMouse;
	private MyKeyboardListener myKeyboard;
	private GraphicView view;

	public Controller() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		universe = new Gravity(CELESTIAL_OBJECT_COUNT, UNIVERSE_WIDTH, UNIVERSE_HEIGHT);
		view = new GraphicView(universe, FRAME_WIDTH, FRAME_HEIGHT);
		
		myMouse = new MyMouseListener();
		myKeyboard = new MyKeyboardListener();
		
		view.addMouseListener(myMouse);
		view.addKeyListener(myKeyboard);
		
//		this.addComponentListener(l);
		this.addKeyListener(myKeyboard);
		
		setLocation(50, 50);
		setTitle(CELESTIAL_OBJECT_COUNT + " - Objects Collision");
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

	private class MyKeyboardListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			char keyPressed = e.getKeyChar();
			System.out.println("Key pressed: " + keyPressed);
			if(keyPressed == 'w')
				view.updateFramePosition(0, -FRAME_HEIGHT);
			else if(keyPressed == 's')
				view.updateFramePosition(0, FRAME_HEIGHT);
			
			else if(keyPressed == 'a')
				view.updateFramePosition(-FRAME_WIDTH, 0);
			else if(keyPressed == 'd')
				view.updateFramePosition(FRAME_WIDTH, 0);
			
		}
		
	}
	
	private class MyMouseListener implements MouseListener, MouseMotionListener {

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
