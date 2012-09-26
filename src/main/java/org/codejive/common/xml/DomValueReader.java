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
 * Created on Oct 11, 2005
 */
package org.codejive.common.xml;

import java.util.Iterator;
import java.util.Stack;

import javax.xml.namespace.NamespaceContext;

import org.codejive.common.util.StringHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomValueReader {
	private Document doc;
	private NamespaceContext context;
	private Node anchor;
	private Stack<Node> anchorStack;
	
	public DomValueReader(Document _doc, NamespaceContext _context) {
		doc = _doc;
		context = _context;
		anchor = _doc;
		anchorStack = new Stack<Node>();
	}

	public Document getDocument() {
		return doc;
	}
	
	public NamespaceContext getContext() {
		return context;
	}

	public Node getAnchor() {
		return anchor;
	}
	
	public Node goTo(Node _node) {
		anchor = _node;
		return _node;
	}
	
	public Node goTo(String _path) {
		Node parent = getAnchor();
		Node node = XPathHelper.selectSingleNode(parent, _path, context);
		if (node != null) {
			goTo(node);
		}
		return node;
	}
	
	public Node pop() {
		Node node = anchorStack.pop();
		return goTo(node);
	}
	
	public Node push(Node _node) {
		anchorStack.push(getAnchor());
		return goTo(_node);
	}
	
	public Node push(String _path) {
		Node parent = getAnchor();
		Node node = XPathHelper.selectSingleNode(parent, _path, context);
		if (node != null) {
			push(node);
		}
		return node;
	}
	
	public Node node(String _path) {
		return XPathHelper.selectSingleNode(anchor, _path, context);
	}
	
	public IterableNodeList nodes() {
		return new IterableNodeListExt(getAnchor().getChildNodes());
	}
	
	public IterableNodeList nodes(String _path) {
		return new IterableNodeListExt(XPathHelper.selectNodes(anchor, _path, context));
	}
	
	public Element element(String _path) {
		return (Element)XPathHelper.selectSingleNode(anchor, _path, context);
	}
	
	public IterableNodeList elements() {
		return new IterableNodeListExt(XPathHelper.selectNodes(anchor, "*", context));
	}
	
	public IterableNodeList elements(String _path) {
		return new IterableNodeListExt(XPathHelper.selectNodes(anchor, _path, context));
	}
	
	public String value() {
		return getAnchor().getTextContent();
	}
	
	public String value(String _path) {
		return value(_path, null);
	}
	
	public String value(String _path, String _default) {
		String result = _default;
		Node node = node(_path);
		if (node != null) {
			result = node.getTextContent();
		}
		return result;
	}
	
	public boolean booleanValue(boolean _default) {
		boolean result = _default;
		String value = value();
		if (value != null) {
			result = Boolean.parseBoolean(value);
		}
		return result;
	}
	
	public boolean booleanValue(String _path, boolean _default) {
		boolean result = _default;
		String value = value(_path);
		if (value != null) {
			result = Boolean.parseBoolean(value);
		}
		return result;
	}
	
	public int intValue(int _default) {
		int result = _default;
		String value = value();
		if (value != null) {
			result = Integer.parseInt(value);
		}
		return result;
	}
	
	public int intValue(String _path, int _default) {
		int result = _default;
		String value = value(_path);
		if (value != null) {
			result = Integer.parseInt(value);
		}
		return result;
	}
	
	public long longValue(long _default) {
		long result = _default;
		String value = value();
		if (value != null) {
			result = Long.parseLong(value);
		}
		return result;
	}
	
	public long longValue(String _path, long _default) {
		long result = _default;
		String value = value(_path);
		if (value != null) {
			result = Long.parseLong(value);
		}
		return result;
	}
	
	public float floatValue(float _default) {
		float result = _default;
		String value = value();
		if (value != null) {
			result = Float.parseFloat(value);
		}
		return result;
	}
	
	public float floatValue(String _path, float _default) {
		float result = _default;
		String value = value(_path);
		if (value != null) {
			result = Float.parseFloat(value);
		}
		return result;
	}
	
	public double doubleValue(double _default) {
		double result = _default;
		String value = value();
		if (value != null) {
			result = Double.parseDouble(value);
		}
		return result;
	}
	
	public double doubleValue(String _path, double _default) {
		double result = _default;
		String value = value(_path);
		if (value != null) {
			result = Double.parseDouble(value);
		}
		return result;
	}
	
	public static String getAttributeValue(Element _elem, String _name) {
		String result;
		Attr attr = _elem.getAttributeNode(_name);
		if (attr != null) {
			result = attr.getValue();
		} else {
			result = null;
		}
		return result;
	}

	public static boolean getBooleanAttribute(Element _elem, String _name, boolean _default) {	
		boolean result = StringHelper.strToBool(getAttributeValue(_elem, _name), _default);
		return result;
	}
	
	public static int getIntegerAttribute(Element _elem, String _name, int _default) {	
		int result = StringHelper.strToInt(getAttributeValue(_elem, _name), _default);
		return result;
	}
	
	public static long getLongAttribute(Element _elem, String _name, long _default) {	
		long result = StringHelper.strToLong(getAttributeValue(_elem, _name), _default);
		return result;
	}
	
	public static float getFloatAttribute(Element _elem, String _name, float _default) {	
		float result = StringHelper.strToFloat(getAttributeValue(_elem, _name), _default);
		return result;
	}
	
	public static double getDoubleAttribute(Element _elem, String _name, double _default) {	
		double result = StringHelper.strToDouble(getAttributeValue(_elem, _name), _default);
		return result;
	}
	
	private class IterableNodeListExt extends IterableNodeList {
		public IterableNodeListExt(NodeList _list) {
			super(_list);
		}

		@Override
		public Iterator<Node> iterator() {
			return new NodeListIteratorExt(super.iterator());
		}
	}
	
	private class NodeListIteratorExt implements Iterator<Node> {
		private Iterator<Node> iter;
		
		public NodeListIteratorExt(Iterator<Node> _iter) {
			iter = _iter;
		}

		public boolean hasNext() {
			return iter.hasNext();
		}

		public Node next() {
			return goTo(iter.next());
		}

		public void remove() {
			iter.remove();
		}
	}
}


/*
 * $Log:	$
 */