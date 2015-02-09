package AttributeCreators;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * AbilityCreator:
 * This class is used for the creation of new abilities.
 * This class is one of four attribute creators used in creating new attributes.
 * This class exists to provide the users with greater options for abilities.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class AbilityCreator extends JFrame{

	EventHandler eh = new EventHandler();	//handles events
	
	//reference to object
	private Character_Creation_Panel ccp = null;	//character creation panel
	private Database_Manager dm = null;				//database manager reference
	private JTextArea abilityNameField = new JTextArea(); //Used for entering ability name
	private JTextArea descriptionField = new JTextArea(); //Used for entering description of ability
	private JComboBox class_list = new JComboBox();		  //Combo box used for the different classes
	private JComboBox levellist = new JComboBox();		  //Combo box used for the different levels
	private JComboBox statlist = new JComboBox();		  //Combo box used for the different stats
	private String[] statDescriptions = new String[6];			//stat descriptions
	private JComboBox dice_list = new JComboBox();		  //Combo box used for the different dice
	private JComboBox slotslist = new JComboBox();		  //Combo box used for the different number of slots
	private JComboBox castlist = new JComboBox();		  //Combo box used for the different casting spells
	private JTextArea abilityDescriptionText = new JTextArea(); //Used for the description panel
	private ArrayList<String> diceList = null;			//ArrayList used for the different dice
	private ArrayList<String> classList =  null;		//ArrayList used for the different classes

	JFrame abilityCreator = this;
	
	/**
	 * Constructor for the class AbilityCreator
	 * Called in Character_Selection_Attribute_panel
	 * @param d (the Database Manager)
	 * @param cp (the Character Creation Panel)
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 * @throws IOException 
	 */
	public AbilityCreator(Database_Manager d, Character_Creation_Panel cp) throws IOException{
		
		this.setTitle("Ability Creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,500);
		this.setLayout(new BorderLayout());
		
		//set the window to appear in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		dm = d;	//database manager
		ccp =cp;	//character creation panel
		diceList = dm.getAll_Dice();	//gets all the dice
		classList = dm.getClass_name();	//gets all the classes
		
		JPanel abilityControlPanel = controlPanel();
		JPanel abilityDescriptionPanel = descriptionPanel();
		this.add(abilityControlPanel, BorderLayout.CENTER);
		this.add(abilityDescriptionPanel, BorderLayout.EAST);
		
	}//end ability creator constructor
	
	/**
	 * The controlPanel is used for the creation of the new ability.
	 * This includes the ability's name, level requirement, stat proficiency,
	 * damage, class usage, spell slots, spell casting stat and description.
	 * Called in the class constructor
	 * @return controlPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 * @throws IOException 
	 */
	private JPanel controlPanel() throws IOException{
		
		JPanel control_panel = new JPanel(); //holds everything within the center of the panel
		control_panel.setSize(400, 400);
		control_panel.setLayout(new BorderLayout());
		
		control_panel.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Ability Creator Control Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		control_panel.add(panelLabel,BorderLayout.NORTH);
		
		JPanel info_panel = new JPanelWithBackground("t_b_i_h.jpg"); // info panel holds everything within the control panel
		info_panel.setSize(400, 400);
		info_panel.setLayout(new GridLayout(0, 1));
		control_panel.add(info_panel, BorderLayout.CENTER);

		JPanel abilityInfo = new JPanel(); // class info panel: label and TextArea
		abilityInfo.setLayout(new BorderLayout());
		info_panel.add(abilityInfo);
		abilityInfo.setOpaque(false);
		
		JLabel abilityNameLabel = new JLabel(" Ability Name:   ", JLabel.CENTER); //label for ability name
		abilityNameLabel.setOpaque(false);
		abilityNameLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("This location is used for determine the name of your ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abilityInfo.add(abilityNameLabel, BorderLayout.WEST);

		abilityNameField = new JTextArea("", 3, 29);	//text area for the ability name
		abilityNameField.setPreferredSize(new Dimension(3, 3));
		abilityNameField.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your race name.");
			}
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your race information.");
			}
		});
		abilityInfo.add(abilityNameField, BorderLayout.CENTER);
		
		JPanel abiInfo = new JPanel(); // ability info panel: labels and combo boxes
		abiInfo.setLayout(new GridLayout(0, 3));
		info_panel.add(abiInfo);
		abiInfo.setOpaque(false);
		
		JLabel extraSpace = new JLabel("Level ", JLabel.RIGHT); //Part 1 of a whole title (Level Requirement)
		extraSpace.setOpaque(false);

		extraSpace.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText
						.setText("Your Level Requirement is used to determine the level that you must be in order to use this ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(extraSpace);
		
		
		JLabel levelLabel = new JLabel("Requirement:", JLabel.LEFT); //Part 2 of a whole title (Level Requirement)
		levelLabel.setOpaque(false);
		levelLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText
						.setText("Your Level Requirement is used to determine the level that you must be in order to use this ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(levelLabel);

		String[] level = { "None", "1", "2", "3"};

		levellist = new JComboBox(level);
		levellist.addActionListener(eh);
		levellist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("This shows the level required to use this ability");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(levellist);
		
		JLabel extraSpace2 = new JLabel("Stat  ", JLabel.RIGHT); //Part 1 of Stat Proficiency
		extraSpace2.setOpaque(false);
		extraSpace2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("Your Stat Proficiency is the stat that you must be proficient in to use this ability.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(extraSpace2);
		
		JLabel statbonusLabel = new JLabel("Proficiency: ", JLabel.LEFT); //Part 2 of Stat Proficiency
		statbonusLabel.setOpaque(false);
		statbonusLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("Your Stat Proficiency is the stat that you must be proficient in to use this ability.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(statbonusLabel);
		
		String[] stat = { "Charisma","Constitution", "Dexterity","Intelligence", "Strength", "Wisdom", "None" };
		
		
		
		statDescriptions[0] = "Charisma measures a character’s force of personality, persuasiveness, personal magnetism, ability to lead,"
				+ "and physical attractiveness. This ability represents actual strength of personality, not merely how one is perceived "
				+ "by others in a social setting. Charisma is most important for paladins, sorcerers, and bards. It is also important for "
				+ "clerics, since it affects their ability to turn undead. Every creature has a Charisma score.";
		statDescriptions[1] = "Constitution represents your character’s health and stamina. A Constitution bonus increases a character’s hit points, "
				+ "so the ability is important for all classes.";
		statDescriptions[2] = "Dexterity measures hand-eye coordination, agility, reflexes, and balance. This ability is the most important one for rogues, "
				+ "but it’s also high on the list for characters who typically wear light or medium armor (rangers and barbarians) or no armor at all "
				+ "(monks, wizards, and sorcerers), and for anyone who wants to be a skilled archer.";
		statDescriptions[3] = "Intelligence determines how well your character learns and reasons. This ability is important for wizards because it affects "
				+ "how many spells they can cast, how hard their spells are to resist, and how powerful their spells can be. It’s also important for any "
				+ "character who wants to have a wide assortment of skills.";
		statDescriptions[4] = "Strength measures your character’s muscle and physical power. This ability is especially important for Fighters, barbarians, "
				+ "paladins, rangers, and monks because it helps them prevail in combat. Strength also limits the amount of equipment your character can carry.";
		statDescriptions[5] = "Wisdom describes a character’s willpower, common sense, perception, and intuition. While Intelligence represents one’s ability to "
				+ "analyze information, Wisdom represents being in tune with and aware of one’s surroundings. Wisdom is the most important ability for "
				+ "clerics and druids, and it is also important for paladins and rangers. If you want your character to have acute senses, "
				+ "put a high score in Wisdom. Every creature has a Wisdom score.";
		
		statlist = new JComboBox(stat);
		statlist.addActionListener(eh);
		statlist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				
				if (statlist.getSelectedItem().equals("Charisma")) {
					abilityDescriptionText.setText(statDescriptions[0]);
				}
				if (statlist.getSelectedItem().equals("Constitution")) {
					abilityDescriptionText.setText(statDescriptions[1]);
				}	
				if (statlist.getSelectedItem().equals("Dexterity")) {
					abilityDescriptionText.setText(statDescriptions[2]);
				}	
				if (statlist.getSelectedItem().equals("Intelligence")) {
					abilityDescriptionText.setText(statDescriptions[3]);
				}	
				if (statlist.getSelectedItem().equals("Strength")) {
					abilityDescriptionText.setText(statDescriptions[4]);
				}	
				if (statlist.getSelectedItem().equals("Wisdom")) {
					abilityDescriptionText.setText(statDescriptions[5]);
				}	
				if (statlist.getSelectedItem().equals("None")) {
					abilityDescriptionText.setText("This ability does not require proficiency in a certain stat.");
				}
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		abiInfo.add(statlist);
		
		JPanel descInfo = new JPanel(); // dice panel: labels and combo boxes
		descInfo.setLayout(new GridLayout(0, 4));
		info_panel.add(descInfo);
		descInfo.setOpaque(false);
		JLabel damageLabel = new JLabel("Damage: ", JLabel.CENTER); //damage label
		damageLabel.setOpaque(false);
		damageLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("This determines the damage of your ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		descInfo.add(damageLabel);
		
		String[] dice = new String[diceList.size()];
		
		for(int i = 0; i< diceList.size(); i++){
			dice[i] = diceList.get(i);
		}

		dice_list = new JComboBox(dice);
		dice_list.addActionListener(eh);
		dice_list.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("This determines the damage of your ability");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
			
		});
		descInfo.add(dice_list);
		
		
		JLabel classLabel = new JLabel("Class Usage: ", JLabel.CENTER);	//class usage label
		classLabel.setOpaque(false);
		classLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("This determines the class that uses your ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		descInfo.add(classLabel);
		
		String[] classNames = new String[classList.size()];
				
				for(int i = 0; i < classList.size(); i++){
					classNames[i] = classList.get(i);
				}
		
				class_list = new JComboBox(classNames);
				class_list.addActionListener(eh);
				class_list.addMouseListener(new MouseAdapter() {
					// when the user enters the JLabel area.
					public void mouseEntered(MouseEvent e) {
						for(int i = 0; i< classList.size(); i++){
						if(class_list.getSelectedItem().equals(classList.get(i)))
							abilityDescriptionText.setText(dm.getClass_description(classList.get(i)));
						}//end for loop
					}//end mouseEntered

					// when the user exits the JLabel area.
					public void mouseExited(MouseEvent e) {
						abilityDescriptionText.setText("Fill in your ability information.");
					}
					
				});
				descInfo.add(class_list);
				
		
		JPanel spellInfo = new JPanel(); // spell panel: labels and combo boxes
		spellInfo.setLayout(new GridLayout(0, 5));
		info_panel.add(spellInfo);
		spellInfo.setOpaque(false);
		JLabel spells = new JLabel("Spell Slots: ", JLabel.CENTER);
		spells.setOpaque(false);
		spells.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("If your ability is a spell, fill in the following information.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		spellInfo.add(spells);
		
		String[] slots = { "1", "2", "3", "4", "5"};
		
		slotslist = new JComboBox(slots);
		slotslist.addActionListener(eh);
		slotslist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("Determine the number of spell slots.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		spellInfo.add(slotslist);
		
		JLabel spell_cast = new JLabel("Spell Cast", JLabel.RIGHT); //Part 1 of spell casting stat
		spell_cast.setOpaque(false);
		spell_cast.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("If your ability is a spell, fill in the following information");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		spellInfo.add(spell_cast);
		
		JLabel spell_cast2 = new JLabel("ing Stat:", JLabel.LEFT);	//Part 2 of spell casting stat
		spell_cast2.setOpaque(false);
		spell_cast2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText.setText("If your ability is a spell, fill in the following information");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		spellInfo.add(spell_cast2);
		
		String[] cast = { "Charisma","Constitution", "Dexterity","Intelligence", "Strength", "Wisdom"};
		
		castlist = new JComboBox(cast);
		castlist.addActionListener(eh);
		castlist.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {

				if (castlist.getSelectedItem().equals("Charisma")) {
					abilityDescriptionText.setText(statDescriptions[0]);
				}
				if (castlist.getSelectedItem().equals("Constitution")) {
					abilityDescriptionText.setText(statDescriptions[1]);
				}	
				if (castlist.getSelectedItem().equals("Dexterity")) {
					abilityDescriptionText.setText(statDescriptions[2]);
				}	
				if (castlist.getSelectedItem().equals("Intelligence")) {
					abilityDescriptionText.setText(statDescriptions[3]);
				}	
				if (castlist.getSelectedItem().equals("Strength")) {
					abilityDescriptionText.setText(statDescriptions[4]);
				}	
				if (castlist.getSelectedItem().equals("Wisdom")) {
					abilityDescriptionText.setText(statDescriptions[5]);
				}	
			}
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		spellInfo.add(castlist);
		
		JLabel extraLine2 = new JLabel("Description: ", JLabel.CENTER); //Description label
		extraLine2.setOpaque(false);
		extraLine2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText
						.setText("The description of your ability.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		info_panel.add(extraLine2);

		JPanel descriptionInfo = new JPanel(); // description panel: label and TextArea
		descriptionInfo.setLayout(new BorderLayout());
		info_panel.add(descriptionInfo);
		descriptionInfo.setOpaque(false);
		descriptionField = new JTextArea();	//User enters description of ability here
		descriptionField.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(descriptionField);
		scrollPane.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				abilityDescriptionText
						.setText("The description of your ability.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				abilityDescriptionText.setText("Fill in your ability information.");
			}
		});
		descriptionInfo.add(scrollPane);
		
		
		JButton confirmButton = new JButton("Confirm New Ability"); //button to confirm new ability
		confirmButton.addActionListener(eh);
		control_panel.add(confirmButton, BorderLayout.SOUTH);
		
		return control_panel;
	}//end control panel method
	
	/**
	 * This panel is used to display information about the different requirements to create an ability.
	 * Called in the class constructor
	 * @return descriptionPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel descriptionPanel(){ 
		JPanel description_panel = new JPanel();
		description_panel.setSize(400,400);
		description_panel.setLayout(new BorderLayout());
		description_panel.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Ability Creator Description Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		description_panel.add(panelLabel,BorderLayout.NORTH);
		
		abilityDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(abilityDescriptionText);	//Scroll Pane for the panel
		description_panel.add(scrollPane, BorderLayout.CENTER);
		abilityDescriptionText.setEditable(false);
		
		JEditorPane classLink = new JEditorPane();	//link to the rules of D&D
		classLink.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		classLink.setEditable(false);
		classLink.setText("<a href=\"http://media.wizards.com/2014/downloads/dnd/PlayerDnDBasicRules_v0.2.pdf\">D&D BASIC RULES</a>");
		
		classLink.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		           // Do something with e.getURL() here
		        	if(Desktop.isDesktopSupported()) {
		    		    try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} 
		    		    catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
		    		    catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    		}
		        }
		    }
		});
		
		description_panel.add(classLink, BorderLayout.SOUTH);
		
		return description_panel;
	}//end description Panel method
	
	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Victor Mancha, Thomas Man, Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener{

		@Override
		/**
		 * @author Devin Simpson and Victor Mancha
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 */
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command == "Confirm New Ability"){
				
				if (abilityNameField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "You must enter an Ability Name", "Missing Field", JOptionPane.ERROR_MESSAGE);
				}
				else if (descriptionField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "You must enter a Description", "Missing Field", JOptionPane.ERROR_MESSAGE);
				}
				else{
			
					int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to create this ability?\n", "Confirm Ability", JOptionPane.YES_NO_OPTION);
					
					//if user uses yes option to confirm, run the confirm 
					if (confirm == JOptionPane.YES_OPTION) {
					
						dm.addAbility(abilityNameField.getText(),levellist.getSelectedItem().toString(),statlist.getSelectedItem().toString(), descriptionField.getText(), dice_list.getSelectedItem().toString(), slotslist.getSelectedItem().toString(), castlist.getSelectedItem().toString(), class_list.getSelectedItem().toString());
						try {
							ccp.setUpAbilityPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							ccp.swapAbilityPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						abilityCreator.dispose();
					
							}
						}//IF ALL INPUT IS CORRECT
				
			}//end confirm new ability if
		}//end actionPerformed
	}//end Event Handler
	
}//end of class