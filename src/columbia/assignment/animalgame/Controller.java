package columbia.assignment.animalgame;


/**
 * 
 * @author thomas
 *
 */
public class Controller 
{
	private static UserInputView UIView;
	private static OpenGLView    GLView;
	private static DatabaseHandlerModel dbHandler;
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
	
	private static void sendGuessToUI(GuessModel guess)
	{
		UIView.onGuess(guess);
		
	}
	private static void sendGuessToGL(GuessModel guess)
	{
		GLView.LoadGuessTexture(guess);
	}
	
	private static void sendQuestionToUI(QuestionModel question)
	{
		UIView.onQuestion(question);
	}
	
	private static void sendQuestionToGL()
	{
		GLView.LoadQuestionTexture();
	}
	/*
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
	*/
	
	/**
	 * Called when receiving the answer to a question from the user
	 * @param answer
	 * @param question
	 */
	public static void receiveQuestionAnswerFromUI(boolean answer, QuestionModel question)
	{
		if(answer)
		{
			//proceed to the next guess
			if(question.getNameOfYesGuess() != null)
			{
				GuessModel guess = dbHandler.queryForGuessModelByName(question.getNameOfYesGuess());
				sendGuessToUI(guess);
				sendGuessToGL(guess);
			}
			else
			{
				System.err.println("QUESTION IS NULL");
				UIView.onProgramEnd(answer);
			}
		}
		else
		{
			if(question.getNameOfNoGuess() != null)
			{
				GuessModel guess = dbHandler.queryForGuessModelByName(question.getNameOfNoGuess());
				sendGuessToUI(guess);
				sendGuessToGL(guess);
			}
			else
			{
				System.err.println("QUESTION IS NULL");
				UIView.onProgramEnd(answer);
			}
		}
	}
	
	
	public static void receiveGuessAnswerFromUI(boolean answer, GuessModel guess)
	{
		if(answer)
		{
			UIView.onProgramEnd(answer);
		}
		else
		{
			if(guess.getNameOfNextQuestion() != null)
			{
				System.err.println("NAME OF NEXT GUESS QUESTION: " + guess.getNameOfNextQuestion());
				UIView.onQuestion(dbHandler.queryForQuestionModelByName(
										guess.getNameOfNextQuestion()));
			}
			else
			{
				UIView.onProgramEnd(answer);
			}
			
		}
		sendQuestionToGL();
	}
}
