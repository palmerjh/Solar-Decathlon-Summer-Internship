
import java.util.*;
import java.io.*;

public class HouseEnergyAnalysis  {
	
	/*-------------------------------------------------------------------------------------------------------------------
	 * -----------------------------------INPUT VALUES HERE-------------------------------------------------------------
	 *-------------------------------------------------------------------------------------------------------------------*/
	
	
	public static final int NUMBER_OF_SUBINTERVALS = 360;
	public static final int NUMBER_OF_WINDOW_SQUARES = 10000; // approx. # of pieces of windowOrDoor area to be analyzed
	
	public static final double LATITUDE = 33.6842 * Math.PI / 180.0;	// Irvine's latitude in radians
	public static final double INSIDE_TEMP = 73.5; 						// average goal indoor temperature
	
	public static final double AMBIENT_HIGH = 80;						// 2013 CALTECH model
		public static final double AMBIENT_HIGH_TIME = 16.5;
	public static final double AMBIENT_LOW = 58;
		public static final double AMBIENT_LOW_TIME = 3.9;
	
	
	public static final String OBSTRUCTION_FILE_NAME = "Obstructions.txt";	// name of file containing obstruction info
		
	public static final String[] ROOM_NAME = 	// list all file names of pre-configured rooms to be analyzed
			 {	//"room_test.txt"								
		 "Kitchen.txt", 
		 "Living_Room.txt", 
		 //"Dining_Room.txt",
		 "Foyer.txt",
		 "Bathroom.txt",
		 "Bedroom.txt",
		 "Master_Bedroom.txt"
		};
			
	public static final int DAY_OF_YEAR = 287; 		// 10/14 (midway through competition)
	public static final double SUNRISE_TIME = 7.1;	// http://www.calendar-365.com/calendar/2015/October.html
	
	public static final double AVG_PEAK_INSOLATION = 225;	// average Btu/hr-ft^2 at midday (ClimateConsultant)
	public static final double FACTOR = 0.6105;				// calculated as explained below
	
	
	/*-------------------------------------------------------------------------------------------------------------------
	 * --------------------(IGNORE THIS SECTION) CALCULATED VALUES HERE--------------------------------------------------
	 *-------------------------------------------------------------------------------------------------------------------*/
	
	
	public static final double SOLAR_DECLINATION = 23.5*Math.PI/180.0*Math.sin(2*Math.PI*(DAY_OF_YEAR-81)/365.0);
	public static final double SUNSET_ANGLE = Math.acos(-Math.tan(SOLAR_DECLINATION) * Math.tan(LATITUDE));
	public static final double MIN_AZIMUTH = LATITUDE - SOLAR_DECLINATION; // azimuth angle at solar noon
	public static final double MIDDAY_TIME = SUNRISE_TIME + SUNSET_ANGLE*12/Math.PI; // time of highest sun (midday)
	
	
	public static final double CONSTANT = AVG_PEAK_INSOLATION / (1 - FACTOR);
	public static final double C_VALUE = -Math.pow(SUNSET_ANGLE*12/Math.PI, 2) / Math.log(FACTOR); 
	// C_VALUE refers to c in the I(H) function: I(H) = Io(exp(-H^2/c) - FACTOR), where H = time from midday,
	// Io = AVG_PEAK_INSOLATION / (1 - FACTOR) = CONSTANT
	// FACTOR was calculated in such a way that I(0) = AVG_PEAK_INSOLATION and
	// the integral over the day = average daily total (1592 Btu/ft^2)
	
	public static final double SLOPE = (AMBIENT_HIGH - AMBIENT_LOW) / (AMBIENT_HIGH_TIME - AMBIENT_LOW_TIME);
	
	
	public static void main(String[] args) throws FileNotFoundException  {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.printf("solar declination: %.4f, sunset angle: %.4f, minimum azimuth: %.2f, midday time: %.2f\n", 
				SOLAR_DECLINATION*180.0/Math.PI, SUNSET_ANGLE*180.0/Math.PI, MIN_AZIMUTH*180.0/Math.PI, MIDDAY_TIME);
		System.out.printf("solar declination: %.4f, sunset angle: %.4f, minimum azimuth: %.2f\n", 
				SOLAR_DECLINATION, SUNSET_ANGLE, MIN_AZIMUTH);
		
		//Obstruction[] allObstructions = createObsArray();
		
		Room[] room = new Room[ROOM_NAME.length];
		
		for (int i = 0; i < room.length; i++) {
			room[i] = createRoom(ROOM_NAME[i]);
		}
		
		//Room[] room = {new Room()};
		
		char response;
		do {

			double totalSolarAbsorbtion = 0.0;
			double totalAdditionalGainsAndLosses = 0.0;
			double[] roomAnalysis;
			for (int roomIndex = 0; roomIndex < room.length; roomIndex++) {
		
				roomAnalysis = integration(room[roomIndex]);
				printResults(room[roomIndex].name, roomAnalysis);
				
				totalSolarAbsorbtion += roomAnalysis[0];
				totalAdditionalGainsAndLosses += roomAnalysis[1];
							
			}
			
			System.out.printf("\nTOTAL SOLAR GAIN = %,5.0f Btu/day\n\nNET GAIN = %,5.0f Btu/day\n\n", 
					totalSolarAbsorbtion, totalSolarAbsorbtion + totalAdditionalGainsAndLosses);
			
			//System.out.print("Would you like to run the simulation again? (Y|N) ");
			//response = keyboard.next().trim().toUpperCase().charAt(0);
			response = 'N';
		} while (response == 'Y');		
	}

