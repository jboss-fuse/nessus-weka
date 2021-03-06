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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import weka.classifiers.Classifier;

public interface FunctionalClassifier<T extends Dataset> {

    T applyToClassifier(UnaryOperator<Classifier> operator);

    T applyToClassifier(Function<T, Classifier> function);

    T consumeClassifier(Consumer<Classifier> consumer);

    T loadClassifier(Supplier<Classifier> supplier);
    
    T buildClassifier(String classifierSpec);
    
    Classifier getClassifier();

}
