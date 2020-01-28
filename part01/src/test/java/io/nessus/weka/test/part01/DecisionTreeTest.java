package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Evaluation;

public class DecisionTreeTest extends AbstractWekaTest {
    
    @Test
    public void testJ48() throws Exception {
        
        Evaluation eval = Dataset.create("data/sfny.arff")
        
                .classifier("J48")
                
                .consumeClassifier(cl -> logInfo("{}", cl))
                
                .crossValidateModel(10, 1)
                
                .getEvaluation();
                
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("85.3659", String.format("%.4f", eval.pctCorrect()));

    }
}
