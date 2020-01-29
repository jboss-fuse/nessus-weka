package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.classifiers.Classifier;

public interface FunctionalClassifier<T extends Dataset> {

    T applyToFunctionalClassifier(UnaryOperator<FunctionalClassifier<T>> operator);

    T applyToClassifier(UnaryOperator<Classifier> operator);

    T consumeFunctionalClassifier(Consumer<FunctionalClassifier<T>> consumer);

    T consumeClassifier(Consumer<Classifier> consumer);

    T crossValidateModel(int numFolds, int seed);

    T evaluateModel(Dataset dataset);

    T evaluateModel();

    T evaluate();

    Classifier getClassifier();

}