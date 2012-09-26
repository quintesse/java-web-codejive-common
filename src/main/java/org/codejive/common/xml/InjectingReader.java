/*
 * [codejive-common] Codejive commons package
 * 
 * Copyright (C) 2006 Tako Schotanus
 * 
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 * 
 * Created on November 19, 2006
 */
package org.codejive.common.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class InjectingReader implements XMLStreamReader {
	private XMLStreamReader reader;
	private XMLStreamReader injectedReader;
	private boolean autoClose;
	private boolean firstInjectedElement;
	
	public InjectingReader(XMLStreamReader _reader) {
		reader = _reader;
	}

	public void inject(XMLStreamReader _injectedReader, boolean _autoClose) {
		injectedReader = _injectedReader;
		autoClose = _autoClose;
		firstInjectedElement = true;
	}

	public int getAttributeCount() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return  rdr.getAttributeCount();
	}

	public String getAttributeLocalName(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return  rdr.getAttributeLocalName(index);
	}

	public QName getAttributeName(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributeName(index);
	}

	public String getAttributeNamespace(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributeNamespace(index);
	}

	public String getAttributePrefix(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributePrefix(index);
	}

	public String getAttributeType(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributeType(index);
	}

	public String getAttributeValue(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributeValue(index);
	}

	public String getAttributeValue(String namespaceURI, String localName) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getAttributeValue(namespaceURI, localName);
	}

	public String getElementText() throws XMLStreamException {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getElementText();
	}

	public int getEventType() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getEventType();
	}

	public String getLocalName() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getLocalName();
	}

	public Location getLocation() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getLocation();
	}

	public QName getName() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getName();
	}

	public NamespaceContext getNamespaceContext() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespaceContext();
	}

	public int getNamespaceCount() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespaceCount();
	}

	public String getNamespacePrefix(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespacePrefix(index);
	}

	public String getNamespaceURI() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespaceURI();
	}

	public String getNamespaceURI(String prefix) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespaceURI(prefix);
	}

	public String getNamespaceURI(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getNamespaceURI(index);
	}

	public String getPIData() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getPIData();
	}

	public String getPITarget() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getPITarget();
	}

	public String getPrefix() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getPrefix();
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getProperty(name);
	}

	public String getText() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getText();
	}

	public char[] getTextCharacters() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getTextCharacters();
	}

	public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getTextCharacters(sourceStart, target, targetStart, length);
	}

	public int getTextLength() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getTextLength();
	}

	public int getTextStart() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.getTextStart();
	}

	public boolean hasName() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.hasName();
	}

	public boolean hasText() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.hasText();
	}

	public boolean isAttributeSpecified(int index) {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isAttributeSpecified(index);
	}

	public boolean isCharacters() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isCharacters();
	}

	public boolean isEndElement() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isEndElement();
	}

	public boolean isStandalone() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isStandalone();
	}

	public boolean isStartElement() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isStartElement();
	}

	public boolean isWhiteSpace() {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		return rdr.isWhiteSpace();
	}

	public boolean hasNext() throws XMLStreamException {
		return (injectedReader == null) ? reader.hasNext() : firstInjectedElement || injectedReader.hasNext() || reader.hasNext();
	}

	public int next() throws XMLStreamException {
		int result;
		if (injectedReader == null) {
			result = reader.next();
		} else {
			if (firstInjectedElement) {
				result = injectedReader.getEventType();
				firstInjectedElement = false;
			} else {
				if (injectedReader.hasNext()) {
					result = injectedReader.next();
				} else {
					if (autoClose) {
						injectedReader.close();
					}
					injectedReader = null;
					result = reader.next();
				}
			}
		}
		return result;
	}

	// To make sure that this is implemented in a way that works for us
	// (we want all access to the underlying reader to go through next()
	// but it seems the implementation of nextTag() doesn't use it)
	public int nextTag() throws XMLStreamException {
		int eventType = next();
		while((eventType == XMLStreamConstants.CHARACTERS && isWhiteSpace()) // skip
				// whitespace
				|| (eventType == XMLStreamConstants.CDATA && isWhiteSpace()) 
				// skip whitespace
				|| eventType == XMLStreamConstants.SPACE
				|| eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
				|| eventType == XMLStreamConstants.COMMENT
		) {
			eventType = next();
		}
		if (eventType != XMLStreamConstants.START_ELEMENT && eventType != XMLStreamConstants.END_ELEMENT) {
			throw new XMLStreamException("expected start or end tag", getLocation());
		}
		return eventType;
	}

	public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
		XMLStreamReader rdr = (injectedReader == null) ? reader : injectedReader;
		rdr.require(type, namespaceURI, localName);
	}

	public boolean standaloneSet() {
		return reader.standaloneSet();
	}

	public String getCharacterEncodingScheme() {
		return reader.getCharacterEncodingScheme();
	}

	public String getEncoding() {
		return reader.getEncoding();
	}

	public String getVersion() {
		return reader.getVersion();
	}

	public void close() throws XMLStreamException {
		if (injectedReader != null && autoClose) {
			injectedReader.close();
			injectedReader = null;
		}
		reader.close();
	}
}
