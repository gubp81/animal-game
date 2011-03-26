package columbia.assignment.animalgame;


/**
 * 
 * @author thomas
 *
 */
public class Controller 
{
	private UserInputView UIView;
	
	public Controller()
	{
		UIView = new UserInputView();
		UIView.show();

		sendGuessToUI(new GuessModel("PATH", "NAME"));
	}
	
	private void sendGuessToUI(GuessModel guess)
	{
		UIView.onGuess(guess);
	}
	
	public void receiveAnswerFromUI(boolean answer)
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
