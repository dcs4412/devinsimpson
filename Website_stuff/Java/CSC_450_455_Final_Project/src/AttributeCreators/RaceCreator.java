package AttributeCreators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * RaceCreator:
 * This class is used for the creation of new Races.
 * This class is one of four attribute creators used in creating new attributes.
 * This class exists to provide the users with greater options for races.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 6th, 2014 (Created 9/23/2014)
 */
public class RaceCreator extends JFrame {

	EventHandler eh = new EventHandler();				//Event Handler

	private Character_Creation_Panel ccp = null;		//Character Creation Panel reference
	private Database_Manager dm = null;					//Database Manager Reference
	private JTextArea raceNameField = new JTextArea();	//race name
	private JComboBox speedList = new JComboBox();		//speed list
	private JComboBox sizeList = new JComboBox();		//size list
	private JComboBox languageList = new JComboBox();	//language list
	private JComboBox skillList = new JComboBox();		//skill list
	private JComboBox skillList2 = new JComboBox();		//second skill list
	private ArrayList<String> skills = null;			//The skills that the race can use
	private ArrayList<String> skillDescriptions = null;	//The descriptions for the skills
	private ArrayList<JCheckBox> primaryStatBoxes = new ArrayList();	//stat bonuses
	private JTextArea descriptionField = new JTextArea();		//description text area
	private String[] stats = new String[6];						//6 stat bonuses
	private String[] statDescriptions = new String[6];			//stat descriptions
	private int primaryStatLimit = 2;							//2 stat bonuses allowed
	private int numberOfChecked = 0;							//number of boxes checked
	private JTextArea raceDescriptionText = new JTextArea();	//description panel text area
	private JPanel description_panel = new JPanel();			//the description panel itself

	// reference to object

	JFrame raceCreator = this;

	/**
	 * RaceCreator is the constructor for the class RaceCreator.
	 * Called in Character_Selection_Attribute_panel
	 * @param d (for Database Manager)
	 * @param cp (for Character Creation Panel)
	 * @throws IOException 
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public RaceCreator(Database_Manager d, Character_Creation_Panel cp) throws IOException {

		this.setTitle("Race Creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 500);
		this.setLayout(new BorderLayout());

		// set the window to appear in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);


		dm = d; //Database Manager
		ccp =cp; //Character Creation Panel
		skills = dm.getSkill_name();				//Skills that the race can use
		skillDescriptions = dm.getSkill_Description(skills); //Descriptions of those skills
		
		JPanel raceControlPanel = controlPanel();
		JPanel raceDisriptionPanel = descriptionPanel();
		this.add(raceControlPanel, BorderLayout.CENTER);
		this.add(raceDisriptionPanel, BorderLayout.EAST);
		
		
		
		
	}

	/**
	 * The Control Panel is used for holding all of the information related to the new Race.
	 * These include race name, speed, size, language, skills, stat bonus and description.
	 * Called in the class constructor
	 * @return control_panel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 * @throws IOException 
	 */
	private JPanel controlPanel() throws IOException {

		JPanel control_panel = new JPanel();	//Holds everything in the center of Race Creator
		control_panel.setSize(400, 400);
		control_panel.setLayout(new BorderLayout());
		control_panel.setBackground(Color.red);
		
		JLabel panelLabel = new JLabel("Race Creator Control Panel",	//label for center
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		control_panel.add(panelLabel, BorderLayout.NORTH);

		JPanel info_panel = new JPanelWithBackground("t_b_i_h.jpg"); // info panel
		info_panel.setSize(400, 400);
		info_panel.setLayout(new GridLayout(0, 1));
		control_panel.add(info_panel, BorderLayout.CENTER);

		JPanel raceInfo = new JPanel(); // race info panel: label and TextArea
		raceInfo.setLayout(new BorderLayout());
		info_panel.add(raceInfo);
		raceInfo.setOpaque(false);
		JLabel raceNameLabel = new JLabel(" Race Name:   ", JLabel.CENTER);
		raceNameLabel.setOpaque(false);
		raceNameLabel.setBackground(Color.red);
		raceNameLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determine the name of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		raceInfo.add(raceNameLabel, BorderLayout.WEST);

		raceNameField = new JTextArea("", 3, 29);		//text area used for the name of the race
		raceNameField.setPreferredSize(new Dimension(3, 3));
		raceNameField.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race name.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		raceInfo.add(raceNameField, BorderLayout.CENTER);

		JPanel roleInfo = new JPanel(); // role info panel: labels and combo boxes
		roleInfo.setLayout(new GridLayout(0, 5));
		info_panel.add(roleInfo);
		roleInfo.setOpaque(false);
		
		JLabel speedLabel = new JLabel("Speed: ", JLabel.CENTER); //Speed label for race
		speedLabel.setOpaque(false);
		speedLabel.setBackground(Color.orange);
		speedLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("Your speed tells you how far you can move in a round "
								+ "and still do something, such as attack or cast a spell. "
								+ "Your speed depends mostly on your race and what armor you’re wearing.");
			}
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		roleInfo.add(speedLabel);

		String[] speed = { "20", "25", "30", "35", "40", "45", "50" };

		speedList = new JComboBox(speed);	//Speed choices
		speedList.addActionListener(eh);
		speedList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determining the speed of your race.");
			}
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		roleInfo.add(speedList);

