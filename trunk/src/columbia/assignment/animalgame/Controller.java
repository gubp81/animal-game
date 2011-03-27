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
	private DatabaseHandlerModel dbHandler;
	
	public Controller()
	{
		dbHandler = new DatabaseHandlerModel();
		
		GuessModel[] guesses = new GuessModel[1];
		guesses[0] = dbHandler.queryForGuessModelByName("YDogGuess");

		
		UIView = new UserInputView();
		UIView.enableButtons();
		UIView.show();

		UIView.onGuess(guesses[0]);
		
		GLView = new OpenGLView(dbHandler.queryForAllGuessModels());
		GLView.LoadGuessTexture(guesses[0]);
		

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
