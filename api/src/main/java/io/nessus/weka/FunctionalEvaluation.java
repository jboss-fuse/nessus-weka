package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.classifiers.Evaluation;

public interface FunctionalEvaluation<T extends Dataset> {

    T applyToFunctionalEvaluation(UnaryOperator<FunctionalEvaluation<T>> operator);

    T applyToEvaluation(UnaryOperator<Evaluation> operator);

    T consumeFunctionalEvaluation(Consumer<FunctionalEvaluation<T>> consumer);

    T consumeEvaluation(Consumer<Evaluation> consumer);

    Evaluation getEvaluation();
}