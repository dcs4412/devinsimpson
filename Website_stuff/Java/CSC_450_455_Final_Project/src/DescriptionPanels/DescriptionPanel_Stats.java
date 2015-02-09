package DescriptionPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Database_Manager;

/**
 * DescriptionPanel_Abilities:
 * This class is used for the creation of the panel that shows the descriptions of the stats
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Stats extends JPanel {

	private Database_Manager dm = null; //Database Manager reference
	private int width, height;
	private static ArrayList<String> statNames; //list of stat names
	private static JTextArea statDescriptionText; //text area for stat descriptions
	private static ArrayList<String> stat_descriptions; //list of stat descriptions
	private JPanel Text;

	/**
	 * Constructor for the class DescriptionPanel_Stats
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param d
	 * @param sn
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Stats(int w, int h, Database_Manager d,
			ArrayList<String> sn) {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Stats Description Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);
		dm = d;
		statNames = sn;

		stat_descriptions = dm.getStat_description(statNames);

		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		statDescriptionText = new JTextArea();

		// Text Field
		statDescriptionText.setText("Default stat description");

		statDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(statDescriptionText);
		statDescriptionText.setEditable(false);
		Text.add(statDescriptionText, BorderLayout.CENTER);

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

		this.add(classLink, BorderLayout.SOUTH);

	}//end class constructor

	/**
	 * Sets the text area to the description of the stat selected
	 * Called by the Character_Creation_Panel
	 * @param stat
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setStat_description(String stat) {

		boolean specificStatDescripton = false;

		for (int i = 0; i < statNames.size(); i++) {
			if (stat == statNames.get(i)) {
				statDescriptionText.setText(stat_descriptions.get(i));
				specificStatDescripton = true;
			}
		}
		if (specificStatDescripton == false) {
			statDescriptionText
					.setText("Each ability partially describes your character and affects some of his or her actions."
							+ "When an ability score changes, all attributes associated with that score change accordingly."
							+ "The ability that governs bonus spells depends on what type of spellcaster your character is: "
							+ "Intelligence for wizards; Wisdom for clerics, druids, paladins, and rangers; or Charisma for "
							+ "sorcerers and bards. In addition to having a high ability score, a spellcaster must be of high "
							+ "enough class level to be able to cast spells of a given spell level.");
		}//end if

	}//end setStat_description method
}//end DescriptionPanel_Stats