package main_panels;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Dice:
 * This class is a Dungeons & Dragons dice simulator, where commands are given
 * to the roll method to produce a dice roll and the results of the dice roll
 * are returned.This class is one of 7 classes of the main panels 
 * used in the creation of a new D&D character.
 * @author Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class Dice {

	private Random randomGenerator = null;

	/**
	 * @author Devin Simpson
	 * 
	 *         This is the constructor for the dice handler, a random number
	 *         generator is required to perform dice rolls so one is initialized
	 *         when the dice handler is created
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public Dice() {

		// set up the random generator for dice rolls
		randomGenerator = new Random();

	}//end class constructor

	/**
	 * @author Devin Simpson
	 * 
	 *         This method simulates dice rolls in Dungeons & Dragons character
	 *         creation and returns the result of the dice roll in the form of
	 *         an array of integers
	 * 
	 * @param dice
	 *            (the Dungeons & Dragons name for the dice roll)
	 * @return the result of the dice roll in an array of integers
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public int[] roll(String dice) {

		// dice are referred to by "number or rolls"d"number of sides" for
		// example
		// 2d4 is a four sided die rolled two times

		// here the d separates the number of rolls and the number of sides,
		// remove it and create a list of the two numbers
		List<String> diceSplit = Arrays.asList(dice.split("d"));

		// the first number is the number of rolls
		int numberOfRolls = Integer.parseInt(diceSplit.get(0));

		// the second number is the number of sides the die has
		int diceRange = Integer.parseInt(diceSplit.get(1));

		// create an array of all the rolls performed by the requested dice roll
		int[] roll = new int[numberOfRolls];

		// fill each element of the array with a random number in the range of
		// the number of sides the die has
		for (int i = 0; i < roll.length; i++) {

			// compute each dice roll
			roll[i] = randomGenerator.nextInt(diceRange) + 1;
		}

		// return the resulting array of dice rolls
		return roll;

	}//end roll method

	/**
	 * @author Devin Simpson
	 * 
	 *         return the sum of a dice roll to the user
	 * 
	 * @param dice
	 * @return the dice roll sum
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public int rollSum(String dice) {

		// Initialize dice roll sum to zero
		int rollSum = 0;

		// get the roll array by calling the roll method to generate the roll
		int[] rollResult = roll(dice);

		// now add each element of the roll array to the rollSum variable
		for (int i = 0; i < rollResult.length; i++) {
			rollSum += rollResult[i];
		}

		// return the sum of the roll
		return rollSum;

	}//end rollSum method

}//end Dice class