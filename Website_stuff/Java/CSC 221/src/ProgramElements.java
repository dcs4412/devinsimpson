import java.io.Serializable;

/**
 * program elements are the three building blocks of java programs the class,
 * the method and the variable this provides a central container for all of these
 * elements
 * 
 * @author Devin Simpson
 * 
 */
public abstract class ProgramElements implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * sets the class ID for the created program element, the class ID is the
	 * primary method for identifying a class's name and all of its methods,
	 * variables, and comments
	 * 
	 * @param s
	 */
	public abstract void setClassID(int s);

	/**
	 * returns the class Id for selected program elements
	 * 
	 * @return the class id for the program elements
	 */
	public abstract int getClassID();

	/**
	 * sets whether a program element is static
	 * 
	 * @param b
	 */
	public abstract void setStatic(boolean b);

	/**
	 * sets the visibility modifiers for the specific program element
	 * 
	 * @param s
	 */
	public abstract void setVisability(String s);

	/**
	 * sets the abstract or final or neither modifiers for the specific program
	 * element
	 * 
	 * @param s
	 */
	public abstract void setHierarchy(String s);

	/**
	 * sets the return type or data type for the specific program element
	 * 
	 * @param s
	 */
	public abstract void setReturnType(String s);

	/**
	 * returns the visibility modifiers for the specific program element
	 * 
	 * @return visibility modifiers
	 */
	public abstract String getVisability();

	/**
	 * returns the abstract or final or neither modifiers for the specific
	 * program element
	 * 
	 * @return abstract or final or neither modifiers
	 */
	public abstract String getHierarchy();

	/**
	 * returns the return type or data type for the specific program element
	 * 
	 * @return return type or data type
	 */
	public abstract String getReturnType();

	/**
	 * returns the name of the specific program element
	 * 
	 * @return name
	 */
	public abstract String getName();

	/**
	 * returns a description of the specific program element: class, method or
	 * instance
	 * 
	 * @return class, method or instance
	 */
	public abstract String getType();

	/**
	 * returns the boolean of whether a program element is static
	 * 
	 * @return static boolean
	 */
	public abstract boolean getStatic();

	/**
	 * sets the position of the program element in its particular class
	 * 
	 * @param i
	 */
	public abstract void setPosition(int i);

	/**
	 * returns the position of the program element in its particular class
	 * 
	 * @return position
	 */
	public abstract int getPosition();

	/**
	 * sets the name of the program element
	 * 
	 * @param s
	 */
	public abstract void setName(String s);

	/**
	 * prints the string HTML representation of the program element to the
	 * display panel
	 * 
	 * @return string HTML representation of the program element
	 */
	public abstract String printToList();

	/**
	 * sets the specific class to report that it has a main method
	 * 
	 * @param b
	 */
	public void addMain(boolean b) {

	}

	/**
	 * returns a boolean response to whether or not a class has a main method
	 * 
	 * @return boolean class has a main method
	 */
	public boolean hasMain() {
		return false;
	}

	/**
	 * returns a boolean response to whether or not a method is a main method
	 * 
	 * @return is method main method
	 */
	public boolean isMain() {
		return false;
	}

	/**
	 * returns the name of the class for which the method, variable, or comment
	 * under which it was found
	 * 
	 * @return class name or method, variable, or comment
	 */
	public String getClassName() {
		return "";
	}

	/**
	 * return a boolean response to whether or not a method is a default
	 * constructor
	 * 
	 * @return is method a default constructor
	 */
	public boolean isConstructor() {
		return false;
	}

	/**
	 * sets the specific class as finished
	 */
	public void finish() {

	}

	/**
	 * returns a boolean response of weather of not the specific class is
	 * finished or not
	 * 
	 * @return is class finished or not
	 */
	public boolean isfinished() {
		return false;
	}

	/**
	 * returns a boolean response of whether of not the specific program element
	 * is a comment
	 * 
	 * @return is program element a comment
	 */
	public abstract boolean isComment();

	/**
	 * returns the string representation of the comment the user typed
	 * 
	 * @return comment
	 */
	public abstract String getComment();

	/**
	 * sets the comment to a new string
	 * 
	 * @param c
	 */
	public abstract void setComment(String c);

	/**
	 * returns the class name of which the comment was about
	 * 
	 * @return class name
	 */
	public abstract String getOriginalCommentName();

	/**
	 * sets the program element that is being commented, that it is commented
	 */
	public abstract void setItemHavingComment();

	/**
	 * returns a boolean response of whether of not the program element has a
	 * comment
	 * 
	 * @return does program element have a comment
	 */
	public abstract boolean hasComment();

	/**
	 * sets the program element to no longer have a comment
	 */
	public abstract void removedComment();

	/**
	 * sets the number of created methods (including deleted ones) to make sure
	 * the default method name per class is unique
	 * 
	 * @param m
	 */
	public void setHighestMethod(int m) {

	}

	/**
	 * sets the number of created variables (including deleted ones) to make
	 * sure the default variables name per class is unique
	 * 
	 * @param v
	 */
	public void setHighestVariable(int v) {

	}

	/**
	 * gets the number of created methods (including deleted ones) to make sure
	 * the default methods name per class is unique
	 * 
	 * @return the number of created methods based on class
	 */
	public int getHighestMethod() {
		return 0;
	}

	/**
	 * gets the number of created variable (including deleted ones) to make sure
	 * the default variables name per class is unique
	 * 
	 * @return the number of created variables based on class
	 */
	public int getHighestVariable() {
		return 0;
	}

	/**
	 * sets the specific method to report that it is not a main method
	 */
	public void removeMain() {

	}

}
