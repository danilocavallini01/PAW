package it.unibo.paw;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {

	private List<Abbigliamento> abbigliamentiSelezionati;

	boolean inAbbigliamento = false;
	boolean isSelected = false;
	String abbigliamento = null;

	boolean inFotografia = false;
	String fotografia = null;
	boolean inDescrizione = false;
	String descrizione = null;
	boolean inPrezzo = false;
	Integer prezzo = null;

	public SAXContentHandler() {
		this.abbigliamentiSelezionati = new ArrayList<Abbigliamento>();
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {

		if (localName.equals("abbigliamento")) {
			
			fotografia = null;
			descrizione = null;
			prezzo = null;
			inAbbigliamento = true;
			isSelected = Boolean.parseBoolean(atts.getValue("selezionato"));
			
		} else if (localName.equals("fotografia")) {
			inFotografia = true;
		} else if (localName.equals("descrizioneCapo")) {
			inDescrizione = true;
		} else if (localName.equals("prezzo")) {
			inPrezzo = true;
		}
	}

	public void characters(char ch[], int start, int length) {
		
		if ( inAbbigliamento && !isSelected ) return;
		
		if (inFotografia) {
			fotografia = new String(ch, start, length);
		} else if (inDescrizione) {
			descrizione = new String(ch, start, length);
		} else if (inPrezzo) {
			prezzo = Integer.parseInt(new String(ch, start, length));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) {

		if (localName.equals("abbigliamento")) {
			inAbbigliamento = false;

			if (isSelected) {
				this.abbigliamentiSelezionati.add(new Abbigliamento(fotografia, descrizione, prezzo));
			}

			isSelected = false;
		} else if (localName.equals("fotografia")) {
			inFotografia = false;
		} else if (localName.equals("descrizioneCapo")) {
			inDescrizione = false;
		} else if (localName.equals("prezzo")) {
			inPrezzo = false;
		}
	}

	public List<Abbigliamento> getScelte() {
		return this.abbigliamentiSelezionati;
	}
}
