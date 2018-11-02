package model;
import java.util.Random;

public class Gravity {
	
	public CelestialBody[] bodies;
	public int count;
	Random generator;
	public static int WIDTH, HEIGHT;
	public static final double G = 6.67e-11;
	public static final double DELTA_T = 1e-6;
	
//	public Gravity(int width, int height){
//		WIDTH = width;
//		HEIGHT = height;
//		this.count = 2;
//		this.bodies = new CelestialBody[count];
//									// x1, y1, M, R, Vx, Vy
////		bodies[0] = new CelestialBody(WIDTH/2, HEIGHT/2, 1.989*(10e+30), 432169, 0, 0);
////		bodies[1] = new CelestialBody(92.96*(10e+6), HEIGHT/2, 5.972*(10e+24), 3959, 30, 0);
//		
////		bodies[2] = new CelestialBody(190, 230, 5e+17, 10, 0, -5e+2);
//		
////		bodies[3] = new CelestialBody(90.0, 62.0, 5e+17, 10, 0, 0);
////		bodies[4] = new CelestialBody(40.0, 60.0, 5e+17, 10, 0, 0);
////		bodies[5] = new CelestialBody(70.0, 60.0, 5e+17, 10,0, 00);
//		
//	}
	
	public Gravity(int count, int width, int height){
		WIDTH = width;
		HEIGHT = height;
		this.count = count;
		this.bodies = new CelestialBody[count];
		generator = new Random();
		
		for(int i=0; i<count; i++){
			label: while(true){
				// create a random (x, y) position and a radius
				// verify it, if valid, accept it and move on.
				
				double x = generator.nextInt(WIDTH);
				double y = generator.nextInt(HEIGHT);
				double radius = generator.nextInt(10)+10;
				
				if(isValid(i, x, y, radius)){
					bodies[i] = new CelestialBody(x, y, 			// (x, y)
							((generator.nextInt(90)+10)*1e+17), 	// mass
							radius, 								// radius
							0, 0);
							//generator.nextInt(40) -20, 				// xVelocity
							//generator.nextInt(40) -20);				// yVelocity
					break label;
				}
			}
		}
	}
	
	// Getters and Setters
	public double getRadius(int index){
		return bodies[index].getRadius();
	}
	
	public double getXPosition(int index){
		return bodies[index].getXPosition();
	}
	
	public double getYPosition(int index){
		return bodies[index].getYPosition();
	}
	
	public double getXVelocity(int index){
		return bodies[index].getXVelocity();
	}
	
	public double getYVelocity(int index){
		return bodies[index].getYVelocity();
	}
	
	public int getCount(){
		return count;
	}
	
	public double getMass(int i) {
		return bodies[i].getMass();
	}
	
