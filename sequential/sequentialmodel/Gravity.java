package sequentialmodel;

import java.util.Random;

public class Gravity {

	public CelestialBody[] bodies;
	public static int count;
	public Random generator;
	public int WIDTH, HEIGHT;
	public static final double G = 6.67e-11;
	public double DELTA_T = 1e-7;

	public static int interBodyCollision;
	public static int borderCollision;

	public Gravity(int c, int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		count = c;
		this.bodies = new CelestialBody[c];
		interBodyCollision = 0;
		borderCollision = 0;
		generator = new Random();

		long startTime = System.nanoTime();
		for (int i = 0; i < c; i++) {
			label: while (true) {
				// create a random (x, y) position and a radius
				// verify it, if valid, accept it and move on.

				double x = generator.nextInt(WIDTH);
				double y = generator.nextInt(HEIGHT);
				double radius = generator.nextInt(10) + 10;

				if (isValid(i, x, y, radius)) {
					bodies[i] = new CelestialBody(x, y, // (x, y)
							((generator.nextInt(90) + 10) * 1e+17), // mass
							radius, // radius
							generator.nextInt(40) - 20, // xVelocity
							generator.nextInt(40) - 20); // yVelocity
					break label;
				}
			}
		}
		long endTime = System.nanoTime();
		long microseconds = (endTime - startTime) / 1000;
		long s = microseconds / 1000000;
		long ms = microseconds % 1000000;

		System.out.println("Preparation Time: " + s + " s, " + ms + "ms");
	}

	// Getters and Setters
	public double getRadius(int index) {
		return bodies[index].getRadius();
	}

	public double getXPosition(int index) {
		return bodies[index].getXPosition();
	}

	public double getYPosition(int index) {
		return bodies[index].getYPosition();
	}

	public double getXVelocity(int index) {
		return bodies[index].getXVelocity();
	}

	public double getYVelocity(int index) {
		return bodies[index].getYVelocity();
	}

	public int getCount() {
		return count;
	}

	public double getMass(int i) {
		return bodies[i].getMass();
	}

	public int getSideBarsCollisions() {
		return borderCollision;
	}

	public int getInterBodyCollision() {
		return interBodyCollision;
	}

	// Calculate the distance between two points
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/*
	 * This function is used only once; while creating the frame. Check if the
	 * initial positioning of an object is valid or not.
	 * 
	 * A frame is said to be valid if no object is within any other object, and
	 * also not crossing the universe bounds. The object need not be in the
	 * initial frame limits if graphics is used.
	 * 
	 * Input: index of the object which will be used to check with the objects
	 * of lower limit to decrease the computations (x, y): proposed coordinates
	 * of the object radius: the radius of the object
	 * 
	 * Returns false if the positioning is invalid, true otherwise.
	 */
	public boolean isValid(int index, double x, double y, double radius) {
		// if the object crosses the side bars, that is invalid
		if (x <= radius || x + radius >= WIDTH) {
			return false;
		}

		// if the object crosses the vertical limits, that is invalid
		if (y <= radius || y + radius >= HEIGHT) {
			return false;
		}

		// if the object is within any other object, that is also invalid
		for (int i = 0; i < index; i++) {
			if (distance(x, y, bodies[i].getXPosition(), bodies[i].getYPosition()) <= (radius + bodies[i].getRadius()))
				return false;
		}
		return true;
	}

