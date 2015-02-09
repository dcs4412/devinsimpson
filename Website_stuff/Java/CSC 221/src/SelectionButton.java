import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;

/**
 * selection buttons are JButtons that populate the bottom of the creation panel
 * and allow a user to select between and view all their created
 * classes,represented by the name on the button
 * 
 * @author Devin Simpson
 * 
 */
public class SelectionButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String myName;
	private static Font f = new Font("Arial", Font.BOLD, 20);

	/**
	 * constructor for the selection buttons
	 * 
	 * @param name
	 */
	public SelectionButton(String name) {
		myName = name;
		setFont(f);
		setText(myName);
		setPreferredSize(new Dimension(12, 35));

	}

	/**
	 * Return the letter that appears on this tile
	 * 
	 * @return The letter that appears on this tile
	 */
	public String getName() {
		return myName;
	}

	
}
