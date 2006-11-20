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
 * Created on November 15, 2006
 */
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
	// (we want all access to the underlying reader to go through next()
	// but it seems the implementation of nextTag() doesn't use it)
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
