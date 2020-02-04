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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.function.Supplier;

import weka.classifiers.Classifier;

public class ModelDecoder implements Supplier<Classifier> {

    private Classifier classifier;
    
    public ModelDecoder() {
    }
    
    public ModelDecoder(String encoded) {
        decode(encoded);
    }

    public ModelDecoder decode(String encoded) {
        try {
            byte[] bytes = Base64.getDecoder().decode(encoded);
            InputStream input = new ByteArrayInputStream(bytes);
            ObjectInputStream ins = new ObjectInputStream(input);
            classifier = (Classifier) ins.readObject();
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public Classifier get() {
        return classifier;
    }
}
