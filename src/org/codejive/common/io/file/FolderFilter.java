package org.codejive.common.io.file;

import java.io.File;
import java.io.FileFilter;

public class FolderFilter implements FileFilter {
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}
}

