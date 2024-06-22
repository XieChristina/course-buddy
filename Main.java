/*=================================================================
Main
Christina Xie
Jan 23, 2022
Java SE 8
=================================================================
Problem Definition – Mark tracker and calculating software
Input – Role, Name, Marks, etc.
Output – Depending on the option that the user chooses
Process – See individual methods
=================================================================
List of Static Variables 
 * static int classSize - integer to store the size of the class
 * static Map<String,Double[]> marks - a hash map that stores the student name as the key and a double array with their marks as the value
 * static Map<String,Double> weight - a hash map that stores the name of the category as the key and the weight as the value
 * static int[] points - integer array that stores the number of points that each category has
 * static String curFilePath - string that holds the current file path
=================================================================
*/
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
 
	static int classSize = 5;
	static Map<String, Double[]> marks = new HashMap<>();
	static Map<String, Double> weight = new HashMap<>();
	static int[] points = new int[5];
	static String curFilePath = "marks.txt";	
	static String newFilePath = "marks2.txt";	
 
	/**main method:
	* This procedural method is called automatically and is used to organize the calling of other methods defined in the class
	*
	* @param args <type String>
	* @throws IO Exception, FileNotFoundException
	* @return void
	*/
	public static void main(String[] args) throws FileNotFoundException, IOException{
		menu();	//call menu()
	}//end of main method
 
	/**menu method:
	* This functional method asks the user for their role.
	* Then it calls the student() or teacher() method depending on their answer.
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* String role - a String variable to store the role of the 
	*
	* @param none
	* @throws IO Exception, FileNotFoundException
	* @return void
	*/
	public static void menu() throws FileNotFoundException, IOException {
		System.out.println("WELCOME TO COURSE BUDDY!");
		System.out.println();
		Scanner sc = new Scanner(System.in);									// create Scanner
		readFile();																//call readFile method
		System.out.println("Welcome! Are you a student or a teacher? (S/T)");	//ask user for their role
		String role = sc.nextLine();											//get input and store in role variable
		if(role.equals("S")) student();											//if the role is student, then call student()
		else if(role.equals("T")) teacher();									//if the role is teacher, then call teacher()
		else { System.out.println("Error, please enter S or T"); menu();}		//else, there is an error and run menu() again
	}//end of menu method
 
	/**student method:
	* This functional method that lets the user choose what they want to see/do
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* String name - a String variable to store the name of the user
	* boolean exit - a boolean variable to keep track if the user wants to exit the program
	* int choice - an integer variable that keeps track of the user choice number
	*
	* @param none
	* @throws IO Exception, FileNotFoundException
	* @return void
	*/
	public static void student() {
		Scanner sc = new Scanner(System.in);		//create Scanner to get input
		System.out.println("Please enter your name (capitalize first letter): ");	//ask the user for their name
		String name = sc.nextLine();	//String variable to keep track of the name
		while(marks.get(name)==null) {	//if the name doesn't exist in the system
			System.out.println("Invalid student name, please try again (capitalize first letter): ");	//error message
			name = sc.nextLine();	//ask for the name again
		}//end of while loop
		System.out.println("Welcome, "+name+"!");	//Welcome message
		boolean exit = false;	//make a boolean exit to keep track if the user wants to exit
		while(!exit) {	//while exit is false
			System.out.println();	
			//print menu
			System.out.println("What would you like to do today?"
					+ "\n1. Check your mark"
					+ "\n2. Calculate new assignment mark"
					+ "\n3. Find out how much you need to score on your exam"
					+ "\n4. Exit");
			System.out.println("Please select an option: ");
			int choice = sc.nextInt();	//make an integer and get the user's choice
			if(choice == 1) markLookup(name); 	//if choice is 1, then call markLookup method
			else if(choice == 2) calculateNew(name);	//if choice is 2, then call calculateNew method
			else if(choice == 3) exam(name);	//if choice is 3, then call exam method
			else if(choice == 4) exit = true;	//if choice 4, then set exit to true
			else System.out.println("Error, please enter an option");	//else, show error message
		}//end of while loop
		System.out.println("Thank you for using this program!");	//thank you message
	}//end of student method
 
	/**markLookup method:
	* This functional method that lets the student look up their mark
	*
	* List of Local Variables
	* 
	* @param String name - to keep track of the name of the student
	* @throws none
	* @return void
	*/
	public static void markLookup(String name) {
		//Display student grade breakdown
		System.out.println("Student: "+name);
		System.out.println("Current grade: "+marks.get(name)[0]+"%");
		System.out.println("Knowledge: "+marks.get(name)[1]+"%"
				+"\nThinking: "+marks.get(name)[2]+"%"
				+"\nCommunication: "+marks.get(name)[3]+"%"
				+"\nApplication: "+marks.get(name)[4]+"%"
				+"\nFinal: "+marks.get(name)[5]+"%");
	}//end of markLookup method
 
	/**calculateNew method:
	* This functional method that lets the user calculate a new mark (mark forecasting)
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* double k - double variable to store the score for the knowledge section
	* int k2 - integer variable to store how many points the knowledge section was out of
	* double t - double variable to store the score for the thinking section
	* int t2 - integer variable to store how many points the thinking section was out of
	* double c - double variable to store the score for the communication section
	* int c2 - integer variable to store how many points the communication section was out of
	* double a - double variable to store the score for the application section
	* int a2 - integer variable to store how many points the application section was out of
	* double f -double variable to store the score for the final section
	* int f2 - integer variable to store how many points the final section was out of
	* double newK - double to store the new knowledge mark
	* double newT - double to store the new thinking mark
	* double newC - double to store the new communication mark
	* double newA - double to store the new application mark
	* double newF -  double to store the new final mark
	* double totalWeight - double to store the total weight of all the categories that have a mark
	* double total - double to store the new average
	*
	* @param String name - string to keep track of the student name
	* @throws none
	* @return void
	*/
	public static void calculateNew(String name) {
		Scanner sc = new Scanner(System.in);	//create Scanner to get input
		//get the marks and points for each section
		System.out.println("Please enter what score you got in each section(%): ");
		System.out.println("Knowledge: ");
		double k = sc.nextDouble();
		System.out.println("How many points it was out of: ");
		int k2 = sc.nextInt();
		System.out.println("Thinking: ");
		double t = sc.nextDouble();
		System.out.println("How many points it was out of: ");
		int t2 = sc.nextInt();
		System.out.println("Communication: ");
		double c = sc.nextDouble();
		System.out.println("How many points it was out of: ");
		int c2 = sc.nextInt();
		System.out.println("Application: ");
		double a = sc.nextDouble();
		System.out.println("How many points it was out of: ");
		int a2 = sc.nextInt();
		System.out.println("Final: ");
		double f = sc.nextDouble();
		System.out.println("How many points it was out of: ");
		int f2 = sc.nextInt();
 
		//calculate the new scores for each section
		double newK = ( (marks.get(name)[1]*points[0]) + k*k2)/(points[0]+k2);
		double newT = ( (marks.get(name)[2]*points[1]) + t*t2)/(points[1]+t2);
		double newC = ( (marks.get(name)[3]*points[2]) + c*c2)/(points[2]+c2);
		double newA = ( (marks.get(name)[4]*points[3]) + a*a2)/(points[3]+a2);
		double newF = ( (marks.get(name)[5]*points[4]) + f*f2)/(points[4]+f2);	
 
		//display the new rounded scores
		System.out.println("After this assesment, you will have: ");
		System.out.println("Knowledge: "+Math.round(newK*10)/10.0+"%"
				+"\nThinking: "+Math.round(newT*10)/10.0+"%"
				+"\nCommunication: "+Math.round(newC*10)/10.0+"%"
				+"\nApplication: "+Math.round(newA*10)/10.0+"%"
				+"\nFinal: "+Math.round(newF*10)/10.0+"%");
 
		//calculate the total weight 
		double totalWeight = 0;
		if(points[0]+k2 != 0) totalWeight+=weight.get("Knowledge");
		if(points[1]+t2 != 0) totalWeight+=weight.get("Thinking");
		if(points[2]+c2 != 0) totalWeight+=weight.get("Communication");
		if(points[3]+a2 != 0) totalWeight+=weight.get("Application");
		if(points[4]+f2 != 0) totalWeight+=weight.get("Final");
 
		//calculate the new score
		double total = 0.0;
		//if there is no mark in a category, don't include the mark in the average
		if(points[0] == 0) total = (weight.get("Thinking")*newT+weight.get("Communication")*newC+weight.get("Application")*newA+weight.get("Final")*newF)/totalWeight;
		if(points[1] == 0) total = (weight.get("Knowledge")*newK+
		weight.get("Communication")*newC+weight.get("Application")*newA+weight.get("Final")*newF)/totalWeight;
		if(points[2] == 0) total = (weight.get("Knowledge")*newK+weight.get("Thinking")*newT+weight.get("Application")*newA+weight.get("Final")*newF)/totalWeight;
		if(points[3] == 0) total = (weight.get("Knowledge")*newK+weight.get("Thinking")*newT+
		weight.get("Communication")*newC+weight.get("Final")*newF)/totalWeight;
		if(points[4] == 0) total = (weight.get("Knowledge")*newK+weight.get("Thinking")*newT+
		weight.get("Communication")*newC+weight.get("Application")*newA)/totalWeight;
 
		System.out.println("Your total score will be: "+Math.round(total*10)/10.0+"%");
	}//end of calculateNew method
 
	/**exam method:
	* This functional method that lets the user calculate how much they need to for their exam to get their desired score in the course
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* double score - double to store the desired mark
	* double finalWeight - double weight of the exam
	* double required - double to store the required score for the exam
	*
	* @param String name - string to keep track of the student name
	* @throws none
	* @return void
	*/
	public static void exam(String name) {
		Scanner sc = new Scanner(System.in);
		//Ask the user for what mark they want in the course
		System.out.println("What score would you like to achieve in this course? (%): ");
		double score = sc.nextDouble();
		double finalWeight = weight.get("Final")/100;	//get the weight of the exam
		double required = (score-marks.get(name)[0]*(1-finalWeight))/finalWeight;	//calculate the required score
		//display the results
		System.out.println("To get a score of "+score+"% at the end of this course, you will need at least "+Math.round(required*10)/10.0+"%");
		//if required score is impossible, then tell the user that it is impossible
		if(required>100) System.out.println("!!This is impossible unless you ask your teacher for extra credit!!");
	}//end of exam method
 
	/**teacher method:
	* This functional method that lets the teacher choose what they want to see/do
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* boolean exit - a boolean variable to keep track if the user wants to exit the program
	* int choice - an integer variable that keeps track of the user choice number
	*
	* @param none
	* @throws IO Exception
	* @return void
	*/
	public static void teacher() throws IOException{
		Scanner sc = new Scanner(System.in);	//make a scanner to get keyboard input
		System.out.println("Welcome teacher!");	//welcome message
		boolean exit = false;	//make a boolean variable to keep track if the user wants to exit the program
		while(!exit) {	//while exit is false
			System.out.println();
			//display options
			System.out.println("What would you like to do today?"
					+ "\n1. Update a student's mark"
					+ "\n2. Exit");
			//ask the user for their choice
			System.out.println("Please select an option: ");
			int choice = sc.nextInt();
			if(choice == 1) updateMark(); //if they chose 2, then call updateMark()
			else if(choice == 2) exit = true;	//if they chose 4, then set exit to true
			else System.out.println("Error, please enter an option");	//else, show error message
		}//end of while loop	
		System.out.println("Thank you for using this program!");	//thank you message
	}//end of teacher method

 
	/**updateMark method:
	* This functional method that lets the teacher update a student's marks
	*
	* List of Local Variables
	* Scanner sc - a Scanner object used to read keyboard input
	* String name - a String that keeps the name of the student that will have marks changed
	* String old - a String that keeps the old line in the file
	* double k - double variable to store the score for the knowledge section
	* double t - double variable to store the score for the thinking section
	* double c - double variable to store the score for the communication section
	* double a - double variable to store the score for the application section
	* double f - double variable to store the score for the final section
	* double totalWeight - double to store the total weight of all the categories that have a mark
	* double total - double to store the new average
	* String n - string to keep the new line that replaces the old line in the file
	* Path path - a Path object to locate the file that we are writing from
	* Path toPath - a Path object to locate the file that we are writing to
	* Charset charset - a Charset object for character encoding for the file
	* BufferedWriter writer - a BufferedWriter for writing to a text file
	* Scanner sc2 - a Scanner object to read text file
	* String line - a String to keep each line in the file
	* 
	*
	* @param none
	* @throws none
	* @return void
	*/
	public static void updateMark() throws IOException, FileNotFoundException{
		Scanner sc = new Scanner(System.in);	//create Scanner to get input
		//ask for student name
		System.out.println("Which student's marks would you like to update? (capitalize first letter)");
		String name = sc.nextLine();
		//check if the name exists 
		while(marks.get(name)==null) {
			System.out.println("Invalid student name, please try again (capitalize first letter): ");	//error message
			name = sc.nextLine();
		}//end of while loop
 
		//display current grade
		System.out.println("Current grade: "+marks.get(name)[0]+"%");	
		System.out.println("Knowledge: "+marks.get(name)[1]+"%"
				+"\nThinking: "+marks.get(name)[2]+"%"
				+"\nCommunication: "+marks.get(name)[3]+"%"
				+"\nApplication: "+marks.get(name)[4]+"%"
				+"\nFinal: "+marks.get(name)[5]+"%");
 
		//keep old marks
		String old = marks.get(name)[0]+" "+marks.get(name)[1]+" "+marks.get(name)[2]+" "
				+marks.get(name)[3]+" "+marks.get(name)[4]+" "+marks.get(name)[5];
 
		//ask user for new grades 
		System.out.println();
		System.out.println("Please enter new grades in one decimal place: ");
		System.out.println("Knowledge: ");
		double k = sc.nextDouble();
		System.out.println("Thinking: ");
		double t = sc.nextDouble();
		System.out.println("Communication: ");
		double c = sc.nextDouble();
		System.out.println("Application: ");
		double a = sc.nextDouble();
		System.out.println("Final: ");
		double f = sc.nextDouble();
 
		//update new marks
		marks.get(name)[1] = k;
		marks.get(name)[2] = t;
		marks.get(name)[3] = c;
		marks.get(name)[4] = a;
		marks.get(name)[5] = f;
 
		//get the weight of the categories that have a mark
		double totalWeight = 0;
		if(points[0] != 0) totalWeight+=weight.get("Knowledge");
		if(points[1] != 0) totalWeight+=weight.get("Thinking");
		if(points[2] != 0) totalWeight+=weight.get("Communication");
		if(points[3] != 0) totalWeight+=weight.get("Application");
		if(points[4] != 0) totalWeight+=weight.get("Final");	
 
		//calculate score
		double total = 0.0;
 
		if(points[0] == 0) total = (weight.get("Thinking")*t+weight.get("Communication")*c+weight.get("Application")*a+weight.get("Final")*f)/totalWeight;
		if(points[1] == 0) total = (weight.get("Knowledge")*k+
		weight.get("Communication")*c+weight.get("Application")*a+weight.get("Final")*f)/totalWeight;
		if(points[2] == 0) total = (weight.get("Knowledge")*k+weight.get("Thinking")*t+weight.get("Application")*a+weight.get("Final")*f)/totalWeight;
		if(points[3] == 0) total = (weight.get("Knowledge")*k+weight.get("Thinking")*t+
		weight.get("Communication")*c+weight.get("Final")*f)/totalWeight;
		if(points[4] == 0) total = (weight.get("Knowledge")*k+weight.get("Thinking")*t+
		weight.get("Communication")*c+weight.get("Application")*a)/totalWeight;
 
		//Display score
		System.out.println("The student's new total score is: "+Math.round(total*10)/10.0+"%");
 
		//keep new marks 
		String n = Math.round(total*10)/10.0+" "+marks.get(name)[1]+" "+marks.get(name)[2]+" "
				+marks.get(name)[3]+" "+marks.get(name)[4]+" "+marks.get(name)[5];
 
		marks.get(name)[0] = Math.round(total*10)/10.0;	//update new average
 
		//update text file	
		Path path = Paths.get(curFilePath);		//keep current file path
		Path toPath = Paths.get(newFilePath);	//keep new file path
		
		Charset charset = Charset.forName("UTF-8");
		BufferedWriter writer = Files.newBufferedWriter(toPath, charset);	//make a buffered writer
		Scanner sc2 = new Scanner(path, charset.name());	
		String line;	
		while(sc2.hasNextLine()) {	//go through each line
			line = sc2.nextLine();	
			line = line.replaceAll(old, n);	//replace all the old information with the new ones
			writer.write(line);				//write the new line
			writer.newLine();
		}//end of while loop
		
		writer.close();		//close the writer and scanner to flush 
		
		//write back to old file paths to update information
		
		path = Paths.get(newFilePath);		//change path from new file
		toPath = Paths.get(curFilePath);	//write to old file
		writer = Files.newBufferedWriter(toPath, charset);	//make a buffered writer for the new toPath
		sc2 = new Scanner(path, charset.name());	//scanner for the new path
		while(sc2.hasNextLine()) {	//go through each line
			line = sc2.nextLine();	
			line = line.replaceAll(old, n);	//replace all the old information with the new ones
			writer.write(line);				//write the new line
			writer.newLine();
		}//end of while loop
		
		writer.close();		//close the writer and scanner to flush 
		sc2.close();
		
	}//end of updateMark method
 
 
	/**readFile method:
	* This functional method that reads the file input
	*
	* List of Local Variables
	* BufferedReader - a BufferedReader object used to read text file 
	* String[] s - a String array that stores each category and weight line and splits them by space
	* Double[] arr - a Double array that holds each student and marks line and splits them by space
	*
	* @param none
	* @throws IOException, FileNotFoundException
	* @return void
	*/
	public static void readFile() throws IOException, FileNotFoundException{
		BufferedReader br = new BufferedReader (new FileReader (curFilePath));	//make a BufferedReader to read file input
		for(int i=0;i<5;i++) {	//for loop for each mark category
			String[] s = br.readLine().split(" ");	//split each line by space and store in s array
			weight.put(s[0], Double.parseDouble(s[1]));	//put each weight into the weight map
			points[i] = Integer.parseInt(s[2]);	//put each points in the points array
		}//end of for loop
		for(int i=0;i<classSize;i++) {	//for loop for each student
			String[] s = br.readLine().split(" ");	// split each line by space and store in s array
			Double[] arr = {Double.parseDouble(s[1]), Double.parseDouble(s[2]), 	//arr that stores the scores in each category for every name
					Double.parseDouble(s[3]), Double.parseDouble(s[4]),Double.parseDouble(s[5]),
					Double.parseDouble(s[6])};
			marks.put(s[0], arr);	//put the name and the corresponding array with marks in the marks map
		}//end of for loop
	}//end of readFile method
 
}