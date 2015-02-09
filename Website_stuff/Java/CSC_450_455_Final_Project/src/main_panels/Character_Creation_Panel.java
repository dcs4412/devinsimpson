package main_panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import AttributeCreators.AbilityCreator;
import AttributeCreators.ClassCreator;
import AttributeCreators.RaceCreator;
import AttributeCreators.SkillCreator;
import ControlPanels.ControlPanel_Abilities;
import ControlPanels.ControlPanel_Class;
import ControlPanels.ControlPanel_ConfirmCharacter;
import ControlPanels.ControlPanel_Details;
import ControlPanels.ControlPanel_Equipment;
import ControlPanels.ControlPanel_Race;
import ControlPanels.ControlPanel_Skills;
import ControlPanels.ControlPanel_Stats;
import DescriptionPanels.DescriptionPanel_Abilities;
import DescriptionPanels.DescriptionPanel_Class;
import DescriptionPanels.DescriptionPanel_ConfirmCharacter;
import DescriptionPanels.DescriptionPanel_Details;
import DescriptionPanels.DescriptionPanel_Equipment;
import DescriptionPanels.DescriptionPanel_Race;
import DescriptionPanels.DescriptionPanel_Skills;
import DescriptionPanels.DescriptionPanel_Stats;
import main_panels.Database_Manager;

