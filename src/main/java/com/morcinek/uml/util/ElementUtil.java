package com.morcinek.uml.util;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.LinkedList;
import java.util.List;

public class ElementUtil {

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

    public static Element createAndAppend(Element p_parentElement, String p_name) {

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

    public static Element getFirstChildElementByName(Element p_element, String p_name) {
        List<Element> elementsList = getChildElementsByName(p_element, p_name);
        if (elementsList.isEmpty()) {
            return null;
        } else {
            return elementsList.get(0);
        }
    }

    /**
     * All children from element that are Element ( type is Element)
     *
     * @return Element list
     */
    private static List<Element> getAllChildElements(Element p_element) {
        List<Element> elementsList = new LinkedList<Element>();

        if (p_element != null) {
            NodeList nodeList = p_element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    elementsList.add((Element) node);
                }
            }
        }

        return elementsList;
    }
}
