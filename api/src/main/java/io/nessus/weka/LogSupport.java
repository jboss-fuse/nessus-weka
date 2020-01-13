package io.nessus.weka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSupport {
	
    protected final Logger LOG = LoggerFactory.getLogger(getClass().getPackage().getName());
    
    protected void logInfo(String msg, Object... args) {
        LOG.info(msg, args);
    }
    
    protected void logInfo() {
        LOG.info("");
    }
}
