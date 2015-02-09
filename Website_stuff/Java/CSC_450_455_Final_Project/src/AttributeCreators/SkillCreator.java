package AttributeCreators;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * SkillCreator:
 * This class is used for the creation of new skills.
 * This class is one of four attribute creators used in creating new attributes.
 * This class exists to provide the users with greater options for skills.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class SkillCreator extends JFrame{

	EventHandler eh = new EventHandler();		//handles events
	private Character_Creation_Panel ccp = null;	//character creation panel
	private Database_Manager dm = null;			//database manager reference
	
	JFrame skillCreator = this;
	private JTextArea skillDescriptionText = new JTextArea();	//description panel text area
	private JTextArea skillNameField = new JTextArea();	//race name
	private JComboBox statList = new JComboBox();		//speed list
	private String[] stats = new String[6];						//6 stat bonuses
	private String[] statDescriptions = new String[6];			//stat descriptions
	private String[] availableClass;							//classes available to the user
	private DefaultListModel recClass = new DefaultListModel(); //Array list of recommended classes
	private JTextArea descriptionField = new JTextArea();		//description text area
	private JList list = new JList();							//list of classes
	private JScrollPane listScroller = new JScrollPane();		//scrollpane of these classes
	private JList list2 = new JList();							//list of users choices of classes
	private JScrollPane listScroller2 = new JScrollPane();		//scrollpane of these choices
	private ArrayList<String> output = new ArrayList<String>(); //actual choices in an array list
	
	/**
	 * Constructor for the skill creator popup window
	 * Called in Character_Selection_Attribute_panel
	 * @param d (database manager)
	 * @param cp (character creation panel)
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 * @throws IOException 
	 */
	public SkillCreator(Database_Manager d, Character_Creation_Panel cp ) throws IOException{

		dm = d;
		ccp =cp;
		
		this.setTitle("Skill Creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,500);
		this.setLayout(new BorderLayout());
		
		//set the window to appear in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		JPanel skillControlPanel = controlPanel();
		JPanel skillDescriptionPanel = descriptionPanel();
		this.add(skillControlPanel, BorderLayout.CENTER);
		this.add(skillDescriptionPanel, BorderLayout.EAST);
	}//end of SkillCreator constructor
	
	/**
	 * The controlPanel is used for the creation of the new skill.
	 * This includes the skill's name, stat modifier, 
	 * recommended class and description.
	 * Called in the class constructor
	 * @return controlPanel
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 * @throws IOException 
	 */
	private JPanel controlPanel() throws IOException{
		
		JPanel control_panel = new JPanel();
		control_panel.setSize(400, 400);
		control_panel.setLayout(new BorderLayout());
		
		control_panel.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Skill Creator Control Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		control_panel.add(panelLabel,BorderLayout.NORTH);
		
		JPanel info_panel = new JPanelWithBackground("t_b_i_h.jpg"); // info panel
		info_panel.setSize(400, 400);
		info_panel.setLayout(new GridLayout(0, 1));
		control_panel.add(info_panel, BorderLayout.CENTER);
		
		JPanel skillInfo = new JPanel(); // skill info panel: label and TextArea
		skillInfo.setLayout(new BorderLayout());
		info_panel.add(skillInfo);
		skillInfo.setOpaque(false);
		JLabel skillNameLabel = new JLabel(" Skill Name:   ", JLabel.CENTER); //skill name label
		skillNameLabel.setOpaque(false);
		
		skillNameLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("This location is used for determing the name of your skill.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		skillInfo.add(skillNameLabel, BorderLayout.WEST);

		skillNameField = new JTextArea("", 3, 29);		//area to enter skill name
		skillNameField.setPreferredSize(new Dimension(3, 3));
		skillNameField.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("Fill in your skill name.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		skillInfo.add(skillNameField, BorderLayout.CENTER);
		
		JPanel statInfo = new JPanel(); 		// stats and class info panel: labels and combo boxes
		statInfo.setLayout(new GridLayout(0, 4));
		info_panel.add(statInfo);
		statInfo.setOpaque(false);
		JLabel statLabel = new JLabel("Stats Modifier: ", JLabel.CENTER); //stats modifier label
		statLabel.setOpaque(false);
		statLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("Your stats modifier make you strong.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		statInfo.add(statLabel);

		stats[0] = "Cha"; //stats modifier choices
		stats[1] = "Con";
		stats[2] = "Dex";
		stats[3] = "Int";
		stats[4] = "Str";
		stats[5] = "Wis";

		//descriptions of the stats modifiers
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

		statList = new JComboBox(stats);
		statList.addActionListener(eh);
		statList.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				if(statList.getSelectedItem().equals("Cha")){
					skillDescriptionText.setText(statDescriptions[0]);
				}
				if(statList.getSelectedItem().equals("Con")){
					skillDescriptionText.setText(statDescriptions[1]);
				}
				if(statList.getSelectedItem().equals("Dex")){
					skillDescriptionText.setText(statDescriptions[2]);
				}
				if(statList.getSelectedItem().equals("Int")){
					skillDescriptionText.setText(statDescriptions[3]);
				}
				if(statList.getSelectedItem().equals("Str")){
					skillDescriptionText.setText(statDescriptions[4]);
				}
				if(statList.getSelectedItem().equals("Wis")){
					skillDescriptionText.setText(statDescriptions[5]);
				}
		}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		statInfo.add(statList);

		JLabel extraSpace = new JLabel("Recommended", JLabel.RIGHT); //Part 1 of Recommended Class
		extraSpace.setOpaque(false);
		statInfo.add(extraSpace);

		JLabel sizeLabel = new JLabel(" Class: ", JLabel.LEFT); //Part 2 of Recommended Class
		sizeLabel.setOpaque(false);
		sizeLabel.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("This location is used for determing the recommended class for your skill.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		statInfo.add(sizeLabel);

		JPanel recomClassInfo = new JPanel(); 		// Holds the 2 lists and their buttons
		recomClassInfo.setLayout(new GridLayout(0, 3));
		info_panel.add(recomClassInfo);
		
		ArrayList<String> clas = dm.getClass_name();
		availableClass = new String[clas.size()];
		
		for(int i = 0; i<clas.size(); i++){
			availableClass[i] = clas.get(i);
		}
		
		list = new JList(availableClass); //data has type String[] for available classes for the skill
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<availableClass.length;i++){
					if(list.isSelectedIndex(i)){
						
						skillDescriptionText
								.setText(dm.getClass_description(availableClass[i]));
						} //end if
					}//end for loop

			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<availableClass.length;i++){
					if(list.isSelectedIndex(i)){
						
						skillDescriptionText
								.setText(dm.getClass_description(availableClass[i]));
						} //end if
					}//end for loop

			}//end mouse entered
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		listScroller = new JScrollPane(list);
		recomClassInfo.add(listScroller);
		
		JPanel buttons = new JPanel();		//holds the buttons Add and Remove
		buttons.setLayout(new GridLayout(0,1));
		buttons.setOpaque(false);
		recomClassInfo.add(buttons);
		
		JButton top = new JButton("Add"); //Adds classes that can use this skill
		top.addActionListener(eh);
		top.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText.setText("Add class(es) that can use this skill.");
			}
			
			public void mouseExited(MouseEvent e){
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		buttons.add(top);
		
		JButton bot = new JButton("Remove"); //Removes classes that can use this skill
		bot.addActionListener(eh);
		bot.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText.setText("Remove class(es) that can use this skill.");
			}
			
			public void mouseExited(MouseEvent e){
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		buttons.add(bot);
		
		list2 = new JList(recClass); //data has type String[] of the classes the user chose
		list2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list2.setLayoutOrientation(JList.VERTICAL);
		list2.setVisibleRowCount(-1);
		list2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				for(int i = 0; i<availableClass.length;i++){
					if(list2.isSelectedIndex(i)){
						
						skillDescriptionText
								.setText(dm.getClass_description(availableClass[i]));
						} //end if
					}//end for loop

			}//end mouse entered

			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i<availableClass.length;i++){
					if(list2.isSelectedIndex(i)){
						
						skillDescriptionText
								.setText(dm.getClass_description(availableClass[i]));
						} //end if
					}//end for loop

			}//end mouse entered
			
			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		listScroller2 = new JScrollPane(list2);
		recomClassInfo.add(listScroller2);
		
		
		JLabel extraLine2 = new JLabel("Description: ", JLabel.CENTER); //label of descripton for skill
		extraLine2.setOpaque(false);
		extraLine2.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("This location is used for determing the description of your skill.");

			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		info_panel.add(extraLine2);
		
		JPanel descriptionInfo = new JPanel(); // description panel: label and TextArea
		descriptionInfo.setLayout(new BorderLayout());
		info_panel.add(descriptionInfo);
		descriptionInfo.setOpaque(false);
		descriptionField = new JTextArea();	//area to enter the description of the skill
		descriptionField.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(descriptionField);
		scrollPane.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				skillDescriptionText
						.setText("This location is used for determing the description of your skill.");
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				skillDescriptionText.setText("Fill in your skill information.");
			}
		});
		descriptionInfo.add(scrollPane);
		
		
		JButton confirmButton = new JButton("Confirm New Skill"); //confirms the new skill
		confirmButton.addActionListener(eh);
		control_panel.add(confirmButton, BorderLayout.SOUTH);
		
		return control_panel;
	}//end of controlPanel method
	
	/**
	 * This panel is used to display information about the different requirements to create a skill.
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
		JLabel panelLabel = new JLabel("Skill Creator Description Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		description_panel.add(panelLabel,BorderLayout.NORTH);
		
				skillDescriptionText = new JTextArea();
				skillDescriptionText.setLineWrap(true);
				JScrollPane scrollPane = new JScrollPane(skillDescriptionText);
				skillDescriptionText.setEditable(false);
				description_panel.add(scrollPane, BorderLayout.CENTER);
		
				JEditorPane classLink = new JEditorPane(); //link to the rules for D&D
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
	}//end of the descriptionPanel method
	
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
			
			if (command == "Add"){	//adds a class to the list of classes that can use this skill
			for(int i = 0; i<availableClass.length;i++){
				if(list.isSelectedIndex(i) && !recClass.contains(availableClass[i])){
			recClass.addElement(availableClass[i]);
					} //end if
				}//end for loop
			}//end if Add
			
			if (command == "Remove"){ //removes a class from the list of classes that can use this skill
				for(int i = 0; i<recClass.getSize();i++){
					if(list2.isSelectedIndex(i)){
				recClass.remove(i);
						} //end if
					}//end for loop
				}//end if Add
			
			//confirms the new skill to be created
			if (command == "Confirm New Skill"){
				if (skillNameField.getText().equals("")){ //if the skill does not have a name, give it one
					JOptionPane.showMessageDialog(null, "You must enter a Skill Name", "Missing Field", JOptionPane.ERROR_MESSAGE);
				}
				else if (descriptionField.getText().equals("")){ //if the skill does not have a description, give it one
					JOptionPane.showMessageDialog(null, "You must enter a Description", "Missing Field", JOptionPane.ERROR_MESSAGE);
				}
				else if (recClass.isEmpty()){ //if there are no classes selected to use this skill, select one
					JOptionPane.showMessageDialog(null, "You must enter at least one class into the list of Recommended Classes", "Missing Field", JOptionPane.ERROR_MESSAGE);
				}
				else{//otherwise, create the new skill
			
					int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to create this Skill?\n", "Confirm Skill", JOptionPane.YES_NO_OPTION);
					
					//if user uses yes option to confirm, run the confirm 
					if (confirm == JOptionPane.YES_OPTION) {
						
						for(int i=0; i<recClass.getSize();i++){
						
						output.add((String) recClass.getElementAt(i));
						}
						
						dm.addSkill(skillNameField.getText(), statList.getSelectedItem().toString(), descriptionField.getText(), output);
						try {
							ccp.setUpSkillPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							ccp.swapSkillPanel();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						skillCreator.dispose();
							}//if yes
					}//Else 
			}//if Confirm New Skill
		}//end Action Performed
	}//end EventHandler
}//end class SkillCreator