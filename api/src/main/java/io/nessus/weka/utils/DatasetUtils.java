package io.nessus.weka.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.nessus.weka.AssertState;
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
    
    public static Instances readDataset(Path inpath) throws Exception {
        return readDataset(inpath.toString());
    }
    
    public static Instances readDataset(String inpath) throws Exception {
        DataSource source = new DataSource(inpath); 
        return source.getDataSet();
    }
    
    public static void writeDataset(Instances dataset, Path outpath) throws Exception {
        DataSink.write(outpath.toString(), dataset);
    }
    
    public static void writeDataset(Instances dataset, String outpath) throws Exception {
        DataSink.write(outpath, dataset);
    }
    
    public static Instances applyFilter(Instances dataset, String filterName, String options) throws Exception {
        return applyFilter(dataset, filterName, optionsNotNull(options).split(" "));
    }

    public static Instances applyFilter(Instances dataset, String filterName, String[] options) throws Exception {
        Filter filter = loadInstance(filterName, Filter.class);
        filter.setInputFormat(dataset);
        filter.setOptions(options);
        dataset = Filter.useFilter(dataset, filter);
        return dataset;
    }

    public static Classifier buildClassifier(Instances dataset, String classifierName, String options) throws Exception {
        return buildClassifier(dataset, classifierName, optionsNotNull(options).split(" "));
    }

    public static Classifier buildClassifier(Instances dataset, String classifierName, String[] options) throws Exception {
        Classifier classifier = loadInstance(classifierName, Classifier.class);
        classifier.buildClassifier(dataset);
        return classifier;
    }

    public static Evaluation crossValidateModel(Classifier classifier, Instances dataset, int numFolds, int seed) throws Exception {
        Evaluation evaluation = new Evaluation(dataset);
        evaluation.crossValidateModel(classifier, dataset, numFolds, new Random(seed));
        return evaluation;
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
            ClassLoader loader = Filter.class.getClassLoader();
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