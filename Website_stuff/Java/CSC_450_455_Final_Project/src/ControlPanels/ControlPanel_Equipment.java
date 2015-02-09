package ControlPanels;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import main_panels.Database_Manager;
import main_panels.Character_Creation_Panel;
import main_panels.JPanelWithBackground;

/**
 * ControlPanel_Equipment:
 * This class is used for the creation of the panel that shows the equipment
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
 */
public class ControlPanel_Equipment extends JPanel {

	private int width, height;

	EventHandler eh = new EventHandler(); //Handles events

	private static Database_Manager dm = null; //Database Manager reference
	private Character_Creation_Panel ccp = null; //Character Creation Panel
	private ArrayList<String> weaponList, armorList, gearList;

	private ArrayList<JCheckBox> equipmentCheckBoxes = new ArrayList<JCheckBox>();
	private ArrayList<int[]> equipmentPrices = new ArrayList<int[]>();

	private int goldIndex = 0, silverIndex = 1, copperIndex = 2;
	private int[] characterWallet = null;

	private ArrayList<String> selectedWeapons = new ArrayList();
	private ArrayList<String> selectedArmor = new ArrayList();
	private ArrayList<String> selectedGear = new ArrayList();

	/**
	 * Constructor for the class ControlPanel_Equipment
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param c
	 * @param d
	 * @param weapons
	 * @param armor
	 * @param gear
	 * @param startingCash
	 * @throws IOException
	 * @author Victor Mancha, Thomas Man, Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ControlPanel_Equipment(int w, int h, Character_Creation_Panel c,
			Database_Manager d, ArrayList<String> weapons,
			ArrayList<String> armor, ArrayList<String> gear, int[] startingCash)
			throws IOException {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.orange);
		JLabel panelLabel = new JLabel("Equipment Control Panel", JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.white);
		this.add(panelLabel, BorderLayout.NORTH);

		dm = d;
		ccp = c;

		weaponList = weapons;
		armorList = armor;
		gearList = gear;
		characterWallet = startingCash;

		JPanel weaponSelectionPanel = buildEquipmentSelectionPanel();
		this.add(weaponSelectionPanel);

		JButton confirm = new JButton("Confirm Equipment");
		confirm.addActionListener(eh);

		this.add(confirm, BorderLayout.SOUTH);
	}//end constructor

	/**
	 * @author Devin Simpson
	 * 
	 *         handle all store transactions by adding or removing gold, silver,
	 *         and copper as the user buys and sells equipment
	 * 			Called by buySellEquipment
	 * @param amount
	 *            , the amount of (gold, silver, or copper) to be added or
	 *            removed from the character's wealth
	 * @param type
	 *            , the type of currency being transacted (gold "gp", silver
	 *            "sp", copper "cp")
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void moneyTransaction(int amount, String type) {

		if (type == "gp") {

			characterWallet[goldIndex] = characterWallet[goldIndex] + amount;

		} else if (type == "sp") {

			characterWallet[silverIndex] = characterWallet[silverIndex]
					+ amount;

			if (characterWallet[silverIndex] > 99) {

				while (characterWallet[silverIndex] > 99) {

					characterWallet[silverIndex] = characterWallet[silverIndex] - 100;
					characterWallet[goldIndex]++;

				}
			} else if (characterWallet[silverIndex] < 0) {

				while (characterWallet[silverIndex] < 0) {

					characterWallet[silverIndex] = characterWallet[silverIndex] + 100;
					characterWallet[goldIndex]--;
				}

				if (characterWallet[silverIndex] == 100) {

					characterWallet[silverIndex] = 0;
					characterWallet[goldIndex]++;
				}
			}

		} else if (type == "cp") {

			characterWallet[copperIndex] = characterWallet[copperIndex]
					+ amount;

			if (characterWallet[copperIndex] > 99) {

				while (characterWallet[copperIndex] > 99) {

					characterWallet[copperIndex] = characterWallet[copperIndex] - 100;
					characterWallet[silverIndex]++;

					if (characterWallet[silverIndex] > 99) {

						characterWallet[silverIndex] = 0;
						characterWallet[goldIndex]++;

					}
				}
			} else if (characterWallet[copperIndex] < 0) {

				while (characterWallet[copperIndex] < 0) {

					characterWallet[copperIndex] = characterWallet[copperIndex] + 100;
					characterWallet[silverIndex]--;

					if (characterWallet[silverIndex] < 0) {

						characterWallet[silverIndex] = 0;
						characterWallet[goldIndex]--;

					}
				}

				if (characterWallet[copperIndex] == 100) {

					characterWallet[copperIndex] = 99;
					characterWallet[silverIndex]++;

				}//end if
			}//end else if
		}//end else cp

	}//end moneyTransaction method

	/**
	 * Builds the check boxes for the equipment available to the user
	 * Called by buildEquipmentSelectionPanel
	 * @param equipmentName
	 * @param color
	 * @param width
	 * @param heigth
	 * @param offset
	 * @param equipment_type
	 * @return equipmentCheckBox
	 * @author Victor Mancha, Thomas Man, Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private Box buildEquipmentCheckBox(final String equipmentName, Color color,
			int width, int heigth, int offset, final String equipment_type) {
		Box equipmentCheckBox = Box.createHorizontalBox();

		equipmentCheckBox.setOpaque(false);

		JCheckBox currentEquipmentCheckBox = new JCheckBox();

		currentEquipmentCheckBox.setName(equipment_type);

		currentEquipmentCheckBox.setBackground(color);
		equipmentCheckBox.setPreferredSize(new Dimension(width, heigth));

		currentEquipmentCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
		currentEquipmentCheckBox.setText(equipmentName);
		currentEquipmentCheckBox.setOpaque(false);

		equipmentCheckBox.add(currentEquipmentCheckBox);
		equipmentCheckBox.setOpaque(false);
		currentEquipmentCheckBox.addMouseListener(new MouseAdapter() {
			// when the user enters the JLabel area.
			public void mouseEntered(MouseEvent e) {
				ccp.equipmentPanel_switch(equipmentName, equipment_type);
			}

			// when the user exits the JLabel area.
			public void mouseExited(MouseEvent e) {
				// change the description panel to that of the default weapon
				// description.
				ccp.equipmentPanel_switch("default", null);
			}
		});

		currentEquipmentCheckBox.addActionListener(eh);

		equipmentCheckBoxes.add(currentEquipmentCheckBox);

		return equipmentCheckBox;

	}//end buildEquipmentCheckBox method

	/**
	 * Builds the panel that holds all of the equipment check boxes
	 * Called by the class constructor
	 * @return
	 * @throws IOException
	 * @author Victor Mancha, Thomas Man, Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private JPanel buildEquipmentSelectionPanel() throws IOException {

		JPanel equipmentSelectionPanel = new JPanelWithBackground("t_b_i_h.jpg");
		equipmentSelectionPanel.setBackground(Color.red);

		ArrayList<String> LongestList = null;

		if (weaponList.size() > armorList.size()
				&& weaponList.size() > gearList.size()) {
			LongestList = weaponList;

		} else if (armorList.size() > weaponList.size()
				&& armorList.size() > gearList.size()) {
			LongestList = armorList;

		} else {
			LongestList = gearList;

		}

		equipmentSelectionPanel.setPreferredSize(new Dimension(400,
				(LongestList.size()) * 25));
		equipmentSelectionPanel.setLayout(new GridLayout(1, 3));

		// build the three lists of equipment, weapons, armor, and gear

		JPanel weaponSelectionPanel = new JPanel();
		weaponSelectionPanel.setOpaque(false);
		weaponSelectionPanel.setPreferredSize(new Dimension(200, (LongestList
				.size()) * 25));
		weaponSelectionPanel.setLayout(new GridBagLayout());

		JPanel armorSelectionPanel = new JPanel();
		armorSelectionPanel.setOpaque(false);
		armorSelectionPanel.setPreferredSize(new Dimension(200, (LongestList
				.size()) * 25));
		armorSelectionPanel.setLayout(new GridBagLayout());

		JPanel gearSelectionPanel = new JPanel();
		gearSelectionPanel.setOpaque(false);
		gearSelectionPanel.setPreferredSize(new Dimension(200, (LongestList
				.size()) * 25));
		gearSelectionPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.anchor = GridBagConstraints.EAST;
		for (String weapons : weaponList) {
			Box currentWeapon = buildEquipmentCheckBox(weapons, Color.red, 200,
					50, 10, "weapon");
			weaponSelectionPanel.add(currentWeapon, c);
		}

		for (String armor : armorList) {
			Box currentArmor = buildEquipmentCheckBox(armor, Color.orange, 200,
					50, 10, "armor");
			armorSelectionPanel.add(currentArmor, c);
		}

		for (String gear : gearList) {
			Box currentArmor = buildEquipmentCheckBox(gear, Color.yellow, 200,
					50, 10, "gear");
			gearSelectionPanel.add(currentArmor, c);
		}

		equipmentSelectionPanel.add(weaponSelectionPanel);
		equipmentSelectionPanel.add(armorSelectionPanel);
		equipmentSelectionPanel.add(gearSelectionPanel);

		JScrollPane scrollPane = new JScrollPane(equipmentSelectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		JPanel fullWeaponPanel = new JPanel();
		fullWeaponPanel.setLayout(new BorderLayout());
		fullWeaponPanel.add(scrollPane);

		return fullWeaponPanel;
	}//end buildEquipmentSelectionPanel

	/**
	 * Sets the item prices for the different equipment items
	 * Called by the Database Manager
	 * @param weaponName
	 * @param weaponPrice
	 * @param armorName
	 * @param armorPrice
	 * @param gearName
	 * @param gearPrice
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setItemPrices(ArrayList<String> weaponName,
			ArrayList<int[]> weaponPrice, ArrayList<String> armorName,
			ArrayList<int[]> armorPrice, ArrayList<String> gearName,
			ArrayList<int[]> gearPrice) {

		for (int i = 0; i < weaponName.size(); i++) {

			for (int j = 0; j < equipmentCheckBoxes.size(); j++) {

				if (weaponName.get(i).equals(
						equipmentCheckBoxes.get(j).getText())) {

					equipmentPrices.add(weaponPrice.get(i));

				}
			}
		}

		for (int i = 0; i < armorName.size(); i++) {

			for (int j = 0; j < equipmentCheckBoxes.size(); j++) {

				if (armorName.get(i).equals(
						equipmentCheckBoxes.get(j).getText())) {

					equipmentPrices.add(armorPrice.get(i));

				}
			}
		}

		for (int i = 0; i < gearName.size(); i++) {

			for (int j = 0; j < equipmentCheckBoxes.size(); j++) {

				if (gearName.get(i)
						.equals(equipmentCheckBoxes.get(j).getText())) {

					equipmentPrices.add(gearPrice.get(i));

				}
			}
		}

		disableExpensiveEquipment();

	}//end setItemPrices method

	/**
	 * Sets the equipment of the character to the database
	 * Called by the Database Manager
	 * @param playerWeapons
	 * @param playerArmor
	 * @param playerGear
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public void setEquipmentFromPlayer(ArrayList<String> playerWeapons,
			ArrayList<String> playerArmor, ArrayList<String> playerGear) {

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {

			boolean moveOn = true;

			for (int j = 0; j < playerWeapons.size(); j++) {

				if (equipmentCheckBoxes.get(i).getText()
						.equals(playerWeapons.get(j))) {

					equipmentCheckBoxes.get(i).setSelected(true);
					moveOn = false;
				}

			}

			if (moveOn == true) {
				for (int j = 0; j < playerArmor.size(); j++) {
					if (equipmentCheckBoxes.get(i).getText()
							.equals(playerArmor.get(j))) {
						equipmentCheckBoxes.get(i).setSelected(true);
						moveOn = false;
					}
				}
			}

			if (moveOn == true) {
				for (int j = 0; j < playerGear.size(); j++) {
					if (equipmentCheckBoxes.get(i).getText()
							.equals(playerGear.get(j))) {
						equipmentCheckBoxes.get(i).setSelected(true);
					}
				}
			}

		}
		disableExpensiveEquipment();
		updateReceipt();
	}//end setEquipmentFromPlayer method

	/**
	 * Updates the receipt for the items that the user bought for their character
	 * Called by setEquipmentFromPlayer and buySellEquipment
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void updateReceipt() {

		ArrayList<String> boughtEquipmentNames = new ArrayList<String>();
		ArrayList<String> boughtEquipmentPrices = new ArrayList<String>();

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {

			if (equipmentCheckBoxes.get(i).isSelected() == true) {

				boughtEquipmentNames.add(equipmentCheckBoxes.get(i).getText());

				String price = "";

				int[] value = equipmentPrices.get(i);

				if (value[goldIndex] > 0) {

					price += value[goldIndex] + " gp ";

				}

				if (value[silverIndex] > 0) {

					price += value[silverIndex] + " sp ";

				}

				if (value[copperIndex] > 0) {

					price += value[copperIndex] + " cp ";

				}

				boughtEquipmentPrices.add(price);

			}
		}

		ccp.setCurrentStoreRecipt(characterWallet, boughtEquipmentNames,
				boughtEquipmentPrices);

	}//end updateReceipt method

	/**
	 * Buys and Sells equipment from and to the store
	 * Called by the EventHandler
	 * @param equipmentName
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void buySellEquipment(String equipmentName) {

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {

			if (equipmentCheckBoxes.get(i).getText().equals(equipmentName)) {

				// user is selling equipment, add money to user wallet
				if (equipmentCheckBoxes.get(i).isSelected() == false) {

					// add the gold amount
					moneyTransaction(equipmentPrices.get(i)[goldIndex], "gp");

					// add the silver amount
					moneyTransaction(equipmentPrices.get(i)[silverIndex], "sp");

					// add the copper amount
					moneyTransaction(equipmentPrices.get(i)[copperIndex], "cp");
				} else {

					// user is buying equipment, remove money from user wallet

					// subtract the gold amount
					moneyTransaction((equipmentPrices.get(i)[goldIndex] * -1),
							"gp");

					// subtract the silver amount
					moneyTransaction(
							(equipmentPrices.get(i)[silverIndex] * -1), "sp");

					// subtract the copper amount
					moneyTransaction(
							(equipmentPrices.get(i)[copperIndex] * -1), "cp");

				}//end else

				disableExpensiveEquipment();
				updateReceipt();
			}//end if

		}//end for loop

	}//end buySellEquipment method

	/**
	 * Disables all of the expensive equipment that the user can't afford
	 * Called by setItemPrices, setEquipmentFromPlayer and buySellEquipment
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	private void disableExpensiveEquipment() {

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {

			boolean enableOrDisable = true;

			if (equipmentPrices.get(i)[goldIndex] > characterWallet[goldIndex]) {
				enableOrDisable = false;

			} else if ((equipmentPrices.get(i)[goldIndex] == 0 && characterWallet[goldIndex] == 0)
					&& (equipmentPrices.get(i)[silverIndex] > characterWallet[silverIndex])) {
				enableOrDisable = false;

			} else if ((equipmentPrices.get(i)[goldIndex] == 0 && characterWallet[goldIndex] == 0)
					&& (equipmentPrices.get(i)[silverIndex] == 0 && characterWallet[silverIndex] == 0)
					&& (equipmentPrices.get(i)[copperIndex] > characterWallet[copperIndex])) {
				enableOrDisable = false;
			}

			if (equipmentCheckBoxes.get(i).isSelected() == true) {

				enableOrDisable = true;

			}

			equipmentCheckBoxes.get(i).setEnabled(enableOrDisable);
		}//end for
	}//end disableExpensiveEquipment

	/**
	 * Gets the selected weapons and sends it to the Database Manager
	 * Called by the Database Manager
	 * @return selectedWeapons
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getSelectedWeapons() {
		ArrayList<String> selectedWeapons = new ArrayList<String>();

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {
			if (equipmentCheckBoxes.get(i).isSelected() == true) {

				if (equipmentCheckBoxes.get(i).getName().equals("weapon")) {
					selectedWeapons.add(equipmentCheckBoxes.get(i).getText());
				}
			}
		}

		return selectedWeapons;
	}//end getSelectedWeapons method

	/**
	 * Gets the selected armor and sends it to the Database Manager
	 * Called by the Database Manager
	 * @return selectedArmor
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getSelectedArmor() {
		ArrayList<String> selectedArmor = new ArrayList<String>();
		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {
			if (equipmentCheckBoxes.get(i).isSelected() == true) {

				if (equipmentCheckBoxes.get(i).getName().equals("armor")) {
					selectedArmor.add(equipmentCheckBoxes.get(i).getText());
				}
			}
		}
		return selectedArmor;
	}//end getSelectedArmor method

	/**
	 * Gets the selected gear and sends it to the Database Manager
	 * Called by the Database Manager
	 * @return selectedGear
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
	 */
	public ArrayList<String> getSelectedGear() {
		ArrayList<String> selectedGear = new ArrayList<String>();

		for (int i = 0; i < equipmentCheckBoxes.size(); i++) {
			if (equipmentCheckBoxes.get(i).isSelected() == true) {

				if (equipmentCheckBoxes.get(i).getName().equals("gear")) {
					selectedGear.add(equipmentCheckBoxes.get(i).getText());
				}//end inner if
			}//end outer if
		}//end for
		return selectedGear;
	}//end getSelectedGear method

