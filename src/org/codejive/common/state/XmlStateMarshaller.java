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
package org.codejive.common.state;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlStateMarshaller implements StateMarshaller {
	private XMLStreamWriter writer;
	private Map<String, String> marshallers;
	private Map<Class, StateMarshaller> marshallerInstances;
	
	private static final String NAMESPACE = "http://www.codejive.org/NS/common/state";

	public XmlStateMarshaller(XMLStreamWriter _writer) {
		writer = _writer;
		marshallers = new HashMap<String, String>();
		marshallerInstances = new HashMap<Class, StateMarshaller>();

		registerTypeMarshaller(Object.class, new DefaultStateMarshaller(this));
		
		StateMarshaller m = new PrimitiveStateMarshaller(this);
		registerTypeMarshaller(String.class, m);
		registerTypeMarshaller(Number.class, m);
		
		registerTypeMarshaller(Collection.class, new CollectionStateMarshaller(this));		
		registerTypeMarshaller(Map.class, new MapStateMarshaller(this));		
	}

	public XMLStreamWriter getWriter() {
		return writer;
	}
	
	public void marshall(Object _object) throws StateMarshallerException {
		String itemName;
		String itemTag;
		String itemNamespace;
		boolean named, typed;
		
		try {
			itemName = null;
			itemTag = "object";
			itemNamespace = NAMESPACE;
			named = true;
			typed = true;
			
			if (!itemTag.startsWith("@")) {
				// First make an element for this property that will hold its value
				if (itemNamespace.length() > 0) {
					writer.writeStartElement(itemNamespace, itemTag);
				} else {
					writer.writeStartElement(itemTag);
				}
				// Check if we need to set the property's name
				if (named && (itemName != null)) {
					writer.writeAttribute("name", itemName);
				}
				// Check if we need to set the object's type info
				if (typed && (_object != null)) {
					writer.writeAttribute("type", _object.getClass().getName());
				}
				marshallObject(_object);
				writer.writeEndElement();
			} else {
				// Write the object's value as an attribute
				writer.writeAttribute(itemTag.substring(1), _object.toString());
			}
		} catch (XMLStreamException e) {
			throw new StateMarshallerException("Could not save object state", e);
		} catch (SecurityException e) {
			throw new StateMarshallerException("Could not save object state", e);
		}
	}
	
	public void marshallObject(Object _object) throws StateMarshallerException {
		try {
			if (_object != null) {
				StateMarshaller marshaller = getTypeMarshaller(_object.getClass());
				marshaller.marshall(_object);
			} else {
				// Indicate that the property has a null value
				writer.writeAttribute("isNull", "true");
			}
		} catch (XMLStreamException e) {
			throw new StateMarshallerException("Could not save object state", e);
		}
	}

	public void registerTypeMarshaller(Class _class, StateMarshaller _marshaller) {
		marshallerInstances.put(_class, _marshaller);
	}

	public void registerTypeMarshaller(String _className, String _marshallerClassName) {
		marshallers.put(_className, _marshallerClassName);
	}
	
	private StateMarshaller findClassMarshaller(Class _class) throws StateMarshallerException {
		StateMarshaller m = marshallerInstances.get(_class);
		if (m == null) {
			for (Map.Entry<String, String> item : marshallers.entrySet()) {
				boolean match = item.getKey().equals(_class.getName());
				if (match) {
					try {
						String marshallerClassName = item.getValue();
						Class c = Class.forName(marshallerClassName);
						Class paramTypes[] = { XmlStateMarshaller.class };
						Object paramValues[] = { this };
						m = (StateMarshaller)c.getConstructor(paramTypes).newInstance(paramValues);
					} catch (Exception e) {
						throw new StateMarshallerException("Failed to create type marshaller '" + item.getValue() + "'", e);
					}
				}
			}
		}
		return m;
	}

	private StateMarshaller findTypeMarshaller(Class _class) throws StateMarshallerException {
		// Check if there is an exact match for this class
		StateMarshaller m = findClassMarshaller(_class);
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

	private StateMarshaller getTypeMarshaller(Class _class) throws StateMarshallerException {
		StateMarshaller m = findTypeMarshaller(_class);
		if (m != null) {
			registerTypeMarshaller(_class, m);
		}
		return m;
	}

	private static class DefaultStateMarshaller implements StateMarshaller {
		private XmlStateMarshaller parent;
		
		public DefaultStateMarshaller(XmlStateMarshaller _parent) {
			parent = _parent;
		}
		
		public void marshall(Object _object) throws StateMarshallerException {
			try {
				// Now retrieve the object's properties
				BeanInfo info = Introspector.getBeanInfo(_object.getClass());
				PropertyDescriptor[] properties = info.getPropertyDescriptors();
				if (properties != null) {
					for (PropertyDescriptor prop : properties) {
						// We always ignore the 'class' property
						if (!"class".equals(prop.getName())) {
							Method readMethod = prop.getReadMethod();
							if (readMethod != null) {
								// Retrieve the property's value
								try {
									Object value = readMethod.invoke(_object, null);
									parent.marshall(value);
								} catch (IllegalAccessException e) {
									throw new StateMarshallerException("Failed to read value for property '" + prop.getName() + "'", e);
								} catch (InvocationTargetException e) {
									throw new StateMarshallerException("Failed to read value for property '" + prop.getName() + "'", e);
								}
							} else {
								// Silently ignore write-only properties
							}
						}
					}
				}
			} catch (IntrospectionException e) {
				throw new StateMarshallerException("Could not save object state", e);
			} catch (SecurityException e) {
				throw new StateMarshallerException("Could not save object state", e);
			}
		}
	}

	private class PrimitiveStateMarshaller implements StateMarshaller {
		private XmlStateMarshaller parent;
		
		public PrimitiveStateMarshaller(XmlStateMarshaller _parent) {
			parent = _parent;
		}

		public void marshall(Object _object) throws StateMarshallerException {
			try {
				XMLStreamWriter swriter = parent.getWriter();
				swriter.writeCharacters(typeToString(_object));
			} catch (XMLStreamException e) {
				throw new StateMarshallerException("Could not save object state", e);
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

	private class CollectionStateMarshaller implements StateMarshaller {
		private XmlStateMarshaller parent;
		
		public CollectionStateMarshaller(XmlStateMarshaller _parent) {
			parent = _parent;
		}

		public void marshall(Object _object) throws StateMarshallerException {
			try {
				XMLStreamWriter swriter = parent.getWriter();
				Collection items = (Collection)_object;
				for (Object obj : items) {
					swriter.writeStartElement(NAMESPACE, "item");
					parent.marshallObject(obj);
					swriter.writeEndElement();
				}
			} catch (XMLStreamException e) {
				throw new StateMarshallerException("Could not save object state", e);
			}
		}
	}

	private class MapStateMarshaller implements StateMarshaller {
		private XmlStateMarshaller parent;
		
		public MapStateMarshaller(XmlStateMarshaller _parent) {
			parent = _parent;
		}

		public void marshall(Object _object) throws StateMarshallerException {
			try {
				XMLStreamWriter swriter = parent.getWriter();
				Map items = (Map)_object;
				for (Object obj : items.entrySet()) {
					Entry entry = (Entry)obj;
					swriter.writeStartElement(NAMESPACE, "item");
					swriter.writeStartElement(NAMESPACE, "key");
					parent.marshallObject(entry.getKey());
					swriter.writeEndElement();
					swriter.writeStartElement(NAMESPACE, "value");
					parent.marshallObject(entry.getValue());
					swriter.writeEndElement();
					swriter.writeEndElement();
				}
			} catch (XMLStreamException e) {
				throw new StateMarshallerException("Could not save object state", e);
			}
		}
	}
}


/*
 * $Log:	$
 */