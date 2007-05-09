/*
 * [codejive-common] ...
 * 
 * Copyright (C) 2007 tako
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
 * Created on May 2, 2007
 */

package org.codejive.common.beanconfig.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codejive.common.beanconfig.BeanConfig;
import org.codejive.common.beanconfig.BeanConfigException;
import org.codejive.common.beanconfig.XmlBeanConfig;

public class PrimitiveBeanConfig implements BeanConfig {
	private XmlBeanConfig parent;
	
	public PrimitiveBeanConfig(XmlBeanConfig _parent) {
		parent = _parent;
	}

	public Object load() throws BeanConfigException {
		// TODO Implement load()
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void save(Object _object) throws BeanConfigException {
		try {
			XMLStreamWriter swriter = parent.getWriter();
			swriter.writeCharacters(typeToString(_object));
		} catch (XMLStreamException e) {
			throw new BeanConfigException("Could not save object state", e);
		}
	}
	
	public String typeToString(Object _value) {
		return _value.toString();
	}
	
	public String stringToType(String _value) {
		// TODO
		return null;
	}
}