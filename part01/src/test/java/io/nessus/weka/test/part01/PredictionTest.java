package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.AssertArg;
import io.nessus.weka.Dataset;
import io.nessus.weka.ModelLoader;
import io.nessus.weka.NominalPredictor;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.core.Instances;

public class PredictionTest extends AbstractWekaTest {
    
    @Test
    public void testJ48() throws Exception {
        
        ModelLoader modelLoader = new ModelLoader("data/sfny-j48.model");
        
        Dataset dataset = Dataset.create("data/sfny-test.arff")
                
                .pushTestSet()
        
                .loadClassifier(modelLoader)
                
                .consumeClassifier(cl -> logInfo("{}", cl))
                
                .evaluateModel()
                
                .consumeEvaluation(ev -> logInfo("{}", ev.toSummaryString()))
                
                .apply("Remove -R last")
                
                .apply("Add -N predicted -T NOM -L 0,1")
                
                .apply("RenameRelation -modify sfny-predicted")
                
                .predictNominal(new NominalPredictor())
                
                .write("data/sfny-predicted.arff");
        
        Instances wasdata = dataset.getInstances();
        Instances expdata = dataset.popTestSet().getInstances();
        int numInstances = expdata.numInstances();
        
        int correct = numCorrectlyClassified(expdata, wasdata);
        
        double accuracy = 100.0 * correct / numInstances;
        int incorrect = numInstances - correct;
        
        logInfo(String.format("Correctly Classified Instances   %d %.4f %%", correct, accuracy));
        logInfo(String.format("Incorrectly Classified Instances %d %.4f %%", incorrect, 100 - accuracy));
        
        Assert.assertEquals("88.8889", String.format("%.4f", accuracy));
    }

    private int numCorrectlyClassified(Instances expdata, Instances wasdata) {
        AssertArg.isEqual(expdata.classIndex(), wasdata.classIndex());
        AssertArg.isEqual(expdata.size(), wasdata.size());
        int numInstances = expdata.numInstances();
        int clidx = expdata.classIndex();
        int correct = 0;
        for (int i = 0; i < numInstances; i++) {
            double expval = expdata.instance(i).value(clidx);
            double wasval = wasdata.instance(i).value(clidx);
            if (expval == wasval) correct += 1;
        }
        return correct;
    }
}
