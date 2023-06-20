package it.unibo.paw;

import java.io.*;
import java.util.Vector;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class MainXMLParser {

	public static void main(String[] args) throws Exception {

		String xmlFilename;

		if (args.length != 0) {
			System.out.println("usage: " + MainXMLParser.class.getSimpleName() + " xmlFilename");
		} else {
			try {
				xmlFilename = "./resources/29Giu2021.xml";

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
				xmlReader.setFeature(schemaFeature, true);

//				// 3) Parsificare il documento
				xmlReader.parse(xmlFilename);

				// 4) visualizzare il risultato
				System.out.println("SAX Risultato = " + handler.getGiocoTradizionale_Fascia6_8_GI());

				PrintWriter pw = new PrintWriter("Pony.txt");

				pw.println("giochi tradizionali individuali di fascia 6-8 anni");

				for (GTInfo g : handler.getGiocoTradizionale_Fascia6_8_GI()) {
					pw.println(g);
				}
				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
