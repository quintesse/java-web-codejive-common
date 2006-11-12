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

import org.w3c.dom.Element;

/**
 * @author tako
 * @version $Revision:	$
 */
public class XmlOutputFactory {

	protected XmlOutputFactory() {
		// Nothing to do here
	}
	
	public DomWriter createDomWriter() {
		return new DomWriter();
	}

	public DomWriter createDomWriter(Element _elem) {
		return new DomWriter(_elem);
	}

	public static XmlOutputFactory newInstance() {
		return new XmlOutputFactory();
	}
}


/*
 * $Log:	$
 */