
public class Wall {
	
	
	// ***************************************************************************************************************
	// *******************************BEGIN FIELDS SECTION************************************************************
	// ***************************************************************************************************************
	
	public double orientation;					// angle between normal and due south (west = positive)
	public String cardinality;					// determined below in private method
	public String name;							// name of wall ("Cardinality + "ern" + room.name + "Wall" + number")
	
	public double rawInsolation;
	// scaled by comparing ClimateConsultant with one wall of the box model solar gain results of this program where
	// overhangLength = 0, all windows have SHGC = 1.0, and GROUND_REFLECTANCE = 0.0; units are Btu/hr-ft^2 
		
	public double altitude;
	
	public double area;							// ignore presence of walls and doors; will be appropriately modified			
	public double uValue;						
	
	public WindowOrDoor[] windowOrDoor;			// doors are considered windows with SGHC = 0.0;
	
	public Obstruction[] obstruction;			// includes overhangs, left/right protrusions, & other sun-blocking objects
	
	public double groundReflectance;			// (gR); % light reflected off ground (grass = 20.0)
	public double baseInsolation; 				// base insolation/day w/o gR (0%) (ClimateConsultant)
	public double peakInsolation; 				// peak insolation/day with 100% gR (ClimateConsultant)
	public final double groundReflectanceContribution = (peakInsolation - baseInsolation) * groundReflectance / 100.0;
	
	// QUICK REFERENCE: gRC ({North, East, South, West}) = {{gR, 0, 0}, {gR, 300, 850}, {gR, 900, 2350}, {gR, 480, 1240}};	
	
	
	// ***************************************************************************************************************
	// *******************************END FIELDS SECTION**************************************************************
	// ***************************************************************************************************************
	
	
	
	
	
	// ***************************************************************************************************************
	// *******************************BEGIN CONSTRUCTORS SECTION******************************************************
	// ***************************************************************************************************************
	
	
	public Wall() {
		this.orientation = 0*Math.PI/180;							// due south
		this.cardinality = "South";
		this.name = "Southern Box Wall";
				
		this.area = 310; 								// assumes 31 x 31 x 10 box		
		this.uValue = 0.029;							// (super efficient house from textbook pp. 251)						
		
		this.windowOrDoor = new WindowOrDoor[4];
		this.windowOrDoor[0] = new WindowOrDoor(5);
		this.windowOrDoor[1] = new WindowOrDoor(12); 
		this.windowOrDoor[2] = new WindowOrDoor(19);
		this.windowOrDoor[3] = new WindowOrDoor(26);
		
		this.obstruction = new Obstruction[0];			// no overhangs, left/right protrusions, or other obstructions
		
		this.groundReflectance = 0.0;					// (grass = 20.0)
		this.baseInsolation = 900; 						// southern base (ok as default because default gR = 0)
		this.peakInsolation = 2350; 					// southern peak (ok as default because default gR = 0)
	}
	
	public Wall(double orientation, double altitude, double area, double uValue, WindowOrDoor[] windowOrDoor, 
			Obstruction[] obstruction, double groundReflectance,
			String roomName) {
		
		this.orientation = orientation * Math.PI / 180.0;
		setCardinality();
		this.name = this.cardinality + "ern " + roomName + " Wall";
			
		this.altitude = altitude;
		
		this.area = area;		
		this.uValue = uValue;						
		
		this.windowOrDoor = windowOrDoor;
		
		this.obstruction = obstruction;
		
		this.groundReflectance = groundReflectance;
		//this.baseInsolation = baseInsolation;
		//this.peakInsolation = peakInsolation; 
	}
	
	
	// ***************************************************************************************************************
	// *******************************END CONSTRUCTORS SECTION********************************************************
	// ***************************************************************************************************************
	
	
	
	// ***************************************************************************************************************
	// *******************************BEGIN METHODS SECTION***********************************************************
	// ***************************************************************************************************************
	
		
	private void setCardinality() {
		if (orientation < -0.75*Math.PI || orientation > 0.75*Math.PI) {
			cardinality = "North";
		} else if (orientation < -0.25*Math.PI) {
			cardinality = "West";
		} else if (orientation < 0.25*Math.PI) {
			cardinality = "South";
		} else {
			cardinality = "East";
		}
		
	}
	

	
	// ***************************************************************************************************************
	// *******************************END METHODS SECTION*************************************************************
	// ***************************************************************************************************************
}

/* BOX MODEL:
 * 4 walls: 
 * 		-North (1 wall):
 * 		-East (1 wall):
 * 		-South (1 wall): (0, 147.55, default)
 * 		-West (1 wall):
 * 
 * SD2015 CURRENT MODEL:
 * 14 walls:
 * 		-North (3 walls):
 * 		-East (3 walls):
 * 		-South (4 walls): {(0, 147.55, default**** (mostly)),......, }
 * 		-West (4 walls):
*/