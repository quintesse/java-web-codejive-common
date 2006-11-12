/*
 * Created on Nov 29, 2004
 */
package org.codejive.common.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author tako
 * @version $Revision:	$
 */
public class XPathHelper {

	public static Node selectSingleNode(Node _source, String _xpathExpression, NamespaceContext _context) {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(_context);
		Node result;
		try {
			result = (Node)xpath.evaluate(_xpathExpression, _source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static Node selectSingleNode(NodeList _source, String _xpathExpression, NamespaceContext _context) {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(_context);
		Node result;
		try {
			result = (Node)xpath.evaluate(_xpathExpression, _source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static IterableNodeList selectNodes(Node _source, String _xpathExpression, NamespaceContext _context) {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(_context);
		NodeList result;
		try {
			result = (NodeList)xpath.evaluate(_xpathExpression, _source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		return new IterableNodeList(result);
	}

	public static IterableNodeList selectNodes(NodeList _source, String _xpathExpression, NamespaceContext _context) {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		xpath.setNamespaceContext(_context);
		NodeList result;
		try {
			result = (NodeList)xpath.evaluate(_xpathExpression, _source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		return new IterableNodeList(result);
	}
}


/*
 * $Log:	$
 */