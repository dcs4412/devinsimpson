package ControlPanels;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.Dice;
import main_panels.JPanelWithBackground;
import DescriptionPanels.DescriptionPanel_Details;

/**
 * ControlPanel_Details:
 * This class is used for the creation of the panel that shows the details
 * of the character the user has been working on.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Details extends JPanel {

	private static final Object[] String = null;

	private int width, height;

	EventHandler eh = new EventHandler(); //handles events

	private Character_Creation_Panel ccp = null; //reference to character creation panel
	private Dice dice = null; //reference to dice class
	private int[] stats;      //stats of the character
	private int[] savingThrows; //number of saving throws
	private String hitDice = null; //which hit dice are used
	private String goldDice = null; //which gold dice are used
	private String defaultText = "Enter your character details, Fill in all the neccessary fields. For information about certain fields, place mouse over the the name label.";
	private ArrayList<String> textInputNames = null;
	private ArrayList<JTextComponent> textInputItems = null;
	private ArrayList<JComboBox<String>> textInputCombo = null;
	private ArrayList<String> comboInputNames = null; 
	private JComboBox<String> alignmentlist = new JComboBox<String>(); //list of available alignments

	private static Database_Manager dm = null; //database manager reference

	private String oldCharacterName = null;

	private String mode = null;

	private int charismaStatIndex = 0;
	private int constitutionStatIndex = 1;
	private int dexterityStatIndex = 2;
	private int intelligenceStatIndex = 3;
	private int strengthStatIndex = 4;
	private int wisdomStatIndex = 5;

	private int baseArmorClass = 10;

	/**
	 * Constructor for the class ControlPanel_Details
	 * Called in Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param c
	 * @param st
	 * @param hitD
	 * @param goldD
	 * @param d
	 * @param db
	 * @param md
	 * @throws IOException
	 * @author Thomas Man, Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Details(int w, int h, Character_Creation_Panel c,
			int[] st, String hitD, String goldD, Dice d, Database_Manager db,
			String md) throws IOException {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Details Control Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel, BorderLayout.NORTH);

		ccp = c;
		dice = d;
		dm = db;
		stats = st;
		hitDice = hitD;
		goldDice = goldD;

		mode = md;

		textInputNames = new ArrayList<String>();
		textInputItems = new ArrayList<JTextComponent>();
		textInputCombo = new ArrayList<JComboBox<String>>();
		savingThrows = new int[stats.length];
		comboInputNames = new ArrayList<String>();

		JPanel detailsPanel = buildDetailsPanel();
		this.add(detailsPanel);

		JButton confirm = new JButton("Confirm Details");
		confirm.addActionListener(eh);

		this.add(confirm, BorderLayout.SOUTH);
	}

	/**
	 * @author Devin Simpson Create the details panel for the character creator,
	 *         all the items in this panel are hard coded unlike other panels,
	 *         this is due to the rigid structure of how character details are
	 *         described, users enter data in the text fields and text areas
	 *         that are enabled, and values are generated for text fields that
	 *         are not enabled.
	 * Called by class constructor
	 * @return the full character details panel
	 * @throws IOException
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildDetailsPanel() throws IOException {

		JPanel detailsPanel = new JPanelWithBackground("t_b_i_h.jpg");

		detailsPanel.setPreferredSize(new Dimension(400, 1100));
		detailsPanel.setLayout(new BorderLayout());
		detailsPanel.setOpaque(false);

		// build the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 2));
		topPanel.setPreferredSize(new Dimension(400, 300));
		topPanel.setOpaque(false);

		// build the saving throws panel
		JPanel savingThrowsPanel = buildSavingThrowPanel();
		savingThrowsPanel.setOpaque(false);

		// build the basic attributes panel
		JPanel basicAttributesPanel = buildBasicCharacterAttributesPanel();
		basicAttributesPanel.setOpaque(false);

		// add the basic attributes panel and saving throws panel in the reverse
		// order that they were made
		topPanel.add(basicAttributesPanel);
		topPanel.add(savingThrowsPanel);

		// build the center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 2));
		centerPanel.setPreferredSize(new Dimension(400, 800));
		centerPanel.setOpaque(false);

		// build the center left panel
		JPanel centerLeftPanel = new JPanel();
		centerLeftPanel.setLayout(new GridLayout(2, 1));
		centerLeftPanel.setOpaque(false);

		// build the appearance Panel and add it to the center left panel
		JPanel appearancePanel = buildAppearancePanel();
		centerLeftPanel.add(appearancePanel);

		// build the back story Panel and add it to the center left panel
		JPanel backStoryPanel = buildBackStoryPanel();
		centerLeftPanel.add(backStoryPanel);

		// build the center right panel
		JPanel centerRightPanel = new JPanel();
		centerRightPanel.setPreferredSize(new Dimension(400, 800));
		centerRightPanel.setLayout(new BorderLayout());
		centerRightPanel.setOpaque(false);

		// build the personality panel and add it to the center right panel
		JPanel personalityPanel = buildPersonalityPanel();
		centerRightPanel.add(personalityPanel);

		// add the center right and center left panels to the center panel
		centerPanel.add(centerLeftPanel);
		centerPanel.add(centerRightPanel);

		// add the top, center, and bottom panels to the details panel
		detailsPanel.add(topPanel, BorderLayout.NORTH);
		detailsPanel.add(centerPanel, BorderLayout.CENTER);

		// create a scroll pane for the panel and then create a panel to hold
		// the scroll pane
		JScrollPane scrollPane = new JScrollPane(detailsPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullDetailsPanel = new JPanelWithBackground("t_b_i_h.jpg");
		fullDetailsPanel.setLayout(new BorderLayout());
		fullDetailsPanel.add(scrollPane);

		// return the panel with the scroll pane
		return fullDetailsPanel;

	}//end of BuildDetailsPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the panel that contains the character's basic attributes
	 *         which are the character's name health, and wealth
	 *         Called in buildDetailsPanel
	 * @param color
	 *            , the background color
	 * @return the basic character attributes JPanel
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildBasicCharacterAttributesPanel() {

		// start the overall Basic Character Attributes Panel
		JPanel basicCharacterAttributesPanel = new JPanel();
		basicCharacterAttributesPanel.setLayout(new BorderLayout());
		basicCharacterAttributesPanel.setPreferredSize(new Dimension(200, 300));
		basicCharacterAttributesPanel.setOpaque(false);

		// create the panel for the user to input the character's name
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.setPreferredSize(new Dimension(200, 60));
		namePanel.setOpaque(false);
		JTextField characterName = new JTextField();
		namePanel.add(characterName, BorderLayout.CENTER);
		TitledBorder namePanelBorder = getBorderForPanel("Name");
		namePanel.setBorder(namePanelBorder);

		textInputNames.add("Name");
		textInputItems.add(characterName);
		// create the panel for the character's health
		JPanel healthPanel = new JPanel();
		healthPanel.setLayout(new GridLayout(3, 1));
		healthPanel.setPreferredSize(new Dimension(200, 180));
		healthPanel.setOpaque(false);
		// create the three sub-panels for the health panel;

		// first sub-panel is the hit points panel

		// first roll for the character's hitpoints

		String[] hitPoints = { Integer.toString(dice.rollSum(hitDice)
				+ savingThrows[constitutionStatIndex]) };

		JPanel hitPointPanel = buildSubPanel(50, 30, "Hit Points", 11, 30, 0,
				0, "text", false, hitPoints);
		hitPointPanel.setOpaque(false);
		hitPointPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Health, or Hit Points, is generated based on your class' hit die. Once a class is picked, your health is determined by the outcome fo the hit dice + your constitution modifier. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});
		// second sub-panel is the armor class panel

		// generate base armor class for character
		String[] armorClass = { Integer.toString(baseArmorClass
				+ savingThrows[dexterityStatIndex]) };

		JPanel armorClassPanel = buildSubPanel(50, 30, "Armor Class", 0, 0, 0,
				0, "text", false, armorClass);
		armorClassPanel.setOpaque(false);
		armorClassPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Armor Class is generated by your class, equipment and dexterity modifier. Your Armor Class (AC) represents how hard it is for opponents to land a solid, damaging blow on you. It’s the attack roll result that an opponent needs to achieve to hit you. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// create the options for character levels
		String[] levelOptions = { "1", "2", "3" };

		// third sub-panel is the level panel
		JPanel levelPanel = buildSubPanel(50, 30, "Level", 34, 30, 0, 0,
				"list", true, levelOptions);
		levelPanel.setOpaque(false);
		levelPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Dungeons and Dragon have a system to measure strength and knowledge which is also referred to as levels. A character will usually start out at level 1. Once he gains enough experience points (or EXP) through the game he will be granted an additional level making him level 2 and so on. This increase in level is referred to as leveling up or the short version lvl up. In addition to being a form of measurement, levels increase stats like hit points or strength points rewarding the player and opening up the use of equipment or difficult parts of the game that they were previously unable to survive in.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// now add the three sub-panels to the health panel and get a border for
		// the health panel
		healthPanel.add(hitPointPanel);
		healthPanel.add(armorClassPanel);
		healthPanel.add(levelPanel);
		TitledBorder healthPanelBorder = getBorderForPanel("Health");
		healthPanel.setBorder(healthPanelBorder);

		// create the panel for the character's wealth
		JPanel wealthPanel = new JPanel();
		wealthPanel.setLayout(new BorderLayout());
		wealthPanel.setPreferredSize(new Dimension(200, 60));
		wealthPanel.setOpaque(false);
		wealthPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Wealth is determined by your class background. When you select your class, gold will be auto-generated based on your class. This value can be use to purchase weapons, armor and gear.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});
		// create the three sub-panels for the wealth panel;

		// first sub-panel is the gold panel
		// make an initial gold generation roll
		String[] gold = { generateGold() };
		JPanel goldPanel = buildSubPanel(33, 30, "Gold", 0, 0, 70, 60, "text",
				false, gold);
		goldPanel.setOpaque(false);

		// second sub-panel is the silver panel

		// character has 0 silver at creation
		String[] silver = { "00" };
		JPanel silverPanel = buildSubPanel(20, 30, "Silver", 0, 0, 66, 60,
				"text", false, silver);
		silverPanel.setOpaque(false);

		// third sub-panel is the copper panel
		// character has 0 copper at creation
		String[] copper = { "00" };
		JPanel copperPanel = buildSubPanel(20, 30, "Copper", 0, 0, 75, 60,
				"text", false, copper);
		copperPanel.setOpaque(false);

		// now add the three sub-panels to the wealth panel and get a border for
		// the wealth panel
		wealthPanel.add(goldPanel, BorderLayout.WEST);
		wealthPanel.add(silverPanel, BorderLayout.CENTER);
		wealthPanel.add(copperPanel, BorderLayout.EAST);
		TitledBorder wealthPanelBorder = getBorderForPanel("Wealth");
		wealthPanel.setBorder(wealthPanelBorder);
		wealthPanel.setOpaque(false);

		// add the name, health, and wealth panel to the overall Basic Character
		// Attributes Panel
		basicCharacterAttributesPanel.add(namePanel, BorderLayout.NORTH);
		basicCharacterAttributesPanel.add(healthPanel, BorderLayout.CENTER);
		basicCharacterAttributesPanel.add(wealthPanel, BorderLayout.SOUTH);

		// add a border to the overall Basic Character Attributes Panel and
		// return it
		TitledBorder basicCharacterAttributesPanelBorder = getBorderForPanel("Basic Character Attributes");
		basicCharacterAttributesPanel
				.setBorder(basicCharacterAttributesPanelBorder);

		return basicCharacterAttributesPanel;

	}//end buildBasicCharacterAttributesPanel method

	/**
	 * Builds the Saving Throws Panel
	 * Called in buildDetailsPanel
	 * @return savingThrowPanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildSavingThrowPanel() {

		JPanel savingThrowPanel = new JPanel();
		savingThrowPanel.setLayout(new GridLayout(6, 1));
		savingThrowPanel.setPreferredSize(new Dimension(200, 300));
		savingThrowPanel.setOpaque(false);
		savingThrowPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Generally, when you are subject to an unusual or magical attack, you get a saving throw to avoid or reduce the effect. Like an attack roll, a saving throw is a d20 roll plus a bonus based on your class, level, and an ability score. Your saving throw modifier is determined by your ability score, or stats \n\n"
								+ "Saving Throw Types:\nThe six different kinds of saving throws are Strength, Dexterity, Intelligence, Wisdom, Charisma, and Constituion."
								+ "\nEach saving throw is used as ability checks when performing certain skills or abilities. They require the stat modifier to determine the success of the character's action ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});
		// build each of the six saving throw sub panels

		// the first sub panel is the charisma saving throw panel
		String[] charismaSavingThrow = { getSavingThrow(
				stats[charismaStatIndex], charismaStatIndex) };
		JPanel charismaSavingThrowPanel = buildSubPanel(50, 30, "Charisma", 15,
				30, 0, 0, "text", false, charismaSavingThrow);
		charismaSavingThrowPanel.setOpaque(false);
		// the second sub panel is the constitution saving throw panel
		String[] constitutionSavingThrow = { getSavingThrow(
				stats[constitutionStatIndex], constitutionStatIndex) };
		JPanel constitutionSavingThrowPanel = buildSubPanel(50, 30,
				"Constitution", 1, 30, 0, 0, "text", false,
				constitutionSavingThrow);
		constitutionSavingThrowPanel.setOpaque(false);

		// the third sub panel is the dexterity saving throw panel
		String[] dexteritySavingThrow = { getSavingThrow(
				stats[dexterityStatIndex], dexterityStatIndex) };
		JPanel dexteritySavingThrowPanel = buildSubPanel(50, 30, "Dexterity",
				18, 30, 0, 0, "text", false, dexteritySavingThrow);
		dexteritySavingThrowPanel.setOpaque(false);

		// the forth sub panel is the intelligence saving throw panel
		String[] intelligenceSavingThrow = { getSavingThrow(
				stats[intelligenceStatIndex], intelligenceStatIndex) };
		JPanel intelligenceSavingThrowPanel = buildSubPanel(50, 30,
				"Intelligence", 3, 30, 0, 0, "text", false,
				intelligenceSavingThrow);
		intelligenceSavingThrowPanel.setOpaque(false);

		// the fifth sub panel is the strength saving throw panel
		String[] strengthSavingThrow = { getSavingThrow(
				stats[strengthStatIndex], strengthStatIndex) };
		JPanel strengthSavingThrowPanel = buildSubPanel(50, 30, "Strength", 18,
				30, 0, 0, "text", false, strengthSavingThrow);
		strengthSavingThrowPanel.setOpaque(false);

		// the sixth sub panel is the wisdom saving throw panel
		String[] wisdomSavingThrow = { getSavingThrow(stats[wisdomStatIndex],
				wisdomStatIndex) };
		JPanel wisdomSavingThrowPanel = buildSubPanel(50, 30, "Wisdom", 20, 30,
				0, 0, "text", false, wisdomSavingThrow);
		wisdomSavingThrowPanel.setOpaque(false);

		// now add the six sub-panels to the saving throws panel
		savingThrowPanel.add(charismaSavingThrowPanel);
		savingThrowPanel.add(constitutionSavingThrowPanel);
		savingThrowPanel.add(dexteritySavingThrowPanel);
		savingThrowPanel.add(intelligenceSavingThrowPanel);
		savingThrowPanel.add(strengthSavingThrowPanel);
		savingThrowPanel.add(wisdomSavingThrowPanel);

		// add a border to the overall Saving Throws Panel and return it
		TitledBorder savingThrowPanelBorder = getBorderForPanel("Saving Throws");
		savingThrowPanel.setBorder(savingThrowPanelBorder);

		return savingThrowPanel;

	}//end buildSavingThrowPanel method

	/**
	 * Builds the panel where the user inserts details about the appearance of the character
	 * Called by buildDetailsPanel
	 * @return appearancePanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildAppearancePanel() {
		JPanel appearancePanel = new JPanel();
		appearancePanel.setPreferredSize(new Dimension(200, 400));
		appearancePanel.setLayout(new GridLayout(2, 1));
		appearancePanel.setOpaque(false);

		// build the top half of the appearance panel
		JPanel appearanceTopPanel = new JPanel();
		appearanceTopPanel.setLayout(new GridLayout(4, 1));
		appearanceTopPanel.setOpaque(false);
		// build the four sub panels to the appearance top panel

		// first sub panel is the age panel
		JPanel agePanel = buildSubPanel(66, 30, "Age", 15, 30, 0, 0, "text",
				true, null);
		agePanel.setOpaque(false);
		agePanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Enter is age of your character. Age can determine the player's experiences and knowlegde of the role-played world.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// second sub panel is the height panel
		JPanel heightPanel = buildSubPanel(66, 30, "Height", 2, 30, 0, 0,
				"text", true, null);
		heightPanel.setOpaque(false);
		heightPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Enter the height in 'feet'. The height should be based on your race. Please reference to your race's size and then determine your height.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// third sub panel is the weight panel
		JPanel weightPanel = buildSubPanel(66, 30, "Weight", 0, 0, 0, 0,
				"text", true, null);
		weightPanel.setOpaque(false);
		weightPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Enter the weight in pounds. The weight should be based on your race. Please reference to your race's size and then determine your weight. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// create the gender options
		String[] genders = { "Male", "Female" };

		// forth sub panel is the gender panel
		JPanel genderPanel = buildSubPanel(66, 30, "Gender", 0, 0, 0, 0,
				"list", true, genders);
		genderPanel.setOpaque(false);
		genderPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Enter the gender of your character, either male or female. If your race, or character has no classfied gender or is both, put MALE as a default.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// add the four sub panels to the top appearance panel
		appearanceTopPanel.add(agePanel);
		appearanceTopPanel.add(heightPanel);
		appearanceTopPanel.add(weightPanel);
		appearanceTopPanel.add(genderPanel);

		// build the appearance bottom panel
		JPanel appearanceBottomPanel = buildTextAreaPanel("Details");
		appearanceBottomPanel.setOpaque(false);
		appearanceBottomPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("In this text area, enter a detailed description of your character's appearance. Describe his facial and body features such as eye color, hair color, and body formation or defects if any.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// add the top and bottom panels to the appearance panel
		appearancePanel.add(appearanceTopPanel);
		appearancePanel.add(appearanceBottomPanel);

		// add a border to the overall appearance Panel and return it
		TitledBorder appearancePanelBorder = getBorderForPanel("Appearance");
		appearancePanel.setBorder(appearancePanelBorder);

		return appearancePanel;
	}//end buildAppearancePanel method

	/**
	 * Builds the panel where the user inserts details about the backstory of the character
	 * Called by buildDetailsPanel
	 * @return backStoryPanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildBackStoryPanel() {

		JPanel backStoryPanel = new JPanel();
		backStoryPanel.setLayout(new BorderLayout());
		backStoryPanel.setOpaque(false);
		JTextArea backStory = new JTextArea();
		backStory.setLineWrap(true);
		backStoryPanel.add(backStory);

		// add a border to the overall appearance Panel and return it
		TitledBorder backStoryPanelBorder = getBorderForPanel("Back Story");
		backStoryPanel.setBorder(backStoryPanelBorder);

		backStoryPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("In this text area, enter a detailed description of your character's origin and backstory. Describe his birthplace, childhood bringings and any other characteristics that define the character that you have not describe in your personality. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		JPanel bottomBackStory = new JPanel();
		bottomBackStory.setLayout(new BorderLayout());
		backStoryPanel.add(bottomBackStory, BorderLayout.SOUTH);
		bottomBackStory.setOpaque(false);
		JLabel alignmentNameLabel = new JLabel(" Alignment:  ", JLabel.CENTER);
		alignmentNameLabel.setOpaque(false);
		alignmentNameLabel.setBackground(Color.red);
		alignmentNameLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("A person's general moral and personal attitudes are represented by its alignment: lawful good, neutral good, chaotic good, lawful neutral, neutral, chaotic neutral, lawful evil, neutral evil, or chaotic evil. Alignment is a tool for developing your character's identity. It is not a straitjacket for restricting your character. Each alignment represents a broad range of personality types or personal philosophies, so two characters of the same alignment can still be quite different from each other. In addition, few people are completely consistent.. ");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});
		bottomBackStory.add(alignmentNameLabel, BorderLayout.CENTER);

		String[] alignment = { "Lawful Good", "Lawful Neutral", "Lawful Evil",
				"Neutral Good", "True Neutral", "Neutral Evil", "Chaotic Good",
				"Chaotic Neutral", "Chaotic Evil" };

		alignmentlist = new JComboBox<String>(alignment);
		bottomBackStory.add(alignmentlist, BorderLayout.EAST);

		alignmentlist.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {

				if (alignmentlist.getSelectedItem().equals("Lawful Good")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Lawful Good: \n"
									+ "A lawful good character acts as a good person is expected or required to act. He combines a commitment to oppose evil with the discipline to fight relentlessly. He tells the truth, keeps his word, helps those in need, and speaks out against injustice. A lawful good character hates to see the guilty go unpunished. Lawful good is the best alignment you can be because it combines honor and compassion. Lawful good can be a dangerous alignment when it restricts freedom and criminalizes self-interest.");
				}

				if (alignmentlist.getSelectedItem().equals("Lawful Neutral")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Lawful Neutral: \n"
									+ "A lawful neutral character acts as law, tradition, or a personal code directs her. Order and organization are paramount to her. She may believe in personal order and live by a code or standard, or she may believe in order for all and favor a strong, organized government. Lawful neutral is the best alignment you can be because it means you are reliable and honorable without being a zealot. Lawful neutral can be a dangerous alignment when it seeks to eliminate all freedom, choice, and diversity in society.");
				}

				if (alignmentlist.getSelectedItem().equals("Lawful Evil")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Lawful Evil: \n"
									+ "A lawful evil villain methodically takes what he wants within the limits of his code of conduct without regard for whom it hurts. He cares about tradition, loyalty, and order but not about freedom, dignity, or life. He plays by the rules but without mercy or compassion. He is comfortable in a hierarchy and would like to rule, but is willing to serve. He condemns others not according to their actions but according to race, religion, homeland, or social rank. He is loath to break laws or promises.");
				}

				if (alignmentlist.getSelectedItem().equals("Neutral Good")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Neutral Good: \n"
									+ "Creatures of neutral good alignment believe that there must be some regulation in combination with freedoms if the best is to be brought to the world--the most beneficial conditions for living things in general and intelligent creatures in particular. Creatures of this alignments see the cosmos as a place where law and chaos are merely tools to use in bringing life, happiness, and prosperity to all deserving creatures. Order is not good unless it brings this to all; neither is randomness and total freedom desirable if it does not bring such good.These characters value life and freedom above all else, and despise those who would deprive others of them. Neutral good characters sometimes find themselves forced to work beyond the law, yet for the law, and the greater good of the people. They are not vicious or vindictive, but are people driven to right injustice. Neutral good characters always attempt to work within the law whenever possible, however.");
				}
				if (alignmentlist.getSelectedItem().equals("True Neutral")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("True Neutral: \n"
									+ "A neutral character does what seems to be a good idea. She doesn't feel strongly one way or the other when it comes to good vs. evil or law vs. chaos. Most neutral characters exhibit a lack of conviction or bias rather than a commitment to neutrality. Such a character thinks of good as better than evil-after all, she would rather have good neighbors and rulers than evil ones. Still, she's not personally committed to upholding good in any abstract or universal way. Some neutral characters, on the other hand, commit themselves philosophically to neutrality. They see good, evil, law, and chaos as prejudices and dangerous extremes. They advocate the middle way of neutrality as the best, most balanced road in the long run. Neutral is the best alignment you can be because it means you act naturally, without prejudice or compulsion. Neutral can be a dangerous alignment when it represents apathy, indifference, and a lack of conviction.");
				}
				if (alignmentlist.getSelectedItem().equals("Neutral Evil")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Neutral Evil: \n"
									+ "A neutral evil villain does whatever she can get away with. She is out for herself, pure and simple. She sheds no tears for those she kills, whether for profit, sport, or convenience. She has no love of order and holds no illusion that following laws, traditions, or codes would make her any better or more noble. On the other hand, she doesn't have the restless nature or love of conflict that a chaotic evil villain has. Some neutral evil villains hold up evil as an ideal, committing evil for its own sake. Most often, such villains are devoted to evil deities or secret societies. Neutral evil beings consider their alignment to be the best because they can advance themselves without regard for others. Neutral evil is the most dangerous alignment because it represents pure evil without honor and without variation.");
				}
				if (alignmentlist.getSelectedItem().equals("Chaotic Good")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Chaotic Good: \n"
									+ "A chaotic good character acts as his conscience directs him with little regard for what others expect of him. He makes his own way, but he's kind and benevolent. He believes in goodness and right but has little use for laws and regulations. He hates it when people try to intimidate others and tell them what to do. He follows his own moral compass, which, although good, may not agree with that of society. Chaotic good is the best alignment you can be because it combines a good heart with a free spirit. Chaotic good can be a dangerous alignment when it disrupts the order of society and punishes those who do well for themselves.");
				}
				if (alignmentlist.getSelectedItem().equals("Chaotic Neutral")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Chaotic Neutral: \n"
									+ "A chaotic neutral character follows his whims. He is an individualist first and last. He values his own liberty but doesn't strive to protect others' freedom. He avoids authority, resents restrictions, and challenges traditions. A chaotic neutral character does not intentionally disrupt organizations as part of a campaign of anarchy. To do so, he would have to be motivated either by good (and a desire to liberate others) or evil (and a desire to make those different from himself suffer). A chaotic neutral character may be unpredictable, but his behavior is not totally random. He is not as likely to jump off a bridge as to cross it. Chaotic neutral is the best alignment you can be because it represents true freedom from both society's restrictions and a do-gooder's zeal. Chaotic neutral can be a dangerous alignment when it seeks to eliminate all authority, harmony, and order in society");
				}
				if (alignmentlist.getSelectedItem().equals("Chaotic Evil")) {
					DescriptionPanel_Details.detailDescriptionText
							.setText("Chaotic Evil: \n"
									+ "A chaotic evil character does whatever his greed, hatred, and lust for destruction drive him to do. He is hot-tempered, vicious, arbitrarily violent, and unpredictable. If he is simply out for whatever he can get, he is ruthless and brutal. If he is committed to the spread of evil and chaos, he is even worse. Thankfully, his plans are haphazard, and any groups he joins or forms are poorly organized. Typically, chaotic evil people can be made to work together only by force, and their leader lasts only as long as he can thwart attempts to topple or assassinate him. Chaotic evil is sometimes called 'demonic' because demons are the epitome of chaotic evil. Chaotic evil beings believe their alignment is the best because it combines self-interest and pure freedom. Chaotic evil is the most dangerous alignment because it represents the destruction not only of beauty and life but also of the order on which beauty and life depend.");
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				// change the description panel to that of the default weapon
				// description.
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}

		});

		textInputNames.add("Back Story");
		textInputItems.add(backStory);
		comboInputNames.add("Alignment");
		textInputCombo.add(alignmentlist);

		return backStoryPanel;

	}//end buildBackStoryPanel method

	/**
	 * Builds the panel where the user inserts details about the personality of the character
	 * Called by buildDetailsPanel
	 * @return personalityPanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildPersonalityPanel() {

		JPanel personalityPanel = new JPanel();
		personalityPanel.setLayout(new GridLayout(4, 1));
		personalityPanel.setOpaque(false);
		// build the text area panel for character's personality traits
		JPanel personalityTraitsPanel = buildTextAreaPanel("Personality Traits");
		personalityTraitsPanel.setOpaque(false);
		personalityTraitsPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Personality traits are small, simple ways to help you set your character apart from every other character. Your personality traits should tell you something interesting and fun about your character. They should be selfdescriptions that are specific about what makes your character stand out. “I’m smart” is not a good trait, because it describes a lot of characters. “I’ve read every book in Candlekeep” tells you something specific about your character’s interests and disposition. Personality traits might describe the things your character likes, his or her past accomplishments, things your character dislikes or fears, your character’s self attitude or mannerisms, or the influence of his or her ability scores. A useful place to start thinking about personality traits is to look at your highest and lowest ability scores and define one trait related to each. Either one could be positive or negative: you might work hard to overcome a low score, for example, or be cocky about your high score.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// build the text area panel for character's ideals
		JPanel idealsPanel = buildTextAreaPanel("Ideals");
		idealsPanel.setOpaque(false);
		idealsPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Describe one ideal that drives your character. Your ideals are the things that you believe in most strongly, the fundamental moral and ethical principles that compel you to act as you do. Ideals encompass everything from your life goals to your core belief system. Ideals might answer any of these questions: What are the principles that you will never betray? What would prompt you to make sacrifices? What drives you to act and guides your goals and ambitions? What is the single most important thing you strive for? You can choose any ideals you like, but your character’s alignment is a good place to start defining them. Each background in this chapter includes six suggested ideals. Five of them are linked to aspects of alignment: law, chaos, good, evil, and neutrality. The last one has more to do with the particular background than with moral or ethical perspectives.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// build the text area panel for character's bonds
		JPanel bondsPanel = buildTextAreaPanel("Bonds");
		bondsPanel.setOpaque(false);
		bondsPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Create one bond for your character. Bonds represent a character’s connections to people, places, and events in the world. They tie you to things from your background. They might inspire you to heights of heroism, or lead you to act against your own best interests if they are threatened. They can work very much like ideals, driving a character’s motivations and goals. Bonds might answer any of these questions: Whom do you care most about? To what place do you feel a special connection? What is your most treasured possession? Your bonds might be tied to your class, your background, your race, or some other aspect of your character’s history or personality. You might also gain new bonds over the course of your adventures.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// build the text area panel for character's flaws
		JPanel flawsPanel = buildTextAreaPanel("Flaws");
		flawsPanel.setOpaque(false);
		flawsPanel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText("Your character’s flaw represents some vice, compulsion, fear, or weakness—in particular, anything that someone else could exploit to bring you to ruin or cause you to act against your best interests. More significant than negative personality traits, a flaw might answer any of these questions: What enrages you? What’s the one person, concept, or event that you are terrified of? What are your vices?");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				DescriptionPanel_Details.detailDescriptionText
						.setText(defaultText);
			}
		});

		// add the text area panels to the personality panel
		personalityPanel.add(personalityTraitsPanel);
		personalityPanel.add(idealsPanel);
		personalityPanel.add(bondsPanel);
		personalityPanel.add(flawsPanel);

		// add a border to the overall personality panel and return it
		TitledBorder personalityPanelBorder = getBorderForPanel("Personality");
		personalityPanel.setBorder(personalityPanelBorder);

		return personalityPanel;
	}//end buildPersonalityPanel method

	/**
	 * Builds subpanels for all of the other building panels
	 * Called by the building panels
	 * @param width
	 * @param height
	 * @param name
	 * @param spaceWidth
	 * @param spaceHeight
	 * @param panelWidth
	 * @param panelHeight
	 * @param inputType
	 * @param enabledOrDisabled
	 * @param listData
	 * @return supPanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildSubPanel(int width, int height, String name,
			int spaceWidth, int spaceHeight, int panelWidth, int panelHeight,
			String inputType, boolean enabledOrDisabled, String[] listData) {

		JPanel supPanel = new JPanel();
		supPanel.setLayout(new FlowLayout());

		supPanel.setOpaque(false);
		if (panelWidth > 0 && panelHeight > 0) {

			supPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));

		}

		JLabel supPanelLabel = new JLabel(name);
		supPanelLabel.setOpaque(false);
		supPanel.add(supPanelLabel);

		if (spaceWidth > 0 && spaceHeight > 0) {
			JPanel supPanelSpace = new JPanel();
			supPanelSpace.setPreferredSize(new Dimension(spaceWidth,
					spaceHeight));
			supPanelSpace.setOpaque(false);
			supPanel.add(supPanelSpace);
		}

		if (inputType.equals("text")) {
			JTextField supPanelValue = new JTextField();
			supPanelValue.setEditable(enabledOrDisabled);

			supPanelValue.setPreferredSize(new Dimension(width, height));

			if (listData != null) {
				supPanelValue.setText(listData[0]);
			}

			textInputNames.add(name);
			textInputItems.add(supPanelValue);

			supPanel.add(supPanelValue);
		} else if (inputType.equals("list")) {
			JComboBox<String> supPanelValueList = new JComboBox<String>(listData);
			supPanelValueList.setPreferredSize(new Dimension(width, height));
			supPanel.add(supPanelValueList);
			comboInputNames.add(name);
			textInputCombo.add(supPanelValueList);
		}

		return supPanel;

	}//end BuildSubPanel method

	/**
	 * Builds Text Area panels for the buildPersonalityPanel
	 * Called by buildPersonalityPanel
	 * @param name
	 * @return textAreaPanel
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildTextAreaPanel(String name) {
		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setLayout(new BorderLayout());
		textAreaPanel.setOpaque(false);
		JLabel textAreaLabel = new JLabel(name, JLabel.CENTER);
		textAreaLabel.setOpaque(true);

		textAreaLabel.setOpaque(false);
		JTextArea textAreaInput = new JTextArea();
		textAreaInput.setLineWrap(true);

		textAreaPanel.add(textAreaLabel, BorderLayout.NORTH);
		textAreaPanel.add(textAreaInput, BorderLayout.CENTER);

		textInputNames.add(name);
		textInputItems.add(textAreaInput);

		return textAreaPanel;
	}//end buildTextAreaPanel

	/**
	 * Sets up Borders for all of the build panels
	 * Called by all building methods
	 * @param name
	 * @return title (TitleBorder)
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private TitledBorder getBorderForPanel(String name) {

		// create a titled border for the panel
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title;
		title = BorderFactory.createTitledBorder(blackline, name);
		title.setTitleJustification(TitledBorder.CENTER);

		return title;
	}

	/**
	 * Gets the saving throws
	 * Called by buildSavingThrowPanel and reSetSavingThrowsHealthArmorClassAndGold
	 * @param stat
	 * @param statIndex
	 * @return savingThrow
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private String getSavingThrow(int stat, int statIndex) {

		// Initialize saving throw to number lower than lowest valid saving
		// throw to ensure valid saving
		// throw generation
		String savingThrow = null;

		int tempSavingThrow = -4;

		if (stat >= 4 && stat <= 5) {

			tempSavingThrow = -3;

		} else if (stat >= 6 && stat <= 7) {

			tempSavingThrow = -2;

		} else if (stat >= 8 && stat <= 9) {

			tempSavingThrow = -1;

		} else if (stat >= 10 && stat <= 11) {

			tempSavingThrow = 0;

		} else if (stat >= 12 && stat <= 13) {

			tempSavingThrow = 1;

		} else if (stat >= 14 && stat <= 15) {

			tempSavingThrow = 2;

		} else if (stat >= 16 && stat <= 17) {

			tempSavingThrow = 3;

		} else if (stat >= 18 && stat <= 19) {

			tempSavingThrow = 4;

		} else if (stat >= 20 && stat <= 21) {

			tempSavingThrow = 5;
		}

		if (tempSavingThrow > -4) {
			savingThrows[statIndex] = tempSavingThrow;

			if (tempSavingThrow > 0) {
				savingThrow = "+" + Integer.toString(tempSavingThrow);

			} else {

				savingThrow = Integer.toString(tempSavingThrow);

			}
		}

		return savingThrow;
	}//end getSavingThrow method

	/**
	 * Grabs the text inputs and sends them to the database
	 * Called by the Database Manager
	 * @param labels
	 * @return detailTextInput
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getTextInputs(ArrayList<String> labels) {

		ArrayList<String> detailTextInput = new ArrayList<String>();

		for (int i = 0; i < textInputItems.size(); i++) {

			detailTextInput.add(textInputItems.get(i).getText());
		}
		return detailTextInput;

	}//end getTextInputs method

	/**
	 * Grabs the gender and level and sends them to the database
	 * Called by the Database Manager
	 * @return genAndLev
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getGenderAndLevel() {

		ArrayList<String> genAndLev = new ArrayList<String>();

		for (int i = 0; i < textInputCombo.size(); i++) {

			genAndLev.add((java.lang.String) textInputCombo.get(i)
					.getSelectedItem());

		}

		return genAndLev;

	}//end getGenderAndLevel method

	/**
	 * Grabs the gold, silver and copper and sends them to the database
	 * Called by the Database Manager
	 * @return goldSilverCopper
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public int[] getGoldSilverCopper() {

		int[] goldSilverCopper = new int[3];

		for (int i = 0; i < textInputNames.size(); i++) {

			switch (textInputNames.get(i)) {
			case "Gold":
				goldSilverCopper[0] = Integer.parseInt(textInputItems.get(i)
						.getText());
				break;
			case "Silver":
				goldSilverCopper[1] = Integer.parseInt(textInputItems.get(i)
						.getText());
				break;
			case "Copper":
				goldSilverCopper[2] = Integer.parseInt(textInputItems.get(i)
						.getText());
				break;
			}
		}

		return goldSilverCopper;
	}//end getGoldSilverCopper method

	/**
	 * Grabs the character's name and sends it to the database
	 * Called by the Database Manager
	 * @return name
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public String getCharacterName() {

		String name = null;
		for (int i = 0; i < textInputNames.size(); i++) {
			switch (textInputNames.get(i)) {

			case "Name":
				name = textInputItems.get(i).getText();
				break;

			}
		}
		return name;
	}//end getCharacterName method

	/**
	 * Generates the gold the user can use
	 * Called by reSetSavingThrowsHealthArmorClassAndGold
	 * @return gold
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private String generateGold() {

		int goldConstant = 10;

		String gold = Integer.toString(dice.rollSum(goldDice) * goldConstant);

		return gold;

	}//end generateGold method

	/**
	 * Grabs the name of the character
	 * Called by the Database Manager
	 * @return oldCharacterName
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public String getOldCharacterName() {

		return oldCharacterName;

	}//end getOldCharacterName method

	/**
	 * Resets the attributes such as saving throws, health, armor class and cold
	 * Called by the Database Manager
	 * @param st
	 * @param hitD
	 * @param goldD
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void reSetSavingThrowsHealthArmorClassAndGold(int[] st, String hitD,
			String goldD) {

		stats = st;
		hitDice = hitD;
		goldDice = goldD;

		for (int i = 0; i < textInputNames.size(); i++) {

			switch (textInputNames.get(i)) {

			case "Hit Points":
				textInputItems.get(i).setText(
						Integer.toString(dice.rollSum(hitDice)
								+ savingThrows[constitutionStatIndex]));
				break;
			case "Armor Class":
				textInputItems.get(i).setText(
						Integer.toString(baseArmorClass
								+ savingThrows[dexterityStatIndex]));
				break;
			case "Gold":
				textInputItems.get(i).setText(generateGold());
				break;
			case "Silver":
				textInputItems.get(i).setText("00");
				break;
			case "Copper":
				textInputItems.get(i).setText("00");
				break;
			case "Charisma":
				textInputItems.get(i).setText(
						getSavingThrow(stats[charismaStatIndex],
								charismaStatIndex));
				break;
			case "Constitution":
				textInputItems.get(i).setText(
						getSavingThrow(stats[constitutionStatIndex],
								constitutionStatIndex));
				break;
			case "Dexterity":
				textInputItems.get(i).setText(
						getSavingThrow(stats[dexterityStatIndex],
								dexterityStatIndex));
				break;
			case "Intelligence":
				textInputItems.get(i).setText(
						getSavingThrow(stats[intelligenceStatIndex],
								intelligenceStatIndex));
				break;
			case "Strength":
				textInputItems.get(i).setText(
						getSavingThrow(stats[strengthStatIndex],
								strengthStatIndex));
				break;
			case "Wisdom":
				textInputItems.get(i)
						.setText(
								getSavingThrow(stats[wisdomStatIndex],
										wisdomStatIndex));
				break;
			}
		}
		setCharacterDetails();
	}//end reSetSavingThrowsHealthArmorClassAndGold method

	/**
	 * Sets the details from the player to the database
	 * Called by the Database Manager
	 * @param playerDetailsText
	 * @param playerDetailsCombo
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setDetailsFromPlayer(ArrayList<String> playerDetailsText,
			ArrayList<String> playerDetailsCombo) {

		// basing text setting on predetermined indexes of argument array lists

		int playerDetailsTextIndex = 0;
		int index = 0;
		while (playerDetailsTextIndex < 21) {

			if (index == textInputItems.size()) {

				index = 0;

			}

			switch (textInputNames.get(index)) {

			case "Name":
				if (playerDetailsTextIndex == 0) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					oldCharacterName = textInputItems.get(index).getText();
					playerDetailsTextIndex++;
				}
				break;
			case "Hit Points":
				if (playerDetailsTextIndex == 1) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Armor Class":
				if (playerDetailsTextIndex == 2) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Gold":
				if (playerDetailsTextIndex == 3) {

					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Silver":
				if (playerDetailsTextIndex == 4) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Copper":
				if (playerDetailsTextIndex == 5) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Charisma":
				if (playerDetailsTextIndex == 6) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Constitution":
				if (playerDetailsTextIndex == 7) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Dexterity":
				if (playerDetailsTextIndex == 8) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Intelligence":
				if (playerDetailsTextIndex == 9) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Strength":
				if (playerDetailsTextIndex == 10) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Wisdom":
				if (playerDetailsTextIndex == 11) {
					String savingThrow = "";
					int positiveTest = Integer.parseInt(playerDetailsText
							.get(playerDetailsTextIndex));

					if (positiveTest > 0) {

						savingThrow += "+";
					}
					savingThrow += playerDetailsText
							.get(playerDetailsTextIndex);

					textInputItems.get(index).setText(savingThrow);
					playerDetailsTextIndex++;
				}
				break;
			case "Back Story":
				if (playerDetailsTextIndex == 16) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Details":
				if (playerDetailsTextIndex == 15) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Personality Traits":
				if (playerDetailsTextIndex == 17) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Ideals":
				if (playerDetailsTextIndex == 18) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Bonds":
				if (playerDetailsTextIndex == 19) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Flaws":
				if (playerDetailsTextIndex == 20) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Age":
				if (playerDetailsTextIndex == 12) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Height":
				if (playerDetailsTextIndex == 13) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			case "Weight":
				if (playerDetailsTextIndex == 14) {
					textInputItems.get(index).setText(
							playerDetailsText.get(playerDetailsTextIndex));
					playerDetailsTextIndex++;
				}
				break;
			}

			index++;
		}

		int playerDetailsComboIndex = 0;

		for (int i = 0; i < comboInputNames.size(); i++) {
			switch (comboInputNames.get(i)) {
			case "Level":
				if (playerDetailsComboIndex == 0) {
					textInputCombo.get(i).setSelectedItem(
							playerDetailsCombo.get(i));
					playerDetailsComboIndex++;
				}
				break;

			case "Gender":
				if (playerDetailsComboIndex == 1) {
					textInputCombo.get(i).setSelectedItem(
							playerDetailsCombo.get(i));
					playerDetailsComboIndex++;
				}
			case "Alignment":
				if (playerDetailsComboIndex == 2) {
					textInputCombo.get(i + 1).setSelectedItem(
							playerDetailsCombo.get(i + 1));
					playerDetailsComboIndex++;
				}
				break;
			}//end switch

		}//end for loop

	}//end setDetailsFromPlayer method

	/**
	 * Sets the Characters Details
	 * Called by reSetSavingThrowsHealthArmorClassAndGold
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void setCharacterDetails() {

		int characterLevel = -1, starting_health = -1, armorClass = -1, strength = -9, dexterity = -9, charisma = -9, intelligence = -9, wisdom = -9, constitution = -9, age = -1;
		String characterName = null, gender = null, height = null, weight = null, appearance = null, backstory = null, personality_traits = null, ideals = null, bonds = null, flaws = null, alignment = null;

		for (int i = 0; i < textInputNames.size(); i++) {

			switch (textInputNames.get(i)) {

			case "Name":
				characterName = textInputItems.get(i).getText();
				break;
			case "Hit Points":
				starting_health = Integer.parseInt(textInputItems.get(i)
						.getText());
				break;
			case "Armor Class":
				armorClass = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Charisma":
				charisma = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Constitution":
				constitution = Integer
						.parseInt(textInputItems.get(i).getText());
				break;
			case "Dexterity":
				dexterity = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Intelligence":
				intelligence = Integer
						.parseInt(textInputItems.get(i).getText());
				break;
			case "Strength":
				strength = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Wisdom":
				wisdom = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Back Story":
				backstory = textInputItems.get(i).getText();
				break;
			case "Details":
				appearance = textInputItems.get(i).getText();
				break;
			case "Personality Traits":
				personality_traits = textInputItems.get(i).getText();
				break;
			case "Ideals":
				ideals = textInputItems.get(i).getText();
				break;
			case "Bonds":
				bonds = textInputItems.get(i).getText();
				break;
			case "Flaws":
				flaws = textInputItems.get(i).getText();
				break;
			case "Age":
				age = Integer.parseInt(textInputItems.get(i).getText());
				break;
			case "Height":
				height = textInputItems.get(i).getText();
				break;
			case "Weight":
				weight = textInputItems.get(i).getText();
				break;

			default:
				//System.out.println("ERROR IN ELEMENT HANDLING "
						//+ textInputNames.get(i));
			}

		}

		for (int i = 0; i < comboInputNames.size(); i++) {

			switch (comboInputNames.get(i)) {
			case "Level":
				characterLevel = Integer
						.parseInt((java.lang.String) textInputCombo.get(i)
								.getSelectedItem());
				break;

			case "Gender":
				gender = (java.lang.String) textInputCombo.get(i)
						.getSelectedItem();
				break;

			case "Alignment":
				alignment = (java.lang.String) textInputCombo.get(i)
						.getSelectedItem();
				break;

			default:
				System.out.println("ERROR IN COMBO HANDLING "
						+ comboInputNames.get(i));
			}
		}

		if (mode.equals("edit")) {
			dm.ChangeNames(oldCharacterName, characterName);
		}

		dm.setCharacter_Details(characterName, characterLevel, starting_health,
				armorClass, strength, dexterity, charisma, intelligence,
				wisdom, constitution, age, height, weight, gender, appearance,
				backstory, personality_traits, ideals, bonds, flaws, alignment);

	}//end setCharacterDetails method

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener {

		@Override
		/**
		 * @author Devin Simpson
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 */
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			if (command == "Confirm Details") {
				String emptyText = "";
				for (int i = 0; i < textInputNames.size(); i++) {

					if (textInputItems.get(i).getText().equals("")) {

						emptyText += textInputNames.get(i);
						if (textInputNames.get(i).equals("Back Story")) {

							emptyText += " (optional)";
						}

						emptyText += "\n";
					}

				}

				int confirm = -1;

				// if the empty text variable contains string but, does not
				// begin with back story, then the user is missing some data in
				// text fields they must enter
				if (!(emptyText.equals("") || emptyText
						.startsWith("Back Story"))) {

					JOptionPane.showMessageDialog(null,
							"You must fill out the following fields before you can proceed\n"
									+ emptyText, "Missing Fields",
							JOptionPane.ERROR_MESSAGE);

					// if only the back story is empty, the user may proceed,
					// but warn the user about the empty back story test
				} else if (emptyText.startsWith("Back Story")) {

					confirm = JOptionPane
							.showConfirmDialog(
									null,
									"Are you sure you want to proceed without a back story?",
									"No Back Story", JOptionPane.YES_NO_OPTION);

					// if all text fields are entered, confirm that the user
					// wants to keep the current data in each field
				} else {

					confirm = JOptionPane
							.showConfirmDialog(
									null,
									"Are you sure you want to proceed with the current character details?",
									"Confirm Details",
									JOptionPane.YES_NO_OPTION);
				}

				// is the user selects the yes option, send the detail data to
				// the database manager
				if (confirm == JOptionPane.YES_OPTION) {
					try {
						setCharacterDetails();
						ccp.handleAttributeConfirmations(command);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//end catch

				}//end if yes
			}//end confirm details

		}//end actionPerformed
	}//end eventHandler
}//end ControlPanel_Details class