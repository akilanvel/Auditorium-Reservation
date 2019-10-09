package Tickets;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		String input = "";

		// get the name of text file and create a file object for it
		System.out.println("Which auditorium would you like to read from?");
		String numAuditorium = scanner.nextLine();

		// scanner object through which we will read from the requested auditorium
		Scanner fileReader = new Scanner(System.in);

		// open the file and catch the FileNotFoundException if the file isn't found
		// keep prompting the user for the auditorium file until they enter a valid one
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

		// create a 2D char array for the auditorium and initialize it
		char[][] auditorium = readInitialAuditorium(fileReader);

		printUser(auditorium);

		// start the program loop
		do {
			// get the user's decision
			System.out.println("1. Reserve");
			System.out.println("2. Exit");
			input = scanner.nextLine();

			// declare variables that the user will enter for reservation
			int rowNumber = 0;
			char startingSeatLetter = '0';
			int numAdult = 0;
			int numChild = 0;
			int numSenior = 0;

			if (!input.equals("Reserve") && !input.equals("Exit")) { // in case they enter an invalid menu option
				System.out.println("Invalid menu option");
			} else if (input.equals("Reserve")) {
				// get input for all the previous declared variables in order to reserve seats
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

				// to account that arrays have starting index 0
				rowNumber--;
				columnNumber--;

				boolean available = true;
				int totalSeats = numChild + numAdult + numSenior;
				// check if all of the seats together fit within the row
				for (int i = 0; totalSeats > 0; i++, totalSeats--) {
					if (auditorium[rowNumber][columnNumber + i] != '.') {
						available = false;
					}
				}
				if (available) { // reserve the seats if they are available
					auditorium = reserveSeats(auditorium, numAdult, numChild, numSenior, rowNumber, columnNumber);
					System.out.println("Done!");
				} else { // recommend different seats and reserve them if the user decides to
					System.out.println("Those seats are not available");
					auditorium = recommendSeats(rowNumber, columnNumber, numAdult, numChild, numSenior, auditorium,
							scanner);
				}
				printUser(auditorium);
			} else if (input.equals("Exit")) {

			}

			// create an auditorium with a string array to read into the file as a string
			String[] tempAuditorium = new String[auditorium.length];

			for (int i = 0; i < tempAuditorium.length; i++) {
				tempAuditorium[i] = "";
			}
			for (int i = 0; i < auditorium.length; i++) {
				for (int x = 0; x < auditorium[i].length; x++) {
					tempAuditorium[i] += Character.toString(auditorium[i][x]);
				}
			}

			// read the auditorium (in string format) into the file
			PrintWriter print = new PrintWriter(numAuditorium + ".txt");
			for (int i = 0; i < tempAuditorium.length; i++) {
				print.println(tempAuditorium[i]);
			}
			print.close();
		} while (!input.equals("Exit"));

		// print final report
		int totalSeats = 0;
		int totalAdult = 0;
		int totalChild = 0;
		int totalSenior = 0;
		double ticketSales = 0;
		int totalSales = 0;

		// create a folder and files array to hold all the files in the current folder
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		// go through each file individually (if its a .txt file) and read information
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

		// print out the final information for all auditoriums in the theater
		System.out.println("Total seats in all auditoriums: " + totalSeats);
		System.out.println("Total tickets sold: " + totalSales);
		System.out.println("Total adult tickets sold: " + totalAdult);
		System.out.println("Total child tickets sold: " + totalChild);
		System.out.println("Total senior tickets sold: " + totalSenior);
		System.out.println("Total ticket sales: $" + ticketSales + "0");

		// close scanner
		fileReader.close();
	}

	// method for reading in an auditorium from a text file
	public static char[][] readInitialAuditorium(Scanner fileReader) throws Exception {
		// read the rows as complete strings first
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

		// set the number of rows and the number of columns in the auditorium based off
		// of the strings we read earlier
		int numRows = 0;
		int numColumns = tempAuditorium[0].length();
		for (int i = 0; i < tempAuditorium.length; i++) {
			if (tempAuditorium[i].equals("no row")) {
				numRows = i;
				break;
			}
		}

		// split up the string array into chars and read it into the auditorium 2D array
		char[][] initialAuditorium = new char[numRows][numColumns];
		for (int i = 0; i < initialAuditorium.length; i++) {
			for (int x = 0; x < initialAuditorium[i].length; x++) {
				initialAuditorium[i][x] = tempAuditorium[i].charAt(x);
			}
		}

		return initialAuditorium;
	}

	// recommend seats if those that the user wanted at first are not available
	public static char[][] recommendSeats(int rowNumber, int columnNumber, int numAdult, int numChild, int numSenior,
			char[][] auditorium, Scanner scanner) {
		String recommendation = "No available seats matching the criteria in row " + (rowNumber + 1);
		int numSeats = numAdult + numChild + numSenior;
		int startingIndex = -1;

		// find the starting index of where the reservation seats will be in the middle
		// of the row
		int startTesting = -1;
		String row = "";
		int rowLength = auditorium[rowNumber].length;
		for (int i = 0; i < auditorium[rowNumber].length; i++) {
			row += Character.toString(auditorium[rowNumber][i]);
		}

		// if the entire row is full, there cannot be recommendations
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

		// start moving left and right of the middle and the moment the seats fit, we
		// can break the loop and recommend these seats
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

		// if there is an applicable recommendation
		if (startingIndex != -1) {
			recommendation = "You can reserve " + numSeats + " seats starting at seat "
					+ ((char) ((startingIndex + 1) + 64)) + " on row " + (rowNumber + 1);
		}

		char decision = 'L';
		System.out.println(recommendation);
		// get the user's decision on whether they want to reserve
		if (startingIndex != -1) {
			System.out.println("Enter Y if you would like to reserve these, or N if not");
			decision = scanner.next().charAt(0);
			scanner.nextLine();
		}

		// if they want to reserve, call the reserveSeats method
		if (decision == 'Y') {
			auditorium = reserveSeats(auditorium, numAdult, numChild, numSenior, rowNumber, startingIndex);
			System.out.println("Done!");
		}

		return auditorium;
	}

	// this method reserves seats
	public static char[][] reserveSeats(char[][] auditorium, int numAdult, int numChild, int numSenior, int rowNumber,
			int columnNumber) {
		// read the auditorium into a temporary 2D char array
		char[][] seatType = new char[auditorium.length][auditorium[0].length];
		for (int i = 0; i < seatType.length; i++) {
			for (int x = 0; x < seatType[i].length; x++) {
				seatType[i][x] = auditorium[i][x];
			}
		}

		// create a string to hold the seats you are trying to reserve
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

		// input the reservation string into the temporary auditorium char[] array
		// (reserving the seats)
		int counter = 0;
		while (reservation.length() > 0) {
			seatType[rowNumber][columnNumber + counter] = reservation.substring(0, 1).charAt(0);
			reservation = reservation.substring(1);
			counter++;
		}

		// update the real auditorium variable by copying the temporary one to it
		for (int i = 0; i < auditorium.length; i++) {
			for (int x = 0; x < auditorium[i].length; x++) {
				auditorium[i][x] = seatType[i][x];
			}
		}

		return auditorium;
	}

	// print the auditorium in a user-friendly way
	public static void printUser(char[][] auditorium) {
		// print out the column letters
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

		// print out each row with spaces between the seats
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