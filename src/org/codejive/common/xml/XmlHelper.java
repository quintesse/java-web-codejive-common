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
 * Created on 10-jul-2002
 */
package org.codejive.common.xml;

import java.io.*;
import java.net.*;

import org.codejive.common.CodejiveException;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Tako
 * @version $Revision: $
 */
public class XmlHelper {

	public static Document ownerDocument(Node _node) {
		Document result;
		if (_node != null) {
			if (_node.getNodeType() != Node.DOCUMENT_NODE) {
				result = _node.getOwnerDocument();
			} else {
				result = (Document)_node;
			}
		} else {
			result = null;
		}
		return result;
	}
	
	public static void removeAllChildren(Node _parentNode) {
		while (_parentNode.getFirstChild() != null) {
			_parentNode.removeChild(_parentNode.getFirstChild());
		}
	}
	
	public static org.w3c.dom.Document parse(InputSource _in) throws CodejiveException {
		Document aDoc = null;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
		dbf.setValidating(false);
	    DocumentBuilder db;
	    try {
		    db = dbf.newDocumentBuilder();
		    aDoc = db.parse(_in);
	    } catch (ParserConfigurationException e) {
	    	throw new CodejiveException("Could not get new DocumentBuilder", e);
	    } catch (SAXException e) {
	    	throw new CodejiveException("Could not parse input source", e);
		} catch (IOException e) {
	    	throw new CodejiveException("Could not read input source", e);
		}
	    return aDoc;
	}

	public static org.w3c.dom.Document parse(InputStream _in) throws CodejiveException {
		return parse(new InputSource(_in));
	}

	public static org.w3c.dom.Document parse(Reader _reader) throws CodejiveException {
		return parse(new InputSource(_reader));
	}

	public static org.w3c.dom.Document parse(String _xml) throws CodejiveException {
		Document aResult = parse(new InputSource(new StringReader(_xml)));
		return aResult;
	}

    public static void serialize(Result _result, Source _source, boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
		Transformer aTransformer = getTransformer(_indent, _omitXmlDecl);
		try {
			aTransformer.transform(_source, _result);
		} catch (TransformerException e) {
			throw new CodejiveException("Could not serialize source", e);
		}
    }

    public static void serialize(Result _result, Source _source) throws CodejiveException {
    	serialize(_result, _source, true, false);
    }

    public static void serialize(Result _result, XMLStreamReader _source) throws CodejiveException {
    	serialize(_result, new StAXSource(_source), true, false);
    }
    
    public static void serialize(Writer _writer, Source _source) throws CodejiveException {
		StreamResult result = new StreamResult(_writer);
		serialize(result, _source);
    }
    
    public static void serialize(Writer _writer, XMLStreamReader _source) throws CodejiveException {
		StreamResult result = new StreamResult(_writer);
		serialize(result, new StAXSource(_source));
    }

    public static void serialize(Writer _writer, Node _node, boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
		DOMSource source = new DOMSource(_node);
		StreamResult result = new StreamResult(_writer);
		serialize(result, source, _indent, _omitXmlDecl);
    }

    public static void serialize(Writer _writer, Node _node) throws CodejiveException {
    	// For an element we'll just assume you want identing and no XML declaration
    	serialize(_writer, _node, true, true);
    }
    
    public static void serialize(Writer _writer, Document _doc) throws CodejiveException {
    	// For a document we'll just assume you want identing and XML declaration
    	serialize(_writer, _doc, true, false);
    }
    
    public static String serialize(Source _source) throws CodejiveException {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		serialize(result, _source);
		return result.toString();
    }

    public static String serialize(XMLStreamReader _source) throws CodejiveException {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		serialize(result, new StAXSource(_source));
		return result.toString();
    }

    public static String serialize(Node _node, String _encoding, boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
		StringWriter result = new StringWriter();
		serialize(result, _node, _indent, _omitXmlDecl);
		return result.toString();
    }

    public static String serialize(Node _node, boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
    	// We'll just leave the encoding as it is
    	return serialize(_node, null, _indent, _omitXmlDecl);
    }
    
    public static String serialize(Node _node) throws CodejiveException {
    	// For an element we'll just assume you want identing and no XML declaration
    	return serialize(_node, null, true, true);
    }
    
    public static String serialize(Document _doc) throws CodejiveException {
    	// For a document we'll just assume you want identing and XML declaration
    	return serialize(_doc, null, true, false);
    }
    
