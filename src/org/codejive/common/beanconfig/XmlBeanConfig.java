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
 * Created on Mar 18, 2005
 */
package org.codejive.common.beanconfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.codejive.common.beanconfig.xml.CollectionBeanConfig;
import org.codejive.common.beanconfig.xml.DefaultBeanConfig;
import org.codejive.common.beanconfig.xml.MapBeanConfig;
import org.codejive.common.beanconfig.xml.PrimitiveBeanConfig;

public class XmlBeanConfig implements BeanConfig {
	private XMLStreamReader reader;
	private XMLStreamWriter writer;
	private Map<String, String> marshallers;
	private Map<Class, BeanConfig> marshallerInstances;
	private LinkedList<Object> hierachy;
	
	public XmlBeanConfig(XMLStreamReader _reader) {
		reader = _reader;
		writer = null;
		init();
	}
	
	public XmlBeanConfig(XMLStreamWriter _writer) {
		reader = null;
		writer = _writer;
		init();
	}
	
	private void init() {
		hierachy = new LinkedList<Object>();
		marshallers = new HashMap<String, String>();
		marshallerInstances = new HashMap<Class, BeanConfig>();

		registerTypeMarshaller(Object.class, new DefaultBeanConfig(this));
		
		BeanConfig m = new PrimitiveBeanConfig(this);
		registerTypeMarshaller(String.class, m);
		registerTypeMarshaller(Character.class, m);
		registerTypeMarshaller(Number.class, m);
		registerTypeMarshaller(Boolean.class, m);
		
		registerTypeMarshaller(Collection.class, new CollectionBeanConfig(this));		
		registerTypeMarshaller(Map.class, new MapBeanConfig(this));		
	}

	public XMLStreamReader getReader() {
		return reader;
	}
	
	public XMLStreamWriter getWriter() {
		return writer;
	}
	
	public Object load() throws BeanConfigException {
		// TODO Implement load()
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void save(Object _object) throws BeanConfigException {
		try {
			String name = getNameForClass(_object.getClass());
			String namespace = getNamespaceForClass(_object.getClass());
			writer.writeStartElement(namespace, name);
			marshallObject(_object);
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			throw new BeanConfigException("Could not save object state", e);
		} catch (SecurityException e) {
			throw new BeanConfigException("Could not save object state", e);
		}
	}
	
	public String getNameForClass(Class _class) {
		// Simple implementation for now that returns the object's class name
		String name = _class.getName();
		name = name.substring(_class.getPackage().getName().length() + 1);
		name = name.replace('$', '-');
		return name;
	}
	
	public String getNamespaceForClass(Class _class) {
		// Simple implementation for now that returns "java://objects.package.name"
		return "java://" + _class.getPackage().getName();
	}
	
	public void marshallObject(Object _object) throws BeanConfigException {
		if (_object != null) {
			hierachy.addLast(_object);
			BeanConfig marshaller = getTypeMarshaller(_object.getClass());
			marshaller.save(_object);
			hierachy.removeLast();
		}
	}

	public boolean circularReference(Object _object) {
		boolean found = false;
		for (int i = 0; !found && (i < (hierachy.size() - 1)); i++) {
			Object obj = hierachy.get(i);
			found = (_object == obj);
		}
		return found;
	}
	
	public void registerTypeMarshaller(Class _class, BeanConfig _marshaller) {
		marshallerInstances.put(_class, _marshaller);
	}

	public void registerTypeMarshaller(String _className, String _marshallerClassName) {
		marshallers.put(_className, _marshallerClassName);
	}
	
	private BeanConfig findClassMarshaller(Class _class) throws BeanConfigException {
		BeanConfig m = marshallerInstances.get(_class);
		if (m == null) {
			for (Map.Entry<String, String> item : marshallers.entrySet()) {
				boolean match = item.getKey().equals(_class.getName());
				if (match) {
					try {
						String marshallerClassName = item.getValue();
						Class c = Class.forName(marshallerClassName);
						Class paramTypes[] = { XmlBeanConfig.class };
						Object paramValues[] = { this };
						m = (BeanConfig)c.getConstructor(paramTypes).newInstance(paramValues);
					} catch (Exception e) {
						throw new BeanConfigException("Failed to create type marshaller '" + item.getValue() + "'", e);
					}
				}
			}
		}
		return m;
	}

	private BeanConfig findTypeMarshaller(Class _class) throws BeanConfigException {
		// Check if there is an exact match for this class
		BeanConfig m = findClassMarshaller(_class);
		if (m == null) {
			// If not, try all of its interfaces
			Class[] interfaces = _class.getInterfaces();
			if (interfaces != null) {
				for (Class c : interfaces) {
					m = findTypeMarshaller(c);
					if (m != null) {
						break;
					}
				}
			}
			// If still no match, try its superclass
			if ((m == null) && (_class != Object.class) && (_class.getSuperclass() != null)) {
				m = findTypeMarshaller(_class.getSuperclass());
			}
		}
		return m;
	}

	private BeanConfig getTypeMarshaller(Class _class) throws BeanConfigException {
		BeanConfig m = findTypeMarshaller(_class);
		if (m != null) {
			registerTypeMarshaller(_class, m);
		}
		return m;
	}
}


/*
 * $Log:	$
 */