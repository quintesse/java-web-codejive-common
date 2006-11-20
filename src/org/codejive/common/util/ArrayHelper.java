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
