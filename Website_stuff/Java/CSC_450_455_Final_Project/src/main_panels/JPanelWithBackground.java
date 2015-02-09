package main_panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * JPanelWithBackground:
 * This class is used for the background of all of the character attribute panels
 * This class is one of 7 classes of the main panels used in the creation of a new D&D character.
 * @author Thomas Man
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class JPanelWithBackground extends JPanel {

	// the image to apply to the JPanel
	private Image backgroundImage;

	/**
	 * @author Thomas Man
	 * 
	 *         Constructor for the JPanelWithBackground class, a file name of an
	 *         image is passed as an argument, and the image is read into the
	 *         Image variable
	 * 
	 * @param fileName
	 * @throws IOException
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public JPanelWithBackground(String fileName) throws IOException {
		
		//insert the images directory to the front of the file name
		String fullPath =  "./Images/" + fileName;
		
		// read in the image using the file name
		backgroundImage = ImageIO.read(new java.io.File(fullPath));

	}// end JPanelWithBackground method

	/**
	 * @author Thomas Man
	 * 
	 *         Overwrite the JPanel paintComponent to include drawing the
	 *         background image
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void paintComponent(Graphics g) {

		// draw the JPanel Components normally
		super.paintComponent(g);

		// draw the image to the JPanel background
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}// end paintComponent method

}// end JPanelWithBackground class