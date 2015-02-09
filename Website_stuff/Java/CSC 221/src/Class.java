import java.io.Serializable;

/**
 * A class is the most general representation of an object. In this program
 * classes are considered program elements and have two forms the class name
 * with modifiers or an ending bracket comments are not available for classes in
 * this program. Each form will display a certain string representation for the
 * display panel and the exported java files
 * 
 * @author Devin Simpson
 * 
 */
public class Class extends ProgramElements implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String className, visability, hierarchy;
	private int ID, position, HighestMethod, HighestVariable;
	private boolean main, isFinished;

	/**
	 * constructor for the class that sets the name and modifiers
	 * 
	 * @param v
	 * @param h
	 * @param n
	 */
	public Class(String v, String h, String n) {

		className = n;
		visability = v;
		hierarchy = h;
		main = false;
		isFinished = false;
		HighestMethod = 1;
		HighestVariable = 1;
	}

	/**
	 * constructor for class that produces ending bracket
	 * 
	 * @param b
	 * @param n
	 * @param id
	 */
	public Class(boolean b, String n, int id) {
		className = n;
		visability = "";
		ID = id;
		isFinished = b;
		hierarchy = "";
	}

	/**
	 * returns the String "class" to identify the program element as a class
	 * 
	 * @return "class"
	 */
	public String getType() {
		return "class";
	}

	/**
	 * sets the name of the class to the new string
	 */
	public void setName(String s) {
		className = s;
	}

	/**
	 * returns the name of the class
	 * 
	 * @return name of class
	 */
	public String getName() {
		return className;
	}

	/**
	 * prints the string HTML representation of the class to the display panel
	 * 
	 * @return string HTML representation of the class
	 */
	public String printToList() {
		if (hierarchy.equals("n")) {
			return "<HTML>" + visability + " class " + className
					+ "{ <BR> <BR>";
		} else if (isFinished == true) {

			return "}";
		} else {
			return "<HTML>" + visability + " " + hierarchy + " class "
					+ className + "{ <BR> <BR>";
		}
	}

	/**
	 * prints the string representation of the class
	 * 
	 * @return string representation of the class
	 */
	public String toString() {
		if (hierarchy.equals("n")) {
			return visability + " class " + className + "{ \n ";
		} else if (isFinished == true) {

			return "}";
		} else {
			return visability + " " + hierarchy + " class " + className
					+ "{ \n ";
		}
	}

	@Override
	/**
	 * sets the class ID for the created class, the class ID is the
	 * primary method for identifying a class's name and all of its methods,
	 * variables, and comments 
	 * 
	 * @param s 
	 */
	public void setClassID(int s) {
		// TODO Auto-generated method stub
		ID = s;
	}

	@Override
	/**
	 * returns the class Id for selected class
	 * 
	 * @return the class id for the class 
	 */
	public int getClassID() {
		// TODO Auto-generated method stub
		return ID;
	}

	/**
	 * sets the specific class as finished
	 * 
	 * @param b
	 */
	public void finish(boolean b) {
		isFinished = b;
	}

	/**
	 * returns a boolean response of weather of not the specific class is
	 * finished or not
	 * 
	 * @return is class finished or not
	 */
	public boolean isfinished() {
		return isFinished;
	}

	/**
	 * sets the specific class to report that it has a main method
	 * 
	 * @param b
	 */
	public void addMain(boolean b) {
		main = b;
	}

	/**
	 * returns a boolean response to whether or not a class has a main method
	 * 
	 * @return boolean class has a main method
	 */
	public boolean hasMain() {
		return main;
	}

	@Override
	/**
	 * sets the visibility modifiers for the specific class
	 * 
	 * @param s 
	 */
	public void setVisability(String s) {
		// TODO Auto-generated method stub
		visability = s;

	}

	@Override
	/**
	 * sets the abstract or final or neither modifiers for the specific class
	 * 
	 * @param s 
	 */
	public void setHierarchy(String s) {
		// TODO Auto-generated method stub
		hierarchy = s;

	}

	@Override
	/**
	 *  does nothing for classes as they do not have return types or data types
	 */
	public void setReturnType(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * returns the visibility modifiers for the specific class
	 * 
	 * @return visibility modifiers 
	 */
	public String getVisability() {
		// TODO Auto-generated method stub
		return visability;
	}

	@Override
	/**
	 * returns the abstract or final or neither modifiers for the specific
	 * class
	 * 
	 * @return abstract or final or neither modifiers 
	 */
	public String getHierarchy() {
		// TODO Auto-generated method stub
		return hierarchy;
	}

	@Override
	/**
	 *  returns a String "no" because a class does not have a data or return type
	 *  
	 *  @return "no"
	 */
	public String getReturnType() {
		// TODO Auto-generated method stub
		return "no";
	}

	@Override
	/**
	 *  returns a false because a class cannot be static
	 *  
	 *  @return false
	 */
	public boolean getStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 *  does nothing because a class cannot be static
	 */
	public void setStatic(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * sets the position of the class
	 * 
	 * @param i
	 */
	public void setPosition(int i) {
		// TODO Auto-generated method stub
		position = i;
	}

	@Override
	/**
	 * returns the position of the class
	 * 
	 * @return position
	 */
	public int getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	/**
	 * returns false because comments are disabled for classes
	 * 
	 * @return false 
	 */
	public boolean isComment() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * returns nothing because comments are disabled for classes
	 * 
	 * @return nothing
	 */
	public String getComment() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	/**
	 * does nothing because comments are disabled for classes 
	 * 
	 * @param c
	 */
	public void setComment(String c) {
		// TODO Auto-generated method stub
	}

	@Override
	/**
	 * returns nothing because comments are disabled for classes
	 * 
	 * @return nothing 
	 */
	public String getOriginalCommentName() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	/**
	 * returns false because comments are disabled for classes
	 * 
	 * @return false  
	 */
	public boolean hasComment() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * does nothing because comments are disabled for classes 
	 */
	public void setItemHavingComment() {
		// TODO Auto-generated method stub
	}

	@Override
	/**
	 * does nothing because comments are disabled for classes 
	 */
	public void removedComment() {
		// TODO Auto-generated method stub
	}

	/**
	 * sets the number of created methods (including deleted ones) to make sure
	 * the default method name per class is unique
	 * 
	 * @param m
	 */
	public void setHighestMethod(int m) {
		HighestMethod = (HighestMethod + m);
	}

	/**
	 * sets the number of created variables (including deleted ones) to make
	 * sure the default variables name per class is unique
	 * 
	 * @param v
	 */
	public void setHighestVariable(int v) {
		HighestVariable = (HighestVariable + v);
	}

	/**
	 * gets the number of created methods (including deleted ones) to make sure
	 * the default methods name per class is unique
	 * 
	 * @return the number of created methods based on class
	 */
	public int getHighestMethod() {
		return HighestMethod;
	}

	/**
	 * gets the number of created variable (including deleted ones) to make sure
	 * the default variables name per class is unique
	 * 
	 * @return the number of created variables based on class
	 */
	public int getHighestVariable() {
		return HighestVariable;
	}
}