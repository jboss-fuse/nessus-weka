package io.nessus.weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import weka.classifiers.Classifier;

public class ModelLoader implements Supplier<Classifier> {

    private final Path inpath;
    
    public ModelLoader(String inpath) {
        this(Paths.get(inpath));
    }

    public ModelLoader(Path inpath) {
        this.inpath = inpath;
    }

    @Override
    public Classifier get() {
        try (InputStream instream = new FileInputStream(inpath.toFile())) {
            return (Classifier) new ObjectInputStream(instream).readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw UncheckedException.create(ex);
        }
   }
}