package io.nessus.weka;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.nessus.weka.utils.DatasetUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;

public class Dataset implements FunctionalEvaluation, FunctionalClassifier, FunctionalInstances {

    private static final String DEFAULT_SLOT = "_default";
    private static final String TRAINING_DATA_SLOT = "_training";
    private static final String TEST_DATA_SLOT = "_test";
    
    private final Map<String, Instances> storage = new HashMap<>();
    private Classifier classifier;
    private Evaluation evaluation;
    private Instances instances;

    public Dataset(Instances instances) {
        
        setInstances(instances);
        
        // Guess the class attribute if not set already
        if (instances.classIndex() < 0) {
            Attribute attr = instances.attribute("class");
            if (attr != null) {
                instances.setClass(attr);
            } else {
                int numatts = instances.numAttributes();
                attr = instances.attribute(numatts - 1);
                if (attr.isNominal()) {
                    instances.setClass(attr);
                }
            }
        }
    }
    
    public static Dataset create(String inpath) {
        Instances instances = DatasetUtils.read(inpath);
        return new Dataset(instances);
    }

    @Override
    public Dataset read(Path inpath) {
        setInstances(DatasetUtils.read(inpath));
        return this;
    }
    
    @Override
    public Dataset read(String inpath) {
        setInstances(DatasetUtils.read(Paths.get(inpath)));
        return this;
    }
    
    @Override
    public Dataset read(URL url) {
        setInstances(DatasetUtils.read(url));
        return this;
    }
    
    @Override
    public Dataset write(Path outpath) {
        DatasetUtils.write(instances, outpath);
        return this;
    }

    @Override
    public Dataset write(String outpath) {
        DatasetUtils.write(instances, Paths.get(outpath));
        return this;
    }

    @Override
    public Dataset filter(String filterSpec) {
        return applyToInstances((i) -> DatasetUtils.applyFilter(i, filterSpec));
    }
    
    @Override
    public Dataset applyToInstances(UnaryOperator<Instances> operator) {
        setInstances(operator.apply(instances));
        return this;
    }
    
    @Override
    public Dataset consumeInstances(Consumer<Instances> consumer) {
        consumer.accept(instances);
        return this;
    }

    @Override
    public FunctionalClassifier classifier(String classifierSpec) {
        setClassifier(DatasetUtils.buildClassifier(getInstances(), classifierSpec));
        return this;
    }
    
    @Override
    public Dataset push() {
        push(DEFAULT_SLOT);
        return this;
    }

    @Override
    public Dataset pushTrainingSet() {
        push(TRAINING_DATA_SLOT);
        return this;
    }

    @Override
    public Dataset pushTestSet() {
        push(TEST_DATA_SLOT);
        return this;
    }

    @Override
    public Dataset push(String name) {
        storage.put(name, new Instances(instances));
        return this;
    }

    @Override
    public Dataset pop() {
        pop(DEFAULT_SLOT);
        return this;
    }

    @Override
    public Dataset popTrainingSet() {
        pop(TRAINING_DATA_SLOT);
        return this;
    }

    @Override
    public Dataset popTestSet() {
        pop(TEST_DATA_SLOT);
        return this;
    }

    @Override
    public Dataset pop(String name) {
        setInstances(storage.remove(name));
        return this;
    }

    @Override
    public Instances getInstances() {
        return instances;
    }

    @Override
    public Dataset applyToFunctionalInstances(UnaryOperator<FunctionalInstances> operator) {
        setInstances(operator.apply(this).getInstances());
        return this;
    }

    @Override
    public Dataset consumeFunctionalInstances(Consumer<FunctionalInstances> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public FunctionalClassifier applyToFunctionalClassifier(UnaryOperator<FunctionalClassifier> operator) {
        setClassifier(operator.apply(this).getClassifier());
        return this;
    }

    @Override
    public FunctionalClassifier applyToClassifier(UnaryOperator<Classifier> operator) {
        setClassifier(operator.apply(assertClassifier()));
        return this;
    }

    @Override
    public FunctionalClassifier consumeFunctionalClassifier(Consumer<FunctionalClassifier> consumer) {
        consumer.accept(this);
        return this;
    }
    
    @Override
    public FunctionalClassifier consumeClassifier(Consumer<Classifier> consumer) {
        consumer.accept(assertClassifier());
        return this;
    }
    
    @Override
    public FunctionalEvaluation evaluateModel(Dataset testset) {
        FunctionalEvaluation feval = evaluate();
        Instances testing = testset.getInstances();
        try {
            Evaluation ev = feval.getEvaluation();
            ev.evaluateModel(assertClassifier(), testing);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public FunctionalEvaluation crossValidateModel(int numFolds, int seed) {
        FunctionalEvaluation feval = evaluate();
        Instances data = getInstances();
        try {
            Evaluation ev = feval.getEvaluation();
            ev.crossValidateModel(assertClassifier(), data, numFolds, new Random(seed));
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }

    @Override
    public FunctionalEvaluation evaluate() {
        try {
            Instances training = getInstances();
            setEvaluation(new Evaluation(training));
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public FunctionalEvaluation applyToFunctionalEvaluation(UnaryOperator<FunctionalEvaluation> operator) {
        setEvaluation(operator.apply(this).getEvaluation());
        return this;
    }

    @Override
    public FunctionalEvaluation applyToEvaluation(UnaryOperator<Evaluation> operator) {
        setEvaluation(operator.apply(assertEvaluation()));
        return this;
    }

    @Override
    public FunctionalEvaluation consumeFunctionalEvaluation(Consumer<FunctionalEvaluation> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public FunctionalEvaluation consumeEvaluation(Consumer<Evaluation> consumer) {
        consumer.accept(assertEvaluation());
        return this;
    }

    @Override
    public Classifier getClassifier() {
        return classifier;
    }
    
    private void setClassifier(Classifier classifier) {
        AssertArg.notNull(classifier, "Null classifier");
        this.classifier = classifier;
    }

    @Override
    public Evaluation getEvaluation() {
        return evaluation;
    }

    private void setEvaluation(Evaluation evaluation) {
        AssertArg.notNull(evaluation, "Null evaluation");
        this.evaluation = evaluation;
    }

    private void setInstances(Instances instances) {
        AssertArg.notNull(instances, "Null instances");
        this.instances = instances;
    }
    
    private Classifier assertClassifier() {
        AssertState.notNull(classifier, "Classifier not available");
        return classifier;
    }
    
    private Evaluation assertEvaluation() {
        AssertState.notNull(evaluation, "Evaluation not available");
        return evaluation;
    }
}