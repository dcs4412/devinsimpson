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

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import AttributeCreators.SkillCreator;

/**
 * DescriptionPanel_Skills:
 * This class is used for the creation of the panel that shows the descriptions of the abilities
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Skills extends JPanel {

	private int width, height;

	EventHandler eh = new EventHandler();	//Handles Events
	private JPanel Text;

	private SkillCreator skill_creator_window = null; //skill creator reference
	private Database_Manager dm = null; //database manager reference
	private Character_Creation_Panel ccp = null; //character creation panel method

	private static ArrayList<String> skillNames; //list of skill names
	private static JTextArea skillDescriptionText; //text area for the descriptions of skills
	private static ArrayList<String> skill_descriptions; //list of skill descriptions

	/**
	 * Constructor for the class DescriptionPanel_Skills
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param sc
	 * @param d
	 * @param skn
	 * @param cp
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Skills(int w, int h, SkillCreator sc,
			Database_Manager d, ArrayList<String> skn,
			Character_Creation_Panel cp) {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Skills Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);
		dm = d;
		ccp = cp;
		skillNames = skn;

		skill_descriptions = dm.getSkill_Description(skillNames);
		// Text Field
		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		skillDescriptionText = new JTextArea();
		skillDescriptionText.setBackground(Color.white);

		skillDescriptionText
				.setText("Along with your powers, skills are a major way to define what your character does in the D&D world. These range from your knowledge about the creatures and world around you to your ability to perform acrobatic or athletic deeds of daring-do; from sneaking around and picking locks to charming your way into a king's good graces, and so on. Some skills require training to perform certain actions, and all skills benefit from the bonuses given by training.");

		skillDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(skillDescriptionText);
		skillDescriptionText.setEditable(false);
		Text.add(skillDescriptionText, BorderLayout.CENTER);

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

		JButton newClassButton = new JButton("Create New Skill");
		newClassButton.addActionListener(eh);
		this.add(newClassButton, BorderLayout.SOUTH);

		skill_creator_window = sc;
	}//end of class constructor

	/**
	 * Sets the text area to the description of a skill
	 * Called by Character_Creation_Panel
	 * @param skill
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setSkill_description(String skill) {

		boolean specificStatDescripton = false;

		for (int i = 0; i < skillNames.size(); i++) {
			if (skill == skillNames.get(i)) {
				skillDescriptionText.setText(skill_descriptions.get(i));
				specificStatDescripton = true;
			}
		}
		if (specificStatDescripton == false) {
			skillDescriptionText
					.setText("Along with your powers, skills are a major way to define what your character does in the D&D world. These range from your knowledge about the creatures and world around you to your ability to perform acrobatic or athletic deeds of daring-do; from sneaking around and picking locks to charming your way into a king's good graces, and so on. Some skills require training to perform certain actions, and all skills benefit from the bonuses given by training.");
		}

	}//end setSkill_description

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

			if (command == "Create New Skill") {
				try {
					skill_creator_window = new SkillCreator(dm, ccp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				skill_creator_window.setVisible(true);

			}//end if
		}//end actionPerformed
	}//end EventHandler
}//end DescriptionPanel_Skills