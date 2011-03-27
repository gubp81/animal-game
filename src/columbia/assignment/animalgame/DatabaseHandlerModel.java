package columbia.assignment.animalgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


/**
 * Using an SQLite database to manage the data/Model side of the application
 * Relies on JDBC SQLite (sqlitejdbc-v056.jar), a Java wrapper over the public domain in-process database
 * manager SQLite
 * @author thomas
 *
 */
public class DatabaseHandlerModel 
{
	//name of the database
	public static final String DatabaseName = "Database/AnimalGame.db";
	
	//create table statements
	//private static final String createQuestionTable = "CREATE TABLE QuestionTable (ID INTEGER PRIMARY KEY AUTOINCREMENT  ,Name TEXT, Question TEXT, NameOfYesGuess TEXT, NameOfNoGuess TEXT)";
	//private static final String createGuessTable    = "CREATE TABLE GuessTable    (ID INTEGER PRIMARY KEY AUTOINCREMENT  ,Name TEXT, Animal TEXT, NameOfNextQuestion TEXT)";
	
	/** class holding constants for the names of the columns of the QuestionTable */
	public static final class QuestionTableColumnNames
	{
		public static final String ID="ID", Name="Name", Question="Question", NameOfYesGuess="NameOfYesGuess",
									NameOfNoGuess="NameOfNoGuess";
	}
	/** class holding constants for the names of the columns of the GuessTable */
	public static final class GuessTableColumnName
	{
		public static final String ID="ID", Name="Name", Animal="Animal", NameOfNextQuestion="NameOfNextQuestion", Path="Path";
	}
	
	private Connection databaseConnection;
	
	public DatabaseHandlerModel()
	{
		try {
			//makes sure the driver is available
		Class.forName("org.sqlite.JDBC");
		
		//get the JDBC connection
	    databaseConnection =
	      DriverManager.getConnection("jdbc:sqlite:Database/AnimalGame.db");

	    //create the 2 tables
		//Statement createTableStatement = databaseConnection.createStatement();
		//createTableStatement.executeUpdate(createQuestionTable);
		//createTableStatement.executeUpdate(createGuessTable);
		}
		catch(SQLException sqle)
		{
			//for our purposes, we don't really need to do any serious
			//SQLException handling (such as getting references to chain exceptions)
			ErrorHandler.handleException(sqle);
		}
		catch(ClassNotFoundException cnfe)
		{
			ErrorHandler.handleException(cnfe);
		}
	}
	
	public GuessModel queryForGuessModelByName(String guessName)
	{
		PreparedStatement queryStatement = null;
		try {
		String query = "SELECT Name, Animal, NameOfNextQuestion, Path FROM GuessTable WHERE Name=?";
		queryStatement = databaseConnection.prepareStatement(query);
		queryStatement.setString(1, guessName);
		ResultSet rs = queryStatement.executeQuery();
		
		if(rs.isClosed())
			return null;
		
		
		String Name = rs.getString("Name");
		String Animal = rs.getString("Animal");
		String NameOfNextQuestion = rs.getString("NameOfNextQuestion");
		String Path = rs.getString("Path");
		//guarantee null instead of an empty string
		if(NameOfNextQuestion != null)
		{
			if(NameOfNextQuestion.equals(""))
				NameOfNextQuestion = null;
		}
		
		return new GuessModel(Path, Animal, NameOfNextQuestion, Name);
		}
		catch(SQLException sqle)
		{
			ErrorHandler.handleException(sqle);
		}
		finally
		{
			if(queryStatement != null)
			{
				try { queryStatement.close(); } 
				catch(Exception e) { ErrorHandler.handleException(e); }
			}
		}
		return null;
	}
	
	public GuessModel[] queryForAllGuessModels()
	{
		Vector<GuessModel> guess_model_vec = new Vector<GuessModel>();
		
		PreparedStatement queryStatement = null;
		try {
		String query = "SELECT Name, Animal, NameOfNextQuestion, Path FROM GuessTable";
		queryStatement = databaseConnection.prepareStatement(query);
		ResultSet rs = queryStatement.executeQuery();
			while(rs.next())
			{
				String Name = rs.getString("Name");
				String Animal = rs.getString("Animal");
				String NameOfNextQuestion = rs.getString("NameOfNextQuestion");
				String Path = rs.getString("Path");
				//guarantee null instead of an empty string
				if(NameOfNextQuestion != null)
				{
					if(NameOfNextQuestion.equals(""))
						NameOfNextQuestion = null;
				}
				guess_model_vec.addElement(new GuessModel(Path, Animal, NameOfNextQuestion, Name));
			}
			guess_model_vec.trimToSize();
			return (GuessModel[]) guess_model_vec.toArray(new GuessModel[guess_model_vec.size()]);
		}
		catch(SQLException sqle)
		{
			ErrorHandler.handleException(sqle);
		}
		finally
		{
			if(queryStatement != null)
			{
				try { queryStatement.close(); } 
				catch(Exception e) { ErrorHandler.handleException(e); }
			}
		}
		return null;
	}
	
	public QuestionModel queryForQuestionModelByName(String QUESTION_NAME)
	{
		String query = "SELECT Name, Question, NameOfYesGuess, NameOfNoGuess FROM QuestionTable WHERE Name=?";
		PreparedStatement queryStatement = null;
		try
		{
			queryStatement = databaseConnection.prepareStatement(query);
			queryStatement.setString(1, QUESTION_NAME);
			ResultSet rs = queryStatement.executeQuery();
			String Name = rs.getString("Name");
			String Question = rs.getString("Question");
			String NameOfYesGuess = rs.getString("NameOfYesGuess");
			String NameOfNoGuess = rs.getString("NameOfNoGuess");
			if(NameOfYesGuess != null)
			{
				if(NameOfYesGuess.equals(""))
					NameOfYesGuess = null;
			}
			if(NameOfNoGuess != null)
			{
				if(NameOfNoGuess.equals(""))
					NameOfNoGuess = null;
			}
			return new QuestionModel(Name, Question, NameOfYesGuess, NameOfNoGuess);
		}
		catch(SQLException sqle)
		{
			ErrorHandler.handleException(sqle);
		}
		finally
		{
			if(queryStatement != null)
			{
				try {
					queryStatement.close();
				}
				catch(Throwable t)
				{ /* ignore */ }
			}
		}
		return null;
	}
}
