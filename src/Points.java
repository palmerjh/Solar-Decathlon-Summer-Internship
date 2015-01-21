
public class Points {

	public double x;
	public double y;

	public Points() {
		x = 0.0;
		y = 0.0;
	}
	
	public Points(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double magnitude() {
		
		return Math.sqrt(x*x + y*y);
	}

}
