import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * team project CSC 221
 * 
 * this project is designed to allow the user to learn the basics of programming by seeing and editing 
 * classes, methods, variables and comments
 * 
 * saving and loading possible to continue work on projects
 * 
 * use can select what and where a programs classes, comments, methods and variables to understand how programs
 * are written
 * 
 * they can name and select modifiers for classes, methods and variables
 * 
 * exporting to java documents is possible for the currently viewed class or all the classes in a program
 * for further editing in a real java development environment
 * 
 * and extra credit feature allows users to add one comment to their methods and variables so they can accurately
 * explain the purpose and function of them
 * 
 * everything in this program was written by me, Devin Simpson
 * 
 * @author Devin Simpson
 * 
 */
public class BasicProgramBuilder extends JFrame implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel creationPanel, selectionPanel, classPanel, infoPanel,
			classUpdatePanel;
	private JPanel classTextPanel, methodTextPanel, instanceVariableTextPanel,
			createClassPanel, createMethodPanel, createInsatcenVariablePanel,
			buttonPanel;

	private Color c = Color.LIGHT_GRAY, varColor = new Color(c.getRed() + 10,
			c.getRed() + 30, c.getRed() + 10), mColor = new Color(
			c.getRed() + 30, c.getRed() + 30, c.getRed() + 60),
			cColor = new Color(c.getRed() + 50, c.getRed() + 12,
					c.getRed() + 50); // colors for each panel based on weather

	private JPanel classCollectionPanel;

	private JList visualPanel;

	private DefaultListModel visual;

	private String className, visability = "public", returnType = "void",
			hierarchy = "n", selectedCreatedPanel = "class";

	private int classID = 0; // determines where created methods and varables
								// are placed

	private boolean staticSelect = true, instanceB = false, methodB = false, // show
																				// if
																				// class,
																				// method
																				// or
																				// variable
																				// buttons
																				// are
																				// clicked
			classB = true;

	private JTextField cName = new JTextField(), mName = new JTextField(),
			vName = new JTextField(), commentReplace = new JTextField();

	private JButton methodCreationSelect, classCreationSelect, // buttons to
																// switch
																// between
																// creating a
																// new class,
																// method or
																// variable
			instanceVariableCreationSelect;

	private JComboBox visableVar, staticSVar, returnTVar, visableC, hierarchyC,
			visableM, hierarchyM, staticSM, returnTM; // defined at class level
														// to allow reseting at
														// will

	private ArrayList<ProgramElements> myProgramElements = new ArrayList<ProgramElements>();
	private ArrayList<SelectionButton> classSelectionButton = new ArrayList<SelectionButton>();
	private ArrayList<Integer> classUniqueDefaultNameGenerator = new ArrayList<Integer>();// used
																							// to
																							// keep
																							// class
																							// names
																							// unique

	private JLabel statusLabel;// used to show informational
								// messages

	private JPanel CreationCards = new JPanel(new CardLayout()); // hold the
																	// class,
																	// method,
																	// variable,
																	// and blank
																	// creation
																	// panels
	private JPanel textCards = new JPanel(new CardLayout()),// hold the class,
															// method, and
															// variable
															// description
															// panels
			classCollectionCards = new JPanel(new CardLayout()),// the JButtons
																// container for
																// selecting
																// different
																// created
																// classes
			classTextCards = new JPanel(new CardLayout()),// there are three
															// class description
															// panels, each with
															// a different
															// number of
															// JButtons
			ButtonCards = new JPanel(new CardLayout()),// displays weather or
														// not the class,
														// method, and variable
														// buttons are displayed
			UpdateCards = new JPanel(new CardLayout()),// different combinations
														// of the add, replace,
														// and delete buttons
			FinishCards = new JPanel(new CardLayout()),// changes between finish
														// and continue working
														// buttons
			commentTitleCards = new JPanel(new CardLayout());// generates title
																// of program
																// element being
																// commented

	private JPanel displayPanel;

	EventHandler eh = new EventHandler();

	/**
	 * main method for basic program builder
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BasicProgramBuilder bbp = new BasicProgramBuilder();
		bbp.setVisible(true);

	}

	/**
	 * constructor for basic program builder
	 */
	public BasicProgramBuilder() {
		setTitle("Basic Program Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1006, 800);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		creationPanel = makeCreationPanel();

		displayPanel = buildDisplayPanel();

		buildMenu();

		cp.add(creationPanel, BorderLayout.WEST);
		cp.add(displayPanel, BorderLayout.EAST);

		cName.setText("Class1");
		mName.setText("method1");
		vName.setText("property1");

	}

	/*
	 * builds the overall display panel that contains all the printed program
	 * elements
	 */
	private JPanel buildDisplayPanel() {
		JPanel p = new JPanel();
		visual = new DefaultListModel();
		visualPanel = new JList(visual);
		visualPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		visualPanel.setSelectedIndex(0);
		Font f = new Font("Arial", Font.PLAIN, 20);
		visualPanel.setFont(f);
		visualPanel.addListSelectionListener(eh);
		visualPanel.setVisibleRowCount(40);
		JScrollPane listScrollPane = new JScrollPane(visualPanel);

		listScrollPane.setPreferredSize(new Dimension(500, 800));
		p.setLayout(new BorderLayout());
		p.add(listScrollPane, BorderLayout.CENTER);

		return p;
	}

	/*
	 * builds the containing panel for all the creation and text panels that
	 * display the creation controls
	 */
	private JPanel makeCreationPanel() {
		// TODO Auto-generated method stub
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 800));
		p.setLayout(new BorderLayout());
		infoPanel = buildInfoPanel();
		createClassPanel = buildCreateClassPanel();
		selectionPanel = buildSelectionPanel();

		createClassPanel = buildCreateClassPanel();
		createMethodPanel = buildCreateMethodPanel();
		createInsatcenVariablePanel = buildCreateVariablePanel();
		CreationCards.add("create class panel", createClassPanel);
		CreationCards.add("create method panel", createMethodPanel);
		CreationCards.add("create instance panel", createInsatcenVariablePanel);
		JPanel panel = ReplaceCommentPanel();
		CreationCards.add("replace comment panel", panel);

		p.add(infoPanel, BorderLayout.NORTH);
		p.add(CreationCards, BorderLayout.CENTER);
		p.add(selectionPanel, BorderLayout.SOUTH);

		JPanel pan = buildCreateNothingPanel();

		CreationCards.add("no creation panel", pan);
		return p;
	}

	/*
	 * builds the containing panel for the class, method, and variable
	 * description panels
	 */
	private JPanel buildInfoPanel() {
		JPanel p = new JPanel();
		p.setBackground(Color.darkGray);
		p.setPreferredSize(new Dimension(500, 300));
		p.setLayout(new BorderLayout());
		JPanel statusBar = createStatusBar();
		classTextPanel = buildEmptyClassTextPanel();
		classTextCards.add("empty class text", classTextPanel);
		methodTextPanel = buildMethodTextPanel();
		instanceVariableTextPanel = buildVariableTextPanel();
		buttonPanel = buildClassOnlyButtonPanel();
		ButtonCards.add("class only button panel", buttonPanel);

		textCards.add("class text panel", classTextCards);
		textCards.add("method text panel", methodTextPanel);
		textCards.add("instance text panel", instanceVariableTextPanel);
		p.add(statusBar, BorderLayout.NORTH);
		p.add(textCards, BorderLayout.CENTER);
		p.add(ButtonCards, BorderLayout.SOUTH);

		buttonPanel = buildButtonPanel();
		ButtonCards.add("full button panel", buttonPanel);

		buttonPanel = buildNoButtonPanel();

		ButtonCards.add("no button panel", buttonPanel);

		return p;
	}

	/*
	 * panel that displays the class, method, and variable buttons that allows
	 * user to switch between the three creation panels
	 */
	private JPanel buildButtonPanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 25));
		p.setLayout(new BorderLayout());
		classCreationSelect = new JButton("Class");
		classCreationSelect.setBackground(Color.RED);
		classCreationSelect.addActionListener(eh);
		classCreationSelect.setPreferredSize(new Dimension(160, 25));

		methodCreationSelect = new JButton("Method");
		methodCreationSelect.setBackground(Color.green);
		methodCreationSelect.addActionListener(eh);
		methodCreationSelect.setPreferredSize(new Dimension(160, 25));

		instanceVariableCreationSelect = new JButton("Variable");
		instanceVariableCreationSelect.setBackground(Color.green);
		instanceVariableCreationSelect.addActionListener(eh);
		instanceVariableCreationSelect.setPreferredSize(new Dimension(180, 25));

		p.add(instanceVariableCreationSelect, BorderLayout.WEST);
		p.add(methodCreationSelect, BorderLayout.CENTER);
		p.add(classCreationSelect, BorderLayout.EAST);
		return p;
	}

	/*
	 * displays the class button only to show that only classes can be created
	 * at this time
	 */
	private JPanel buildClassOnlyButtonPanel() {
		JPanel p = new JPanel();
		p.setBackground(cColor);
		p.setSize(500, 50);
		classCreationSelect = new JButton("First Class");
		classCreationSelect.setBackground(Color.RED);
		classCreationSelect.addActionListener(eh);

		p.add(classCreationSelect, BorderLayout.CENTER);
		return p;
	}

	/*
	 * panel that removes any button to show that the period for creating
	 * classes, methods, and variables has ended
	 */
	private JPanel buildNoButtonPanel() {
		JPanel p = new JPanel();
		p.setBackground(cColor);
		p.setSize(500, 50);
		return p;
	}

	/*
	 * builds class description panel that contains both the main method button
	 * and default constructor button
	 */
	private JPanel buildFullClassTextPanel() {
		JPanel p = new JPanel();
		p.setSize(500, 200);
		JButton main = new JButton("main method");
		JButton defaultC = new JButton("default constructor");
		String str = "A class is the most general representation of an object. In each \n"
				+ "class there exists the blueprint for all the objects made from it."
				+ "\n\nFor example if a class called bicycle was made, it would represent all"
				+ "\nthe properties and  functionality of  bicycles. A single bicycle would"
				+ "\nbe an instance of the class bicycle, an object called bicycle."
				+ "\n\nEach class is named based on a couple of modifiers and a name\n(that is usually capitalized)."
				+ "\n\nmodifiers \n\npublic: sets the visability of the class so that it can be accessed"
				+ "\nby other classes.\n\nprivate: sets the visability of the class so that it can be accessed"
				+ "\nonly by the class containing it.\n\nabstract: designates the class as a container for further,more specific"
				+ "\n,classes thus creating a hierarchy of classes.These classes can now\nhave abstract methods.\n\n"
				+ "final: designates the class to be the final class in the hierarchy of\nclasses."
				+ "\n\nAn example of a common class would be:\n\npublic Class CommonClass{\n\n\n}";

		JTextArea area = new JTextArea(str, 10, 35);
		area.setEditable(false);

		JScrollPane spane;

		spane = new JScrollPane(area);

		main.addActionListener(eh);
		defaultC.addActionListener(eh);

		p.add(spane);
		p.add(main);
		p.add(defaultC);

		p.setBackground(cColor);
		return p;
	}

	/*
	 * builds class description panel that contains only the default constructor
	 * button
	 */
	private JPanel buildConstructorClassTextPanel() {
		JPanel p = new JPanel();
		p.setSize(500, 200);
		JButton defaultC = new JButton("default constructor");

		defaultC.addActionListener(eh);

		String str = "A class is the most general representation of an object. In each \n"
				+ "class there exists the blueprint for all the objects made from it."
				+ "\n\nFor example if a class called bicycle was made, it would represent all"
				+ "\nthe properties and  functionality of  bicycles. A single bicycle would"
				+ "\nbe an instance of the class bicycle, an object called bicycle."
				+ "\n\nEach class is named based on a couple of modifiers and a name\n(that is usually capitalized)."
				+ "\n\nmodifiers \n\npublic: sets the visability of the class so that it can be accessed"
				+ "\nby other classes.\n\nprivate: sets the visability of the class so that it can be accessed"
				+ "\nonly by the class containing it.\n\nabstract: designates the class as a container for further,more specific"
				+ "\n,classes thus creating a hierarchy of classes.These classes can now\nhave abstract methods.\n\n"
				+ "final: designates the class to be the final class in the hierarchy of\nclasses."
				+ "\n\nAn example of a common class would be:\n\npublic Class CommonClass{\n\n\n}";

		JTextArea area = new JTextArea(str, 10, 35);
		area.setEditable(false);
		JScrollPane spane;

		spane = new JScrollPane(area);

		p.add(spane);

		p.add(defaultC);

		p.setBackground(cColor);
		return p;
	}

	/*
	 * builds class description panel that contains no other buttons
	 */
	private JPanel buildEmptyClassTextPanel() {
		JPanel p = new JPanel();
		p.setSize(500, 200);
		p.setBackground(cColor);
		String str = "A class is the most general representation of an object. In each \n"
				+ "class there exists the blueprint for all the objects made from it."
				+ "\n\nFor example if a class called bicycle was made, it would represent all"
				+ "\nthe properties and  functionality of  bicycles. A single bicycle would"
				+ "\nbe an instance of the class bicycle, an object called bicycle."
				+ "\n\nEach class is named based on a couple of modifiers and a unique\nname (that is usually capitalized)."
				+ "\n\nmodifiers \n\npublic: sets the visability of the class so that it can be accessed"
				+ "\nby other classes.\n\nprivate: sets the visability of the class so that it can be accessed"
				+ "\nonly by the class containing it.\n\nabstract: designates the class as a container for further,more specific"
				+ "\n,classes thus creating a hierarchy of classes.These classes can now\nhave abstract methods.\n\n"
				+ "final: designates the class to be the final class in the hierarchy of\nclasses."
				+ "\n\nAn example of a common class would be:\n\npublic Class CommonClass{\n\n\n}";

		JTextArea area = new JTextArea(str, 10, 35);
		area.setEditable(false);
		JScrollPane spane;

		spane = new JScrollPane(area);

		p.add(spane);

		return p;
	}

	/*
	 * builds method description panel for when method button is clicked
	 */
	private JPanel buildMethodTextPanel() {
		JPanel p = new JPanel();
		p.setSize(500, 200);
		p.setBackground(mColor);

		String str = "A method is a section of code within a class that carries out a\nsepecific funtion."
				+ "the method performs its funtion when it is called\nat some point in the class simply by typing its name.\n\n"
				+ "The method is named by defining some modifiers, a return type and\na unique name."
				+ "\n\nmodifiers\n\npublic: sets the visability of the method so that any class can call"
				+ "\nthis method. This method can be overridden by a class lower in a\nclass hierarchy."
				+ "\n\nprivate: only the class containing the method may call it. Method\ncannot be overridden."
				+ "\n\nprotected: only classes that are lower within the class hierarchy may\ncall this method."
				+ "\n\nabstract: (only for abstract classes) defined a method who functionality\nwill be only specified in class lower in the class hierarchy\n"
				+ "(these classes must contain these overridden methods)."
				+ "\n\nfinal: this class cannout be overwridden by any other class, unlike\nprivate, it can be used outside its containing class, but cannot be"
				+ "\noverridden.\n\nstatic: these methods are for classes and not for objects like the rest."
				+ "\nthey can be called without an object being reference unlike the other\nmodifiers. They can access static data, but not instance varables."
				+ "\n\nreturn types\nthere are many possible data types to return, but this program only\nhandles the following:"
				+ "\n\nint: method must return data in the Integer format when it is called."
				+ "\n\ndouble: method must return data in the double format when it is called."
				+ "\n\nfloat: method must return data in the float format when it is called."
				+ "\n\nString: method must return data in the String format when it is called."
				+ "\n\nvoid: method must preform an action and does not return any data\ntype."
				+ "\n\nnames for methods usually start with the first word uncapitalized"
				+ "\nwith the first letter every proceeding word captialized."
				+ "\n\nAn example of a common method would be:\n\npublic Int getInt(){\n\n}";

		JTextArea area = new JTextArea(str, 10, 35);
		area.setEditable(false);
		JScrollPane spane;

		spane = new JScrollPane(area);

		p.add(spane);
		return p;
	}

	/*
	 * builds variable description panel for when variable button is clicked
	 */
	private JPanel buildVariableTextPanel() {
		JPanel p = new JPanel();
		p.setSize(500, 200);
		p.setBackground(varColor);
		String str = "Variables are the specific data that define something in a program. This"
				+ "\ndata is stored in the form of specific data types such a integers "
				+ "\nor strings."
				+ "\n\nThe scope of the variable determines where it can be referenced or"
				+ "\nchanged. This means that if a variable is created at the class level,"
				+ "\nevery part of the class has access to the variable, if it is created at"
				+ "\nthe method level, it can only be used within that particular method."
				+ "\n\ninstance varables define the propeties of an object, static(or class)"
				+ "\nvariablesdefine the properties of a class."
				+ "\n\nA variable is named by defining a visibility modifier, deciding weather"
				+ "\nthe varable is an insatnce or static(class) variable, a data type, and"
				+ "\na unique name."
				+ "\n\nvisibility modifiers"
				+ "\n\npublic: the variable can be accessed out side of its containing "
				+ "\nclass."
				+ "\n\nprivate: variable can only be accessed within its containing class."
				+ "\n\nprotected: varable can be accessed by a class lower in its class"
				+ "\nhierarchy ."
				+ "\n\nthere are many data types for varables to be,but this program only"
				+ "\nhandles the following: "
				+ "\n\nString: variable is a String."
				+ "\n\nint:  variable is a  integer."
				+ "\n\ndouble: variable is a  double."
				+ "\n\nfloat: variable is a  float."
				+ "\n\nnames for methods usually start with the first word uncapitalized"
				+ "\nwith the first letter every proceeding word captialized."
				+ "\n\nAn example of a common variable would be:"
				+ "\n\npublic int integerValue;";

		JTextArea area = new JTextArea(str, 10, 35);

		JScrollPane spane;

		spane = new JScrollPane(area);

		p.add(spane);
		return p;
	}

	/*
	 * builds status bar to verbally show what creation screen is being
	 * displayed
	 */
	private JPanel createStatusBar() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		statusLabel = new JLabel("Create Class selected");

		panel.add(statusLabel, BorderLayout.CENTER);

		panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return panel;
	}

	/*
	 * builds the panel that allows the user to create variables
	 */
	private JPanel buildCreateVariablePanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 100));
		p.setBackground(varColor);
		p.setLayout(new GridLayout(2, 4));

		JLabel visableLabel = new JLabel("            Visability");
		JLabel staticLabel = new JLabel("   instance or class?");
		JLabel returnLabel = new JLabel("         data type");
		JLabel nameLabel = new JLabel("             name");

		String[] visiabilityOptions = { "public", "private", "protected" };
		String[] staticOptions = { "static", "no" };
		String[] returnOptions = { "String", "int", "double", "float", };
		visableVar = new JComboBox(visiabilityOptions);
		staticSVar = new JComboBox(staticOptions);
		returnTVar = new JComboBox(returnOptions);

		visableVar.addItemListener(this);
		staticSVar.addItemListener(this);
		returnTVar.addItemListener(this);

		p.add(visableLabel);
		p.add(staticLabel);
		p.add(returnLabel);
		p.add(nameLabel);
		p.add(visableVar);
		p.add(staticSVar);
		p.add(returnTVar);
		p.add(vName);

		return p;
	}

	/*
	 * builds the panel that allows the user to create classeseate Class Panel
	 */
	private JPanel buildCreateClassPanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 100));
		p.setBackground(cColor);
		p.setLayout(new GridLayout(2, 3));

		JLabel visableLabel = new JLabel("                  Visability");
		JLabel abstractLabel = new JLabel("   abstract or final modifiers");
		JLabel nameLabel = new JLabel("                     name");

		String[] visiabilityOptions = { "public", "private" };
		String[] hierarchyOptions = { "neither", "abstract", "final" };
		visableC = new JComboBox(visiabilityOptions);
		hierarchyC = new JComboBox(hierarchyOptions);

		visableC.addItemListener(this);
		hierarchyC.addItemListener(this);

		p.add(visableLabel);
		p.add(abstractLabel);
		p.add(nameLabel);
		p.add(visableC);
		p.add(hierarchyC);
		p.add(cName);

		return p;
	}

	/*
	 * builds the panel that allows the user to create methods
	 */
	private JPanel buildCreateMethodPanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 100));
		p.setBackground(mColor);
		p.setLayout(new GridLayout(2, 5));

		JLabel visableLabel = new JLabel("        Visability");
		JLabel abstractLabel = new JLabel(" other modifiers");
		JLabel staticLabel = new JLabel("         static?");
		JLabel returnLabel = new JLabel("     return type");
		JLabel nameLabel = new JLabel("          name");

		String[] visiabilityOptions = { "public", "private", "protected" };
		String[] hierarchyOptions = { "neither", "abstract", "final" };
		String[] staticOptions = { "static", "no" };
		String[] returnOptions = { "void", "String", "int", "double", "float", };
		visableM = new JComboBox(visiabilityOptions);
		hierarchyM = new JComboBox(hierarchyOptions);
		staticSM = new JComboBox(staticOptions);
		returnTM = new JComboBox(returnOptions);

		visableM.addItemListener(this);
		hierarchyM.addItemListener(this);
		staticSM.addItemListener(this);
		returnTM.addItemListener(this);

		p.add(visableLabel);
		p.add(abstractLabel);
		p.add(staticLabel);
		p.add(returnLabel);
		p.add(nameLabel);
		p.add(visableM);
		p.add(hierarchyM);
		p.add(staticSM);
		p.add(returnTM);
		p.add(mName);

		return p;
	}

	/*
	 * builds panel that is displayed when finish is click, thus preventing any
	 * further program elements from being created for that class
	 */
	private JPanel buildCreateNothingPanel() {
		JPanel p = new JPanel();
		p.setBackground(cColor);
		p.setPreferredSize(new Dimension(500, 100));
		return p;
	}

	/*
	 * builds a specific label for when a comment is created so a user is
	 * reminded of what they commented
	 */
	private JPanel ReplaceCommentDialog(int ID, int in) {
		JPanel p = new JPanel();
		if (ID > 0) {
			int index = 0;
			if (in == Integer.MAX_VALUE) {
				index = visualPanel.getSelectedIndex();
			} else {
				index = in;
			}
			ProgramElements rC = null;
			for (ProgramElements pro : myProgramElements) {
				if (pro.getClassID() == ID && pro.getPosition() == (index - 1)) {
					rC = pro;
				}
			}

			JLabel c = new JLabel("current comment for "
					+ rC.getOriginalCommentName());
			p.add(c);
		}
		return p;
	}

	/*
	 * builds a text field for a user to replace a selected comment
	 */
	private JPanel ReplaceCommentPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setPreferredSize(new Dimension(500, 100));

		JPanel pan = ReplaceCommentDialog(0, 0);
		commentTitleCards.add("0defualt0", pan);
		commentReplace = new JTextField();
		p.add(commentReplace, BorderLayout.CENTER);
		p.add(commentTitleCards, BorderLayout.NORTH);

		return p;
	}

	/*
	 * builds a containing panel of all the created classes selection buttons,
	 * update buttons, and finish or continue working buttons
	 */
	private JPanel buildSelectionPanel() {

		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 400));
		p.setBackground(Color.lightGray);
		p.setLayout(new BorderLayout());

		classPanel = buildClassPanel();

		JScrollPane cSpane = new JScrollPane(classPanel);

		cSpane.setPreferredSize(new Dimension(500, 350));

		classCollectionCards.add("classPanel", cSpane);

		classUpdatePanel = buildAddOnlyUpdateClassPanel();

		UpdateCards.add("add only update panel", classUpdatePanel);

		JPanel pan = Finishbutton();

		FinishCards.add("finish button", pan);

		p.add(UpdateCards, BorderLayout.NORTH);
		p.add(FinishCards, BorderLayout.SOUTH);
		p.add(classCollectionCards, BorderLayout.CENTER);
		classUpdatePanel = buildNoReplaceUpdateClassPanel();

		UpdateCards.add("no replace update panel", classUpdatePanel);

		classUpdatePanel = buildUpdateClassPanel();

		UpdateCards.add("full update panel", classUpdatePanel);

		classUpdatePanel = buildExportUpdateClassPanel();

		UpdateCards.add("add export update panel", classUpdatePanel);

		classUpdatePanel = buildUpdateReplaceDeletePanel();
		UpdateCards.add("replace and delete panel", classUpdatePanel);

		JPanel c = ContinueWorkButton();
		FinishCards.add("continue working", c);
		return p;
	}

	/*
	 * creates a finish button at the bottom of the creation panel that allows
	 * the user to complete their class and export it ,this prevents the user
	 * from making changes to the class
	 */
	private JPanel Finishbutton() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 50));
		JButton finish = new JButton("finish");
		finish.setPreferredSize(new Dimension(500, 30));
		finish.setBackground(Color.orange);
		finish.addActionListener(eh);
		p.add(finish);
		return p;
	}

	/*
	 * creates a continue work button at the bottom of the creation panel that
	 * allows the user to un-finish the class and make changes to it, this
	 * prevents the user from exporting the class
	 */
	private JPanel ContinueWorkButton() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 50));
		JButton con = new JButton("continue working");
		con.setPreferredSize(new Dimension(500, 30));
		con.setBackground(Color.orange);
		con.addActionListener(eh);
		p.add(con);
		return p;
	}

	/*
	 * builds the collection of created classes selection buttons to allow the
	 * user to switch between their classes
	 */
	private JPanel buildClassPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		for (SelectionButton s : classSelectionButton) {
			s.setMaximumSize(new Dimension(500, 35));
			s.setBackground(cColor);
			s.addActionListener(eh);
			p.add(s);
		}

		return p;
	}

	/*
	 * builds the update panel with the add, delete and replace button visible
	 * valid only in certain circumstances, such as if a class is selected and
	 * the class button is clicked or if a method or variable is selected and
	 * the method or variable button is clicked
	 */
	private JPanel buildUpdateClassPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setPreferredSize(new Dimension(500, 50));
		JButton add = new JButton("Add!");
		add.setBackground(Color.cyan);
		add.addActionListener(eh);

		JButton delete = new JButton("delete");
		delete.setBackground(Color.pink);
		delete.addActionListener(eh);

		delete.setPreferredSize(new Dimension(150, 25));
		add.setPreferredSize(new Dimension(170, 25));

		JButton replace = new JButton("replace");
		replace.setBackground(Color.magenta);
		replace.addActionListener(eh);
		replace.setPreferredSize(new Dimension(160, 25));
		classCollectionPanel = buildClassCollectionPanel();

		p.add(add, BorderLayout.EAST);
		p.add(delete, BorderLayout.CENTER);
		p.add(replace, BorderLayout.WEST);

		p.add(classCollectionPanel, BorderLayout.SOUTH);

		return p;
	}

	/*
	 * builds update panel that only has the delete and replace buttons visible
	 * this is for when a comment is selected in the display panel
	 */
	private JPanel buildUpdateReplaceDeletePanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JButton delete = new JButton("delete");
		delete.setBackground(Color.pink);
		delete.addActionListener(eh);

		delete.setPreferredSize(new Dimension(250, 25));

		JButton replace = new JButton("replace");
		replace.setBackground(Color.magenta);
		replace.addActionListener(eh);
		replace.setPreferredSize(new Dimension(250, 25));
		classCollectionPanel = buildClassCollectionPanel();

		p.add(delete, BorderLayout.CENTER);
		p.add(replace, BorderLayout.WEST);

		p.add(classCollectionPanel, BorderLayout.SOUTH);

		return p;
	}

	/*
	 * builds the update panel with only the add button, this is used when there
	 * are no existing program elements
	 */
	private JPanel buildAddOnlyUpdateClassPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setPreferredSize(new Dimension(500, 50));
		JButton add = new JButton("Add!");
		add.setBackground(Color.cyan);
		add.addActionListener(eh);
		add.setPreferredSize(new Dimension(500, 50));
		p.add(add, BorderLayout.EAST);
		classCollectionPanel = buildClassCollectionPanel();

		p.add(classCollectionPanel, BorderLayout.SOUTH);

		return p;
	}

	/*
	 * update panel that only contains an add and delete button used in the case
	 * when the a class is highlighted on the visual panel but the variable or
	 * method button are clicked or in the instance that a variable or method is
	 * highlighted on the visual panel and the class button is selected this
	 * prevents a user from breaking the format of class on top and below and
	 * methods and variables within
	 */
	private JPanel buildNoReplaceUpdateClassPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setPreferredSize(new Dimension(500, 50));
		JButton add = new JButton("Add!");
		add.setBackground(Color.cyan);
		add.addActionListener(eh);

		JButton delete = new JButton("delete");
		delete.setBackground(Color.pink);
		delete.addActionListener(eh);

		delete.setPreferredSize(new Dimension(250, 25));
		add.setPreferredSize(new Dimension(250, 25));

		classCollectionPanel = buildClassCollectionPanel();

		p.add(add, BorderLayout.EAST);
		p.add(delete, BorderLayout.CENTER);

		p.add(classCollectionPanel, BorderLayout.SOUTH);

		return p;
	}

	/*
	 * replaces the update panel with an export current button when the user
	 * clicks finish
	 */
	private JPanel buildExportUpdateClassPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setPreferredSize(new Dimension(500, 50));
		JButton ex = new JButton("Export Current");
		ex.setBackground(Color.yellow);
		ex.addActionListener(eh);

		ex.setPreferredSize(new Dimension(500, 25));

		classCollectionPanel = buildClassCollectionPanel();

		p.add(ex, BorderLayout.CENTER);

		p.add(classCollectionPanel, BorderLayout.SOUTH);
		return p;
	}

	/*
	 * displays the the label for class button selection
	 */
	private JPanel buildClassCollectionPanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 25));
		JLabel cL = new JLabel("Saved Classes");
		p.add(cL);
		return p;

	}

	/*
	 * builds the menu of the basic program builder with three Jmenu options
	 * file menu, export menu, and extra menu
	 */
	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu exportMenu = buildexportMenu();
		JMenu OptionsMenu = buildOptionsMenu();

		JMenu fileMenu = buildFileMenu();

		menuBar.add(fileMenu);
		menuBar.add(exportMenu);
		menuBar.add(OptionsMenu);

		setJMenuBar(menuBar);

	}

	/*
	 * builds the menu options under the extra menu button this is the add
	 * comment feature
	 */
	private JMenu buildOptionsMenu() {
		JMenu oM = new JMenu("Extra");

		JMenuItem menuItem = new JMenuItem("add comment");
		menuItem.addActionListener(eh);
		oM.add(menuItem);

		return oM;
	}

	/*
	 * builds the menu options under the export menu button these are export
	 * current, and export all
	 */
	private JMenu buildexportMenu() {
		JMenu exportMenu = new JMenu("Export");

		JMenuItem menuItem = new JMenuItem("Export Current");
		menuItem.addActionListener(eh);
		exportMenu.add(menuItem);

		menuItem = new JMenuItem("Export All");
		menuItem.addActionListener(eh);
		exportMenu.add(menuItem);
		return exportMenu;
	}

	/*
	 * builds the menu options under the file menu button these options are new,
	 * save, load, and exit
	 */
	private JMenu buildFileMenu() {
		JMenu fileMenu = new JMenu("File");

		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		return fileMenu;
	}

	/*
	 * sets all the card layouts to display the finished selected class
	 */
	private void ShowFinishMode() {

		((CardLayout) classCollectionCards.getLayout()).show(
				classCollectionCards, "new classes");

		((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
				"add export update panel");

		((CardLayout) FinishCards.getLayout()).show(FinishCards,
				"continue working");

		((CardLayout) ButtonCards.getLayout()).show(ButtonCards,
				"no button panel");

		((CardLayout) CreationCards.getLayout()).show(CreationCards,
				"no creation panel");

		((CardLayout) textCards.getLayout())
				.show(textCards, "class text panel");
		((CardLayout) classTextCards.getLayout()).show(classTextCards,
				"empty class text");
	}

	/*
	 * reads an object file and returns an array list of all the program
	 * elements
	 */
	private ArrayList<ProgramElements> loadFile() throws IOException {
		ArrayList<ProgramElements> p = null;
		JFileChooser fileSelect = new JFileChooser();
		int returnvalue = fileSelect.showOpenDialog(null);
		if (returnvalue == JFileChooser.APPROVE_OPTION) {
			File file = fileSelect.getSelectedFile();

			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);

				// read object from object input stream
				Object o = ois.readObject();

				// cast object correctly
				p = (ArrayList<ProgramElements>) o;
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Problems reading file");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Class information not found");
				e.printStackTrace();
			}
		}
		return p;
	}

	/*
	 * allows user to save their program elements as an object file, name and
	 * location is determined by user
	 * 
	 * @throws IOException
	 */
	private void saveFile() throws IOException {

		JFileChooser fileSelect = new JFileChooser();
		int returnvalue = fileSelect.showSaveDialog(null);
		if (returnvalue == JFileChooser.APPROVE_OPTION) {
			File file = fileSelect.getSelectedFile();

			try {
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				// write object (i.e. state of object) to object output stream

				oos.writeObject(myProgramElements);

			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Problems writing file!");
				e.printStackTrace();
			}

		}

	}

	/*
	 * checks method and variable names to be unique within their selected class
	 * 
	 * @param name
	 * @param type
	 * @return weather or not the name already exists in the selected class
	 */
	private boolean checkForUniqueName(String name, String type) {

		for (ProgramElements p : myProgramElements) {
			if (p.getName().equals(name) && p.getClassID() == classID
					&& p.getType().equals(type)) {
				return false;
			}
		}

		return true;

	}

	/*
	 * re orders the program elements fist by class id and then by the position
	 * of the object in the JList to make saving, loading and exporting much
	 * easier since all the elements are always in order.
	 */
	private void reOrderMyProgams() {

		int numberOfClasses = 0;

		int index = 0;

		for (ProgramElements p : myProgramElements) {
			if (p.getType().equals("class") && p.isfinished() == false) {
				numberOfClasses++;

			}
		}

		int max = 0;
		for (ProgramElements p : myProgramElements) {
			if (p.getPosition() > max) {
				max = p.getPosition();
			}
		}

		ArrayList<ProgramElements> tempProgram = new ArrayList<ProgramElements>();
		ArrayList<ProgramElements> tempProgramClassID = new ArrayList<ProgramElements>();
		ArrayList<ProgramElements> tempProgramPos = new ArrayList<ProgramElements>();

		int[] classIDs = new int[numberOfClasses];
		int[] numberOfPositions = new int[numberOfClasses];

		for (ProgramElements p : myProgramElements) { // get all existing class
														// ids
			if (p.getPosition() == 0) {
				classIDs[index] = p.getClassID();
				index++;
			}
		}

		for (int i = 0; i < numberOfPositions.length; i++) { // get how many
																// elements are
																// in each class

			int size = 0;

			for (ProgramElements p : myProgramElements) {
				if (p.getClassID() == classIDs[i]) {
					size++;
				}
			}
			numberOfPositions[i] = size;
		}

		int p = 0;

		while (p < numberOfClasses) {
			int count = 0;
			int pos = 0;
			for (ProgramElements pro : myProgramElements) { // get all the
															// elements with the
															// same class ids
				if (pro.getClassID() == classIDs[p]) {
					tempProgramClassID.add(pro);
					count++;
				}
			}

			if (count == numberOfPositions[p]) {

				while (pos < numberOfPositions[p]) { // reorder the specific
														// class by the position
														// values of the
														// elements
					for (ProgramElements t : tempProgramClassID) {
						if (t.getPosition() == pos) {
							tempProgramPos.add(t);
							pos++;
						}
					}
				}

			}

			if (pos == numberOfPositions[p]) { // add the reordered elemnts to a
												// temp array
				for (ProgramElements t : tempProgramPos) {
					tempProgram.add(t);
				}
				tempProgramClassID = new ArrayList<ProgramElements>();
				tempProgramPos = new ArrayList<ProgramElements>();
				p++;
			}
		}

		myProgramElements = new ArrayList<ProgramElements>(); // emtpy array
																// list

		for (ProgramElements t : tempProgram) { // place the sorted array back
												// into the array list
			myProgramElements.add(t);

		}

	}

	/*
	 * generates a unique name for each class, method, and variable, by checking
	 * the class name array list and the variable and method vales saved on the
	 * selected class
	 */
	private void resetTextField() {

		int numberOfSpecificMethods = 1;
		for (ProgramElements p : myProgramElements) {
			if (p.getClassID() == classID && p.getType().equals("class")
					&& p.isfinished() == false) {
				numberOfSpecificMethods = p.getHighestMethod();
			}
		}

		int numberOfSpecificVariables = 1;
		for (ProgramElements p : myProgramElements) {
			if (p.getClassID() == classID && p.getType().equals("class")
					&& p.isfinished() == false) {
				numberOfSpecificVariables = p.getHighestVariable();
			}
		}

		mName.setText("method" + numberOfSpecificMethods);
		cName.setText("Class"
				+ (classUniqueDefaultNameGenerator
						.get(classUniqueDefaultNameGenerator.size() - 1) + 1));
		vName.setText("property" + numberOfSpecificVariables);
	}

	/*
	 * handles all the button clicks and list changes for the basic program
	 * builder 
	 */
	private class EventHandler implements ActionListener, ListSelectionListener {
		/**
		 * method to handle all the JButtons being clicked add will create a new
		 * class, method or variable based on the class, method or variable button
		 * being clicked.
		 * replace allows class names and modifiers to replace currently selected one,
		 * a method or variable with a method or variable, and comments with other 
		 * comments, if a commented method or variable is replaced, its comment will
		 * be deleted
		 * the class, method, and variable buttons change the creation screens and force every
		 * other action to be based around their selected program element
		 * delete, will delete individual methods, variables, and comments,but will remove
		 * all the program elements of a class that is deleted, if a method or variable is
		 * commented, its comment will also be deleted.
		 * finish will create an ending bracket at the end of the class and allow user to export
		 * the class
		 * continue working removes the bracket and allows user to continue editing the class, but
		 * can no longer export
		 * export current exports the currently selected class
		 * export all exports all classes
		 * new erases all the program elements and recreates the opening view form the program
		 * save allows the user to save their work to an object file
		 * open reads in an object file and populated the display panel with the first class and all
		 * appropriate selection buttons
		 * clicking a selection button populates the display panel with all the program elements of the
		 * selected class
		 * add comment allows the user to add one comment to a variable or method
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();

			if (s.equals("Add!")) {

				int index = visualPanel.getSelectedIndex(); // get selected
															// index
				if (index == -1) { // no selection, so insert at beginning

					index = 0;

				} else { // add after the selected item
					index++;
				}

				if (selectedCreatedPanel.equals("class")) {

					((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
							"full update panel");

					int count = 1;
					for (ProgramElements p : myProgramElements) {
						if (p.getType().equals("class"))
							count++;
					}
					index = 0;
					String n = cName.getText();

					int checkName = 0;

					if (count > 1) {
						for (ProgramElements p : myProgramElements) {
							if (p.getName().equals(n)) {
								checkName++;
							}
						}
					}

					className = n;

					if (checkName == 0) {
						Class c = new Class(visability, hierarchy, className);
						c.setClassID(count);
						classID = count;
						SelectionButton b = new SelectionButton(c.getName());
						classSelectionButton.add(b);
						myProgramElements.add(c);

						if (c.getClassID() == 1) {
							((CardLayout) ButtonCards.getLayout()).show(
									ButtonCards, "full button panel");
						}

						classPanel = buildClassPanel();
						JScrollPane cSpane = new JScrollPane(classPanel);
						cSpane.setPreferredSize(new Dimension(500, 400));

						classCollectionCards.add("new classes", cSpane);

						((CardLayout) classCollectionCards.getLayout()).show(
								classCollectionCards, "new classes");

						visual.clear();
						c.setPosition(index);
						classUniqueDefaultNameGenerator.add(c.getClassID());
						visual.insertElementAt(c.printToList(), index);
						classTextPanel = buildFullClassTextPanel();
						classTextCards.add("full text panel", classTextPanel);
						((CardLayout) classTextCards.getLayout()).show(
								classTextCards, "full text panel");

						classTextPanel = buildConstructorClassTextPanel();
						classTextCards.add("constructor text panel",
								classTextPanel);
					} else
						JOptionPane
								.showMessageDialog(null,
										"each class in your program must have a unique name");
				} else if (selectedCreatedPanel.equals("method")) {

					String n = mName.getText();
					className = n;
					if (checkForUniqueName(n, "method")) {
						Method m = new Method(visability, hierarchy,
								returnType, className, staticSelect);
						if (classID == 0) {
							m.setClassID(1);
						} else {
							m.setClassID(classID);
						}
						ProgramElements classOfMethod = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getClassID() == classID
									&& p.getType().equals("class")
									&& p.isfinished() == false) {
								classOfMethod = p;
							}
						}
						classOfMethod.setHighestMethod(1);
						myProgramElements.add(m);
						m.setPosition(index);
						for (ProgramElements p : myProgramElements) {
							if ((p.getPosition() == m.getPosition() || p
									.getPosition() > m.getPosition())
									&& p != m
									&& p.getClassID() == classID) {
								p.setPosition(p.getPosition() + 1);
							}
						}
						visual.insertElementAt(m.printToList(), index);

					} else {
						ProgramElements className = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getType().equals("class")
									&& p.getClassID() == classID) {
								className = p;
							}
						}
						JOptionPane.showMessageDialog(null,
								"You must have a unique name for every method in "
										+ className.getName());
					}

				} else if (selectedCreatedPanel.equals("instance")) {

					String n = vName.getText();
					className = n;
					if (checkForUniqueName(n, "instance")) {
						Variable iV = new Variable(visability, staticSelect,
								returnType, className);

						if (classID == 0) {
							iV.setClassID(1);
						} else {
							iV.setClassID(classID);
						}

						myProgramElements.add(iV);
						ProgramElements classOfVariable = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getClassID() == classID
									&& p.getType().equals("class")
									&& p.isfinished() == false) {
								classOfVariable = p;
							}
						}
						classOfVariable.setHighestVariable(1);
						iV.setPosition(index);
						for (ProgramElements p : myProgramElements) {
							if ((p.getPosition() == iV.getPosition() || p
									.getPosition() > iV.getPosition())
									&& p != iV
									&& p.getClassID() == classID) {
								p.setPosition(p.getPosition() + 1);
							}
						}
						visual.insertElementAt(iV.printToList(), index);
					} else {
						ProgramElements className = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getType().equals("class")
									&& p.getClassID() == classID) {
								className = p;
							}
						}
						JOptionPane.showMessageDialog(null,
								"You must have a unique name for every variable in "
										+ className.getName());
					}
				}
				reOrderMyProgams();
				visualPanel.setSelectedIndex(index);

				int classCount = 1;
				int methodCount = 1;
				int variableCount = 1;

				for (ProgramElements p : myProgramElements) {
					if (p.getPosition() == 0) {
						classCount++;
					}

					if (p.getType().equals("method")
							&& p.getClassID() == classID) {
						methodCount++;
					}

					if (p.getType().equals("instance")
							&& p.getClassID() == classID) {
						variableCount++;
					}
				}

				resetTextField();

			} else if (s.equals("replace")) {
				int index = visualPanel.getSelectedIndex();
				int commentCheck = 0;
				ProgramElements selectedp = null;

				ProgramElements classOfReplacement = null;

				for (ProgramElements p : myProgramElements) {

					if (p.getPosition() == index && p.getClassID() == classID) {
						selectedp = p;
					}
				}

				for (ProgramElements p : myProgramElements) {
					if (p.getClassID() == selectedp.getClassID()
							&& p.getType().equals("class")) {
						classOfReplacement = p;
					}
				}

				if (selectedp.isComment()) { //replaces comment with other comment
					visual.clear();
					String comment = commentReplace.getText();

					selectedp.setComment(comment);

					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == classID) {
							visual.insertElementAt(p.printToList(),
									p.getPosition());
						}
					}
				} else if (selectedp.getType().equals("class")) {//replaces class data with new class data

					SelectionButton cB = null;
					for (SelectionButton sel : classSelectionButton) {
						if (sel.getText().equals(selectedp.getName())) {
							cB = sel;
						}
					}
					String name = cName.getText();
					int nameCheck = 0;
					for (ProgramElements p : myProgramElements) {
						if (p.getName().equals(name)) {
							nameCheck++;
						}
					}
					if (nameCheck == 0) {
						selectedp.setName(name);
						selectedp.setHierarchy(hierarchy);
						selectedp.setVisability(visability);
						cB.setName(name);
						cB.setText(name);
						visual.remove(index);
						visual.insertElementAt(selectedp.printToList(), index);
					} else {
						JOptionPane.showMessageDialog(null,
								"every class in a program \n"
										+ "must have a unique name");
					}
				} else if (selectedp.getType().equals("method")) {
					selectedp.removedComment();
					if (methodB == true) {//replaces method with other method 
						ProgramElements comment = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getName().equals(
									selectedp.getName() + "c"
											+ selectedp.getClassID())) {
								commentCheck++;
								comment = p;
							} else if (p.getName().equals(
									selectedp.getClassName() + "'s main method"
											+ "c" + selectedp.getClassID()) //if replacing main method
									&& selectedp.isMain()) {
								commentCheck++;
								comment = p;
							} else if (p.getName().equals(
									selectedp.getClassName()
											+ "'s default Constructor"
											+ selectedp.getPosition() + "c"
											+ selectedp.getClassID()) //if replacing default constructor
									&& p.isConstructor()) {
								commentCheck++;
								comment = p;
							}
						}
						if (comment != null) {
							visual.remove(comment.getPosition());
							myProgramElements.remove(comment);
						}

						String name = mName.getText();
						if (checkForUniqueName(name, "method")) {
							selectedp.setName(name);
							selectedp.setStatic(staticSelect);
							selectedp.setReturnType(returnType);
							selectedp.setVisability(visability);
							selectedp.setHierarchy(hierarchy);
							selectedp.setPosition(selectedp.getPosition()
									- commentCheck);
							if (selectedp.isMain()) {
								classOfReplacement.addMain(false);

								((CardLayout) classTextCards.getLayout()).show(
										classTextCards, "full text panel");
							}

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID
										&& p.getPosition() > selectedp
												.getPosition()) {
									p.setPosition(p.getPosition() - commentCheck);
								}
							}

							if (selectedp.isMain()) {
								selectedp.removeMain();
								ProgramElements classOfMethod = null;
								for (ProgramElements p : myProgramElements) {
									if (p.getType().equals("class")
											&& p.getClassID() == classID
											&& p.isfinished() == false) {
										classOfMethod = p;
									}
								}
								classOfMethod.setHighestMethod(1);
								resetTextField();
							}
							reOrderMyProgams();
							visual.clear();

							checkClassTextCorrectness();

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID) {
									visual.insertElementAt(p.printToList(),
											p.getPosition());
								}
							}
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"every method name must be unique\n"
													+ " withing their respective class");
						}
					} else if (instanceB == true) { //replaces method with variable
						ProgramElements comment = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getName().equals(
									selectedp.getName() + "c"
											+ selectedp.getClassID())) {
								commentCheck++;
								comment = p;
							} else if (p.getName().equals(
									selectedp.getClassName() + "'s main method"
											+ "c" + selectedp.getClassID()) //if replacing main method
									&& selectedp.isMain()) {
								commentCheck++;
								comment = p;
							} else if (p.getName().equals(
									selectedp.getClassName()
											+ "'s default Constructor"
											+ selectedp.getPosition() + "c"
											+ selectedp.getClassID())//if replacing default constructor
									&& p.isConstructor()) {
								commentCheck++;
								comment = p;
							}
						}
						if (comment != null) {
							visual.remove(comment.getPosition());
							myProgramElements.remove(comment);
						}
						String n = vName.getText();
						className = n;
						if (checkForUniqueName(n, "instance")) {
							int id = selectedp.getClassID();
							myProgramElements.remove(selectedp);
							Variable i = new Variable(visability, staticSelect,
									returnType, className);
							i.setClassID(id);
							i.setPosition(index - commentCheck);
							myProgramElements.add(i);

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID
										&& p.getPosition() > i.getPosition()) {
									p.setPosition(p.getPosition() - commentCheck);
								}
							}
							reOrderMyProgams();
							visual.clear();

							checkClassTextCorrectness();

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID) {
									visual.insertElementAt(p.printToList(),
											p.getPosition());
								}
							}

						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"every variable name must be unique\n"
													+ " withing their respective class");
						}
					}
				} else if (selectedp.getType().equals("instance")) { //replaces variable with new variable
					selectedp.removedComment();
					if (instanceB == true) {
						ProgramElements comment = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getName().equals(
									selectedp.getName() + "c"
											+ selectedp.getClassID())) {
								commentCheck++;
								comment = p;
							}
						}
						if (comment != null) {
							visual.remove(comment.getPosition());
							myProgramElements.remove(comment);
						}
						String name = vName.getText();
						if (checkForUniqueName(name, "instance")) {
							selectedp.setName(name);
							selectedp.setStatic(staticSelect);
							selectedp.setReturnType(returnType);
							selectedp.setVisability(visability);
							selectedp.setPosition(selectedp.getPosition()
									- commentCheck);

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID
										&& p.getPosition() > selectedp
												.getPosition()) {
									p.setPosition(p.getPosition() - commentCheck);
								}
							}
							reOrderMyProgams();
							visual.clear();

							checkClassTextCorrectness();

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID) {
									visual.insertElementAt(p.printToList(),
											p.getPosition());
								}
							}
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"every variable name must be unique\n"
													+ " withing their respective class");
						}
					} else if (methodB == true) { //replaces variable with method
						int id = selectedp.getClassID();
						ProgramElements comment = null;
						for (ProgramElements p : myProgramElements) {
							if (p.getName().equals(
									selectedp.getName() + "c"
											+ selectedp.getClassID())) {
								commentCheck++;
								comment = p;
							}
						}
						if (comment != null) {
							visual.remove(comment.getPosition());
							myProgramElements.remove(comment);
						}
						myProgramElements.remove(selectedp);
						String n = mName.getText();
						if (checkForUniqueName(n, "method")) {
							className = n;
							Method m = new Method(visability, hierarchy,
									returnType, className, staticSelect);
							m.setClassID(id);
							m.setPosition(index - commentCheck);
							myProgramElements.add(m);

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID
										&& p.getPosition() > m.getPosition()) {
									p.setPosition(p.getPosition() - commentCheck);
								}
							}
							reOrderMyProgams();
							visual.clear();
							checkClassTextCorrectness();

							for (ProgramElements p : myProgramElements) {
								if (p.getClassID() == classID) {
									visual.insertElementAt(p.printToList(),
											p.getPosition());
								}
							}
						}
					}

					System.out.println(commentCheck);
					resetTextField();
				} else {
					JOptionPane.showMessageDialog(null,
							"every method name must be unique\n"
									+ " withing their respective class");
				}
				visualPanel.setSelectedIndex(index - commentCheck);
			} else if (s.equals("Class")) {
				int index = visualPanel.getSelectedIndex();
				classB = true;
				instanceB = false;
				methodB = false;

				visableC.setSelectedIndex(0);
				hierarchyC.setSelectedIndex(0);

				resetTextField();

				visability = "public";
				hierarchy = "n";
				selectedCreatedPanel = "class";

				int commentCheck = 0;
				for (ProgramElements p : myProgramElements) {
					if (p.getPosition() == index && p.getClassID() == classID
							&& p.isComment()) {
						commentCheck++;
					}
				}
				if (commentCheck == 0) {
					if (index == 0) {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "full update panel");
					} else {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "no replace update panel");
					}

					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create class panel");
				}
				methodCreationSelect.setBackground(Color.green);
				classCreationSelect.setBackground(Color.RED);
				instanceVariableCreationSelect.setBackground(Color.green);

				((CardLayout) textCards.getLayout()).show(textCards,
						"class text panel");
				selectedCreatedPanel = "class";
				statusLabel.setText("Create Class Selected");
				for (SelectionButton b : classSelectionButton) {
					b.setBackground(cColor);
				}
				checkClassTextCorrectness();
			} else if (s.equals("Method")) {

				for (SelectionButton b : classSelectionButton) {
					b.setBackground(mColor);
				}

				resetTextField();
				visability = "public";
				returnType = "void";
				hierarchy = "n";
				staticSelect = true;

				int index = visualPanel.getSelectedIndex();
				classB = false;
				instanceB = false;
				methodB = true;
				visableM.setSelectedIndex(0);
				hierarchyM.setSelectedIndex(0);
				staticSM.setSelectedIndex(0);
				returnTM.setSelectedIndex(0);
				int commentCheck = 0;
				for (ProgramElements p : myProgramElements) {
					if (p.getPosition() == index && p.getClassID() == classID
							&& p.isComment()) {
						commentCheck++;
					}
				}
				if (commentCheck == 0) {
					if (index > 0) {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "full update panel");
					} else {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "no replace update panel");
					}
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create method panel");

				}
				methodCreationSelect.setBackground(Color.RED);
				classCreationSelect.setBackground(Color.green);
				instanceVariableCreationSelect.setBackground(Color.green);
				((CardLayout) textCards.getLayout()).show(textCards,
						"method text panel");
				selectedCreatedPanel = "method";
				statusLabel.setText("Create Method Selected");
			} else if (s.equals("Variable")) { 

				visability = "public";
				returnType = "string";
				staticSelect = true;

				resetTextField();
				visableVar.setSelectedIndex(0);
				staticSVar.setSelectedIndex(0);
				returnTVar.setSelectedIndex(0);
				int index = visualPanel.getSelectedIndex();
				classB = false;
				instanceB = true;
				methodB = false;
				int commentCheck = 0;
				for (ProgramElements p : myProgramElements) {
					if (p.getPosition() == index && p.getClassID() == classID
							&& p.isComment()) {
						commentCheck++;
					}
				}
				if (commentCheck == 0) {
					if (index > 0) {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "full update panel");
					} else {
						((CardLayout) UpdateCards.getLayout()).show(
								UpdateCards, "no replace update panel");
					}
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create instance panel");
				}
				methodCreationSelect.setBackground(Color.green); 
				classCreationSelect.setBackground(Color.green);
				instanceVariableCreationSelect.setBackground(Color.RED); 

				for (SelectionButton b : classSelectionButton) {
					b.setBackground(varColor);
				}
				((CardLayout) textCards.getLayout()).show(textCards,
						"instance text panel");
				statusLabel.setText("Create Variable Selected");
				selectedCreatedPanel = "instance";

			} else if (s.equals("delete")) {
				int index = visualPanel.getSelectedIndex();
				int commentCheck = 0;
				ProgramElements deleteP = null;
				for (ProgramElements p : myProgramElements) {
					if (p.getPosition() == index && p.getClassID() == classID) {
						deleteP = p;
					}
				}

				if (deleteP.isComment()) {
					ProgramElements commentItem = null;
					for (ProgramElements p : myProgramElements) {
						if (p.getName()
								.equals(deleteP.getOriginalCommentName())) {
							commentItem = p;
						}
					}
					commentItem.removedComment();
				}

				int id = deleteP.getClassID();
				if (deleteP.getType().equals("class")) {
					SelectionButton sB = null;
					for (SelectionButton sel : classSelectionButton) {
						if (sel.getText().equals(deleteP.getName())) {
							sB = sel;
						}
					}

					int lastClass = 0;
					int classIDCheck = deleteP.getClassID();

					for (ProgramElements p : myProgramElements) {
						if (deleteP.getClassID() == p.getClassID()) {
							lastClass++;
						}
					}

					classSelectionButton.remove(sB);
					classPanel = buildClassPanel();
					JScrollPane cSpane = new JScrollPane(classPanel);
					cSpane.setPreferredSize(new Dimension(500, 400));

					classCollectionCards.add("new classes", cSpane);

					((CardLayout) classCollectionCards.getLayout()).show(
							classCollectionCards, "new classes");

					Iterator<ProgramElements> it = myProgramElements.iterator();
					while (it.hasNext()) { //id a class is deleted , all its elements must also be deleted

						ProgramElements p = it.next();
						if (p.getClassID() == deleteP.getClassID()) {
							it.remove();
						}

					}
					visual.clear();
					int stop = 0;
					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() < classIDCheck) {
							classIDCheck = p.getClassID();
							stop++;
						} else if (p.getClassID() > classIDCheck && stop == 0) {
							classIDCheck = p.getClassID();
							stop++;
						}
					}

					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == classIDCheck) {
							visual.insertElementAt(p.printToList(),
									p.getPosition());
							visualPanel.setSelectedIndex(p.getPosition());
						}
					}
					if (classB == true) {
						checkClassTextCorrectness();
						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create class panel");
						((CardLayout) textCards.getLayout()).show(textCards,
								"class text panel");
						for (SelectionButton button : classSelectionButton) {
							button.setBackground(cColor);
						}

					} else if (methodB == true) {
						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create method panel");
						((CardLayout) textCards.getLayout()).show(textCards,
								"method text panel");
						for (SelectionButton button : classSelectionButton) {
							button.setBackground(mColor);
						}
					} else if (instanceB == true) {
						((CardLayout) textCards.getLayout()).show(textCards,
								"instance text panel");
						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create instance panel");

						for (SelectionButton button : classSelectionButton) {
							button.setBackground(varColor);
						}
					}
					classID = classIDCheck;
				} else if (deleteP.getType().equals("method")) {
					visual.clear();
					if (deleteP.isMain()) {
						for (ProgramElements p : myProgramElements) {
							if (p.getClassID() == deleteP.getClassID()
									&& p.getType().equals("class")) {
								p.addMain(false);
								((CardLayout) classTextCards.getLayout()).show(
										classTextCards, "full text panel");
							}
						}
					}

					commentCheck = 0;
					ProgramElements comment = null;
					for (ProgramElements p : myProgramElements) {
						if (p.isComment()
								&& p.getPosition() == (deleteP.getPosition() - 1)
								&& p.getClassID() == deleteP.getClassID()) {
							comment = p;
							commentCheck++;
						}
					}
					if (comment != null) {
						myProgramElements.remove(comment);
					}

					myProgramElements.remove(deleteP);
					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == id) {
							if (p.getPosition() > index) {
								p.setPosition(p.getPosition()
										- (1 + commentCheck));
							}
							visual.insertElementAt(p.printToList(),
									p.getPosition());
						}
					}
				} else if (deleteP.getType().equals("instance")) {
					commentCheck = 0;
					ProgramElements comment = null;
					for (ProgramElements p : myProgramElements) {
						if (p.getName().equals(
								deleteP.getName() + "c" + deleteP.getClassID())) {
							comment = p;
							commentCheck++;
						}
					}
					if (comment != null) {
						myProgramElements.remove(comment);
					}

					visual.clear();
					myProgramElements.remove(deleteP);
					for (ProgramElements p : myProgramElements) {

						if (p.getClassID() == id && p.getPosition() > index) {
							p.setPosition(p.getPosition() - (1 + commentCheck));
						}
						visual.insertElementAt(p.printToList(), p.getPosition());

					}
				}

				if (myProgramElements.size() == 0) { //show beginning screen if everything is deleted

					((CardLayout) classTextCards.getLayout()).show(
							classTextCards, "empty class text");

					((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
							"add only update panel");

					((CardLayout) ButtonCards.getLayout()).show(ButtonCards,
							"class only button panel");

					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create class panel");

					((CardLayout) textCards.getLayout()).show(textCards,
							"class text panel");

					selectedCreatedPanel = "class";
					classB = true;
					methodB = false;
					instanceB = false;
				}
				resetTextField();
				visualPanel.setSelectedIndex(index - (1 + commentCheck));
			} else if (s.equals("finish")) {

				if (myProgramElements.size() == 0) { // make sure class exists
					JOptionPane
							.showMessageDialog(null,
									"you must first create a class before you can finish it!");
				} else {

					for (SelectionButton selec : classSelectionButton) {
						selec.setBackground(cColor);
					}

					int index = 0;
					ProgramElements currentClass = null;
					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == classID
								&& p.getType().equals("class")) {
							currentClass = p;
						}

						if (p.getClassID() == classID) {
							index++;
						}
					}

					Class c = new Class(true, currentClass.getName(),
							currentClass.getClassID());
					c.setPosition(index);
					myProgramElements.add(c);
					visual.insertElementAt(c.printToList(), index);

					ShowFinishMode();
				}
			} else if (s.equals("main method")) {
				int index = visualPanel.getSelectedIndex();
				ProgramElements currentPro = null;
				for (ProgramElements p : myProgramElements) {
					if (p.getClassID() == classID
							&& p.getType().equals("class")) {
						currentPro = p;
					}
				}

				Method main = new Method("m", currentPro.getName());
				main.setPosition(index + 1);
				currentPro.addMain(true);
				main.setClassID(currentPro.getClassID());
				myProgramElements.add(main);

				((CardLayout) classTextCards.getLayout()).show(classTextCards,
						"constructor text panel");

				visual.insertElementAt(main.printToList(), index + 1);
				for (ProgramElements p : myProgramElements) {
					if ((p.getPosition() == main.getPosition() || p.getPosition() > main
							.getPosition())
							&& p != main
							&& p.getClassID() == classID) {
						p.setPosition(p.getPosition() + 1);
					}
				}
				reOrderMyProgams();
				visualPanel.setSelectedIndex(index + 1);

			} else if (s.equals("default constructor")) {
				int index = visualPanel.getSelectedIndex();
				ProgramElements currentPro = null;
				for (ProgramElements p : myProgramElements) {
					if (p.getClassID() == classID
							&& p.getType().equals("class")) {
						currentPro = p;
					}
				}

				Method cons = new Method("c", currentPro.getName());
				cons.setPosition(index + 1);

				cons.setClassID(currentPro.getClassID());

				myProgramElements.add(cons);

				for (ProgramElements p : myProgramElements) {
					if ((p.getPosition() == cons.getPosition() || p.getPosition() > cons
							.getPosition())
							&& p != cons
							&& p.getClassID() == classID) {
						p.setPosition(p.getPosition() + 1);
					}
				}

				visual.insertElementAt(cons.printToList(), index + 1);
				reOrderMyProgams();
				visualPanel.setSelectedIndex(index + 1);

			} else if (s.equals("First Class")) {
				JOptionPane.showMessageDialog(null,
						"select the parameters for your first class \n"
								+ "and click add to begin");
			} else if (s.equals("Exit")) {

				System.exit(0);
			} else if (s.equals("add comment")) {
				int stop = 0;
				String commentName = "";
				if (myProgramElements.size() > 0) {
					int index = visualPanel.getSelectedIndex();
					ProgramElements commentP = null;
					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == classID
								&& p.getPosition() == index) {
							commentP = p;
						}
					}
					if (commentP.isfinished()) {
						JOptionPane
								.showMessageDialog(null,
										"Error, you cannot comment the ending bracket!");
						stop++;
					} else if (commentP.isComment()) {
						JOptionPane.showMessageDialog(null,
								"Error, you cannot comment a comment!");
						stop++;
					} else if (commentP.hasComment()) {
						JOptionPane
								.showMessageDialog(
										null,
										"Error, "
												+ commentP.getName()
												+ " already has a comment, replace "
												+ "\nthe current one if you need to change it ");
						stop++;
					} else if (commentP.getType().equals("class")) {

						JOptionPane.showMessageDialog(null,
								"comments are not valid for classes, sorry");
						stop++;
					} else {
						if (stop == 0) {
							String name = "";
							if (commentP.isConstructor()) {
								name = commentP.getClassName()
										+ "'s default constructor"
										+ commentP.getPosition();
							} else if (commentP.isMain()) {
								name = commentP.getClassName()
										+ "'s main method";
							} else {
								name = commentP.getName();
							}
							String comment = JOptionPane
									.showInputDialog("enter the discription of "
											+ name);

							if (commentP.getType().equals("method")) {

								String cName = "";
								if (commentP.isMain()) {
									cName = commentP.getClassName()
											+ "'s main method";
								} else if (commentP.isConstructor()) {
									cName = commentP.getClassName()
											+ "'s default Constructor";
								} else {
									cName = commentP.getName();
								}

								Method m = new Method(comment, cName,
										commentP.getClassID());

								m.setPosition(index);
								m.setClassID(classID);
								commentName = m.getName();
								myProgramElements.add(m);
								for (ProgramElements p : myProgramElements) {
									if ((p.getPosition() == m.getPosition() || p
											.getPosition() > m.getPosition())
											&& p != m
											&& p.getClassID() == classID) {
										p.setPosition(p.getPosition() + 1);
									}
								}
								visual.insertElementAt(m.printToList(), index);
							} else if (commentP.getType().equals("instance")) {
								Variable i = new Variable(comment,
										commentP.getName(),
										commentP.getClassID());
								i.setPosition(index);
								i.setClassID(classID);
								commentName = i.getName();
								myProgramElements.add(i);
								for (ProgramElements p : myProgramElements) {
									if ((p.getPosition() == i.getPosition() || p
											.getPosition() > i.getPosition())
											&& p != i
											&& p.getClassID() == classID) {
										p.setPosition(p.getPosition() + 1);
									}
								}
								visual.insertElementAt(i.printToList(), index);
							}
						}

						commentP.setItemHavingComment();

						creationPanel = ReplaceCommentDialog(classID,
								Integer.MAX_VALUE);

						commentTitleCards.add(commentName, creationPanel);
						reOrderMyProgams();
						visualPanel.setSelectedIndex(index);
					}
				} else
					JOptionPane.showMessageDialog(null,
							"you must first create a class, variable, or method\n"
									+ "before you can comment it");

			} else if (s.equals("Save")) {

				if (myProgramElements.size() > 0) {
					try {
						saveFile();
					} catch (IOException save) {
						// TODO Auto-generated catch block
						save.printStackTrace();
					}
				} else
					JOptionPane.showMessageDialog(null,
							"must create at least one class to save");
			} else if (s.equals("Open")) {

				myProgramElements = new ArrayList<ProgramElements>();
				classUniqueDefaultNameGenerator = new ArrayList<Integer>();

				classSelectionButton = new ArrayList<SelectionButton>();
				int finishCheck = 0;
				try {
					ArrayList<ProgramElements> loaded = loadFile();
					classID = 0;

					visual.clear();
					classCollectionCards.removeAll();

					int start = 0;
					try {
						start = loaded.get(0).getClassID();
					} catch (NullPointerException nul) {

					}

					classTextPanel = buildFullClassTextPanel();
					classTextCards.add("full text panel", classTextPanel);

					classTextPanel = buildConstructorClassTextPanel();
					classTextCards
							.add("constructor text panel", classTextPanel);
					classID = start;
					if (loaded != null) {
						for (ProgramElements l : loaded) {
							if (l.getType().equals("class")) {

								if (l.isfinished()) {
									Class c = new Class(true, l.getName(),
											l.getClassID());
									c.setPosition(l.getPosition());
									c.setClassID(l.getClassID());
									myProgramElements.add(c);

									if (l.getClassID() == start
											&& l.isfinished()) {
										ShowFinishMode();
										finishCheck++;
										classUniqueDefaultNameGenerator.add(l
												.getClassID());
									}
								} else {

									Class c = new Class(l.getVisability(),
											l.getHierarchy(), l.getName());
									c.setClassID(l.getClassID());
									c.setPosition(l.getPosition());
									c.setHighestMethod((l.getHighestMethod() - 1));
									c.setHighestVariable((l
											.getHighestVariable() - 1));
									SelectionButton b = new SelectionButton(
											c.getName());
									c.addMain(l.hasMain());
									classSelectionButton.add(b);
									classID = l.getClassID();
									myProgramElements.add(c);

									classPanel = buildClassPanel();
									JScrollPane cSpane = new JScrollPane(
											classPanel);
									cSpane.setPreferredSize(new Dimension(500,
											400));

									classCollectionCards.add("classes", cSpane);
									classUniqueDefaultNameGenerator.add(l
											.getClassID());
									((CardLayout) classCollectionCards
											.getLayout()).show(
											classCollectionCards, "classes");
								}

							} else if (l.getType().equals("method")) {

								if (l.isComment()) {
									Method m = new Method(l.getComment(),
											l.getOriginalCommentName(),
											l.getClassID());
									m.setClassID(l.getClassID());
									m.setPosition(l.getPosition());
									myProgramElements.add(m);
								} else if (l.isMain()) {
									Method m = new Method("m", l.getClassName());
									m.setClassID(l.getClassID());
									m.setPosition(l.getPosition());
									myProgramElements.add(m);

								} else if (l.isConstructor()) {
									Method m = new Method("c", l.getClassName());
									m.setClassID(l.getClassID());
									m.setPosition(l.getPosition());
									myProgramElements.add(m);
								} else {

									Method m = new Method(l.getVisability(),
											l.getHierarchy(),
											l.getReturnType(), l.getName(),
											l.getStatic());
									m.setClassID(l.getClassID());
									m.setPosition(l.getPosition());
									myProgramElements.add(m);
									((CardLayout) classTextCards.getLayout())
											.show(classTextCards,
													"full text panel");
								}

							} else if (l.getType().equals("instance")) {

								if (l.isComment()) {
									Variable i = new Variable(l.getComment(),
											l.getOriginalCommentName(),
											l.getClassID());
									i.setClassID(l.getClassID());
									i.setPosition(l.getPosition());
									myProgramElements.add(i);
								} else {

									Variable v = new Variable(
											l.getVisability(), l.getStatic(),
											l.getReturnType(), l.getName());
									v.setClassID(l.getClassID());
									v.setPosition(l.getPosition());
									myProgramElements.add(v);
								}

							}

						}
						int position = 0;
						int checkMain = 0;
						for (ProgramElements p : myProgramElements) {

							if (p.hasMain()) {
								checkMain++;
							} else
								((CardLayout) classTextCards.getLayout()).show(
										classTextCards, "full text panel");

							if (p.getClassID() == start) {
								position++;
								visual.insertElementAt(p.printToList(),
										p.getPosition());
							}
						}

						for (ProgramElements p : myProgramElements) {
							if (p.isComment()) {
								creationPanel = ReplaceCommentDialog(
										p.getClassID(), p.getPosition() + 1);

								commentTitleCards.add(p.getName(),
										creationPanel);
							}
						}

						if (finishCheck == 0) {

							if (checkMain > 0) {
								((CardLayout) classTextCards.getLayout()).show(
										classTextCards,
										"constructor text panel");
							} else {
								((CardLayout) classTextCards.getLayout()).show(
										classTextCards, "full text panel");
							}
							visualPanel.setSelectedIndex(position - 1);
							((CardLayout) ButtonCards.getLayout()).show(
									ButtonCards, "full button panel");
							if (position - 1 > 0) {
								((CardLayout) UpdateCards.getLayout()).show(
										UpdateCards, "no replace update panel");
							} else {
								((CardLayout) UpdateCards.getLayout()).show(
										UpdateCards, "full update panel");
							}
						} else {
							((CardLayout) classTextCards.getLayout()).show(
									classTextCards, "empty class text");
						}

						selectedCreatedPanel = "class";
						classB = true;
						methodB = false;
						instanceB = false;
						classID = start;
						resetTextField();

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else if (s.equals("Exit")) {
				System.exit(0);
			} else if (s.equals("New")) {
				
				classUniqueDefaultNameGenerator = new ArrayList<Integer>();

				myProgramElements = new ArrayList<ProgramElements>();
				visual.clear();

				classB = true;
				methodB = false;
				instanceB = false;

				selectedCreatedPanel = "class";

				classTextCards.removeAll();

				((CardLayout) CreationCards.getLayout()).show(CreationCards,
						"create class panel");

				classSelectionButton = new ArrayList<SelectionButton>();

				classTextPanel = buildEmptyClassTextPanel();

				classTextCards.add("empty text panel", classTextPanel);
				((CardLayout) classTextCards.getLayout()).show(classTextCards,
						"empty text panel");

				((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
						"add only update panel");

				((CardLayout) ButtonCards.getLayout()).show(ButtonCards,
						"class only button panel");

				classPanel = buildClassPanel();
				JScrollPane cSpane = new JScrollPane(classPanel);
				cSpane.setPreferredSize(new Dimension(500, 400));

				classCollectionCards.add("classes", cSpane);

				((CardLayout) classCollectionCards.getLayout()).show(
						classCollectionCards, "classes");

				resetTextField();

			} else if (s.equals("Export Current")) {

				int check = 0;
				int amount = 0;

				ProgramElements exportClass = null;

				for (ProgramElements p : myProgramElements) {

					if (p.getClassID() == classID) {
						amount++;
					}

					if (p.getPosition() == 0 && p.getClassID() == classID) {
						exportClass = p;
					}

					if (p.isfinished() && p.getClassID() == classID) {
						check++;
					}
				}

				if (check > 0) {

					JFileChooser fileSelect = new JFileChooser();
					fileSelect.setDialogTitle("where do you want to export "
							+ exportClass.getName() + ".java");
					File file = new File(exportClass.getName() + ".java");
					fileSelect.setSelectedFile(file);

					int returnvalue = fileSelect.showSaveDialog(null);

					if (returnvalue == JFileChooser.APPROVE_OPTION) {
						File fi = fileSelect.getCurrentDirectory();
						String fileName = fi.getPath() + "\\" + file.getName(); // make sure the name is always the class name + .java 
																				//despite what user types in  
						try {

							int position = 0;

							FileWriter fw = new FileWriter(fileName); // write
																		// to
																		// file
							PrintWriter pw = new PrintWriter(fw);
							while (position < (amount - 1)) {
								for (ProgramElements p : myProgramElements) {
									if (p.getClassID() == classID
											&& p.getPosition() == position) {
										pw.println(p.toString());
										position++;
									}
								}
							}

							pw.close();
						} catch (FileNotFoundException f) {
							f.printStackTrace();
						} catch (IOException f) {
							// TODO Auto-generated catch block
							f.printStackTrace();
						}

					}
				} else
					JOptionPane
							.showMessageDialog(
									null,
									"Your current class is not finished!\n"
											+ "please finish your class before trying to export it");

			} else if (s.equals("Export All")) {

				int classCheck = 0;
				int finishCheck = 0;

				for (ProgramElements p : myProgramElements) {
					if (p.getType().equals("class") && p.isfinished() == false) {
						classCheck++;
					} else if (p.getType().equals("class") && p.isfinished()) {
						finishCheck++;
					}
				}

				int index = 0;

				int[] classIDs = new int[finishCheck];

				for (ProgramElements p : myProgramElements) {
					if (p.getType().equals("class") && p.isfinished() == false) {
						classIDs[index] = p.getClassID();
						index++;
					}
				}

				String name = "";
				File[] files = new File[classCheck];
				for (int i = 0; i < classIDs.length; i++) {
					for (ProgramElements p : myProgramElements) {
						if (p.getClassID() == classIDs[i]
								&& p.getType().equals("class")
								&& p.isfinished() == false) {
							name = p.getName() + ".java";
							File file = new File(name);
							files[i] = file;
						}
					}
				}

				if (classCheck == finishCheck) {

					JFileChooser fileSelect = new JFileChooser();
					fileSelect
							.setDialogTitle("where do you want to export your classes?");
					fileSelect.setSelectedFiles(files);

					int returnvalue = fileSelect.showSaveDialog(null);

					if (returnvalue == JFileChooser.APPROVE_OPTION) {
						File fi = fileSelect.getCurrentDirectory(); //make sure classes end up in a user defined location

						fileSelect.setSelectedFiles(files);
						String[] fileNames = new String[files.length];

						for (int i = 0; i < fileNames.length; i++) {
							fileNames[i] = fi.getPath()
									+ "\\"
									+ fileSelect.getSelectedFiles()[i]
											.getPath(); // make sure the names are always the class name + .java 
														//despite what user types in 
						}
						try {
							for (int i = 0; i < files.length; i++) {

								int amount = 0;

								for (ProgramElements p : myProgramElements) {
									if (p.getClassID() == classIDs[i]) {
										amount++;
									}
									if (p.getClassID() == classIDs[i]
											&& p.getType().equals("class")
											&& p.isfinished() == false) {
										name = p.getName();
									}
								}

								int position = 0;

								File f = new File(fileNames[i]);

								FileWriter fw = new FileWriter(f); // write
																	// to
																	// file
								PrintWriter pw = new PrintWriter(fw);

								while (position < (amount - 1)) {
									for (ProgramElements p : myProgramElements) {
										if (p.getClassID() == classIDs[i]
												&& p.getPosition() == position) {
											pw.println(p.toString());
											position++;
										}
									}
								}

								pw.close();
							}
						} catch (FileNotFoundException f) {
							f.printStackTrace();
						} catch (IOException f) {
							// TODO Auto-generated catch block
							f.printStackTrace();
						}

					}

				} else
					JOptionPane
							.showMessageDialog(
									null,
									"All of your classes are not yet finished!\n"
											+ "please make sure to finish all of your classes\n"
											+ "before trying to export them");

			} else if (s.equals("continue working")) {
				ProgramElements selectedClass = null;
				ProgramElements remove = null;
				int position = 0;
				for (ProgramElements p : myProgramElements) {

					if (p.getClassID() == classID && p.getPosition() == 0) {
						selectedClass = p;
					}
					if (p.getClassID() == classID && p.isfinished()) {
						remove = p;
						position = p.getPosition();
					}
				}
				visual.removeElementAt(position);
				myProgramElements.remove(remove);
				resetBacktoWork(selectedClass);
				resetTextField();

				if (methodB == true) {

					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create method panel");
					((CardLayout) textCards.getLayout()).show(textCards,
							"method text panel");

					for (SelectionButton selec : classSelectionButton) {
						selec.setBackground(mColor);
					}
				} else if (instanceB == true) {
					((CardLayout) textCards.getLayout()).show(textCards,
							"instance text panel");
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create instance panel");
					for (SelectionButton selec : classSelectionButton) {
						selec.setBackground(varColor);
					}
				}

			} else {

				SelectionButton sB = (SelectionButton) e.getSource();

				ProgramElements selectedClass = null;

				for (ProgramElements m : myProgramElements) {

					if (sB.getText().equals(m.getName())) {
						selectedClass = m;
					}
				}

				int checkforFinish = 0;
				for (ProgramElements p : myProgramElements) {
					if (p.getClassID() == selectedClass.getClassID()
							&& p.isfinished()) {
						checkforFinish++;
					}
				}

				if (checkforFinish == 1) {
					ShowFinishMode();
					for (SelectionButton button : classSelectionButton) {
						button.setBackground(cColor);
					}
				} else {

					resetBacktoWork(selectedClass);
				}
				classID = selectedClass.getClassID();
				visual.clear();

				int position = 0;
				for (ProgramElements p : myProgramElements) {

					if (selectedClass.getClassID() == p.getClassID()) {
						position++;
						visual.insertElementAt(p.printToList(), p.getPosition());
					}
				}

				if (classB == true && checkforFinish == 0) {
					checkClassTextCorrectness();
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create class panel");
					((CardLayout) textCards.getLayout()).show(textCards,
							"class text panel");
					for (SelectionButton button : classSelectionButton) {
						button.setBackground(cColor);
					}

				} else if (methodB == true && checkforFinish == 0) {
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create method panel");
					((CardLayout) textCards.getLayout()).show(textCards,
							"method text panel");
					for (SelectionButton button : classSelectionButton) {
						button.setBackground(mColor);
					}
				} else if (instanceB == true && checkforFinish == 0) {
					((CardLayout) textCards.getLayout()).show(textCards,
							"instance text panel");
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "create instance panel");

					for (SelectionButton button : classSelectionButton) {
						button.setBackground(varColor);
					}
				}

				visualPanel.setSelectedIndex(position - 1);
				resetTextField();
			}
		}

		@Override
		/**
		 * keeps track of where the user is selecting and displays the appropriate panels
		 */
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			int index = visualPanel.getSelectedIndex();
			int commentCheck = 0;
			int finishCheck = 0;
			ProgramElements comment = null;
			for (ProgramElements p : myProgramElements) {
				if (p.getClassID() == classID && p.getPosition() == index
						&& p.isComment()) {
					commentCheck++;
					comment = p;
				} else if (p.getClassID() == classID && p.isfinished()) {
					finishCheck++;
				}
			}
			if (finishCheck == 0) {
				if (commentCheck == 0) {
					trackButtons(index);
					if (classB == true) {

						checkClassTextCorrectness();

						for (ProgramElements p : myProgramElements) {
							if (p.getClassID() == classID && p.isfinished()) {
								ShowFinishMode();
							}
						}

						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create class panel");
					} else if (methodB == true) {
						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create method panel");
					} else if (instanceB == true) {
						((CardLayout) CreationCards.getLayout()).show(
								CreationCards, "create instance panel");
					}

				} else {
					((CardLayout) classTextCards.getLayout()).show(
							classTextCards, "empty class text");
					((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
							"replace and delete panel");
					((CardLayout) CreationCards.getLayout()).show(
							CreationCards, "replace comment panel");
					((CardLayout) commentTitleCards.getLayout()).show(
							commentTitleCards, comment.getName());

					commentReplace.setText(comment.getComment());
				}

			}
		}
	}
	/*
	 * method to show appropriate panels for when user decides to continue work after clicking finish
	 */
	private void resetBacktoWork(ProgramElements selectedClass) {
		((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
				"full update panel");

		((CardLayout) FinishCards.getLayout()).show(FinishCards,
				"finish button");

		((CardLayout) ButtonCards.getLayout()).show(ButtonCards,
				"full button panel");

		if (classB == true) {
			((CardLayout) CreationCards.getLayout()).show(CreationCards,
					"create class panel");
			methodCreationSelect.setBackground(Color.green);
			classCreationSelect.setBackground(Color.RED);
			instanceVariableCreationSelect.setBackground(Color.green);
			((CardLayout) textCards.getLayout()).show(textCards,
					"class text panel");
		} else if (methodB == true) {
			((CardLayout) CreationCards.getLayout()).show(CreationCards,
					"create method panel");
			methodCreationSelect.setBackground(Color.RED);
			classCreationSelect.setBackground(Color.green);
			instanceVariableCreationSelect.setBackground(Color.green);
		} else if (instanceB == true) {
			((CardLayout) CreationCards.getLayout()).show(CreationCards,
					"create instance panel");
			methodCreationSelect.setBackground(Color.green);
			classCreationSelect.setBackground(Color.green);
			instanceVariableCreationSelect.setBackground(Color.RED);
			((CardLayout) textCards.getLayout()).show(textCards,
					"instance text panel");

		}

		if (selectedClass.hasMain()) {

			((CardLayout) classTextCards.getLayout()).show(classTextCards,
					"constructor text panel");
		} else {
			((CardLayout) classTextCards.getLayout()).show(classTextCards,
					"full text panel");
		}

	}
	/*
	 * method to make sure correct class description panel is being displayed
	 */
	private void checkClassTextCorrectness() {
		int mainCheck = 0;
		for (ProgramElements p : myProgramElements) {
			if (p.getClassID() == classID && p.isMain()) {
				mainCheck++;
			}
		}
		if (mainCheck == 0) {
			((CardLayout) classTextCards.getLayout()).show(classTextCards,
					"full text panel");
		} else {
			((CardLayout) classTextCards.getLayout()).show(classTextCards,
					"constructor text panel");
		}
	}
	/*
	 * method that shows the right panels when a new area of the display panel is selected
	 */
	private void trackButtons(int index) {

		if (index == 0 && classB == true) {
			((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
					"full update panel");
		} else if ((instanceB == true || methodB == true) && index > 0) {
			((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
					"full update panel");
		} else if ((instanceB == true || methodB == true) && index == 0) {
			((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
					"no replace update panel");
		} else if (index > 0 && classB == true) {
			((CardLayout) UpdateCards.getLayout()).show(UpdateCards,
					"no replace update panel");
		}

	}

	@Override
	/**
	 * determines the specific modifiers for the program elements when a new one is created, or an old one is replaced
	 */
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

		String s = (String) e.getItem();

		if (s.equals("private")) {
			visability = "private";
		} else if (s.equals("public")) {
			visability = "public";
		} else if (s.equals("protected")) {
			visability = "protected";
		} else if (s.equals("abstract")) {
			hierarchy = "abstract";
		} else if (s.equals("final")) {
			hierarchy = "final";
		} else if (s.equals("neither")) {
			hierarchy = "n";
		} else if (s.equals("no")) {
			staticSelect = false;
		} else if (s.equals("static")) {
			staticSelect = true;
		} else if (s.equals("void")) {
			returnType = "void";
		} else if (s.equals("float")) {
			returnType = "float";
		} else if (s.equals("int")) {
			returnType = "int";
		} else if (s.equals("double")) {
			returnType = "double";
		} else if (s.equals("String")) {
			returnType = "String";
		}

	}
}