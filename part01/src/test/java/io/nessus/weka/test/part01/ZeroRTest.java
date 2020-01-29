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
