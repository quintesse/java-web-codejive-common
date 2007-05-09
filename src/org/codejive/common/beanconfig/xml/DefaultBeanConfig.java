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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codejive.common.beanconfig.BeanConfig;
import org.codejive.common.beanconfig.BeanConfigException;
import org.codejive.common.beanconfig.XmlBeanConfig;

public class DefaultBeanConfig implements BeanConfig {
	private XmlBeanConfig parent;
	
	public DefaultBeanConfig(XmlBeanConfig _parent) {
		parent = _parent;
	}
	
	public Object load() throws BeanConfigException {
		// TODO Implement load()
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void save(Object _object) throws BeanConfigException {
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
								// Write an element or attribute for the property (if not null)
								Object value = readMethod.invoke(_object, (Object[])null);
								if ((value != null) && !parent.circularReference(value)) {
									XMLStreamWriter swriter = parent.getWriter();
									String name = prop.getName();
									if (needClassDef(prop.getPropertyType(), value.getClass())) {
										String namespace = parent.getNamespaceForClass(value.getClass());
										swriter.writeStartElement(namespace, name);
										swriter.writeAttribute("class", parent.getNameForClass(value.getClass()));
									} else {
										String namespace = parent.getNamespaceForClass(_object.getClass());
										swriter.writeStartElement(namespace, name);
									}
									parent.marshallObject(value);
									swriter.writeEndElement();
								}
							} catch (IllegalAccessException e) {
								throw new BeanConfigException("Failed to read value for property '" + prop.getName() + "'", e);
							} catch (InvocationTargetException e) {
								throw new BeanConfigException("Failed to read value for property '" + prop.getName() + "'", e);
							} catch (XMLStreamException e) {
								throw new BeanConfigException("Could not save object state for property '" + prop.getName() + "'", e);
							}
						} else {
							// Silently ignore write-only properties
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			throw new BeanConfigException("Could not save object state", e);
		} catch (SecurityException e) {
			throw new BeanConfigException("Could not save object state", e);
		}
	}

	private boolean needClassDef(Class<?> propertyType, Class<?> valueType) {
		boolean needDef = propertyType != valueType;
		if (needDef && compatibleTypes.containsKey(propertyType)) {
			Class<?> compatibleClass = compatibleTypes.get(propertyType);
			needDef = (compatibleClass != valueType);
		}
		return needDef;
	}
	
	private static HashMap<Class<?>, Class<?>> compatibleTypes = new HashMap<Class<?>, Class<?>>();
	
	static {
		compatibleTypes.put(boolean.class, Boolean.class);
		compatibleTypes.put(char.class, Character.class);
		compatibleTypes.put(byte.class, Byte.class);
		compatibleTypes.put(short.class, Short.class);
		compatibleTypes.put(int.class, Integer.class);
		compatibleTypes.put(long.class, Long.class);
		compatibleTypes.put(float.class, Float.class);
		compatibleTypes.put(double.class, Double.class);
	}
}