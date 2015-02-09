import java.io.Serializable;

/**
 * methods are like mini classes, they are containers for specific instructions
 * that are executed when they are called. In this program they are a type of
 * program element that display a certain string representation for the display
 * panel and the exported java files. In this program methods come in the form of a 
 * standard method, a main method, a default constructor, and a comment.
 * 
 * @author Devin Simpson
 * 
 */
public class Method extends ProgramElements implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String methodName, returnType, visability, abstractF,
			mainOrConstructor, className, comment, OriginalCommentName;
	private boolean staticS, isMain, isConstructor, isComment, hasComment;
	private int ID, position;

	/**
	 * constructor for a method based on the method Name, return Type,
	 * visibility, and abstract and static booleans
	 * 
	 * @param v
	 * @param a
	 * @param r
	 * @param n
	 * @param s
	 */
	public Method(String v, String a, String r, String n, boolean s) {
		// TODO Auto-generated constructor stub
		methodName = n;
		returnType = r;
		visability = v;
		abstractF = a;
		staticS = s;
		isMain = false;
		isConstructor = false;
		isComment = false;
		className = "";
		comment = "";
		OriginalCommentName = "";
		hasComment = false;
	}

	/**
	 * constructor for the main method and default constructor methods
	 * 
	 * @param s
	 * @param c
	 */
	public Method(String s, String c) {
		methodName = "";
		visability = "";
		abstractF = "";
		returnType = "";
		staticS = false;
		OriginalCommentName = "";
		mainOrConstructor = s;
		className = c;
		if (s.equals("m")) {
			isConstructor = false;
			isMain = true;
		} else if (s.equals("c")) {
			isConstructor = true;
			isMain = false;
		}
		hasComment = false;
	}

	/**
	 * constructor for method comments
	 * 
	 * @param c
	 * @param n
	 * @param id
	 */
	public Method(String c, String n, int id) {
		visability = "";
		abstractF = "";
		returnType = "";
		OriginalCommentName = n;
		mainOrConstructor = "";
		methodName = n + "c" + id;
		comment = c;
		isComment = true;
		staticS = false;
		ID = id;
		hasComment = false;
	}

	/**
	 * returns the String "method" to identify the program element as a method
	 * 
	 * @return "method"
	 */
	public String getType() {
		return "method";
	}

	/**
	 * returns a boolean response to whether or not a method is a main method
	 * 
	 * @return is method main method
	 */
	public boolean isMain() {
		return isMain;
	}

	/**
	 * sets the specific method to report that it is not a main method
	 */
	public void removeMain() {
		isMain = false;
	}

	/**
	 * returns a boolean response to whether or not a method is a default
	 * constructor
	 * 
	 * @return is method default constructor
	 */
	public boolean isConstructor() {
		return isConstructor;
	}

	/**
	 * sets the name of the method to the new string
	 */
	public void setName(String n) {
		methodName = n;
	}

	/**
	 * prints the string HTML representation of the method to the display panel
	 * 
	 * @return string HTML representation of the method
	 */
	public String printToList() {
		if (abstractF.equals("n") && staticS == false) {
			return "<HTML>" + "&nbsp;&nbsp;&nbsp;&nbsp;" + visability + " "
					+ returnType + " " + methodName + "(){ <BR><BR>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;}<BR><BR>";
		} else if (staticS == false && abstractF.equals("final")) {
			return "<HTML>" + "&nbsp;" + visability + " " + abstractF + " "
					+ returnType + " " + methodName + "(){ <BR><BR>"
					+ "   &nbsp;&nbsp;&nbsp;&nbsp;}<BR><BR>";
		} else if (staticS == true) {
			return "<HTML>" + "&nbsp;&nbsp;&nbsp;&nbsp;" + visability + " "
					+ " static " + returnType + " " + methodName
					+ "(){ <BR><BR>" + "   &nbsp;&nbsp;&nbsp;&nbsp;}<BR><BR>";
		} else if (abstractF.equals("abstract")) {
			return "<HTML>" + "&nbsp;&nbsp;&nbsp;&nbsp;;" + visability + " "
					+ abstractF + " " + returnType + " " + methodName
					+ "();<BR><BR>";
		} else if (mainOrConstructor.equals("m")) {
			return "<HTML> &nbsp;&nbsp;&nbsp;&nbsp; public static void main(String[] args){ <BR><BR>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;}";
		} else if (mainOrConstructor.equals("c")) {
			return "<HTML>&nbsp;&nbsp;&nbsp;&nbsp; public " + className
					+ "() { <BR><BR> &nbsp;&nbsp;&nbsp;&nbsp;}";
		} else if (isComment == true) {
			return "<HTML>" + "&nbsp;&nbsp;&nbsp;&nbsp;" + "//" + comment;
		}
		return "";
	}

	/**
	 * prints the string representation of the method
	 * 
	 * @return string representation of the method
	 */
	public String toString() {
		if (abstractF.equals("n") && staticS == false) {
			return "\t" + visability + " " + returnType + " " + methodName
					+ "(){\n\n" + "\t}\n";
		} else if (staticS == false && abstractF.equals("final")) {
			return "\t" + visability + " " + abstractF + " " + returnType + " "
					+ methodName + "(){\n" + "\t}\n";
		} else if (staticS == true) {
			return "\t" + visability + " " + " static " + returnType + " "
					+ methodName + "(){ \n\n" + "\t}\n";
		} else if (abstractF.equals("abstract")) {
			return "\t" + visability + " " + abstractF + " " + returnType + " "
					+ methodName + "();\n";
		} else if (mainOrConstructor.equals("m")) {
			return "\t" + "public static void main(String[] args){ \n\n"
					+ "\t}\n";
		} else if (mainOrConstructor.equals("c")) {
			return "\t" + "public " + className + "() { \n\n\t}\n";
		} else if (isComment == true) {
			return "\t" + "//" + comment;
		}
		return "";
	}

	@Override
	/**
	 * sets the class ID for the created method, the class ID is the
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
	 * returns the class Id for selected method
	 * 
	 * @return the class id for the method 
	 */
	public int getClassID() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	/**
	 * sets the visibility modifiers for the specific method
	 * 
	 * @param s 
	 */
	public void setVisability(String s) {
		// TODO Auto-generated method stub
		visability = s;
	}

	@Override
	/**
	 * sets the abstract or final or neither modifiers for the specific method
	 * 
	 * @param s 
	 */
	public void setHierarchy(String s) {
		// TODO Auto-generated method stub
		abstractF = s;
	}

	/**
	 * returns the class name for which the specific method is located in
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	@Override
	/**
	 * returns the return type for the specific method
	 * 
	 * @return return type of method 
	 */
	public void setReturnType(String s) {
		// TODO Auto-generated method stub
		returnType = s;
	}

	@Override
	/**
	 * returns the visibility modifiers for the specific method
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
	 * method
	 * 
	 * @return abstract or final or neither modifiers 
	 */
	public String getHierarchy() {
		// TODO Auto-generated method stub
		return abstractF;
	}

	@Override
	/**
	 *  returns the String return Type of the specific method 
	 *  
	 *  @return return type 
	 */
	public String getReturnType() {
		// TODO Auto-generated method stub
		return returnType;
	}

	@Override
	/**
	 * returns the name of the method
	 * 
	 * @return name of method 
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return methodName;
	}

	@Override
	/**
	 * returns the boolean of whether a method is static
	 * 
	 * @return static boolean 
	 */
	public boolean getStatic() {
		// TODO Auto-generated method stub
		return staticS;
	}

	@Override
	/**
	 * sets whether a method is static
	 * 
	 * @param b 
	 */
	public void setStatic(boolean b) {
		// TODO Auto-generated method stub
		staticS = b;

	}

	@Override
	/**
	 * sets the position of the method in its particular class
	 * 
	 * @param i 
	 */
	public void setPosition(int i) {
		// TODO Auto-generated method stub
		position = i;
	}

	@Override
	/**
	 * returns the position of the method in its particular class
	 * 
	 * @return position 
	 */
	public int getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	/**
	 * returns a boolean response of whether of not the specific method
	 * is a comment
	 * 
	 * @return is method a comment 
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
	 * returns the method name of the method being commented
	 * 
	 * @return Original method Name
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
	 * returns a boolean response of whether of not the method has a
	 * comment
	 * 
	 * @return does method have a comment 
	 */
	public boolean hasComment() {
		// TODO Auto-generated method stub
		return hasComment;
	}

	@Override
	/**
	 * sets the method that had a comment, that it no longer has a comment
	 */
	public void removedComment() {
		// TODO Auto-generated method stub
		hasComment = false;
	}
}