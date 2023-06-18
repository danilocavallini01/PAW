package it.unibo.paw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {

	private List<GTInfo> result;

	private boolean inGT = false;
	private boolean inMarca = false;
	private boolean inNome = false;
	private boolean inFascia = false;
	private boolean inModalita = false;

	private String marca;
	private String nome;
	private String fascia;
	private String modalita;

	public SAXContentHandler() {
		this.result = new ArrayList<GTInfo>();
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {

		if (localName.equals("GT")) {

			inGT = true;

			marca = null;
			nome = null;
			fascia = null;
			modalita = null;

		} else if (localName.equals("marca")) {
			inMarca = true;
		} else if (localName.equals("nome")) {
			inNome = true;
		} else if (localName.equals("fasciaEta")) {
			inFascia = true;
		} else if (localName.equals("modalitaUtilizzo")) {
			inModalita = true;
		}
	}

	public void characters(char ch[], int start, int length) {

		if (!inGT)
			return;

		if (inMarca) {
			marca = new String(ch, start, length);
		} else if (inNome) {
			nome = new String(ch, start, length);
		} else if (inFascia) {
			fascia = new String(ch, start, length);
		} else if (inModalita) {
			modalita = new String(ch, start, length);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) {

		if (localName.equals("GT")) {

			inGT = false;

			if (modalita.equals("GI") && fascia.equals("6-8")) {
				result.add(new GTInfo(nome, marca));
			}

		} else if (localName.equals("marca")) {
			inMarca = false;
		} else if (localName.equals("nome")) {
			inNome = false;
		} else if (localName.equals("fasciaEta")) {
			inFascia = false;
		} else if (localName.equals("modalitaUtilizzo")) {
			inModalita = false;
		}
	}

	public List<GTInfo> getGiocoTradizionale_Fascia6_8_GI() {
		return this.result;
	}
}
