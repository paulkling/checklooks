package org.gradle;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {
	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		
		DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
		builderfactory.setNamespaceAware(false);
		
		DocumentBuilder builder = builderfactory.newDocumentBuilder();
		
		String string = "<XmlOrder Reference=\"01053-201408071800-49549\" />";

		ByteArrayInputStream bis = new ByteArrayInputStream(string.getBytes());
		
		Document xmlDocument = builder.parse(bis);
		
		
		XPathExpression xPathExpression = xPath.compile( "//XmlOrder/@Reference");
	
		String attributeValue = "" + xPathExpression.evaluate(xmlDocument, XPathConstants.STRING);
		System.out.println(attributeValue);
		
	
		
	
		
	}
	
	
	
}
