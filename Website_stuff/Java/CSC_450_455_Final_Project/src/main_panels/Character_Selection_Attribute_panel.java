package main_panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import static javax.swing.ScrollPaneConstants.*;
import AttributeCreators.AbilityCreator;
import AttributeCreators.ClassCreator;
import AttributeCreators.RaceCreator;
import AttributeCreators.SkillCreator;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * Character_Selection_Attribute_panel: This class is used for the creation of
 * all of the character selection attribute panels This class is one of 7
 * classes of the main panels used in the creation of a new D&D character.
 * 
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class Character_Selection_Attribute_panel extends JPanel {

	EventHandler eh = new EventHandler(); // Handles events

	private DnD_Character_Creator mainPanel = null; // DnD_Character_Creator
													// reference

	// create a reference to the attribute creators
	private Database_Manager databaseManager = null;
	private Character_Creation_Panel characterCreationPanel;
	public Image background;

	// create a list of all current characters in the database
	private ArrayList<String> characterList = new ArrayList<String>();

	/**
	 * Constructor for the class Character_Selection_Attribute_panel Called by
	 * DnD_Character_Creator
	 * 
	 * @param m
	 * @param d
	 * @param cList
	 * @param cp
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public Character_Selection_Attribute_panel(DnD_Character_Creator m,
			Database_Manager d, ArrayList<String> cList,
			Character_Creation_Panel cp) throws IOException {

		this.setPreferredSize(new Dimension(900, 600));
		this.setLayout(new BorderLayout());

		// establish the list of characters, the database manager, the dice
		// handler, and the character creation panel reference
		characterList = cList;
		databaseManager = d;
		mainPanel = m;
		characterCreationPanel = cp;

		JPanel characterSelectionPanel = buildCharacterSelectionPanel();

		JPanel attributeCreatorPanel = buildAttributeCreatorPanel();

		this.add(characterSelectionPanel, BorderLayout.CENTER);
		this.add(attributeCreatorPanel, BorderLayout.EAST);

	}// end class constructor

	/**
	 * Builds the character buttons for the attributes Called by
	 * buildCharacterSelectionPanel
	 * 
	 * @param characterName
	 * @param color
	 * @param width
	 * @param heigth
	 * @param offset
	 * @return characterButton
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildCharacterButton(String characterName, Color color,
			int width, int heigth, int offset) {
		JPanel characterButton = new JPanel();
		characterButton.setLayout(new BorderLayout());
		characterButton.setOpaque(false);

		// to make a nice looking layout for the character buttons, place some
		// space on the top, bottom, right, and left of the button
		JPanel northSpace = new JPanel();
		northSpace.setOpaque(false);
		northSpace.setPreferredSize(new Dimension(width, offset));

		JPanel southSpace = new JPanel();
		southSpace.setOpaque(false);
		southSpace.setPreferredSize(new Dimension(width, offset));

		JPanel westSpace = new JPanel();
		westSpace.setOpaque(false);
		westSpace.setPreferredSize(new Dimension(offset, heigth));

		JPanel eastSpace = new JPanel();
		eastSpace.setOpaque(false);
		eastSpace.setPreferredSize(new Dimension(offset, heigth));

		JButton currentCharacter = new JButton(characterName);
		currentCharacter.setPreferredSize(new Dimension(width - offset * 2,
				heigth - offset * 2));
		currentCharacter.addActionListener(eh);

		characterButton.add(westSpace, BorderLayout.WEST);
		characterButton.add(eastSpace, BorderLayout.EAST);
		characterButton.add(southSpace, BorderLayout.SOUTH);
		characterButton.add(northSpace, BorderLayout.NORTH);
		characterButton.add(currentCharacter, BorderLayout.CENTER);

		characterButton.setPreferredSize(new Dimension(width, heigth));

		return characterButton;

	}// end buildCharacterButton method

	/**
	 * Builds the Character Selection Panel Called by class constructor
	 * 
	 * @return characterSelectionPanel
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildCharacterSelectionPanel() throws IOException {

		JPanel characterSelectionPanel = new JPanelWithBackground(
				"fantasy_dragon_art_wallpaper.jpg");
		// characterSelectionPanel.setBackground(Color.blue);
		characterSelectionPanel.setPreferredSize(new Dimension(600,
				(characterList.size() + 1) * 100));
		characterSelectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		// build character buttons
		for (String character : characterList) {
			JPanel currentCharacter = buildCharacterButton(character,
					Color.blue, 600, 100, 20);
			characterSelectionPanel.add(currentCharacter, c);
		}

		JPanel newCharacter = buildCharacterButton("New Character", Color.blue,
				600, 100, 20);
		// add character buttons to panel
		characterSelectionPanel.add(newCharacter, c);
		JScrollPane scrollPane = new JScrollPane(characterSelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullCharacterPanel = new JPanel();
		fullCharacterPanel.setLayout(new BorderLayout());
		fullCharacterPanel.add(scrollPane);

		return fullCharacterPanel;
	}// end buildCharacterSelectionPanel method

	/**
	 * Builds the Attribute Creator Panel Called by class constructor
	 * 
	 * @return attributeCreatorPanel
	 * @throws IOException
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildAttributeCreatorPanel() throws IOException {

		//setup the attribute crator panel to contain buttons that launch the attribute creators in new windows
		JPanel attributeCreatorPanel = new JPanelWithBackground("t_b_i_h.jpg");
		attributeCreatorPanel.setSize(new Dimension(200, 200));
		attributeCreatorPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		
		
		// build character buttons
		JPanel createClass = buildCharacterButton("Create New Class",
				Color.magenta, 200, 100, 20);
		JPanel createRace = buildCharacterButton("Create New Race",
				Color.magenta, 200, 100, 20);
		JPanel createSkill = buildCharacterButton("Create New Skill",
				Color.magenta, 200, 100, 20);
		JPanel createAbility = buildCharacterButton("Create New Ability",
				Color.magenta, 200, 100, 20);

		// add character buttons to panel
		attributeCreatorPanel.add(createClass, c);
		attributeCreatorPanel.add(createRace, c);
		attributeCreatorPanel.add(createSkill, c);
		attributeCreatorPanel.add(createAbility, c);

		JScrollPane scrollPane = new JScrollPane(attributeCreatorPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullattributeCreatorPanel = new JPanel();
		fullattributeCreatorPanel.setLayout(new BorderLayout());
		fullattributeCreatorPanel.add(scrollPane);

		return fullattributeCreatorPanel;
	}// end buildAttributeCreatorPanel

	/**
	 * The Event Handler handles all the different actions that are performed
	 * within the GUI Called by all ActionListeners
	 * 
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

			boolean editCharacter = false;

			// check if user clicked on one of the character buttons (the edit
			// the character)
			for (String character : characterList) {
				if (command == character) {
					editCharacter = true;
				}

			}

			if (editCharacter == true) {

				// if the command is a name of a character, then move to edit
				// the character
				try {
					mainPanel.swapScreens("Edit Character", command);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// if the command is new character, the set up a new character
			} else if (command == "New Character") {

				try {
					mainPanel.swapScreens(command, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// if the command is Create New Class, the build a new class
				// creator
			} else if (command == "Create New Class") {

				try {
					ClassCreator class_creator_window = new ClassCreator(
							databaseManager, characterCreationPanel);
					class_creator_window.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				// if the command is Create New Race, the build a new race
				// creator
			} else if (command == "Create New Race") {

				RaceCreator race_creator_window = null;
				try {
					race_creator_window = new RaceCreator(databaseManager,
							characterCreationPanel);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				race_creator_window.setVisible(true);

				// if the command is Create New Skill, the build a new skill
				// creator
			} else if (command == "Create New Skill") {

				SkillCreator skill_creator_window = null;
				try {
					skill_creator_window = new SkillCreator(
							databaseManager, characterCreationPanel);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				skill_creator_window.setVisible(true);

				// if the command is Create New Ability, the build a new ability
				// creator
			} else if (command == "Create New Ability") {

				AbilityCreator ability_creator_window = null;
				try {
					ability_creator_window = new AbilityCreator(
							databaseManager, characterCreationPanel);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ability_creator_window.setVisible(true);

			}// end else if create new ability
		}// end actionPerformed
	}// end EventHandler

}// end class