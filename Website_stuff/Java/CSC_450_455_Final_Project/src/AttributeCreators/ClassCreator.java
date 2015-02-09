package AttributeCreators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.management.relation.RoleList;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import DescriptionPanels.DescriptionPanel_Details;
import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * ClassCreator:
 * This class is used for the creation of new classes.
 * This class is one of four attribute creators used in creating new attributes.
 * This class exists to provide the users with greater options for classes.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ClassCreator extends JFrame {

	EventHandler eh = new EventHandler();

	private Character_Creation_Panel ccp = null;	//character creation panel
	private Database_Manager dm = null;				//database manager reference
	private JTextArea classNameField = new JTextArea(); // area where user
														// inserts class name
	private JComboBox roleList = new JComboBox(); // choices of roles
	private JComboBox powerList = new JComboBox(); // choices of powers
	private JComboBox hitList = new JComboBox(); // choices of hit die
	private JComboBox goldList = new JComboBox(); // choices of gold die
	private JComboBox alignmentList = new JComboBox(); // choices of alignment
	// check boxes for primary stats
	private ArrayList<JCheckBox> primaryStatBoxes = new ArrayList<JCheckBox>();
	// check boxes for secondary stats
	private ArrayList<JCheckBox> secondaryStatBoxes = new ArrayList<JCheckBox>();
	// area where user inserts description of class
	private JTextArea descriptionField = new JTextArea();
	private String[] stats = new String[6]; // stat choices
	private int primaryStatLimit = 2; // user can only select 2 primary stats
	private int secondaryStatLimit = 1; // user can only select 1 secondary stat
	private int numberOfChecked = 0; // number of primary checked
	private int numberOfChecked2 = 0; // number of secondary checked
	private boolean reEnableCheckBoxes = false; // exactly what it looks like
	private boolean reEnableCheckBoxes2 = false; // same
	JTextArea classDescriptionText; // populated by fun facts
	private JList flist = new JList(); // fight types list
	private JScrollPane flistScroller = new JScrollPane();
	private JList flist2 = new JList();
	private JScrollPane flistScroller2 = new JScrollPane();
	
	private String[] fightTypes = new String[5]; // fighting types available to
													// the user
	private DefaultListModel fChoices = new DefaultListModel();
	private ArrayList<String> foutput = new ArrayList<String>();
	// Array list of chosen fight types
	private JList alist = new JList(); //
	private JScrollPane alistScroller = new JScrollPane();
	private JList alist2 = new JList();
	private JScrollPane alistScroller2 = new JScrollPane();
	private ArrayList<String> aoutput = new ArrayList<String>();
	private String[] armorTypes = new String[6]; // fighting types available to
													// the user
	private DefaultListModel aChoices = new DefaultListModel();
	// Array list of chosen armor types

	public String defaultText = "This panel allows you to create  your new class. Fill in all the following information";
	// refernce to object

	JFrame classCreator = this;

	/**
	 * Constructor for the class ClassCreator
	 * Called in Character_Selection_Attribute_panel
	 * @param d (the Database Manager)
	 * @param cp (the Character Creation Panel)
	 * @author Victor Mancha and Devin Simpson
	 */
	public ClassCreator(Database_Manager d, Character_Creation_Panel cp) throws IOException {

		this.setTitle("Class Creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 500);
		this.setLayout(new BorderLayout());

		// set the window to appear in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);

		JPanel classControlPanel = controlPanel();
		JPanel classDescriptionPanel = descriptionPanel();

		this.add(classControlPanel, BorderLayout.CENTER);
		this.add(classDescriptionPanel, BorderLayout.EAST);
		dm = d;
		ccp = cp;
	}//end ClassCreator constructor

	/**
	 * The controlPanel is used for the creation of the new class.
	 * This includes the class' name, role, power source, hit die, gold die,
	 * primary stats, secondary stat, alignment, weapon proficiency, armor profiency and description.
	 * Called in class constructor
	 * @return controlPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel controlPanel() throws IOException {

		JPanel control_panel = new JPanel();

		control_panel.setSize(400, 400);
		control_panel.setLayout(new BorderLayout());

		control_panel.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Class Creator Control Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		control_panel.add(panelLabel, BorderLayout.NORTH);

		
		JPanel info_panel = new JPanelWithBackground("t_b_i_h.jpg");
		
		info_panel.setSize(400, 400);
		info_panel.setLayout(new GridLayout(0, 1));
		control_panel.add(info_panel, BorderLayout.CENTER);
		
		JPanel classInfo = new JPanel(); // class info panel: label and TextArea
		classInfo.setLayout(new BorderLayout());
		info_panel.add(classInfo);
		classInfo.setOpaque(false);
		
		JLabel classNameLabel = new JLabel(" Class Name:   ", JLabel.CENTER);
		classNameLabel.setOpaque(false);
		classInfo.add(classNameLabel, BorderLayout.WEST);

		classNameField = new JTextArea("", 3, 29);
		classNameField.setPreferredSize(new Dimension(3, 3));
		classInfo.add(classNameField, BorderLayout.CENTER);

		classNameField.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("This is where Class name is entered");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				// change the description panel to that of the default weapon
				// description.
				classDescriptionText.setText(defaultText);
			}

		});

		JPanel roleInfo = new JPanel(); // role info panel: labels and combo
										// boxes
		roleInfo.setLayout(new GridLayout(0, 5));
		info_panel.add(roleInfo);
		roleInfo.setOpaque(false);
		JLabel speedLabel = new JLabel("Role: ", JLabel.CENTER);
		speedLabel.setOpaque(false);
		roleInfo.add(speedLabel);
		speedLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Role: \n"
								+ "Class Role is the one of four basic combat functions within a party dynamic:controller, defender, leader, or striker.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		String[] roles = { "Striker", "Defender", "Controller", "Leader" };

		roleList = new JComboBox(roles);	//combo box of roles for class
		roleList.addActionListener(eh);
		roleInfo.add(roleList);

		roleList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {

				if (roleList.getSelectedItem().equals("Controller")) {
					classDescriptionText
							.setText("Controller: \n"
									+ "Controllers deal with large numbers of enemies at the same time. They favor offense over defense, using powers that deal damage to multiple foes at once, as well as subtler powers that weaken, confuse, or delay their foes. A character with the controller role primarily handles crowds by creating hazardous terrain and repositioning enemies, or spreading conditions and damage over multiple enemies. The wizard is the classic controller class");
				}

				if (roleList.getSelectedItem().equals("Defender")) {
					classDescriptionText
							.setText("Defender: \n"
									+ "Defenders have the highest defenses in the game and good close-up offense. They are the party’s front-line combatants; wherever they’re standing, that’s where the action is. Defenders have abilities and powers that make it difficult for enemies to move past them or to ignore them in battle. A character with the defender role primarily focuses enemy fire by making it difficult for enemies to move past, and punishing enemies who attack other party members. The fighter is the classic defender class.");
				}

				if (roleList.getSelectedItem().equals("Striker")) {
					classDescriptionText
							.setText("Striker: \n"
									+ "Strikers specialize in dealing high amounts of damage to a single target at a time. They have the most concentrated offense of any character in the game. Strikers rely on superior mobility, trickery, or magic to move around tough foes and single out the enemy they want to attack. A striker primarily eliminates single threats by closing with a target quickly and safely, then rapidly dealing damage to it. The rogue is the classic striker class.");
				}

				if (roleList.getSelectedItem().equals("Leader")) {
					classDescriptionText
							.setText("Leader: \n"
									+ "Leaders inspire, heal, and aid the other characters in an adventuring group. Leaders have good defenses, but their strength lies in powers that protect their companions and target specific foes for the party to concentrate on. Clerics and warlords (and other leaders) encourage and motivate their adventuring companions, but just because they fill the leader role doesn’t mean they’re necessarily a group’s spokesperson or commander.");
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		JLabel extraSpace = new JLabel("Power", JLabel.RIGHT);	//Part 1 of Power Source
		extraSpace.setOpaque(false);
		roleInfo.add(extraSpace);

		JLabel powerLabel = new JLabel(" Source: ", JLabel.LEFT);	//Part 2 of Power Source
		powerLabel.setOpaque(false);
		roleInfo.add(powerLabel);

		extraSpace.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Power Source: \n"
								+ "The power source is a narrative device to provide you information as to where your character got their abilities and training. A martial character has been schooled in how to fight; an arcane character can draw on magic; a divine character receives their powers from their deity.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		powerLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Power Source: \n"
								+ "The power source is a narrative device to provide you information as to where your character got their abilities and training. A martial character has been schooled in how to fight; an arcane character can draw on magic; a divine character receives their powers from their deity.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		String[] powerSource = { "Arcana", "Martial", "Psionic", "Divine",
				"Psionic", "Primal", "Shadow" };

		powerList = new JComboBox(powerSource); //Combo Box of Power Sources for class
		powerList.addActionListener(eh);
		roleInfo.add(powerList);

		powerList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {

				if (powerList.getSelectedItem().equals("Arcana")) {
					classDescriptionText
							.setText("Arcana: \n"
									+ "The arcane keyword indicates that the power source of a class or power is drawn from the study of magical energies or a pact with a supernatural creature. Drawing on magical energy that permeates the cosmos, the arcane power source can be used for a wide variety of effects, from fireballs to flight to invisibility. Each class is the representative of a different tradition of arcane study, and other traditions exist. Arcane powers are also called spells.");
				}

				if (powerList.getSelectedItem().equals("Martial")) {
					classDescriptionText
							.setText("Martial: \n"
									+ "The martial keyword indicates that the power source of a class or power is martial arts training and physical prowess.");
				}

				if (powerList.getSelectedItem().equals("Psionic")) {
					classDescriptionText
							.setText("Psionic: \n"
									+ "The psionic keyword indicates that the power source of a class or power is the character's own trained mind and incredible willpower.");
				}

				if (powerList.getSelectedItem().equals("Primal")) {
					classDescriptionText
							.setText("Primal: \n"
									+ "The primal keyword indicates that the power source of a class or power is an ancient tradition or connection to the spirits of the natural world.");
				}

				if (powerList.getSelectedItem().equals("Divine")) {
					classDescriptionText
							.setText("Divine: \n"
									+ "The divine keyword indicates that the power source of a class or power is the worship of a deity or adherence to a moral code.");
				}

				if (powerList.getSelectedItem().equals("Shadow")) {
					classDescriptionText
							.setText("Shadow: \n"
									+ "The shadow keyword indicates that the power source of a class or power is submission to the power of shadow and bonding with the Shadowfell. The Shadowfell is a dark and shadowy realm where colors are dark and faded and shapes are obscured at best. It somewhat pararells the mortal world. Creatures from this realm have shadow origins.");
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		JPanel dieInfo = new JPanel(); // dice panel: labels and combo boxes
		dieInfo.setLayout(new GridLayout(0, 5));
		info_panel.add(dieInfo);
		dieInfo.setOpaque(false);

		JLabel hitLabel = new JLabel("Hit Die: ", JLabel.CENTER);
		hitLabel.setOpaque(false);
		dieInfo.add(hitLabel);

		hitLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Hit Die: \n"
								+ "Hit dice are an abstract representation of your character's increasing capacity to survive as they grow. As you gain levels your character becomes harder to kill. This concept is is where HP comes from. HP is an abstraction of the physical capability a character has to withstand harm. Hit dice contribute to this concept as you level up.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		String[] hitDie = { "1d6", "1d8", "1d10", "1d12" };

		hitList = new JComboBox(hitDie);
		hitList.addActionListener(eh);
		dieInfo.add(hitList);

		hitList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Hit Die: \n"
								+ "Hit dice are an abstract representation of your character's increasing capacity to survive as they grow. As you gain levels your character becomes harder to kill. This concept is is where HP comes from. HP is an abstraction of the physical capability a character has to withstand harm. Hit dice contribute to this concept as you level up.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		JLabel extraSpace2 = new JLabel("Gold", JLabel.RIGHT);	//Part 1 of Gold Die
		extraSpace2.setOpaque(false);
		dieInfo.add(extraSpace2);

		JLabel goldLabel = new JLabel(" Die: ", JLabel.LEFT);	//Part 2 of Gold Die
		goldLabel.setOpaque(false);
		dieInfo.add(goldLabel);

		goldLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Hit Die: \n"
								+ "Gold dice is used to determine the starting gold of the player. Each class is associated with a gold dice. Deciding the gold dice must relate to the backstory of the class and wealth is determined for that class. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		String[] goldDie = { "6d4", "5d4", "4d4" };

		goldList = new JComboBox(goldDie);	//Combo Box of Gold Die choices
		goldList.addActionListener(eh);
		dieInfo.add(goldList);

		goldList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Hit Die: \n"
								+ "Gold dice is used to determine the starting gold of the player. Each class is associated with a gold dice. Deciding the gold dice must relate to the backstory of the class and wealth is determined for that class. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		JPanel statInfo = new JPanel(); // primary and secondary stats
		statInfo.setLayout(new GridLayout(0, 3));
		info_panel.add(statInfo);
		statInfo.setOpaque(false);
		
		JPanel primaryInfo = new JPanel();	//the primary info panel holds label, 6 check boxes and 6 labels
		primaryInfo.setLayout(new BorderLayout());
		statInfo.add(primaryInfo); // first of 3 panels (
		primaryInfo.setOpaque(false);
		
		JLabel primaryStat = new JLabel("Primary Stat", JLabel.CENTER); //Main label in primary info
		primaryStat.setOpaque(false);
		primaryInfo.add(primaryStat, BorderLayout.NORTH);
		primaryStat.setOpaque(false);
		
		JPanel boxInfo = new JPanel(); // primary stats (holds the check boxes)
		boxInfo.setLayout(new GridLayout(0, 6));
		primaryInfo.add(boxInfo, BorderLayout.CENTER);
		boxInfo.setOpaque(false);
		
		JPanel boxNameInfo = new JPanel(); //name of primary stats (holds the names)
		boxNameInfo.setLayout(new GridLayout(0, 6));
		primaryInfo.add(boxNameInfo, BorderLayout.SOUTH);
		boxNameInfo.setOpaque(false);
		stats[0] = "Cha";
		stats[1] = "Con";
		stats[2] = "Dex";
		stats[3] = "Int";
		stats[4] = "Str";
		stats[5] = "Wis";

		for (int i = 0; i < stats.length; i++) { // Build check boxes
			JCheckBox x = buildStatCheckBox(stats[i]);
			x.addActionListener(eh);
			boxInfo.add(x);
			primaryStatBoxes.add(x);
		}
		for (int i = 0; i < stats.length; i++) { // Build checkbox labels
			JLabel x = new JLabel(stats[i]);
			x.setOpaque(false);
			boxNameInfo.add(x);
		}

		JPanel secondaryInfo = new JPanel();	//secondary panel holds main label, 6 checkboxes, 6 labels
		secondaryInfo.setLayout(new BorderLayout());
		statInfo.add(secondaryInfo); // second of 3 panels
		secondaryInfo.setOpaque(false);
		JLabel secondaryStat = new JLabel("Secondary Stat", JLabel.CENTER);
		secondaryStat.setOpaque(false);
		secondaryInfo.add(secondaryStat, BorderLayout.NORTH);

		JPanel boxInfo2 = new JPanel(); // secondary stats (holds the check boxes)
		boxInfo2.setLayout(new GridLayout(0, 6));
		secondaryInfo.add(boxInfo2, BorderLayout.CENTER);
		boxInfo2.setOpaque(false);
		
		JPanel boxNameInfo2 = new JPanel();		//name of secondary stats (holds the names)
		boxNameInfo2.setLayout(new GridLayout(0, 6));
		secondaryInfo.add(boxNameInfo2, BorderLayout.SOUTH);
		boxNameInfo2.setOpaque(false);
		for (int i = 0; i < stats.length; i++) { // Build check boxes
			JCheckBox x = buildStatCheckBox(stats[i]);
			x.addActionListener(eh);
			boxInfo2.add(x);
			secondaryStatBoxes.add(x);
		}
		for (int i = 0; i < stats.length; i++) { // Build checkbox labels
			JLabel x = new JLabel(stats[i]);
			x.setOpaque(false);
			boxNameInfo2.add(x);
		}

		JPanel AlignmentInfo = new JPanel();		//Alignment holds main label and combo box
		AlignmentInfo.setLayout(new BorderLayout());
		statInfo.add(AlignmentInfo); // Third of 3 panels
		AlignmentInfo.setOpaque(false);
		JLabel AlignmentStat = new JLabel("Alignment", JLabel.CENTER); 	//main label for alignment panel
		AlignmentStat.setOpaque(false);
		AlignmentInfo.add(AlignmentStat, BorderLayout.NORTH);

		String[] alignments = { "Lawful Good", "Neutral Good", "Chaotic Good",
				"Lawful Neutral", "True Neutral", "Chaotic Neutral",
				"Lawful Evil", "Neutral Evil", "Chaotic Evil", "Any",
				"Any Lawful", "Any Neutral", "Any Chaotic" };

		alignmentList = new JComboBox(alignments);		//alignment choices for class
		alignmentList.addActionListener(eh);
		alignmentList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {

				if (alignmentList.getSelectedItem().equals("Lawful Good")) {
					classDescriptionText
							.setText("Lawful Good: \n"
									+ "A lawful good character acts as a good person is expected or required to act. He combines a commitment to oppose evil with the discipline to fight relentlessly. He tells the truth, keeps his word, helps those in need, and speaks out against injustice. A lawful good character hates to see the guilty go unpunished. Lawful good is the best alignment you can be because it combines honor and compassion. Lawful good can be a dangerous alignment when it restricts freedom and criminalizes self-interest.");
				}

				if (alignmentList.getSelectedItem().equals("Lawful Neutral")) {
					classDescriptionText
							.setText("Lawful Neutral: \n"
									+ "A lawful neutral character acts as law, tradition, or a personal code directs her. Order and organization are paramount to her. She may believe in personal order and live by a code or standard, or she may believe in order for all and favor a strong, organized government. Lawful neutral is the best alignment you can be because it means you are reliable and honorable without being a zealot. Lawful neutral can be a dangerous alignment when it seeks to eliminate all freedom, choice, and diversity in society.");
				}

				if (alignmentList.getSelectedItem().equals("Lawful Evil")) {
					classDescriptionText
							.setText("Lawful Evil: \n"
									+ "A lawful evil villain methodically takes what he wants within the limits of his code of conduct without regard for whom it hurts. He cares about tradition, loyalty, and order but not about freedom, dignity, or life. He plays by the rules but without mercy or compassion. He is comfortable in a hierarchy and would like to rule, but is willing to serve. He condemns others not according to their actions but according to race, religion, homeland, or social rank. He is loath to break laws or promises.");
				}

				if (alignmentList.getSelectedItem().equals("Neutral Good")) {
					classDescriptionText
							.setText("Neutral Good: \n"
									+ "Creatures of neutral good alignment believe that there must be some regulation in combination with freedoms if the best is to be brought to the world--the most beneficial conditions for living things in general and intelligent creatures in particular. Creatures of this alignments see the cosmos as a place where law and chaos are merely tools to use in bringing life, happiness, and prosperity to all deserving creatures. Order is not good unless it brings this to all.");
				}
				if (alignmentList.getSelectedItem().equals("True Neutral")) {
					classDescriptionText
							.setText("True Neutral: \n"
									+ "A neutral character does what seems to be a good idea. She doesn't feel strongly one way or the other when it comes to good vs. evil or law vs. chaos. Most neutral characters exhibit a lack of conviction or bias rather than a commitment to neutrality. Such a character thinks of good as better than evil-after all, she would rather have good neighbors and rulers than evil ones. Still, she's not personally committed to upholding good in any abstract or universal way.");
				}
				if (alignmentList.getSelectedItem().equals("Neutral Evil")) {
					classDescriptionText
							.setText("Neutral Evil: \n"
									+ "A neutral evil villain does whatever she can get away with. She is out for herself, pure and simple. She sheds no tears for those she kills, whether for profit, sport, or convenience. She has no love of order and holds no illusion that following laws, traditions, or codes would make her any better or more noble. On the other hand, she doesn't have the restless nature or love of conflict that a chaotic evil villain has. Some neutral evil villains hold up evil as an ideal, committing evil for its own sake.");
				}
				if (alignmentList.getSelectedItem().equals("Chaotic Good")) {
					classDescriptionText
							.setText("Chaotic Good: \n"
									+ "A chaotic good character acts as his conscience directs him with little regard for what others expect of him. He makes his own way, but he's kind and benevolent. He believes in goodness and right but has little use for laws and regulations. He hates it when people try to intimidate others and tell them what to do. He follows his own moral compass, which, although good, may not agree with that of society. Chaotic good is the best alignment you can be because it combines a good heart with a free spirit.");
				}
				if (alignmentList.getSelectedItem().equals("Chaotic Neutral")) {
					classDescriptionText
							.setText("Chaotic Neutral: \n"
									+ "A chaotic neutral character follows his whims. He is an individualist first and last. He values his own liberty but doesn't strive to protect others' freedom. He avoids authority, resents restrictions, and challenges traditions. A chaotic neutral character does not intentionally disrupt organizations as part of a campaign of anarchy. To do so, he would have to be motivated either by good (and a desire to liberate others) or evil (and a desire to make those different from himself suffer). A chaotic neutral character may be unpredictable, but his behavior is not totally random.");
				}
				if (alignmentList.getSelectedItem().equals("Chaotic Evil")) {
					classDescriptionText
							.setText("Chaotic Evil: \n"
									+ "A chaotic evil character does whatever his greed, hatred, and lust for destruction drive him to do. He is hot-tempered, vicious, arbitrarily violent, and unpredictable. If he is simply out for whatever he can get, he is ruthless and brutal. If he is committed to the spread of evil and chaos, he is even worse. Thankfully, his plans are haphazard, and any groups he joins or forms are poorly organized. Typically, chaotic evil people can be made to work together only by force, and their leader lasts only as long as he can thwart attempts to topple or assassinate him.");
				}
				if (alignmentList.getSelectedItem().equals("Any")) {
					classDescriptionText
							.setText("Any: \n"
									+ "A character can be any alignment that they so choose.");
				}
				if (alignmentList.getSelectedItem().equals("Any Lawful")) {
					classDescriptionText
							.setText("Any Lawful: \n"
									+ "A character can be any lawful alignment that they so choose. Lawful Good, Lawful Neutral, or Lawful Evil.");
				}
				if (alignmentList.getSelectedItem().equals("Any Neutral")) {
					classDescriptionText
							.setText("Any Neutral: \n"
									+ "A character can be any neutral alignment that they so choose. Neutral Good, True Neutral, or Neutral Evil.");
				}
				if (alignmentList.getSelectedItem().equals("Any Chaotic")) {
					classDescriptionText
							.setText("Any Chaotic: \n"
									+ "A character can be any chaotic alignment that they so choose. Chaotic Good, Chaotic Neutral, or Chaotic Evil.");
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});
		AlignmentInfo.add(alignmentList);

		JPanel fightInfo = new JPanel(); // Weapon Proficiency choices
		fightInfo.setLayout(new GridLayout(0, 3));
		info_panel.add(fightInfo);
		fightInfo.setOpaque(false);
		fightTypes[0] = "Simple Melee";
		fightTypes[1] = "Martial Melee";
		fightTypes[2] = "Simple Range";
		fightTypes[3] = "Martial Range";
		fightTypes[4] = "Magic Weapons";
		
		final String [] fightTypeDescriptions = new String[5];
		fightTypeDescriptions[0] = "Generally inexpensive and light in weight, simple weapons get the job done nevertheless.";
		fightTypeDescriptions[1] = "More advanced melee weapons used for making melee attacks, though some of them can be thrown as well.";
		fightTypeDescriptions[2] = "Simple ranged weapons that are thrown or projectiles that are not effective in melee.";
		fightTypeDescriptions[3] = "More advanced ranged weapons that are thrown or projectiles that are not effective in melee.";
		fightTypeDescriptions[4] = "Magic weapons have enhancement bonuses ranging from +1 to +5. They apply these bonuses to both attack and damage rolls when used in combat. All magic weapons are also masterwork weapons, but their masterwork bonus on attack rolls does not stack with their enhancement bonus on attack rolls. In addition to an enhancement bonus, weapons may have special abilities. Special abilities count as additional bonuses for determining the market value of the item, but do not modify attack or damage bonuses (except where specifically noted).";
		
		flist = new JList(fightTypes); // data has type String[] of weapon types
		flist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		flist.setLayoutOrientation(JList.VERTICAL);
		flist.setVisibleRowCount(-1);
		flist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<fightTypes.length;i++){
					if(flist.isSelectedIndex(i)){
						
						classDescriptionText
								.setText(fightTypeDescriptions[i]);
						} //end if
					}//end for loop
			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<fightTypes.length;i++){
					if(flist.isSelectedIndex(i)){
						
						classDescriptionText
								.setText(fightTypeDescriptions[i]);
						} //end if
					}//end for loop

			}//end mouse entered
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		flistScroller = new JScrollPane(flist);
		fightInfo.add(flistScroller);

		JPanel buttons = new JPanel();		//holds the buttons Add and Remove
		buttons.setLayout(new GridLayout(0, 1));
		buttons.setOpaque(false);
		fightInfo.add(buttons);
		
		JButton top = new JButton("Add");	//Adds weapon proficiency
		top.addActionListener(eh);
		top.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				classDescriptionText.setText("Add fighting type(s) to your class.");
			}
			
			public void mouseExited(MouseEvent e){
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		buttons.add(top);
		JButton bot = new JButton("Remove");	//Removes weapon proficiency
		bot.addActionListener(eh);
		bot.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				classDescriptionText.setText("Remove fighting type(s) from your class.");
			}
			
			public void mouseExited(MouseEvent e){
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		buttons.add(bot);

		flist2 = new JList(fChoices); // data has type String[] of weapon types
		flist2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		flist2.setLayoutOrientation(JList.VERTICAL);
		flist2.setVisibleRowCount(-1);
		flist2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<fightTypes.length;i++){
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Simple Melee")){
						
						classDescriptionText
								.setText(fightTypeDescriptions[0]);
						} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Martial Melee")){
						
						classDescriptionText
								.setText(fightTypeDescriptions[1]);
						} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Simple Range")){
	
						classDescriptionText.setText(fightTypeDescriptions[2]);
					} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Martial Range")){
	
						classDescriptionText.setText(fightTypeDescriptions[3]);
					} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Magic Weapons")){
						classDescriptionText.setText(fightTypeDescriptions[4]);
					} //end if
				}//end for loop

			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<fightTypes.length;i++){
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Simple Melee")){
						
						classDescriptionText
								.setText(fightTypeDescriptions[0]);
						} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Martial Melee")){
						
						classDescriptionText
								.setText(fightTypeDescriptions[1]);
						} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Simple Range")){
	
						classDescriptionText.setText(fightTypeDescriptions[2]);
					} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Martial Range")){
	
						classDescriptionText.setText(fightTypeDescriptions[3]);
					} //end if
					if(flist2.isSelectedIndex(i) && flist2.getSelectedValue().equals("Magic Weapons")){
						classDescriptionText.setText(fightTypeDescriptions[4]);
					} //end if
					}//end for loop

			}//end mouse entered
			
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		flistScroller2 = new JScrollPane(flist2);
		fightInfo.add(flistScroller2);

		JPanel armorInfo = new JPanel(); // armor types for class
		armorInfo.setLayout(new GridLayout(0, 3));
		armorInfo.setOpaque(false);
		info_panel.add(armorInfo);

		armorTypes[0] = "Cloth";
		armorTypes[1] = "Light Armor";
		armorTypes[2] = "Medium Armor";
		armorTypes[3] = "Heavy Armor";
		armorTypes[4] = "Shield";
		armorTypes[5] = "Tower Shield";

		final String [] armorTypeDescriptions = new String[6]; //armor descriptions
		armorTypeDescriptions[0] = "Cloth Armor: \nThe most basic form of armor. It most commonly exists in the form of everyday clothing. All characters are proficient with cloth armor. Jackets, mantles, woven robes, and padded vests don't, by themselves, provide any significant protection. However, you can imbue them with protective magic. Cloth armor doesn't slow you down or hinder your movement at all. Cloth armor is also less likely to arouse suspicion in sensitive locations than heavy armors. A wizard sneaking around in his robes is much less obvious a threat than a paladin in full plate mail.";
		armorTypeDescriptions[1] = "Light Armor: \nThe second lightest armor, after Cloth Armor. In its most basic incarnation, it provides a +2 armor bonus. This armor bonus is added to the higher of the character's Dexterity or Intelligence modifiers to provide the total armor bonus protection. Light armor provides the highest bonus without imparting a penalty to character Strength, Dexterity, or Consitution-based skill checks while wearing the armor.";
		armorTypeDescriptions[2] = "Medium Armor: \nMost medium armor (except for the archaic chainmail shirt) is not terribly heavy, but nonetheless provides a significant amount of protection—at the expense of some speed.";
		armorTypeDescriptions[3] = "Heavy Armor: \nA character with leather armor or hide armor proficiency, 13 or higher Strength, and 13 or higher Constitution can take the Armor Proficiency: Chainmail feat to gain proficiency with chainmail. A character with chainmail proficiency, 13 or higher Strength, and 13 or higher Constitution can take the Armor Proficiency: Scale feat to gain proficiency with scale armor. A character with scale proficiency, 15 or higher Strength, and 15 or higher Constitution can take the Armor Proficiency: Plate feat to gain proficiency with plate armor.";
		armorTypeDescriptions[4] = "Shield: \nYou strap a shield to your forearm and grip it with your hand. A small shield’s light weight lets you carry other items in that hand (although you cannot use weapons). A character with 13 or higher Strength can take the Shield Proficiency (Light) feat to gain proficiency with light shields.";
		armorTypeDescriptions[5] = "Tower Shield: \nYou strap a shield to your forearm and grip it with your hand.A large shield is too heavy for you to use your shield hand for anything else.";
		
		
		alist = new JList(armorTypes); // data has type String[] of armor types
		alist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		alist.setLayoutOrientation(JList.VERTICAL);
		alist.setVisibleRowCount(-1);
		alist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<armorTypes.length;i++){
					if(alist.isSelectedIndex(i)){
						
						classDescriptionText
								.setText(armorTypeDescriptions[i]);
						} //end if
					}//end for loop

			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<armorTypes.length;i++){
					if(alist.isSelectedIndex(i)){
						
						classDescriptionText
								.setText(armorTypeDescriptions[i]);
						} //end if
					}//end for loop

			}//end mouse entered
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		alistScroller = new JScrollPane(alist);
		armorInfo.add(alistScroller);

		JPanel buttons2 = new JPanel();		//Second panel of buttons, holds Add Armor and Remove Armor
		buttons2.setLayout(new GridLayout(0, 1));
		armorInfo.add(buttons2);
		buttons2.setOpaque(false);
		
		JButton top2 = new JButton("Add Armor"); //Adds an armor proficiency to class
		top2.addActionListener(eh);
		top2.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				classDescriptionText.setText("Add armor type(s) to your class.");
			}
			
			public void mouseExited(MouseEvent e){
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		buttons2.add(top2);
		JButton bot2 = new JButton("Remove Armor"); //Removes an armor proficiency from a class
		bot2.addActionListener(eh);
		bot2.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				classDescriptionText.setText("Remove armor type(s) from your class.");
			}
			
			public void mouseExited(MouseEvent e){
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		buttons2.add(bot2);

		alist2 = new JList(aChoices); // data has type String[] of armor choices
		alist2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		alist2.setLayoutOrientation(JList.VERTICAL);
		alist2.setVisibleRowCount(-1);
		alist2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<armorTypes.length;i++){
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Cloth")){
						
						classDescriptionText
								.setText(armorTypeDescriptions[0]);
						} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Light Armor")){
						
						classDescriptionText
								.setText(armorTypeDescriptions[1]);
						} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Medium Armor")){
	
						classDescriptionText.setText(armorTypeDescriptions[2]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Heavy Armor")){
	
						classDescriptionText.setText(armorTypeDescriptions[3]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Shield")){
						classDescriptionText.setText(armorTypeDescriptions[4]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Tower Shield")){
						classDescriptionText.setText(armorTypeDescriptions[5]);
					} //end if
				}//end for loop

			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<armorTypes.length;i++){
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Cloth")){
						
						classDescriptionText
								.setText(armorTypeDescriptions[0]);
						} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Light Armor")){
						
						classDescriptionText
								.setText(armorTypeDescriptions[1]);
						} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Medium Armor")){
	
						classDescriptionText.setText(armorTypeDescriptions[2]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Heavy Armor")){
	
						classDescriptionText.setText(armorTypeDescriptions[3]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Shield")){
						classDescriptionText.setText(armorTypeDescriptions[4]);
					} //end if
					if(alist2.isSelectedIndex(i) && alist2.getSelectedValue().equals("Tower Shield")){
						classDescriptionText.setText(armorTypeDescriptions[5]);
					} //end if
				}//end for loop

			}//end mouse entered
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText("Fill in your class information.");
			}
		});
		alistScroller2 = new JScrollPane(alist2);
		armorInfo.add(alistScroller2);

		JLabel extraLine2 = new JLabel("Description: ", JLabel.CENTER);
		extraLine2.setOpaque(false);
		info_panel.add(extraLine2);
		
		JPanel descriptionInfo = new JPanel(); // description panel: label and TextArea
		descriptionInfo.setOpaque(false);										
		descriptionInfo.setLayout(new BorderLayout());
		info_panel.add(descriptionInfo);

		descriptionField = new JTextArea();		//description of class is entered here
		descriptionField.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(descriptionField);
		descriptionInfo.add(scrollPane);

		descriptionField.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				classDescriptionText
						.setText("Enter a detailed Description of the Class");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}

		});

		JButton confirmButton = new JButton("Confirm New Class");	//confirms the new class
		confirmButton.addActionListener(eh);
		control_panel.add(confirmButton, BorderLayout.SOUTH);

		return control_panel;
	}//end of controlPanel method

	/**
	 * This panel is used to display information about the different requirements to create a class.
	 * Called in class constructor
	 * @return descriptionPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel descriptionPanel() {
		JPanel description_panel = new JPanel();
		description_panel.setSize(400, 400);
		description_panel.setLayout(new BorderLayout());
		description_panel.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Class Creator Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		description_panel.add(panelLabel, BorderLayout.NORTH);

		classDescriptionText = new JTextArea();	//the descriptions of different attributes come from here
		classDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(classDescriptionText);
		classDescriptionText.setEditable(false);
		classDescriptionText.setText(defaultText);
		description_panel.add(scrollPane, BorderLayout.CENTER);

		JEditorPane classLink = new JEditorPane(); //link to the rules of D&D
		classLink.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		classLink.setEditable(false);
		classLink.setText("<a href=\"http://media.wizards.com/2014/downloads/dnd/PlayerDnDBasicRules_v0.2.pdf\">D&D BASIC RULES</a>");
		
		classLink.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		           // Do something with e.getURL() here
		        	if(Desktop.isDesktopSupported()) {
		    		    try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} 
		    		    catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
		    		    catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    		}
		        }
		    }
		});
		
		description_panel.add(classLink, BorderLayout.SOUTH);
		
		return description_panel;
	}//end descriptionPanel method

	/**
	 * Builds the stat check boxes for the Primary and Secondary Stats
	 * Called in controlPanel method
	 * @param box (name of the box)
	 * @return JCheckBox
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JCheckBox buildStatCheckBox(final String box) {
		JCheckBox statCheckBox = new JCheckBox();

		statCheckBox.setName(box);

		statCheckBox.setOpaque(false);

		statCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);

		statCheckBox.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {

				classDescriptionText
						.setText("Primary Stats and Secondary Stats: \n"
								+ "Primary Stats are the main ability score that the class uses the most. "
								+ "Many classes uses special abilities that rely on your primary stat. "
								+ "For example: Wizard require Intelligence(Int) to effectively uses spells and get damage bonuses. \n"
								+ "Secondary Stats are not the most essential stats but are useful for the class. "
								+ "It can provide the class aid with skills and possibly learn other usefull abilities. "
								+ "For example: Wizard can use Constitution as a secondary stats because their start will very low hit points because of their hit die.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				classDescriptionText.setText(defaultText);
			}
		});
		return statCheckBox;
	}//end of buildStatCheckBox method

	/**
	 * Determines which primary stats have been selected
	 * Called in event handler
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectPrimaryStats() {
		// we are going to find the number of check boxes clicked, if
		// two have been clicked, disable
		// all non clicked check boxes so only at most three can be
		// selected (2 primary, 1 secondary)
		numberOfChecked = 0;

		// find all the checked check boxes
		for (JCheckBox skillCheckBox : primaryStatBoxes) {

			if (skillCheckBox.isSelected() == true) {
				numberOfChecked++;
			}// end if

		}// end for loop

		// if that number is three plus the race skills, disable all non
		// checked check boxes
		if (numberOfChecked == primaryStatLimit) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				for (JCheckBox skillCheckBox : primaryStatBoxes) {

					if (skillCheckBox.isSelected() == false) {
						skillCheckBox.setEnabled(false);
					}
				}// end inner for loop
				if (primaryStatBoxes.get(i).isSelected() == true
						&& primaryStatBoxes.get(i).getName()
								.equals(secondaryStatBoxes.get(i).getName())) {
					secondaryStatBoxes.get(i).setEnabled(false);
				}

			}// end outer for loop

			// set a boolean to show that non checked boxes have been
			// disabled
			reEnableCheckBoxes = true;

			// if the number of checked check boxes is less than 2
		} else if (numberOfChecked == 0 && numberOfChecked2 == 0) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				secondaryStatBoxes.get(i).setEnabled(true);
				primaryStatBoxes.get(i).setEnabled(true);
			}// end for loop

		} else if (numberOfChecked == 1) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				if (primaryStatBoxes.get(i).isSelected() == true
						&& secondaryStatBoxes.get(i).getName()
								.equals(primaryStatBoxes.get(i).getName())) {
					secondaryStatBoxes.get(i).setEnabled(false);
				} else {
					secondaryStatBoxes.get(i).setEnabled(true);
					primaryStatBoxes.get(i).setEnabled(true);
				}
			}// end for loop
		}//end else if
	}//end selectPrimaryStats method

	/**
	 * Determines which secondary stat has been selected
	 * Called in event handler
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectSecondaryStats() {
		// we are going to find the number of check boxes clicked, if
		// two have been clicked, disable
		// all non clicked check boxes so only at most three can be
		// selected (2 primary, 1 secondary)
		numberOfChecked2 = 0;

		// find all the checked check boxes
		for (JCheckBox skillCheckBox : secondaryStatBoxes) {

			if (skillCheckBox.isSelected() == true) {
				numberOfChecked2++;
			}// end if

		}// end for loop

		// if that number is three plus the race skills, disable all non
		// checked check boxes
		if (numberOfChecked2 == secondaryStatLimit) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				for (JCheckBox skillCheckBox : secondaryStatBoxes) {

					if (skillCheckBox.isSelected() == false) {
						skillCheckBox.setEnabled(false);
					}
					if (secondaryStatBoxes.get(i).isSelected() == true
							&& primaryStatBoxes
									.get(i)
									.getName()
									.equals(secondaryStatBoxes.get(i).getName())) {
						primaryStatBoxes.get(i).setEnabled(false);
					}// end if

				}// end inner for loop
			}// end outer for loop

			// set a boolean to show that non checked boxes have been
			// disabled
			reEnableCheckBoxes2 = true;

			// if the number of checked check boxes is less than 2
		} else if (numberOfChecked2 == 0 && numberOfChecked == 0) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				secondaryStatBoxes.get(i).setEnabled(true);
				primaryStatBoxes.get(i).setEnabled(true);
			}// end for loop

		} else if (numberOfChecked2 == 1) {
			for (int i = 0; i < secondaryStatBoxes.size(); i++) {
				if (secondaryStatBoxes.get(i).isSelected() == true
						&& secondaryStatBoxes.get(i).getName()
								.equals(primaryStatBoxes.get(i).getName())) {
					primaryStatBoxes.get(i).setEnabled(false);
				} else {
					secondaryStatBoxes.get(i).setEnabled(true);
					primaryStatBoxes.get(i).setEnabled(true);
				}
			}// end for loop
		}// end else if
		else {

			// check if the non checked check boxes are disabled using
			// the boolean
			if (reEnableCheckBoxes2 == true) {
				numberOfChecked2 = 0;
				// if the non checked check boxes are disabled, re
				// enable them
				for (int i = 0; i < secondaryStatBoxes.size(); i++) {

					boolean secondaryStat = false;

					for (JCheckBox rSkill : secondaryStatBoxes) {

						if (rSkill.isSelected() == true
								&& rSkill.getName().equals(stats[i])) {
							secondaryStat = true;
							numberOfChecked2++;
						}
					}// end advanced for loop

					if (secondaryStat == false) {
						secondaryStatBoxes.get(i).setEnabled(true);
					}

					if (primaryStatBoxes.get(i).getName()
							.equals(secondaryStatBoxes.get(i).getName())
							&& primaryStatBoxes.get(i).isSelected() == true) {
						secondaryStatBoxes.get(i).setEnabled(false);
					}// end if

				}// end for loop

				// update the boolean to reflect the re enabled
				// checkboxes
				reEnableCheckBoxes2 = false;
			}// end if reEnable == true
		}// end else
	}// end secondaryStats method

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Victor Mancha, Thomas Man, Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			// the checkbox's action listener produces an empty string, so when
			// the action is listener is called with the "" argument, we know a
			// check box has been clicked
			if (command == "") {
				selectPrimaryStats();
				selectSecondaryStats();
			}// end if command == ""

			//Add adds a weapon proficiency to the list that the class can use
			if (command == "Add") {
				for (int i = 0; i < fightTypes.length; i++) {
					if (flist.isSelectedIndex(i)
							&& !fChoices.contains(fightTypes[i])) {
						fChoices.addElement(fightTypes[i]);
						foutput.add(fightTypes[i]);
					} // end if
				}// end for loop
			}// end if Add

			//Remove removes a weapon proficiency from the list that the class can use
			if (command == "Remove") {
				for (int i = 0; i < fChoices.getSize(); i++) {
					if (flist2.isSelectedIndex(i)) {
						fChoices.remove(i);
						foutput.remove(fightTypes[i]);
					} // end if
				}// end for loop
			}// end if Add

			//Adds an armor proficiency to the list that the class can use
			if (command == "Add Armor") {
				for (int i = 0; i < armorTypes.length; i++) {
					if (alist.isSelectedIndex(i)
							&& !aChoices.contains(armorTypes[i])) {
						aChoices.addElement(armorTypes[i]);
						aoutput.add(armorTypes[i]);
					} // end if
				}// end for loop
			}// end if Add

			//Removes an armor proficiency from the list that the class can use
			if (command == "Remove Armor") {
				for (int i = 0; i < aChoices.getSize(); i++) {
					if (alist2.isSelectedIndex(i)) {
						aChoices.remove(i);
						aoutput.remove(armorTypes[i]);
					} // end if
				}// end for loop
			}// end if Add

			//Everything is said and done and the user is happy with the class
			if (command == "Confirm New Class") {

				//checks to see which primary stats have been selected
				ArrayList<String> primaryStatSelect = new ArrayList<String>();
				for (int i = 0; i < stats.length; i++) {

					if (primaryStatBoxes.get(i).isSelected() == true) {
						if (primaryStatBoxes.get(i).getName().equals("Str"))
							primaryStatSelect.add("Strength");
						if (primaryStatBoxes.get(i).getName().equals("Dex"))
							primaryStatSelect.add("Dexterity");
						if (primaryStatBoxes.get(i).getName().equals("Int"))
							primaryStatSelect.add("Intelligence");
						if (primaryStatBoxes.get(i).getName().equals("Con"))
							primaryStatSelect.add("Constitution");
						if (primaryStatBoxes.get(i).getName().equals("Cha"))
							primaryStatSelect.add("Charisma");
						if (primaryStatBoxes.get(i).getName().equals("Wis"))
							primaryStatSelect.add("Wisdom");

					}

				}
				
				//checks to see which secondary stat has been selected
				String secondaryStatSelect = "";
				for (int i = 0; i < stats.length; i++) {

					if (secondaryStatBoxes.get(i).isSelected() == true) {
						if (secondaryStatBoxes.get(i).getName().equals("Str"))
							secondaryStatSelect += "Strength";
						else if (secondaryStatBoxes.get(i).getName()
								.equals("Dex"))
							secondaryStatSelect += "Dexterity";
						else if (secondaryStatBoxes.get(i).getName()
								.equals("Int"))
							secondaryStatSelect += "Intelligence";
						else if (secondaryStatBoxes.get(i).getName()
								.equals("Con"))
							secondaryStatSelect += "Constitution";
						else if (secondaryStatBoxes.get(i).getName()
								.equals("Cha"))
							secondaryStatSelect += "Charisma";
						else if (secondaryStatBoxes.get(i).getName()
								.equals("Wis"))
							secondaryStatSelect += "Wisdom";
					}
				}

				if (primaryStatSelect.isEmpty()) { //if no primary stats were selected, fix that
					JOptionPane
							.showMessageDialog(
									null,
									"No Primary Stats have been selected! You must select at least one ability before proceeding",
									"No Stats", JOptionPane.ERROR_MESSAGE);
				} else if (secondaryStatSelect.equals("")) { //if no secondary stat was selected, fix that
					JOptionPane
							.showMessageDialog(
									null,
									"No Secondary Stats have been selected! You must select at least one ability before proceeding",
									"No Stats", JOptionPane.ERROR_MESSAGE);
				} else if (classNameField.getText().equals("")) { //if the class has no name, give it one
					JOptionPane.showMessageDialog(null,
							"You must enter a Class Name", "Missing Field",
							JOptionPane.ERROR_MESSAGE);
				} else if (descriptionField.getText().equals("")) { //if the class has no description, give it one
					JOptionPane.showMessageDialog(null,
							"You must enter a Description", "Missing Field",
							JOptionPane.ERROR_MESSAGE);
				} else if (fChoices.isEmpty()) { //if the class has no weapon proficiency, give it some
					JOptionPane
							.showMessageDialog(
									null,
									"You must enter at least one fight type into the list of Fighting Types",
									"Missing Field", JOptionPane.ERROR_MESSAGE);
				} else if (aChoices.isEmpty()) { //if the class has no armor proficiency, give it some
					JOptionPane
							.showMessageDialog(
									null,
									"You must enter at least one armor into the list of Armor Types",
									"Missing Field", JOptionPane.ERROR_MESSAGE);
				} else {//else, you are good to go

					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to create this class?\n",
							"Confirm Class", JOptionPane.YES_NO_OPTION);

					// if user uses yes option to confirm, run the confirm
					if (confirm == JOptionPane.YES_OPTION) {

						dm.addClass(classNameField.getText(), roleList
								.getSelectedItem().toString(), powerList
								.getSelectedItem().toString(), hitList
								.getSelectedItem().toString(), descriptionField
								.getText(), goldList.getSelectedItem()
								.toString(), primaryStatSelect,
								secondaryStatSelect, alignmentList
										.getSelectedItem().toString(), foutput, aoutput);
						try {
							ccp.setUpClassPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							ccp.swapClassPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						classCreator.dispose();
					}
				}// IF ALL INPUT IS CORRECT
			}//end confirm new class if
		}//end action performed
	}//end event handler
}//end ClassCreator class