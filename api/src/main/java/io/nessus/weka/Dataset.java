package io.nessus.weka;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;

import io.nessus.weka.internal.DatasetImpl;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;

public abstract class Dataset implements FunctionalEvaluation<Dataset>, FunctionalClassifier<Dataset>, FunctionalInstances<Dataset> {

    public static Dataset create(String inpath) {
        return create(DatasetUtils.read(inpath));
    }

    public static Dataset create(Path inpath) {
        return create(DatasetUtils.read(inpath));
    }

    public static Dataset create(URL inurl) {
        return create(DatasetUtils.read(inurl));
    }

    public static Dataset create(InputStream input) {
        return create(DatasetUtils.read(input));
    }

    public static Dataset create(Instances instances) {
        return new DatasetImpl(instances);
    }

    public abstract Dataset consumeDataset(Consumer<Dataset> consumer);
}