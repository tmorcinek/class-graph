package com.morcinek.uml.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TreeUtil {

	public static Element getElementsFromFile(String fileName) {
		Element element = null;
		try {
			File file = new File(fileName);
			// Create a factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// Use the factory to create a builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			// Get a list of all elements in the document
			element = (Element) doc.getDocumentElement();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return element;
	}

	public static void saveElementToFile(Element p_element, String p_filePath) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(p_element.getOwnerDocument());
			transformer.transform(source, result);

			String xmlString = result.getWriter().toString();
			FileWriter writer = new FileWriter(p_filePath);
			writer.write(xmlString);
			writer.close();

		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String elementToString(Element p_element) throws TransformerFactoryConfigurationError, TransformerException{
		org.w3c.dom.Document document = p_element.getOwnerDocument();
		org.w3c.dom.ls.DOMImplementationLS domImplLS = (org.w3c.dom.ls.DOMImplementationLS) document
		    .getImplementation();
		org.w3c.dom.ls.LSSerializer serializer = domImplLS.createLSSerializer();
		return serializer.writeToString(p_element);
	}
	
	public static Element createElement(String p_name) {
		Element element = null;
		try {
			// Create a factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// Use the factory to create a builder
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.newDocument();
			element = doc.createElement(p_name);
			doc.appendChild(element);

		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return element;
	}
	
	public static Element createElement(Element p_parentElement, String p_name) {
		Element element = null;
		try {
			Document doc = p_parentElement.getOwnerDocument();
			element = doc.createElement(p_name);

			return element;

		} catch (DOMException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Element createAndAppend(Element p_parentElement, String p_name){
		
		Element element = createElement(p_parentElement, p_name);
		p_parentElement.appendChild(element);
		
		return element;
	}
	
	/**
	 * Finds and return list of children that has tag name = <code>p_name</code>
	 * 
	 * @param p_name
	 * @return
	 */
	public static List<Element> getChildElementsByName(Element p_element, String p_name) {
		List<Element> elementsList = new LinkedList<Element>();

		for (Element elem : getAllChildElements(p_element)) {
			if (elem.getNodeName().equals(p_name)) {
				elementsList.add(elem);
			}
		}

		return elementsList;
	}
	
	public static Element getFirstChildElementByName(Element p_element, String p_name){
		List<Element> elementsList = getChildElementsByName(p_element, p_name);
		if(elementsList.isEmpty()){
			return null;
		} else {
			return elementsList.get(0);
		}
	}
	
	/**
	 *  All children from element that are Element ( type is Element)
	 *  
	 * @return Element list
	 */
	private static List<Element> getAllChildElements(Element p_element) {
		List<Element> elementsList = new LinkedList<Element>();

		NodeList nodeList = p_element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elementsList.add((Element) node);
			}
		}

		return elementsList;
	}
}
