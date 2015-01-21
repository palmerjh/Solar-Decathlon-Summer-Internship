
public class LineSegment { // these are line segments defined in the xy-plane by slope, y-intercept, and domain

	public Points lineVector; 					// (v2 - v1)
	public Points lineStart;					// v1
	public final double maxT = 1.0;				// lineStart (v1) + maxT (1) * lineVector (v2 - v1) = lineEnd (v2)
	
	public Points normalVector;					// default is +90 degrees from lineVector
	public Points normalStart;					// axis and counterclockwise is positive (in radians); can be switched

	
	public LineSegment() {
		
		lineVector = new Points(1, 0);
		lineStart = new Points(0, 0);
		
		normalVector = new Points(0, 1);
		normalStart = new Points(0.5, 0);
	}
	
	public LineSegment(Points p1, Points p2) {
		
		lineVector = new Points(p2.x - p1.x, p2.y - p1.y);
		lineStart = p1;
		
		normalVector = new Points(-lineVector.y, lineVector.x);
		normalStart = new Points((p1.x + p2.x) / 2.0, (p1.y + p2.y) / 2.0);
	}
	
	public void flipNormal() {
		
		normalVector.x *= -1;
		normalVector.y *= -1;
	}

}
