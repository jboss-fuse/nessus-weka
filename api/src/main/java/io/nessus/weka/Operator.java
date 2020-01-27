package io.nessus.weka;

import java.util.Arrays;

public class Operator {
    
    private final String name;
    private final String[] options;
    
    public Operator(String spec) {
        this(name(spec), options(spec));
    }
    
    public Operator(String name, String options) {
        this(name, optionsNotNull(options).split(" "));
    }
    
    public Operator(String name, String[] options) {
        AssertArg.notNull(name, "Null name");
        AssertArg.notNull(options, "Null options");
        this.name = name;
        this.options = options;
    }
    
    public String getName() {
        return name;
    }

    public String[] getOptions() {
        return options;
    }

    private static String name(String spec) {
        int idx = spec.indexOf(" ");
        return spec.substring(0, idx);
    }

    private static String options(String spec) {
        int idx = spec.indexOf(" ");
        return spec.substring(idx + 1);
    }

    private static String optionsNotNull(String options) {
        return options != null ? options : "";
    }
    
    public String toString() {
        return name + " " + Arrays.asList(options);
    }
}