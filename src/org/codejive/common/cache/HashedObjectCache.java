/*
 * Created on Apr 14, 2005
 */
package org.codejive.common.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashedObjectCache<K, T> implements ObjectCache<K, T> {
	private HashMap<K, CachedItem> items;
	
	private class CachedItem {
		public K key;
		public T object;
		public UpdateMonitor monitor;
		
		public CachedItem(K _key, T _object, UpdateMonitor _monitor) {
			key = _key;
			object = _object;
			monitor = _monitor;
		}
	}

	public HashedObjectCache() {
		items = new HashMap<K, CachedItem>();
	}
	
	public T put(K _key, T _value) {
		return put(_key, _value, null);
	}
	
	public T put(K _key, T _value, UpdateMonitor _monitor) {
		CachedItem item = new CachedItem(_key, _value, _monitor);
		items.put(_key, item);
		return _value;
	}

	public void putAll(Map<? extends K, ? extends T> t) {
		// TODO Auto-generated method stub
		
	}

	public int size() {
		return items.size();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public boolean containsKey(Object _key) {
		return items.containsKey(_key);
	}

	public boolean containsValue(Object _value) {
		return items.containsValue(_value);
	}

	public T get(Object _key) {
		T value = null;
		CachedItem item = items.get(_key);
		if (item != null) {
			if ((item.monitor == null) || (!item.monitor.isUpdated())) {
				value = item.object;
			} else {
				items.remove(_key);
			}
		}
		return value;
	}

	public T remove(Object _key) {
		T value = null;
		CachedItem item = items.remove(_key);
		if (item != null) {
			value = item.object;
		}
		return value;
	}

	public void clear() {
		items.clear();
	}

	public Set<K> keySet() {
		return items.keySet();
	}

	public Collection<T> values() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Entry<K, T>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
}


/*
 * $Log:	$
 */