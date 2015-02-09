package DescriptionPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.Character_Creation_Panel;
import main_panels.Database_Manager;

/**
 * DescriptionPanel_Equipment:
 * This class is used for the creation of the panel that shows the descriptions of the equipment
 * available to the user.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_Equipment extends JPanel {

	private ArrayList<int[]> weaponPrices, armorPrices, gearPrices;

	private static JTextArea equipmentDescriptionText;
	private static JTextArea storeReceiptText;

	private static Database_Manager dm = null;
	private static ArrayList<String> weaponNames, gearNames, armorNames;
	private static ArrayList<String> weapon_descriptions, armor_descriptions,
			gear_descriptions;

	private Character_Creation_Panel ccp = null;
	private JPanel Text;

	/**
	 * Class Constructor DescriptionPanel_Equipment
	 * Called by Character_Creation_Panel
	 * @author Devin Simpson, Thoams Man, Victor Mancha
	 * @param w
	 * @param h
	 * @param d
	 * @param wep
	 * @param arm
	 * @param ger
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_Equipment(int w, int h, Database_Manager d,
			ArrayList<String> wep, ArrayList<String> arm,
			ArrayList<String> ger, Character_Creation_Panel c) {

		// Initialize size, layout and background of the equipment description
		// panel
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);

		// labeling the panel as the equipment panel, create panel label here
		JLabel panelLabel = new JLabel("Equipment Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);

		// add the label to the panel
		this.add(panelLabel, BorderLayout.NORTH);

		// set up prices from the equipment descriptions
		weaponPrices = new ArrayList<int[]>();
		armorPrices = new ArrayList<int[]>();
		gearPrices = new ArrayList<int[]>();

		// set the list of equipment names and the database manager object
		weaponNames = wep;
		armorNames = arm;
		gearNames = ger;
		dm = d;
		ccp = c;
		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		// get the descriptions of each equipment piece at panel creation
		weapon_descriptions = dm.getWeapon_Description(weaponNames,
				weaponPrices);
		armor_descriptions = dm.getArmor_Description(armorNames, armorPrices);
		gear_descriptions = dm.getGear_Description(gearNames, gearPrices);

		// TEXT AREA
		equipmentDescriptionText = new JTextArea();
		equipmentDescriptionText
				.setText("Welcome to the Store! \n\n Please select the item checkboxes to purchase it. Your Wallet is generated based on your class and background.");
		equipmentDescriptionText.setLineWrap(true);

		// add text area to panel
		equipmentDescriptionText.setEditable(false);
		Text.add(equipmentDescriptionText, BorderLayout.CENTER);

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

		// receipt text area
		storeReceiptText = new JTextArea(17, 3);
		storeReceiptText.setLineWrap(true);
		storeReceiptText.setBorder(getBorderForPanel("Check Out"));

		// add receipt text area to panel with scroll bar

		storeReceiptText.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(storeReceiptText);
		this.add(scrollPane, BorderLayout.SOUTH);

		// send prices to control panel
		ccp.setEquipmentPricesInStore(weaponNames, weaponPrices, armorNames,
				armorPrices, gearNames, gearPrices);

	}//end of class constructor

	/**
	 * Sets the equipmentDescriptionText to the default text 
	 * Called by setEquipment_description
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setDefault() {

		// have a default message for the equipment description panel
		equipmentDescriptionText
				.setText("Welcome to the Store! \n\n Please select the item checkboxes to purchase it. Your Wallet is generated based on your class and background.");
	}//end setDefault method

	/**
	 * Sets the store's receipt
	 * Called by Character_Creation_Panel
	 * @param characterWallet
	 * @param boughtEquipmentNames
	 * @param boughtEquipmentPrices
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setStoreReceiptText(int[] characterWallet,
			ArrayList<String> boughtEquipmentNames,
			ArrayList<String> boughtEquipmentPrices) {

		String fullReceipt = "Current Wallet:\n";
		int goldIndex = 0, silverIndex = 1, copperIndex = 2;

		fullReceipt += characterWallet[goldIndex] + " gp "
				+ characterWallet[silverIndex] + " sp "
				+ characterWallet[copperIndex] + " cp\n";

		fullReceipt += "\nBought Items:\n";

		if (boughtEquipmentNames.size() > 0) {

			for (int i = 0; i < boughtEquipmentNames.size(); i++) {

				fullReceipt += "\n" + boughtEquipmentNames.get(i) + "\nPrice: "
						+ boughtEquipmentPrices.get(i) + "\n";

			}
		}

		storeReceiptText.setText(fullReceipt);
		storeReceiptText.setCaretPosition(0);
	}//end setStoreReceiptText method

	/**
	 * Sets up border for the description panel
	 * Called by Class Constructor
	 * @param name
	 * @return title
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private TitledBorder getBorderForPanel(String name) {

		// create a titled border for the panel
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title;
		title = BorderFactory.createTitledBorder(blackline, name);
		title.setTitleJustification(TitledBorder.CENTER);

		return title;
	}

	/**
	 * @author Devin Simpson
	 * 
	 *         set the text area to the appropriate message depending on what
	 *         description the user action has triggered. if the action results
	 *         in the default message being requested, display it else, display
	 *         the equipment description their mouse is hovering over
	 * 
	 * @param equipment_name
	 * @param equipment_type
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public static void setEquipment_description(String equipment_name,
			String equipment_type) {

		String text = null;

		// check if the description to be displayed is the default message for
		// the equipment description panel
		if (equipment_name.equals("default")) {

			setDefault();

		} else {

			// if it is not, check the equipment type, and display the
			// appropriate message corresponding to the user action

			if (equipment_type.equals("weapon")) {
				for (int i = 0; i < weapon_descriptions.size(); i++) {
					if (weaponNames.get(i).equals(equipment_name)) {

						text = weapon_descriptions.get(i);
					}
				}
			} else if (equipment_type.equals("armor")) {
				for (int i = 0; i < armor_descriptions.size(); i++) {
					if (armorNames.get(i).equals(equipment_name)) {
						text = armor_descriptions.get(i);
					}
				}

			} else if (equipment_type.equals("gear")) {
				for (int i = 0; i < gear_descriptions.size(); i++) {
					if (gearNames.get(i).equals(equipment_name)) {
						text = gear_descriptions.get(i);
					}
				}
			}

			equipmentDescriptionText.setText(text);
		}

	}// end equipment_description method

}// end DescriptionPanel_Equipment class