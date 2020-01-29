package io.nessus.weka;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.function.Supplier;

import weka.classifiers.Classifier;

public class ModelDecoder implements Supplier<Classifier> {

    private Classifier classifier;
    
    public ModelDecoder() {
    }
    
    public ModelDecoder(String encoded) {
        decode(encoded);
    }

    public ModelDecoder decode(String encoded) {
        try {
            byte[] bytes = Base64.getDecoder().decode(encoded);
            InputStream input = new ByteArrayInputStream(bytes);
            ObjectInputStream ins = new ObjectInputStream(input);
            classifier = (Classifier) ins.readObject();
        } catch (Exception ex) {
            throw UncheckedException.create(ex);
        }
        return this;
    }
    
    @Override
    public Classifier get() {
        return classifier;
    }
}