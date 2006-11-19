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
 * Created on Nov 27, 2004
 */
package org.codejive.common.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * @author tako
 * @version $Revision:	$
 */
public class SimpleNamespaceContext implements NamespaceContext {
	private Map<String, String> namespaces;
	
	public SimpleNamespaceContext() {
		namespaces = new HashMap<String, String>();
	}

	public SimpleNamespaceContext(String _namespaceURI) {
		namespaces = new HashMap<String, String>();
		setDefaultNamespace(_namespaceURI);
	}

	public SimpleNamespaceContext(String _prefix, String _namespaceURI) {
		namespaces = new HashMap<String, String>();
		setPrefix(_prefix, _namespaceURI);
	}

	public void setPrefix(String _prefix, String _namespaceURI) {
		if (_prefix == null) {
			throw new IllegalArgumentException("Prefix may not be null");
		}
		if (_namespaceURI == null) {
			throw new IllegalArgumentException("Namespace URI may not be null");
		}
		namespaces.put(_namespaceURI, _prefix);
	}
	
	public void setDefaultNamespace(String _namespaceURI) {
		setPrefix(XMLConstants.DEFAULT_NS_PREFIX, _namespaceURI);
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	public String getNamespaceURI(String _prefix) {
		if (_prefix == null) {
			throw new IllegalArgumentException("Prefix may not be null");
		}
		if (XMLConstants.XML_NS_PREFIX.equals(_prefix)) {
			return XMLConstants.XML_NS_URI;
		}
		if (XMLConstants.XMLNS_ATTRIBUTE.equals(_prefix)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		}
		for (Map.Entry<String, String> entry : namespaces.entrySet()) {
			if (_prefix.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return XMLConstants.NULL_NS_URI;
	}

	/* (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	public String getPrefix(String _namespaceURI) {
		if (_namespaceURI == null) {
			throw new IllegalArgumentException("Namespace URI may not be null");
		}
		if (XMLConstants.XML_NS_URI.equals(_namespaceURI)) {
			return XMLConstants.XML_NS_PREFIX;
		}
		if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(_namespaceURI)) {
			return XMLConstants.XMLNS_ATTRIBUTE;
		}
		return namespaces.get(_namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	public Iterator getPrefixes(String _namespaceURI) {
		if (_namespaceURI == null) {
			throw new IllegalArgumentException("Namespace URI may not be null");
		}
		ArrayList<String> prefixes = new ArrayList<String>();
		if (XMLConstants.XML_NS_URI.equals(_namespaceURI)) {
			prefixes.add(XMLConstants.XML_NS_PREFIX);
		} else if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(_namespaceURI)) {
			prefixes.add(XMLConstants.XMLNS_ATTRIBUTE);
		} else for (Map.Entry<String, String> entry : namespaces.entrySet()) {
			if (_namespaceURI.equals(entry.getKey())) {
				prefixes.add(entry.getValue());
			}
		}
		return Collections.unmodifiableList(prefixes).iterator();
	}

	public Iterator getAllPrefixes() {
		return namespaces.values().iterator();
	}

	public static NamespaceContext createContext(Node _node) {
		SimpleNamespaceContext ctx = new SimpleNamespaceContext();
		while (_node != null) {
			String prefix;
			if (_node.getNamespaceURI() != null) {
				prefix = _node.getPrefix();
				if (prefix == null) {
					prefix = XMLConstants.DEFAULT_NS_PREFIX;
				}
				if (XMLConstants.NULL_NS_URI.equals(ctx.getNamespaceURI(prefix))) {
					ctx.setPrefix(prefix, _node.getNamespaceURI());
				}
			}
			if (_node.getAttributes() != null) {
				for (int i = 0; i < _node.getAttributes().getLength(); i++) {
					Attr attr = (Attr)_node.getAttributes().item(i);
					if (("xmlns".equals(attr.getPrefix())) || ("xmlns".equals(attr.getLocalName()))) {
						if ("xmlns".equals(attr.getLocalName())) {
							prefix = XMLConstants.DEFAULT_NS_PREFIX;
						} else {
							prefix = attr.getLocalName();
						}
						if (XMLConstants.NULL_NS_URI.equals(ctx.getNamespaceURI(prefix))) {
							ctx.setPrefix(prefix, attr.getNodeValue());
						}
					}
				}
			}
			_node = _node.getParentNode();
		}
		return ctx;
	}
}


/*
 * $Log:	$
 */