	public static void printResults(String name, double[] roomAnalysis) {
		
		int hour = (int) roomAnalysis[3];
		int minute = (int) Math.round((roomAnalysis[3] - hour) * 60);
		String time = String.format("%d:%d", hour, minute);
		
		System.out.printf("\n%s ENERGY ANALYSIS:\n\n", name.toUpperCase());
		System.out.printf("SOLAR GAIN = %,5.0f Btu/day\nNET GAIN = %,5.0f Btu/day\n"
				+ "THERE'S A PEAK NET GAIN OF %,5.0f Btu/hr at %s\n\n", roomAnalysis[0], roomAnalysis[0] + roomAnalysis[1],
				roomAnalysis[2], time);
		
	}

	public static Room createRoom(String roomFileName) throws FileNotFoundException { 
				
		Scanner inputScanner = new Scanner(new File(OBSTRUCTION_FILE_NAME));
		int nObs = inputScanner.nextInt();
		inputScanner.nextLine();
		
		// 2D array of all points outlining each obstruction
		ThreeDimensionalPoint[][] obsPoints = new ThreeDimensionalPoint[nObs][];
		double[] shgc = new double[nObs];
		for (int i = 0; i < nObs; i++) {
			
			Scanner lineScanner = new Scanner(inputScanner.nextLine());
			int nPoints = lineScanner.nextInt();
			
			obsPoints[i] = new ThreeDimensionalPoint[nPoints];

			for (int j = 0; j < nPoints; j++) {
			
				obsPoints[i][j] = new ThreeDimensionalPoint(lineScanner.nextDouble(), lineScanner.nextDouble(), 
						lineScanner.nextDouble());
			}
			
			shgc[i] = lineScanner.nextDouble();
			
		}	
		
		File room = new File(roomFileName);
		Scanner roomReader = new Scanner(room);
		
		String roomName = roomReader.nextLine();
		
		double ach = roomReader.nextDouble();
		
		int nHeatSources = roomReader.nextInt();
		
		roomReader.nextLine();
		InternalHeatGainSource[] heatSource = new InternalHeatGainSource[nHeatSources];
		for (int i = 0; i < nHeatSources; i++) {
			
			Scanner lineScanner = new Scanner(roomReader.nextLine());
			int nPeriods = lineScanner.nextInt();
			
			double[][] schedule = new double[nPeriods][2];
			for (int j = 0; j < nPeriods; j++) {
				
				schedule[j][0] = lineScanner.nextDouble();
				schedule[j][1] = lineScanner.nextDouble();
			}
			
			double wattage;
			if (i == 0) {       // i == 0 refers to internal heat gains due to people
				wattage = 350.0 / 3.412141633 * lineScanner.nextDouble(); // numbers cited in InternalHeatGainSource.java
			} else {
				wattage = lineScanner.nextDouble();
			}
			
			heatSource[i] = new InternalHeatGainSource(schedule, wattage);
		}
		
		double ceilingArea = roomReader.nextDouble();
		double ceilingUValue = roomReader.nextDouble();
		double floorArea = roomReader.nextDouble();
		double floorUValue = roomReader.nextDouble();
				
		int nWalls = roomReader.nextInt();
		Wall[] wall = new Wall[nWalls];
		
		roomReader.nextLine();
		for (int i = 0; i < nWalls; i++) {
			
			Scanner lineScanner = new Scanner(roomReader.nextLine());
			int nWindows = lineScanner.nextInt();
			
			WindowOrDoor[] win = new WindowOrDoor[nWindows];

			for (int j = 0; j < nWindows; j++) {
			
				win[j] = new WindowOrDoor(lineScanner.nextDouble(), lineScanner.nextDouble(), lineScanner.nextDouble(), 
						lineScanner.nextDouble(), lineScanner.nextDouble(), lineScanner.nextDouble());
			}
			
			double orientation = lineScanner.nextDouble();
			double area = lineScanner.nextDouble();
			double uValue = lineScanner.nextDouble();
			double groundReflectance = lineScanner.nextDouble();
			
			ThreeDimensionalPoint corner = new ThreeDimensionalPoint(lineScanner.nextDouble(), 
					lineScanner.nextDouble(), lineScanner.nextDouble());
			
			double altitude = corner.z;
			
			// converts orientations (angles with respect to -y-axis; counterclockwise = neg.) to
			// to angles with respect to +x-axis; counterclockwise = pos.
			double angle = -Math.PI / 2.0 * (orientation / 90 + 1);
			
			// establishes normalized basis for plane of wall
			ThreeDimensionalPoint e3 = new ThreeDimensionalPoint(Math.cos(angle), Math.sin(angle), 0); // new x-axis
			ThreeDimensionalPoint e1 = new ThreeDimensionalPoint(-e3.y, e3.x, 0); // new z-axis
			ThreeDimensionalPoint e2 = new ThreeDimensionalPoint(0, 0, 1); // new y-axis 
			
			double[][] mat =	{{e1.x, e2.x, e3.x},
								 {e1.y, e2.y, e3.y},
								 {e1.z, e2.z, e3.z}};
			
			// calculates linear transformation that takes points in standard basis to points in wall basis
			double[][] linTrans = inverse(mat);
			
			Obstruction[] obs = new Obstruction[nObs];
			for (int obsIndex = 0; obsIndex < nObs; obsIndex++) {
				for (int pointIndex = 0; pointIndex < obsPoints[obsIndex].length; pointIndex++) {
					double[][] relPoint = 	{{obsPoints[obsIndex][pointIndex].x - corner.x},
											 {obsPoints[obsIndex][pointIndex].y - corner.y},
											 {obsPoints[obsIndex][pointIndex].z - corner.z}};
					
					double[][] adjPoint = matrixMult(linTrans, relPoint);
					
					obsPoints[obsIndex][pointIndex].x = adjPoint[0][0];
					obsPoints[obsIndex][pointIndex].y = adjPoint[1][0];
					obsPoints[obsIndex][pointIndex].z = adjPoint[2][0];
				}
				
				// creates obstruction objects with adjusted points
				obs[obsIndex] = new Obstruction(obsPoints[obsIndex], shgc[obsIndex]);
			}
			
			wall[i] = new Wall(orientation, altitude, area, uValue, win, obs, groundReflectance, roomName);
		}
		
		double volume = roomReader.nextDouble();

		return new Room(ach, heatSource, ceilingArea, ceilingUValue, floorArea, floorUValue, roomName, wall, volume);
		
		/*public Room(double ach, InternalHeatGainSource[] heatSource, double ceilingArea, double ceilingUValue, double floorArea,
		double floorUValue,	String name, Wall[] wall, double volume) {*/
	}

