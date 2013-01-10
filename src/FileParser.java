/*
	Author: Nicole Whitehead
	
	Input a grammar file in the *.gable format.
 */

import java.io.File;
import org.w3c.dom.Document; 
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; 

public class FileParser {	
	
	protected File inputFile; 
	protected Grammar gr; 
	
	public FileParser(String fileName) {
		String startSymbol = ""; 
		int numberOfIterations = 0;
		double angle = 0;
		double length = 0;
		
		try {
			inputFile = new File(fileName); 
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document doc = db.parse(inputFile); 
			doc.getDocumentElement().normalize(); 
			System.out.println("Root element " + doc.getDocumentElement().getNodeName());

			//Retrieves the start symbol of the grammar
			NodeList nodes = doc.getElementsByTagName("Start"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getChildNodes();
					
					startSymbol = ((Node) firstElementList.item(0)).getNodeValue(); 
					
					if(startSymbol == "") {
						System.out.println("No start sign input.");
					}
					else {
						System.out.println("Start symbol input."); 
					}
				}
			}
						
			//Retrieves the number of iterations
			nodes = doc.getElementsByTagName("Iterations"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getChildNodes();
					
					numberOfIterations = Integer.parseInt(((Node) firstElementList.item(0)).getNodeValue()); 
					
					if(numberOfIterations == 0) {
						System.out.println("No number of iterations input.");
					}
					else {
						System.out.println("Number of iterations input."); 
					}
				}
			}
			
			//Declaration of the grammar both the number of iterations and the start symbol
			//have been input.
			gr = new Grammar(startSymbol, numberOfIterations); 
			
			nodes = doc.getElementsByTagName("Length"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getChildNodes();
					
					length = Double.parseDouble(((Node) firstElementList.item(0)).getNodeValue()); 
					
					if(length == 0) {
						System.out.println("No length input.");
					}
					else {
						System.out.println("Length input."); 
					}
				}
			}

			gr.setLength(length);
			
			//For parsing out each of the Rule tags
			nodes = doc.getElementsByTagName("Rule"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				String variable = ""; 
				String terminal = ""; 
				double prob;
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getElementsByTagName("RuleStart"); 
					Element ruleStartElement = (Element) firstElementList.item(0); 
					NodeList ruleStartList = ruleStartElement.getChildNodes(); 
					variable = ((Node) ruleStartList.item(0)).getNodeValue(); 
					
					firstElementList = firstElement.getElementsByTagName("RuleEnd"); 
					Element ruleEndElement = (Element) firstElementList.item(0); 
					NodeList ruleEndList = ruleEndElement.getChildNodes(); 
					terminal = ((Node) ruleEndList.item(0)).getNodeValue();
					
					firstElementList = firstElement.getElementsByTagName("RuleProb"); 
					Element ruleProbElement = (Element) firstElementList.item(0); 
					NodeList ruleProbList = ruleProbElement.getChildNodes(); 
					prob = Double.parseDouble(((Node) ruleProbList.item(0)).getNodeValue());
					
					gr.addRule(variable, terminal,prob); 
				}
			}
			
			nodes = doc.getElementsByTagName("LeafRule"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				String variable = ""; 
				String terminal = ""; 
				double prob;
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getElementsByTagName("RuleStart"); 
					Element ruleStartElement = (Element) firstElementList.item(0); 
					NodeList ruleStartList = ruleStartElement.getChildNodes(); 
					variable = ((Node) ruleStartList.item(0)).getNodeValue(); 
					
					firstElementList = firstElement.getElementsByTagName("RuleEnd"); 
					Element ruleEndElement = (Element) firstElementList.item(0); 
					NodeList ruleEndList = ruleEndElement.getChildNodes(); 
					terminal = ((Node) ruleEndList.item(0)).getNodeValue();
					
					firstElementList = firstElement.getElementsByTagName("RuleProb"); 
					Element ruleProbElement = (Element) firstElementList.item(0); 
					NodeList ruleProbList = ruleProbElement.getChildNodes(); 
					prob = Double.parseDouble(((Node) ruleProbList.item(0)).getNodeValue());
					
					gr.addLeafRule(variable, terminal,prob); 
				}
			}
			
			nodes = doc.getElementsByTagName("Angle"); 
			for(int i = 0; i < nodes.getLength(); i++){
				Node firstNode = nodes.item(i); 
				
				if(firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode; 
					NodeList firstElementList = firstElement.getChildNodes();
					
					angle = Double.parseDouble(((Node) firstElementList.item(0)).getNodeValue()); 
					
					if(numberOfIterations == 0) {
						System.out.println("No angle input.");
					}
					else {
						System.out.println("Angle input."); 
					}
				}
			}
			
			gr.setAngle((double)angle);
			
			System.out.println("Grammar rules input"); 						
		} catch (Exception e) { System.out.println("File not properly parsed. Please try again."); }
	}
	
	public Grammar getGrammar() {
		return gr; 
	}

}