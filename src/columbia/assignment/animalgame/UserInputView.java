package columbia.assignment.animalgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserInputView
{
	/** The main JFrame */
	private JFrame uiframe;
	/** Content panel for the JFrame */
	private JPanel uipanel;
	
	
	/** Text label displaying the question at the top */
	private JLabel textLabel;
	
	/** Buttons populating the window */
	private JButton yesButton, noButton;
	/** Images for the ImageIcons that go in the buttons */
	private BufferedImage columbiaImage;
	private BufferedImage yesButtonImage;
	private BufferedImage noButtonImage;
	
	private ImageIcon     winIcon;
	private ImageIcon     loseIcon;
	
	//could use a boolean, but this is more intuitive
	/** represents the current state of the UI, whether its guessing or asking a question */
	private static int CURRENT_STATE = 0;
	private static final int GUESS = 1, QUESTION = 2;
	
	private GuessModel    currentGuess    = null;
	private QuestionModel currentQuestion = null;
	
	/**
	 * CTOR
	 */
	public UserInputView()
	{
		//construct the JFrame
		uiframe = new JFrame("GO COLUMBIA!");
		//located at screen position (150, 150) by default, width of 200, height 500
		uiframe.setBounds(new Rectangle(800, 150, 200, 500));
		//non-resizable
		uiframe.setResizable(false);
		
		//construct the JPanel
		uipanel = new JPanel();
		//a text display to the user
		textLabel = new JLabel();
		//the <HTML> </HTML> tags make the text wrap
		textLabel.setText("<HTML>Welcome to Animal Game, a Columbia assignment.</HTML>");
		//Yes/No buttons for user interaction
		//The text uses HTML formatting
		yesButton = new JButton("<HTML><p style = 'font-size:14px;color:red'>Yes</p></HTML>");
		noButton = new JButton( "<HTML><p style = 'font-size:14px;color:blue'>No</p></HTML>");
		
		/*
		 * Add ActionListeners to the buttons.
		 * They call Controller.receiveAnswerFromUI,
		 * passing either true or false.
		 * In this way Controller can know when the user has answered and
		 * and can proceed
		 */
		yesButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(CURRENT_STATE == 0)
					System.err.println("ERROR:  CURRENT STATE IS 0");
				else if(CURRENT_STATE == GUESS)
				{
					//this should not happen
					if(currentGuess == null)
						throw new NullPointerException("ERROR:  CURRENT STATE IS GUESS BUT currentGuess IS NULL!");
					
					Controller.receiveGuessAnswerFromUI(true, currentGuess);
				}
				else if(CURRENT_STATE == QUESTION)
				{
					if(currentQuestion == null)
						throw new NullPointerException("ERROR:  CURRENT STATE IS QUESTION BUT currentQuestion IS NULL!");
					
					Controller.receiveQuestionAnswerFromUI(true, currentQuestion);
				}
				else
				{
					//this should never execute
					System.err.println("ERROR:  UNKNOWN STATE");
				}
			}
		}
		);
		noButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(CURRENT_STATE == 0)
					System.err.println("ERROR:  CURRENT STATE IS 0");
				else if(CURRENT_STATE == GUESS)
				{
					Controller.receiveGuessAnswerFromUI(false, currentGuess);
				}
				else if(CURRENT_STATE == QUESTION)
				{
					Controller.receiveQuestionAnswerFromUI(false, currentQuestion);
				}
				else
				{
					//this should never execute
					System.err.println("ERROR:  UNKNOWN STATE");
				}
			}
		}
		);
		
		/*
		 * Add icons to the buttons
		 */
		try {
			//yes button gets a thumbs up, no button gets a thumbs down
		yesButtonImage = ImageIO.read(new BufferedInputStream(
									new FileInputStream("Data/Icons/thumbsup.png")));
		noButtonImage = ImageIO.read(new BufferedInputStream(
									new FileInputStream("Data/Icons/thumbsdown.png")));
		
		} catch(IOException ioe)
		{
			//handle IOException
			ErrorHandler.handleException(ioe);
		}
		//create the ImageIcon objects from the BufferedImage's
		ImageIcon yesButtonIcon = new ImageIcon(yesButtonImage);
		ImageIcon noButtonIcon = new  ImageIcon(noButtonImage);
		//and add them to the buttons
		yesButton.setIcon(yesButtonIcon);
		noButton.setIcon(noButtonIcon);
		
		//Box is a JComponent using the BoxLayout layout manager
		Box box = Box.createVerticalBox();

		//grey out the buttons by default
		yesButton.setEnabled(false);
		noButton.setEnabled(false);
		
		//add the labels and buttons to the Box
		box.add(textLabel);
		//add separators
		box.add(Box.createRigidArea(new Dimension(0, 30)));
		box.add(yesButton);
		box.add(Box.createRigidArea(new Dimension(0, 15)));
		box.add(noButton);
		box.add(Box.createRigidArea(new Dimension(0, 130)));
		
		
		/*
		 * Loads an the image columbia.png, places it in a JLabel, and adds that
		 * JLabel to our Box
		 */
		try {
		columbiaImage = ImageIO.read(new BufferedInputStream(
											new FileInputStream("Data/Icons/columbia.png")));
		}
		catch(IOException ioe)
		{
			ErrorHandler.handleException(ioe);
		}
		ImageIcon imgicon = new ImageIcon(columbiaImage);
		JLabel imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(100, 100));
		imageLabel.setIcon(imgicon);
		box.add(imageLabel);
		
		/*
		 * 
		 */
		try {
		BufferedImage winImage = ImageIO.read(new BufferedInputStream(
				new FileInputStream("Data/Icons/youwin.png")));
		BufferedImage loseImage = ImageIO.read(new BufferedInputStream(
				new FileInputStream("Data/Icons/youlose.png")));
		
		winIcon = new ImageIcon(winImage);
		loseIcon = new ImageIcon(loseImage);
		}
		catch(IOException ioe)
		{
			
		}
		
		//add the JComponent holding all our other components
		uipanel.add(box);
		//white background
		uipanel.setBackground(Color.WHITE);

		uiframe.setContentPane(uipanel);
	}
	
	//Un-grey out buttons
	public void enableButtons()
	{
		yesButton.setEnabled(true);
		noButton.setEnabled(true);
	}
	//Grey out buttons
	public void disableButtons()
	{
		yesButton.setEnabled(false);
		noButton.setEnabled(false);
	}
	
	//Show the frame
	public void show()
	{
		uiframe.setVisible(true);
	}
	
	//called when we need to guess
	public void onGuess(GuessModel guess)
	{			
		String animal_name = guess.getAnimalName();
		textLabel.setText("<HTML><p style = 'font-size:13px'>Is it a " + animal_name + "?</p></HTML>");
		//set state
		CURRENT_STATE = GUESS;
		currentQuestion = null;
		currentGuess = guess;
	}
	
	/**
	 * Called when we need to ask the user a question
	 * @param question
	 */
	public void onQuestion(QuestionModel question)
	{
		textLabel.setText("<HTML><p style = 'font-size:13px'>"+question.getQuestion()+"</p></HTML>");
		//set state
		CURRENT_STATE = QUESTION;
		currentGuess = null;
		currentQuestion = question;
		
	}
	
	/**
	 * Called when the program should end, either because
	 * we ran out of guesses or we got it correct
	 * @param computer_win True if we guessed correctly, false otherwise
	 */
	public void onProgramEnd(boolean computer_win)
	{
		if(!computer_win)
		{
			JOptionPane.showMessageDialog(uipanel, "GAME OVER - YOU WIN!", "VICTORY",
					JOptionPane.INFORMATION_MESSAGE, winIcon);
		}
		else
		{
			JOptionPane.showMessageDialog(uipanel, "Better luck next time", "Defeat...",
					JOptionPane.INFORMATION_MESSAGE, loseIcon);
		}
		System.exit(0);
	}
}