	public static double[] integration(Room room) {
		
		double solarRaysGain = 0.0;
		double otherGainOrLoss = 0.0;
		
		double subintervalWidth = 2.0*Math.PI / NUMBER_OF_SUBINTERVALS;
		int subinterval = 1;
		
		double maxNetGain = 0.0;
		double timeOfNetMaxGain = 0.0;
		double instantaneousNetGain;
		
		// NOTE: hourAngle = hour * PI / 12
		double initialHourAngle = subintervalWidth / 2.0 - MIDDAY_TIME * Math.PI/12;
		
		// integration over all hourAngles in day
		for (double hourAngle = initialHourAngle; subinterval <= NUMBER_OF_SUBINTERVALS; 
				hourAngle += subintervalWidth, subinterval++) {
			
			double time = MIDDAY_TIME + hourAngle * 12/Math.PI;
			double tempGradient = getTemp(time) - INSIDE_TEMP;
			
			double solar = 0.0;
			double other = 0.0;
			
			// integration over all walls around room	
			for (int wallIndex = 0; wallIndex < room.wall.length; wallIndex++) {
				Wall wall = room.wall[wallIndex];		
				boolean skipReflectAnalysis = wall.groundReflectance == 0.0;
				
				// hour angle at which the sun rises OR (WALL[i].orientation - 90) degrees if sun rises at 
				// an hour angle smaller than (WALL[i].orientation - 90)
				double lowerLimit = Math.max(-SUNSET_ANGLE, wall.orientation - Math.PI / 2.0);
				
				// hour angle at which the sun sets OR (90 + WALL[i].orientation) degrees if sun sets at 
				// an hour angle greater than (90 + WALL[i].orientation)
				double upperLimit = Math.min(SUNSET_ANGLE, wall.orientation + Math.PI / 2.0);				
				// the limits of integration for solarRaysGain are lowerLimit and upperLimit respectively
										
				double totalWindowArea = 0.0;
				double totalAreaTimesUValue = 0.0;
				double windowArea;
				
				for (int i = 0; i < wall.windowOrDoor.length; i++) {
					windowArea = wall.windowOrDoor[i].height * wall.windowOrDoor[i].width;
					totalWindowArea += windowArea;
					totalAreaTimesUValue += windowArea * wall.windowOrDoor[i].uValue;
				}				
				double netWallArea = wall.area - totalWindowArea;
				
				totalAreaTimesUValue += netWallArea * wall.uValue;
						
				double instantaneousThermalLoss = (tempGradient) * totalAreaTimesUValue;
				other += instantaneousThermalLoss;
		
				// adds up all pieces of window * SHGC NOT blocked by obstruction
				if (hourAngle >= lowerLimit && hourAngle <= upperLimit) {
					double azimuth = Math.acos(Math.cos(hourAngle)*Math.cos(SOLAR_DECLINATION)*Math.cos(LATITUDE) + 
							Math.sin(SOLAR_DECLINATION)*Math.sin(LATITUDE));
					
					double angle = hourAngle - wall.orientation; // angle between hour angle and normal
					
					double areaTimesSHGC = findAreaTimesSHGC(angle, azimuth, wall, skipReflectAnalysis);
					
					double insolation = getInsolation(time) * Math.sin(azimuth) * Math.cos(angle);
					
					solar += insolation * areaTimesSHGC;
					
				}
				
			}
			
			other += ((room.ceilingArea*room.ceilingUValue + room.floorArea*room.floorUValue + room.infiltrationRate())
					* (tempGradient) + room.internalHeatGain(time));
			
			//System.out.printf("%.2f\t%,5.0f\t%.2f\n", time, other, other*subintervalWidth * 12/Math.PI);
			
			instantaneousNetGain = solar + other;
			
			if (instantaneousNetGain > maxNetGain) {
				maxNetGain = instantaneousNetGain;
				timeOfNetMaxGain = time;
			}
				
			solarRaysGain += solar;
			otherGainOrLoss += other;
							
		}
		
		solarRaysGain *= subintervalWidth * 12/Math.PI;
		otherGainOrLoss *= subintervalWidth * 12/Math.PI;
		
		double[] roomResults = {solarRaysGain, otherGainOrLoss, maxNetGain, timeOfNetMaxGain};
			
		return roomResults;
	}

