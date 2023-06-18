package it.unibo.paw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {

	private Set<String> voli;

	private boolean inPrenotazione = false;
	private boolean inCodice = false;
	private boolean inTipoVolo = false;
	private boolean inCompagniaAerea = false;

	private String codice;
	private String tipoVolo;
	private String compagniaAerea;

	public SAXContentHandler() {
		this.voli = new HashSet<String>();
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {

		if (localName.equals("prenotazione")) {

			codice = null;
			tipoVolo = null;
			compagniaAerea = null;

			inPrenotazione = true;
		} else if (localName.equals("codice")) {
			inCodice = true;
		} else if (localName.equals("nomeCompagniaAerea")) {
			inCompagniaAerea = true;
		} else if (localName.equals("tipoVolo")) {
			inTipoVolo = true;
		}
	}

	public void characters(char ch[], int start, int length) {

		if (!inPrenotazione)
			return;

		if (inCodice) {
			codice = new String(ch, start, length);
		} else if (inTipoVolo) {
			tipoVolo = new String(ch, start, length);
		} else if (inCompagniaAerea) {
			compagniaAerea = new String(ch, start, length);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) {

		if (localName.equals("prenotazione")) {
			inPrenotazione = false;

			if (tipoVolo.equals("Solo-Andata") && compagniaAerea.equals("Alitalia")) {
				this.voli.add(codice);
			}

		} else if (localName.equals("codice")) {
			inCodice = false;
		} else if (localName.equals("nomeCompagniaAerea")) {
			inCompagniaAerea = false;
		} else if (localName.equals("tipoVolo")) {
			inTipoVolo = false;
		}
	}

	public Set<String> voli() {
		return this.voli;
	}
}
