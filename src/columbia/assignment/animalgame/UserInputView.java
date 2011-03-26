package columbia.assignment.animalgame;

import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserInputView
{
	private JFrame uiframe;
	private JPanel uipanel;
	
	private JLabel textLabel;
	
	private JButton yesButton, noButton;
	
	
	/**
	 * CTOR
	 */
	public UserInputView()
	{
		//construct the JFrame
		uiframe = new JFrame("GO COLUMBIA!");
		//located at screen position (150, 150) by default, width of 200, height 500
		uiframe.setBounds(new Rectangle(150, 150, 200, 500));
		//non-resizable
		uiframe.setResizable(false);
		
		//construct the JPanel
		uipanel = new JPanel();
		//a text display to the user
		textLabel = new JLabel();
		//the <HTML> </HTML> tags make the text wrap
		textLabel.setText("<HTML>Welcome to Animal Game, a Columbia assignment.</HTML>");
		
		yesButton = new JButton("Yes");
		noButton = new JButton( "No");
		
		BoxLayout boxLayout = new BoxLayout(uipanel, BoxLayout.Y_AXIS);
		//boxLayout.
		uipanel.setLayout(boxLayout);
		
		yesButton.setEnabled(false);
		noButton.setEnabled(false);
		
		uipanel.add(textLabel);
		uipanel.add(yesButton);
		uipanel.add(noButton);
		

		
		uiframe.setContentPane(uipanel);
	}
	
	
	
	public void show()
	{
		uiframe.setVisible(true);
	}
	
	public void onGuess(GuessModel guess)
	{
		String animal_name = guess.getAnimalName();
		textLabel.setText("Is it a " + animal_name + "?");
	}
	

}