/**
 * Character_Creation_Panel: This class is used for the creation of all of the
 * character attribute panels This class is one of 7 classes of the main panels
 * used in the creation of a new D&D character.
 * 
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class Character_Creation_Panel extends JPanel {

	EventHandler eh = new EventHandler(); // Handles events

	// array of control panels for each of the character panel buttons
	private JPanel controlPanel_cards = new JPanel(new CardLayout());

	// array of control panels for each of the character panel buttons
	private JPanel descriptionPanel_cards = new JPanel(new CardLayout());

	// reference to character panel buttons
	private ArrayList<JToggleButton> characterButtonsArray = new ArrayList<JToggleButton>();

	// create mode and edit mode have different behaviors associated with
	// creating characters
	// handle these changes with a state variable
	private String mode = "";

	// get reference to parent JFrame
	private DnD_Character_Creator mainPanel = null;

	// get reference to database manager
	private Database_Manager databaseManager = null;

	// get a reference to the control panels so their methods may be called in
	// more than one method
	private ControlPanel_Class controlPanelClass = null;
	private ControlPanel_Race controlPanelRace = null;
	private ControlPanel_Stats controlPanelStats = null;
	private ControlPanel_Details controlPanelDetails = null;
	private ControlPanel_Skills controlPanelSkills = null;
	private ControlPanel_Abilities controlPanelAbilities = null;
	private ControlPanel_Equipment controlPanelEquipment = null;
	private ControlPanel_ConfirmCharacter controlPanelConfirmCharacter = null;

	private Dice diceHandler = null;

	/**
	 * Constructor for the class Character_Creation_Panel Called by
	 * DnD_Character_Creator
	 * 
	 * @param m
	 * @param d
	 * @param di
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public Character_Creation_Panel(DnD_Character_Creator m,
			Database_Manager d, Dice di) throws IOException {

		this.setSize(800, 600);
		this.setLayout(new BorderLayout());

		//set the database ,manager and dice handler
		databaseManager = d;
		diceHandler = di;
		JPanel characterPanel = buildCharacterPanel();

		databaseManager.initalizeCharacterVariables();

		this.add(characterPanel, BorderLayout.WEST);
		this.add(controlPanel_cards, BorderLayout.CENTER);
		this.add(descriptionPanel_cards, BorderLayout.EAST);

		mainPanel = m;

	}// end class constructor

	/**
	 * Sets the mode for the character Called by handleAttributeConfirmations
	 * 
	 * @param m
	 * @param name
	 * @param characterInDatabase
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setMode(String m, String name, boolean characterInDatabase)
			throws IOException {

		mode = m;

		if (mode == "new") {

			databaseManager.initalizeCharacterVariables();

			setUpClassPanel();
			for (JToggleButton characterButton : characterButtonsArray) {

				characterButton.setEnabled(false);

			}

			((CardLayout) controlPanel_cards.getLayout()).show(
					controlPanel_cards, "Control Panel -- Class");
			((CardLayout) descriptionPanel_cards.getLayout()).show(
					descriptionPanel_cards, "Description Panel -- Class");

			for (JToggleButton characterButton : characterButtonsArray) {

				if (characterButton.getText() == "Class") {

					characterButton.setSelected(true);

				} else {
					characterButton.setSelected(false);
				}
			}

		} else if (mode == "edit") {

			if (characterInDatabase == true) {

				databaseManager.setEditCharacter(name);
				setupCharacterEdit(name, characterInDatabase);
			}

			for (JToggleButton characterButton : characterButtonsArray) {
				characterButton.setEnabled(true);
			}

			for (JToggleButton characterButton : characterButtonsArray) {

				if (characterButton.getText() == "Confirm Character") {
					characterButton.setSelected(true);
				} else {
					characterButton.setSelected(false);
				}
			}

			((CardLayout) controlPanel_cards.getLayout()).show(
					controlPanel_cards, "Control Panel -- Confirm Character");
			((CardLayout) descriptionPanel_cards.getLayout()).show(
					descriptionPanel_cards,
					"Description Panel -- Confirm Character");
		}// end else if
	}// end setMode method

	/**
	 * Handles the attribute confirmations Called by DnD_Character_Creator
	 * 
	 * @param command
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void handleAttributeConfirmations(String command) throws IOException {

		// when user confirms the character class
		if (command == "Confirm Class") {

			// if the character is new
			if (mode == "new") {

				// build the race panel as it is the next panel the user will
				// see
				setUpRacePanel();

				// move the user to race panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Race");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards, "Description Panel -- Race");

				// apply the click on the race panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Race") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// if character is being edited (not new)
			} else if (mode == "edit") {

				// the character class determines the abilities the character
				// has access to so build the ability panel
				setUpAbilityPanel();

				// also reflect the current character attributes in the
				// character sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();
			}

			// when user confirms the character race
		} else if (command == "Confirm Race") {

			// if the character is new
			if (mode == "new") {

				// build the stats panel as it is the next panel the user will
				// see
				setUpStatsPanel();

				// move the user to stats panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Stats");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards, "Description Panel -- Stats");

				// apply the click on the stats panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Stats") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// if character is being edited (not new)
			} else if (mode == "edit") {

				// the character race determines the skills the character has
				// access to so build the skill panel
				setUpSkillPanel();

				// also reflect the current character attributes in the
				// character sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();

			}

			// when user confirms the character stats
		} else if (command == "Confirm Stats") {

			// if the character is new
			if (mode == "new") {

				// build the details panel as it is the next panel the user will
				// see
				setUpDetailPanel();

				// move the user to details panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Details");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards, "Description Panel -- Details");

				// apply the click on the details panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Details") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}
			}
			// if character is being edited (not new)
			else if (mode == "edit") {

				// items in the details panel are determined by stats (the
				// saving throws) so rebuild the details panel
				int[] statValues = controlPanelStats.getStats();
				String selectedClass = controlPanelClass.getSelectedClass();

				// regenerate the gold the character starts out with
				String hitDice = databaseManager.getHit_Dice(selectedClass);
				String goldDice = databaseManager.getGold_Dice(selectedClass);

				// apply the changes to the details panel
				controlPanelDetails.reSetSavingThrowsHealthArmorClassAndGold(
						statValues, hitDice, goldDice);

				// also reflect the current character attributes in the
				// character sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();

			}

			// when user confirms the character details
		} else if (command == "Confirm Details") {

			// if the character is new
			if (mode == "new") {

				// build the skills panel as it is the next panel the user will
				// see
				setUpSkillPanel();

				// move the user to skills panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Skills");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards, "Description Panel -- Skills");

				// apply the click on the skills panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Skills") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// if character is being edited (not new)
			} else if (mode == "edit") {

				// reflect the current character attributes in the character
				// sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();

			}

			// when user confirms the character Skills
		} else if (command == "Confirm Skills") {

			// if the character is new
			if (mode == "new") {

				// build the ability panel as it is the next panel the user will
				// see
				setUpAbilityPanel();

				// move the user to ability panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Abilities");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards,
						"Description Panel -- Abilities");

				// apply the click on the ability panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Abilities") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// if character is being edited (not new)
			} else if (mode == "edit") {

				// reflect the current character attributes in the character
				// sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();

			}

			// when user confirms the character abilities
		} else if (command == "Confirm Abilities") {

			// if the character is new
			if (mode == "new") {

				// build the equipment panel as it is the next panel the user
				// will see
				setUpEquipmentPanel();

				// move the user to equipment panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards, "Control Panel -- Equipment");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards,
						"Description Panel -- Equipment");

				// apply the click on the equipment panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Equipment") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// if character is being edited (not new)
			} else if (mode == "edit") {

				// reflect the current character attributes in the character
				// sheet by rebuilding confirm character panel
				setUpConfirmCharacterPanel();

			}

			// when user confirms the character equipment
		} else if (command == "Confirm Equipment") {

			// show all the current character attributes in the character sheet
			// by building the confirm character panel
			setUpConfirmCharacterPanel();

			// if the character is new
			if (mode == "new") {

				// move the user to equipment panel
				((CardLayout) controlPanel_cards.getLayout()).show(
						controlPanel_cards,
						"Control Panel -- Confirm Character");
				((CardLayout) descriptionPanel_cards.getLayout()).show(
						descriptionPanel_cards,
						"Description Panel -- Confirm Character");

				// apply the click on the equipment panel button (in navigation)
				for (JToggleButton characterButton : characterButtonsArray) {

					if (characterButton.getText() == "Confirm Character") {

						characterButton.setSelected(true);

					} else {
						characterButton.setSelected(false);
					}
				}

				// change the mode to "edit" as at any point the user confirms
				// their equipment, they will be sent to edit mode, use the
				// current character name to get character specific attributes
				String name = controlPanelDetails.getCharacterName();

				setMode("edit", name, false);

			}// end if mode new

		}// end else if confirm equipment

	}// end handleAttribute Confirmations

	/**
	 * Sets up the character to be edited Called by setMode
	 * 
	 * @param characterName
	 * @param characterInDatabase
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void setupCharacterEdit(String characterName,
			boolean characterInDatabase) throws IOException {
		if (characterInDatabase == true) {

			System.out.println("Character name here is " + characterName);

			// query the database manager for the character specific attributes
			String className = databaseManager.getCharacter_class_name(characterName);

			String raceName = databaseManager.getCharacter_race_name(characterName);

			ArrayList<Integer> statValues = databaseManager
					.getCharacter_stats(characterName);

			ArrayList<String> textInputDetails = databaseManager
					.getCharacter_details(characterName);
			ArrayList<String> comboInputDetails = databaseManager
					.getCharacter_levelGenderAlignment(characterName);

			ArrayList<String> playerSkills = databaseManager
					.getCharacter_skills(characterName);

			ArrayList<String> playerAbilites = databaseManager
					.getCharacter_ability(characterName);

			ArrayList<String> playerWeapons = databaseManager
					.getCharacter_weapon(characterName);
			ArrayList<String> playerAromor = databaseManager
					.getCharacter_armor(characterName);
			ArrayList<String> playerGear = databaseManager.getCharacter_gear(characterName);

			// set up the class panel with the player's selected class
			setUpClassPanel();
			controlPanelClass.setClassFromPlayer(className);

			// set up the race panel with the player's selected race
			setUpRacePanel();
			controlPanelRace.setRaceFromPlayer(raceName);

			// set up the stats panel with the player's generated stats
			setUpStatsPanel();
			controlPanelStats.setStatsFromPlayer(statValues);

			// set up the details panel with the player's details
			setUpDetailPanel();
			controlPanelDetails.setDetailsFromPlayer(textInputDetails,
					comboInputDetails);

			// set up the skill panel with the player's selected skills
			setUpSkillPanel();
			controlPanelSkills.setSkillsFromPlayer(playerSkills);

			// set up the ability panel with the player's selected abilities
			setUpAbilityPanel();
			controlPanelAbilities.setAbilitesFromPlayer(playerAbilites);

			// set up the equipment panel with the player's purchased equipment
			setUpEquipmentPanel();
			controlPanelEquipment.setEquipmentFromPlayer(playerWeapons,
					playerAromor, playerGear);

			// reflect the current character attributes in the character sheet
			setUpConfirmCharacterPanel();

		}// end if
	}// end setupCharacterEdit

	/**
	 * @author Devin Simpson Construct the character option panel here, this
	 *         panel does not change
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public JPanel buildCharacterPanel() throws IOException {

		// build the character attribute navigation panel with a specific
		// background
		JPanel characterPanel = new JPanelWithBackground("brick_background.jpg");

		characterPanel.setPreferredSize(new Dimension(200, 600));
		characterPanel.setLayout(new BorderLayout());

		JPanel characterButtons = characterButtons();

		// to get a nice looking button layout, add space to the top, bottom,
		// left and right of the button panel
		JPanel westSpace = new JPanel();
		westSpace.setOpaque(false);
		westSpace.setPreferredSize(new Dimension(30, 600));

		JPanel eastSpace = new JPanel();
		eastSpace.setOpaque(false);
		eastSpace.setPreferredSize(new Dimension(30, 600));

		JPanel southSpace = new JPanel();
		southSpace.setOpaque(false);
		southSpace.setPreferredSize(new Dimension(200, 50));

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(100, 20));

		// apply the space panels to the overall button navigation panel and add
		// the button panel in the center
		characterPanel.add(westSpace, BorderLayout.WEST);
		characterPanel.add(eastSpace, BorderLayout.EAST);
		characterPanel.add(southSpace, BorderLayout.SOUTH);
		characterPanel.add(cancelButton, BorderLayout.NORTH);
		characterPanel.add(characterButtons, BorderLayout.CENTER);

		return characterPanel;
	}

	/**
	 * Builds Character buttons (all 8 of them). (Abilities, Class,
	 * ConfirmCharacter, Details, Equipment, Race, Skills, and Stats) Called by
	 * buildCharacterPanel
	 * 
	 * @return characterButtons
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private JPanel characterButtons() throws IOException {

		// set up a panel to hold the buttons
		JPanel characterButtons = new JPanel();
		characterButtons.setPreferredSize(new Dimension(100, 500));
		characterButtons.setLayout(new GridLayout(8, 1));

		// display the background image from the parent panel
		characterButtons.setOpaque(false);

		// set up the image for the button
		BufferedImage buttonIcon = ImageIO.read(new File(
				"./Images/button_stone_2.jpg"));

		// build character buttons
		JToggleButton classButton = new JToggleButton(new ImageIcon(buttonIcon));
		JToggleButton raceButton = new JToggleButton(new ImageIcon(buttonIcon));
		JToggleButton statsButton = new JToggleButton(new ImageIcon(buttonIcon));
		JToggleButton detailsButton = new JToggleButton(new ImageIcon(
				buttonIcon));
		JToggleButton skillsButton = new JToggleButton(
				new ImageIcon(buttonIcon));
		JToggleButton abilitiesButton = new JToggleButton(new ImageIcon(
				buttonIcon));
		JToggleButton equipmentButton = new JToggleButton(new ImageIcon(
				buttonIcon));
		JToggleButton confirmButton = new JToggleButton(new ImageIcon(
				buttonIcon));

		// apply a black border to the buttons
		classButton.setBorder(BorderFactory.createLineBorder(Color.black));
		raceButton.setBorder(BorderFactory.createLineBorder(Color.black));
		statsButton.setBorder(BorderFactory.createLineBorder(Color.black));
		detailsButton.setBorder(BorderFactory.createLineBorder(Color.black));
		skillsButton.setBorder(BorderFactory.createLineBorder(Color.black));
		abilitiesButton.setBorder(BorderFactory.createLineBorder(Color.black));
		equipmentButton.setBorder(BorderFactory.createLineBorder(Color.black));
		confirmButton.setBorder(BorderFactory.createLineBorder(Color.black));

		// set the text of the buttons
		classButton.setText("Class");
		raceButton.setText("Race");
		statsButton.setText("Stats");
		detailsButton.setText("Details");
		skillsButton.setText("Skills");
		abilitiesButton.setText("Abilities");
		equipmentButton.setText("Equipment");
		confirmButton.setText("Confirm Character");

		// move the button image to the center of the button space (horizontal)
		classButton.setHorizontalTextPosition(JToggleButton.CENTER);
		raceButton.setHorizontalTextPosition(JToggleButton.CENTER);
		statsButton.setHorizontalTextPosition(JToggleButton.CENTER);
		detailsButton.setHorizontalTextPosition(JToggleButton.CENTER);
		skillsButton.setHorizontalTextPosition(JToggleButton.CENTER);
		abilitiesButton.setHorizontalTextPosition(JToggleButton.CENTER);
		equipmentButton.setHorizontalTextPosition(JToggleButton.CENTER);
		confirmButton.setHorizontalTextPosition(JToggleButton.CENTER);

		// move the button image to the center of the button space (vertical)
		classButton.setVerticalTextPosition(JToggleButton.CENTER);
		raceButton.setVerticalTextPosition(JToggleButton.CENTER);
		statsButton.setVerticalTextPosition(JToggleButton.CENTER);
		detailsButton.setVerticalTextPosition(JToggleButton.CENTER);
		skillsButton.setVerticalTextPosition(JToggleButton.CENTER);
		abilitiesButton.setVerticalTextPosition(JToggleButton.CENTER);
		equipmentButton.setVerticalTextPosition(JToggleButton.CENTER);
		confirmButton.setVerticalTextPosition(JToggleButton.CENTER);

		// set the color of the button label to be black
		classButton.setForeground(Color.black);
		raceButton.setForeground(Color.black);
		statsButton.setForeground(Color.black);
		detailsButton.setForeground(Color.black);
		skillsButton.setForeground(Color.black);
		abilitiesButton.setForeground(Color.black);
		equipmentButton.setForeground(Color.black);
		confirmButton.setForeground(Color.black);

		// add the action listener to the character buttons
		classButton.addActionListener(eh);
		raceButton.addActionListener(eh);
		statsButton.addActionListener(eh);
		detailsButton.addActionListener(eh);
		skillsButton.addActionListener(eh);
		abilitiesButton.addActionListener(eh);
		equipmentButton.addActionListener(eh);
		confirmButton.addActionListener(eh);

		// add character buttons to panel
		characterButtons.add(classButton);
		characterButtons.add(raceButton);
		characterButtons.add(statsButton);
		characterButtons.add(detailsButton);
		characterButtons.add(skillsButton);
		characterButtons.add(abilitiesButton);
		characterButtons.add(equipmentButton);
		characterButtons.add(confirmButton);

		// add character buttons to reference array
		characterButtonsArray.add(classButton);
		characterButtonsArray.add(raceButton);
		characterButtonsArray.add(statsButton);
		characterButtonsArray.add(detailsButton);
		characterButtonsArray.add(skillsButton);
		characterButtonsArray.add(abilitiesButton);
		characterButtonsArray.add(equipmentButton);
		characterButtonsArray.add(confirmButton);

		return characterButtons;

	}// end characterButtons method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the race control and description panels to reflect all the
	 *         current information on the character specific race
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setUpRacePanel() throws IOException {

		// get the list of all races
		ArrayList<String> currentRaceList = databaseManager.getRace_name();

		// Construct the control panel and add it to the control panel cards
		controlPanelRace = new ControlPanel_Race(400, 600, this, databaseManager,
				currentRaceList);
		controlPanel_cards.add(controlPanelRace, "Control Panel -- Race");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Race dp_r = new DescriptionPanel_Race(400, 600, this,
				databaseManager, currentRaceList);
		descriptionPanel_cards.add(dp_r, "Description Panel -- Race");

	}// end setUpRacePanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the stats control and description panels to reflect all the
	 *         current information on the character specific stats
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void setUpStatsPanel() throws IOException {

		// get the character's current race and get the race specific stat
		// bonuses
		String selectedCharacterRace = controlPanelRace.getSelectedRace();
		String raceStatBonus = databaseManager.getRace_statBonus(selectedCharacterRace);

		// move through the list of race and stat bonuses, separating the name
		// and the value
		List<String> combinedNameAndValue = Arrays.asList(raceStatBonus
				.split("\n"));
		ArrayList<String> statNames = new ArrayList<String>();
		ArrayList<Integer> statBonus = new ArrayList<Integer>();
		for (String item : combinedNameAndValue) {

			// each stat label should have the bonus reflected in the stat label
			List<String> temp = Arrays.asList(item.split(" +"));
			statNames.add(temp.get(0));
			statBonus.add(Integer.parseInt(temp.get(1)));

		}

		// get the list of stats
		ArrayList<String> currentStatList = databaseManager.getStat_name();

		// Construct the control panel and add it to the control panel cards
		controlPanelStats = new ControlPanel_Stats(400, 600, this,
				currentStatList, statNames, statBonus, diceHandler, databaseManager);
		controlPanel_cards.add(controlPanelStats, "Control Panel -- Stats");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Stats dp_st = new DescriptionPanel_Stats(400, 600, databaseManager,
				currentStatList);
		descriptionPanel_cards.add(dp_st, "Description Panel -- Stats");

	}// end setUpStatsPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the detail control and description panels to reflect all
	 *         the current information on the character specific details
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void setUpDetailPanel() throws IOException {

		// get the characters current stats
		int[] statValues = controlPanelStats.getStats();

		// get the character's current class
		String selectedClass = controlPanelClass.getSelectedClass();

		// get the character's hit dice and gold dice based off their class
		String hitDice = databaseManager.getHit_Dice(selectedClass);
		String goldDice = databaseManager.getGold_Dice(selectedClass);

		// Construct the control panel and add it to the control panel cards
		controlPanelDetails = new ControlPanel_Details(400, 600, this,
				statValues, hitDice, goldDice, diceHandler, databaseManager, mode);
		controlPanel_cards.add(controlPanelDetails, "Control Panel -- Details");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Details dp_d = new DescriptionPanel_Details(400, 600);
		descriptionPanel_cards.add(dp_d, "Description Panel -- Details");

	}// end setUpDetailPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the skill control and description panels to reflect all the
	 *         current information on the character's skills
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setUpSkillPanel() throws IOException {

		// get the list of all skills and the race specific skills the character
		// can use
		ArrayList<String> currentSkillList = databaseManager.getSkill_name();
		ArrayList<String> raceSkills = databaseManager.getRace_skills(controlPanelRace
				.getSelectedRace());

		// Construct the control panel and add it to the control panel cards
		controlPanelSkills = new ControlPanel_Skills(400, 600, this, databaseManager,
				currentSkillList, raceSkills);
		controlPanel_cards.add(controlPanelSkills, "Control Panel -- Skills");

		SkillCreator skill_creator_window = new SkillCreator(databaseManager, this);

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Skills dp_sk = new DescriptionPanel_Skills(400, 600,
				skill_creator_window, databaseManager, currentSkillList, this);
		descriptionPanel_cards.add(dp_sk, "Description Panel -- Skills");

	}// end setUpSkillPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the ability control and description panels to reflect all
	 *         the current information on the character's abilities
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setUpAbilityPanel() throws IOException {

		// get the list of abilities the character can use based on their class
		ArrayList<String> currentAbilityList = databaseManager
				.getClass_ability(controlPanelClass.getSelectedClass());

		// Construct the control panel and add it to the control panel cards
		controlPanelAbilities = new ControlPanel_Abilities(400, 600, this, databaseManager,
				currentAbilityList);
		controlPanel_cards.add(controlPanelAbilities,
				"Control Panel -- Abilities");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Abilities dp_a = new DescriptionPanel_Abilities(400,
				600, this, databaseManager, currentAbilityList);
		descriptionPanel_cards.add(dp_a, "Description Panel -- Abilities");

	}// end setUpAbilityPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the equipment control and description panels to reflect all
	 *         the current information on the character's equipment
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void setUpEquipmentPanel() throws IOException {

		// get the list of equipment for purchase
		ArrayList<String> currentWeaponList = databaseManager.getWeapon_Name();
		ArrayList<String> currentArmorList = databaseManager.getArmor_Name();
		ArrayList<String> currentGearList = databaseManager.getGear_Name();

		// Construct the control panel and add it to the control panel cards
		controlPanelEquipment = new ControlPanel_Equipment(400, 600, this, databaseManager,
				currentWeaponList, currentArmorList, currentGearList,
				controlPanelDetails.getGoldSilverCopper());
		controlPanel_cards.add(controlPanelEquipment,
				"Control Panel -- Equipment");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_Equipment dp_e = new DescriptionPanel_Equipment(400,
				600, databaseManager, currentWeaponList, currentArmorList, currentGearList,
				this);
		descriptionPanel_cards.add(dp_e, "Description Panel -- Equipment");

	}// end setUpEquipmentPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         build the confirm character control and description panels to
	 *         reflect all the current information on the character
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void setUpConfirmCharacterPanel() throws IOException {

		// get the character name so the following data can be collected
		String characterName = controlPanelDetails.getCharacterName();

		// the character's class and race
		String className = databaseManager.getCharacter_class_name(characterName);
		String raceName = databaseManager.getCharacter_race_name(characterName);

		// the character's stats
		ArrayList<Integer> statValues = databaseManager.getCharacter_stats(characterName);

		// the character specific detail
		ArrayList<String> textInputDetails = databaseManager
				.getCharacter_details(characterName);
		ArrayList<String> levelAndGender = databaseManager
				.getCharacter_levelGenderAlignment(characterName);
		ArrayList<String> detailLabels = databaseManager.getDetailLabels();

		// the character's skills and abilities
		ArrayList<String> playerSkills = databaseManager.getCharacter_skills(characterName);
		ArrayList<String> playerAbilites = databaseManager
				.getCharacter_ability(characterName);

		System.out.println("SDFADFSDF " + playerAbilites);

		// the character's equipment
		ArrayList<String> playerWeapons = databaseManager.getCharacter_weapon(characterName);
		ArrayList<String> playerArmor = databaseManager.getCharacter_armor(characterName);
		ArrayList<String> playerGear = databaseManager.getCharacter_gear(characterName);

		// Construct the control panel and add it to the control panel cards
		controlPanelConfirmCharacter = new ControlPanel_ConfirmCharacter(400,
				600, this, databaseManager, characterName, className, raceName, statValues,
				textInputDetails, detailLabels, levelAndGender, playerSkills,
				playerAbilites, playerWeapons, playerArmor, playerGear);

		controlPanel_cards.add(controlPanelConfirmCharacter,
				"Control Panel -- Confirm Character");

		// Construct the description panel and add it to the control panel cards
		DescriptionPanel_ConfirmCharacter dp_cc = new DescriptionPanel_ConfirmCharacter(
				400, 600, this);

		descriptionPanel_cards.add(dp_cc,
				"Description Panel -- Confirm Character");

	}// end setUpConfirmCharacterPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         set up the class control and description panels
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setUpClassPanel() throws IOException {

		ArrayList<String> currentClassList = databaseManager.getClass_name();

		// build each control panel using its own class
		controlPanelClass = new ControlPanel_Class(400, 600, this,
				currentClassList, databaseManager);

		// now add each control panel to the control card layout
		controlPanel_cards.add(controlPanelClass, "Control Panel -- Class");

		// build each description panel using its own class
		DescriptionPanel_Class dp_c = new DescriptionPanel_Class(400, 600,
				this, databaseManager, currentClassList);

		// now add each description panel to the description card layout
		descriptionPanel_cards.add(dp_c, "Description Panel -- Class");

	}// end setUpClassPanel method

	/**
	 * @author Devin Simpson
	 * 
	 *         This is the time when a user has signaled that they have
	 *         finishing creating/editing their character That operation to
	 *         confirm the character must occur here, then the view is switched
	 * @param command
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void confirmingCharacter(String command) throws IOException {

		mainPanel.swapScreens(command, null);

	}// end confirmingCharacter method

	/**
	 * @author Thomas Man
	 * 
	 *         switch out the class control and description panel to reflect the
	 *         new class added
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void swapClassPanel() throws IOException {

		((CardLayout) controlPanel_cards.getLayout()).show(controlPanel_cards,
				"Control Panel -- Class");
		((CardLayout) descriptionPanel_cards.getLayout()).show(
				descriptionPanel_cards, "Description Panel -- Class");

	}// end swapClassPanel method

	/**
	 * @author Thomas Man
	 * 
	 *         switch out the race control and description panel to reflect the
	 *         new race added
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void swapRacePanel() throws IOException {

		((CardLayout) controlPanel_cards.getLayout()).show(controlPanel_cards,
				"Control Panel -- Race");
		((CardLayout) descriptionPanel_cards.getLayout()).show(
				descriptionPanel_cards, "Description Panel -- Race");

	}// end swapRacePanel method

	/**
	 * @author Thomas Man
	 * 
	 *         switch out the ability control and description panel to reflect
	 *         the new ability added
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void swapAbilityPanel() throws IOException {

		((CardLayout) controlPanel_cards.getLayout()).show(controlPanel_cards,
				"Control Panel -- Abilities");
		((CardLayout) descriptionPanel_cards.getLayout()).show(
				descriptionPanel_cards, "Description Panel -- Abilities");

	}// end swapAbilityPanel method

	/**
	 * @author Thomas Man
	 * 
	 *         switch out the skill control and description panel to reflect the
	 *         new skill added
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void swapSkillPanel() throws IOException {

		((CardLayout) controlPanel_cards.getLayout()).show(controlPanel_cards,
				"Control Panel -- Skills");
		((CardLayout) descriptionPanel_cards.getLayout()).show(
				descriptionPanel_cards, "Description Panel -- Skills");

	}// end swapSkillPanel method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the current stat description to the item of current user
	 *         interest
	 * 
	 * @param stat_name
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void statPanel_switch(String stat_name) {

		DescriptionPanel_Stats.setStat_description(stat_name);

	}// end statPanel_switch method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the current ability description to the item of current
	 *         user interest
	 * 
	 * @param ability_name
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void abilityPanel_switch(String ability_name) {

		DescriptionPanel_Abilities.setAbility_description(ability_name);

	}// end abilityPanel_switch method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the skill equipment description to the item of current
	 *         user interest
	 * 
	 * @param skill_name
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void skillPanel_switch(String skill_name) {

		DescriptionPanel_Skills.setSkill_description(skill_name);

	}// end skillPanel_switch method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the current class description to the item of current user
	 *         interest
	 * 
	 * @param class_name
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void classPanel_switch(String class_name) {

		DescriptionPanel_Class.setClass_description(class_name);

	}// end classPanel_switch method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the current race description to the item of current user
	 *         interest
	 * 
	 * @param race_name
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void racePanel_switch(String race_name) {

		DescriptionPanel_Race.setRace_description(race_name);

	}// end racePanel_switch method

	/**
	 * @author Thomas Man
	 * 
	 *         switch the current equipment description to the item of current
	 *         user interest
	 * 
	 * @param equipment_name
	 * @param equipment_type
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void equipmentPanel_switch(String equipment_name,
			String equipment_type) {

		DescriptionPanel_Equipment.setEquipment_description(equipment_name,
				equipment_type);

	}// end equipmentPanel_switch method

	/**
	 * 
	 * @author Devin Simpson
	 * 
	 *         set up the store by sending the items and their prices to the
	 *         equipment description panel
	 * 
	 * @param weaponName
	 * @param weaponPrice
	 * @param armorName
	 * @param armorPrice
	 * @param gearName
	 * @param gearPrice
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setEquipmentPricesInStore(ArrayList<String> weaponName,
			ArrayList<int[]> weaponPrice, ArrayList<String> armorName,
			ArrayList<int[]> armorPrice, ArrayList<String> gearName,
			ArrayList<int[]> gearPrice) {

		controlPanelEquipment.setItemPrices(weaponName, weaponPrice, armorName,
				armorPrice, gearName, gearPrice);

	}// end setEquipmentPricesInStore method

	/**
	 * 
	 * @author Devin Simpson
	 * 
	 *         Update the current store receipt with the current state of the
	 *         character's wallet as well as the items the character has bought
	 * 
	 * @param characterWallet
	 * @param boughtEquipmentNames
	 * @param boughtEquipmentPrices
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void setCurrentStoreRecipt(int[] characterWallet,
			ArrayList<String> boughtEquipmentNames,
			ArrayList<String> boughtEquipmentPrices) {

		DescriptionPanel_Equipment.setStoreReceiptText(characterWallet,
				boughtEquipmentNames, boughtEquipmentPrices);

	}// end setCurrentStoreRecipt method

	/**
	 * @author Devin Simpson
	 * 
	 *         send the character sheet to the confirm character description
	 *         panel so that it may be printed
	 * @return the character sheet
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public CharacterSheetPrintPreview getCharacterSheetGraphics() {

		return controlPanelConfirmCharacter.getCharacterSheet();

	}// end getCharacterSheetGraphics method

	/**
	 * @author Devin Simpson The event handler class will handle button press
	 *         and text field events for the DnD Character Creator
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener {

		@Override
		/**
		 * @author Devin Simpson
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String command = e.getActionCommand();

			for (int i = 0; i < characterButtonsArray.size(); i++) {

				// search the array of buttons, to find the clicked button, move
				// to the correct page when the
				// correct button has been found
				if (characterButtonsArray.get(i).getText().equals(command)) {

					characterButtonsArray.get(i).setSelected(true);

					((CardLayout) controlPanel_cards.getLayout()).show(
							controlPanel_cards, "Control Panel -- " + command);
					((CardLayout) descriptionPanel_cards.getLayout()).show(
							descriptionPanel_cards, "Description Panel -- "
									+ command);

				} else {

					characterButtonsArray.get(i).setSelected(false);
				}// end else

			}// end for loop

		}// end actionPerformed method

	}// end EventHandler class

}// end Character_Creation_Panel class