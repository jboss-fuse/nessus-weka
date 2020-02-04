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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.function.Consumer;

import weka.classifiers.Classifier;

public class ModelEncoder implements Consumer<Classifier> {

    private String encoded;
    
    public ModelEncoder() {
    }
    
    public ModelEncoder(Classifier cl) {
        accept(cl);
    }

    @Override
    public void accept(Classifier cl) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(cl);
            byte[] bytes = baos.toByteArray();
            encoded = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            throw UncheckedException.create(ex);
        }
   }
    
   public String getEncodedModel() {
        return encoded;
   }
}
