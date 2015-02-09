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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import AttributeCreators.AbilityCreator;
import AttributeCreators.ClassCreator;
import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;

/**
 * DescriptionPanel_Abilities:
 * This class is used for the creation of the panel that shows the descriptions of the abilities
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Abilities extends JPanel {

	private int width, height;

	private static Database_Manager dm = null; //Database Manager reference
	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	private static ArrayList<String> abilityNames; //list of ability names
	private static JTextArea abilityDescriptionText; //text area for the descriptions of those abilities
	private static String ability_descriptions; //descriptions of those abilities
	private JPanel Text; 

	EventHandler eh = new EventHandler(); //Handles events

	/**
	 * Constructor for the class DescriptionPanel_Abilities
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param cp
	 * @param d
	 * @param an
	 * @author Thomas Man
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Abilities(int w, int h,
			Character_Creation_Panel cp, Database_Manager d,
			ArrayList<String> an) {
		this.setSize(w, h);

		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Abilities Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);

		JButton newClassButton = new JButton("Create New Ability");
		newClassButton.addActionListener(eh);
		this.add(newClassButton, BorderLayout.SOUTH);
		dm = d;
		abilityNames = an;
		ccp = cp;

		
		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		abilityDescriptionText = new JTextArea();

		abilityDescriptionText
				.setText("Abilities generally describe a creature's physical and mental characteristics, and determine the creature's strengths and weaknesses. Ability modifiers, based on the ability scores, contribute to all kinds of effects like attack rolls, defenses, and skill checks.Creatures have three physical abilities, Strength (Str), Constitution (Con), and Dexterity (Dex), and three mental abilities, Intelligence (Int), Wisdom (Wis), and Charisma (Cha).");

		abilityDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(abilityDescriptionText);
		abilityDescriptionText.setEditable(false);
		Text.add(abilityDescriptionText, BorderLayout.CENTER);

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
	}//end of class constructor

	/**
	 * Sets the text area to the description of the selected ability
	 * Called by Character_Creation_Panel
	 * @param ability
	 * @author Thomas Man
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setAbility_description(String ability) {

		boolean specificStatDescripton = false;

		String text = dm.getAbility_Description(ability);

		abilityDescriptionText.setText(text);
		specificStatDescripton = true;

		if (specificStatDescripton == false) {
			abilityDescriptionText
					.setText("Abilities generally describe a creature's physical and mental characteristics, and determine the creature's strengths and weaknesses. Ability modifiers, based on the ability scores, contribute to all kinds of effects like attack rolls, defenses, and skill checks.Creatures have three physical abilities, Strength (Str), Constitution (Con), and Dexterity (Dex), and three mental abilities, Intelligence (Int), Wisdom (Wis), and Charisma (Cha).");
		}

	}//end setAbility_description

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener {

		@Override
		/**
		 * @author Thomas Man
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 */
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();

			if (command == "Create New Ability") {

				AbilityCreator ability_creator_window = null;
				try {
					ability_creator_window = new AbilityCreator(dm,
							ccp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ability_creator_window.setVisible(true);

			}//end if

		}//end actionPerformed
	}//end EventHandler
}//end class DescriptionPanel_Abilities