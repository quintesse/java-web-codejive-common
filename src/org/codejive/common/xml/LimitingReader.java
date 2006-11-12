package org.codejive.common.xml;

import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

public class LimitingReader extends StreamReaderDelegate {
	private int startElementCount;
	
	public LimitingReader(XMLStreamReader _reader) {
		super(_reader);
		if (_reader.getEventType() != START_ELEMENT) {
			throw new IllegalStateException("Parent reader not pointing to a START_ELEMENT");
		}
		startElementCount = 1;
	}

	@Override
	public boolean hasNext() throws XMLStreamException {
		boolean result;
		if ((getEventType() == END_ELEMENT) && (startElementCount == 0)) {
			result = false;
		} else {
			result = super.hasNext();
		}
		return result;
	}

	@Override
	public int next() throws XMLStreamException {
		if (!hasNext()) {
			throw new NoSuchElementException("Trying to read past end of stream");
		}
		int result = super.next();
		switch (getEventType()) {
		case START_ELEMENT:
			startElementCount++;
			break;
		case END_ELEMENT:
			startElementCount--;
			break;
		}
		return result;
	}

	// To make sure that this is implemented in a way that works for us
	@Override
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

	@Override
	public void close() throws XMLStreamException {
		// We don't allow the wrapper to close the parent reader
	}
}
