/*-
 * #%L
 * Nessus :: Weka :: Part 01
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
package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Evaluation;

public class ZeroRTest extends AbstractWekaTest {
    
    @Test
    public void testZeroR() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.arff");
        
        Evaluation eval = dataset
        
            .buildClassifier("ZeroR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel(dataset)
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("54.4715", String.format("%.4f", eval.pctCorrect()));
    }
}
