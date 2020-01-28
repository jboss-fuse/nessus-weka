package io.nessus.weka;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.core.Instances;

public interface FunctionalInstances {

    Dataset read(URL url);
    
    Dataset read(Path inpath);
    
    Dataset read(String inpath);
    
    Dataset write(Path outpath);

    Dataset write(String outpath);

    Dataset push();
    
    Dataset pushTrainingSet();
    
    Dataset pushTestSet();
    
    Dataset push(String name);
    
    Dataset pop();
    
    Dataset popTrainingSet();
    
    Dataset popTestSet();
    
    Dataset pop(String name);
    
    Dataset filter(String filterSpec);

    FunctionalClassifier classifier(String classifierSpec);
    
    Dataset applyToFunctionalInstances(UnaryOperator<FunctionalInstances> operator);

    Dataset applyToInstances(UnaryOperator<Instances> operator);

    Dataset consumeFunctionalInstances(Consumer<FunctionalInstances> consumer);

    Dataset consumeInstances(Consumer<Instances> consumer);
    
    Instances getInstances();
}