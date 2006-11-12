package org.codejive.common.xml;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableNodeList implements NodeList, Iterable<Node> {
	private NodeList list;
	
	public IterableNodeList(NodeList _list) {
		list = _list;
	}

	public Node item(int index) {
		return list.item(index);
	}

	public int getLength() {
		return list.getLength();
	}

	public Iterator<Node> iterator() {
		return new NodeListIterator(this);
	}

}