		JLabel extraSpace = new JLabel("", JLabel.RIGHT);	//extra space in center of 2nd row
		extraSpace.setOpaque(false);
		roleInfo.add(extraSpace);

		JLabel sizeLabel = new JLabel(" Size: ", JLabel.LEFT); //size label for race
		sizeLabel.setOpaque(false);
		sizeLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the size of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		roleInfo.add(sizeLabel);

		String[] size = { "Fine", "Diminutive", "Tiny", "Small", "Medium",
				"Large", "Huge", "Gargantuan", "Colossal" };
		
		sizeList = new JComboBox(size);	//size choices
		sizeList.addActionListener(eh);
		sizeList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				if(sizeList.getSelectedItem().equals("Fine")){
					raceDescriptionText.setBackground(Color.WHITE);
				raceDescriptionText.setText("Fine:\nA character or monster is considered Fine "
						+ "when he, she or it is stands shorter than 6 inches tall while weighing "
						+ "less than 1/8 of a pound. Such creatures take half a foot of space and occupy "
						+ "1/100th of a 5 ft. square. They have a 0 ft. reach. Mice or Cockroaches are Fine creatures.");	
				}

				if(sizeList.getSelectedItem().equals("Diminutive")){
					raceDescriptionText.setText("Diminutive:\nA character or monster is considered Diminutive when he, "
							+ "she or it stands between 6 inches to 1 foot tall while weighing 1/8th of a lb to 1 lb. "
							+ "Such creatures take one 1 ft. square of space and occupy 1/25th of a 5 ft. square. "
							+ "They have a 0 ft. reach. Rats and racoons are examples of diminutive creatures.");	
					}
				
				if(sizeList.getSelectedItem().equals("Tiny")){
					raceDescriptionText.setText("Tiny:\nA character or monster is considered Tiny when he, she or it stands between "
							+ "1 foot to 2 feet tall while weighing 1 lb to 8 lbs. Such creatures take one 2.5 ft. square of space "
							+ "and occupy 1/4th of a 5 ft. square. They have a 0 ft. reach. Fey and Dread Blossoms are examples of "
							+ "Tiny creatures.");	
					}
				
				if(sizeList.getSelectedItem().equals("Small")){
					raceDescriptionText.setText("Small:\nA character or monster is considered Small when he, she or it stands 2 to 4 feet "
							+ "tall while weighing 8 to 60 lb. Such creatures take one 5 ft. square of place and have 5 ft. reach. "
							+ "Out of standard races, gnomes and halflings are Small creatures.");	
					}
				
