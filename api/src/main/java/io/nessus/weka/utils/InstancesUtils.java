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
package io.nessus.weka.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instances;

public class InstancesUtils {

    // Hide ctor
    private InstancesUtils() {
    }
    
    public static List<Attribute> attributes(Instances instances) {
    	List<Attribute> atts = new ArrayList<>();
    	Enumeration<Attribute> en = instances.enumerateAttributes();
    	while (en.hasMoreElements()) atts.add(en.nextElement());
        return atts;
    }
    
}
