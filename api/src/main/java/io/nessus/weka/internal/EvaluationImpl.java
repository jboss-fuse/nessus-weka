package io.nessus.weka.internal;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.nessus.weka.FunctionalEvaluation;
import weka.classifiers.Evaluation;

public class EvaluationImpl extends ClassifierImpl implements FunctionalEvaluation {

    private Evaluation evaluation;
    
    public EvaluationImpl(ClassifierImpl cl, Evaluation evaluation) {
        super(cl);
        this.evaluation = evaluation;
    }

    @Override
    public FunctionalEvaluation applyToFunctionalEvaluation(UnaryOperator<FunctionalEvaluation> operator) {
        evaluation = operator.apply(this).getEvaluation();
        return this;
    }

    @Override
    public FunctionalEvaluation applyToEvaluation(UnaryOperator<Evaluation> operator) {
        evaluation = operator.apply(evaluation);
        return this;
    }

    @Override
    public FunctionalEvaluation consumeFunctionalEvaluation(Consumer<FunctionalEvaluation> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public FunctionalEvaluation consumeEvaluation(Consumer<Evaluation> consumer) {
        consumer.accept(evaluation);
        return this;
    }

    @Override
    public Evaluation getEvaluation() {
        return evaluation;
    }
    
    
}