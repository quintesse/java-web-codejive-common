package org.codejive.common.io.file;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
	private Order order;
	
	public enum Order { ASCENDING, DESCENDING };
	
	public FileComparator(Order _order) {
		order = _order;
	}
	
	public int compare(File _file1, File _file2) {
		int result;
		if (order == Order.ASCENDING) {
			result = _file1.getName().compareTo(_file2.getName());
		} else {
			result = _file2.getName().compareTo(_file1.getName());
		}
		return result;
	}
}
