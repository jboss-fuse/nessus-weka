/*-
 * #%L
 * Nessus :: Weka :: API
 * %%
 * Copyright (C) 2020 Nessus
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.nessus.weka.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.nessus.weka.AssertArg;
import io.nessus.weka.AssertState;
import io.nessus.weka.UncheckedException;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.Loader;
import weka.filters.Filter;

public class DatasetUtils {

    // Hide ctor
    private DatasetUtils() {
    }
    
    public static Instances read(URL url) {
        return readInternal(url);
    }
    
    public static Instances read(Path inpath) {
        return readInternal(inpath);
    }

    public static Instances read(String inpath) {
        return readInternal(Paths.get(inpath));
    }

    public static Instances read(InputStream input) {
        return readInternal(input);
    }
    
    public static void write(Instances instances, Path outpath) {
        writeInternal(instances, outpath);
    }
    
    public static Instances applyFilter(Instances instances, String filterSpec) {
        return applyFilter(instances, new OperatorSpec(filterSpec));
    }

    public static Instances applyFilter(Instances instances, String name, String[] options) {
        return applyFilter(instances, new OperatorSpec(name, options));
    }

    public static Instances applyFilter(Instances instances, OperatorSpec spec) {
        try {
            Filter filter = loadInstance(spec.getName(), Filter.class);
            filter.setOptions(spec.getOptions());
            filter.setInputFormat(instances);
            instances = Filter.useFilter(instances, filter);
            return instances;
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }

    public static Classifier buildClassifier(Instances instances, String classifierSpec) {
        return buildClassifier(instances, new OperatorSpec(classifierSpec));
    }
    
    public static Classifier buildClassifier(Instances instances, String name, String[] options) {
        return buildClassifier(instances, new OperatorSpec(name, options));
    }
    
    public static Classifier buildClassifier(Instances instances, OperatorSpec spec) {
        try {
            Classifier classifier = loadInstance(spec.getName(), Classifier.class);
            ((OptionHandler) classifier).setOptions(spec.getOptions());
            classifier.buildClassifier(instances);
            return classifier;
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }
    
    private static Instances readInternal(Path inpath) {
        try {
            
            Instances instances = null;
            
            // https://github.com/tdiesler/weka-3.8/issues/6
            String fileName = inpath.getFileName().toString();
            if (instances == null && fileName.endsWith(".csv")) {
                
                try (InputStream input = new FileInputStream(inpath.toFile())) {
                    instances = readInternal(input);
                }
                
            } else {
                
                DataSource source = new DataSource(inpath.toString()); 
                instances = source.getDataSet();
                
            }
            
            return instances;
            
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }
    
    private static Instances readInternal(URL url) {
        
        // https://github.com/tdiesler/weka-3.8/issues/7
        String externalForm = url.toExternalForm();
        if (externalForm.startsWith("file:")) {
            Path inpath = Paths.get(url.getPath());
            return read(inpath);
        }
        
        try {
            DataSource source = new DataSource(externalForm); 
            return source.getDataSet();
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
    }
    
    private static Instances readInternal(InputStream input) {
        
        Instances instances = null;
        
        try {
            
            // https://github.com/tdiesler/weka-3.8/issues/6
            input = new BufferedInputStream(input);
            input.mark(10240);
            
            // First try .arff
            try {
                Loader loader = new ArffLoader();
                loader.setSource(input);
                loader.getStructure();
                instances = loader.getDataSet();
            } catch (IOException ex) {
                String exmsg = ex.getMessage();
                if (!exmsg.contains("Unable to determine structure as arff")) {
                    throw ex;
                }
                input.reset();
            }
            
            // Next try .csv
            if (instances == null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(baos));
                
                try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                    String line = br.readLine();
                    while (line != null) {
                        if (!line.startsWith("#")) {
                            bw.write(line);
                            bw.newLine();
                        }
                        line = br.readLine();
                    }
                    bw.flush();
                }

                input = new ByteArrayInputStream(baos.toByteArray());
                
                Loader loader = new CSVLoader();
                loader.setSource(input);
                instances = loader.getDataSet();
            }
            
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        
        return instances;
    }
    
    private static void writeInternal(Instances instances, Path outpath) {
        try {
            DataSink.write(outpath.toString(), instances);
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
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
                "weka.classifiers.rules",
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
    
    public static class OperatorSpec {
        
        private final String name;
        private final String[] options;
        
        public OperatorSpec(String spec) {
            this(name(spec), options(spec));
        }
        
        public OperatorSpec(String name, String options) {
            this(name, optionsNotNull(options).split(" "));
        }
        
        public OperatorSpec(String name, String[] options) {
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
            return idx > 0 ? spec.substring(0, idx) : spec;
        }

        private static String options(String spec) {
            int idx = spec.indexOf(" ");
            return idx > 0 ? spec.substring(idx + 1) : "";
        }

        private static String optionsNotNull(String options) {
            return options != null ? options : "";
        }
        
        public String toString() {
            return name + " " + Arrays.asList(options);
        }
    }
}
