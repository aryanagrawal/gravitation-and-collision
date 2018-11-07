package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;

import multithreadedmodel.GravityMT;
import view.GraphicViewMT;

public class ControllerMT extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The dimensions of frame for the Graphics
	public static final int FRAME_WIDTH = 720;
	public static final int FRAME_HEIGHT = 480;

	// Graphics view and hardware listeners
	private GravityMT universe;
	private MyKeyboardListener myKeyboard;
	private MyButtonListener myButtons;
	private GraphicViewMT view;

	// initial magnification of the graphics screen
	public static double magnification = 1;

	// Dimensions of the actual plane.
	public static int UNIVERSE_WIDTH = 720*1024;
	public static int UNIVERSE_HEIGHT = 480*1024;

	// number of bodies on the plane
	public static int CELESTIAL_OBJECT_COUNT = 1024;

	public static int NUM_WORKERS = 8;
	public static int TIMESTEPS = 5000;
	public static boolean withgraphics = false;

	public ControllerMT(boolean withgraphics) {
		universe = new GravityMT(CELESTIAL_OBJECT_COUNT, UNIVERSE_WIDTH, UNIVERSE_HEIGHT, NUM_WORKERS);

		if (withgraphics) {
			// setup the graphics panel and controller (navigator)
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			view = new GraphicViewMT(universe, FRAME_WIDTH, FRAME_HEIGHT, magnification);
			myKeyboard = new MyKeyboardListener();
			myButtons = new MyButtonListener();
			this.addKeyListener(myKeyboard);
			setupController();
			setLocation(50, 50);
			setTitle(CELESTIAL_OBJECT_COUNT + " - Objects Collision");
			add(view);
		}
	}

	public static void main(String[] args) {
		/*
		 * Command line args number of workers, 1 to 32. This argument will be
		 * ignored by the sequential solution. number of bodies. size of each
		 * body. number of time steps.
		 * 
		 */

		int numargs = args.length;
		int size = 32;
		int sizes[] = new int[size];
		if (numargs == 0) {
			System.out.println("no command line arguments specified, here are the specifications");
			System.out.println("number of workers: 1");
			System.out.println("number of bodies: 32");
			System.out.println("timesteps: 500");
			System.out.println("screen dimensions: 720x480");
		} else if (numargs == 1) {
			// this means read the data from a file.
			// CELESTIAL_OBJECT_COUNT
			// NUM_WORKERS
			// TIMESTEPS
		} else if (numargs == 4) {
			// This means that the size of all objects is the same
			NUM_WORKERS = Integer.parseInt(args[0]);
			CELESTIAL_OBJECT_COUNT = Integer.parseInt(args[1]);
			size = Integer.parseInt(args[2]);
			TIMESTEPS = Integer.parseInt(args[3]);
		} else if (numargs == 3 + Integer.parseInt(args[1])) {
			// this means that the bodies are different sizes
			NUM_WORKERS = Integer.parseInt(args[0]);
			CELESTIAL_OBJECT_COUNT = Integer.parseInt(args[1]);
			size = Integer.parseInt(args[2]);
			for (int i = 3; i < 3 + size; i++) {
				sizes[i - 3] = Integer.parseInt(args[i]);
			}
			TIMESTEPS = Integer.parseInt(args[3 + size]);
		}

		ControllerMT universeController = new ControllerMT(withgraphics);

		if (withgraphics) {
			universeController.setVisible(true);
			while (true) {
				universeController.view.repaint();
				universeController.universe.updateDynamics();
			}
		} else {
			long startTime = System.nanoTime();
			for (int i = 0; i < TIMESTEPS; i++) {
				// while(true) {
				universeController.universe.updateDynamics();
			}
			
			long endTime = System.nanoTime();
			long microseconds = (endTime - startTime) / 1000;
			long s = microseconds / 1000000;
			long ms = microseconds % 1000000;
			System.out.println("Time taken by "+NUM_WORKERS+" threads: " + s + " s, " + ms + "ms");
			System.out.println("Total Collisions:");
			System.out.println("\t Collision by Walls: " + universeController.universe.getSideBarsCollisions());
			System.out.println("\t Total interbody collisions: " + universeController.universe.getInterBodyCollision());
		}
	}

	private JFrame control;
	private JButton up, down, left, right;
	private JButton zoomIn, zoomOut;
	private JButton speedUp, speedDown;

	private void setupController() {
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

		zoomIn = new JButton("+");
		zoomIn.setSize(50, 60);
		zoomIn.setLocation(195, 150);
		zoomIn.addActionListener(myButtons);
		control.add(zoomIn);

		zoomOut = new JButton("-");
		zoomOut.setSize(50, 60);
		zoomOut.setLocation(195, 210);
		zoomOut.addActionListener(myButtons);
		control.add(zoomOut);

		speedUp = new JButton("^");
		speedUp.setSize(50, 60);
		speedUp.setLocation(55, 150);
		speedUp.addActionListener(myButtons);
		control.add(speedUp);

		speedDown = new JButton("v");
		speedDown.setSize(50, 60);
		speedDown.setLocation(55, 210);
		speedDown.addActionListener(myButtons);
		control.add(speedDown);

	}

	private class MyButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton pressed = (JButton) e.getSource();
			if (pressed == up) {
				view.updateFramePosition(0, (int) (-FRAME_HEIGHT));
			}
			if (pressed == down) {
				view.updateFramePosition(0, (int) (FRAME_HEIGHT));
			}
			if (pressed == left) {
				view.updateFramePosition((int) (-FRAME_WIDTH), 0);
			}
			if (pressed == right) {
				view.updateFramePosition((int) (FRAME_WIDTH), 0);
			}
			if (pressed == zoomIn) {
				magnification /= 2;
				view.changeMagnification(magnification);
			}
			if (pressed == zoomOut) {
				magnification *= 2;
				view.changeMagnification(magnification);
			}
			if (pressed == speedUp) {
				view.changeSpeed(10);
			}
			if (pressed == speedDown) {
				view.changeSpeed(0.1);
			}

		}
	}

	private class MyKeyboardListener implements KeyListener {

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
			if (keyPressed == 'w')
				view.updateFramePosition(0, (int) (-FRAME_HEIGHT));
			else if (keyPressed == 's')
				view.updateFramePosition(0, (int) (FRAME_HEIGHT));
			else if (keyPressed == 'a')
				view.updateFramePosition((int) (-FRAME_WIDTH), 0);
			else if (keyPressed == 'd')
				view.updateFramePosition((int) (FRAME_WIDTH), 0);
			else if (keyPressed == 'i') {
				magnification *= 2;
				view.changeMagnification(magnification);
			} else if (keyPressed == 'k') {
				magnification /= 2;
				view.changeMagnification(magnification);
			}
		}
	}
}