	public static double getTemp(double time) {
		
		// absolute value function for temperature is from Caltech 2013 model
		if (time >= AMBIENT_LOW_TIME) {
			return -SLOPE * Math.abs(time - AMBIENT_HIGH_TIME) + AMBIENT_HIGH;
		}

		return SLOPE * (AMBIENT_LOW_TIME - time) + AMBIENT_LOW;
	}

	public static double getInsolation(double time) {
		
		// function and constants determined from SolarFunctionFinder.java that tried to fit data from
		// ClimateConsultant
		double timeFromMidday = time - MIDDAY_TIME;
		double insolation = CONSTANT * (Math.pow(Math.E, -Math.pow(timeFromMidday, 2) / C_VALUE) - FACTOR); 
		//System.out.printf("%.2f\t%,5.0f\n", time, insolation);
		return insolation;
	}

	public static double findAreaTimesSHGC(double angle, double azimuth, Wall wall, boolean skipReflectAnalysis) {
		
		Obstruction[] obstruction = wall.obstruction;
		
		// calculates vector pointing towards Sun (****in terms of the basis of the wall plane)
		ThreeDimensionalPoint solarVectorDirect = new ThreeDimensionalPoint();
		solarVectorDirect.x = -Math.sin(azimuth) * Math.sin(angle);
		solarVectorDirect.y = Math.cos(azimuth);
		solarVectorDirect.z = Math.sin(azimuth) * Math.cos(angle);
		
		// method finds obstructions that have a possibility of blocking rays;
		// these are obstructions whose normals are not parallel with the solar vectors
		Obstruction[] relevantObsDirect = findNonParallelObs(solarVectorDirect, obstruction);
				
		ThreeDimensionalPoint solarVectorReflected = new ThreeDimensionalPoint();
		
		Obstruction[] relevantObsReflected = null;
		
		if (!skipReflectAnalysis && solarVectorDirect.y > 0.00001) {
			
			// vector pointed towards Sun reflected over y-axis
			solarVectorReflected.x = solarVectorDirect.x;
			solarVectorReflected.y = -solarVectorDirect.y;
			solarVectorReflected.z = solarVectorDirect.z;
			
			// method finds obstructions that have a possibility of blocking rays refelcted off ground;
			// these are obstructions whose normals are not parallel with the reflected solar vector
			relevantObsReflected = findNonParallelObs(solarVectorReflected, obstruction);
		}
		
		double totalAreaTimesSHGC = 0.0;
		// iterates over every window/door in wall
		for (int windowIndex = 0; windowIndex < wall.windowOrDoor.length; windowIndex++) {
			
			// optimizes NUMBER_OF_WINDOW_SQUARES request while simultaneously getting nX/nY as close to 
			// height/width as possible so that almost perfect squares are made out of the windows/doors
			WindowOrDoor windowOrDore = wall.windowOrDoor[windowIndex];
			double b = Math.sqrt(NUMBER_OF_WINDOW_SQUARES * windowOrDore.height / windowOrDore.width);
			double a = NUMBER_OF_WINDOW_SQUARES / b;
			
			int nX = (int) Math.round(a);
			int nY = (int) Math.round(b);
			
			//double windowAreaTimesSHGC = 0.0;
			double intervalWidth = windowOrDore.width / nX;
			double intervalHeight = windowOrDore.height / nY;
			
			// iterates over each piece of window area, shooting out a vector towards the sun or 
			// one that will reflect off ground towards Sun to see if it is obstructed
			int column = 1;
			for (double y = windowOrDore.altitude + intervalHeight / 2.0; column <= nY; y += intervalHeight, column++) {
				int row = 1;
				for (double x = windowOrDore.xCoord + intervalWidth / 2.0; row <= nX; x += intervalWidth, row++) {
					ThreeDimensionalPoint start = new ThreeDimensionalPoint(x, y, 0);
					
					boolean[] thereIsDirectObstruction = thereIsObs(start, solarVectorDirect, relevantObsDirect, -1.0);
					
					double compositeSHGCDirect = 1.0;
					for (int i = 0; i < relevantObsDirect.length; i++) {
						if (thereIsDirectObstruction[i]) {
							compositeSHGCDirect *= relevantObsDirect[i].shgc; // usually 0
						}
					}
					
					double compositeSHGCReflected = 1.0;
					
					if (!skipReflectAnalysis && solarVectorDirect.y > 0.00001) {
						
						double maxT = -(wall.altitude + start.y) / solarVectorReflected.y;
						
						// tests for obstruction on the way from wall to ground
						boolean[] thereIsReflectedObstruction1 = thereIsObs(start, solarVectorReflected, 
								relevantObsReflected, maxT);

						for (int i = 0; i < relevantObsReflected.length; i++) {
							if (thereIsReflectedObstruction1[i]) {
								compositeSHGCReflected *= relevantObsReflected[i].shgc; // usually 0
							}
						}
						
						if (compositeSHGCReflected > 0.00001) {
							
							// tests for obstruction after reflection off ground
							ThreeDimensionalPoint groundPoint = new ThreeDimensionalPoint();
							groundPoint.x = start.x + maxT * solarVectorReflected.x;
							groundPoint.y = start.y + maxT * solarVectorReflected.y;
							groundPoint.z = start.z + maxT * solarVectorReflected.z;
							
							boolean[] thereIsReflectedObstruction2 = thereIsObs(groundPoint, solarVectorDirect, 
									relevantObsDirect, -1.0);

							for (int i = 0; i < relevantObsDirect.length; i++) {
								if (thereIsReflectedObstruction2[i]) {
									compositeSHGCReflected *= relevantObsDirect[i].shgc;
								}
							}
						}
						
					}
					
					totalAreaTimesSHGC += intervalHeight * intervalWidth * (compositeSHGCDirect + 
							compositeSHGCReflected * wall.groundReflectance) * wall.windowOrDoor[windowIndex].shgc;	
				}
			}
		}
		
		//System.out.println(totalAreaTimesSHGC);
		
		return totalAreaTimesSHGC;
	}

