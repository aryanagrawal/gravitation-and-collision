package multithreadedmodel;

public class Monitor {

	public static void main(String[] args){
		GravityMT model = new GravityMT(10, 100, 100);
		System.out.println(model.toString());
	}
}