				if(sizeList.getSelectedItem().equals("Medium")){
					raceDescriptionText.setText("Medium:\nA character or monster is considered Medium when he, she or it stands 4 to 8 feet tall "
							+ "while weighing 60 to 500 lb. Such creatures take one 5 ft. square of place and have 5 ft. reach. "
							+ "Humans are Medium creatures. A Medium creature does not have a size modifier to anything.");	
					}
				
				if(sizeList.getSelectedItem().equals("Large")){
					raceDescriptionText.setText("Large:\nA character or a monster is considered Large when he, she or it stands 8 to 16 feet tall "
							+ "while weighing from 500 lb. to 2 tons. Such creatures usually take a 10 ft. square of place and have 5 ft. "
							+ "reach if they are long and 10 ft. reach if they are tall. A Large creature gets a -1 size penalty to "
							+ "Armor Class and attack rolls, a +4 size bonus to grapple checks and a -4 size penalty on Hide checks. "
							+ "A Large creature’s carrying capacity is twice as good as that of a Medium character (thrice for quadrupeds). "
							+ "A Large creature uses larger weapons than a Medium character.");	
					}
				
				if(sizeList.getSelectedItem().equals("Huge")){
					raceDescriptionText.setText("Huge:\nA character or a monster is considered Huge when he, she or it stands 16 to 32 feet tall "
							+ "while weighing 2 to 16 tons. Such creatures usually take a 15 ft. square of place and have 10 ft. reach if they are "
							+ "long and 15 ft. reach if they are tall. A Huge creature gets a -2 size penalty to Armor Class and attack rolls, "
							+ "a +8 size bonus to grapple checks and a -8 size penalty on Hide checks. A Huge creature’s carrying capacity is 4 times "
							+ "as good as that of a Medium character (6 times for quadrupeds). A Huge creature uses much larger weapons "
							+ "than a Medium character.");	
					}
				
				if(sizeList.getSelectedItem().equals("Gargantuan")){
					raceDescriptionText.setText("Gargantuan:\nA character or a monster is considered Gargantuan when he, she or it stands 32 to 64 feet tall "
							+ "while weighing 16 to 125 tons. Such creatures usually take a 20 ft. square of place and have 15 ft. reach if they are "
							+ "long and 20 ft. reach if they are tall. A Gargantuan creature gets a -4 size penalty to Armor Class and attack rolls, "
							+ "a +12 size bonus to grapple checks and a -12 size penalty on Hide checks. "
							+ "A Huge creature’s carrying capacity is 8 times as good as that of a Medium character (12 times for quadrupeds). "
							+ "A Gargantuan creature uses much larger weapons than a Medium character.");	
					}
				
				if(sizeList.getSelectedItem().equals("Colossal")){
					raceDescriptionText.setText("Colossal:\nColossal is the biggest possible size in D&D system. A character or a monster is considered Colossal when he, "
							+ "she or it exceeds 64 ft. in height or length and weighs more than 125 tons. Such creatures usually take a 30 ft. square of place "
							+ "and have 20 ft. reach if they are long and 30 ft. reach if they are tall."
							+ "A Colossal creature gets a -8 size penalty to Armor Class and attack rolls, a +16 size bonus to grapple checks "
							+ "and a -16 size penalty on Hide checks. A Colossal creature’s carrying capacity is 16 times as good as that of a "
							+ "Medium character (24 times for quadrupeds).");	
					}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		roleInfo.add(sizeList);

		JPanel dieInfo = new JPanel(); // more race info panel: labels and combo boxes
		dieInfo.setLayout(new GridLayout(0, 5));
		info_panel.add(dieInfo);
		dieInfo.setOpaque(false);
		
		JLabel languageLabel = new JLabel("Language: ", JLabel.CENTER);	//language label for race
		languageLabel.setOpaque(false);
		languageLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the language of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		dieInfo.add(languageLabel);

		String[] languages = { "Abyssal", "Aquan", "Auran", "Celestial",
				"Common", "Draconic", "Druidic", "Dwarven", "Elven", "Giant",
				"Gnome", "Goblin", "Gnoll", "Halfling", "Ignan", "Infernal",
				"Orc", "Sylvan", "Terran", "Undercommon" };

