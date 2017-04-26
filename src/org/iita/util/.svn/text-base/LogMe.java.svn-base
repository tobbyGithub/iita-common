/**
 * iita-common Oct 8, 2010
 */
package org.iita.util;

import org.apache.commons.logging.Log;

/**
 * @author mobreza
 */
public class LogMe {
	private LogMe() {

	}

	public static void trace(final Log log, final String format, final Object... args) {
		if (log.isTraceEnabled())
			log.trace(String.format(format, args));
	}

	public static void debug(final Log log, final String format, final Object... args) {
		if (log.isDebugEnabled())
			log.debug(String.format(format, args));
	}

	public static void info(final Log log, final String format, final Object... args) {
		if (log.isInfoEnabled())
			log.info(String.format(format, args));
	}

	public static void warn(final Log log, final String format, final Object... args) {
		if (log.isWarnEnabled())
			log.warn(String.format(format, args));
	}

	public static void error(final Log log, final String format, final Object... args) {
		if (log.isErrorEnabled())
			log.error(String.format(format, args));
	}

	public static void fatal(final Log log, final String format, final Object... args) {
		if (log.isFatalEnabled())
			log.fatal(String.format(format, args));
	}

}
