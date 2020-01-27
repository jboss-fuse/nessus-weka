package io.nessus.weka.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.nessus.weka.AssertState;
import io.nessus.weka.CheckedException;
import io.nessus.weka.Operator;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

public class DatasetUtils {

    // Hide ctor
    private DatasetUtils() {
    }
    
    public static Instances read(Path inpath) {
        return read(inpath.toString());
    }
    
    public static Instances read(String inpath) {
        try {
            DataSource source = new DataSource(inpath); 
            return source.getDataSet();
        } catch (Exception ex) {
            throw CheckedException.create(ex);
        }
    }
    
    public static void write(Instances dataset, Path outpath) {
        write(dataset, outpath.toString());
    }
    
    public static void write(Instances dataset, String outpath) {
        try {
            DataSink.write(outpath, dataset);
        } catch (Exception ex) {
            throw CheckedException.create(ex);
        }
    }
    
    public static Instances applyFilter(Instances dataset, String spec) {
        return applyFilter(dataset, new Operator(spec));
    }

    public static Instances applyFilter(Instances dataset, String name, String options) {
        return applyFilter(dataset, new Operator(name, options));
    }

    public static Instances applyFilter(Instances dataset, Operator spec) {
        return applyFilter(dataset, spec.getName(), spec.getOptions());
    }

    public static Instances applyFilter(Instances dataset, String name, String[] options) {
        try {
            Filter filter = loadInstance(name, Filter.class);
            filter.setInputFormat(dataset);
            filter.setOptions(options);
            dataset = Filter.useFilter(dataset, filter);
            return dataset;
        } catch (Exception ex) {
            throw CheckedException.create(ex);
        }
    }

    public static Classifier buildClassifier(Instances dataset, String classifierName, String options) {
        return buildClassifier(dataset, classifierName, optionsNotNull(options).split(" "));
    }

    public static Classifier buildClassifier(Instances dataset, String classifierName, String[] options) {
        try {
            Classifier classifier = loadInstance(classifierName, Classifier.class);
            classifier.buildClassifier(dataset);
            return classifier;
        } catch (Exception ex) {
            throw CheckedException.create(ex);
        }
    }

    public static Evaluation crossValidateModel(Classifier classifier, Instances dataset, int numFolds, int seed) {
        try {
            Evaluation evaluation = new Evaluation(dataset);
            evaluation.crossValidateModel(classifier, dataset, numFolds, new Random(seed));
            return evaluation;
        } catch (Exception ex) {
            throw CheckedException.create(ex);
        }
    }
    
    private static String optionsNotNull(String options) {
        return options != null ? options : "";
    }

    @SuppressWarnings("unchecked")
    private static <T extends Object> T loadInstance(String name, Class<T> type) {
        
        List<String> packages = Arrays.asList(
                "weka.filters.supervised.attribute",
                "weka.filters.supervised.instance",
                "weka.filters.unsupervised.attribute",
                "weka.filters.unsupervised.instance",
                "weka.filters",
                "weka.classifiers.bayes",
                "weka.classifiers.functions",
                "weka.classifiers.lazy",
                "weka.classifiers.trees",
                "weka.classifiers");
        
        List<T> instances = new ArrayList<>();
        
        for (String packageName : packages) {
            ClassLoader loader = type.getClassLoader();
            try {
                Class<?> clazz = loader.loadClass(packageName + "." + name);
                T filter = (T) clazz.newInstance();
                instances.add(filter);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                // ignore
            }
        }
        
        List<String> fqnames = instances.stream()
                .map(f -> f.getClass().getName())
                .collect(Collectors.toList());
        
        AssertState.isFalse(fqnames.isEmpty(), "Cannot obtain " + type.getSimpleName() + " for name: " + name);
        AssertState.isEqual(1, fqnames.size(), "Ambiguous " + type.getSimpleName() + " name: " + fqnames);
        
        return instances.get(0);
    }
}