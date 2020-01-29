package io.nessus.weka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSupport {
	
    protected final Logger LOG = LoggerFactory.getLogger(getClass().getPackage().getName());
    
    protected void logError(String msg, Object... args) {
        LOG.error(msg, args);
    }
    
    protected void logWarn(String msg, Object... args) {
        LOG.warn(msg, args);
    }
    
    protected void logInfo(String msg, Object... args) {
        LOG.info(msg, args);
    }
    
    protected void logDebug(String msg, Object... args) {
        LOG.debug(msg, args);
    }
    
    protected void logInfo() {
        LOG.info("");
    }
}
