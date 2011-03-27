package columbia.assignment.animalgame;

import java.io.File;

/**
 * Class encapsulating the data of a Guess
 * Holds an object of class Animal, which is the animal the program guesses
 * @author thomas
 *
 */
public class GuessModel
{
	private Animal animal;
	private String NameOfGuess;
	private String NameOfNextQuestion;
	
	public GuessModel(File path, String name, String NONQ, String NOG)
	{
		animal = new Animal(path, name);
		NameOfNextQuestion = NONQ;
		NameOfGuess = NOG;
	}
	public GuessModel(String path, String name, String NONQ, String NOG)
	{
		animal = new Animal(new File(path), name);
		NameOfNextQuestion = NONQ;
		NameOfGuess = NOG;
	}
	
	public File   getpathToTexture()   { return animal.getFile();   }
	public String getAnimalName()      { return animal.getName();   }
	public String getNameOfNextQuestion() { return NameOfNextQuestion; }
	public String getNameOfGuess() { return NameOfGuess; }

}

/**
 * Encapsulates an animal, holding a path to the animal's texture
 * and the animal's name
 * @author thomas
 *
 */
class Animal
{
	private File   pathToTexture;
	private String name;
	
	public Animal(File path, String Name)
	{
		pathToTexture = path;
		name = Name;
	}
	public File getFile() { return pathToTexture; }
	public String getName() { return name; }
}