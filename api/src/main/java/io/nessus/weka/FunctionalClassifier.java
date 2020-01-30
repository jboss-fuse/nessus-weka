package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import weka.classifiers.Classifier;

public interface FunctionalClassifier<T extends Dataset> {

    T applyToClassifier(UnaryOperator<Classifier> operator);

    T applyToClassifier(Function<T, Classifier> function);

    T consumeClassifier(Consumer<Classifier> consumer);

    T loadClassifier(Supplier<Classifier> supplier);
    
    T buildClassifier(String classifierSpec);
    
    Classifier getClassifier();

}