/*
 * Created on Oct 13, 2005
 */
package org.codejive.common.config;

import org.codejive.common.CodejiveRuntimeException;

public class ConfigurationException extends CodejiveRuntimeException {

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}


/*
 * $Log:	$
 */