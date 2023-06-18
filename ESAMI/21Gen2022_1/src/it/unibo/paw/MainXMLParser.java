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
				xmlFilename = "./resources/21Gen2022.xml";
				
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
				System.out.println("SAX Scelte = " + handler.voli());
				
				PrintWriter pw = new PrintWriter("Volo.txt");
				
				pw.println("Voli di Alitalia di solo-andata");
				
				for (String s : handler.voli()) {
					pw.println(s);
				}
				pw.close();
				
			    
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
