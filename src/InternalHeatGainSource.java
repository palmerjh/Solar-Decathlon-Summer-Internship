
public class InternalHeatGainSource {
	
	public double[][] schedule;			// schedule of usage: (i.e. 8 to 9 am and 6:30 to 8 pm would be {{8.0, 9.0}, 
										// {18.5, 20.0}}
	public double wattage;

	public InternalHeatGainSource() {
		schedule = new double[1][2];
		schedule[0][0] = 0.0;
		schedule[0][1] = 23.99;
		
		wattage = 10.0;
	}
	
	public InternalHeatGainSource(double[][] schedule, double wattage) {
		this.schedule = schedule;
		this.wattage = wattage;
	}

}
