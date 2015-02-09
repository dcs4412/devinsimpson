package ControlPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;
import main_panels.CharacterSheetPrintPreview;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;

/**
 * ControlPanel_ConfirmCharacter:
 * This class is used for the creation of the panel that shows the 
 * confirmation of the character the user has been working on.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_ConfirmCharacter extends JPanel{

	private int width, height;

	EventHandler eh = new EventHandler(); //handles events
	private static Database_Manager dm = null; //database manager reference
	private Character_Creation_Panel ccp = null; //character creation panel
	
	private String className, raceName, characterName;
	private ArrayList<Integer> statValues;
	private ArrayList<String> textInputDetails, playerSkills, playerAbilites, playerWeapons, playerArmor, playerGear, detailLabels, levelGenderAlignment;
	
	private CharacterSheetPrintPreview characterSheet;

	/**
	 * Constructor for the class ControlPanel_ConfirmCharacter
	 * Called in Character_Creation_Panel
	 * @param w (width)
	 * @param h (height)
	 * @param c (character creation panel)
	 * @param d (database manager)
	 * @param chName
	 * @param clName
	 * @param rName
	 * @param stValues
	 * @param details
	 * @param dLabels
	 * @param levGenAl
	 * @param pSkill
	 * @param pAbility
	 * @param pWeapon
	 * @param pArmor
	 * @param pGear
	 * @throws IOException
	 * @author Victor Mancha, Thomas Man and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_ConfirmCharacter(int w, int h, Character_Creation_Panel c, Database_Manager d, String chName, 
			String clName, String rName, ArrayList<Integer> stValues, ArrayList<String> details,ArrayList<String> dLabels,
			ArrayList<String> levGenAl, ArrayList<String> pSkill, ArrayList<String> pAbility,
			ArrayList<String> pWeapon, ArrayList<String> pArmor, ArrayList<String> pGear) throws IOException{
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Confirm Character Control Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel,BorderLayout.NORTH);
		
		ccp = c;
		dm = d;
		
		characterName = chName;
		className = clName;
		raceName = rName;
		
		statValues = stValues;
		
		textInputDetails =details;
		levelGenderAlignment = levGenAl;
		detailLabels = dLabels;
		
		playerSkills = pSkill;
		playerAbilites = pAbility;
		
		playerWeapons = pWeapon;
		playerArmor = pArmor;
		playerGear  = pGear;
		
		JPanel printPreviewPanel = characterSheetPreviewPanel();
		
		JButton confirm = new JButton("Confirm Character");
		confirm.addActionListener(eh);
		
		this.add(printPreviewPanel, BorderLayout.CENTER);
		this.add(confirm, BorderLayout.SOUTH);
		
	}//end method
	
	/**
	 * Creates the panel that shows a preview of the character the user has been working on.
	 * Called by the class constructor
	 * @return fullPreviewPanel
	 * @throws IOException
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel characterSheetPreviewPanel() throws IOException{
		
		JPanel fullPreviewPanel = new JPanelWithBackground("t_b_i_h.jpg");
		fullPreviewPanel.setLayout(new BorderLayout());
		
		//fill the sides of the panel with empty space to make the print preview look nice
		JPanel northSpace = new JPanel();
		northSpace.setPreferredSize(new Dimension(400,50));
		northSpace.setOpaque(false);
		JPanel southSpace = new JPanel();
		southSpace.setPreferredSize(new Dimension(400,50));
		southSpace.setOpaque(false);
		JPanel eastSpace = new JPanel();
		eastSpace.setPreferredSize(new Dimension(40,600));
		eastSpace.setOpaque(false);
		JPanel westSpace = new JPanel();
		westSpace.setPreferredSize(new Dimension(40,600));
		westSpace.setOpaque(false);
		
		JPanel centerSpace = new JPanel();
		TitledBorder characterSheetBorder = getBorderForPanel("Character Sheet Preview");
		centerSpace.setBorder(characterSheetBorder);
		centerSpace.setOpaque(false);
		characterSheet = new CharacterSheetPrintPreview(characterName, className, raceName ,statValues, textInputDetails,levelGenderAlignment,detailLabels ,playerSkills ,playerAbilites ,playerWeapons,playerArmor ,playerGear   );
		
		
		centerSpace.setLayout(new BorderLayout());
		
		
		
		JScrollPane scrollPane = new JScrollPane(characterSheet); 
		centerSpace.add(scrollPane);
		
		fullPreviewPanel.add(northSpace, BorderLayout.NORTH);
		fullPreviewPanel.add(southSpace, BorderLayout.SOUTH);
		fullPreviewPanel.add(eastSpace, BorderLayout.EAST);
		fullPreviewPanel.add(westSpace, BorderLayout.WEST);
		fullPreviewPanel.add(centerSpace, BorderLayout.CENTER);

		return fullPreviewPanel;
		
	}//end method
	
	/**
	 * Gets the border for the panel fullPreviewPanel
	 * Called by characterSheetPreviewPanel
	 * @param name
	 * @return title
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private TitledBorder getBorderForPanel(String name){
		
		//create a titled border for the panel
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title;
		title = BorderFactory.createTitledBorder(blackline, name);
		title.setTitleJustification(TitledBorder.CENTER);
		
		return title;
		
	}//end method
	/**
	 * Returns the character sheet
	 * Called by the Database Manager
	 * @return characterSheet
	 * @author Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public CharacterSheetPrintPreview getCharacterSheet(){
		
		return characterSheet;
		
	}
	
	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Thomas Man and Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private class EventHandler implements ActionListener{

		@Override
		/**
		 * @author Devin Simpson
		 * implementation of the action listener's action performed method, where action events
		 * are handled 
		 */
		public void actionPerformed(ActionEvent e) {
			
			String command = e.getActionCommand();
			
			if (command == "Confirm Character"){
				
				
				
				String mode = "new";
				
				ArrayList<String> allCharacterNames = dm.getCharacter_name();
				
				for(String characterName: allCharacterNames){
					if(characterName.equals(characterName)){
						mode = "edit";
					}
					
				}
				
				dm.AddNewCharacter(mode);
				
				try {
					ccp.confirmingCharacter(command);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}//end actionPerformed method
		
	}//end EventHandlerclass
	
}//end ControlPanel_ConfirmCharacter class