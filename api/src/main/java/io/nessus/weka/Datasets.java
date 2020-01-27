package io.nessus.weka;

import java.util.HashMap;
import java.util.function.Supplier;

public class Datasets extends HashMap<String, Dataset> implements Supplier<Dataset> {

    private static final long serialVersionUID = 3562105567846990433L;
    
    public static final String DEFAULT = "_default";
    
    public Datasets() {
    }
    
    public Datasets (Dataset dataset) {
        put(DEFAULT, dataset);
    }

    @Override
    public Dataset get() {
        return get(DEFAULT);
    }
}