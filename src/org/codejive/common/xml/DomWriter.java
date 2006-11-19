/*
 * [codejive-common-xml] Codejive utility classes
 * 
 * Copyright (C) 2003 Tako Schotanus
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created on Nov 26, 2004
 */
package org.codejive.common.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * @author tako
 * @version $Revision:	$
 */
public class DomWriter implements XMLStreamWriter {
	private Document doc;
	private String encoding;
	private Node current;
	private SimpleNamespaceContext context;
	private boolean docStarted;
	
	protected DomWriter() {
		doc = XmlHelper.createDocument();
		current = doc;
		encoding = null;
		context = new SimpleNamespaceContext();
		docStarted = false;
	}
	
	protected DomWriter(Element _elem) {
		doc = _elem.getOwnerDocument();
		encoding = doc.getXmlEncoding();
		current = _elem;
		context = new SimpleNamespaceContext();
		docStarted = true;
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public String getEncoding() {
		return encoding;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getPrefix(java.lang.String)
	 */
	public String getPrefix(String _uri) throws XMLStreamException {
		return context.getPrefix(_uri);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setPrefix(java.lang.String, java.lang.String)
	 */
	public void setPrefix(String _prefix, String _uri) throws XMLStreamException {
		context.setPrefix(_prefix, _uri);
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setDefaultNamespace(java.lang.String)
	 */
	public void setDefaultNamespace(String _uri) throws XMLStreamException {
		context.setDefaultNamespace(_uri);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getNamespaceContext()
	 */
	public NamespaceContext getNamespaceContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setNamespaceContext(javax.xml.namespace.NamespaceContext)
	 */
	public void setNamespaceContext(NamespaceContext _context) throws XMLStreamException {
		throw new RuntimeException("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument()
	 */
	public void writeStartDocument() throws XMLStreamException {
		writeStartDocument(null, null);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String)
	 */
	public void writeStartDocument(String _version) throws XMLStreamException {
		writeStartDocument(_version, null);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String, java.lang.String)
	 */
	public void writeStartDocument(String _encoding, String _version) throws XMLStreamException {
		if (docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document already started"));
		}
		
		if (_version != null) {
			doc.setXmlVersion(_version);
		}
		if (_encoding != null) {
			encoding = _encoding;
		}
		docStarted = true;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEndDocument()
	 */
	public void writeEndDocument() throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		current = null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String)
	 */
	public void writeStartElement(String _localName) throws XMLStreamException {
		writeStartElement(null, _localName, null);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String, java.lang.String)
	 */
	public void writeStartElement(String _namespaceURI, String _localName) throws XMLStreamException {
		String prefix = context.getPrefix(_namespaceURI);
		writeStartElement(prefix, _localName, _namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void writeStartElement(String _prefix, String _localName, String _namespaceURI) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		String qualifiedName = _localName;
		if (_prefix != null) {
			qualifiedName = _prefix + ":" + qualifiedName;
		}
		Node node = doc.createElementNS(_namespaceURI, qualifiedName);
		current.appendChild(node);
		current = node;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEndElement()
	 */
	public void writeEndElement() throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended)"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		current = current.getParentNode();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String)
	 */
	public void writeEmptyElement(String _localName) throws XMLStreamException {
		writeStartElement(_localName);
		writeEndElement();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String)
	 */
	public void writeEmptyElement(String _namespaceURI, String _localName) throws XMLStreamException {
		writeStartElement(_namespaceURI, _localName);
		writeEndElement();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void writeEmptyElement(String _prefix, String _localName, String _namespaceURI) throws XMLStreamException {
		writeStartElement(_prefix, _localName, _namespaceURI);
		writeEndElement();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String)
	 */
	public void writeAttribute(String _localName, String _value) throws XMLStreamException {
		writeAttribute(null, null, _localName, _value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void writeAttribute(String _namespace, String _localName, String _value) throws XMLStreamException {
		writeAttribute(null, _namespace, _localName, _value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void writeAttribute(String _prefix, String _namespace, String _localName, String _value) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		String qualifiedName = _localName;
		if (_prefix != null) {
			qualifiedName = _prefix + ":" + qualifiedName;
		}
		Element elem = (Element)current;
		elem.setAttributeNS(_namespace, qualifiedName, _value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCharacters(java.lang.String)
	 */
	public void writeCharacters(String _text) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		Text textNode = doc.createTextNode(_text);
		current.appendChild(textNode);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCharacters(char[], int, int)
	 */
	public void writeCharacters(char[] _text, int _start, int _len) throws XMLStreamException {
		String text = new String(_text, _start, _len);
		writeCharacters(text);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCData(java.lang.String)
	 */
	public void writeCData(String _data) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		CDATASection cdata = doc.createCDATASection(_data);
		current.appendChild(cdata);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEntityRef(java.lang.String)
	 */
	public void writeEntityRef(String _name) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		EntityReference ref =  doc.createEntityReference(_name);
		current.appendChild(ref);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeComment(java.lang.String)
	 */
	public void writeComment(String _data) throws XMLStreamException {
		Comment comment = doc.createComment(_data);
		current.appendChild(comment);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String)
	 */
	public void writeProcessingInstruction(String _target) throws XMLStreamException {
		writeProcessingInstruction(_target, null);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String, java.lang.String)
	 */
	public void writeProcessingInstruction(String _target, String _data) throws XMLStreamException {
		ProcessingInstruction pi = doc.createProcessingInstruction(_target, _data);
		current.appendChild(pi);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeDTD(java.lang.String)
	 */
	public void writeDTD(String _dtd) throws XMLStreamException {
		throw new XMLStreamException("Not implemented");
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeNamespace(java.lang.String, java.lang.String)
	 */
	public void writeNamespace(String _prefix, String _namespaceURI) throws XMLStreamException {
		if (!docStarted) {
			throw new XMLStreamException(new IllegalStateException("Document not started yet"));
		}
		if (current == null) {
			throw new XMLStreamException(new IllegalStateException("Document already ended"));
		}
		if (current == doc) {
			throw new XMLStreamException(new IllegalStateException("No element started"));
		}
		String qualifiedName = "xmlns";
		if (_prefix != null) {
			qualifiedName = qualifiedName + ":" + _prefix;
		}
		Element elem = (Element)current;
		elem.setAttribute(qualifiedName, _namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeDefaultNamespace(java.lang.String)
	 */
	public void writeDefaultNamespace(String _namespaceURI) throws XMLStreamException {
		writeNamespace(null, _namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getProperty(java.lang.String)
	 */
	public Object getProperty(String _name) throws IllegalArgumentException {
		if (_name == null) {
			throw new IllegalArgumentException("Argument name may not be null");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#close()
	 */
	public void close() throws XMLStreamException {
		// Nothing to do here
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#flush()
	 */
	public void flush() throws XMLStreamException {
		// Nothing to do here
	}
}


/*
 * $Log:	$
 */