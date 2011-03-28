package columbia.assignment.animalgame;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JTextArea;


/**
 * Go Columbia!
 * @author thomas
 *
 */
public class Main
{
	public static void main(String[] args)
	{
		//launch the Controller and IntroMessage
		new Controller();
		new IntroMessage();
	}
}

/**
 * Instructions and information to display to the user
 * This could alternatively be displayed on a menu item selection
 * @author thomas
 *
 */
class IntroMessage
{
	public IntroMessage()
	{
		JFrame frame = new JFrame("Welcome");
		JTextArea textArea = new JTextArea(welcomeMessage);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.setContentPane(textArea);
		frame.setBounds(new Rectangle(1050, 150, 200, 200));
		frame.setVisible(true);
	}
	private String welcomeMessage =
		"Welcome to Animal Game, a Columbia Computer Science assignment.  Think of an animal," +
		" and I will try to guess it.  If I can't guess it with my questions, you win." +
		"\nWritten by Thomas Jakway, March 2011. " +
		" Uses JDBC SQLite and JoGL v1.1  (http://opengl.j3d.org/licenses/sun-BSD.html)";
		
}