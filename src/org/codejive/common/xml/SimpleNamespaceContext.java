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