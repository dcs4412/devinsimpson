package DescriptionPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import AttributeCreators.RaceCreator;

/**
 * DescriptionPanel_Race:
 * This class is used for the creation of the panel that shows the descriptions of the races
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Race extends JPanel {

	private int width, height;

	EventHandler eh = new EventHandler();	//Handles events
	private static Database_Manager dm = null; //Database Manager reference
	private Character_Creation_Panel ccp = null; //Character Creation Panel

	private static ArrayList<String> raceName; //list of race names
	private static JTextArea raceDescriptionText; //text area for descriptions of races
	private static ArrayList<String> race_descriptions; //list of race descriptions
	private static JEditorPane classLink; //class link for D&D rules
	private JPanel Text;

	/**
	 * Class constructor for DescriptionPanel_Race
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param cp
	 * @param d
	 * @param rn
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Race(int w, int h, Character_Creation_Panel cp,
			Database_Manager d, ArrayList<String> rn) {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Race Description Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);

		ccp = cp;
		dm = d;
		// insert text field here
		// String p = "Elf";
		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		raceDescriptionText = new JTextArea();

		raceDescriptionText
				.setText("Every character belongs to a race, one of the many intelligent humanoid species in the D&D world. The most common player character races are dwarves, elves, halflings, and humans. Some races also have subraces, such as mountain dwarf or wood elf.  The race you choose contributes to your character’s identity in an important way, by establishing a general appearance and the natural talents gained from culture and ancestry. Your character’s race grants particular racial traits, such as special senses, proficiency with certain weapons or tools, proficiency in one or more skills, or the ability to use minor spells.");

		raceDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(raceDescriptionText);
		raceDescriptionText.setEditable(false);
		Text.add(raceDescriptionText, BorderLayout.CENTER);

		JEditorPane classLink = new JEditorPane();
		classLink.setEditorKit(JEditorPane
				.createEditorKitForContentType("text/html"));
		classLink.setEditable(false);
		classLink
				.setText("<a href=\"http://media.wizards.com/2014/downloads/dnd/PlayerDnDBasicRules_v0.2.pdf\">D&D BASIC RULES</a>");

		classLink.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					// Do something with e.getURL() here
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});

		Text.add(classLink, BorderLayout.SOUTH);

		JButton newClassButton = new JButton("Create New Race");
		newClassButton.addActionListener(eh);
		this.add(newClassButton, BorderLayout.SOUTH);

	}//end class constructor

	/**
	 * Sets the text area to the description of a race
	 * Called by the Character_Creation_Panel
	 * @param race_name
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setRace_description(String race_name) {

		boolean specificStatDescripton = false;

		String text = dm.getRace_description(race_name);

		raceDescriptionText.setText(text);

		specificStatDescripton = true;

		// Default Panel
		if (specificStatDescripton == false) {
			raceDescriptionText.setText("Default");
		}

	}//end setRace_description

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
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

			if (command == "Create New Race") {
				RaceCreator race_creator_window = null;
				try {
					race_creator_window = new RaceCreator(dm, ccp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				race_creator_window.setVisible(true);

			}//end if

		}//end actionPerformed
	}//end EventHandler
}//end DescriptionPanel_Race class