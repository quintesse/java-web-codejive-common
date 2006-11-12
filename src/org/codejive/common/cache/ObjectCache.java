/*
 * Created on Apr 14, 2005
 */
package org.codejive.common.cache;

import java.util.Map;

public interface ObjectCache<K, T> extends Map<K, T> {
	public T put(K _key, T _value, UpdateMonitor _monitor);
}


/*
 * $Log:	$
 */