package io.nessus.weka;

import io.nessus.weka.internal.ClassifierImpl;
import io.nessus.weka.internal.InstancesImpl;
import io.nessus.weka.utils.DatasetUtils;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class Dataset extends InstancesImpl<Dataset> {

    public Dataset(Instances instances) {
        super(instances);
    }
    
    public static Dataset create(String inpath) {
        Instances instances = DatasetUtils.read(inpath);
        return new Dataset(instances);
    }
    
    public FunctionalClassifier classifier(String classifierSpec) {
        Classifier classifier = DatasetUtils.buildClassifier(getInstances(), classifierSpec);
        return new ClassifierImpl(this, classifier);
    }
}