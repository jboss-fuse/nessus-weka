package io.nessus.weka;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;

public class Dataset implements Supplier<Instances>, Serializable {

    private static final long serialVersionUID = -6156633269278788880L;
    
    private Instances dataset;
    
    public Dataset(Instances dataset) {
        AssertArg.notNull(dataset, "Null dataset");
        this.dataset = dataset;
    }

    public static Dataset read(String inpath) {
        return Dataset.read(Paths.get(inpath));
    }
    
    public static Dataset read(Path inpath) {
        Instances dataset = DatasetUtils.read(inpath);
        return new Dataset(dataset);
    }
    
    public Dataset write(String outpath) {
        write(Paths.get(outpath));
        return this;
    }

    public Dataset write(Path outpath) {
        DatasetUtils.write(dataset, outpath);
        return this;
    }

    @Override
    public Instances get() {
        return dataset;
    }
    
    public Dataset apply(String filterSpec) {
        return apply((i) -> DatasetUtils.applyFilter(i, filterSpec));
    }
    
    public Dataset apply(UnaryOperator<Instances> op) {
        dataset = op.apply(dataset);
        return this;
    }
    
    public Dataset accept(Consumer<Instances> consumer) {
        consumer.accept(dataset);
        return this;
    }
}