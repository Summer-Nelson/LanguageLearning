/**
 * @author Avery Lamprecht and Summer Nelson
 * @date 23/02/2024
 */
package project1;

public class Duck {
	
	private int fuel;
	private int maxfuel;
	private int lane;
	private int position;
	private boolean hasflag;
	
	/**
	 * constructor method for duck
	 * @param maxfuel - the maximum amount of fuel
	 * @param lane - the lane the duck is in
	 */
	public Duck(int maxfuel, int lane) {
		this.maxfuel = maxfuel;
		this.fuel = maxfuel; //fuel always starts at max
		this.lane = lane;
		this.position = 0; //position always starts at 0
		this.hasflag = false; //ducks all start not having the flag. this is irrelevant for ducks that are not the flag duck
	}
	
	/**
	 * constructor method used when cloning ducks
	 * only called in the clone method, so only used locally, hence private
	 * @param maxfuel
	 * @param fuel
	 * @param lane
	 * @param position
	 * @param hasflag
	 * has every class variable as a parameter
	 */
	private Duck(int maxfuel, int fuel, int lane, int position, boolean hasflag) {
		this.maxfuel = maxfuel;
		this.fuel = fuel;
		this.lane = lane;
		this.position = position;
		this.hasflag = hasflag;
	}
	
	/**
	 * method to move the duck in a given direction
	 * @param direction - the direction to move. true = positive = left, false = negative = right
	 * @throws Exception - throws the fuelUp and fuelDown exceptions if they happen
	 */
	public void move(boolean direction) throws Exception {
		if (direction) {
			fuelDown();
			positionUp();
		} else {
			fuelDown();
			positionDown();
		}
	}
	
	/**
	 * equals method to check ducks against each other. 
	 * important for checking states against each other - used in DuckRace's equals method
	 * @param d1 - the duck to compare
	 * @return returns a boolean value
	 */
	public boolean equals(Duck d1) {
		if (this.fuel == d1.fuel && this.position == d1.position && this.hasflag == d1.hasflag)
			return true;
		return false;
	}
	
	//increment/decrement. max position isn't stored in duck, so no exceptions for those. fuelmax is stored in duck, so fuelUp and fuelDown throw exceptions
	public void positionUp() {
		this.position++;
	}
	public void positionDown() {
		this.position--;
	}
	public void fuelUp() throws Exception {
		if (this.fuel < this.maxfuel)
			this.fuel++;
		else
			throw new Exception("Whoops! Fuel is already full.");
	}
	public void fuelDown() throws Exception {
		if (this.fuel > 0) 
			this.fuel--;
		else
			throw new Exception("Duck is too eepy.");
	}
	
	// getters and setters
	public void giveFlag() {
		this.hasflag = true;
	}
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getFuel() {
		return this.fuel;
	}
	public int getMaxFuel() {
		return this.maxfuel;
	}
	public int getLane() {
		return this.lane;
	}
	public int getPosition() {
		return this.position;
	}
	public boolean hasFlag() {
		return this.hasflag;
	}
	
	/**
	 * duck's toString method. format is:
	 * [lane: fuel/maxfuel, position]
	 */
	@Override
	public String toString() {
		return "["+this.lane+": "+this.fuel+"/"+this.maxfuel+", "+this.position+"]";
	}
	
	/**
	 * clones a duck!
	 */
	@Override
	public Duck clone() {
		return new Duck(maxfuel, fuel, lane, position, hasflag);
	}
}
