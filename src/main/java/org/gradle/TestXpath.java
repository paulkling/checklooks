package org.gradle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestXpath {

	XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
	private String cachePath="http://cir.lifetouch.net/cache/aggressivecache?url=";
	//private String cachePath="";
	private String env="";
	
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		TestXpath testXpath = new TestXpath();
		testXpath.testX1Look("60000");
		/*
		testXpath.testX1Look("21985");
		testXpath.testX1Look("11023");
		testXpath.testX1Look("21368");
		testXpath.testX1Look("20307");
		testXpath.testX1Look("21985");
		testXpath.testX1Look("21361");
		
		testXpath.testX1Look("11023");
		
		testXpath.testX1Look("60017");
		
		System.out.println("\n\n\n\n\n\n\n\n");
		testXpath.testX1Look("60130");
		
		testXpath.testX1Look("30211");*/
	}

	private void testX1Look(String lookId) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		TestXpath testXpath = new TestXpath();
		System.out.println("Step1");
		System.out.println("\tLayoutInTheme = " + lookId);
		
		Document xmlDocumentStep1 = testXpath.getXmlDocForStep1(lookId);
		LayoutInTheme layoutInTheme = testXpath.step1(xmlDocumentStep1);
		
		System.out.println("Step2");
		for ( IrisUrl irisUrl : layoutInTheme.getIris()){
			System.out.println("\t"+irisUrl.getId());
			Document xmlDocumentStep2 = testXpath.getXmlDocumentStep2(layoutInTheme.getExternalKey(),irisUrl); 
			testXpath.step2(layoutInTheme.getExternalKey(),xmlDocumentStep2, irisUrl);
		}
		
		System.out.println("Step3");
		for ( IrisUrl irisUrl : layoutInTheme.getIris()){
			System.out.println("\t"+irisUrl.getId());
			Document xmlDocumentStep3 = testXpath.getXmlDocumentStep3(irisUrl);
			testXpath.step3(xmlDocumentStep3, irisUrl);
		}
			
		System.out.println("Step4");
		for ( IrisUrl irisUrl : layoutInTheme.getIris()){
			System.out.println("\t"+irisUrl.getId());
			testXpath.step4(irisUrl);
		}
		
	}
	


	private Document getXmlDocForStep1(String lookId) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
		builderfactory.setNamespaceAware(true);
		DocumentBuilder builder = builderfactory.newDocumentBuilder();
		String url = cachePath + "http://"+env+"esb-ws.lifetouch.net/layout/v1.0/layoutsInTheme/"+lookId;
	
		Document xmlDocument = builder.parse(callWeb(url));
		//Document xmlDocument = builder.parse(new File(TestXpath.class.getResource("step1.txt").getFile().replace("%20", " ")));
		return xmlDocument;
	}
	
	private LayoutInTheme step1(Document xmlDocument) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		XPath xPath = factory.newXPath();
		
		LayoutInTheme layoutInTheme = new LayoutInTheme();
		

		XPathExpression xPathExpression = xPath.compile( "//LayoutTheme//Layouts//Layout//ExternalKey");
		String externalKey = xPathExpression.evaluate(xmlDocument,XPathConstants.STRING).toString();
		layoutInTheme.setExternalKey(externalKey);
		System.out.println("\texternalKey ---> " + layoutInTheme.getExternalKey());
		
		//many static images in look
		XPathExpression xPathExpression2 = xPath.compile( "//LayoutTheme//Layouts//Layout//LayoutStaticImages//StaticImage//Id");
		NodeList nodeList =  (NodeList) xPathExpression2.evaluate(xmlDocument,XPathConstants.NODESET);
		
		for (int index = 0; index < nodeList.getLength(); index++) {
			String id = nodeList.item(index).getTextContent();
			IrisUrl irisUrl = new IrisUrl();
			irisUrl.setId(id);
			layoutInTheme.getIris().add(irisUrl);
			System.out.println("\tid " + (index + 1) + " ---> " + id);
		}
		return layoutInTheme;
	}
	
	
	
	private void step2(String id, Document xmlDocument, IrisUrl irisUrl) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		//XPath xPath = factory.newXPath();
		
		//XPathExpression xPathExpression = xPath.compile( "//URLs//URL//ResponseUrl");
		//String responseUrl = xPathExpression.evaluate(xmlDocument,XPathConstants.STRING).toString();
		String responseUrl = "http://"+env+"esb-ws.lifetouch.net/layout/v1.0/layoutAssetVariantList/"+id+"/"+irisUrl.getId();
		irisUrl.setUrl(responseUrl);
		System.out.println("\t\tresponseUrl ---> " + responseUrl);
		
	}

	private Document getXmlDocumentStep2(String id, IrisUrl irisUrl) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
		builderfactory.setNamespaceAware(true);
		
		DocumentBuilder builder = builderfactory.newDocumentBuilder();
		String url = cachePath+"http://"+env+"iris.lifetouch.net/image-retrieval-service/iris-url-gen?location=layoutcatalog&context=imagelist&ltImageUrl=ltimage:LC:"
				+id+":"+irisUrl.getId();
		Document xmlDocument = builder.parse(callWeb(url));
		//Document xmlDocument = builder.parse(new File(TestXpath.class.getResource("step2.txt").getFile().replace("%20", " ")));
		return xmlDocument;
	}
	
	
	private void step3(Document xmlDocument, IrisUrl irisUrl) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		XPath xPath = factory.newXPath();
		
		//many static images in look
		XPathExpression xPathExpression2 = xPath.compile( "//LayoutStaticImageVariantsResponse//StaticImages//StaticImage//Instances//Instance//Location");
		NodeList nodeList =  (NodeList) xPathExpression2.evaluate(xmlDocument,XPathConstants.NODESET);
		
		for (int index = 0; index < nodeList.getLength(); index++) {
			String graphic = nodeList.item(index).getTextContent();
			System.out.println("\t\tgraphic " + (index + 1) + " ---> " + graphic);
			irisUrl.getGraphicsUrls().add(graphic);
		}
	}

	private Document getXmlDocumentStep3(IrisUrl irisUrl) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
		builderfactory.setNamespaceAware(false);
		
		DocumentBuilder builder = builderfactory.newDocumentBuilder();
		
		String url = cachePath+irisUrl.getUrl();
		
		Document xmlDocument = builder.parse(callWeb(url));
		//Document xmlDocument = builder.parse(new File(TestXpath.class.getResource("step3.txt").getFile().replace("%20", " ")));
		return xmlDocument;
	}
	
	
	private void step4(IrisUrl irisUrl) throws IOException {
		List<String> strings = irisUrl.getGraphicsUrls();
		for (String string : strings){
			InputStream stream = callWeb(cachePath+string);
		}
	}
	
	
	private InputStream callWeb(String url) throws IOException{
		//Instantiate an HttpClient
        HttpClient client = new HttpClient();
        //Instantiate a GET HTTP method
        GetMethod method = new GetMethod(url);
        try{
	        method.setRequestHeader("X-LTCallingApplicationId","24");
	        method.setRequestHeader("X-LTCallingApplicationName","MLT");
	        method.setRequestHeader("X-LTCallingApplicationInstance","testCheckLooks");
	        method.setRequestHeader("X-LTCallingUser","testCheckLooks");

	        int statusCode = client.executeMethod(method);

	        Header[] header = method.getResponseHeaders("Content-Length");
	        String length = "NA";
	        if (header.length>0)
	        	length = header[0].getValue();
	        
	        Header[] header2 = method.getResponseHeaders("Content-Type");
	        String type = "NA";
	        if (header2.length>0)
	        	type = header2[0].getValue();
 

	        byte[] bytes = IOUtils.toByteArray(method.getResponseBodyAsStream());
			if (bytes.length==0){
				System.out.println("<============Error size is zero");
			}
			

	        System.out.println("\t\t" + HttpStatus.getStatusText(statusCode)
	        		+"-"+statusCode
	        		+"-"+bytes.length + " bytes"
	        		+"-"+length
	        		+"-"+type
	        		+"-\t"+url);

	        InputStream is = new ByteArrayInputStream(bytes);
	        return is;
        }finally{
	        //release connection
	       // method.releaseConnection();
        }
	}
}
