package ControlPanels;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.Dice;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Stats:
 * This class is used for the creation of the panel that shows the stats
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Stats extends JPanel {

	private int width, height; //width and height of the screen for the panel

	EventHandler eh = new EventHandler(); //Handles the events

	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	private ArrayList<String> statList; //list of stats for the user
	private Database_Manager dm = null; //Database Manager reference

	// use a variable to track whether or not stats have been generated
	private boolean hasStats = false;

	// reference to class panel buttons
	private ArrayList<JTextField> statValueList = new ArrayList<JTextField>();

	// keep a reference to the ability modifiers and the mode (new or edit)
	private ArrayList<String> AbilityModifers;
	private ArrayList<Integer> modificationValues;
	private Dice statDice = null;
	private int[] stats = new int[6];

	/**
	 * Constructor for the ControlPanel_Stats class
	 * Called by the Character_Creation_Panel
	 * @param w (width)
	 * @param h (height)
	 * @param c (Character Creation Panel reference)
	 * @param sn (Stat List)
	 * @param aM (Ability Modifiers)
	 * @param mV (Modification Values)
	 * @param di (stat dice)
	 * @param d (Database Manager)
	 * @throws IOException
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Stats(int w, int h, Character_Creation_Panel c,
			ArrayList<String> sn, ArrayList<String> aM, ArrayList<Integer> mV,
			Dice di, Database_Manager d) throws IOException {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		JLabel panelLabel = new JLabel("Stats Control Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel, BorderLayout.NORTH);

		AbilityModifers = aM;
		modificationValues = mV;
		statDice = di;
		dm = d;
		ccp = c;
		statList = sn;

		stats = new int[statList.size()];

		JPanel statFeilds = buildStatGenerationPanel();
		this.add(statFeilds);
		JButton confirm = new JButton("Confirm Stats");
		confirm.addActionListener(eh);

		this.add(confirm, BorderLayout.SOUTH);
	}//end ControlPanel_Stats constructor

	/**
	 * Builds the field that holds all the stats
	 * Called by buildStatGenerationPanel
	 * @param statName
	 * @param color
	 * @param width
	 * @param heigth
	 * @param offset
	 * @return statField
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildStatField(String statName, Color color, int width,
			int heigth, int offset) {
		JPanel statField = new JPanel();
		statField.setLayout(new BorderLayout());
		statField.setOpaque(false);

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

		JPanel statPanel = new JPanel();
		statPanel.setLayout(new GridLayout(1, 3));
		statPanel
				.setMinimumSize(new Dimension(width - offset, heigth - offset));
		statPanel.setOpaque(false);

		// if the current stat is modified by the characters race, reflect this
		// in the label
		int modifierIndex = -1;

		for (int i = 0; i < AbilityModifers.size(); i++) {
			if (AbilityModifers.get(i).equals(statName)) {
				modifierIndex = i;
			}
		}

		if (modifierIndex > -1) {

			statName += " + " + modificationValues.get(modifierIndex);

		}

		final JLabel currentStatLabel = new JLabel(statName, JLabel.CENTER);
		currentStatLabel.setOpaque(false);
		currentStatLabel.setBackground(Color.white);

		JPanel centerSpace = new JPanel();
		centerSpace.setOpaque(false);

		JTextField statValue = new JTextField();
		statValue.setEditable(false);

		statPanel.add(currentStatLabel);
		statPanel.add(centerSpace);
		statPanel.add(statValue);

		// create a mouse listener to look for the mouse entering and exiting
		// the label
		// if the mouse enters the label area, the description switched the
		// description to that
		// of the specific stat the mouse is over, when the mouse leaves the
		// label area, the
		// description is switched back to the default description of all stats.
		currentStatLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				ccp.statPanel_switch(currentStatLabel.getText());
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				// change the description panel to that of the default stats
				// description.
				ccp.statPanel_switch("default");

			}
		});

		statField.add(westSpace, BorderLayout.WEST);
		statField.add(eastSpace, BorderLayout.EAST);
		statField.add(southSpace, BorderLayout.SOUTH);
		statField.add(northSpace, BorderLayout.NORTH);
		statField.add(statPanel, BorderLayout.CENTER);

		statValueList.add(statValue);

		statField.setPreferredSize(new Dimension(width, heigth));

		return statField;

	}//end buildStatField

	/**
	 * Get the stats the user has selected
	 * Called by the Database Manager
	 * @return stats
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public int[] getStats() {

		return stats;

	}//end getStats method

	/**
	 * Get the Stat Values that the user has selected
	 * Called by the Database Manager
	 * @return statValues
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<Integer> getStatValues() {

		ArrayList<Integer> statValues = new ArrayList<Integer>();

		for (int i = 0; i < statValueList.size(); i++) {
			statValues.add(Integer.parseInt(statValueList.get(i).getText()));
		}
		return statValues;
	}//end getStatValues method

	/**
	 * Sets the stats the user chose to the database
	 * Called by the Database Manager
	 * @param playerStats
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setStatsFromPlayer(ArrayList<Integer> playerStats) {

		for (int i = 0; i < statValueList.size(); i++) {
			statValueList.get(i).setText(Integer.toString(playerStats.get(i)));
		}
	}//end setStatsFromPlayer method

	/**
	 * Builds the panel for Stat Generation
	 * Called by the class constructor
	 * @return statGenerationPanel
	 * @throws IOException
	 */
	private JPanel buildStatGenerationPanel() throws IOException {

		// set up the panel to hold all the stat labels and text fields as well
		// as the "generate stats" button
		JPanel statGenerationPanel = new JPanelWithBackground("t_b_i_h.jpg");
		// statGenerationPanel.setBackground(Color.red);
		statGenerationPanel.setPreferredSize(new Dimension(400, (statList
				.size() + 1) * 50));
		statGenerationPanel.setLayout(new GridBagLayout());

		// set some constants for displaying the stat labels and text fields
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;

		// build stat labels and text fields
		for (String stats : statList) {
			JPanel currentStat = buildStatField(stats, Color.red, 300, 50, 10);
			statGenerationPanel.add(currentStat, c);
		}

		// build the panel to hold the "generate stats" button
		JPanel generateButtonPanel = new JPanel();
		generateButtonPanel.setOpaque(false);
		generateButtonPanel.setLayout(new BorderLayout());

		// we require a button to be placed in the panel, so we cannot use the
		// buildStatField method
		JPanel northSpace = new JPanel();
		northSpace.setOpaque(false);
		northSpace.setPreferredSize(new Dimension(150, 10));
		JPanel southSpace = new JPanel();
		southSpace.setOpaque(false);
		southSpace.setPreferredSize(new Dimension(150, 10));
		JPanel westSpace = new JPanel();
		westSpace.setOpaque(false);
		westSpace.setPreferredSize(new Dimension(10, 50));
		JPanel eastSpace = new JPanel();
		eastSpace.setOpaque(false);
		eastSpace.setPreferredSize(new Dimension(10, 50));

		// the "generate stats" button
		JButton generateButton = new JButton("Generate Stats");
		generateButton.addActionListener(eh);

		// build the panel with the padding and button
		generateButtonPanel.add(westSpace, BorderLayout.WEST);
		generateButtonPanel.add(eastSpace, BorderLayout.EAST);
		generateButtonPanel.add(southSpace, BorderLayout.SOUTH);
		generateButtonPanel.add(northSpace, BorderLayout.NORTH);
		generateButtonPanel.add(generateButton, BorderLayout.CENTER);

		// add the button to the end of the panel
		statGenerationPanel.add(generateButtonPanel, c);

		return statGenerationPanel;

	}//end buildStatGenerationPanel

	/**
	 * @author Devin Simpson
	 * 
	 *         This method is responsible for generating the character stats by
	 *         the 4D6 Best of Three procedure
	 *  		Called by the Event Handler
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private int generateStat(String statName) {

		int[] fourDice = statDice.roll("4d6");

		// find the highest three of the four dice (highest are at the end)
		Arrays.sort(fourDice);

		// if the current stat is modified by the characters race, reflect add
		// the bonus to the returned stat
		int modifierIndex = -1;
		int modifyValue = 0;

		for (int i = 0; i < AbilityModifers.size(); i++) {
			if (AbilityModifers.get(i).equals(statName)) {
				modifierIndex = i;
			}
		}

		if (modifierIndex > -1) {

			modifyValue = modificationValues.get(modifierIndex);

		}

		// return the sum of the highest three values as the value of the stat
		return fourDice[1] + fourDice[2] + fourDice[3] + modifyValue;

	}//end generateStat method

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

			if (command == "Confirm Stats") {

				// if user ahs not generated any stats, put up an error message
				// dialog to inform them to generate stats
				if (hasStats == false) {

					JOptionPane
							.showMessageDialog(
									null,
									"No stats have been generated! you must generate stats before proceeding",
									"no stats", JOptionPane.ERROR_MESSAGE);

				}
				// if user has stats, print out all the stats on the confirm
				// dialog so the user can review them
				else if (hasStats == true) {

					// set up varaibles for the stat values
					String CharismaValue = "";
					String ConstitutionValue = "";
					String DexterityValue = "";
					String IntelligenceValue = "";
					String StrengthValue = "";
					String WisdomValue = "";

					// assign the values a number if the correct stat is found
					for (int i = 0; i < statList.size(); i++) {
						if (statList.get(i).equals("Charisma")) {
							CharismaValue = statValueList.get(i).getText();
						} else if (statList.get(i).equals("Constitution")) {
							ConstitutionValue = statValueList.get(i).getText();
						} else if (statList.get(i).equals("Dexterity")) {
							DexterityValue = statValueList.get(i).getText();
						} else if (statList.get(i).equals("Intelligence")) {
							IntelligenceValue = statValueList.get(i).getText();
						} else if (statList.get(i).equals("Strength")) {
							StrengthValue = statValueList.get(i).getText();
						} else if (statList.get(i).equals("Wisdom")) {
							WisdomValue = statValueList.get(i).getText();
						}
					}

					// print the confirmation
					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to use these stats?\nCharisma: "
									+ CharismaValue + "\nConstitution: "
									+ ConstitutionValue + "\nDexterity: "
									+ DexterityValue + "\nIntelligence: "
									+ IntelligenceValue + "\nStrength: "
									+ StrengthValue + "\nWisdom: "
									+ WisdomValue, "Confirm Stats",
							JOptionPane.YES_NO_OPTION);

					// if user uses yes option to confirm, run the confirm
					if (confirm == JOptionPane.YES_OPTION) {
						dm.setCharacter_Stats(Integer.parseInt(StrengthValue),
								Integer.parseInt(DexterityValue),
								Integer.parseInt(CharismaValue),
								Integer.parseInt(IntelligenceValue),
								Integer.parseInt(WisdomValue),
								Integer.parseInt(ConstitutionValue));
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			} else if (command == "Generate Stats") {

				// go through each stat in the list and set the value to the
				// value produced in the geneateStat() method
				for (int i = 0; i < statList.size(); i++) {

					int currentStat = generateStat(statList.get(i));

					// set the text here
					statValueList.get(i).setText(String.valueOf(currentStat));
					stats[i] = currentStat;

				}//end for loop
				hasStats = true;

			}//end else if

		}//end action performed
	}//end event handler
}//end ControlPanel_Stats