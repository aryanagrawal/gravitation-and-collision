package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
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
	 */
	
	public static final int FRAME_WIDTH = 720;
	public static final int FRAME_HEIGHT = 480;
	
	public static final int UNIVERSE_WIDTH = 720*2;
	public static final int UNIVERSE_HEIGHT = 480*2;
	
	public static double magnification = 1;
	
	public static final int CELESTIAL_OBJECT_COUNT = 20;
	
	private Gravity universe;
	private MyMouseListener myMouse;
	private MyKeyboardListener myKeyboard;
	private MyButtonListener myButtons;
	
	private GraphicView view;
	

	public Controller() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		universe = new Gravity(CELESTIAL_OBJECT_COUNT, UNIVERSE_WIDTH, UNIVERSE_HEIGHT);
		view = new GraphicView(universe, FRAME_WIDTH, FRAME_HEIGHT, magnification);
		
		myMouse = new MyMouseListener();
		myKeyboard = new MyKeyboardListener();
		myButtons = new MyButtonListener();
		
		this.addKeyListener(myKeyboard);
		
		setupController();
		
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
	
	private JFrame control;
	private JButton up, down, left, right;
	private void setupController(){
		control = new JFrame("Controller");
		control.setLocation(800, 50);
		control.setSize(300, 480);
		control.setVisible(true);
		
		left = new JButton("\u2190");
		left.setSize(50, 50);
		left.setLocation(55, 370);
		left.addActionListener(myButtons);
		control.add(left);
		
		up = new JButton("\u2191");
		up.setSize(50, 50);
		up.setLocation(125, 300);
		up.addActionListener(myButtons);
		control.add(up);
		
		right = new JButton("\u2192");
		right.setSize(50, 50);
		right.setLocation(195, 370);
		right.addActionListener(myButtons);
		control.add(right);
		
		down = new JButton("\u2193");
		down.setSize(50, 50);
		down.setLocation(125, 370);
		down.addActionListener(myButtons);
		control.add(down);
	}
	private class MyButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton pressed = (JButton) e.getSource();
			if(pressed == up){
				view.updateFramePosition(0, (int) (-FRAME_HEIGHT));
			} else if(pressed == down){
				view.updateFramePosition(0, (int) (FRAME_HEIGHT));
			} else if(pressed == left){
				view.updateFramePosition((int) (-FRAME_WIDTH), 0);
			} else if(pressed == right){
				view.updateFramePosition((int) (FRAME_WIDTH), 0);
			}
			/*
			 * if(keyPressed == 'w')
				view.updateFramePosition(0, (int) (-FRAME_HEIGHT));
			else if(keyPressed == 's')
				view.updateFramePosition(0, (int) (FRAME_HEIGHT));
			else if(keyPressed == 'a')
				view.updateFramePosition((int) (-FRAME_WIDTH), 0);
			else if(keyPressed == 'd')
				view.updateFramePosition((int) (FRAME_WIDTH), 0);
			else if(keyPressed == 'i'){
				magnification *= 2;
				view.changeMagnification(magnification);
			}
			else if(keyPressed == 'k'){
				magnification /= 2;
				view.changeMagnification(magnification);
			}
			 */
			
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
			if(keyPressed == 'w')
				view.updateFramePosition(0, (int) (-FRAME_HEIGHT));
			else if(keyPressed == 's')
				view.updateFramePosition(0, (int) (FRAME_HEIGHT));
			else if(keyPressed == 'a')
				view.updateFramePosition((int) (-FRAME_WIDTH), 0);
			else if(keyPressed == 'd')
				view.updateFramePosition((int) (FRAME_WIDTH), 0);
			else if(keyPressed == 'i'){
				magnification *= 2;
				view.changeMagnification(magnification);
			}
			else if(keyPressed == 'k'){
				magnification /= 2;
				view.changeMagnification(magnification);
			}
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