		languageList = new JComboBox(languages);	//language choices
		languageList.addActionListener(eh);
		languageList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				if(languageList.getSelectedItem().equals("Abyssal")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nAbyssal\n"
							+ "Typical Speakers:\nDemons, Chaotic Evil Outsiders\n"
							+ "Alphabet:\nInfernal");
				}
				if(languageList.getSelectedItem().equals("Aquan")){
					raceDescriptionText.setText("Language:\nAquan\n"
							+ "Typical Speakers:\nWater-based creatures\n"
							+ "Alphabet:\nElven");
				}
				if(languageList.getSelectedItem().equals("Auran")){
					raceDescriptionText.setText("Language:\nAuran\n"
							+ "Typical Speakers:\nAir-based creatures\n"
							+ "Alphabet:\nDraconic");
				}
				if(languageList.getSelectedItem().equals("Celestial")){
					raceDescriptionText.setText("Language:\nCelestial\n"
							+ "Typical Speakers:\nGood Outsiders.\n"
							+ "Alphabet:\nCelestial");
				}
				if(languageList.getSelectedItem().equals("Common")){
					raceDescriptionText.setText("Language:\nCommon\n"
							+ "Typical Speakers:\nHumans, Halflings, Half-elves, Half-orcs\n"
							+ "Alphabet:\nCommon");
				}
				if(languageList.getSelectedItem().equals("Draconic")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nDraconic\n"
							+ "Typical Speakers:\nKobolds, Troglodytes, Lizardfolk, Dragons\n"
							+ "Alphabet:\nDraconic");
				}
				if(languageList.getSelectedItem().equals("Druidic")){
					raceDescriptionText.setText("Language:\nDruidic\n"
							+ "Typical Speakers:\nDruids(only)\n"
							+ "Alphabet:\nDruidic");
				}
				if(languageList.getSelectedItem().equals("Dwarven")){
					raceDescriptionText.setText("Language:\nDwarven\n"
							+ "Typical Speakers:\nDwarves\n"
							+ "Alphabet:\nDwarven");
				}
				if(languageList.getSelectedItem().equals("Elven")){
					raceDescriptionText.setText("Language:\nElven\n"
							+ "Typical Speakers:\nElves\n"
							+ "Alphabet:\nElven");
				}
				if(languageList.getSelectedItem().equals("Giant")){
					raceDescriptionText.setText("Language:\nGiant\n"
							+ "Typical Speakers:\nOgres, Giants\n"
							+ "Alphabet:\nDwarven");
				}
				
				if(languageList.getSelectedItem().equals("Gnome")){
					raceDescriptionText.setText("Language:\nGnome\n"
							+ "Typical Speakers:\nGnomes\n"
							+ "Alphabet:\nDwarven");
				}
				if(languageList.getSelectedItem().equals("Goblin")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nGoblin\n"
							+ "Typical Speakers:\nGoblins, Hobgoblins, Bugbears\n"
							+ "Alphabet:\nDwarven");
				}
				if(languageList.getSelectedItem().equals("Gnoll")){
					raceDescriptionText.setText("Language:\nGnoll\n"
							+ "Typical Speakers:\nGnolls\n"
							+ "Alphabet:\nCommon");
				}
				if(languageList.getSelectedItem().equals("Halfling")){
					raceDescriptionText.setText("Language:\nHalfling\n"
							+ "Typical Speakers:\nHalflings\n"
							+ "Alphabet:\nCommon");
				}
				if(languageList.getSelectedItem().equals("Ignan")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nIgnan\n"
							+ "Typical Speakers:\nFire-based creatures\n"
							+ "Alphabet:\nDraconic");

				}
				if(languageList.getSelectedItem().equals("Infernal")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nInfernal\n"
							+ "Typical Speakers:\nDevils, Lawful Evil Outsiders\n"
							+ "Alphabet:\nInfernal");
				}
				if(languageList.getSelectedItem().equals("Orc")){
					raceDescriptionText.setFont(new Font("Arial", Font.BOLD, 12));
					raceDescriptionText.setText("Language:\nOrc\n"
							+ "Typical Speakers:\nOrcs\n"
							+ "Alphabet:\nDwarven");

				}
				if(languageList.getSelectedItem().equals("Sylvan")){
					raceDescriptionText.setText("Language:\nSylvan\n"
							+ "Typical Speakers:\nDryads, Brownies, Leprechauns\n"
							+ "Alphabet:\nElven");
				}
				if(languageList.getSelectedItem().equals("Terran")){
					raceDescriptionText.setText("Language:\nTerran\n"
							+ "Typical Speakers:\nXorns and other earth-based creatures\n"
							+ "Alphabet:\nDwarven");
				}
				if(languageList.getSelectedItem().equals("Undercommon")){
					raceDescriptionText.setText("Language:\nUndercommon\n"
							+ "Typical Speakers:\nDrow\n"
							+ "Alphabet:\nElven");
				}
				
			}//end mouse entered

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
				raceDescriptionText.setBackground(Color.WHITE);
				raceDescriptionText.setFont(new Font("Arial", Font.PLAIN, 12));
				raceDescriptionText.setForeground(Color.black);
			}
		});
		dieInfo.add(languageList);

		JLabel extraSpace2 = new JLabel("Skills:", JLabel.CENTER); //skill label for race
		extraSpace2.setOpaque(false);
		extraSpace2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the skills of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		dieInfo.add(extraSpace2);

		String[] skillz = new String[skills.size()];
		
		for(int i = 0; i< skills.size(); i++){
			skillz[i] = skills.get(i);
		}

		skillList = new JComboBox(skillz);	//skill choices number 1
		skillList.addActionListener(eh);
		skillList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i < skills.size(); i++){
				if(skillList.getSelectedItem().equals(skills.get(i))){
				raceDescriptionText.setText(skillDescriptions.get(i));
				}
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		dieInfo.add(skillList);
		
		skills.add("None");
		String[] skillz2 = new String[skills.size()];
		
		for(int i = 0; i< skills.size(); i++){
			skillz2[i] = skills.get(i);
		}
		skillDescriptions.add("Second Choice is not required");
												//^
		skillList2 = new JComboBox(skillz2); //skill choice number 2, but not required
		skillList2.addActionListener(eh);
		skillList2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i < skills.size(); i++){
					if(skillList2.getSelectedItem().equals(skills.get(i))){
					raceDescriptionText.setText(skillDescriptions.get(i));
					}
					}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		dieInfo.add(skillList2);
		
		

		JPanel statInfo = new JPanel(); // Stat info panel
		statInfo.setLayout(new GridLayout(0, 2));
		info_panel.add(statInfo);
		statInfo.setOpaque(false);
		
		JLabel primaryStat = new JLabel("Stat Bonus: ", JLabel.CENTER); //Stat Bonus label
		primaryStat.setOpaque(false);
		primaryStat.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the stat bonus of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		statInfo.add(primaryStat);

		JPanel primaryInfo = new JPanel(); // primary stats panel holds all of the choices for a stat bonus
		primaryInfo.setLayout(new BorderLayout());
		primaryInfo.setOpaque(false);
		primaryInfo.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the stat bonus of your race.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		statInfo.add(primaryInfo);

		JPanel boxInfo = new JPanel(); // primary stats choices
		boxInfo.setLayout(new GridLayout(0, 6));
		primaryInfo.add(boxInfo, BorderLayout.CENTER);
		boxInfo.setOpaque(false);
		
		JPanel boxNameInfo = new JPanel();	//primary stats labels
		boxNameInfo.setLayout(new GridLayout(0, 6));
		primaryInfo.add(boxNameInfo, BorderLayout.SOUTH);
		boxNameInfo.setOpaque(false);
		
		stats[0] = "Cha";
		stats[1] = "Con";
		stats[2] = "Dex";
		stats[3] = "Int";
		stats[4] = "Str";
		stats[5] = "Wis";

		statDescriptions[0] = "Charisma measures a character’s force of personality, persuasiveness, personal magnetism, ability to lead,"
				+ "and physical attractiveness. This ability represents actual strength of personality, not merely how one is perceived "
				+ "by others in a social setting. Charisma is most important for paladins, sorcerers, and bards. It is also important for "
				+ "clerics, since it affects their ability to turn undead. Every creature has a Charisma score.";
		statDescriptions[1] = "Constitution represents your character’s health and stamina. A Constitution bonus increases a character’s hit points, "
				+ "so the ability is important for all classes.";
		statDescriptions[2] = "Dexterity measures hand-eye coordination, agility, reflexes, and balance. This ability is the most important one for rogues, "
				+ "but it’s also high on the list for characters who typically wear light or medium armor (rangers and barbarians) or no armor at all "
				+ "(monks, wizards, and sorcerers), and for anyone who wants to be a skilled archer.";
		statDescriptions[3] = "Intelligence determines how well your character learns and reasons. This ability is important for wizards because it affects "
				+ "how many spells they can cast, how hard their spells are to resist, and how powerful their spells can be. It’s also important for any "
				+ "character who wants to have a wide assortment of skills.";
		statDescriptions[4] = "Strength measures your character’s muscle and physical power. This ability is especially important for Fighters, barbarians, "
				+ "paladins, rangers, and monks because it helps them prevail in combat. Strength also limits the amount of equipment your character can carry.";
		statDescriptions[5] = "Wisdom describes a character’s willpower, common sense, perception, and intuition. While Intelligence represents one’s ability to "
				+ "analyze information, Wisdom represents being in tune with and aware of one’s surroundings. Wisdom is the most important ability for "
				+ "clerics and druids, and it is also important for paladins and rangers. If you want your character to have acute senses, "
				+ "put a high score in Wisdom. Every creature has a Wisdom score.";

		JCheckBox s0 = buildStatCheckBox(stats[0]);					//Charisma
		s0.addActionListener(eh);
		s0.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[0]);
			}
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s0);
		primaryStatBoxes.add(s0);
		
		JCheckBox s1 = buildStatCheckBox(stats[1]);					//Constitution
		s1.addActionListener(eh);
		s1.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[1]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s1);
		primaryStatBoxes.add(s1);
		
		JCheckBox s2 = buildStatCheckBox(stats[2]);					//Dexterity
		s2.addActionListener(eh);
		s2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[2]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s2);
		primaryStatBoxes.add(s2);
		
		JCheckBox s3 = buildStatCheckBox(stats[3]);					//Intelligence
		s3.addActionListener(eh);
		s3.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[3]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s3);
		primaryStatBoxes.add(s3);
		
		JCheckBox s4 = buildStatCheckBox(stats[4]);					//Strength
		s4.addActionListener(eh);
		s4.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[4]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s4);
		primaryStatBoxes.add(s4);
		
		JCheckBox s5 = buildStatCheckBox(stats[5]);					//Wisdom
		s5.addActionListener(eh);
		s5.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[5]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxInfo.add(s5);
		primaryStatBoxes.add(s5);

		JLabel sl0 = new JLabel(stats[0]);						//Charisma
		sl0.setOpaque(false);
		sl0.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[0]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl0);

		JLabel sl1 = new JLabel(stats[1]);						//Constitution
		sl1.setOpaque(false);
		sl1.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[1]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl1);

		JLabel sl2 = new JLabel(stats[2]);						//Dexterity
		sl2.setOpaque(false);
		sl2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[2]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl2);
		
		JLabel sl3 = new JLabel(stats[3]);						//Intelligence
		sl3.setOpaque(false);
		sl3.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[3]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl3);
		
		JLabel sl4 = new JLabel(stats[4]);						//Strength
		sl4.setOpaque(false);
		sl4.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[4]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl4);
		
		JLabel sl5 = new JLabel(stats[5]);						//Wisdom
		sl5.setOpaque(false);
		sl5.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText.setText(statDescriptions[5]);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText
						.setText("Fill in your race information.");
			}
		});
		boxNameInfo.add(sl5);
		
		JLabel extraLine2 = new JLabel("Description: ", JLabel.CENTER); //Description label
		extraLine2.setOpaque(false);
		extraLine2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the description of your race.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		info_panel.add(extraLine2);

		JPanel descriptionInfo = new JPanel(); // description panel: label and TextArea
		descriptionInfo.setLayout(new BorderLayout());
		info_panel.add(descriptionInfo);
		descriptionInfo.setOpaque(false);
		descriptionField = new JTextArea();
		descriptionField.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(descriptionField);
		scrollPane.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				raceDescriptionText
						.setText("This location is used for determing the description of your race.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				raceDescriptionText.setText("Fill in your race information.");
			}
		});
		descriptionInfo.add(scrollPane);

		JButton confirmButton = new JButton("Confirm New Race"); //Confirm New Race button
		confirmButton.addActionListener(eh);
		control_panel.add(confirmButton, BorderLayout.SOUTH);

		return control_panel;
	} //end of control panel method

	/**
	 * Description Panel gives descriptions for each of the different attributes that are used to 
	 * create a new race. These include race name, speed, size, 
	 * language, skills, stat bonus and description.
	 * Called in class constructor
	 * @return descriptionPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel descriptionPanel() {
		description_panel = new JPanel();
		description_panel.setSize(400, 400);
		description_panel.setLayout(new BorderLayout());
		description_panel.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Race Creator Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		description_panel.add(panelLabel, BorderLayout.NORTH);

		raceDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(raceDescriptionText);
		raceDescriptionText.setEditable(false);
		description_panel.add(scrollPane, BorderLayout.CENTER);

		JEditorPane classLink = new JEditorPane(); //classLink for D&D Rules
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
	} //end of description panel method

	/**
	 * This method is used for building the Stat Check Boxes.
	 * Called in controlPanel method
	 * @param box (the string that was entered into the method)
	 * @return JCheckBox
	 * @author Victor Mancha
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JCheckBox buildStatCheckBox(final String box) {
		JCheckBox statCheckBox = new JCheckBox();

		statCheckBox.setName(box);	//set the name of the box to the string that was entered
		statCheckBox.setOpaque(false);

		statCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);

		statCheckBox.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				System.out.println(box);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				System.out.println("Default");
			}
		});
		return statCheckBox;
	}//end buildStatCheckBox method

	/**
	 * This method checks to see which of the primary stats have been selected as the bonus.
	 * Called in Event Handler
	 * @author Victor Mancha
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectPrimaryStats() {
		// we are going to find the number of check boxes clicked, if
		// two have been clicked, disable
		// all non clicked check boxes so only at most three can be
		// selected
		numberOfChecked = 0;

		// find all the checked check boxes
		for (JCheckBox skillCheckBox : primaryStatBoxes) {
			if (skillCheckBox.isSelected() == true) {
				numberOfChecked++;
			}// end if

		}// end for loop

		// if that number is three plus the rase skills, disable all non
		// checked check boxes
		if (numberOfChecked == primaryStatLimit) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				for (JCheckBox skillCheckBox : primaryStatBoxes) {
					if (skillCheckBox.isSelected() == false) {
						skillCheckBox.setEnabled(false);
					}
				}// end inner for loop
			}// end outer for loop

			// if the number of checked check boxes is less than 2
		} else if (numberOfChecked <= 1) {
			for (int i = 0; i < primaryStatBoxes.size(); i++) {
				primaryStatBoxes.get(i).setEnabled(true);
			}// end for loop
		}
	}//end of select primary stats

	/**
	 * The Event Handler meets the needs of the GUI when a certain action has been performed.
	 * Called by all ActionListners
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener {

		@Override
		/**
		 * @author Devin Simpson and Victor Mancha
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 */
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			// the checkbox's action listener produces an empty string, so when
			// the action is listener is called with the "" argument, we know a
			// check box has been clicked
			if (command == "") {
				selectPrimaryStats();
			}// end if command == ""

			if (command == "Confirm New Race") {
					
					String abilitySelect = "";
					Boolean clear1 = false;
					Boolean clear2 = false;
					Boolean clear3 = false;
					ArrayList<String> statSelect = new ArrayList<String>();
					
					for(int i = 0; i < primaryStatBoxes.size();i++){
						
						if(primaryStatBoxes.get(i).isSelected() == true){
							if(primaryStatBoxes.get(i).getName().equals("Cha")){
								abilitySelect += "Charisma \t";
								statSelect.add("Charisma");
							}
							if(primaryStatBoxes.get(i).getName().equals("Con")){
								abilitySelect += "Constitution \t";
								statSelect.add("Constitution");
							}
							if(primaryStatBoxes.get(i).getName().equals("Dex")){
								abilitySelect += "Dexterity \t";
								statSelect.add("Dexterity");
							}
							if(primaryStatBoxes.get(i).getName().equals("Int")){
								abilitySelect += "Intelligence \t";
								statSelect.add("Intelligence");
							}
							if(primaryStatBoxes.get(i).getName().equals("Str")){
								abilitySelect += "Strength \t";
								statSelect.add("Strength");
							}
							if(primaryStatBoxes.get(i).getName().equals("Wis")){
								abilitySelect += "Wisdom \t";
								statSelect.add("Wisdom");
							}//end if
						}//end bigger if
					}//end for loop
					
					if(raceNameField.getText().equals("")){
						JOptionPane.showMessageDialog(null, "No Race Name has been entered! You must enter a Race Name before proceeding.", "No Race Name", JOptionPane.ERROR_MESSAGE);
					clear1 = false;
					}else{
						clear1=true;
					}
					if(descriptionField.getText().equals("")){
						JOptionPane.showMessageDialog(null, "No Race Description has been entered! You must enter a Race Description before proceeding.", "No Race Description", JOptionPane.ERROR_MESSAGE);
					clear2 = false;
					}else{
						clear2=true;
					}
					if(skillList.getSelectedItem().equals(skillList2.getSelectedItem())){
						JOptionPane.showMessageDialog(null, "No Two Selected Skills can be the same! You must select different skills before proceeding.", "No Same Skills", JOptionPane.ERROR_MESSAGE);
						clear3 = false;
					}else{
						clear3 = true;
					}
					if (abilitySelect.equals("")){
						JOptionPane.showMessageDialog(null, "No Stat Bonuses have been selected! You must select at least one Stat Bonus before proceeding.", "No Stat Bonus", JOptionPane.ERROR_MESSAGE);
					}else{
					
						int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to use these Stat Bonuses?\n"+ abilitySelect, "Confirm Stat Bonuses", JOptionPane.YES_NO_OPTION);
						
						//if user uses yes option to confirm, run the confirm 
						if (confirm == JOptionPane.YES_OPTION && clear1 && clear2 && clear3) {
						//	ccp.handleAttributeConfirmations(command);
							//setAbilites();
							
							System.out.println("Race Name: " +raceNameField.getText());
							System.out.println("Speed: " + speedList.getSelectedItem());
							System.out.println("Size: " + sizeList.getSelectedItem());
							System.out.println("Language: " + languageList.getSelectedItem());
							System.out.println("Skills: " + skillList.getSelectedItem() + " and " + skillList2.getSelectedItem());
							System.out.println("Stat Bonus(es): " + abilitySelect);
							System.out.println("Description: " + descriptionField.getText());
							
							dm.addRace(raceNameField.getText(),speedList.getSelectedItem().toString(),sizeList.getSelectedItem().toString(),descriptionField.getText(),skillList.getSelectedItem().toString(),skillList2.getSelectedItem().toString(), languageList.getSelectedItem().toString(),statSelect);
							try {
								ccp.setUpRacePanel();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								ccp.swapRacePanel();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							raceCreator.dispose();
						}//end if
					}//end else
					
				}//end command confirm new race
			
		}//end actionPerformed
	}//end event Handler

}//end RaceCreator class
