/*
 * Created on Apr 22, 2005
 */
package org.codejive.common.cache;

import java.io.File;

public class FileUpdateMonitor implements UpdateMonitor {
	private File file;
	
	private long lastModified;
	
	public FileUpdateMonitor(File _file) {
		file = _file;
		lastModified = file.lastModified();
	}

	public boolean isUpdated() {
		return (file.lastModified() != lastModified);
	}

}


/*
 * $Log:	$
 */