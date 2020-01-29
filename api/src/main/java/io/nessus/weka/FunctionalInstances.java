package io.nessus.weka;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import weka.core.Instances;

public interface FunctionalInstances<T extends Dataset> {

    T read(URL url);
    
    T read(Path inpath);
    
    T read(String inpath);
    
    T write(Path outpath);

    T write(String outpath);

    T push();
    
    T pushTrainingSet();
    
    T pushTestSet();
    
    T push(String name);
    
    T pop();
    
    T popTrainingSet();
    
    T popTestSet();
    
    T pop(String name);
    
    T apply(String filterSpec);

    T classifier(String classifierSpec);
    
    T applyToFunctionalInstances(UnaryOperator<FunctionalInstances<T>> operator);

    T applyToInstances(UnaryOperator<Instances> operator);

    T consumeFunctionalInstances(Consumer<FunctionalInstances<T>> consumer);

    T consumeInstances(Consumer<Instances> consumer);
    
    Instances getInstances();
}