	public static Obstruction[] findNonParallelObs(ThreeDimensionalPoint v, Obstruction[] o) {
		
		int nObs = 0;
		for (int oIndex = 0; oIndex < o.length; oIndex++) {
			// parallel test
			if (Math.abs(Math.abs(dot(v, o[oIndex].normal)) - o[oIndex].normal.magnitude() * v.magnitude()) > 0.00001) {
				nObs++;
			}
		}
		
		Obstruction[] relevantObs = new Obstruction[nObs];
		
		int index = 0;
		for (int oIndex = 0; oIndex < o.length; oIndex++) {
			// parallel test
			if (Math.abs(Math.abs(dot(v, o[oIndex].normal)) - o[oIndex].normal.magnitude() * v.magnitude()) > 0.00001) {
				relevantObs[index] = o[oIndex];
				index++;
			}
		}
		return relevantObs;
	}

	// returns whether or not v passes through each obstruction of the array; if maxT = -1, v = ray; otherwise, v = 
	// line segment with maximum parameter value of maxT
	// maxT = -1 refers to solar vectors with no limit on how far they can travel
	// maxT >= 0 refers to reflected solar vectors whose distance is limited by the ground
	public static boolean[] thereIsObs(ThreeDimensionalPoint vStart, ThreeDimensionalPoint v,
			Obstruction[] obstruction, double maxT) {
		
		boolean[] thereIsObstruction = new boolean[obstruction.length];
		
		for (int oIndex = 0; oIndex < obstruction.length; oIndex++) {
			Obstruction o = obstruction[oIndex];
			double vectorParam = (o.shift - dot(o.normal, vStart)) / 
					dot(v, o.normal);
			
			//System.out.println(vectorParam);
			//System.out.println();
			
			// tests to ensure that vector v is hitting obstruction plane between exterior of wall and Sun
			// and that either vector is ray or that the vector parameter falls within the maximum 
			// parameter for reflected rays where the ground must be taken into account
			// if this is true, 3D point in space is created marking 
			if (vectorParam >= 0 && (maxT == -1.0 || vectorParam <= maxT)) {
				double[][] threeDPlanePoint = {{vStart.x + vectorParam * v.x - obstruction[oIndex].point[0].x},
											   {vStart.y + vectorParam * v.y - obstruction[oIndex].point[0].y},
											   {vStart.z + vectorParam * v.z - obstruction[oIndex].point[0].z}};
				
				
				/*System.out.println(threeDPlanePoint[0][0]);
				System.out.println(threeDPlanePoint[1][0]);
				System.out.println(threeDPlanePoint[2][0]);*/
				
				//System.out.println();
				
				
				double[][] temp = matrixMult(o.linearTransformation, threeDPlanePoint);
				Points planePoint = new Points(temp[0][0], temp[1][0]);
				
				//System.out.println(planePoint.x);
				//System.out.println(planePoint.y);
				
				//System.out.println();
				
				thereIsObstruction[oIndex] = pointIsInside(planePoint, o.lineSegment);
			} else {
				thereIsObstruction[oIndex] = false;
			}
						
		}
		return thereIsObstruction;
	}

