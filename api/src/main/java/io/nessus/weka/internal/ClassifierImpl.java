package io.nessus.weka.internal;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.nessus.weka.Dataset;
import io.nessus.weka.FunctionalClassifier;
import io.nessus.weka.FunctionalEvaluation;
import io.nessus.weka.UncheckedException;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class ClassifierImpl extends Dataset implements FunctionalClassifier {

    private Classifier classifier;
    
    public ClassifierImpl(Dataset stack, Classifier classifier) {
        super(stack.getInstances());
        this.classifier = classifier;
    }
    
    public ClassifierImpl(ClassifierImpl cl) {
        this(cl, cl.classifier);
    }
    
    @Override
    public FunctionalClassifier applyToFunctionalClassifier(UnaryOperator<FunctionalClassifier> operator) {
        classifier = operator.apply(this).getClassifier();
        return this;
    }

    @Override
    public FunctionalClassifier applyToClassifier(UnaryOperator<Classifier> operator) {
        classifier = operator.apply(classifier);
        return this;
    }

    @Override
    public FunctionalClassifier consumeFunctionalClassifier(Consumer<FunctionalClassifier> consumer) {
        consumer.accept(this);
        return this;
    }
    
    @Override
    public FunctionalClassifier consumeClassifier(Consumer<Classifier> consumer) {
        consumer.accept(classifier);
        return this;
    }
    
    @Override
    public FunctionalEvaluation evaluateModel(Dataset testset) {
        FunctionalEvaluation evaluation = evaluate();
        Instances testing = testset.getInstances();
        try {
            Evaluation ev = evaluation.getEvaluation();
            ev.evaluateModel(classifier, testing);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return evaluation;
    }
    
    @Override
    public FunctionalEvaluation crossValidateModel(int numFolds, int seed) {
        FunctionalEvaluation evaluation = evaluate();
        Instances data = getInstances();
        try {
            Evaluation ev = evaluation.getEvaluation();
            ev.crossValidateModel(classifier, data, numFolds, new Random(seed));
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return evaluation;
    }

    @Override
    public FunctionalEvaluation evaluate() {
        Evaluation evaluation;
        try {
            Instances training = getInstances();
            evaluation = new Evaluation(training);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return new EvaluationImpl(this, evaluation);
    }
    
    @Override
    public Classifier getClassifier() {
        return classifier;
    }
}