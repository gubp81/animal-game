package columbia.assignment.animalgame;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class XMLHandlerModel
{
	
	private DocumentBuilderFactory builderFactory;
	private DocumentBuilder docBuilder;
	private Document        Doc;
	
	XMLHandlerModel()
	{
		DocumentBuilderFactory builderFactory =
	        DocumentBuilderFactory.newInstance();
	    
	try {
	    DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
	} catch (ParserConfigurationException e) {
	    ErrorHandler.handleException(e);
	}
	
	try {
	    Doc = docBuilder.parse(
	            new FileInputStream("Data/data.xml"));
	} catch (SAXException e) {
	    ErrorHandler.handleException(e);
	} catch (IOException e) {
	    ErrorHandler.handleException(e);
	}
	
	}

}
