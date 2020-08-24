/*-
 * #%L
 * Nessus :: Weka :: API
 * %%
 * Copyright (C) 2020 Nessus
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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