package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.ModelDecoder;
import io.nessus.weka.ModelEncoder;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Evaluation;

public class ModelPersistenceTest extends AbstractWekaTest {
    
    @Test
    public void testJ48() throws Exception {
        
        ModelEncoder modelEncoder = new ModelEncoder();
        
        Dataset dataset = Dataset.create("data/sfny-train.arff")
        
                .buildClassifier("J48")
                
                .consumeClassifier(cl -> logInfo("{}", cl))
                
                .crossValidateModel(10, 1)
                
                .consumeClassifier(modelEncoder)
                
                .read("data/sfny-test.arff")
                
                .evaluateModel();
        
        
        Evaluation ev = dataset.getEvaluation();
        logInfo("{}", ev.toSummaryString(false)); 
        Assert.assertEquals("88.8889", String.format("%.4f", ev.pctCorrect()));

        String encoded = modelEncoder.getEncodedModel();
        
        dataset = Dataset.create("data/sfny-test.arff")
                
                .loadClassifier(new ModelDecoder(encoded))
                
                .consumeClassifier(cl -> logInfo("{}", cl))
                
                .evaluateModel();
        
        ev = dataset.getEvaluation();
        logInfo("{}", ev.toSummaryString(false)); 
        Assert.assertEquals("88.8889", String.format("%.4f", ev.pctCorrect()));
    }
}
