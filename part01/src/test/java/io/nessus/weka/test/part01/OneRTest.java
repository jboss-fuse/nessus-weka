package io.nessus.weka.test.part01;

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
        
            .classifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel(dataset)
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("82.7236", String.format("%.4f", eval.pctCorrect()));
    }
    
    @Test
    public void testOneRPercentageSplit() throws Exception {
        
        Dataset training = Dataset.create("data/sfny-80pct.arff");
        Dataset testing = Dataset.create("data/sfny-20pct.arff");
        
        Evaluation eval = training
        
            .classifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .evaluateModel(testing)
            
            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("77.3196", String.format("%.4f", eval.pctCorrect()));
    }
    
    @Test
    public void testOneRStratifiedSplit() throws Exception {
        
        Dataset training = Dataset.create("data/sfny-80pct-strat.arff");
        Dataset testing = Dataset.create("data/sfny-20pct-strat.arff");
        
        Evaluation eval = training
        
            .classifier("OneR")
            
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
        
            .classifier("OneR")
            
            .consumeClassifier(cl -> logInfo("{}", cl))
        
            .crossValidateModel(10, 1)

            .getEvaluation();
        
        logInfo("{}", eval.toSummaryString(false)); 
        
        Assert.assertEquals("77.0325", String.format("%.4f", eval.pctCorrect()));
    }
}
