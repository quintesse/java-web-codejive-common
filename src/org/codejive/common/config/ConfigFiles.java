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
