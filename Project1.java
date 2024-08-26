/**
 * @author Avery Lamprecht and Summer Nelson
 * @date 23/02/2024
 */
package project1;
//get ready for So Many Comments. I think this is the highest density of comments in code i have ever written.

import java.util.ArrayList;
import java.util.Scanner;
import project1.PuzzleSolver.Node;

public class Project1 {

	/**
	 * driver method for program
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		ArrayList<Integer> info = getInput(in); 
		PuzzleSolver greg = new PuzzleSolver(info.get(0), info.get(1), info.get(2), info.get(3));
		
		final long startTime = System.currentTimeMillis(); //grabbing time before the search, to time how long it takes
		Node george = greg.search(); //actually calling the search method
		final long endTime = System.currentTimeMillis(); //the time after the search 
		
		if (george != null) { //checking if greg found a solution. if so, print it
			System.out.println("Solution found with "+george.steps()+" moves: "+george.getPath());
			//System.out.println(george.toString());
			//System.out.println(george.prog(george));
		} else
			System.out.println("No solution."); //if not, print "No solution"
		
		//cool stats!
		System.out.println("Frontier expanded "+greg.getCount()+" times."); 
		System.out.println(time(startTime, endTime)); //prints the amount of time the search took
	}
	
	/**
	 * method to get input to set up puzzle
	 * @param in - scanner object for input
	 * @return an ArrayList of four integers for puzzle setup
	 */
	public static ArrayList<Integer> getInput(Scanner in) {
		System.out.println("Please enter, in order and separated by one space, the number of rows/ducks, the length of each lane, "
				+ "the number of the flag duck, and the maximum fuel.");
		String input = in.nextLine();
		if (!input.matches("[0-9]{1,} [0-9]{1,} [0-9]{1,} [0-9]{1,}")) {
			//basic input protection using regex, checks that there are four numbers separated by a space
			System.out.print("Bad input. "); // lets the user know the input was bad
			return getInput(in); //calls the method again. while loop works just as well but its another level of nesting for the same thing
		}
		String[] split = input.split(" "); //split string by space for an array
		//creates the arraylist for the integers. could also be an array but i like arraylists more
		ArrayList<Integer> info = new ArrayList<Integer>(4); 
		for (int i = 0; i < 4; i++) {
			info.add(Integer.parseInt(split[i])); //gets integers from the split strings
		}
		return info;
	}
	
	/**
	 * method we made for timing the runtime of our code, so we could leave it running and come back to it with an accurate timer
	 * (we didnt want to sit and stare at the running program for 30+ minutes)
	 * @param startTime
	 * @param endTime
	 */
	public static String time(long startTime, long endTime) {
		//simple time calculations. if it took less than 1 second to run, it prints ms
		long searchtime = endTime-startTime;
		if (searchtime >= 1000) {
			//just some basic unit conversion
			long time = searchtime/1000;
			int minutes = (int) (time/60);
			int seconds = (int) (time-(minutes*60));
			int hours = minutes/60;
			minutes = minutes-(hours*60);
			return "Search time: "+hours+"h "+minutes+"m "+seconds+"s"; //formatting the print statement
		} else
			return "Search time: "+searchtime+"ms"; //if its less than a second this allows for some more specificity
	}
}