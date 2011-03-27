package columbia.assignment.animalgame;


/**
 * 
 * @author thomas
 *
 */
public class Controller 
{
	private UserInputView UIView;
	private OpenGLView    GLView;
	
	public Controller()
	{
		GuessModel[] guesses = new GuessModel[3];
		guesses[0] = new GuessModel("Data/dog.png", "Dog");
		guesses[1] = new GuessModel("Data/rabbit.png", "Rabbit");
		
		
		UIView = new UserInputView();
		UIView.enableButtons();
		UIView.show();

		UIView.onGuess(guesses[0]);
		
		GLView = new OpenGLView(guesses);
		//GLView.LoadGuessTexture(guesses[0]);
		

		sendGuessToUI(guesses[1]);
		sendGuessToGL(guesses[1]);
	}
	
	private void sendGuessToUI(GuessModel guess)
	{
		UIView.onGuess(guess);
		
	}
	private void sendGuessToGL(GuessModel guess)
	{
		GLView.LoadGuessTexture(guess);
	}
	
	public static void receiveAnswerFromUI(boolean answer)
	{
		if(answer)
		{
			//act on YES
		}
		if(!answer)
		{
			//act on NO
		}
	}
}
