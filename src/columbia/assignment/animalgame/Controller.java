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
	private static boolean askingQuestion;
	
	public Controller()
	{
		dbHandler = new DatabaseHandlerModel();
		
		//GuessModel[] guesses = new GuessModel[1];
		//guesses[0] = dbHandler.queryForGuessModelByName("YDogGuess");

		
		UIView = new UserInputView();
		UIView.enableButtons();
		UIView.show();

		//UIView.onGuess(guesses[0]);
		
		GLView = new OpenGLView(dbHandler.queryForAllGuessModels());
		//GLView.LoadGuessTexture(guesses[0]);
		

		//sendGuessToUI(guesses[0]);
		//sendGuessToGL(guesses[0]);
		
		//the name of the first question if "VertQ" for VertebrateQuestion
		QuestionModel firstQuestion = dbHandler.queryForQuestionModelByName(
												"VertQ");
		sendQuestionToUI(firstQuestion);
		askingQuestion = true;
		
	}
	
	private void sendGuessToUI(GuessModel guess)
	{
		UIView.onGuess(guess);
		
	}
	private void sendGuessToGL(GuessModel guess)
	{
		GLView.LoadGuessTexture(guess);
	}
	
	private void sendQuestionToUI(QuestionModel question)
	{
		UIView.onQuestion(question);
	}
	
	public static void receiveAnswerFromUI(boolean answer)
	{
		if(answer)
		{
			//we're asking a question
			if(askingQuestion)
			{
				
			}
		}
		if(!answer)
		{
			//act on NO
		}
	}
	
	public static void receiveQuestionAnswerFromUI(boolean answer, QuestionModel question)
	{
		
	}
	public static void receiveGuessAnswerFromUI(boolean answer, GuessModel guess)
	{
		
	}
}