	// method returns whether or not a point is within a plane bounded by the given array of LineSegments
	public static boolean pointIsInside(Points point, LineSegment[] lS) {
		
		Points analysisRay = new Points(0, 1);	// arbitrary direction for analysis; ray starts at Points point
		double minDistance = legitHitDistance(analysisRay, point, lS[0].lineVector, lS[0].lineStart);
		LineSegment closestLS = lS[0];
		
		double currentDistance;
		
		for (int i = 1; i < lS.length; i++) {
			
			currentDistance = legitHitDistance(analysisRay, point, lS[i].lineVector, lS[i].lineStart);
			
			//System.out.println(currentDistance);
			
			if (currentDistance != -1.0) {
				if (minDistance == -1.0) {
					minDistance = currentDistance;
					closestLS = lS[i];
				} else if (currentDistance < minDistance) {
					minDistance = currentDistance;
					closestLS = lS[i];
				}
			}
			
			/*System.out.printf("currentDistance:\t%.2f\nminDistance:\t%.2f\nclosestLS:\t(%.2f, %.2f)\n\n",
					currentDistance, minDistance, closestLS.lineStart.x, closestLS.lineStart.y);*/
		}
		
		// test that no intersection was made with any LS 
		if (minDistance == -1.0) {
			return false;
		}
		
		double dotProduct = dot(closestLS.normalVector, analysisRay);
		//System.out.println(dotProduct);
		
		// test that the closest LS has orientation directed in direction of intersection of vector with plane
		if (dotProduct < 0) {
			return false;
		}
		
		return true;
	}
	
	// method returns distance between legit ray w hits of lineSegment v and wStart; returns -1 for illegit hits
	public static double legitHitDistance(Points w, Points wStart, Points v, Points vStart ) {
			
		/*System.out.println(Math.abs(dot(w, v)) - w.magnitude() * v.magnitude());
		System.out.println(dot(w, v));
		System.out.println(w.x);
		System.out.println(v.x);*/
		
		// parallel test
		if (Math.abs(Math.abs(dot(w, v)) - w.magnitude() * v.magnitude()) < 0.00001) {
			return -1.0;
		}			
		double lineSegParam;
		
		// algorithm to determine where ray w intersects the vector v representing the 
		// direction an LS in the obstruction in terms of parameter of v
		if (Math.abs(w.y) > 0.00001) {
			lineSegParam = (wStart.x - vStart.x + w.x / w.y * (vStart.y - wStart.y)) / (v.x - w.x*v.y / w.y);
			
		} else {
			lineSegParam = (wStart.y - vStart.y) / v.y;	
		}	
		
		// if param is within [0, 1], then that means that the ray w hits the LS! if not, it doesn't 
		if (lineSegParam < 0 || lineSegParam > 1) {
			return -1.0;
	
		}
			
		Points connection = new Points();
		connection.x = lineSegParam*v.x + vStart.x - wStart.x;
		connection.y = lineSegParam*v.y + vStart.y - wStart.y;
		
		//System.out.printf("connection:\t(%.2f, %.2f)\nmagnitude: %.2f\n\n",
		//		connection.x, connection.y, connection.magnitude());
		
		// makes sure that the connection with LS is actually in direction of ray w (i.e w param > 0)
		if (dot(connection, w) < 0) {
			return -1.0;
		}
					
		return connection.magnitude();
	}

