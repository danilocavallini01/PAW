
package it.unibo.paw;

import java.io.*;
import java.util.Vector;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class MainXMLParser {
	
	public static void main(String[] args) throws Exception{
		
		String xmlFilename;
		
		if ( args.length != 0 ) {
			System.out.println("usage: "+MainXMLParser.class.getSimpleName()+" xmlFilename");
		}
		else{
			try {
				xmlFilename = "resources/AddressList.xml";
				
				String schemaFeature = "http://apache.org/xml/features/validation/schema";
				String ignorableWhitespace = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
				
				// SAX 
				// 1) Costruire un parser SAX che validi il documento XML
				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setValidating(true);
				spf.setNamespaceAware(true);
				SAXParser saxParser = spf.newSAXParser();
				
				// 2) agganciare opportuni listener al lettore XML
				XMLReader xmlReader = saxParser.getXMLReader();
				ErrorHandler errorHandler = new ErrorHandler();
			    xmlReader.setErrorHandler(errorHandler);
				// (unico content handler per tutti i DTD/XSD; 
				// sarebbe pi� corretto content handler diversi per DTD/XSD diversi)
				SAXContentHandler handler = new SAXContentHandler();
			    xmlReader.setContentHandler(handler);
//
//				// seguente istruzione per specificare che stiamo validando tramite XML Schema 
				xmlReader.setFeature(schemaFeature,true);
				
//				// 3) Parsificare il documento
				xmlReader.parse(xmlFilename);
			    
				// 4) visualizzare il risultato
				System.out.println("SAX IgnorableWhitespace = " + handler.getIgnorableWhitespace());
				System.out.println("SAX PeopleAmount = " + handler.getPeopleAmount());
				System.out.println("SAX PeoplePreMM = " + handler.getPeoplePreMM());
				System.out.println("SAX DonTel = " + handler.getDonTel());
				
				
//				// DOM
//				// 1) Costruire un DocumentBuilder che validi il documento XML
//				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//				dbf.setValidating(true);
//				dbf.setNamespaceAware(true);
//				
//				// seguente istruzione per specificare che stiamo validando tramite XML Schema 
//				dbf.setFeature(schemaFeature,true);
//				
//				// seguente istruzione per specificare che gli "ignorable whitespace" (tab, new line...) 
//				// tra un tag ed un altro devono essere scartati e non considerati come text node
//				dbf.setFeature(ignorableWhitespace, false);
//				
//				// 2) Attivare un gestore di non-conformita'
//				DocumentBuilder db = dbf.newDocumentBuilder();
//				db.setErrorHandler(new ErrorHandler());
//				
//				// 3) Parsificare il documento, ottenendo un documento DOM
//				Document domDocument = db.parse(new File(xmlFilename));
//				domDocument.getDocumentElement().normalize();
//				
//				// 4) utilizzare il documento DOM
//				System.out.println("DOM PeopleAmount = " + getPeopleAmount(domDocument));
//				System.out.println("DOM PeoplePreMM = " + getPeoplePreMM(domDocument));
//				System.out.println("DOM DonTel1 = " + getTel("Don", domDocument) );
//				System.out.println("DOM MickeyTel1 = " + getTel("Mickey", domDocument) );
//				setTel("Donald", "Duck", "1234", domDocument);
//				setTel("Mickey", "Mouse", "5678", domDocument);
//				System.out.println("DOM DonTel2 = " + getTel("Don", domDocument) );
//				System.out.println("DOM MickeyTel2 = " + getTel("Mickey", domDocument) );
			    
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static int getPeopleAmount(Document domDocument){
		NodeList informationList = domDocument.getElementsByTagName("Information");
		return informationList.getLength();
	}
	
	private static int getPeoplePreMM(Document domDocument){
		int proplePreMM = 0;
		boolean mmFound = false;

		NodeList informationList = domDocument.getElementsByTagName("Information");
		for(int i=0; !mmFound && i<informationList.getLength(); i++){
			Node information = informationList.item(i);
			
			// cerco "Full_name"
			NodeList infoChildrenNodes = information.getChildNodes();
			Node fullName = null;
			for(int j=0; fullName==null && j<infoChildrenNodes.getLength(); j++){
				Node node = infoChildrenNodes.item(j);
				if( node.getNodeName().equals("Full_name")){
					fullName = node;
				}
			}
			NodeList fullNameList = fullName.getChildNodes();
			
			// cerco "First_name" e "Last_name"
			String firstName = null, lastName = null;
			for(int j=0; j<fullNameList.getLength(); j++){
				Node el = fullNameList.item(j);
				if( el.getLocalName()!=null ){
					if ( el.getLocalName().equals("First_name")){
						firstName = el.getTextContent();
					}
					else if(el.getLocalName().equals("Last_name")){
						lastName = el.getTextContent();
					}
				}
			}
			
			//if( firstName!=null && lastName!=null ){
				if( firstName.equals("Mickey") && lastName.equals("Mouse") ){
					mmFound = true;
				}
			//}
			else{
				proplePreMM++;
			}
		}
		
		return proplePreMM;
	}
	
	private static Vector<String> getTel(String startsWith, Document domDocument){
		Vector<String> tel = new Vector<String>();
		
		NodeList informationList = domDocument.getElementsByTagName("Information");
		//System.out.println("getPeoplePreMM informationList.getLength()="+informationList.getLength());
		for(int i=0; i<informationList.getLength(); i++){
			boolean found = false;
			
			Node information = informationList.item(i);

			// cerco "Full_name"
			NodeList infoChildrenNodes = information.getChildNodes();
			Node fullName = null;
			for(int j=0; fullName==null && j<infoChildrenNodes.getLength(); j++){
				Node node = infoChildrenNodes.item(j);
				if( node.getNodeName().equals("Full_name")){
					fullName = node;
				}
			}
			NodeList fullNameList = fullName.getChildNodes();
			
			// cerco "First_name"
			for(int j=0; j<fullNameList.getLength(); j++){
				Node el = fullNameList.item(j);if( el.getLocalName()!=null ){
					if ( el.getLocalName().equals("First_name")){
						if(el.getTextContent().startsWith(startsWith)){
							found = true;
						}
					}
				}
			}
			
			if(found){
				// cerco "Telephone"
				boolean telephone = false;
				for(int j=0; !telephone && j<infoChildrenNodes.getLength(); j++){
					Node node = infoChildrenNodes.item(j);
					if( node.getNodeName().equals("Telephone")){
						telephone = true;
						tel.addElement(node.getTextContent());
					}
				}
			}
		}
		
		return tel;
	}
	
	private static void setTel(String firstName, String lastName, String newTelephone, Document domDocument){
		NodeList informationList = domDocument.getElementsByTagName("Information");
		//System.out.println("getPeoplePreMM informationList.getLength()="+informationList.getLength());
		for(int i=0; i<informationList.getLength(); i++){
			boolean foundFirstName = false;
			boolean foundLastName = false;
			
			Node information = informationList.item(i);

			// cerco "Full_name"
			NodeList infoChildrenNodes = information.getChildNodes();
			Node fullName = null;
			for(int j=0; fullName==null && j<infoChildrenNodes.getLength(); j++){
				Node node = infoChildrenNodes.item(j);
				if( node.getNodeName().equals("Full_name")){
					fullName = node;
				}
			}
			NodeList fullNameList = fullName.getChildNodes();
			
			// cerco "First_name" e "Last_name"
			for(int j=0; j<fullNameList.getLength(); j++){
				Node el = fullNameList.item(j);
				if( el.getLocalName()!=null ){
					if ( el.getLocalName().equals("First_name")){
						if(el.getTextContent().equals(firstName)){
							foundFirstName = true;
						}
					}
					else if ( el.getLocalName().equals("Last_name")){
						if(el.getTextContent().equals(lastName)){
							foundLastName = true;
						}
					}
				}
			}
			
			if( foundFirstName && foundLastName ){
				// cerco "Telephone"
				boolean telephone = false;
				for(int j=0; !telephone && j<infoChildrenNodes.getLength(); j++){
					Node node = infoChildrenNodes.item(j);
					if( node.getNodeName().equals("Telephone")){
						telephone = true;
						node.setTextContent(newTelephone);
					}
				}
				if( ! telephone ){
					Element telEl = domDocument.createElement("Telephone");
					telEl.setTextContent(newTelephone);
					information.appendChild(telEl);
				}
			}
		}
	}
	
}
