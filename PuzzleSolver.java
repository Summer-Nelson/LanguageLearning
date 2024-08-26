/**
 * @author Avery Lamprecht and Summer Nelson
 * @date 23/02/2024
 */
package project1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PuzzleSolver {
	
	//node class to store states and link to nodes
	protected class Node implements Comparable<Node> {

		private Node parent; //the parent node. technically since we are building our path as we expand we don't use this
		private DuckRace state; //the state stored by the node
		private String path; //the path to get to the nodes (the moves it took)
		
		/**
		 * constructor method for node class
		 * @param parent - node to set as parent
		 * @param state - DuckRace to set as state
		 */
		public Node(Node parent, DuckRace state, String path) {
			this.parent = parent;
			this.state = state;
			this.path = path;
		}

		//getters, don't need setters since parent and state shouldn't change
		public Node getParent() {
			return this.parent;
		}
		public DuckRace getState() {
			return this.state;
		}
		public String getPath() {
			return this.path;
		}
		
		/**
		 * toString method
		 * format:
		 * DuckRace's to string | cost of the node | the path without the weird space at the end
		 */
		@Override
		public String toString() {
			return this.state.toString()+" | "+getCost()+" | "+steps()+" moves: "+this.path.trim();
		}
		
		/**
		 * compareTo method for priority queue
		 * @return the cost of this node, minus the cost of the given node, to find the difference
		 */
		@Override
		public int compareTo(Node o) {
			return this.getCost()-o.getCost();
		}
		
		/**
		 * method to check if this node is equal to another
		 * @param n
		 * @return
		 */
		public boolean equals(Node n) {
			return this.state.equals(n.state);
		}
		
		/** 
		 * method to get all of the states leading up to the given one, recursive. used in testing.
		 * @param n 
		 * @return
		 */
		public String prog(Node n) {
			if (n == null)
				return "";
			return prog(n.parent)+n.toString()+"\n";
		}
		
		/**
		 * gets the cost of the node:
		 * this is the number of steps taken to get to the current node (using the path and splitting by spaces)
		 * plus the remaining distance to the goal state, calculated in duckrace
		 * @return
		 */
		public int getCost() {
			//g plus h. steps taken plus the minimum number of moves to get to the goal state
			return steps()+this.state.remainingDist(); 
		}
		
		/**
		 * counts the number of steps it took to get to the current node
		 * @return
		 */
		public int steps() {
			String steps = this.path.trim(); //removes the space at the end of the string
			String[] stepArray = steps.split(" "); //splits by space to separate the individual moves
			return stepArray.length; //returns the length of the array, aka the number of steps
		}
		
		/**
		 * alternate method of counting steps. gets the same result as the method above, but has a higher runtime - O(N)ish vs O(1)ish
		 * @return
		 */
		public int nodeSteps() {
			Node p1 = this; //start at this node
			int count = 0;
			while (p1.getParent() != null) {
				p1 = p1.getParent(); //go to each parent and increment count until there is no longer a parent
				count++;
			}
			return count;
		}
		
		/**
		 * counts the number of transfers that have happened in the past few moves, starting from the end and moving backwards
		 * @return
		 */
		public int numTransfers() {
			String steps = this.path.trim(); //removes the space at the end of the string
			String[] stepArray = steps.split(" "); //splits by space to separate the individual moves
			int count = 0;
			for (int i = stepArray.length-1; i >= 0; i--) {
				if (stepArray[i].contains("T"))
					count++;
				else
					break;
			}
			return count;
		}
		
		/**
		 * returns the most recent move from the path string
		 * honestly this probably couldve been a lot more elegent but eh whatever
		 * @return
		 */
		public String getLastMove() {
			String steps = this.path.trim(); //removes the space at the end of the string
			String[] stepArray = steps.split(" "); //splits by space to separate the individual moves
			return stepArray[stepArray.length-1];
		}
	}

	private Node s; //start state. parent node for all other nodes
	private int count; //counts the number of times the frontier expanded
	private ArrayList<Node> visited = new ArrayList<Node>(); //arraylist of nodes we have visited

	/**
	 * default constructor with no params. creates the default DuckRace as its starting state
	 */
	public PuzzleSolver() {
		s = new Node(null, new DuckRace(), "");
		count = 0;
	}

	/**
	 * constructor with params. uses those params to create a starting state DuckRace
	 * @param numrows - number of rows/ducks
	 * @param lanelength - length of each lane, i.e number of positions within
	 * @param flagduck - which duck has the flag
	 * @param maxfuel - max fuel per duck
	 */
	public PuzzleSolver(int numrows, int lanelength, int flagduck, int maxfuel) {
		s = new Node(null, new DuckRace(numrows, lanelength, flagduck, maxfuel), "");
		count = 0;
	}
	
	/**
	 * returns the number of times that the frontier expanded.
	 * @return
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * our final method for our search
	 * @return returns a solution node or null if no solution
	 */
	public Node search() {
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); //priority queue for the frontier
		frontier.add(s); //add the starting node
		while(!frontier.isEmpty()) { //while the frontier isnt empty, we expand each of the nodes in the frontier
			for (int i = 0; i < frontier.size(); i++) {
				Node current = frontier.poll(); //take the first off of the frontier and store it to current
				if (current.state.isGoalState()) { //checks if the node polled is the goal state
					return current; //yay! return it!
				} else {
					expand(current, frontier); //otherwise, expand it
				}
				//System.out.println(frontier.toString());
				count++; //counting the number of times we expanded our frontier. because yay stats
			}
		}
		return null; //if we got here, the frontier is empty and we didnt find a solution. thus, no solution, thus, null
	}

	/**
	 * our method for breadth-first search. we built on this to make our best-first
	 * @return
	 */
	public Node breadthFirst() {
		Queue<Node> frontier = new LinkedList<>(); //queue for the frontier
		frontier.add(s); //add the starting node
		while(!frontier.isEmpty()) { //while the frontier isnt empty, we expand each of the nodes in the frontier
			for (int i = 0; i < frontier.size(); i++) {
				Node current = frontier.remove(); //take the first off of the frontier and store it to current
				if (current.state.isGoalState()) { //checks if the node removed is the goal state
					return current; //yay! return it!
				} else {
					expand(current, frontier); //otherwise, expand it
				}
				//System.out.println(frontier.toString());
				count++; //counting the number of times we expanded our frontier. because yay stats
			}
		}
		return null; //if we got here, the frontier is empty and we didnt find a solution. thus, no solution, thus, null
	}
	
	/**
	 * expanding each node, in a separate method for less egregious nesting
	 * @param current - the node we are expanding from
	 * @param frontier - the frontier we are adding to
	 */
	public void expand(Node current, Queue<Node> frontier) {
		for (int i = 0; i < current.state.getNumRows(); i++) {
			Node n = null;
			try {
				n = new Node(current, current.state.moveDuck(i, true), current.path+"L"+i+" ");
			} catch(Exception e) {}
			/*
			 * before adding a node we just generated, we first check if:
			 * a) n is not null (if its null then an exception was thrown which means the move was illegal)
			 * b) the state is solvable (using the solvable method in duckrace)
			 * c) the state has not been visited previously
			 * d) 
			 */
			if (n != null && n.state.solvable() && !visited(n) && n.numTransfers() < n.state.getNumRows()) {
				frontier.add(n);
				visited.add(n);
			}
			n = null;
			try {
				n = new Node(current, current.state.moveDuck(i, false), current.path+"R"+i+" ");
			} catch(Exception e) {}
			if (n != null && n.state.solvable() && !visited(n) && n.numTransfers() < n.state.getNumRows()) {
				frontier.add(n);
				visited.add(n);
			}
			n = null;
			try {
				n = new Node(current, current.state.transferEnergy(i, i+1), current.path+"T"+i+"->"+(i+1)+" ");
			} catch(Exception e) {}
			//last check in the if statement here checks to make sure that the reverse transfer did not just happen
			//this is to avoid move sequences like "T1->0 T0->1"
			if (n != null && n.state.solvable() && !visited(n) 
					&& n.numTransfers() < n.state.getNumRows() && checkTransfer(i, true, n.parent)) {
				frontier.add(n);
				visited.add(n);
			}
			n = null;
			try {
				n = new Node(current, current.state.transferEnergy(i, i-1), current.path+"T"+i+"->"+(i-1)+" ");
			} catch(Exception e) {}
			if (n != null && n.state.solvable() && !visited(n) 
					&& n.numTransfers() < n.state.getNumRows() && checkTransfer(i, false, n.parent)) {
				frontier.add(n);
				visited.add(n);
			}
		}
	}
	
	/**
	 * checks if a transfer matches its reverse
	 * @param i - the index being transferred from
	 * @param direction - the direction of the transfer
	 * @param n - the previous node
	 * @return true or false
	 */
	public boolean checkTransfer(int i, boolean direction, Node n) {
		String str = "";
		//generates the string for the reverse of the move being made
		if (direction)
			str = "T"+(i+1)+"->"+i; //if the move would be "T"+i+"->"+(i+1)", its inverse is this
		else
			str = "T"+(i-1)+"->"+i; //if the move would be "T"+i+"->"+(i-1)", its inverse is this
		//checks if the reverse of the move being made matches the last move made
		if (n.getLastMove().equals(str))
			return false; //if the generated string matches the last move made, we do not want the move added
		else
			return true; //otherwise, it can be added
	}
	
	/**
	 * checks a node against the arraylist of visited nodes to see if it is equal to any other visited node
	 * @param n - the node
	 * @return a boolean value
	 */
	public boolean visited(Node n) {
		for (int i = 0; i < visited.size(); i++)
			if (n.equals(visited.get(i)) && n.compareTo(visited.get(i)) <= 0) 
				/*
				 * this if statement is breaking my brain but i made a minor change 
				 * to the compareTo check and suddenly the code was working as intended?
				 * specifically, i changed the comparison from > to <=. 
				 * and it changed from taking 15+ hours to taking a few minutes. 
				 * my brain does not understand why thats the comparison
				 * and if this is whats making it 2 moves longer than optimal then like
				 * id honestly prefer a handful of minutes for a slightly less optimal solution
				 * than a 15+ hour wait for the optimal one.
				 */
				return true;
		return false;
	}
}