	public static double dot(ThreeDimensionalPoint v1, ThreeDimensionalPoint v2) {	// returns dot product of v1 and v2
			
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
		
	}
		
	public static double dot(Points v1, Points v2) {		// returns dot product of v1 and v2
			
		return v1.x*v2.x + v1.y*v2.y;
		
	}
	
	public static double[][] transpose(double[][] matrix) { 
		double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
		for (int i=0;i<matrix.length;i++) { 
			for (int j=0;j<matrix[0].length;j++) { 
				transposedMatrix[j][i] = matrix[i][j];
				
			} 
			
		} 
		return transposedMatrix; 
		
	}

	public static double determinant(double[][] matrix) { 
	
		double sum = 0.0;
		if (matrix.length==1) {
			return matrix[0][0];
		}
					
		if (matrix.length!=matrix[0].length) {
			throw new IllegalArgumentException("matrix is not square");
		}
		
		if (matrix.length==2) { 
			return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
			
		} 
		
		for (int i=0; i<matrix[0].length; i++) { 
			double det = determinant(createSubMatrix(matrix, 0, i));

			sum += changeSign(i) * matrix[0][i] * det; 
			
		} 
		
		
		return sum; 
		
	}
	
	public static double[][] createSubMatrix(double[][] matrix, int excluding_row, int excluding_col) {
		double[][]  mat = new double[matrix.length - 1][matrix[0].length - 1];
		int r = -1; 
		for (int i=0;i<matrix.length;i++) { 
			if (i==excluding_row) {
				continue; 
			}
			
			r++; 
			int c = -1; 
			for (int j=0;j<matrix[0].length;j++) { 
				if (j==excluding_col) {
					continue; 
				}
				
				mat[r][++c] = matrix[i][j];
				
			} 
			
		} 
		
		return mat; 
		
	}

	public static int changeSign(int i) {
		if (i%2==0) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public static double[][] cofactor(double[][] matrix) {
		double[][] mat = new double[matrix.length][matrix[0].length]; 
		for (int i=0;i<matrix.length;i++) { 
			for (int j=0; j<matrix[0].length;j++) { 
				mat[i][j] = changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)); 
				
			} 
			
		} 
		
