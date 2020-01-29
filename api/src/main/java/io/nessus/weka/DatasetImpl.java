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

public class DatasetImpl extends Dataset implements FunctionalEvaluation<Dataset>, FunctionalClassifier<Dataset>, FunctionalInstances<Dataset> {

    private static final String DEFAULT_SLOT = "_default";
    private static final String TRAINING_DATA_SLOT = "_training";
    private static final String TEST_DATA_SLOT = "_test";
    
    private final Map<String, Instances> storage = new HashMap<>();
    private Classifier classifier;
    private Evaluation evaluation;
    private Instances instances;

    public DatasetImpl(Instances instances) {
        AssertArg.notNull(instances, "Null instances");
        this.instances = instances;
        
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
    
    @Override
    public Dataset read(Path inpath) {
        Instances result = DatasetUtils.read(inpath);
        AssertState.notNull(result, "Null instances");
        instances = result;
        return this;
    }
    
    @Override
    public Dataset read(String inpath) {
        Instances result = DatasetUtils.read(Paths.get(inpath));
        AssertState.notNull(result, "Null instances");
        instances = result;
        return this;
    }
    
    @Override
    public Dataset read(URL url) {
        Instances result = DatasetUtils.read(url);
        AssertState.notNull(result, "Null instances");
        instances = result;
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
    public Dataset apply(String filterSpec) {
        return applyToInstances((i) -> DatasetUtils.applyFilter(i, filterSpec));
    }
    
    @Override
    public Dataset applyToInstances(UnaryOperator<Instances> operator) {
        Instances result = operator.apply(instances);
        AssertState.notNull(result, "Null instances");
        instances = result;
        return this;
    }
    
    @Override
    public Dataset consumeInstances(Consumer<Instances> consumer) {
        consumer.accept(instances);
        return this;
    }

    @Override
    public Dataset classifier(String classifierSpec) {
        Classifier result = DatasetUtils.buildClassifier(getInstances(), classifierSpec);
        AssertState.notNull(result, "Null classifier");
        classifier = result;
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
        Instances result = storage.remove(name);
        AssertState.notNull(result, "Null instances");
        instances = result;
        return this;
    }

    @Override
    public Instances getInstances() {
        return instances;
    }

    @Override
    public Dataset applyToFunctionalInstances(UnaryOperator<FunctionalInstances<Dataset>> operator) {
        Instances result = operator.apply(this).getInstances();
        AssertState.notNull(result, "Null instances");
        instances = result;
        return this;
    }

    @Override
    public Dataset consumeFunctionalInstances(Consumer<FunctionalInstances<Dataset>> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public Dataset applyToFunctionalClassifier(UnaryOperator<FunctionalClassifier<Dataset>> operator) {
        Classifier result = operator.apply(this).getClassifier();
        AssertState.notNull(result, "Null classifier");
        classifier = result;
        return this;
    }

    @Override
    public Dataset applyToClassifier(UnaryOperator<Classifier> operator) {
        Classifier result = operator.apply(assertClassifier());
        AssertState.notNull(result, "Null classifier");
        classifier = result;
        return this;
    }

    @Override
    public Dataset consumeFunctionalClassifier(Consumer<FunctionalClassifier<Dataset>> consumer) {
        consumer.accept(this);
        return this;
    }
    
    @Override
    public Dataset consumeClassifier(Consumer<Classifier> consumer) {
        consumer.accept(assertClassifier());
        return this;
    }
    
    @Override
    public Dataset evaluateModel(Dataset dataset) {
        try {
            Evaluation ev = evaluate().getEvaluation();
            Instances testing = dataset.getInstances();
            ev.evaluateModel(assertClassifier(), testing);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public Dataset evaluateModel() {
        try {
            Evaluation ev = evaluate().getEvaluation();
            ev.evaluateModel(assertClassifier(), instances);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public Dataset crossValidateModel(int numFolds, int seed) {
        Instances data = getInstances();
        try {
            Evaluation ev = evaluate().getEvaluation();
            ev.crossValidateModel(assertClassifier(), data, numFolds, new Random(seed));
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }

    @Override
    public Dataset evaluate() {
        try {
            evaluation = new Evaluation(instances);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public Dataset applyToFunctionalEvaluation(UnaryOperator<FunctionalEvaluation<Dataset>> operator) {
        Evaluation result = operator.apply(this).getEvaluation();
        AssertState.notNull(result, "Null evaluation");
        evaluation = result;
        return this;
    }

    @Override
    public Dataset applyToEvaluation(UnaryOperator<Evaluation> operator) {
        Evaluation result = operator.apply(assertEvaluation());
        AssertState.notNull(result, "Null evaluation");
        evaluation = result;
        return this;
    }

    @Override
    public Dataset consumeFunctionalEvaluation(Consumer<FunctionalEvaluation<Dataset>> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public Dataset consumeEvaluation(Consumer<Evaluation> consumer) {
        consumer.accept(assertEvaluation());
        return this;
    }

    @Override
    public Classifier getClassifier() {
        return classifier;
    }
    
    @Override
    public Evaluation getEvaluation() {
        return evaluation;
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