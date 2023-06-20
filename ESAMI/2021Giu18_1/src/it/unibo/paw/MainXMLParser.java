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
				xmlFilename = "resources/18Giu2021.xml";

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

				pw.println("totaleOfferte: " + getNumeroTotaleOfferte(domDocument));

				System.out.println(getNumeroTotaleOfferte(domDocument));

				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
	}

	public static int getNumeroTotaleOfferte(Document dom) {

		int total = 0;

		total += getNumOfferteByCategoria("antipasti", dom);
		total += getNumOfferteByCategoria("primi", dom);
		total += getNumOfferteByCategoria("secondi", dom);
		total += getNumOfferteByCategoria("contorni", dom);
		total += getNumOfferteByCategoria("dessert", dom);

		return total;

	}

	public static int getNumOfferteByCategoria(String nomeCategoria, Document dom) {
		NodeList categorie = dom.getElementsByTagName(nomeCategoria);
		Node categoria = categorie.item(0);

		return categoria.getChildNodes().getLength();
	}
}
