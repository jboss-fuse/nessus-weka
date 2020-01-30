package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import weka.classifiers.Evaluation;

public interface FunctionalEvaluation<T extends Dataset> {

    T applyToEvaluation(UnaryOperator<Evaluation> operator);

    T applyToEvaluation(Function<T, Evaluation> function);

    T consumeEvaluation(Consumer<Evaluation> consumer);

    T crossValidateModel(int numFolds, int seed);

    T evaluateModel(T dataset);

    T evaluateModel();

    T evaluate();

    Evaluation getEvaluation();
}