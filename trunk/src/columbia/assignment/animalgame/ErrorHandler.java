package columbia.assignment.animalgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Handles errors or Exceptions that occur during program execution
 * @author thomas
 *
 */
public class ErrorHandler
{

	private static File       appDir;
	private static File       errorFilePath;
	private static FileWriter errorFile;
	
	
	static
	{
		//initialize to current application directory
		appDir = new File(System.getProperty("user.dir"));
		
		//initialize the File object that will be used to write
		//the error text file if an unrecoverable exception is thrown
		errorFilePath = new File(appDir, "ERROR.txt");
	}
	

	/**
	 * Handles an unrecoverable exception by giving the user an error dialog
	 * and printing the Exception's message to a file
	 * @param e
	 */
	public static void handleException(Exception e)
	{
		String message = e.getMessage();
		JOptionPane.showMessageDialog(null,"EXCEPTION: " + message + "\nSee ERROR.txt for backtrace.");
		try {
		errorFile = new FileWriter(errorFilePath);
		errorFile.write("CAUGHT EXCEPTION");
		errorFile.write(message);
		} catch(IOException ioe) { }
		finally { 
			
			try {
				errorFile.close();
				}
			//ignore
			catch(Throwable t)
			{ }
		}

			
	}

}
