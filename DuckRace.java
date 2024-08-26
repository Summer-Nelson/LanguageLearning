/**
 * @author Avery Lamprecht and Summer Nelson
 * @date 23/02/2024
 */
package project1;

import java.util.ArrayList;

public class DuckRace {
	private int numrows; //the number of rows (ducks) there are
	private int lanelength; //the length of each lane, aka the number of positions in each lane
	private int flagduck; //which duck will get the flag
	private ArrayList<Duck> ducks = new ArrayList<Duck>(); //the arraylist of duck objects
	
	/**
	 * default constructor. uses the numbers from the base problem. technically this isn't used since we have input checking,
	 * but it allows for easier testing
	 */
	public DuckRace() {
		for (int i = 0; i < 4; i++) {
			ducks.add(new Duck(4, i));
		}
		this.numrows = 4;
		this.lanelength = 5;
		this.flagduck = 1;
	}
	
	/**
	 * constructor for creating a new DuckRace as a starting state
	 * @param numrows
	 * @param lanelength
	 * @param flagduck
	 * @param maxfuel
	 * for parameter explanations, see class variables
	 */
	public DuckRace(int numrows, int lanelength, int flagduck, int maxfuel) {
		for (int i = 0; i < numrows; i++) {
			ducks.add(new Duck(maxfuel, i));
		}
		this.numrows = numrows;
		this.lanelength = lanelength;
		this.flagduck = flagduck;
	}
	/**
	 * constructor for creating new DuckRace states based off of an already existing one
	 * @param numrows
	 * @param lanelength
	 * @param flagduck
	 * @param maxfuel
	 * @param ducks
	 * for parameter explanations, see class variables
	 */
	public DuckRace(int numrows, int lanelength, int flagduck, int maxfuel, ArrayList<Duck> ducks) {
		this.ducks = ducks;
		this.numrows = numrows;
		this.lanelength = lanelength;
		this.flagduck = flagduck;
	}
	
	/**
	 * moves a singular duck one space in one direction
	 * @param duck - which duck to move
	 * @param direction - true = positive = left, false = negative = right
	 * @return - the new state created with the duck moved
	 * @throws Exception - if trying to move a duck below 0 or above the lane length
	 */
	public DuckRace moveDuck(int duck, boolean direction) throws Exception {
		//create a variable for the new state to make changes to. uses the third version of the constructor to keep the data stored in the ducks
		ArrayList<Duck> newducks = new ArrayList<Duck>(); //making a new arraylist of ducks, cloning each duck over to the new one
		for (int i = 0; i < this.ducks.size(); i++) {
			newducks.add(ducks.get(i).clone());
		}
		DuckRace dr1 = new DuckRace(this.numrows, this.lanelength, this.flagduck, this.ducks.get(0).getMaxFuel(), newducks);
		if (direction) {
			if (dr1.ducks.get(duck).getPosition() < lanelength-1) //need to make this check here since lanelength isn't stored in the duck
				dr1.ducks.get(duck).move(direction);
			else
				throw new Exception("Whoops! Duck "+ducks.get(duck)+" ran in to the end of the lane."); 
		} else {
			if (dr1.ducks.get(duck).getPosition() > 0) //these checks are so that the ducks don't move out of the lane. better redundant than no checks at all
				dr1.ducks.get(duck).move(direction);
			else
				throw new Exception("Whoops! Duck "+ducks.get(duck)+" ran into the beginning of the lane."); //plus i get to write fun exception messages
		}
		if (duck == dr1.flagduck && dr1.ducks.get(duck).getPosition() == dr1.lanelength-1) //check if the flag duck just moved to the end of his lane
			dr1.ducks.get(duck).giveFlag(); //if so, give him the flag
		return dr1; //returns the state
	}
	
	/**
	 * transfers 1 fuel from one duck to another duck
	 * @param duck - the duck to give fuel
	 * @param duck1 - the duck to receive fuel
	 * @return - the new state created with the energy transferred
	 * @throws Exception - if the ducks are not next to each other, or if they are in different positions. 
	 * fuelUp and fuelDown also throw separate exceptions
	 */
	public DuckRace transferEnergy(int duck, int duck1) throws Exception {
		ArrayList<Duck> newducks = new ArrayList<Duck>(); //making a new arraylist of ducks, cloning each duck over to the new one
		for (int i = 0; i < this.ducks.size(); i++) {
			newducks.add(ducks.get(i).clone());
		}
		DuckRace dr1 = new DuckRace(this.numrows, this.lanelength, this.flagduck, this.ducks.get(0).getMaxFuel(), newducks);
		//checking that the ducks are at the same position, and they are in adjacent lanes
		if (dr1.ducks.get(duck).getPosition() != dr1.ducks.get(duck1).getPosition()
				|| (dr1.ducks.get(duck).getLane() != dr1.ducks.get(duck1).getLane()-1 
				&& dr1.ducks.get(duck).getLane() != dr1.ducks.get(duck1).getLane()+1)) { 
			//i only need to make these checks here, since fuelUp and fuelDown have their own checks against maxfuel
			throw new Exception("The ducks longingly gaze at one another, but are too far apart."); //lol funny exception method
		} else {
			dr1.ducks.get(duck1).fuelUp();
			dr1.ducks.get(duck).fuelDown();
		}
		return dr1;
	}
	
