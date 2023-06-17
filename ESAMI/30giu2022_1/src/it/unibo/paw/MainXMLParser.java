package it.unibo.paw;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainXMLParser {
	public static void main(String[] args) throws Exception {

		String xmlFilename;
		PrintWriter pw = null;

		if (args.length != 0) {
			System.out.println("usage: " + MainXMLParser.class.getSimpleName() + " xmlFilename");
		} else {
			try {
				xmlFilename = "resources/30giu2022.xml";

				String schemaFeature = "http://apache.org/xml/features/validation/schema";
				String ignorableWhitespace = "http://apache.org/xml/features/dom/include-ignorable-whitespace";

				// DOM
				// 1) Costruire un DocumentBuilder che validi il documento XML
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(true);
				dbf.setNamespaceAware(true);

				// seguente istruzione per specificare che stiamo validando tramite XML Schema
				dbf.setFeature(schemaFeature, true);

				// seguente istruzione per specificare che gli "ignorable whitespace" (tab, new
				// line...)
				// tra un tag ed un altro devono essere scartati e non considerati come text
				// node
				dbf.setFeature(ignorableWhitespace, false);

				// 2) Attivare un gestore di non-conformita'
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.setErrorHandler(new ErrorHandler());

				// 3) Parsificare il documento, ottenendo un documento DOM
				Document domDocument = db.parse(new File(xmlFilename));
				domDocument.getDocumentElement().normalize();

				// 4) utilizzare il documento DOM
				
				pw = new PrintWriter("Cineteca.txt");
				
				pw.println("audioVisivi in bianco e nero: " + getAudiovisiviBN(domDocument));
				pw.println("pellicole 16mm in buone condizioni: " + getPellicole16(domDocument));
				
				System.out.println(getAudiovisiviBN(domDocument));
				System.out.println(getPellicole16(domDocument));
				
				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if ( pw != null ) {
					pw.close();
				}
			}
		}
	}

	public static int getAudiovisiviBN(Document dom) {
		int total = 0;

		NodeList audiovisivi = dom.getElementsByTagName("audiovisivo");

		for (int i = 0; i < audiovisivi.getLength(); i++) {
			NodeList avInfos = audiovisivi.item(i).getChildNodes();

			for (int j = 0; j < avInfos.getLength(); j++) {
				Node avInfo = avInfos.item(j);
				if (avInfo.getLocalName().equals("tipoColore") && avInfo.getTextContent().equals("BN")) {
					total++;
				}
			}
		}

		return total;
	}

	public static int getPellicole16(Document dom) {
		int total = 0;

		NodeList pellicole = dom.getElementsByTagName("pellicola");

		for (int i = 0; i < pellicole.getLength(); i++) {
			NodeList pInfos = pellicole.item(i).getChildNodes();

			int check = 0;

			for (int j = 0; j < pInfos.getLength(); j++) {
				Node pInfo = pInfos.item(j);
				if (pInfo.getLocalName().equals("buonoStato") && pInfo.getTextContent().equals("true")) {
					check++;
				}

				if (pInfo.getLocalName().equals("tipologia") && pInfo.getTextContent().equals("16")) {
					check++;
				}
			}

			if (check == 2) {
				total++;
			}
		}

		return total;
	}
}
