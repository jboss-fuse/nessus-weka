package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.classifiers.Classifier;

public interface FunctionalClassifier extends FunctionalInstances {

    FunctionalClassifier applyToFunctionalClassifier(UnaryOperator<FunctionalClassifier> operator);

    FunctionalClassifier applyToClassifier(UnaryOperator<Classifier> operator);

    FunctionalClassifier consumeFunctionalClassifier(Consumer<FunctionalClassifier> consumer);

    FunctionalClassifier consumeClassifier(Consumer<Classifier> consumer);

    FunctionalEvaluation crossValidateModel(int numFolds, int seed);

    FunctionalEvaluation evaluateModel(Dataset dataset);

    FunctionalEvaluation evaluate();

    Classifier getClassifier();

}