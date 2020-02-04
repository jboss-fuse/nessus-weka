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

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import io.nessus.weka.internal.DatasetImpl;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;

public abstract class Dataset implements FunctionalEvaluation<Dataset>, FunctionalClassifier<Dataset>, FunctionalInstances<Dataset> {

    public static Dataset create(String inpath) {
        return create(DatasetUtils.read(inpath));
    }

    public static Dataset create(Path inpath) {
        return create(DatasetUtils.read(inpath));
    }

    public static Dataset create(URL inurl) {
        return create(DatasetUtils.read(inurl));
    }

    public static Dataset create(InputStream input) {
        return create(DatasetUtils.read(input));
    }

    public static Dataset create(Instances instances) {
        return new DatasetImpl(instances);
    }
}
