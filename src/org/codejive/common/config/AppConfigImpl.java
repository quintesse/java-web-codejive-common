/*
 * Created on May 9, 2005
 */
package org.codejive.common.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.codejive.common.cache.HashedObjectCache;
import org.codejive.common.cache.ObjectCache;

public class AppConfigImpl extends AppConfig {
	private ObjectCache<String, Object> cache = new HashedObjectCache<String, Object>();
	
	private Set<File> paths = new HashSet<File>();
	
	public Logger getLogger() {
		return Logger.getLogger("global");
	}
	
	public ObjectCache<String, Object> getCache() {
		return cache;
	}
	
	public void addResourcePath(File _path) {
		paths.add(_path);
	}
	
	public Set<File> getResourcePaths() {
		return paths;
	}
	
	public void addPrivateResourcePath(File _path) {
		paths.add(_path);
	}
	
	public Set<File> getPrivateResourcePaths() {
		return paths;
	}
}


/*
 * $Log:	$
 */