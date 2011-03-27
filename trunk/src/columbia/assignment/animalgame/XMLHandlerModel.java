package columbia.assignment.animalgame;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLHandlerModel
{
	
	private DocumentBuilderFactory builderFactory;
	private DocumentBuilder docBuilder;
	private Document        Doc;
	
	private Element rootElement;
	private NodeList nodeList;
	
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
	            new FileInputStream("XML/AnimalGameTree.xml"));
	} catch (SAXException e) {
	    ErrorHandler.handleException(e);
	} catch (IOException e) {
	    ErrorHandler.handleException(e);
	}
	
		rootElement = Doc.getDocumentElement();
		nodeList = rootElement.getChildNodes();
		
		for(int i=0; i<nodeList.getLength(); i++){
			  Node node = nodeList.item(i);

			  if(node instanceof Element){
			    //a child element to process
			    Element child = (Element) node;
			   /* String answer = child.getAttribute("Answer");
			    String name   = child.getAttribute("Name");
			    String path   = child.getAttribute("Path");
			    System.out.println("XML  ANSWER: " + answer + "  NAME: " + name +
			    				"PATH:  " + path);
			    				*/
			    String text = child.getAttribute("Text");
			    System.out.println("TEXT:  " + text);
			  }
			}
	
	}

}
