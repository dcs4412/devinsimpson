package ControlPanels;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Race:
 * This class is used for the creation of the panel that shows the races
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Race extends JPanel {

	private int width, height; //width and height of the screen for the panel

	EventHandler eh = new EventHandler(); //Handles the events

	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	private Database_Manager dm = null; //Database Manager reference
	private ArrayList<String> raceList; //list of races available to choose from
	// reference to class panel buttons
	private ArrayList<JToggleButton> raceButtonsArray = new ArrayList<JToggleButton>();
	private String selectedRace = null; //the race that is selected for character

	/**
	 * The constructor for the class ControlPanel_Race
	 * Called by the Character_Creation_Panel
	 * @param w (width of the screen)
	 * @param h (height of the screen)
	 * @param c (Character Creation Panel reference)
	 * @param d (Database Manager reference
	 * @param rn (ArrayList<String> for races available)
	 * @throws IOException
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Race(int w, int h, Character_Creation_Panel c,
			Database_Manager d, ArrayList<String> rn) throws IOException {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Race Control Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel, BorderLayout.NORTH);

		raceList = rn;
		ccp = c;
		dm = d;

		// get all races
		raceList = dm.getRace_name();
		JPanel classButtons = buildRaceSelectionPanel();
		this.add(classButtons);

		JButton confirm = new JButton("Confirm Race");
		confirm.addActionListener(eh);

		this.add(confirm, BorderLayout.SOUTH);
	}//end ControlPanel_Race constructor

	/**
	 * Builds the buttons for races available
	 * Called by the buildRaceSelectionPanel
	 * @param className (Actually the race name)
	 * @param color (Color of the button)
	 * @param width (Width of the button)
	 * @param height (height of the button)
	 * @param offset (offset for the button)
	 * @return raceButton (a JPanel in the form of a button)
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildRaceButton(String className, Color color, int width,
			int height, int offset) {
		JPanel raceButton = new JPanel();
		raceButton.setLayout(new BorderLayout());
		raceButton.setOpaque(false);

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

		JToggleButton currentClass = new JToggleButton(className);
		currentClass.setPreferredSize(new Dimension(width - offset, height
				- offset));
		currentClass.addActionListener(eh);

		raceButton.add(westSpace, BorderLayout.WEST);
		raceButton.add(eastSpace, BorderLayout.EAST);
		raceButton.add(southSpace, BorderLayout.SOUTH);
		raceButton.add(northSpace, BorderLayout.NORTH);
		raceButton.add(currentClass, BorderLayout.CENTER);

		raceButtonsArray.add(currentClass);

		raceButton.setPreferredSize(new Dimension(width, height));

		return raceButton;
	}//end buildRaceButton method

	/**
	 * Build the Selection Panel that holds all of the buttons for the available races
	 * Called by the class constructor
	 * @return fullRacePanel
	 * @throws IOException
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildRaceSelectionPanel() throws IOException {

		JPanel raceSelectionPanel = new JPanelWithBackground("t_b_i_h.jpg");
		raceSelectionPanel.setPreferredSize(new Dimension(400,
				(raceList.size() + 1) * 100));
		raceSelectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;

		// build race buttons
		
		for (String races : raceList) {
			JPanel currentCharacter = buildRaceButton(races, Color.red, 400,
					100, 10);
			raceSelectionPanel.add(currentCharacter, c);
		}

		JScrollPane scrollPane = new JScrollPane(raceSelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullRacePanel = new JPanel();
		fullRacePanel.setLayout(new BorderLayout());
		fullRacePanel.add(scrollPane);

		return fullRacePanel;
	}//end buildRaceSelectionPanel method

	/**
	 * Gets the selected race and sends it to the Database Manager
	 * Called by the Database Manager
	 * @return selectedRace
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public String getSelectedRace() {

		return selectedRace;

	}//end getSelectedRace method

	/**
	 * Sets the race the user chose to the database 
	 * Called by the Database Manager
	 * @param playerRace
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setRaceFromPlayer(String playerRace) {
		selectedRace = playerRace;
		ccp.racePanel_switch(playerRace);
		selectRace(playerRace);
	}//end setRaceFromPlayer method

	/**
	 * Determines which race is selected
	 * Called by setRaceFromPlayer
	 * @param selectedRace
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectRace(String selectedRace) {
		// go through each button on the array and select the clicked on button,
		// remove selection on all others
		for (int i = 0; i < raceList.size(); i++) {

			if (selectedRace.equals(raceList.get(i))) {
				raceButtonsArray.get(i).setSelected(true);
				ccp.racePanel_switch(selectedRace);
			} else {
				raceButtonsArray.get(i).setSelected(false);
			}//end else
		}//end for loop
	}//end selectRace method

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

			if (command == "Confirm Race") {

				boolean raceSet = false;
				selectedRace = null;

				// check if the user has set a race
				for (JToggleButton classButton : raceButtonsArray) {

					if (classButton.isSelected() == true) {

						raceSet = true;
						selectedRace = classButton.getText();
					}//end if

				}//end for loop

				if (raceSet == true) {

					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to use the " + selectedRace
									+ " race?", "Confirm Race",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {

						dm.setCharacter_Race(selectedRace);
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						selectRace(selectedRace);

					}
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"No race has been selected! you must select a race before proceeding",
									"no race", JOptionPane.ERROR_MESSAGE);
				}
			} else {

				selectRace(command);

			}//end else

		}//end action performed
	}//end event handler
}//end ControlPanel_Race class