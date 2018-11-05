package multithreadedmodel;

public class CelestialObject {

	private double xPosition, yPosition;
	private double xVelocity, yVelocity;
	private double xForce, yForce;
	
	private double mass;
	private double radius;
	
	public CelestialObject(double xPosition, double yPosition,
							double mass,
							double radius,
							double xVelocity, double yVelocity){
		
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.mass = mass;
		this.radius = radius;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	// getters
	public double getXPosition(){
		return xPosition;
	}

	public double getYPosition(){
		return yPosition;
	}
	
	public double getXVelocity(){
		return xVelocity;
	}

	public double getYVelocity(){
		return yVelocity;
	}
	
	public double getXForce(){
		return xForce;
	}
	
	public double getYForce(){
		return yForce;
	}
	
	public double getRadius(){
		return radius;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void setRadius(double r){
		this.radius = r;
	}
	
	public void setPosition(double x, double y){
		this.xPosition = x;
		this.yPosition = y;
	}
	
	public void setVelocity(double x, double y){
		this.xVelocity = x;
		this.yVelocity = y;
	}
	
	public void setForces(double x, double y){
		this.xForce = x;
		this.yForce = y;
	}
	
	// used to debug mostly
	@Override
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("Mass: " + mass + "\n");
		string.append("Radius: " + radius + "\n");
		string.append("Position: (" + xPosition + ", " + yPosition + ")\n");
		string.append("Velocity: (" + xVelocity + ", " + yVelocity + ")\n");
		string.append("Forces: (" + xForce + ", " + yForce + ")\n");
		return string.toString();
	}
}