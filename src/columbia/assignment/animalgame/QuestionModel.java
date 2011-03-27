package columbia.assignment.animalgame;

public class QuestionModel 
{
	private String nameOfQuestion, Question, NameOfYesGuess, NameOfNoGuess;
	
	public QuestionModel(String NOQ, String Q, String NOYG, String NONG)
	{
		nameOfQuestion = NOQ;
		Question = Q;
		NameOfYesGuess = NOYG;
		NameOfNoGuess = NONG;
	}
	
	public String getnameOfQuestion() { return nameOfQuestion; }
	public String getQuestion() { return Question; }
	public String getNameOfYesGuess() { return NameOfYesGuess; }
	public String getNameOfNoGuess() { return NameOfNoGuess; }
	
	
}