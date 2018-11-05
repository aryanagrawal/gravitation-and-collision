package multithreadedmodel;

import java.util.Random;

public class GravityMT {

//	public CelestialObject[] bodies;
	public int count;
	Random generator;
	public int WIDTH, HEIGHT;
	public static final double G = 6.67e-11;
	public double DELTA_T = 1e-6;
	
	// this new data thing maybe this would work lol
	public double mass[];
	public double radius[];
	public double positionX[], positionY[];
	public double velocityX[], velocityY[];
	public double forceX[], forceY[];

	public GravityMT(int count, int width, int height) {

		WIDTH = width;
		HEIGHT = height;
		this.count = count;
		
		this.radius    = new double[count];
		this.mass      = new double[count];
		this.positionX = new double[count];
		this.positionY = new double[count];
		this.velocityX = new double[count];
		this.velocityY = new double[count];
		this.forceX    = new double[count];
		this.forceY    = new double[count];
		generator = new Random();

		for (int i = 0; i < count; i++) {
			label: while (true) {
				// create a random (x, y) position and a radius
				// verify it, if valid, accept it and move on.

				double x = generator.nextInt(WIDTH);
				double y = generator.nextInt(HEIGHT);
				double radius = generator.nextInt(10) + 10;

				if (isValid(i, x, y, radius)) {
					double xVel = generator.nextInt(40) - 20;
					double yVel = generator.nextInt(40) - 20;
					double mass = (generator.nextInt(90) + 10) * 1e+17;
					
					this.mass[i] = mass;
					this.radius[i] = radius;
					this.positionX[i] = x;
					this.positionY[i] = y;
					this.velocityX[i] = xVel;
					this.velocityY[i] = yVel;
					
					break label;
				}
			}
		}
	}

	// Read the data from the Objects and return the requested value
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

	public double getMass(int i) {
		return mass[i];
	}
	
	/*
	 * Calculate the distance between two points
	 * Input: (x1, y1) for point 1, (x2, y2) for point 2
	 * Output: Apply the distance formula, plug in the values and return the answer.
	 */
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
	
	/*
	 * Check if the positioning of the object at 'index' is valid or not
	 * Input: index of the object with its (x, y) coordinates
	 * 			also the radius of the object
	 * Check with all the other objects and the walls
	 * 		return true if the object is free
	 * 		return false if the object is within any other object or crossing the wall
	 * 
	 * Reads the data from all other object's position and radius.
	 */
	public boolean isValid(int index, double x, double y, double r) {
		if (x <= r || x + r >= WIDTH) {
			return false;
		}

		if (y <= r || y + r >= HEIGHT) {
			return false;
		}
		
		for (int i = 0; i < index; i++) {
			if (distance(x, y, positionX[i], positionY[i])
					<= (r + radius[i]))
				return false;
		}
		return true;
	}

	// Do an O(n^2) traversal, see if things collide
	// for every object, check with every object if it collides.
	// if it does, update the velocity.
	public void checkCollision() {
		double vels[][][] = new double[count][count][2];
		boolean vchange[] = new boolean[count];

		for (int i = 0; i < this.count; i++) {

			// check for collision with sides
			if (positionX[i] <= radius[i] || positionX[i] + radius[i] >= WIDTH) {
				velocityX[i] *= (-1);
			}

			// check for collision with horizontal bars
			if (positionY[i] <= radius[i] || positionY[i] + radius[i] >= HEIGHT) {
				velocityY[i] *= (-1);
			}

			label: for (int j = 0; j < this.count; j++) {
				if (j == i)
					continue label;
				else {
					if (distance(positionX[i], positionY[i], positionX[j], positionY[j])
							<= (radius[i] + radius[j])) {
						vchange[i] = true;
						double mass_sum = mass[i] + mass[j];
						double mass_dif = mass[i] - mass[j];
						double v1_x = velocityX[i];
						double v2_x = velocityX[j];
						double v1_y = velocityY[i];
						double v2_y = velocityY[j];
						double m2 = mass[j];
						double v1_x_new = (v1_x * mass_dif + 2 * m2 * v2_x) / mass_sum;
						double v1_y_new = (v1_y * mass_dif + 2 * m2 * v2_y) / mass_sum;
						vels[i][j][0] = v1_x_new;
						vels[i][j][1] = v1_y_new;
					}
				}
			}
		}

		for (int i = 0; i < this.count; i++) {
			if (vchange[i]) {
				double v_x = 0, v_y = 0;
				for (int j = 0; j < this.count; j++) {
					v_x += vels[i][j][0];
					v_y += vels[i][j][1];
				}
				velocityX[i] = v_x;
				velocityY[i] = v_y;
			}
		}
	}

	// using position and mass, this function calculates
	// the total forces acting on a body.
	public void updateForces() {
		double[][][] forces = new double[count][count][2];
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
				forces[i][j][0] = fX;
				forces[i][j][1] = fY;

				forces[j][i][0] = -fX;
				forces[j][i][1] = -fY;
			}
		}

		for (int i = 0; i < count; i++) {
			double fX = 0, fY = 0;
			for (int j = 0; j < count; j++) {

				fX += forces[i][j][0];
				fY += forces[i][j][1];
			}
			forceX[i] = fX;
			forceY[i] = fY;
		}
	}

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
		int index = 0;
		for (int i=0; i<count; i++) {
			s.append("" + (index+1) + '\n');
			s.append("Radius: " + this.radius[index] + "\n");
			s.append("Mass: " + this.mass[index] + "\n");
			s.append("Position: (" + this.positionX[index] + ", " + this.positionY[index] + ")\n");
			s.append("Velocity: (" + this.velocityX[index] + ", " + this.velocityY[index] + ")\n");
			s.append("Force: (" + this.forceX[index] + ", " + this.forceY[index] + ")\n");
			index++;
			s.append('\n');
		}
		return s.toString();
	}
}
