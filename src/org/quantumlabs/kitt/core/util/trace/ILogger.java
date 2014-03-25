package org.quantumlabs.kitt.core.util.trace;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public interface ILogger
{
    public ILogger CONSOLE_LOGGER = new ILogger() {
		
		@Override
		public void log(String message) {
			System.out.println(message);
		}
	};
	public ILogger DEFAULT_LOGGER = new ILogger() {
		final java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();;
		{
			try {
				Handler handler = new FileHandler("c:\\kitt.log");
				logger.addHandler(handler);
			} catch (Exception e) {
				//ignore
			}
		}
		@Override
		public void log(String message) {
			logger.log(Level.ALL, message);
		}
	};

	public void log( String message );
}
