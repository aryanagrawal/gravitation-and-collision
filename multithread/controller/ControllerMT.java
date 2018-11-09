package controller;

import multithreadedmodel.GravityMT;

public class ControllerMT {

	/**
	 * 
	 */

	// Graphics view and hardware listeners
	private GravityMT universe;

	// Dimensions of the actual plane.
	public static int UNIVERSE_WIDTH = 1440;
	public static int UNIVERSE_HEIGHT = 960;

	// number of bodies on the plane
	public static int CELESTIAL_OBJECT_COUNT = 64;

	public static int NUM_WORKERS = 4;
	public static int TIMESTEPS = 5000;

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
		System.out.println("Options:");
		System.out.println("1. Default run, no command line arguments required\n");
		
		System.out.println("2. Provide the following as command line arguments:");
		System.out.println("   Radius will be randomly generated in range 10 - 20");
		System.out.println("\t number of workers");
		System.out.println("\t number of bodies");
		System.out.println("\t number of iterations (timesteps)");
		
		System.out.println(numargs + " Command line argumets provided");
		
		if (numargs == 0) {
			System.out.println("\t number of workers: 4");
			System.out.println("\t number of bodies: 64");
			System.out.println("\t timesteps: 5000");
			System.out.println("\t plane dimensions: 1440 x 960");
		} else if (numargs == 3) {
			// This means that the size of all objects is the same
			NUM_WORKERS = Integer.parseInt(args[0]);
			CELESTIAL_OBJECT_COUNT = Integer.parseInt(args[1]);
			TIMESTEPS = Integer.parseInt(args[2]);
		}

		ControllerMT universeController = new ControllerMT();

		long startTime = System.nanoTime();
		universeController.universe.updateDynamics(TIMESTEPS);

		long endTime = System.nanoTime();
		long microseconds = (endTime - startTime) / 1000;
		long s = microseconds / 1000000;
		long ms = microseconds % 1000000;
		System.out.println("Time taken by " + NUM_WORKERS + " threads: " + s + " s, " + ms + "ms");
		System.out.println("Total Collisions:");
		System.out.println("\t Collision by Walls: " + universeController.universe.getSideBarsCollisions());
		System.out.println("\t Total interbody collisions: " + universeController.universe.getInterBodyCollision());

	}
}
