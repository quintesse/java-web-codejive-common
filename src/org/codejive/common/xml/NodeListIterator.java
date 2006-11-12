package org.codejive.common.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Node> {
	private NodeList nodes;
	private int index;
	
	public NodeListIterator(NodeList _nodes) {
		nodes = _nodes;
		index = 0;
	}

	public boolean hasNext() {
		return ((index + 1) <= nodes.getLength());
	}

	public Node next() {
		if (index > nodes.getLength()) {
			throw new NoSuchElementException();
		}
		return nodes.item(index++);
	}

	public void remove() {
		throw new UnsupportedOperationException("Remove not supported for NodeLists");
	}

}
