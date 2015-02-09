package ControlPanels;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;
import main_panels.Character_Selection_Attribute_panel;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Class:
 * This class is used for the creation of the panel that shows the classes
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Class  extends JPanel{

	private int width, height; //width and height of the screen for the panel
	
	EventHandler eh = new EventHandler(); //Handles the events
	
	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	private ArrayList<String>classList; //list of classes available to choose from
	//reference to class panel buttons
	private ArrayList<JToggleButton> classButtonsArray = new ArrayList<JToggleButton>();
	private String selectedClass = null; //the class that is selected for character
	private static Database_Manager dm = null;  //Database Manager reference
	
	/**
	 * Constructor for the ControlPanel_Class
	 * Called in Character_Creation_Panel
	 * @param w (width of the screen)
	 * @param h (height of the screen)
	 * @param c (Character_Creation_Panel reference)
	 * @param cn (ArrayList<String> for classes available)
	 * @param d (Database_Manager reference)
	 * @throws IOException
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Class(int w, int h, Character_Creation_Panel c, ArrayList<String> cn, Database_Manager d) throws IOException{
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		JLabel panelLabel = new JLabel("Class Control Panel",JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel,BorderLayout.NORTH);
		
		ccp = c;
		classList = cn;
		dm = d;
		JPanel classButtons = buildClassSelectionPanel();
		this.add(classButtons);
		
		JButton confirm = new JButton("Confirm Class");
		confirm.addActionListener(eh);
		
		this.add(confirm, BorderLayout.SOUTH);
	}//end of ControlPanel_Class constructor
	
	/**
	 * Builds the buttons for the classes available
	 * Called by BuildClassSelectionPanel
	 * @param className (the name of the class)
	 * @param color (the color of the button)
	 * @param width (the width of the button)
	 * @param heigth (the height of the button)
	 * @param offset (the offset for the button's size)
	 * @return classButton (JPanel of a button)
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildClassButton(String className, Color color,int width, int heigth, int offset)
	{
		JPanel classButton = new JPanel();
		classButton.setLayout(new BorderLayout());
		classButton.setOpaque(false);
		
		
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
		
		JToggleButton  currentClass = new JToggleButton(className);
		currentClass.setPreferredSize(new Dimension(width - offset, heigth - offset ));
		currentClass.addActionListener(eh);
		

		classButton.add(westSpace, BorderLayout.WEST);
		classButton.add(eastSpace, BorderLayout.EAST);
		classButton.add(southSpace, BorderLayout.SOUTH);
		classButton.add(northSpace, BorderLayout.NORTH);
		classButton.add(currentClass, BorderLayout.CENTER);
		
		classButtonsArray.add(currentClass);
		
		classButton.setPreferredSize(new Dimension(width, heigth));
		
		return classButton;
		
	}//end BuildClassButton method
	
	/**
	 * Returns the class that was selected
	 * Called by the Database_Manager
	 * @return selectedClass
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public String getSelectedClass(){
		
		return selectedClass;
		
	}//end getSelectedClass method
	
	/**
	 * Builds the class selection panel
	 * Called by the class constructor
	 * @return fullClassPanel (A Panel that contains all the class buttons) 
	 * @throws IOException
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildClassSelectionPanel() throws IOException{
		
		JPanel classSelectionPanel = new JPanelWithBackground("t_b_i_h.jpg");
		classSelectionPanel.setPreferredSize(new Dimension(400, (classList.size()+1)*100));
		classSelectionPanel.setLayout(new GridBagLayout());  
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		//build class buttons
		
		for (String classes : classList){
			JPanel  currentCharacter = buildClassButton(classes, Color.red, 400, 100, 10);
			classSelectionPanel.add(currentCharacter,c);
		}
		
		JScrollPane scrollPane = new JScrollPane(classSelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullClassPanel = new JPanel();
		fullClassPanel.setLayout(new BorderLayout());
		fullClassPanel.add(scrollPane);
		
		return fullClassPanel;
		
	}//end buildClassSelectionPanel method
	
	/**
	 * Sets the class the user chose to the database
	 * Called by the Database Manager
	 * @param playerClass
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setClassFromPlayer(String playerClass){
		selectedClass = playerClass;
		ccp.classPanel_switch(playerClass);
		selectClass(playerClass);
	}//end setClassFromPlayer method
	
	/**
	 * Determines which class is selected
	 * Called by the EventHandler
	 * @param SelectedClass
	 * @author Victor Mancha and Devin Simpson
     * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void selectClass(String SelectedClass){
		
		//go through each button on the array and select the clicked on button, remove selection on all others
		for(int i = 0; i < classList.size();i++){
			
			if (SelectedClass.equals(classList.get(i))){
				
				classButtonsArray.get(i).setSelected(true);
				ccp.classPanel_switch(SelectedClass);
			}else{
			
				classButtonsArray.get(i).setSelected(false);
				
			}//end else
		}//end for loop
		
	}//end selectClass method
	
	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * @author Devin Simpson
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

			if (command == "Confirm Class"){
				
				boolean classSet = false;
				selectedClass = null;
				
				//check if the user has set a class 
				for(JToggleButton classButton: classButtonsArray){
					
					if (classButton.isSelected() == true){
						
						classSet = true;
						selectedClass = classButton.getText();
					}
					
				}
				
				if(classSet == true){
					
					int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to use the " +selectedClass + " class?", "Confirm Class", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						
						dm.setCharacter_Class(selectedClass);
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						selectClass(selectedClass);
						
					}
				}else{
					JOptionPane.showMessageDialog(null, "No class has been selected! you must select a class before proceeding", "no class", JOptionPane.ERROR_MESSAGE);
				}
				
			}else{
				
				selectClass(command);
				
			}//end last else
	
		}//end action performed
	}//end event handler
}//end class ControlPanel_Class