	/** Creates and returns a new empty DOM document.
	 * @return a new, empty DOM document.
	 * @throws a BackStreamRuntimeException if it was unable to create the necessary factory for creating the document.
	 */
	public static Document createDocument() {
	    DocumentBuilderFactory aDBFact = DocumentBuilderFactory.newInstance();
	    aDBFact.setNamespaceAware(true);
		aDBFact.setValidating(false);
	    DocumentBuilder aDB;
		try {
			aDB = aDBFact.newDocumentBuilder();
		} catch (javax.xml.parsers.ParserConfigurationException ex) {
			throw new RuntimeException("Error creating document builder needed to create a new document", ex);
		}
	    Document aDoc = aDB.newDocument();
	    return aDoc;
	}

	/** Creates and returns a new empty DOM document using the XML in the given string.
	 * @param _xml the XML that should be used to create the document
	 * @return a new DOM document filled with the given XML.
	 * @throws a BackStreamRuntimeException if it was unable to create the necessary factory for creating the document.
	 */
	public static Document createDocument(String _xml) throws CodejiveException {
		return parse(_xml);
	}

    public static org.w3c.dom.Document readDocument(Reader _reader) throws CodejiveException {
		return parse(_reader);
    }
	
    public static org.w3c.dom.Document readDocument(InputStream _in) throws CodejiveException {
		return parse(_in);
    }
	
    public static org.w3c.dom.Document readDocument(String _fileName, String _encoding) throws CodejiveException {
		InputSource is = new InputSource(_fileName);
		is.setSystemId(_fileName);
		if (_encoding != null) {
			is.setEncoding(_encoding);
		}
		Document aDoc = parse(is);
		return aDoc;
    }
	
	public static org.w3c.dom.Document readDocument(String _fileName) throws CodejiveException {
		return readDocument(_fileName, null);
	}
    
	public static org.w3c.dom.Document readDocument(File _file) throws CodejiveException {
		try {
			return readDocument(_file.toURI().toURL());
		} catch (IOException e) {
			throw new CodejiveException(e);
		}
	}
    
	public static org.w3c.dom.Document readDocument(URL _url) throws CodejiveException {
		try {
			return parse(_url.openStream());
		} catch (IOException e) {
			throw new CodejiveException(e);
		}
	}
    
    public static void writeDocument(Document _doc, Writer _writer) throws CodejiveException {
    	serialize(_writer, _doc);
    }
	
    public static void writeDocument(org.w3c.dom.Document _aDoc, String _fileName) throws CodejiveException {
		BufferedWriter wr = null; 
		try { 
			wr = new BufferedWriter(new FileWriter(_fileName));
			writeDocument(_aDoc, wr);
		} catch (IOException e) {
			throw new CodejiveException(e);
		} finally {
			if(wr != null) {
				try { wr.close(); } catch(Exception e) {}
			}
		}
	}

    public static Transformer getTransformer() throws CodejiveException {
    	return getTransformer(null, true, false);
    }

    public static Transformer getTransformer(boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
    	return getTransformer(null, _indent, _omitXmlDecl);
    }

    public static Transformer getTransformer(Source _source) throws CodejiveException {
    	return getTransformer(_source, true, false);
    }

    public static Transformer getTransformer(String _uri) throws CodejiveException {
    	Source source = new StreamSource(_uri);
    	return getTransformer(source, true, false);
    }

    public static Transformer getTransformer(Source _source, boolean _indent, boolean _omitXmlDecl) throws CodejiveException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			if (_source != null) {
				transformer = factory.newTransformer(_source);
			} else {
				transformer = factory.newTransformer();
			}
			transformer.setOutputProperty(OutputKeys.INDENT, (_indent ? "yes" : "no"));
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (_omitXmlDecl ? "yes" : "no"));
		} catch (TransformerConfigurationException e) {
			throw new CodejiveException(e);
		}
		return transformer;
    }
    
    public static void transform(Transformer _transformer, Document _doc, Writer _writer) throws CodejiveException {
    	DOMSource source = new DOMSource(_doc);
    	StreamResult result = new StreamResult(_writer);
    	try {
			_transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new CodejiveException(e);
		}
    }
    
    public static void transform(Transformer _transformer, String _sourceUri, Writer _writer) throws CodejiveException {
    	StreamSource source = new StreamSource(_sourceUri);
    	StreamResult result = new StreamResult(_writer);
    	try {
			_transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new CodejiveException(e);
		}
    }
    
    public static void transform(String _transformerUri, Document _doc, Writer _writer) throws CodejiveException {
    	StreamSource source = new StreamSource(_transformerUri);
    	Transformer transformer = getTransformer(source);
    	transform(transformer, _doc, _writer);
    }
    
    public static void transform(String _transformerUri, String _sourceUri, Writer _writer) throws CodejiveException {
    	StreamSource source = new StreamSource(_transformerUri);
    	Transformer transformer = getTransformer(source);
    	transform(transformer, _sourceUri, _writer);
    }
}
