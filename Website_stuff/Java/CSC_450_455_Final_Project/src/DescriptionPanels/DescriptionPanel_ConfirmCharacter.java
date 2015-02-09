package DescriptionPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main_panels.CharacterSheetPrintPreview;
import main_panels.Character_Creation_Panel;



import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * DescriptionPanel_Abilities:
 * This class is used for the creation of the panel that shows the description of 
 * the confirmed character.
 * This class is one of 8 panels that are used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class DescriptionPanel_ConfirmCharacter extends JPanel {

	private int width, height;
	private JPanel Text;
	private JTextArea comfirmDescriptionText; //text area for the confirmed character's description
	private Character_Creation_Panel ccp = null; //Character Creation Panel reference
	EventHandler eh = new EventHandler(); //Handles Events

	/**
	 * Class constructor for DescriptonPanel_ConfirmCharacter
	 * Called by Character_Creation_Panel
	 * @param w
	 * @param h
	 * @param c
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public DescriptionPanel_ConfirmCharacter(int w, int h,
			Character_Creation_Panel c) {
		this.setSize(w, h);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.blue);
		JLabel panelLabel = new JLabel("Confirm Character Description Panel",
				JLabel.CENTER);
		panelLabel.setOpaque(true);
		panelLabel.setBackground(Color.lightGray);
		this.add(panelLabel, BorderLayout.NORTH);

		ccp = c;

		Text = new JPanel();
		Text.setLayout(new BorderLayout());
		this.add(Text, BorderLayout.CENTER);

		comfirmDescriptionText = new JTextArea();

		comfirmDescriptionText
				.setText("A Print Preview of your D&D Character Sheet \n Click the Print Buttom below to print it \n You can also edit your character and make any changes.");

		comfirmDescriptionText.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(comfirmDescriptionText);
		comfirmDescriptionText.setEditable(false);
		Text.add(comfirmDescriptionText, BorderLayout.CENTER);

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

		JButton printCharacterSheet = new JButton("Print Character Sheet");
		printCharacterSheet.addActionListener(eh);
		this.add(printCharacterSheet, BorderLayout.SOUTH);
	}//end class constructor

	/**
	 * Document:
	 * Sets up the character sheet as a document and prints it
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private class Document implements Printable {

		private Graphics charSheet;

		/**
		 * Turns the Graphics into a document
		 * Called by print
		 * @param g
		 * @author Devin Simpson
		 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
		 */
		public Document(Graphics g) {

			charSheet = g;

		}//end public document

		/**
		 * Method: print
		 * <p>
		 * 
		 * @param g
		 *            a value of type Graphics
		 * @param pageFormat
		 *            a value of type PageFormat
		 * @param page
		 *            a value of type int
		 * @return a value of type int
		 * @author Devin Simpson
		 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
		 */
		public int print(Graphics g, PageFormat pageFormat, int page) {

			// --- Create the Graphics2D object
			Graphics2D g2d = (Graphics2D) charSheet;

			// --- Translate the origin to 0,0 for the top left corner
			g2d.translate(pageFormat.getImageableX(),
					pageFormat.getImageableY());

			// --- Set the drawing color to black
			g2d.setPaint(Color.black);

			// --- Validate the page
			return (PAGE_EXISTS);

		}//end print method
	}//end private class

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

			if (command == "Print Character Sheet") {

				CharacterSheetPrintPreview printCharacter = ccp.getCharacterSheetGraphics();

				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(printCharacter);
				boolean ok = job.printDialog();
				if (ok) {
					try {
						job.print();
					} catch (PrinterException ex) {
						/* The job did not successfully complete */
					}//end catch
				}//if ok
			}//if print character sheet
		}//end actionPerformed
	}//end eventHandler
}//end class DescriptionPanel_ConfirmCharacter