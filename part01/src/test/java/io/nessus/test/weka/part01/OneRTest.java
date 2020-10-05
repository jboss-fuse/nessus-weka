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
package io.nessus.test.weka.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Evaluation;

public class OneRTest extends AbstractWekaTest {
    
    @Test
    public void testOneR() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.arff");
        
        Evaluation eval = dataset
        
            .buildClassifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel()
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("82.7236", String.format("%.4f", eval.pctCorrect()));
    }
    
    @Test
    public void testOneRPercentageSplit() throws Exception {
        
        Dataset training = Dataset.create("data/sfny-train80.arff");
        Dataset testing = Dataset.create("data/sfny-test20.arff");
        
        Evaluation eval = training
        
            .buildClassifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel(testing)
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("77.3196", String.format("%.4f", eval.pctCorrect()));
    }
    
    @Test
    public void testOneRStratifiedSplit() throws Exception {
        
        Dataset training = Dataset.create("data/sfny-train.arff");
        Dataset testing = Dataset.create("data/sfny-test.arff");
        
        Evaluation eval = training
        
            .buildClassifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel(testing)
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("81.8182", String.format("%.4f", eval.pctCorrect()));
    }
    
    @Test
    public void testOneRCrossValidation() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.arff");
        
        Evaluation eval = dataset
        
            .buildClassifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .crossValidateModel(10, 1)

            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("77.0325", String.format("%.4f", eval.pctCorrect()));
    }
}
