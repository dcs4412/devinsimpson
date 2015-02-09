package main_panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import AttributeCreators.AbilityCreator;
import AttributeCreators.ClassCreator;
import AttributeCreators.RaceCreator;
import AttributeCreators.SkillCreator;
import main_panels.Database_Manager;
import main_panels.Dice;

/**
 * DnD_Character_Creator: 
 * The main class upon which all other classes come together to run.
 * This class is one of 7 classes of the main panels used in the creation of a new D&D character.
 * @author Devin Simpson
 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
 */
public class DnD_Character_Creator extends JFrame {

	// array of character panels
	private JPanel characterPanelCards = new JPanel(new CardLayout());

	// get reference to the character creation panel
	private Character_Creation_Panel cc_panel = null;

	// establish the JBDC database handler
	private Database_Manager dm = new Database_Manager();

	// create a reference to the dice simulator
	private Dice dice = null;

	/**
	 * Main method for the DnD Character Creator
	 * @author Devin Simpson
	 * @param args
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void main(String[] args) throws IOException {

		DnD_Character_Creator dnd_cc = new DnD_Character_Creator();
		dnd_cc.setVisible(true);

	}//end main method

	/**
	 * @author Devin Simpson
	 * 
	 *        Constructor for DnD Character Creator
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DnD_Character_Creator() throws IOException {

		this.setTitle("Dungeons and Dragons Character Creator");

		// set custom close behavior to close the JBDC connection before JFRame
		// is destroyed
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dm.close_connection();
				System.exit(0);
			}
		});

		this.setSize(900, 600);
		this.setLayout(new BorderLayout());

		// set the window to appear in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);

		// create the dice simulator that will be used throught the project
		dice = new Dice();

		// create the character creation panel for the first time, this is where
		// new characters
		// will be made and current characters will be edited
		Character_Creation_Panel create_edit_character = new Character_Creation_Panel(
				this, dm, dice);
		cc_panel = create_edit_character;

		// add the character creation panel to the cardlayout of the
		characterPanelCards.add(create_edit_character, "create,edit character");

		// add the JPanel card layout to the JFrame (this is used to move
		// between character creation and character selection)
		this.add(characterPanelCards, BorderLayout.CENTER);

		// build the character creation and editing panel and add it the JPanel
		// cards
		createCharacterEditAndNewPanel();

		((CardLayout) characterPanelCards.getLayout()).show(
				characterPanelCards, "select character, add attribute");

	}//end of DnD_Character_Creator constructor

	/**
	 * @author Devin Simpson
	 * 
	 *         This builds the panel that will contain the controls for the
	 *         creation of new characters and the editing of currently existing
	 *         ones
	 * 
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void createCharacterEditAndNewPanel() throws IOException {
		ArrayList<String> characterList = dm.getCharacter_name();

		// build the character selection and attribute panel here, this is done
		// as a separate method
		// because every time a new character is created this panel should be
		// rebuilt to reflect
		// that change
		Character_Selection_Attribute_panel select_character_attribute = new Character_Selection_Attribute_panel(
				this, dm, characterList, cc_panel);

		// add the new panel to the card layout of the
		characterPanelCards.add(select_character_attribute,
				"select character, add attribute");

	}// end createCharacterEditAndNewPanel method

	/**
	 * 
	 * @author Devin Simpson
	 * 
	 *         This method switches the screen from the character selection
	 *         panel to the character new/editing panel and vice versa
	 * 
	 * @param select
	 * @param name
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void swapScreens(String select, String name) throws IOException {

		// if the user is creating a new or editing an existing character
		// display the character creation panel and set the mode
		// the panel so it will behave properly
		if (select == "Edit Character") {
			cc_panel.setMode("edit", name, true);
			((CardLayout) characterPanelCards.getLayout()).show(
					characterPanelCards, "create,edit character");

		} else if (select == "New Character") {
			cc_panel.setMode("new", name, false);
			((CardLayout) characterPanelCards.getLayout()).show(
					characterPanelCards, "create,edit character");

			// when the confirm character button has been clicked in character
			// creation/editing
			// move back to the character selection panel
		} else if (select == "Confirm Character") {

			// rebuilt the character select and attribute panel to show the new
			// character
			createCharacterEditAndNewPanel();

			// display the new character select and attribute panel
			((CardLayout) characterPanelCards.getLayout()).show(
					characterPanelCards, "select character, add attribute");
		}//end else if

	}// end swapScreens method

}// end DnD_Character_Creator class