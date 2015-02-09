import java.io.Serializable;

/**
 * Variables are the specific data that define something in a program. In this
 * program they are a type of program element that display a certain string
 * representation for the display panel and the exported java files. In this
 * program variables come in the form of a standard variable and a comment.
 * 
 * @author Devin Simpson
 * 
 */
public class Variable extends ProgramElements implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String visability, dataType, variableName, comment,
			OriginalCommentName;
	private boolean staticS, isComment, hasComment;
	private int ID, position;

	/**
	 * constructor for variable based on its visibility, dataType, variableName
	 * and static modifiers
	 * 
	 * @param v
	 * @param b
	 * @param r
	 * @param n
	 */
	public Variable(String v, boolean b, String r, String n) {
		visability = v;
		dataType = r;
		variableName = n;
		staticS = b;
		isComment = false;
		comment = "";
		OriginalCommentName = "";
		hasComment = false;
	}

	/**
	 * constructor for the comment of the variable
	 * 
	 * @param c
	 * @param n
	 * @param id
	 */
	public Variable(String c, String n, int id) {
		visability = "";
		dataType = "";
		staticS = false;
		OriginalCommentName = n;
		variableName = n + "c" + id;
		comment = c;
		isComment = true;
		ID = id;
		hasComment = false;
	}

	/**
	 * returns the String "instance" to identify the program element as a
	 * variable
	 * 
	 * @return "instance"
	 */
	public String getType() {
		return "instance";
	}

	/**
	 * returns the name of the variable
	 * 
	 * @return name of variable
	 */
	public String getName() {
		return variableName;
	}

	/**
	 * prints the string HTML representation of the variable to the display
	 * panel
	 * 
	 * @return string HTML representation of the variable
	 */
	public String printToList() {
		if (staticS == false && isComment == false) {
			return "<HTML>&nbsp;&nbsp;&nbsp;&nbsp;" + visability + " "
					+ dataType + " " + variableName + ";<BR> <BR>";
		} else if (isComment == true && visability.equals("")) {
			return "<HTML>" + "&nbsp;&nbsp;&nbsp;&nbsp;" + "//" + comment;
		} else
			return "<HTML>&nbsp;&nbsp;&nbsp;&nbsp;" + visability + " static "
					+ dataType + " " + variableName + ";<BR> <BR>";
	}

	/**
	 * prints the string representation of the variable
	 * 
	 * @return string representation of the variable
	 */
	public String toString() {
		if (staticS == false && isComment == false) {
			return "\t" + visability + " " + dataType + " " + variableName
					+ ";\n";
		} else if (isComment == true) {
			return "\t" + "//" + comment;
		} else
			return "\t" + visability + " static " + dataType + " "
					+ variableName + ";\n";
	}

	@Override
	/**
	 * sets the class ID for the created variable, the class ID is the
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
	 * returns the class Id for selected variable
	 * 
	 * @return the class id for the variable
	 */
	public int getClassID() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	/**
	 ** sets the visibility modifiers for the specific variable
	 * 
	 * @param s 
	 */
	public void setVisability(String s) {
		// TODO Auto-generated method stub
		visability = s;
	}

	@Override
	/**
	 * does nothing as variables do not have abstract modifiers and this
	 * program does not support final variables
	 * 
	 * @param s
	 */
	public void setHierarchy(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * sets the data type of the specific variable 
	 * 
	 * @param s
	 */
	public void setReturnType(String s) {
		// TODO Auto-generated method stub
		dataType = s;
	}

	@Override
	/**
	 * sets the name of the specific variable
	 * 
	 * @param s
	 */
	public void setName(String s) {
		// TODO Auto-generated method stub
		variableName = s;
	}

	@Override
	/**
	 * returns the visibility modifiers for the specific variable
	 * 
	 * @return visibility modifiers  
	 */
	public String getVisability() {
		// TODO Auto-generated method stub
		return visability;
	}

	@Override
	/**
	 * returns a string "no" because variables do not have abstract modifiers and this
	 * program does not support final variables 
	 * 
	 * @return "no"
	 */
	public String getHierarchy() {
		// TODO Auto-generated method stub
		return "no";
	}

	@Override
	/**
	 *  returns the String return Type of the specific variable 
	 *  
	 *  @return return type 
	 */
	public String getReturnType() {
		// TODO Auto-generated method stub
		return dataType;
	}

	@Override
	/**
	 * returns the boolean of whether a variable is static
	 * 
	 * @return static boolean 
	 */
	public boolean getStatic() {
		// TODO Auto-generated method stub
		return staticS;
	}

	@Override
	/**
	 * sets whether a variable is static
	 * 
	 * @param b 
	 */
	public void setStatic(boolean b) {
		// TODO Auto-generated method stub
		staticS = b;
	}

	@Override
	/**
	 * sets the position of the variable in its particular class
	 * 
	 * @param i 
	 */
	public void setPosition(int i) {
		// TODO Auto-generated method stub
		position = i;
	}

	@Override
	/**
	 * returns the position of the variable in its particular class
	 * 
	 * @return position 
	 */
	public int getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	/**
	 * returns a boolean response of whether of not the specific variable
	 * is a comment
	 * 
	 * @return is variable a comment  
	 */
	public boolean isComment() {
		// TODO Auto-generated method stub
		return isComment;
	}

	@Override
	/**
	 * returns the string representation of the comment the user typed
	 * 
	 * @return comment  
	 */
	public String getComment() {
		// TODO Auto-generated method stub
		return comment;
	}

	@Override
	/**
	 * sets the comment to a new string
	 * 
	 * @param c 
	 */
	public void setComment(String c) {
		// TODO Auto-generated method stub
		comment = c;
	}

	@Override
	/**
	 * returns the method name of the variable being commented
	 * 
	 * @return Original variable Name 
	 */
	public String getOriginalCommentName() {
		// TODO Auto-generated method stub
		return OriginalCommentName;
	}

	@Override
	/**
	 * sets the method being commented that it has a comment 
	 */
	public void setItemHavingComment() {
		// TODO Auto-generated method stub
		hasComment = true;
	}

	@Override
	/**
	 * returns a boolean response of whether of not the variable has a
	 * comment
	 * 
	 * @return does variable have a comment 
	 */
	public boolean hasComment() {
		// TODO Auto-generated method stub
		return hasComment;
	}

	@Override
	/**
	 * sets the variable that had a comment, that it no longer has a comment 
	 */
	public void removedComment() {
		// TODO Auto-generated method stub
		hasComment = false;
	}
}