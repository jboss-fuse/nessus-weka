package io.nessus.weka.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.nessus.weka.AssertState;
import weka.core.Instances;
import weka.filters.Filter;

public class DatasetUtils {

    // Hide ctor
    private DatasetUtils() {
    }
    
    public static Instances applyFilter(Instances dataset, String filterName, String options) throws Exception {
        return applyFilter(dataset, filterName, options.split(" "));
    }

    public static Instances applyFilter(Instances dataset, String filterName, String[] options) throws Exception {
        Filter filter = loadFilter(filterName);
        filter.setInputFormat(dataset);
        filter.setOptions(options);
        dataset = Filter.useFilter(dataset, filter);
        return dataset;
    }

    private static Filter loadFilter(String filterName) {
        
        List<String> filterPackages = Arrays.asList(
                "weka.filters.supervised.attribute",
                "weka.filters.supervised.instance",
                "weka.filters.unsupervised.attribute",
                "weka.filters.unsupervised.instance",
                "weka.filters");
        
        List<Filter> filters = new ArrayList<>();
        
        for (String packageName : filterPackages) {
            ClassLoader loader = Filter.class.getClassLoader();
            try {
                Class<?> filterClass = loader.loadClass(packageName + "." + filterName);
                Filter filter = (Filter) filterClass.newInstance();
                filters.add(filter);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                // ignore
            }
        }
        
        List<String> filterNames = filters.stream()
                .map(f -> f.getClass().getName())
                .collect(Collectors.toList());
        
        AssertState.isFalse(filterNames.isEmpty(), "Cannot obtain filter for name: " + filterName);
        AssertState.isEqual(1, filterNames.size(), "Ambiguous filter name: " + filterNames);
        
        return filters.get(0);
    }
}