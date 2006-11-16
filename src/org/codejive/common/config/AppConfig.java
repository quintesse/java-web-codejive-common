/*
 * Created on Oct 12, 2005
 */
package org.codejive.common.config;

import java.io.File;
import java.util.Set;

import org.codejive.common.cache.HashedObjectCache;
import org.codejive.common.cache.ObjectCache;

public abstract class AppConfig {
	public abstract ObjectCache getCache();
	public abstract Set<File> getResourcePaths();
	public abstract Set<File> getPrivateResourcePaths();
	
	private static AppConfig config = null;
	
	public ObjectCache getCache(Class _owner) {
		ObjectCache ownedCache = (ObjectCache)getCache().get(_owner.getName());
		if (ownedCache == null) {
			ownedCache = new HashedObjectCache<String, Object>();
			getCache().put(_owner.getName(), ownedCache);
		}
		return ownedCache;
	}
	
	public File getResourceFile(String _fileName) {
		File file = new File(_fileName);
		if (file.exists()) {
			return file;
		}
		for (File path : getResourcePaths()) {
			file = new File(path, _fileName);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}
	
	public File getPrivateResourceFile(String _fileName) {
		File file = new File(_fileName);
		if (file.exists()) {
			return file;
		}
		for (File path : getPrivateResourcePaths()) {
			file = new File(path, _fileName);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	public synchronized static AppConfig getInstance() throws ConfigurationException {
		if (config == null) {
			String className = System.getProperty("org.codejive.common.config.AppConfig.implementation");
			if (className == null) {
				// Provide a fallback name
				className = "org.codejive.common.config.AppConfigImpl";
			}
			try {
				config = (AppConfig)Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new ConfigurationException(e);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException(e);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(e);
			}
		}
		return config;
	}
}


/*
 * $Log:	$
 */