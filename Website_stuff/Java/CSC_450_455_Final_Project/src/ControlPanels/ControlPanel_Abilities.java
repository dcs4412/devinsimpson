package ControlPanels;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Abilities:
 * This class is used for the creation of the panel that shows the abilities
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Abilities extends JPanel{

	private int width, height;
	
	EventHandler eh = new EventHandler(); //handles events
	
	private Character_Creation_Panel ccp = null; //character creation panel
	
	private ArrayList <String> abilityList = new ArrayList<String>(); //list of abilities available
	private ArrayList <JCheckBox> abilitycheckBoxes = new ArrayList<JCheckBox> (); //check boxes of abilities
	private static Database_Manager dm = null; //database manager reference
	
	/**
	 * Constructor for the class ControlPanel_Abilities
	 * Called in Character_Creation_Panel
	 * @param d (the Database Manager)
	 * @param cp (the Character Creation Panel)
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Abilities(int w, int h, Character_Creation_Panel c, Database_Manager d, ArrayList<String> an) throws IOException{
		
		this.setSize(w, h);
		
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		JLabel panelLabel = new JLabel("Abilities Control Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel,BorderLayout.NORTH);
		
		dm = d;
		ccp = c;
		abilityList = an;
		
		JPanel abilitySelectionPanel = buildAbilitySelectionPanel();
		this.add(abilitySelectionPanel);
		
		JButton confirm = new JButton("Confirm Abilities");
		confirm.addActionListener(eh);
		
		this.add(confirm, BorderLayout.SOUTH);
	}//end class constructor
	
	/**
	 * Builds ability checkboxes and then returns them
	 * Called by buildAbilitySelectionPanel
	 * @param abilityName
	 * @param color
	 * @param width
	 * @param heigth
	 * @param offset
	 * @return abilityCheckBox
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildAbilityCheckBox(String abilityName, Color color,int width, int heigth, int offset)
	{
		JPanel abilityCheckBox = new JPanel();
		abilityCheckBox.setLayout(new BorderLayout());
		abilityCheckBox.setOpaque(false);
		
		
		JPanel northSpace = new JPanel();
		northSpace.setOpaque(false);
		northSpace.setPreferredSize(new Dimension(width, offset));
		
		JPanel southSpace = new JPanel();
		southSpace.setOpaque(false);
		southSpace.setPreferredSize(new Dimension(width, offset));
		
		JPanel westSpace = new JPanel();
		westSpace.setOpaque(false);
		westSpace.setPreferredSize(new Dimension(offset, heigth));
		
		JPanel eastSpace = new JPanel();
		eastSpace.setOpaque(false);
		eastSpace.setPreferredSize(new Dimension(offset, heigth));
		
		JPanel abilityPanel = new JPanel();
		abilityPanel.setOpaque(false);
		abilityPanel.setLayout(new GridLayout(1,3));
		JCheckBox  currentAbilityCheckBox = new JCheckBox();
		
		currentAbilityCheckBox.setOpaque(false);
		final JLabel  currentAbilityName = new JLabel(abilityName,JLabel.CENTER);
		currentAbilityName.setOpaque(false);
		JPanel centerSpace = new JPanel();
		centerSpace.setOpaque(false);
		
		currentAbilityName.addMouseListener(new MouseAdapter()  
		{
			//when the user enters the JLabel area.
		    public void mouseEntered(MouseEvent e)  
		    {  
		       ccp.abilityPanel_switch(currentAbilityName.getText());
		    }  
		    
		  //when the user exits the JLabel area.
		    public void mouseExited(MouseEvent e)  
		    {  
		       //change the description panel to that of the default stats description.
		    	ccp.abilityPanel_switch("default");
		    } 
		}); 
		
		abilityPanel.add(currentAbilityName);
		abilityPanel.add(centerSpace);
		abilityPanel.add(currentAbilityCheckBox);
		
		abilityCheckBox.add(westSpace, BorderLayout.WEST);
		abilityCheckBox.add(eastSpace, BorderLayout.EAST);
		abilityCheckBox.add(southSpace, BorderLayout.SOUTH);
		abilityCheckBox.add(northSpace, BorderLayout.NORTH);
		abilityCheckBox.add(abilityPanel, BorderLayout.CENTER);
		
		abilitycheckBoxes.add(currentAbilityCheckBox);
		abilityCheckBox.setPreferredSize(new Dimension(width, heigth));
		currentAbilityCheckBox.setOpaque(false);
		return abilityCheckBox;
	}//end buildAbilityCheckBox
	
	/**
	 * Builds the selection panel that holds all of the ability checkboxes
	 * Called by ControlPanel method
	 * @return fullAbilityPanel
	 * @throws IOException
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildAbilitySelectionPanel() throws IOException{
	
		JPanel abilitySelectionPanel = new JPanelWithBackground("t_b_i_h.jpg");
		//abilitySelectionPanel.setBackground(Color.red);
		abilitySelectionPanel.setPreferredSize(new Dimension(400, (abilityList.size())*50));
		abilitySelectionPanel.setLayout(new GridBagLayout());  
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		//build ability checkboxes
		
		for (String abilites : abilityList){
			JPanel  currentCharacter = buildAbilityCheckBox(abilites, Color.BLUE, 300, 50, 10);
			abilitySelectionPanel.add(currentCharacter,c);
		}
		
		JScrollPane scrollPane = new JScrollPane(abilitySelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullAbilityPanel = new JPanel();
		fullAbilityPanel.setLayout(new BorderLayout());
		fullAbilityPanel.add(scrollPane);
		
		return fullAbilityPanel;
	}//end buildAbilitySelectionPanel method
	
	/**
	 * Sets the abilities the player chose.
	 * Called by the Database Manager
	 * @param playerAbilites
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void  setAbilitesFromPlayer(ArrayList<String> playerAbilites){
		
		for (int i = 0; i < abilityList.size(); i++){
			for(int j = 0; j < playerAbilites.size(); j++ ){
				if(abilityList.get(i).equals(playerAbilites.get(j))){
					abilitycheckBoxes.get(i).setSelected(true);
				}
			}
		}
		
	}//end setAbilitiesFromPlayer method
	
	/**
	 * Sets the abilities the player chose.
	 * Called by Event Handler, used in Database Manager
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void setAbilites(){
		ArrayList<String> selectedAbilities = new ArrayList<String>();
		
		for(int i = 0; i < abilityList.size();i++){
			
			if(abilitycheckBoxes.get(i).isSelected() == true){
				selectedAbilities.add(abilityList.get(i));
			}
		}
		
		dm.setCharacter_ability(selectedAbilities);
	}
	
	/**
	 * Gets the abilities that were selected.
	 * Called by the Database Manager
	 * @return selectedAbilities
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getSelectedAbilites(){
		
		ArrayList<String> selectedAbilites = new ArrayList<String>();
		
		for(int i = 0; i < abilitycheckBoxes.size(); i++){
			
			if(abilitycheckBoxes.get(i).isSelected() == true){
				
				selectedAbilites.add(abilitycheckBoxes.get(i).getText());
			}
			
		}
		return selectedAbilites;
	}//end getSelectedAbilities method
	
	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Victor Mancha, Thomas Man, Devin Simpson
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
			
			if (command == "Confirm Abilities"){
				
				String abilitySelect = "";
				
				for(int i = 0; i < abilityList.size();i++){
					
					if(abilitycheckBoxes.get(i).isSelected() == true){
						abilitySelect += abilityList.get(i)+"\n";
					}
				}
				
				if (abilitySelect.equals("")){
					JOptionPane.showMessageDialog(null, "No abilites have been selected! You must select at least one ability before proceeding", "No Ability", JOptionPane.ERROR_MESSAGE);
					
				}else{
				
					int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to use these abilites?\n"+ abilitySelect, "Confirm Abilites", JOptionPane.YES_NO_OPTION);
					
					//if user uses yes option to confirm, run the confirm 
					if (confirm == JOptionPane.YES_OPTION) {
						setAbilites();
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					}//end if yes
				}//end else
			}//end confirm abilities
		}//end action performed
	}//end event handler
}//end ControlPanel_Abilities class