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

import java.text.*;

/**
 * @author Tako
 *
 * Some handy methods operating on Strings
 */
public class StringHelper {

	public static boolean isNumeric(Object _value) {
		boolean result = false;
		if (_value instanceof String) {
			result = ((String)_value).matches("[\\d]+");
		} else if (_value instanceof Integer) {
			result = true;
		};
		return result;
	}
	
	public static String formatLong(long _number, int _minDigits, int _maxDigits) {
		NumberFormat fmt = NumberFormat.getIntegerInstance();
		fmt.setMaximumIntegerDigits(_maxDigits);
		fmt.setMinimumIntegerDigits(_minDigits);
		return fmt.format(_number);
	}
	
	public static String escape(String _text) {
		String result;
		if (_text != null) {
			result = "";
			for (int i = 0; i < _text.length(); i++) {
				char c = _text.charAt(i);
				NumberFormat fmt = NumberFormat.getIntegerInstance();
				fmt.setMaximumIntegerDigits(4);
				fmt.setMinimumIntegerDigits(4);
				result += "\\u" + fmt.format(c);
			}
		} else {
			result = null;
		}
		return result;
	}
	
	public static boolean strToBool(String _bool, boolean _default) {
		boolean value;
		if ((_bool != null) && (_bool.length() > 0)) {
			value = Boolean.parseBoolean(_bool);
		} else {
			value = _default;
		}
		return value;
	}
	
	public static int strToInt(String _number, int _default) {
		int num;
		if ((_number != null) && (_number.length() > 0)) {
			num = Integer.parseInt(_number);
		} else {
			num = _default;
		}
		return num;
	}
	
	public static long strToLong(String _number, long _default) {
		long num;
		if ((_number != null) && (_number.length() > 0)) {
			num = Long.parseLong(_number);
		} else {
			num = _default;
		}
		return num;
	}
	
	public static float strToFloat(String _number, float _default) {
		float num;
		if ((_number != null) && (_number.length() > 0)) {
			num = Float.parseFloat(_number);
		} else {
			num = _default;
		}
		return num;
	}
	
	public static double strToDouble(String _number, double _default) {
		double num;
		if ((_number != null) && (_number.length() > 0)) {
			num = Double.parseDouble(_number);
		} else {
			num = _default;
		}
		return num;
	}
}
