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
 * Created on November 15, 2006
 */
package org.codejive.common.config;

import java.io.File;
import java.util.logging.Logger;

import org.codejive.common.CodejiveException;
import org.codejive.common.cache.FileUpdateMonitor;
import org.codejive.common.xml.XmlHelper;
import org.w3c.dom.Document;

/**
 * Utility class to load and cache XML documents found on the resource path.
 * The parsed DOM documents are cached while changes to the original XML files
 * are monitored wich means that the updated documents are automatically
 * reflected in the cache. This also means that you should normally not hold
 * on to any references to these documents.
 * @author tako
 * @see AppConfig ObjectCache
 */
public class ConfigFiles {

	private static Logger logger = Logger.getLogger(ConfigFiles.class.getName());
	private static AppConfig config = AppConfig.getInstance();

	/**
	 * Returns the parsed document found at the given location.
	 * For this the application's resource paths are searched to see
	 * if the given file can be found there. If the document can be parsed
	 * correctly it will be cached in the application's global cache and on
	 * all subsequent calls to this method the cached object will be returned
	 * unless the original file has been changed in the meantime which will
	 * result in a reload of the XML document.
	 * @param _fileName the location of the XML document to load
	 * @return the parsed XML document
	 * @throws ConfigurationException runtime exception that will be thrown
	 * if the XML document can't be found or can't be parsed.
	 * @see AppConfig ObjectCache
	 */
	public static Document getDocument(String _fileName) {
		Document doc = (Document)config.getCache(ConfigFiles.class).get(_fileName);
		if (doc == null) {
			File file = config.getResourceFile(_fileName);
			try {
				logger.info("ConfigFiles::getDocument - Reading '" + file.getAbsolutePath() + "'");
				doc = XmlHelper.readDocument(file);
			} catch (CodejiveException e) {
				throw new ConfigurationException(e);
			}
			FileUpdateMonitor mon = new FileUpdateMonitor(file);
			config.getCache(ConfigFiles.class).put(_fileName, doc, mon);
		}
		return doc;
	}

	/**
	 * Returns the parsed document found at the given location.
	 * For this the application's private resource paths are searched to see
	 * if the given file can be found there. If the document can be parsed
	 * correctly it will be cached in the application's global cache and on
	 * all subsequent calls to this method the cached object will be returned
	 * unless the original file has been changed in the meantime which will
	 * result in a reload of the XML document.
	 * @param _fileName the location of the XML document to load
	 * @return the parsed XML document
	 * @throws ConfigurationException runtime exception that will be thrown
	 * if the XML document can't be found or can't be parsed.
	 * @see AppConfig ObjectCache
	 */
	public static Document getPrivateDocument(String _fileName) {
		Document doc = (Document)config.getCache(ConfigFiles.class).get(_fileName);
		if (doc == null) {
			File file = config.getPrivateResourceFile(_fileName);
			try {
				logger.info("ConfigFiles::getPrivateDocument - Reading '" + file.getAbsolutePath() + "'");
				doc = XmlHelper.readDocument(file);
			} catch (CodejiveException e) {
				throw new ConfigurationException(e);
			}
			FileUpdateMonitor mon = new FileUpdateMonitor(file);
			config.getCache(ConfigFiles.class).put(_fileName, doc, mon);
		}
		return doc;
	}
}