		return mat; 
		
	}
	
	public static double[][] inverse(double[][] matrix) {
		
		double[][] mat = transpose(cofactor(matrix));
		
		double constant = 1.0/determinant(matrix);
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] *= constant;
			}
		}
		
		return mat; 
		
	}
	
	public static double[][] matrixMult (double[][] m1, double[][] m2) {
		
		int nRows = m1.length; int nCols = m2[0].length;
		double[][] answer = new double[nRows][nCols];
		m2 = transpose(m2);
		
		for (int r = 0; r < nRows; r++) {
			for (int c = 0; c < nCols; c++) {
				
				answer[r][c] = 0.0;
				for (int entry = 0; entry < m1[0].length; entry++) {
					answer[r][c] += m1[r][entry] * m2[c][entry];
				}
			}
		}
		
		return answer;
	}
	public static double getOverhangLength(Scanner keyboard) {
		
		/*for (int roomIndex = 0; roomIndex < room.length; roomIndex++) {
			for (int wallIndex = 0; wallIndex < room[roomIndex].wall.length; wallIndex++) {
				for (int obstructionIndex = 0; obstructionIndex < room[roomIndex].wall[wallIndex].obstruction.length;
						obstructionIndex++) {
					if (room[roomIndex].wall[wallIndex].obstruction[obstructionIndex].canSetLength) {
						//room[roomIndex].wall[wallIndex].obstruction[obstructionIndex].
						//setLength(getOverhangLength(keyboard));
					}	
				}
			}
		}	*/
		
		
		System.out.print("\nOverhang length? ");
		double overhangLength = keyboard.nextDouble();

		return overhangLength;
	}
	
	/*public static double getNightlyThermalLoss() {
		
		//public static final double AVERAGE_TEMP = 65; 	// avg. temp. for Irvine during October (ClimateConsultant)
		//public static final double NIGHTLY_THERMAL_LOSS = getNightlyThermalLoss(); // units are F-hr/ft^2
		
		double midnightTemp = AMBIENT_LOW_TIME * SLOPE + AMBIENT_LOW;
		double thermalLoss = (midnightTemp + AMBIENT_LOW) / 2.0 * AMBIENT_LOW_TIME;
		thermalLoss += ((SUNRISE_TIME - AMBIENT_LOW_TIME)*SLOPE + 2*AMBIENT_LOW) / 2.0 * (SUNRISE_TIME - AMBIENT_LOW_TIME);
		double sunsetTime = SUNRISE_TIME + 2*12/Math.PI * SUNSET_ANGLE;
		thermalLoss += ((24.0 - sunsetTime)*SLOPE + 2*midnightTemp) / 2.0 * (24.0 - sunsetTime);
		return thermalLoss;
	}*/
	
	
	/*double justThermalLossTime = 2*Math.PI - (upperLimit - lowerLimit);
		
	double lowerLimitTime = MIN_AZIMUTH_TIME + lowerLimit*12/Math.PI;
	double upperLimitTime = MIN_AZIMUTH_TIME + upperLimit*12/Math.PI;
	double additionalThermalLoss;
	if (lowerLimitTime <= AMBIENT_HIGH_TIME) {
		double dX = lowerLimitTime - SUNRISE_TIME;
		additionalThermalLoss = 
	}
		
		
		double netGain = solarGain - thermalLoss;
		double netRatio = netGain / (unshadedComparison - thermalLoss);
					
		
	*/	
	
	/*public static void hourlyShadingAnalysis(Scanner keyboard, double firstLimit) {
	
	if (userWantsHourlyShadingAnalyis(keyboard) && userWantsCustomShadingRange(keyboard)) {
		double[] shadingRange = getRange(keyboard);
	}
	
	
}

public static double[] getRange(Scanner keyboard) {
	System.out.print("\nWhat is the lower limit of the percent shaded range? ");
	System.out.print("\nWhat is the upper limit of the percent shaded range? ");
	double[] range = {keyboard.nextDouble(), keyboard.nextDouble()};
	return range;
}

public static boolean userWantsHourlyShadingAnalyis(Scanner keyboard) {
	System.out.print("\nWould you like to run an optional hourly shading analysis? (Y|N) ");
	char response = keyboard.next().trim().toUpperCase().charAt(0);
	if (response == 'Y') {
		return true;
	}
	return false;
}

public static boolean userWantsCustomShadingRange(Scanner keyboard) {
	System.out.print("\nWould you like to customize a range for the shading analysis (i.e. this entails"
			+ "\nfinding the times of the day during which the window is shaded between two particular\n"
			+ "percentages)? (Y|N) ");
	char response = keyboard.next().trim().toUpperCase().charAt(0);
	if (response == 'Y') {
		return true;
	}
	return false;
}

public static void energyAnalysis(double solarGain, Scanner keyboard) {
	
	double totalWindowArea = 0.0;
	for (int i = 0; i < WINDOW.length; i++) {
		totalWindowArea += WINDOW[i].height * WINDOW[i].width;
	}
	double groundReflectanceContribution = (PEAK - BASE) * GROUND_REFLECTANCE / 100.0 * totalWindowArea;
	solarGain += groundReflectanceContribution;
	double unshadedComparison = (integration(0) + groundReflectanceContribution);
	double grossRatio = solarGain / unshadedComparison;
	
	double totalWindowAreaTimesUValue = 0.0;
	for (int i = 0; i < WINDOW.length; i++) {
		totalWindowAreaTimesUValue += WINDOW[i].height * WINDOW[i].width * WINDOW[i].uValue;
	}
	double thermalLoss = (INSIDE_TEMP - AVERAGE_TEMP) * 24 * totalWindowAreaTimesUValue;
	double netGain = solarGain - thermalLoss;
	double netRatio = netGain / (unshadedComparison - thermalLoss);
				
	System.out.printf("\nSOLAR GAIN = %,5.0f Btu/day\n\t-Ratio to unshaded window: %.3f\n\n"
			+ "NET GAIN = %,5.0f Btu/day\n\t-Ratio to unshaded window: %.3f\n\nWould you "
			+ "like to run the simulation again? (Y|N) ", solarGain, grossRatio, netGain, 
			netRatio); 
	
} */
	
	
	/*public static Obstruction[] createObsArray() throws FileNotFoundException {

		Scanner inputScanner = new Scanner(new File(OBSTRUCTION_FILE_NAME));
		int nObs = inputScanner.nextInt();
		inputScanner.nextLine();
		
		Obstruction[] obs = new Obstruction[nObs];
		for (int i = 0; i < nObs; i++) {
			
			Scanner lineScanner = new Scanner(inputScanner.nextLine());
			int nPoints = lineScanner.nextInt();
			
			ThreeDimensionalPoint[] point = new ThreeDimensionalPoint[nPoints];

			for (int j = 0; j < nPoints; j++) {
			
				point[j] = new ThreeDimensionalPoint(lineScanner.nextDouble(), lineScanner.nextDouble(), 
						lineScanner.nextDouble());
			}
			
			double shgc = lineScanner.nextDouble();
			
			obs[i] = new Obstruction(point, shgc);
		}
		
		return obs;
	}*/

}
