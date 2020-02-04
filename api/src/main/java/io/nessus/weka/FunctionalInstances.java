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

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import weka.core.Attribute;
import weka.core.Instances;

public interface FunctionalInstances<T extends Dataset> {

    T read(URL url);
    
    T read(Path inpath);
    
    T read(String inpath);
    
    T write(Path outpath);

    T write(String outpath);

    T push();
    
    T pushTrainingSet();
    
    T pushTestSet();
    
    T push(String name);
    
    T pop();
    
    T popTrainingSet();
    
    T popTestSet();
    
    T pop(String name);
    
    T apply(String filterSpec);

    T applyToInstances(UnaryOperator<Instances> operator);

    T applyToInstances(Function<T, Instances> function);

    T consumeInstances(Consumer<Instances> consumer);
    
    T consumeDataset(Consumer<T> consumer);
    
    List<Attribute> getAttributes();

    Instances getInstances();
}
