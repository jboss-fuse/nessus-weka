package io.nessus.weka.internal;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.nessus.weka.AssertArg;
import io.nessus.weka.AssertState;
import io.nessus.weka.FunctionalInstances;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Attribute;
import weka.core.Instances;

@SuppressWarnings("unchecked")
public class InstancesImpl<T> implements FunctionalInstances<T> {

    private static final String DEFAULT_SLOT = "_default";
    private static final String TRAINING_DATA_SLOT = "_training";
    private static final String TEST_DATA_SLOT = "_test";
    
    private Map<String, Instances> storage = new HashMap<>();
    private Instances instances;
    
    public InstancesImpl(Instances instances) {
        AssertArg.notNull(instances, "Null instances");
        this.instances = instances;
        
        // Guess the class attribute if not set already
        if (instances.classIndex() < 0) {
            Attribute attr = instances.attribute("class");
            if (attr != null) {
                instances.setClass(attr);
            } else {
                int numatts = instances.numAttributes();
                attr = instances.attribute(numatts - 1);
                if (attr.isNominal()) {
                    instances.setClass(attr);
                }
            }
        }
    }

    @Override
    public T read(Path inpath) {
        instances = DatasetUtils.read(inpath);
        return (T) this;
    }
    
    @Override
    public T read(String inpath) {
        instances = DatasetUtils.read(Paths.get(inpath));
        return (T) this;
    }
    
    @Override
    public T read(URL url) {
        instances = DatasetUtils.read(url);
        return (T) this;
    }
    
    @Override
    public T rename(String name) {
        instances.setRelationName(name);
        return (T) this;
    }
    
    @Override
    public T write(Path outpath) {
        DatasetUtils.write(instances, outpath);
        return (T) this;
    }

    @Override
    public T write(String outpath) {
        DatasetUtils.write(instances, Paths.get(outpath));
        return (T) this;
    }

    @Override
    public T apply(String filterSpec) {
        return applyToInstances((i) -> DatasetUtils.applyFilter(i, filterSpec));
    }
    
    @Override
    public T applyToFunctionalInstances(UnaryOperator<FunctionalInstances<T>> operator) {
        instances = operator.apply(this).getInstances();
        return (T) this;
    }
    
    @Override
    public T applyToInstances(UnaryOperator<Instances> operator) {
        instances = operator.apply(instances);
        return (T) this;
    }
    
    @Override
    public T consumeFunctionalInstances(Consumer<FunctionalInstances<T>> consumer) {
        consumer.accept(this);
        return (T) this;
    }

    @Override
    public T consumeInstances(Consumer<Instances> consumer) {
        consumer.accept(instances);
        return (T) this;
    }

    @Override
    public T push() {
        push(DEFAULT_SLOT);
        return (T) this;
    }

    @Override
    public T pushTrainingSet() {
        push(TRAINING_DATA_SLOT);
        return (T) this;
    }

    @Override
    public T pushTestSet() {
        push(TEST_DATA_SLOT);
        return (T) this;
    }

    @Override
    public T push(String name) {
        storage.put(name, new Instances(instances));
        return (T) this;
    }

    @Override
    public T pop() {
        pop(DEFAULT_SLOT);
        return (T) this;
    }

    @Override
    public T popTrainingSet() {
        pop(TRAINING_DATA_SLOT);
        return (T) this;
    }

    @Override
    public T popTestSet() {
        pop(TEST_DATA_SLOT);
        return (T) this;
    }

    @Override
    public T pop(String name) {
        Instances aux = storage.remove(name);
        AssertState.notNull(aux, "No dataset in slot: " + name);
        instances = aux;
        return (T) this;
    }

    @Override
    public Instances getInstances() {
        return instances;
    }

    @Override
    public T get() {
        return (T) this;
    }
}