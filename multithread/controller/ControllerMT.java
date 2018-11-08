package controller;

import javax.swing.JFrame;

import multithreadedmodel.GravityMT;

public class ControllerMT extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Graphics view and hardware listeners
	private GravityMT universe;

	// Dimensions of the actual plane.
	public static int UNIVERSE_WIDTH = 720*512;
	public static int UNIVERSE_HEIGHT = 480*512;

	// number of bodies on the plane
	public static int CELESTIAL_OBJECT_COUNT = 1024;

	public static int NUM_WORKERS = 8;
	public static int TIMESTEPS = 1024*4;

	public ControllerMT() {
		universe = new GravityMT(CELESTIAL_OBJECT_COUNT, UNIVERSE_WIDTH, UNIVERSE_HEIGHT, NUM_WORKERS);
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

		ControllerMT universeController = new ControllerMT();

		
			long startTime = System.nanoTime();
			universeController.universe.updateDynamics(TIMESTEPS);
			
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
