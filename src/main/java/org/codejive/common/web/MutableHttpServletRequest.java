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
 * Created on 14-dec-2002
 */
package org.codejive.common.web;

import java.util.*;
import java.net.*;
import java.io.*;
import javax.servlet.http.*;

/**
 * @author Tako
 */
public class MutableHttpServletRequest extends HttpServletRequestWrapper {
	HttpServletRequest request;
	Map items;
	
	public MutableHttpServletRequest(HttpServletRequest _request) {
		super(_request);
		request = _request;
		items = new HashMap(_request.getParameterMap());
	}
	
	@Override
	public String getParameter(String _name) {
		String result;
		if (items.containsKey(_name)) {
			result = ((String[])items.get(_name))[0];
		} else {
			result = null;
		}
		return result;
	}

	public void setParameter(String _name, String _value) {
		String[] newValue = new String[1];
		newValue[0] = _value;
		items.put(_name, newValue);
	}

	public void removeParameter(String _name) {
		items.remove(_name);
	}

	@Override
	public Map getParameterMap() {
		return Collections.unmodifiableMap(items);
	}

	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	@Override
	public String[] getParameterValues(String _name) {
		String[] result;
		if (items.containsKey(_name)) {
			result = ((String[])items.get(_name));
		} else {
			result = super.getParameterValues(_name);
		}
		return result;
	}

	public void setParameterValues(String _name, String[] _values) {
		items.put(_name, _values);
	}

	@Override
	public String getQueryString() {
		StringBuffer buf = new StringBuffer();
		Iterator i = items.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry)i.next();
			String[] values = (String[])entry.getValue();
			for (int j = 0; j < values.length; j++) {
				if (buf.length() > 0) {
					buf.append("&");
				}
				try {
					buf.append(URLEncoder.encode((String)entry.getKey(), "utf-8"));
					buf.append("=");
					buf.append(URLEncoder.encode(values[j], "utf-8"));
				} catch (UnsupportedEncodingException e) { /* ignore */ };
			}
		}
		return buf.toString();
	}

}
