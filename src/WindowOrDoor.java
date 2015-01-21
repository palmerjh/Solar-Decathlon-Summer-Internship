
public class WindowOrDoor {
	
	public double xCoord;				// distance from left side of wall to left side of window
	public double altitude;				// distance from ground to bottom side of window
	public double height;
	public double width;
	public double shgc;
	public double uValue;

	public WindowOrDoor() {
		xCoord = 2;
		setDefault();
	}
	
	public WindowOrDoor(double xCoord) {
		this.xCoord = xCoord;
		setDefault();
	}
	
	public WindowOrDoor(double xCoord, double altitude, double height, double width, double shgc, 
			double uValue) {
		this.xCoord = xCoord;
		this.altitude = altitude;
		this.height = height;
		this.width = width;
		this.shgc = shgc;
		this.uValue = uValue;
	}
	
	private void setDefault() {
		altitude = 1.5;
		height = 5.5;
		width = 4;
		shgc = 1;		// Suntuitive
		uValue = 0.24;		// Suntuitive
	}

}
