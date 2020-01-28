package io.nessus.weka;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.classifiers.Evaluation;

public interface FunctionalEvaluation extends FunctionalClassifier {

    FunctionalEvaluation applyToFunctionalEvaluation(UnaryOperator<FunctionalEvaluation> operator);

    FunctionalEvaluation applyToEvaluation(UnaryOperator<Evaluation> operator);

    FunctionalEvaluation consumeFunctionalEvaluation(Consumer<FunctionalEvaluation> consumer);

    FunctionalEvaluation consumeEvaluation(Consumer<Evaluation> consumer);

    Evaluation getEvaluation();
}