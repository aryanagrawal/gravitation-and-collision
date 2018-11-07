package multithreadedmodel;

import java.util.Random;


public class GravityMT {

	public static int count;
	public static Random generator;
	public static long WIDTH;
	public static long HEIGHT;
	public static final double G = 6.67e-11;
	public double DELTA_T = 1e-6;

	public static boolean defined[];
	public static double mass[];
	public static double radius[];
	public static double positionX[], positionY[];
	public static double velocityX[], velocityY[];
	public static double forceX[], forceY[];
	public static int creatorIndices[][];
	
	static CreateObjects objectsCreator[];
	static int numThreads = 6;
	
	
	public GravityMT(int num, long width, long height) {

		WIDTH = width;
		HEIGHT = height;
		count = num;

		defined = new boolean[num];
		radius = new double[num];
		mass = new double[num];
		positionX = new double[num];
		positionY = new double[num];
		velocityX = new double[num];
		velocityY = new double[num];
		forceX = new double[num];
		forceY = new double[num];
		generator = new Random();

		
		creatorIndices = new int[count][2];
		int evenDistribution = count / numThreads;
		int extras = count % numThreads;
		int first = 0;
		int last = evenDistribution-1;
		if(extras > 0){
			last++;
			extras--;
		}
		for(int i=0; i<numThreads; i++){
			creatorIndices[i][0] = first;
			creatorIndices[i][1] = last;
			first = last + 1;
			last = first + evenDistribution-1;
			if(extras > 0){
				last++;
				extras--;
			}
		}
		objectsCreator = new CreateObjects[numThreads];
		// create N processes
		for (int i = 0; i < numThreads; i++) {
			objectsCreator[i] = new CreateObjects(i, count, numThreads);
		}

		// tell them to create the objects
		for (int i = 0; i < numThreads; i++)
			objectsCreator[i].start();
		
		// kill them when they're done doing that
		for(int i=0; i < numThreads; i++){
			try {
				objectsCreator[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		System.out.println(toString());
//		for (int i = 0; i < num; i++) {
//			label: while (true) {
//				// create a random (x, y) position and a radius
//				// verify it, if valid, accept it and move on.
//
//				double x = generator.nextInt((int)WIDTH);
//				double y = generator.nextInt((int)HEIGHT);
//				double r = generator.nextInt(10000) + 2500;
//
//				if (isValid(x, y, r)) {
//					double xVel = generator.nextInt(40) - 20;
//					double yVel = generator.nextInt(40) - 20;
//					double m = (generator.nextInt(90) + 10) * 1e+17;
//
//					GravityMT.mass[i] = m;
//					radius[i] = r;
//					positionX[i] = x;
//					positionY[i] = y;
//					velocityX[i] = xVel;
//					velocityY[i] = yVel;
//					break label;
//				}
//			}
//		}
		
	}
	
	static class CreateObjects extends Thread{
		int threadId;
		
		public CreateObjects(int threadId, int totalCount, int totalThreads) {
			this.threadId = threadId;
		}
		
		public void run(){
			createObjects(creatorIndices[threadId][0], creatorIndices[threadId][1]);
		}
		
		private void createObjects(int left, int right){
			for (int i = left; i <= right; i++) {
				label: while (true) {
					// create a random (x, y) position and a radius
					// verify it, if valid, accept it and move on.

					double x = generator.nextInt((int)WIDTH);
					double y = generator.nextInt((int)HEIGHT);
					double r = generator.nextInt(10000) + 2500;

					if (isValid(x, y, r)) {
						double xVel = generator.nextInt(40) - 20;
						double yVel = generator.nextInt(40) - 20;
						double m = (generator.nextInt(90) + 10) * 1e+17;

						GravityMT.mass[i] = m;
						radius[i] = r;
						positionX[i] = x;
						positionY[i] = y;
						velocityX[i] = xVel;
						velocityY[i] = yVel;
						defined[i] = true;
						break label;
					}
				}
			}
		}
	}

	// Read the data from the Objects and return the requested value
	public double getMass(int i) {
		return mass[i];
	}

	public double getRadius(int index) {
		return radius[index];
	}

	public double getXPosition(int index) {
		return positionX[index];
	}

	public double getYPosition(int index) {
		return positionY[index];
	}

	public double getXVelocity(int index) {
		return velocityX[index];
	}

	public double getYVelocity(int index) {
		return velocityY[index];
	}

	public int getCount() {
		return count;
	}

	/*
	 * Calculate the distance between two points Input: (x1, y1) for point 1,
	 * (x2, y2) for point 2 Output: Apply the distance formula, plug in the
	 * values and return the answer.
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/*
	 * Check if the positioning of the object at 'index' is valid or not Input:
	 * index of the object with its (x, y) coordinates also the radius of the
	 * object Check with all the other objects and the walls return true if the
	 * object is free return false if the object is within any other object or
	 * crossing the wall
	 * 
	 * Reads the data from all other object's position and radius.
	 */
	public static boolean isValid(double x, double y, double r) {
		if (x <= r || x + r >= WIDTH) {
			return false;
		}

		if (y <= r || y + r >= HEIGHT) {
			return false;
		}

		for (int i = 0; i < count; i++) {
			if(!defined[i])
				continue;
			if (distance(x, y, positionX[i], positionY[i]) <= (r + radius[i]))
				return false;
		}
		return true;
	}

	/*
	 * Check if there is a potential collision in a set frame If there is,
	 * update the velocities by applying the formula
	 * 
	 * No input, but allocate a 2D array, write the velocities if there is a
	 * collision, then add the resultant velocity and update for the new one.
	 * 
	 * Running Time: O(n^2) Space Complexity: O(n)
	 */
	public void checkCollision() {

		// these three arrays are the updated velocities,
		// and if there is an update or not
		double velX[] = new double[count];
		double velY[] = new double[count];
		boolean vchange[] = new boolean[count];

		// if a is not colliding with b, then b is not colliding with b
		// if a is colliding with b, then so is b.
		// this collision can be paired up and calculated altogether
		for (int i = 0; i < count; i++) {

			// check for collision vertical edge bars
			if (positionX[i] <= radius[i] || positionX[i] + radius[i] >= WIDTH) {
				velocityX[i] *= (-1);
			}
			
			// check for collision with horizontal edge bars
			if (positionY[i] <= radius[i] || positionY[i] + radius[i] >= HEIGHT) {
				velocityY[i] *= (-1);
			}
			
			double v1_x = velocityX[i];
			double v1_y = velocityY[i];
			
			for (int j = i + 1; j < count; j++) {
				// pair of two colliding, update their velocities
				if (distance(positionX[i], positionY[i], positionX[j], positionY[j]) <= (radius[i] + radius[j])) {
					vchange[i] = true;
					vchange[j] = true;
					double m1 = mass[i];
					double m2 = mass[j];
					double mass_sum = m1 + m2;
					double mass_dif = m1 - m2;
					double v2_x = velocityX[j];
					double v2_y = velocityY[j];
					double v1_x_new = (v1_x * mass_dif + 2 * m2 * v2_x) / mass_sum;
					double v1_y_new = (v1_y * mass_dif + 2 * m2 * v2_y) / mass_sum;
					double v2_x_new = (v2_x * mass_dif * (-1) + 2 * m1 * v1_x) / mass_sum;
					double v2_y_new = (v2_y * mass_dif * (-1) + 2 * m1 * v1_y) / mass_sum;
					velX[i] += v1_x_new;
					velY[i] += v1_y_new;
					velX[j] += v2_x_new;
					velY[j] += v2_y_new;
				}
			}
			// add up all the velocities from the array and write it to the index in the data
			if (vchange[i]) {
				velocityX[i] = velX[i];
				velocityY[i] = velY[i];
			}
		}
	}
	
	/*
	 * Calculate the net force acting on all the objects and
	 * update their values on the array.
	 * 
	 * For each object, READ their mass and position, and use Newton's formula
	 * to calculate the force in both directions.
	 * 
	 * This depends on data from other objects, so threads must be careful here.
	 * Another O(n^2) runtime algorithm.
	 */
	public void updateForces() {
		double forceOnX[] = new double[count];
		double forceOnY[] = new double[count];
		
		for (int i = 0; i < count; i++) {
			double m1 = mass[i];
			double x1 = positionX[i];
			double y1 = positionY[i];
			for (int j = i + 1; j < count; j++) {
				double m2 = mass[j];
				double x2 = positionX[j];
				double y2 = positionY[j];
				double R = distance(x1, y1, x2, y2);
				double mode = (G * m1 * m2) / (R * R * R);
				double fX = mode * (x2 - x1);
				double fY = mode * (y2 - y1);
				forceOnX[i] += fX;
				forceOnY[i] += fY;
				forceOnX[j] += -fX;
				forceOnY[j] += -fY;
			}
			forceX[i] = forceOnX[i];
			forceY[i] = forceOnY[i];
		}
	}

	/*
	 * Use Newton's second law to update the position of all the objects.
	 * This also updates their velocities.
	 * 
	 * Runtime: O(n) -> This does not depend on any other object, 
	 * so no coordination between threads
	 */
	public void updatePosition() {

		for (int i = 0; i < count; i++) {
			double velX = velocityX[i];
			double velY = velocityY[i];

			double time = DELTA_T;
			// x = u*t + 0.5*a*t*t
			double x = velX * time + (0.5 * forceX[i] * time * time) / mass[i];
			double y = velY * time + (0.5 * forceY[i] * time * time) / mass[i];
			positionX[i] += x;
			positionY[i] += y;
			// v = u + at
			x = velX + (forceX[i] * time) / mass[i];
			y = velY + (forceY[i] * time) / mass[i];

			velocityX[i] = x;
			velocityY[i] = y;
		}
	}

	/*
	 * This function first sees if there is a collision or not, if there is,
	 * then it updates its velocity
	 * 
	 * After colliding, update the net forces acting on each object due to every other
	 * 
	 * Then, update the position and the velocity of every object
	 * 
	 * Total Runtime: O(n^2 + n^2 + n) = O(n^2)
	 */
	public void updateDynamics() {
		checkCollision();
		updateForces();
		updatePosition();
	}

	/*
	 * This updates the value of Delta T,
	 * The change in time between two observations.
	 * 
	 * The values will be more accurate when this change is small,
	 * else we will have to dive into integral calculus.
	 * 
	 * This function is called by the controller which changes the DELTA_T value
	 * 
	 * Careful here, wait until we are not updating the dynamics, because that is time dependent.
	 * When this function is called, wait for all processes to finish, then block all writer functions
	 * that need time for calculation, then update this value, then release all other threads
	 * to continue with their calculation.
	 */
	public void updateSpeed(double factor) {
		this.DELTA_T *= factor;
	}

	// You really need documentation for this?
	public String toString() {
		StringBuilder s = new StringBuilder();
		int index = 0;
		for (int i = 0; i < count; i++) {
			s.append("" + (index + 1) + '\n');
			s.append("Radius: " + radius[index] + "\n");
			s.append("Mass: " + mass[index] + "\n");
			s.append("Position: (" + positionX[index] + ", " + positionY[index] + ")\n");
			s.append("Velocity: (" + velocityX[index] + ", " + velocityY[index] + ")\n");
			s.append("Force: (" + forceX[index] + ", " + forceY[index] + ")\n");
			index++;
			s.append('\n');
		}
		return s.toString();
	}
}
