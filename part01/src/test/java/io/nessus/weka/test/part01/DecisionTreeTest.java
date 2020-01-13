package io.nessus.weka.test.part01;

import org.junit.Test;

import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * https://weka.sourceforge.io/doc.stable-3-8/weka/classifiers/trees/J48.html
 */
public class DecisionTreeTest extends AbstractWekaTest {
    
    @Test
    public void testJ48() throws Exception {
        
        DataSource source = new DataSource("data/labor.arff");   
        Instances dataset = source.getDataSet();
        
        // Setting class attribute 
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // Split the dataset in train/test data
        Instances train = dataset.trainCV(5, 0);
        Instances test = dataset.testCV(5, 0);
        
        logInfo("All Instances: {}", dataset.size());
        logInfo("Train with: {}", train.size());
        logInfo("Test with: {}", test.size());
        
        J48 tree = new J48();
        tree.setUnpruned(false);
        tree.buildClassifier(train);

        // Print tree
        logInfo("{}", tree);

        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(tree, test);
        logInfo(eval.toSummaryString(false));
    }
}
