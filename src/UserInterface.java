import java.util.*;
import java.io.*;

public class UserInterface {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner keyboard = new Scanner(System.in);
				
		System.out.print("Would you like to create/edit obstruction(s)? (Y|N) ");		
		if (keyboard.next().trim().toUpperCase().charAt(0) == 'Y') {
			obstructionEditor(keyboard);
		}

		System.out.print("\nWould you like to create/edit room(s)? (Y|N) ");
		if (keyboard.next().trim().toUpperCase().charAt(0) == 'Y') {
			char response;
			do {

				roomEditor(keyboard);

				System.out.print("\nWould you like to create/edit another room? (Y|N) ");
				response = keyboard.next().trim().toUpperCase().charAt(0);
			} while (response == 'Y');
		}

	}


	public static void obstructionEditor(Scanner keyboard) throws FileNotFoundException {
		System.out.print("\nWould you like to create a new file of obstructions? (If no, then you will be "
				+ "prompted for the name of an existing file.) (Y|N) ");
		boolean createNewFile = keyboard.next().trim().toUpperCase().charAt(0) == 'Y';
		
		if (!createNewFile) {
			existingObstructionEditor(keyboard);		
		} else {
			newObstructionCreator(keyboard);
		}
	}

	public static void roomEditor(Scanner keyboard) throws FileNotFoundException {
		
		boolean createNewFile = welcome(keyboard);
		
		if (!createNewFile) {
			existingRoomEditor(keyboard);
		} else {
			newRoomCreator(keyboard);
		}
	}


	public static void newObstructionCreator(Scanner keyboard) throws FileNotFoundException {
		
		PrintStream output = new PrintStream(new File(getFileName(keyboard)));
		System.out.print("\nHow many obstructions would you like to create? (include relevant walls) ");
		int nObs = keyboard.nextInt();
		output.println(nObs);
		
		for (int i = 1; i <= nObs; i++) {
			System.out.print("\nObstruction #" + i + ":");
			System.out.print("\nHow many points does Obstruction #" + i + " contain? ");
			int nPoints = keyboard.nextInt();
			output.print(nPoints + " ");
			
			for (int j = 1; j <= nPoints; j++) {
				System.out.print("\nPoint #" + j + " x Coordinate? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nPoint #" + j + " y Coordinate? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nPoint #" + j + " z Coordinate? ");
				output.print(keyboard.nextDouble() + " ");
				
			}
			
			System.out.print("\nWhat is the SHGC value of Obstruction #" + i + "? ");
			output.println(keyboard.nextDouble());
		}
		
	}

	public static void newRoomCreator(Scanner keyboard) throws FileNotFoundException {
		
		PrintStream output = new PrintStream(new File(getFileName(keyboard)));
		
		System.out.print("\nWhat is the name of the room? ");
		output.println(keyboard.next());
		
		System.out.print("\nHow many air changes are there per hour? ");
		output.println(keyboard.nextDouble());
		
		setupInternalHeatGains(output, keyboard);
		
		System.out.print("\nWhat is the ceiling area? ");
		output.println(keyboard.nextDouble());
		
		System.out.print("\nWhat is the ceiling U-Value? ");
		output.println(keyboard.nextDouble());
		
		System.out.print("\nWhat is the floor area? ");
		output.println(keyboard.nextDouble());
		
		System.out.print("\nWhat is the floor U-Value? ");
		output.println(keyboard.nextDouble());
		
		setupWalls(output, keyboard);
		
		System.out.print("\nWhat is the volume? ");
		output.println(keyboard.nextDouble());
	}


	private static void setupWalls(PrintStream output, Scanner keyboard) throws FileNotFoundException {

		System.out.println("\nNow we shall discuss walls. God speed. How many exterior walls does this room have? ");
		int nWalls = keyboard.nextInt();
		output.println(nWalls);
		
		/*System.out.println("\nI'll now ask you some questions about these walls. One question is whether or not it can\n"
				+ "be considered an obstruction. If so, you'll be asked some additional information. God speed.");
		
		boolean needToOpenObstructionFile = false;*/
		
		for (int i = 1; i <= nWalls; i++) {
			System.out.print("\nWall #" + i + ":");
			System.out.print("\nHow many windows does Wall #" + i + " contain? ");
			int nWindows = keyboard.nextInt();
			output.print(nWindows + " ");
			
			for (int j = 1; j <= nWindows; j++) {
				System.out.print("\nWindow #" + j + " x-Coordinate? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nWindow #" + j + " altitude? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nWindow #" + j + " height? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nWindow #" + j + " width? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nWindow #" + j + " shgc? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("\nWindow #" + j + " u-Value? ");
				output.print(keyboard.nextDouble() + " ");			
			}
			
			System.out.print("\nWhat is the orientation of Wall #" + i + "? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\nWhat is the area of Wall #" + i + "? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\nWhat is the u-Value of Wall #" + i + "? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\nWhat is the ground reflectance (albedo) of Wall #" + i + "? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\nBottom-left corner coordinates of Wall #" + i + ": ");

			System.out.print("\nx Coordinate? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\ny Coordinate? ");
			output.print(keyboard.nextDouble() + " ");
			
			System.out.print("\nz Coordinate? ");
			output.println(keyboard.nextDouble() + " ");
			
			/*System.out.print("\nCan Wall #" + i + " be considered an obstruction? (Y|N) ");

			boolean wallIsObstruction = keyboard.next().trim().toUpperCase().charAt(0) == 'Y';
			
			if (wallIsObstruction) {
				System.out.println("\nThis wall needs to be added to the list of obstructions. I need additional info.");
				if (needToOpenObstructionFile) {
					System.out.print("\nWhat is the name of the obstruction file that you would like to add this wall,\n"
							+ "as well as others, to? ");
					String fileName = keyboard.next();
					Scanner fileReader = new Scanner(new File(fileName));
				}
			}*/
		}
		
		
		/*public double orientation;					// angle between normal and due south (west = positive)
		public String cardinality;					// determined below in private method
		public String name;							// name of wall ("Cardinality + "ern" + room.name + "Wall" + number")
		
		public double rawInsolation;
		// scaled by comparing ClimateConsultant with one wall of the box model solar gain results of this program where
		// overhangLength = 0, all windows have SHGC = 1.0, and GROUND_REFLECTANCE = 0.0; units are Btu/hr-ft^2 
			
		public double area;							// ignore presence of walls and doors; will be appropriately modified			
		public double uValue;						
		
		public WindowOrDoor[] windowOrDoor;			// doors are considered windows with SGHC = 0.0;
		
		public Obstruction[] obstruction;			// includes overhangs, left/right protrusions, & other sun-blocking objects
		
		public double groundReflectance;			// (gR); % light reflected off ground (grass = 20.0)
		public double baseInsolation; 				// base insolation/day w/o gR (0%) (ClimateConsultant)
		public double peakInsolation; 				// peak insolation/day with 100% gR (ClimateConsultant)
		public final double groundReflectanceContribution = (peakInsolation - baseInsolation) * groundReflectance / 100.0;*/
	}


	private static void setupInternalHeatGains(PrintStream output, Scanner keyboard) {
		System.out.print("\nNow we shall discuss occupancy & appliance usage in order to determine internal heat gains.\n"
				+ "Would you like to create some preset schedules to make input easier? (Y|N)");
		
		boolean createPresets = keyboard.next().trim().toUpperCase().charAt(0) == 'Y';
		String[] schedule;
		
		if (createPresets) {
			System.out.print("\nHow many different preset schedules would you like to create? ");
			int nSchedules = keyboard.nextInt();
			schedule = new String[nSchedules];
			for (int i = 0; i < nSchedules; i++) {
				schedule[i] = "";
				
				System.out.println("\nSchedule #" + (i+1) + ":");
				
				System.out.print("How many separate time periods are there? ");
				int nPeriods = keyboard.nextInt();
				schedule[i] += nPeriods + " ";
				
				for (int j = 1; j <= nPeriods; j++) {
					System.out.println("\nTime Period #" + j + ":");
					
					System.out.print("Start? ");
					schedule[i] += keyboard.nextDouble() + " ";
					
					System.out.print("End? ");
					schedule[i] += keyboard.nextDouble() + " ";
				}
			}
		} else {
			schedule = null;
		}
		
		System.out.print("\nHow many internal heat gain sources (includes humans and appliances) are there? ");
		int nSources = keyboard.nextInt();
		output.println(nSources);
		
		boolean skipManualOccupanceInput = false;
		if (createPresets) {
			System.out.print("\nWould you like to use a preset schedule for occupancy? (Y|N)");
			boolean usePreset = keyboard.next().trim().toUpperCase().charAt(0) == 'Y';
			if (usePreset) {
				printPresets(schedule);
				System.out.print("\nWhich preset would you like to use? (1-" + schedule.length + ") ");
				int schedIndex = keyboard.nextInt() - 1;
				output.print(schedule[schedIndex]);
				skipManualOccupanceInput = true;
			}
		}
		
		if (!skipManualOccupanceInput) {
			System.out.print("\nHow many separate time periods will this room be occupied? ");
			int nHumanPeriods = keyboard.nextInt();
			output.print(nHumanPeriods + " ");
			
			for (int i = 1; i <= nHumanPeriods; i++) {
				System.out.println("\nTime Period #" + i + ":");
				
				System.out.print("\nStart? ");
				output.print(keyboard.nextDouble() + " ");
				
				System.out.print("End? ");
				output.print(keyboard.nextDouble() + " ");
			}
		}

		System.out.print("\nOn average, how many people will occupy this area during these times? ");
		output.println(keyboard.nextDouble());
	
		for (int i = 1; i < nSources; i++) {
			System.out.print("\nAppliance #" + i + ":");
			boolean skipManualInput = false;
			if (createPresets) {
				System.out.print("\nWould you like to use a preset schedule for this appliance? (Y|N)");
				boolean usePreset = keyboard.next().trim().toUpperCase().charAt(0) == 'Y';
				if (usePreset) {
					printPresets(schedule);
					System.out.print("\nWhich preset would you like to use? (1-" + schedule.length + ") ");
					int schedIndex = keyboard.nextInt() - 1;
					output.print(schedule[schedIndex]);
					skipManualInput = true;
				}
			}
			
			if (!skipManualInput) {
				System.out.print("\nHow many separate time periods will this appliance be used? ");
				int nPeriods = keyboard.nextInt();
				output.print(nPeriods + " ");
				
				for (int index = 1; index <= nPeriods; index++) {
					System.out.println("\nTime Period #" + index + ":");
					
					System.out.print("\nStart? ");
					output.print(keyboard.nextDouble() + " ");
					
					System.out.print("End? ");
					output.print(keyboard.nextDouble() + " ");
				}
			}
			
			System.out.print("\nWhat is the wattage of this appliance? ");
			output.println(keyboard.nextDouble());
		}
		
	}


	public static void printPresets(String[] schedule) {
		
		for (int i = 0; i < schedule.length; i++) {
			
			System.out.print("Schedule #" + (i+1) + ": ");
			
			Scanner lineScanner = new Scanner(schedule[i]);
			lineScanner.next();
			
			System.out.print(lineScanner.next());
			int count = 2;
			
			while (lineScanner.hasNext()) {
				if (count % 2 == 0) {
					System.out.print(" to "); 
				} else {
					System.out.print("; ");
				}
				System.out.print(lineScanner.next());
				
				count++;
			}
			System.out.println();
			
		}
		
		
	}


	public static void existingObstructionEditor(Scanner keyboard) throws FileNotFoundException {
		String fileName = getFileName(keyboard);
		Scanner inputScanner = new Scanner(new File(fileName));
		PrintStream outputTemp = new PrintStream(new File("obstruction_editor.txt"));
		
		outputTemp.println("\nHere are the current obstructions:");
		
		int nObs = inputScanner.nextInt();
		outputTemp.println("\nNumber of obstructions: " + nObs);
		inputScanner.nextLine();
	
		for (int i = 1; i <= nObs; i++) {
			Scanner inputLineScanner = new Scanner(inputScanner.nextLine());
			outputTemp.println("\nObstruction #" + i + ":");
			
			int nPoints = inputLineScanner.nextInt();
			outputTemp.println("\n\tNumber of points: " + nPoints);
			
			for (int j = 1; j <= nPoints; j++) {
				outputTemp.println("\n\t\tPoint #" + j + ":");
				outputTemp.println("\t\t\tx-Coord: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\ty-Coord: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tz-Coord: " + inputLineScanner.nextDouble());
			}
			
			outputTemp.println("\n\tSHGC Value: " + inputLineScanner.nextDouble());
		}
		
		/*if (inputScanner.hasNextLine()) {
			outputTemp.println("\nIGNORE THESE VALUES BELOW. THEY REPRESENT WALLS THAT SHOULD ONLY BE CHANGED WITHIN\n"
					+ "THE ROOM EDITOR. HAVE A GOOD DAY! :0");
			int nWalls = inputScanner.nextInt();
			outputTemp.println(nWalls);
			inputScanner.nextLine();
			
			for (int i = 1; i <= nWalls; i++) {

				Scanner inputLineScanner = new Scanner(inputScanner.nextLine());
				int nPoints = inputScanner.nextInt();
				outputTemp.print(nPoints + " ");
				
				for (int j = 1; j <= nPoints; j++) {

					outputTemp.print(inputLineScanner.nextDouble() + " ");
					
					outputTemp.print(inputLineScanner.nextDouble() + " ");
					
					outputTemp.print(inputLineScanner.nextDouble() + " ");
					
				}
				
				outputTemp.println(inputLineScanner.nextDouble());
			}
		}*/
		
		System.out.println("\nEditing instructions: A new file entitled \"obstruction_editor.txt\" has been created.\n"
				+ "Refresh, open it, & change whatever you'd like. When you're done, save the file & return to Eclipse.\n"
				+ "Refresh the directory and then press Enter within the program. Voila! You'll be done (almost)!\n"
				+ "CAUTION: Make sure that the number of obstructions matches the displayed number. This also applies to\n"
				+ "the number of points for each obstruction. Thanks for your cooperation! :)\n");
		
		keyboard.nextLine();
		
		PrintStream output;
		System.out.print("\nIs this file merely a template (i.e. save changes to a new file)? If no, current file will be"
				+ " overwritten. (Y|N) ");
		
		if (keyboard.next().trim().toUpperCase().charAt(0) == 'Y') {
			output = new PrintStream(new File(getFileName(keyboard)));
		} else {
			output = new PrintStream(new File(fileName));
		}
		
		Scanner inputTemp = new Scanner(new File("obstruction_editor.txt"));
					
		while (!inputTemp.hasNextInt()) {
			inputTemp.next();
		}
		int nObs2 = inputTemp.nextInt();
		output.println(nObs2);
		
		for (int index = 1; index <= nObs2; index++) {
			while (!inputTemp.hasNextInt()) {
				inputTemp.next();
			}
			int nPoints = inputTemp.nextInt();
			output.print(nPoints + " ");
			
			for (int j = 1; j <= nPoints; j++) {
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				
			}
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			
			double troll = inputTemp.nextDouble();
			output.println(troll);
		}
		


	}

	public static void existingRoomEditor(Scanner keyboard) throws FileNotFoundException {

		String fileName = getFileName(keyboard);
		Scanner inputScanner = new Scanner(new File(fileName));
		PrintStream outputTemp = new PrintStream(new File("room_editor.txt"));
		
		outputTemp.println(inputScanner.nextLine());
		
		outputTemp.println("\nAir changes per hour: " + inputScanner.nextLine());
		
		int nHeatSources = inputScanner.nextInt();
		outputTemp.println("\nNumber of heat sources: " + nHeatSources);
		inputScanner.nextLine();
	
		for (int i = 1; i <= nHeatSources; i++) {
			Scanner inputLineScanner = new Scanner(inputScanner.nextLine());
			outputTemp.println("\nHeat Source #" + i + ":");
					
			int nTimeIntervals = inputLineScanner.nextInt();
		
			if (i == 1) {
				outputTemp.println("\n\tNumber of time intervals in schedule of occupancy: " + nTimeIntervals);
			} else {
				outputTemp.println("\n\tNumber of time intervals in schedule: " + nTimeIntervals);
			}
			
			for (int j = 1; j <= nTimeIntervals; j++) {
				outputTemp.println("\n\t\tInterval #" + j + ":");
				outputTemp.println("\t\t\tStart: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tEnd: " + inputLineScanner.nextDouble());

			}
			
			if (i == 1) {
				outputTemp.println("\n\tAverage number of humans occupying room: " + inputLineScanner.nextDouble());
			} else {
				outputTemp.println("\n\tWattage: " + inputLineScanner.nextDouble());
			}
			
		}
		
		outputTemp.println("\nCeiling area: " + inputScanner.nextLine());
		outputTemp.println("\nCeiling U-Value: " + inputScanner.nextLine());
		
		outputTemp.println("\nFloor area: " + inputScanner.nextLine());
		outputTemp.println("\nFloor U-Value: " + inputScanner.nextLine());

		int nWalls = inputScanner.nextInt();
		outputTemp.println("\nNumber of walls: " + nWalls);
		inputScanner.nextLine();
	
		for (int i = 1; i <= nWalls; i++) {
			Scanner inputLineScanner = new Scanner(inputScanner.nextLine());
			outputTemp.println("\nWall #" + i + ":");
					
			int nWindows = inputLineScanner.nextInt();
			outputTemp.println("\n\tNumber of windows: " + nWindows);
			
			
			for (int j = 1; j <= nWindows; j++) {
				outputTemp.println("\n\t\tWindow #" + j + ":");
				outputTemp.println("\t\t\tx-Coordinate: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tAltitude: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tHeight: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tWidth: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tSHGC: " + inputLineScanner.nextDouble());
				outputTemp.println("\t\t\tU-Value: " + inputLineScanner.nextDouble());

			}
			
			outputTemp.println("\n\tOrientation: " + inputLineScanner.nextDouble());
			
			outputTemp.println("\n\tWall area: " + inputLineScanner.nextDouble());

			outputTemp.println("\n\tWall U-Value: " + inputLineScanner.nextDouble());
			
			outputTemp.println("\n\tGround reflectance: " + inputLineScanner.nextDouble());
			
			outputTemp.println("\n\tBottom-left corner coordinates:");
			outputTemp.println("\n\t\tx-Coordinate: " + inputLineScanner.nextDouble());
			outputTemp.println("\t\ty-Coordinate: " + inputLineScanner.nextDouble());
			outputTemp.println("\t\tz-Coordinate: " + inputLineScanner.nextDouble());
			
		}
		
		outputTemp.println("\nVolume: " + inputScanner.nextLine());
		
		System.out.println("\nEditing instructions: A new file entitled \"room_editor.txt\" has been created.\n"
				+ "Refresh, open it, & change whatever you'd like. When you're done, save the file & return to Eclipse.\n"
				+ "Refresh the directory and then press Enter within the program. Voila! You'll be done (almost)!\n"
				+ "CAUTION: Make sure that numbers of objects (i.e. walls, heat sources) match displayed numbers.\n");
		
		keyboard.nextLine();
		
		PrintStream output;
		System.out.print("\nIs this file merely a template (i.e. save changes to a new file)? If no, current file will be"
				+ " overwritten. (Y|N) ");
		
		if (keyboard.next().trim().toUpperCase().charAt(0) == 'Y') {
			output = new PrintStream(new File(getFileName(keyboard)));
		} else {
			output = new PrintStream(new File(fileName));
		}
		
		Scanner inputTemp = new Scanner(new File("room_editor.txt"));
		
		output.println(inputTemp.nextLine());
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		
		output.println(inputTemp.nextDouble());
					
		while (!inputTemp.hasNextInt()) {
			inputTemp.next();
		}
		int nHeatSources2 = inputTemp.nextInt();
		output.println(nHeatSources2);
		
		for (int index = 1; index <= nHeatSources2; index++) {
			while (!inputTemp.hasNextInt()) {
				inputTemp.next();
			}
			int nPeriods = inputTemp.nextInt();
			output.print(nPeriods + " ");
			
			for (int j = 1; j <= nPeriods; j++) {
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");				
			}
			
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.println(inputTemp.nextDouble());
		}
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		output.println(inputTemp.nextDouble());
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		output.println(inputTemp.nextDouble());
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		output.println(inputTemp.nextDouble());
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		output.println(inputTemp.nextDouble());

		while (!inputTemp.hasNextInt()) {
			inputTemp.next();
		}
		int nWalls2 = inputTemp.nextInt();
		output.println(nWalls2);
		
		for (int index = 1; index <= nWalls2; index++) {
			while (!inputTemp.hasNextInt()) {
				inputTemp.next();
			}
			int nWindows = inputTemp.nextInt();
			output.print(nWindows + " ");
			
			for (int j = 1; j <= nWindows; j++) {
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");		
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");	
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");
				while (!inputTemp.hasNextDouble()) {
					inputTemp.next();
				}
				output.print(inputTemp.nextDouble() + " ");	
			}
			
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");	
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");	
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.print(inputTemp.nextDouble() + " ");	
			
			while (!inputTemp.hasNextDouble()) {
				inputTemp.next();
			}
			output.println(inputTemp.nextDouble());
		}
		
		while (!inputTemp.hasNextDouble()) {
			inputTemp.next();
		}
		output.println(inputTemp.nextDouble());		
	}

	public static String getFileName(Scanner keyboard) {
		System.out.print("\nWhat is the name of the file that you would like to open/create? ");
		return keyboard.next();

	}

	public static boolean welcome(Scanner keyboard) {
		System.out.print("Welcome to the room creator/editor! Would you like to create a new room (1) or open an existing "
				+ "one (2)?");
		return keyboard.next().trim().charAt(0) == '1';
	}
	
	// customized overhang-type Obstruction constructor
	/*public Obstruction(double angle, double length, double minX, double maxX, double maxAltitude, double wallLength, 
			boolean canSetLength, boolean p12IsSameHeightAsP34) {
		
		this.angle = angle * Math.PI / 180;
		this.length = length;
		
		double y = maxAltitude - Math.sin(this.angle) * this.length;
		double z = Math.cos(this.angle) * this.length;
		double q;
		
		this.point = new ThreeDimensionalPoint[6];
		
		if (p12IsSameHeightAsP34) {
			q = y;
			
		} else {
			q = maxAltitude;
		}
		
		point[0] = new ThreeDimensionalPoint(minX, q, 0);
		point[1] = new ThreeDimensionalPoint(0, maxAltitude, 0);
		point[2] = new ThreeDimensionalPoint(wallLength, maxAltitude, 0);
		point[3] = new ThreeDimensionalPoint(maxX, q, 0);
		point[4] = new ThreeDimensionalPoint(maxX, y, z);
		point[5] = new ThreeDimensionalPoint(minX, y, z);
				
		this.canSetLength = canSetLength;
		this.p12IsSameHeightAsP34 = p12IsSameHeightAsP34;
	} */

	/*int nObs = inputScanner.nextInt(); (obstruction editor)
	inputScanner.nextLine();
	int newNOBS;
	
	System.out.print("\nFile "+ fileName +" contains "+ nObs +" obstructions. "
			+ "Would you would like to change that? (Y|N) ");
	if (keyboard.next().trim().toUpperCase().charAt(0) == 'Y') {
		System.out.print("\nHow many obstructions would you like there to be? ");
		newNOBS = keyboard.nextInt();
	} else {
		newNOBS = nObs;
	}
	
	String[] obsData = new String[nObs];
	for (int i = 0;; ){}*/
	
	/*public static Obstruction[] getObstructions(Scanner keyboard) throws FileNotFoundException {
		Scanner inputScanner = new Scanner(new File(getFileName(keyboard)));
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
