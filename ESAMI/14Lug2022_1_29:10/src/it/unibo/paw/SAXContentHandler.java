package it.unibo.paw;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {

	private List<String> linkCliccati;

	boolean inRisultato = false;
	boolean isClicked = false;

	String nomeHotel = null;

	boolean isHotel = false;

	public SAXContentHandler() {
		this.linkCliccati = new ArrayList<String>();
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {

		if (localName.equals("risultato")) {
			nomeHotel = null;
			inRisultato = true;
			isClicked = Boolean.parseBoolean(atts.getValue("visitatoLink"));

		} else if (localName.equals("nomeHotel")) {
			isHotel = true;
		}
	}

	public void characters(char ch[], int start, int length) {

		
		if (!inRisultato || inRisultato && !isClicked)
			return;

		if (isHotel) {
			nomeHotel = new String(ch, start, length);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) {

		if (localName.equals("risultato")) {
			if (isClicked) {
				this.linkCliccati.add(nomeHotel);
			}
			
			inRisultato = false;
			isClicked = false;
			nomeHotel = null;

		} else if (localName.equals("nomeHotel")) {
			isHotel = false;
		}
	}

	public List<String> getSelezione() {
		return this.linkCliccati;
	}
}
