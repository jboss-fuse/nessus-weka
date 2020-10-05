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

import java.util.function.Function;

import io.nessus.common.AssertState;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class NominalPredictor implements Function<Dataset, Instances> {

    @Override
    public Instances apply(Dataset ds) {
        try {
            Classifier cl = ds.getClassifier();
            Instances result = ds.getInstances();
            Attribute clattr = result.attribute("predicted");
            AssertState.notNull(clattr, "Cannot find attribute 'predicted' in: " + ds.getAttributes());
            result.setClass(clattr);
            for (Instance item : result) {
                double label = cl.classifyInstance(item);
                item.setValue(clattr.index(), label);
            }
            return result;
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }
}