	/*
	 * This function checks for a potential collision between objects and the
	 * edge bars While moving in the plane, it is possible for an object to
	 * collide with the other. If that happens, update their velocities.
	 * 
	 * For every object, check if it collides with the edges. If it does, switch
	 * the direction then for every following object, check if they are
	 * colliding, and if they do, use the Newton's formula to get the new
	 * velocity and do the update on both the objects. We can do the updates for
	 * two objects at a time
	 * 
	 * Runtime: O(n^2) Space Complexity: O(n)
	 */
	public void checkCollision() {
		double vels[][] = new double[count][2];
		boolean vchange[] = new boolean[count];

		// Iterate over every object and check for collision
		for (int i = 0; i < count; i++) {
			// check for collision with sides
			if (bodies[i].getXPosition() <= bodies[i].getRadius()
					|| bodies[i].getXPosition() + bodies[i].getRadius() >= WIDTH) {
				bodies[i].setVelocity(bodies[i].getXVelocity() * (-1), bodies[i].getYVelocity());
				borderCollision++;
			}

			// check for collision with horizontal bars
			if (bodies[i].getYPosition() <= bodies[i].getRadius()
					|| bodies[i].getYPosition() + bodies[i].getRadius() >= HEIGHT) {
				bodies[i].setVelocity(bodies[i].getXVelocity(), bodies[i].getYVelocity() * (-1));
				borderCollision++;
			}

			// for the object at i-th position, check with every other object
			// we won't check the collision for a lower indexed object because
			// it has already been checked.
			for (int j = i + 1; j < count; j++) {
				if (distance(bodies[i].getXPosition(), bodies[i].getYPosition(), bodies[j].getXPosition(),
						bodies[j].getYPosition()) <= (bodies[i].getRadius() + bodies[j].getRadius())) {
					interBodyCollision++;
					vchange[i] = true;
					vchange[j] = true;
					double mass_sum = bodies[i].getMass() + bodies[j].getMass();
					double mass_dif = bodies[i].getMass() - bodies[j].getMass();
					double v1_x = bodies[i].getXVelocity();
					double v2_x = bodies[j].getXVelocity();
					double v1_y = bodies[i].getYVelocity();
					double v2_y = bodies[j].getYVelocity();
					double m1 = bodies[i].getMass();
					double m2 = bodies[j].getMass();
					double v1_x_new = (v1_x * mass_dif + 2 * m2 * v2_x) / mass_sum;
					double v1_y_new = (v1_y * mass_dif + 2 * m2 * v2_y) / mass_sum;
					double v2_x_new = (v2_x * mass_dif * (-1) + 2 * m1 * v1_x) / mass_sum;
					double v2_y_new = (v2_y * mass_dif * (-1) + 2 * m1 * v1_y) / mass_sum;
					vels[i][0] += v1_x_new;
					vels[i][1] += v1_y_new;
					vels[j][0] += v2_x_new;
					vels[j][1] += v2_y_new;
				}
			}
			// if there is a collision, this thing gets true, and then we update
			// the velocity.
			if (vchange[i])
				bodies[i].setVelocity(vels[i][0], vels[i][1]);
		}
	}

	// using position and mass, this function calculates
	// the total forces acting on a body.
	public void updateForces() {
		double[][] forces = new double[count][2];
		for (int i = 0; i < count; i++) {
			double m1 = bodies[i].getMass();
			double x1 = bodies[i].getXPosition();
			double y1 = bodies[i].getYPosition();

			for (int j = i + 1; j < count; j++) {
				double m2 = bodies[j].getMass();
				double x2 = bodies[j].getXPosition();
				double y2 = bodies[j].getYPosition();
				double R = distance(x1, y1, x2, y2);
				double mode = (G * m1 * m2) / (R * R * R);
				double forceX = mode * (x2 - x1);
				double forceY = mode * (y2 - y1);

				// update two at a time
				// force of one on two is negative of that from two on one
				forces[i][0] += forceX;
				forces[i][1] += forceY;
				forces[j][0] += -forceX;
				forces[j][1] += -forceY;
			}
		}

		for (int i = 0; i < count; i++) {
			bodies[i].setForces(forces[i][0], forces[i][1]);
		}
	}

	public void updatePosition() {

		for (int i = 0; i < count; i++) {
			double velocityX = bodies[i].getXVelocity();
			double velocityY = bodies[i].getYVelocity();
			double time = DELTA_T;
			double x = velocityX * time + 0.5 * bodies[i].getXForce() * time * time / bodies[i].getMass();
			double y = velocityY * time + 0.5 * bodies[i].getYForce() * time * time / bodies[i].getMass();
			bodies[i].setPosition(bodies[i].getXPosition() + x, bodies[i].getYPosition() + y);
			// v = u + at
			x = velocityX + bodies[i].getXForce() * time / bodies[i].getMass();
			y = velocityY + bodies[i].getYForce() * time / bodies[i].getMass();
			bodies[i].setVelocity(x, y);
		}
	}

	public void updateDynamics() {
		checkCollision();
		updateForces();
		updatePosition();
	}

	public void updateSpeed(double factor) {
		this.DELTA_T *= factor;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		int num = 1;
		for (CelestialBody body : bodies) {
			s.append("" + num + '\n');
			s.append(body.toString());
			s.append('\n');
			num++;
		}
		return s.toString();
	}
}