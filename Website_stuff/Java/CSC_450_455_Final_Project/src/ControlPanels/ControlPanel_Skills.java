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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main_panels.Database_Manager;
import main_panels.Character_Creation_Panel;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Skills:
 * This class is used for the creation of the panel that shows the skills
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Skills extends JPanel {

	private int width, height; //width and height of the screen for the panel

	EventHandler eh = new EventHandler(); //Handles the events

	private Character_Creation_Panel ccp = null; //Character Creation Panel reference

	private ArrayList<JCheckBox> skillCheckBoxes = new ArrayList();
	private ArrayList<String> skillList = null;  //list of skills available to choose from
	private ArrayList<String> raceSkills = null; //skills that come with the given race
	private static Database_Manager dm = null;   //Database Manager reference
	private boolean reEnableCheckBoxes = false;

	private int baseSkillSelectionLimit = 3;

	/**
	 * Constructor the ControlPanel_Skills class
	 * Called in Character_Creation_Panel
	 * @param w (width of screen)
	 * @param h (height of screen)
	 * @param c (Character Creation Panel reference)
	 * @param d (Database Manager reference)
	 * @param skn (skill list available to choose from)
	 * @param skr (skill list that comes with race)
	 * @throws IOException
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Skills(int w, int h, Character_Creation_Panel c,
			Database_Manager d, ArrayList<String> skn, ArrayList<String> skr)
			throws IOException {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Skills Control Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel, BorderLayout.NORTH);
		dm = d;
		skillList = skn;
		raceSkills = skr;
		ccp = c;

		baseSkillSelectionLimit += raceSkills.size();

		JPanel skillSelectionPanel = buildSkillSelectionPanel();
		this.add(skillSelectionPanel);
		skillSelectionPanel.setOpaque(false);
		JButton confirm = new JButton("Confirm Skills");
		confirm.addActionListener(eh);

		this.add(confirm, BorderLayout.SOUTH);
	}//end of ControlPanel_Skills Constructor

	/**
	 * Builds the check boxes for the skills available
	 * Called by the buildSkillSelectionPanel
	 * @param skillName (the name of the skill)
	 * @param color	(the color of the button)
	 * @param width (the width of the button)
	 * @param height (the height of the button)
	 * @param offset (the offset of the button)
	 * @return skillCheckBox (JPanel of a check box)
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildSkillCheckBox(String skillName, Color color, int width,
			int height, int offset) {
		JPanel skillCheckBox = new JPanel();
		skillCheckBox.setLayout(new BorderLayout());
		skillCheckBox.setOpaque(false);

		JPanel northSpace = new JPanel();
		northSpace.setOpaque(false);
		northSpace.setPreferredSize(new Dimension(width, offset));

		JPanel southSpace = new JPanel();
		southSpace.setOpaque(false);
		southSpace.setPreferredSize(new Dimension(width, offset));

		JPanel westSpace = new JPanel();
		westSpace.setOpaque(false);
		westSpace.setPreferredSize(new Dimension(offset, height));

		JPanel eastSpace = new JPanel();
		eastSpace.setOpaque(false);
		eastSpace.setPreferredSize(new Dimension(offset, height));

		JPanel skillPanel = new JPanel();
		skillPanel.setOpaque(false);
		skillPanel.setLayout(new GridLayout(1, 3));
		JCheckBox currentSkillCheckBox = new JCheckBox();

		currentSkillCheckBox.setBackground(color);
		final JLabel currentSkillName = new JLabel(skillName, JLabel.CENTER);
		currentSkillName.setOpaque(false);
		currentSkillName.setBackground(Color.white);

		boolean raceSkillDisableSelect = false;

		for (String raceSkill : raceSkills) {
			if (skillName.equals(raceSkill)) {
				raceSkillDisableSelect = true;
			}//end if
		}//end for loop

		if (raceSkillDisableSelect == true) {
			currentSkillCheckBox.setSelected(true);
			currentSkillCheckBox.setEnabled(false);
		}//end if

		JPanel centerSpace = new JPanel();
		centerSpace.setOpaque(false);

		currentSkillName.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				ccp.skillPanel_switch(currentSkillName.getText());
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				// change the description panel to that of the default stats
				// description.
				ccp.skillPanel_switch("default");

			}
		});

		skillPanel.add(currentSkillName);
		skillPanel.add(centerSpace);
		skillPanel.add(currentSkillCheckBox);

		skillCheckBox.add(westSpace, BorderLayout.WEST);
		skillCheckBox.add(eastSpace, BorderLayout.EAST);
		skillCheckBox.add(southSpace, BorderLayout.SOUTH);
		skillCheckBox.add(northSpace, BorderLayout.NORTH);
		skillCheckBox.add(skillPanel, BorderLayout.CENTER);

		skillCheckBoxes.add(currentSkillCheckBox);
		currentSkillCheckBox.addActionListener(eh);
		currentSkillCheckBox.setOpaque(false);
		skillCheckBox.setPreferredSize(new Dimension(width, height));

		return skillCheckBox;
	}//end buildSkillCheckBox method

	/**
	 * Builds the panel that holds all of the skill buttons available
	 * Called by the class constructor
	 * @return fullSkillPanel
	 * @throws IOException
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildSkillSelectionPanel() throws IOException {

		JPanel skillSelectionPanel = new JPanelWithBackground("t_b_i_h.jpg");
		skillSelectionPanel.setBackground(Color.red);
		skillSelectionPanel.setPreferredSize(new Dimension(400, (skillList
				.size()) * 50));
		skillSelectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;

		// build skill checkboxes

		for (String skills : skillList) {
			JPanel currentCharacter = buildSkillCheckBox(skills, Color.red,
					300, 50, 10);
			skillSelectionPanel.add(currentCharacter, c);
		}

		JScrollPane scrollPane = new JScrollPane(skillSelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullSkillPanel = new JPanel();
		fullSkillPanel.setLayout(new BorderLayout());
		fullSkillPanel.add(scrollPane);

		return fullSkillPanel;

	}//end buildSkillSelectionPanel method

	/**
	 * Sets the skills the user chose to the database
	 * Called by the Database Manager
	 * @param playerChosenSkills
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setSkillsFromPlayer(ArrayList<String> playerChosenSkills) {

		for (int i = 0; i < skillList.size(); i++) {

			for (int j = 0; j < playerChosenSkills.size(); j++) {
				if (skillList.get(i).equals(playerChosenSkills.get(j))) {
					skillCheckBoxes.get(i).setSelected(true);
				}//end if
			}//end inner for loop

		}//end outer for loop
		selectSkills();
	}//end setSkillsFromPlayer method

	/**
	 * Determines which skills were selected
	 * Called by setSkillsFromPlayer
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectSkills() {

		// we are going to find the number of check boxes clicked, if three have
		// been clicked, disable
		// all non clicked check boxes so only at most three can be selected
		int numberOfChecked = 0;

		// find all the checked check boxes
		for (JCheckBox skillCheckBox : skillCheckBoxes) {

			if (skillCheckBox.isSelected() == true) {
				numberOfChecked++;
			}

		}

		// if that number is three plus the rase skills, disable all non checked
		// check boxes
		if (numberOfChecked == baseSkillSelectionLimit) {

			for (JCheckBox skillCheckBox : skillCheckBoxes) {

				if (skillCheckBox.isSelected() == false) {
					skillCheckBox.setEnabled(false);
				}

			}

			// set a boolean to show that non checked boxes have been disabled
			reEnableCheckBoxes = true;

			// if the number of checked check boxes is less than 3
		} else {

			// check if the non checked check boxes are disabled using the
			// boolean
			if (reEnableCheckBoxes == true) {

				// if the non checked check boxes are disabled, re enable them
				for (int i = 0; i < skillCheckBoxes.size(); i++) {

					boolean raceSkill = false;

					for (String rSkill : raceSkills) {

						if (skillList.get(i).equals(rSkill)) {
							raceSkill = true;
						}

					}

					if (raceSkill == false) {
						skillCheckBoxes.get(i).setEnabled(true);
					}
				}

				// update the boolean to reflect the re enabled checkboxes
				reEnableCheckBoxes = false;
			}//end if
		}//end else
	}//end selectSkills method

	/**
	 * Sets the selected skills to the specific character in the database
	 * Called by the EventHandler
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void setSkills() {

		ArrayList<String> selectedSkills = new ArrayList();
		for (int i = 0; i < skillList.size(); i++) {

			if (skillCheckBoxes.get(i).isSelected() == true) {
				selectedSkills.add(skillList.get(i));
			}//end if

		}//end for loop

		dm.setCharacter_skills(selectedSkills);
	}//end setSkills method

	/**
	 * Gets the selected skills and sends them to the Database Manager
	 * Called by the Database Manager
	 * @return selectedSkills
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getSelectedSkills() {

		ArrayList<String> selectedSkills = new ArrayList<String>();

		for (int i = 0; i < skillCheckBoxes.size(); i++) {

			if (skillCheckBoxes.get(i).isSelected() == true) {

				selectedSkills.add(skillCheckBoxes.get(i).getText());
			}

		}
		return selectedSkills;
	}//end getSelectedSkills method

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
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
			// the checkbox's action listener produces an empty string, so when
			// the
			// action is listener is called with the "" argument, we know a
			// check box has been clicked
			if (command == "") {

				selectSkills();

			}

			else if (command == "Confirm Skills") {

				String skillSelect = "";

				for (int i = 0; i < skillList.size(); i++) {

					if (skillCheckBoxes.get(i).isSelected() == true) {
						skillSelect += skillList.get(i) + "\n";
					}

				}

				if (skillSelect.equals("")) {
					JOptionPane
							.showMessageDialog(
									null,
									"No skills have been selected! You must select at least one skill before proceeding",
									"No Skill", JOptionPane.ERROR_MESSAGE);

				} else {

					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to use these skills?\n"
									+ skillSelect, "Confirm Skills",
							JOptionPane.YES_NO_OPTION);

					// if user uses yes option to confirm, run the confirm
					if (confirm == JOptionPane.YES_OPTION) {
						setSkills();
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}//end catch
					}//end confirm if yes
				}//end else
			}//end else if command == Confirm Skills
		}//end ActionPerformed
	}//end EventHandler
}//end ControlPanel_Skills class