/*
 * [codejive-common] Codejive utility classes
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
 * Created on 4-jul-2002
 */
package org.codejive.common.util;

/**
 * @author Tako
 *
 * Contains methods to make life easier when working with arrays
 */
public class ArrayHelper {

	public static int arraySize(Object[] _array) {
		int size;
		if (_array != null) {
			size = _array.length;
		} else {
			size = 0;
		}
		return size;
	}
	
	public static Object[] grow(Object[] _array, int _growBy, Class _itemClass) {
		Object newArray[] = (Object [])java.lang.reflect.Array.newInstance(_itemClass, arraySize(_array) + _growBy);
		if (_array != null) {
			java.lang.System.arraycopy(_array, 0, newArray, 0, arraySize(_array));
		}
		return newArray;
	}
	
	public static Object[] grow(Object[] _array, Class _itemClass) {
		return grow(_array, 1, _itemClass);
	}
	
	public static Object[] grow(Object[] _array, Object[] _items, Class _itemClass) {
		Object newArray[] = grow(_array, arraySize(_items), _itemClass);
		if (_array != null) {
			java.lang.System.arraycopy(_items, 0, newArray, arraySize(_array), arraySize(_items));
		}
		return newArray;
	}
	
	public static Object[] grow(Object[] _array, Object _item, Class _itemClass) {
		Object newArray[] = grow(_array, 1, _itemClass);
		newArray[arraySize(_array)] = _item;
		return newArray;
	}
}
