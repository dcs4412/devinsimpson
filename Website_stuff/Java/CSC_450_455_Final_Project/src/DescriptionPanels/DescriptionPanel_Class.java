package DescriptionPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import AttributeCreators.ClassCreator;
import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;

/**
 * DescriptionPanel_Class:
 * This class is used for the creation of the panel that shows the descriptions of the classes
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Class extends JPanel {

	private int width, height;

	EventHandler eh = new EventHandler();	//Handles events

	private static Database_Manager dm = null; //Database Manager reference
	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	private static ArrayList<String> className; //list of class names
	private static JTextArea classDescriptionText; //area for descriptions of classes
	private static ArrayList<String> class_descriptions; //list of descriptions of classes
	private static JEditorPane classLink; //class link to the rules
	private JPanel Text;

	/**
	 * Constructor for the class DescriptionPanel_Class
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param cp
	 * @param d
	 * @param cn
	 * @author Thomas Man
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Class(int w, int h, Character_Creation_Panel cp,
			Database_Manager d, ArrayList<String> cn) {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Class Description Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);
		dm = d;
		className = cn;

		// TEXT FIELD
		// String p = "paladin";
		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		classDescriptionText = new JTextArea();

		classDescriptionText
				.setText("Every adventurer is a member of a class. Class broadly describes a character’s vocation, what special talents he or she possesses, and the tactics he or she is most likely to employ when exploring a dungeon, fighting monsters, or engaging in a tense negotiation. Your character receives a number of benefits from your choice of class. Many of these benefits are class features—capabilities (including spellcasting) that set your character apart from members of other classes. You also gain a number of proficiencies: armor, weapons, skills, saving throws, and sometimes tools. Your proficiencies define many of the things your character can do particularly well, from using certain weapons to telling a convincing lie.");

		classDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(classDescriptionText);
		classDescriptionText.setEditable(false);
		Text.add(classDescriptionText, BorderLayout.CENTER);

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

		JButton newClassButton = new JButton("Create New Class");
		newClassButton.addActionListener(eh);
		this.add(newClassButton, BorderLayout.SOUTH);

		ccp = cp;

	}//end class constructor

	/**
	 * Sets the class description in the text area
	 * Called by Character_Creation_Panel
	 * @param class_name
	 * @author Thomas Man
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setClass_description(String class_name) {

		boolean specificStatDescripton = false;

		String text = dm.getClass_description(class_name);

		classDescriptionText.setText(text);

		specificStatDescripton = true;

		// Default Panel
		if (specificStatDescripton == false) {
			classDescriptionText
					.setText("This is where you select your class. Every adventurer is a member of a class. Class broadly describes a character’s vocation, what special talents he or she possesses, and the tactics he or she is most likely to employ when exploring a dungeon, fighting monsters, or engaging in a tense negotiation. Your character receives a number of benefits from your choice of class. Many of these benefits are class features—capabilities (including spellcasting) that set your character apart from members of other classes. You also gain a number of proficiencies: armor, weapons, skills, saving throws, and sometimes tools. Your proficiencies define many of the things your character can do particularly well, from using certain weapons to telling a convincing lie");
		}

	}//end setClass_description

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

			if (command == "Create New Class") {

				ClassCreator class_creator_window = null;
				try {
					class_creator_window = new ClassCreator(dm, ccp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				class_creator_window.setVisible(true);

			}//end if

		}//end actionPerformed
	}//end EventHandler
}//end DescriptionPanel_Class class