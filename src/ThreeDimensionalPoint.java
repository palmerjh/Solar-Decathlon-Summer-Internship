
public class ThreeDimensionalPoint {
	
	public double x;
	public double y;
	public double z;

	public ThreeDimensionalPoint() {
		x = 0.0;
		y = 0.0;
		z = 0.0;

	}
	
	public ThreeDimensionalPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}

	public double magnitude() {
		
		return Math.sqrt(x*x + y*y + z*z);
	}

}
