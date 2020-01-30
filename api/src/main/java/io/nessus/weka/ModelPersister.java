package io.nessus.weka;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import weka.classifiers.Classifier;

public class ModelPersister implements Consumer<Classifier> {

    private final Path outpath;
    
    public ModelPersister(String outpath) {
        this(Paths.get(outpath));
    }

    public ModelPersister(Path outpath) {
        this.outpath = outpath;
    }

    @Override
    public void accept(Classifier cl) {
        try (OutputStream outstream = new FileOutputStream(outpath.toFile())) {
            outpath.getParent().toFile().mkdirs();
            new ObjectOutputStream(outstream).writeObject(cl);
        } catch (IOException ex) {
            throw UncheckedException.create(ex);
        }
   }
}