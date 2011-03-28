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
	
	public Controller()
	{
		//initialize the database handler
		dbHandler = new DatabaseHandlerModel();

		
		//initialize and show the UI
		UIView = new UserInputView();
		UIView.enableButtons();
		UIView.show();

		
		//initialize the OpenGL window, passing an array of all GuessModel's
		//so it can load textures
		GLView = new OpenGLView(dbHandler.queryForAllGuessModels());

		
		//the name of the first question if "VertQ" for VertebrateQuestion
		QuestionModel firstQuestion = dbHandler.queryForQuestionModelByName(
												"VertQ");
		sendQuestionToUI(firstQuestion);
		
	}
	
	/**
	 * sends a guess to the UI window
	 * @param guess
	 */
	private static void sendGuessToUI(GuessModel guess)
	{
		UIView.onGuess(guess);
		
	}
	
	/**
	 * sends a guess to the OpenGL window
	 * @param guess
	 */
	private static void sendGuessToGL(GuessModel guess)
	{
		GLView.LoadGuessTexture(guess);
	}
	
	/**
	 * sends a question to the UI window
	 * @param question
	 */
	private static void sendQuestionToUI(QuestionModel question)
	{
		UIView.onQuestion(question);
	}
	
	/**
	 * sends a question to the OpenGL window
	 */
	private static void sendQuestionToGL()
	{
		GLView.LoadQuestionTexture();
	}

	
	/**
	 * Called when receiving the answer to a question from the user
	 * @param answer
	 * @param question
	 */
	public static void receiveQuestionAnswerFromUI(boolean answer, QuestionModel question)
	{
		//if they answered yes...
		if(answer)
		{
			//proceed to the next guess
			if(question.getNameOfYesGuess() != null)
			{
				//query for the next guess
				GuessModel guess = dbHandler.queryForGuessModelByName(question.getNameOfYesGuess());
				//and send it to the UI and OpenGL
				sendGuessToUI(guess);
				sendGuessToGL(guess);
			}
			else
			{
				UIView.onProgramEnd(answer);
			}
		}
		//if they answered no...
		else
		{
			if(question.getNameOfNoGuess() != null)
			{
				//same
				GuessModel guess = dbHandler.queryForGuessModelByName(question.getNameOfNoGuess());
				sendGuessToUI(guess);
				sendGuessToGL(guess);
			}
			else
			{
				UIView.onProgramEnd(answer);
			}
		}
	}
	
	
	/**
	 * called by UI on receiving an answer
	 * @param answer
	 * @param guess
	 */
	public static void receiveGuessAnswerFromUI(boolean answer, GuessModel guess)
	{
		//if yes...
		if(answer)
		{
			//we guessed correctly and the program ends
			UIView.onProgramEnd(answer);
		}
		//otherwise no
		else
		{
			//get the name of the next question
			if(guess.getNameOfNextQuestion() != null)
			{
				//and send it to the UI
				UIView.onQuestion(dbHandler.queryForQuestionModelByName(
										guess.getNameOfNextQuestion()));
			}
			else
			{
				UIView.onProgramEnd(answer);
			}
			
		}
		//send the question to OpenGL
		sendQuestionToGL();
	}
}
