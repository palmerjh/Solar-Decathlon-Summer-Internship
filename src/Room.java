
public class Room {
	
	public double ach;				// air changes per hour
	
	public InternalHeatGainSource[] heatSource;		// wattages and schedules of operations; includes people
	
	public double ceilingArea;
	public double ceilingUValue;
	
	public double floorArea;
	public double floorUValue;
	
	public String name;
	
	public Wall[] wall;
	
	public double volume;

	public Room() {
		
		ach = 0.3;					// value from super-efficient home in textbook (pp. 251)
		
		// extrapolated from textbook pp. 247, assuming 90 kWh over 8 days of 
		// competition (this excludes car); appliance energy usage = 1620 Btu/hr.; then divided by 2
		// which assumes that this room is a kitchen that is one of the 2 big power
		// houses (the other is living room); 350 is Btu/hr of heat given off by each person times
		// 3 people; 3.412141633 Btu/hr = 1 Watt
		heatSource = new InternalHeatGainSource[2];
		double[][] schedule1 = {{8, 9}, {13, 14}, {18, 19}};
		heatSource[0] = new InternalHeatGainSource(schedule1, (350.0*3)/3.412141633); 	// humans
		heatSource[1] = new InternalHeatGainSource(schedule1, (1620.0/2)/3.412141633);	// all other appliances
		
		ceilingArea = 961;			// assumes 31 x 31 box
		ceilingUValue = 0.021;		// value from super-efficient home in textbook (pp. 251)
		
		floorArea = 961; 
		floorUValue = 0.027;		// value from super-efficient home in textbook (pp. 251)
		
		name = "box";
		
		wall = new Wall[1];
		wall[0] = new Wall();
		
		/*for (int i = 0; i < 4; i++) {
			wall[i] = new Wall();
		}*/
		
		volume = 9610;
	}
	
	public Room(double ach, InternalHeatGainSource[] heatSource, double ceilingArea, double ceilingUValue, double floorArea,
			double floorUValue,	String name, Wall[] wall, double volume) {
	
		//for (int i = 0; i < appliance.length; i++) {
		//	this.appliance[i] = appliance[i];}
		
		this.ach = ach;
		
		this.heatSource = heatSource;
		
		this.ceilingArea = ceilingArea;			// assumes 31 x 31 box
		this.ceilingUValue = ceilingUValue;		// value from super-efficient home in textbook (pp. 251)
		
		this.floorArea = floorArea; 
		this.floorUValue = floorUValue;			// textbook
		
		this.name = name;
		
		this.wall = wall;
		
		this.volume = volume;
		
		//for (int i = 0; i < wall.length; i++) {
		//	this.wall[i] = wall[i];}
	}
	
	public double infiltrationRate() {
		
		return 0.018 * ach * volume;		// equation from textbook pp. 240; units are Btu/hr-F
		
	}
	
	public double internalHeatGain(double time) {
		
		double instantaneousHeatGain = 0.0; // Btu/hr.
		for (int i = 0; i < this.heatSource.length; i++) {
			boolean heatSourceIsActive = timeIsWithinSchedule(time, heatSource[i].schedule);
			if (heatSourceIsActive) {
				instantaneousHeatGain += heatSource[i].wattage;
			}
		}
		
		instantaneousHeatGain *= 3.412141633; // conversion between Watts and Btu/hr
		
		return instantaneousHeatGain;
	}

	private boolean timeIsWithinSchedule(double time, double[][] schedule) {
		for (int i = 0; i < schedule.length; i++) {
			double[] s = schedule[i];
			if (time >= s[0] && time <= s[1]) {
				return true;
			}
		}
		return false;
	}

}
