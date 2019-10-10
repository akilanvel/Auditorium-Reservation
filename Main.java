package Tickets;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		String input = "";

		// Get the name of the file containing the auditorium
		System.out.println("Which auditorium would you like to read from?");
		String numAuditorium = scanner.nextLine();

		// Initialize Scanner object through which we will read from the requested
		// auditorium
		Scanner fileReader = new Scanner(System.in);

		// Open the file, but make sure the file exists
		// Keep prompting the user for the auditorium file until they enter a valid one
		// if the original was invalid
		boolean fileFound = false;
		while (!fileFound) {
			try {
				File file = new File(numAuditorium + ".txt");
				fileReader = new Scanner(file);
				fileFound = true;
			} catch (FileNotFoundException e) {
				System.out.println("The entered auditorium was not found");
				System.out.println("Which auditorium would you like to read from?");
				numAuditorium = scanner.nextLine();
			}
		}

		// Create a 2D char array to house the auditorium
		char[][] auditorium = readInitialAuditorium(fileReader);

		printUser(auditorium);

		// Start the program loop
		do {
			// Get the user's decision
			System.out.println("1. Reserve");
			System.out.println("2. Exit");
			input = scanner.nextLine();

			// Declare variables that the user will enter for reservation
			int rowNumber = 0;
			char startingSeatLetter = '0';
			int numAdult = 0;
			int numChild = 0;
			int numSenior = 0;

			if (!input.equals("Reserve") && !input.equals("Exit")) { // If the user enters an invalid menu option, tell
																		// them it was invalid
				System.out.println("Invalid menu option");
			} else if (input.equals("Reserve")) { // If the user decides to reserve a seat
				// Get the details of the seat and make sure it is valid
				System.out.println("Please enter the row number");
				rowNumber = scanner.nextInt();
				scanner.nextLine();
				while (rowNumber < 1 || rowNumber > auditorium.length) {
					System.out.println("Invalid row");
					rowNumber = scanner.nextInt();
					scanner.nextLine();
				}
				System.out.println("Please enter the starting seat letter");
				startingSeatLetter = scanner.next().charAt(0);
				int columnNumber = (int) startingSeatLetter - 64;
				scanner.nextLine();
				while (columnNumber < 1 || columnNumber > auditorium[0].length) {
					System.out.println("Invalid seat");
					startingSeatLetter = scanner.next().charAt(0);
					columnNumber = (int) startingSeatLetter - 64;
				}
				System.out.println("Please enter the number of adult tickets");
				numAdult = scanner.nextInt();
				scanner.nextLine();
				while (numAdult < 0 || numAdult > auditorium[0].length) {
					System.out.println("Invalid adults");
					numAdult = scanner.nextInt();
					scanner.nextLine();
				}
				System.out.println("Please enter the number of child tickets");
				numChild = scanner.nextInt();
				scanner.nextLine();
				while (numChild < 0 || numChild > auditorium[0].length) {
					System.out.println("Invalid children");
					numChild = scanner.nextInt();
					scanner.nextLine();
				}
				System.out.println("Please enter the number of senior tickets");
				numSenior = scanner.nextInt();
				scanner.nextLine();
				while (numSenior < 0 || numSenior > auditorium[0].length) {
					System.out.println("Invalid senior");
					numSenior = scanner.nextInt();
					scanner.nextLine();
				}

				// To account that arrays have starting index 0
				rowNumber--;
				columnNumber--;

				// Check if the requested seats are available
				boolean available = true;
				int totalSeats = numChild + numAdult + numSenior;
				// check if all of the seats together fit within the row
				for (int i = 0; totalSeats > 0; i++, totalSeats--) {
					if (auditorium[rowNumber][columnNumber + i] != '.') {
						available = false;
					}
				}

				if (available) { // Reserve the seats if they are available
					auditorium = reserveSeats(auditorium, numAdult, numChild, numSenior, rowNumber, columnNumber);
					System.out.println("Done!");
				} else { // Recommend different seats and reserve them if the user decides to (if the
							// seats aren't available
					System.out.println("Those seats are not available");
					auditorium = recommendSeats(rowNumber, columnNumber, numAdult, numChild, numSenior, auditorium,
							scanner);
				}
				printUser(auditorium);
			} else if (input.equals("Exit")) {

			}

			// Create a String array to hold each line of the auditorium
			String[] tempAuditorium = new String[auditorium.length];
			for (int i = 0; i < tempAuditorium.length; i++) {
				tempAuditorium[i] = "";
			}
			for (int i = 0; i < auditorium.length; i++) {
				for (int x = 0; x < auditorium[i].length; x++) {
					tempAuditorium[i] += Character.toString(auditorium[i][x]);
				}
			}

			// Write the auditorium into the original file one row at a time
			PrintWriter print = new PrintWriter(numAuditorium + ".txt");
			for (int i = 0; i < tempAuditorium.length; i++) {
				print.println(tempAuditorium[i]);
			}
			print.close();
		} while (!input.equals("Exit"));

		// Declare variables to hold the final values for all auditoriums in the
		// directory
		int totalSeats = 0;
		int totalAdult = 0;
		int totalChild = 0;
		int totalSenior = 0;
		double ticketSales = 0;
		int totalSales = 0;

		// Loop through every auditorium file in the directory
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		// Read the information from the auditorium file if it is an auditorium file
		for (int i = 0; i < listOfFiles.length; i++) {
			File tempFile = listOfFiles[i];
			if (tempFile.isFile() && tempFile.getName().toLowerCase().endsWith(".txt")) {
				Scanner temp = new Scanner(tempFile);

				char[][] tempA = readInitialAuditorium(temp);
				for (int x = 0; x < tempA.length; x++) {
					for (int y = 0; y < tempA[x].length; y++) {
						if (tempA[x][y] == 'A') {
							totalAdult++;
							totalSales++;
						} else if (tempA[x][y] == 'C') {
							totalChild++;
							totalSales++;
						} else if (tempA[x][y] == 'S') {
							totalSenior++;
							totalSales++;
						}
						totalSeats++;
					}
				}
			}
		}
		ticketSales = ((double) totalAdult * 10.0) + ((double) totalChild * 5.0) + ((double) totalSenior * 7.5);

		// Print out the final information for all auditoriums in the theater
		System.out.println("Total seats in all auditoriums: " + totalSeats);
		System.out.println("Total tickets sold: " + totalSales);
		System.out.println("Total adult tickets sold: " + totalAdult);
		System.out.println("Total child tickets sold: " + totalChild);
		System.out.println("Total senior tickets sold: " + totalSenior);
		System.out.println("Total ticket sales: $" + ticketSales + "0");

		// Close the scanner
		fileReader.close();
	}

	// This method reads an auditorium file and returns a 2D char array containing
	// the auditorium
	public static char[][] readInitialAuditorium(Scanner fileReader) throws Exception {
		// Read the rows into a 2D array as complete strings first
		String[] tempAuditorium = new String[10];
		for (int i = 0; i < tempAuditorium.length; i++) {
			tempAuditorium[i] = "no row";
		}

		for (int i = 0; i < tempAuditorium.length; i++) {
			if (!fileReader.hasNext()) {
				break;
			} else {
				tempAuditorium[i] = fileReader.nextLine();
			}
		}

		// Determine the number of rows and columns in the auditorium
		int numRows = 0;
		int numColumns = tempAuditorium[0].length();
		for (int i = 0; i < tempAuditorium.length; i++) {
			if (tempAuditorium[i].equals("no row")) {
				numRows = i;
				break;
			}
		}

		// Split up the string array into chars and read it into the auditorium 2D char
		// array
		char[][] initialAuditorium = new char[numRows][numColumns];
		for (int i = 0; i < initialAuditorium.length; i++) {
			for (int x = 0; x < initialAuditorium[i].length; x++) {
				initialAuditorium[i][x] = tempAuditorium[i].charAt(x);
			}
		}

		return initialAuditorium;
	}

	// This method recommends seats that are closest to the middle of a requested
	// row
	public static char[][] recommendSeats(int rowNumber, int columnNumber, int numAdult, int numChild, int numSenior,
			char[][] auditorium, Scanner scanner) {
		String recommendation = "No available seats matching the criteria in row " + (rowNumber + 1);
		int numSeats = numAdult + numChild + numSenior;
		int startingIndex = -1;

		// Find the starting index of where the reservation seats will be in the middle
		// of the row
		int startTesting = -1;
		String row = "";
		int rowLength = auditorium[rowNumber].length;
		for (int i = 0; i < auditorium[rowNumber].length; i++) {
			row += Character.toString(auditorium[rowNumber][i]);
		}

		// Account for the entire row being full
		boolean full = true;
		for (int i = 0; i < row.length(); i++) {
			if (row.substring(i, i + 1).contentEquals(".")) {
				full = false;
			}
		}
		if (full) {
			System.out.println(recommendation);
			return auditorium;
		}

		// if the length of row is even
		if (rowLength % 2 == 0) {
			for (int i = 0; i < auditorium[rowNumber].length - numSeats; i++) {
				// and the number of seats that we want to reserve are even, then make it right
				// in the middle
				if (numSeats % 2 == 0) {
					if (Math.abs(row.substring(0, i).length() - row.substring(i + numSeats).length()) == 0) {
						startTesting = i;
						break;
					}
					// and the number of seats that we want to reserve are odd, then make it a
					// little to the left (because we can't put it exactly in the middle)
				} else {
					if (Math.abs(row.substring(0, i).length() - row.substring(i + numSeats).length()) == 1) {
						startTesting = i;
						break;
					}
				}
			}
			// if the length of the row is odd
		} else {
			for (int i = 0; i < auditorium[rowNumber].length - numSeats; i++) {
				// and the number of seats that we want to reserve are even, then make it a
				// little to the left (because we can't put it exactly in the middle)
				if (numSeats % 2 == 0) {
					if (Math.abs(row.substring(0, i).length() - row.substring(i + numSeats).length()) == 1) {
						startTesting = i;
						break;
					}
					// and the number of seats that we want to reserve are even, then make it right
					// in the middle
				} else {
					if (Math.abs(row.substring(0, i).length() - row.substring(i + numSeats).length()) == 0) {
						startTesting = i;
						break;
					}
				}
			}
		}

		// Start moving left and right of the middle of the row. If the seats fit, break
		// the process. If the entire loop executes without giving a recommendation,
		// there is no room in the row to reserve the requested seats.
		int x;
		int y;
		if (startTesting != -1) {
			for (x = startTesting, y = startTesting; x >= 0 || y < auditorium[rowNumber].length; y++, x--) {
				boolean xWorks = true;
				boolean yWorks = true;

				// check if the seats fit as long as we are in the bounds of the array
				if (y < auditorium[rowNumber].length) {
					if (y + numSeats <= auditorium[rowNumber].length/* <= auditorium[rowNumber].length */) {
						for (int i = y, counter = 0; counter < numSeats; counter++, i++) {
							if (auditorium[rowNumber][i] != '.') {
								yWorks = false;
							}
						}
					}
				}

				if (yWorks) {
					startingIndex = y;
					break;
				}

				// check if the seats fit as long as we are in the bounds of the array
				if (x > -1) {
					if (x + numSeats <= auditorium[rowNumber].length/* <= auditorium[rowNumber].length */) {
						for (int i = x, counter = 0; counter < numSeats; counter++, i++) {
							if (auditorium[rowNumber][i] != '.') {
								xWorks = false;
							}
						}
					}
				}

				if (xWorks) {
					startingIndex = x;
					break;
				}
			}
		}

		// If there is an applicable recommendation, print it
		if (startingIndex != -1) {
			recommendation = "You can reserve " + numSeats + " seats starting at seat "
					+ ((char) ((startingIndex + 1) + 64)) + " on row " + (rowNumber + 1);
		}

		char decision = 'L';
		System.out.println(recommendation);

		// Get the user's decision on whether they want to reserve the recommended seats
		if (startingIndex != -1) {
			System.out.println("Enter Y if you would like to reserve these, or N if not");
			decision = scanner.next().charAt(0);
			scanner.nextLine();
		}

		// If they want to reserve, call the reserveSeats method
		if (decision == 'Y') {
			auditorium = reserveSeats(auditorium, numAdult, numChild, numSenior, rowNumber, startingIndex);
			System.out.println("Done!");
		}

		return auditorium;
	}

	// This method reserves seats in an auditorium given the row number, starting
	// column number, and the number of people. It returns a 2D array containing the
	// updated auditorium.
	public static char[][] reserveSeats(char[][] auditorium, int numAdult, int numChild, int numSenior, int rowNumber,
			int columnNumber) {
		// Read the auditorium into a temporary 2D char array
		char[][] seatType = new char[auditorium.length][auditorium[0].length];
		for (int i = 0; i < seatType.length; i++) {
			for (int x = 0; x < seatType[i].length; x++) {
				seatType[i][x] = auditorium[i][x];
			}
		}

		// Create a string to hold the seats you are trying to reserve
		String reservation = "";
		while (numAdult > 0) {
			reservation += "A";
			numAdult--;
		}
		while (numChild > 0) {
			reservation += "C";
			numChild--;
		}
		while (numSenior > 0) {
			reservation += "S";
			numSenior--;
		}

		// Insert the reservation string into the temporary auditorium char[] array
		// (reserving the seats)
		int counter = 0;
		while (reservation.length() > 0) {
			seatType[rowNumber][columnNumber + counter] = reservation.substring(0, 1).charAt(0);
			reservation = reservation.substring(1);
			counter++;
		}

		// Update the real auditorium variable by copying the temporary one to it
		for (int i = 0; i < auditorium.length; i++) {
			for (int x = 0; x < auditorium[i].length; x++) {
				auditorium[i][x] = seatType[i][x];
			}
		}

		return auditorium;
	}

	// This method prints the auditorium in a user-friendly manner with the reserved
	// seats represented as '#'
	public static void printUser(char[][] auditorium) {
		// Print the column letters
		System.out.print("  ");
		if (auditorium[0].length % 2 == 0) {
			for (int i = 0; i < auditorium[0].length; i++) {
				System.out.print(((char) (65 + i)) + " ");
			}
			System.out.println("");
		} else {
			for (int i = 0; i < auditorium[0].length; i++) {
				System.out.print(((char) (65 + i)) + " ");
			}
			System.out.println("");
		}

		// Print each individual row with spaces between the seats
		for (int i = 0; i < auditorium.length; i++) {
			System.out.print((i + 1) + " "); // prints row number
			for (int x = 0; x < auditorium[i].length; x++) {
				// print out a # if there is a seat reserved
				if (auditorium[i][x] != '.' && auditorium[i][x] != '#') {
					System.out.print("# ");
				} else {
					System.out.print(auditorium[i][x] + " ");
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}
}