package io.nessus.weka.test.part01;

import org.junit.Test;

import io.nessus.weka.testing.AbstractWekaTest;
import io.nessus.weka.utils.DatasetUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DecisionTreeTest extends AbstractWekaTest {
    
    @Test
    public void testJ48() throws Exception {
        
        DataSource source = new DataSource("data/sfny.arff");   
        Instances dataset = source.getDataSet();
        
        // Setting class attribute 
        dataset.setClass(dataset.attribute("in_sf"));

        // Build the J48 classifier
        Classifier classifier = DatasetUtils.buildClassifier(dataset, "J48", "");
        logInfo("{}", classifier);

        // Evaluate with 10 fold cross validation
        Evaluation eval = DatasetUtils.crossValidateModel(classifier, dataset, 10, 1);
        logInfo(eval.toSummaryString(false));
    }
}
