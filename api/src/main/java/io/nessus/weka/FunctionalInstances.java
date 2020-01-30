package io.nessus.weka;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import weka.core.Attribute;
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

    T applyToInstances(UnaryOperator<Instances> operator);

    T applyToInstances(Function<T, Instances> function);

    T consumeInstances(Consumer<Instances> consumer);
    
    List<Attribute> getAttributes();

    Instances getInstances();
}