	/**
	 * checks if this state is the goal state. two checks: one to check if the flagduck has the 
	 * flag, one to check that all ducks are at position 0
	 * @return returns a boolean value
	 */
	public boolean isGoalState() {
		//look how short and clean this method is!!! so pretty
		return hasFlag() && ducksAtZero();
	}
	
	/**
	 * small method for use in isGoalState. mainly just makes the isGoalState method look nicer
	 * @return returns a boolean value
	 */
	private boolean ducksAtZero() {
		for (int duck = 0; duck < ducks.size(); duck++) {
			//using duck as my iterator in this for loop makes the get statements more fun :D
			if (ducks.get(duck).getPosition() != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * calculates the remaining distance to the goal state. 
	 * @return an integer that is the total distance
	 */
	public int remainingDist() {
		if (hasFlag())
			return totalDist(); //if the flag duck has the flag, this is just the total distance back to 0.
		else
			return totalDist()+(this.lanelength-ducks.get(flagduck).getPosition())*2; 
		//if the flag duck does not have the flag, we need to add the distance to and from the flag on the flag duck
	}
	
	/**
	 * loops through the ducks and adds their distance (positions) together
	 * the distance to 0 is the same as the position the duck is in
	 * only used locally in remainingDist and solvable
	 * @return the total distance
	 */
	private int totalDist() {
		int totaldist = 0;
		for (int duck = 0; duck < ducks.size(); duck++) {
			totaldist += ducks.get(duck).getPosition();
		}
		return totaldist;
	}
	
	/**
	 * loops through the ducks and adds their fuel together. 
	 * only used locally in solvable
	 * @return the amount of total fuel the ducks have
	 */
	private int totalFuel() {
		int totalfuel = 0;
		for (int duck = 0; duck < ducks.size(); duck++) {
			totalfuel += ducks.get(duck).getFuel();
		}
		return totalfuel;
	}
	
	/**
	 * checks if the state is solvable by checking if the total amount of fuel is greater than or equal to the distance
	 * that the ducks have left to travel
	 * @return a boolean value
	 */
	public boolean solvable() {
		return totalFuel() >= remainingDist();
	}
	
	/**
	 * checks if the state matches another state
	 * @param dr1 - the state to check against
	 * @return returns a boolean value
	 */
	public boolean equals(DuckRace dr1) {
		//simple for loop and if statement. returns false if any duck is not at 0, otherwise all ducks are at 0 and can return true
		for (int duck = 0; duck < this.ducks.size(); duck++) 
			if (!this.ducks.get(duck).equals(dr1.ducks.get(duck)))
				return false;
		return true;
	}
	
	/**
	 * ducks.get(flagduck).hasFlag() is clunky. Node.state.ducks.get(flagduck).hasFlag() is clunkier.
	 * this shortens those to hasFlag() and Node.state.hasFlag() respectively. much simpler, easier to read
	 * @return returns the same boolean value from the duck's hasFlag method
	 */
	public boolean hasFlag() {
		return ducks.get(flagduck).hasFlag();
	}
	
	//getters and setters
	public int getNumRows() {
		return this.numrows;
	}
	public int getFlagDuck() {
		return this.flagduck;
	}
	public int getLaneLength() {
		return this.lanelength;
	}
	public ArrayList<Duck> getDucks() {
		return this.ducks;
	}
	
	/**
	 * toString method override. format is:
	 * {0 for not flagduck, 1 for flagduck[duck toString], repeat per duck} length of lanes and does the flagduck have the flag
	 * @return returns the string
	 */
	@Override
	public String toString() {
		String str = "{";
		for (int duck = 0; duck < ducks.size(); duck++) {
			if (duck == flagduck)
				str = str + "1" + ducks.get(duck).toString();
			else 
				str = str + "0" + ducks.get(duck).toString();
			if (duck != ducks.size()-1) {
				str = str + ", ";
			}
		}
		return str + "} " + lanelength + " " + hasFlag();
	}
}
