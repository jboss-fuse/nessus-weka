package io.nessus.weka;

import java.util.function.Function;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class NominalPredictor implements Function<Dataset, Instances> {

    @Override
    public Instances apply(Dataset ds) {
        try {
            Classifier cl = ds.getClassifier();
            Instances result = ds.getInstances();
            Attribute clattr = result.attribute("predicted");
            AssertState.notNull(clattr, "Cannot find attribute 'predicted' in: " + ds.getAttributes());
            result.setClass(clattr);
            for (Instance item : result) {
                double label = cl.classifyInstance(item);
                item.setValue(clattr.index(), label);
            }
            return result;
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }
}