	/**
	 * The Event Handler handles all the different actions that are performed within the GUI
	 * Called by all ActionListeners
	 * @author Devin Simpson
	 * @version (Most Recent) December 7th, 2014 (Created 9/23/2014)
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

			for (int i = 0; i < equipmentCheckBoxes.size(); i++) {

				if (equipmentCheckBoxes.get(i).getText().equals(command)) {

					buySellEquipment(command);

				}

			}

			if (command == "Confirm Equipment") {

				String equipmentSelect = "";
				ArrayList<String> selectedWeapons = new ArrayList();
				ArrayList<String> selectedArmor = new ArrayList();
				ArrayList<String> selectedGear = new ArrayList();

				// Checks Checkboxes for Weapon List
				for (int i = 0; i < equipmentCheckBoxes.size(); i++) {
					if (equipmentCheckBoxes.get(i).isSelected() == true) {

						if (equipmentCheckBoxes.get(i).getName()
								.equals("weapon")) {
							selectedWeapons.add(equipmentCheckBoxes.get(i)
									.getText());
						} else if (equipmentCheckBoxes.get(i).getName()
								.equals("armor")) {
							selectedArmor.add(equipmentCheckBoxes.get(i)
									.getText());
						} else if (equipmentCheckBoxes.get(i).getName()
								.equals("gear")) {
							selectedGear.add(equipmentCheckBoxes.get(i)
									.getText());
						}
						equipmentSelect += equipmentCheckBoxes.get(i).getText()
								+ "\n";

					}
				}

				if (equipmentSelect.equals("")) {
					JOptionPane
							.showMessageDialog(
									null,
									"No weapons have been selected! You must select at least one weapon before proceeding",
									"No Equipment", JOptionPane.ERROR_MESSAGE);

				} else {

					int confirm = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to use this equipment?\n"
									+ equipmentSelect, "Confirm Equipment",
							JOptionPane.YES_NO_OPTION);

					// if user uses yes option to confirm, run the confirm
					if (confirm == JOptionPane.YES_OPTION) {
						dm.setCharacter_Equipment(selectedWeapons,
								selectedArmor, selectedGear,
								characterWallet[goldIndex],
								characterWallet[silverIndex],
								characterWallet[copperIndex]);
						try {
							ccp.handleAttributeConfirmations(command);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}//end catch

					}//end if yes
				}//end else
			}//end if confirm equipment
		}//end actionPerformed
	}//end eventHandler
}//end ControlPanel_Equipment class