	// Calculate the distance between two points
	public double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt((y2-y1)*(y2-y1) + (x2-x1)*(x2-x1));
	}
	
	// check for the validity of two objects
	// if the objects are even slightly within the other, return false
	// else, return true
	public boolean isValid(int index, double x, double y, double radius){
		if(x <= radius || x + radius  >= WIDTH){
			return false;
		}
		
		if(y <= radius || y + radius  >= HEIGHT){
			return false;
		}
		
		for(int i = 0; i < index; i++){
			if(distance(x, y, bodies[i].getXPosition(), bodies[i].getYPosition()) <= (radius+bodies[i].getRadius()))
				return false;
		}
		
		return true;
	}
	
	// Do an O(n^2) traversal, see if things collide
	// for every object, check with every object if it collides.
	// if it does, update the velocity.
	public void checkCollision(){
		double vels[][][] = new double[this.count][this.count][2];
		boolean vchange[] = new boolean[this.count];
		
		for(int i=0; i<this.count; i++){
			
			// check for collision with sides
			if(bodies[i].getXPosition() <= bodies[i].getRadius() || 
					bodies[i].getXPosition() + bodies[i].getRadius()  >= WIDTH){
				bodies[i].setVelocity(bodies[i].getXVelocity()*(-1), bodies[i].getYVelocity());
			}
			
			// check for collision with horizontal bars
			if(bodies[i].getYPosition() <= bodies[i].getRadius() || 
					bodies[i].getYPosition() + bodies[i].getRadius()  >= HEIGHT){
				bodies[i].setVelocity(bodies[i].getXVelocity(), bodies[i].getYVelocity()*(-1));
			}
			
			label: for(int j=0; j<this.count; j++){
				if(j==i)
					continue label;
				else{
					
					if(distance(bodies[i].getXPosition(), bodies[i].getYPosition(),
							bodies[j].getXPosition(), bodies[j].getYPosition()) <=
							(bodies[i].getRadius() + bodies[j].getRadius())){
						vchange[i] = true;
						double mass_sum = bodies[i].getMass() + bodies[j].getMass();
						double mass_dif = bodies[i].getMass() - bodies[j].getMass();
						double v1_x = bodies[i].getXVelocity();
						double v2_x = bodies[j].getXVelocity();
						double v1_y = bodies[i].getYVelocity();
						double v2_y = bodies[j].getYVelocity();
						double m2 = bodies[j].getMass();
						double v1_x_new = (v1_x*mass_dif + 2*m2*v2_x)/mass_sum;
						double v1_y_new = (v1_y*mass_dif + 2*m2*v2_y)/mass_sum;
						vels[i][j][0] = v1_x_new;
						vels[i][j][1] = v1_y_new;
					}
				}
			}
		}
		
		for(int i=0; i<this.count; i++){
			if(vchange[i]){
				double v_x = 0, v_y = 0;
				for(int j=0; j<this.count; j++){
					v_x += vels[i][j][0];
					v_y += vels[i][j][1];
				}
				bodies[i].setVelocity(v_x, v_y);
			}
		}
	}
	
	// using position and mass, this function calculates
	// the total forces acting on a body.
	public void updateForces(){
		double[][][] forces = new double[count][count][2];
		for(int i=0; i<count; i++){
			
			double m1 = bodies[i].getMass();
			double x1 = bodies[i].getXPosition();
			double y1 = bodies[i].getYPosition();
			
			for(int j=i+1; j<count; j++){
				
				double m2 = bodies[j].getMass();
				double x2 = bodies[j].getXPosition();
				double y2 = bodies[j].getYPosition();
				
				double R = distance(x1,y1,x2,y2);
				double mode = (G*m1*m2)/(R*R*R);
				
				double forceX = mode*(x2-x1);
				double forceY = mode*(y2-y1);
				forces[i][j][0] = forceX;
				forces[i][j][1] = forceY;
				
				forces[j][i][0] = -forceX;
				forces[j][i][1] = -forceY;
			}
		}
		
		for(int i=0; i<count; i++){
			double forceX = 0, forceY = 0;
			for(int j=0; j<count; j++){
				
				forceX += forces[i][j][0];
				forceY += forces[i][j][1];
			}
			bodies[i].setForces(forceX, forceY);
		}
	}
	
	public void updatePosition(){
		
		for(int i=0; i<count; i++){
			double velocityX = bodies[i].getXVelocity();
			double velocityY = bodies[i].getYVelocity();
			
			double time = DELTA_T;
			double x = velocityX*time+0.5*bodies[i].getXForce()*time*time/bodies[i].getMass();
			double y = velocityY*time+0.5*bodies[i].getYForce()*time*time/bodies[i].getMass();
			bodies[i].setPosition(bodies[i].getXPosition()+x, bodies[i].getYPosition()+y);
			
			// v = u + at
			x = velocityX + bodies[i].getXForce()*time/bodies[i].getMass();
			y = velocityY + bodies[i].getYForce()*time/bodies[i].getMass();
	
			bodies[i].setVelocity(x, y);
		}
	}
	
	public void updateDynamics(){
		checkCollision();
		updateForces();
		updatePosition();
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		int num = 1;
		for(CelestialBody body : bodies){
			s.append(""+num + '\n');
			s.append(body.toString());
			s.append('\n');
			num++;
		}
		return s.toString();
	}

}