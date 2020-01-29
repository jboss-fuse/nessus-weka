package io.nessus.weka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.function.Consumer;

import weka.classifiers.Classifier;

public class ModelEncoder implements Consumer<Classifier> {

    private String encoded;
    
    public ModelEncoder() {
    }
    
    public ModelEncoder(Classifier cl) {
        accept(cl);
    }

    @Override
    public void accept(Classifier cl) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(cl);
            byte[] bytes = baos.toByteArray();
            encoded = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            throw UncheckedException.create(ex);
        }
   }
    
   public String getEncodedModel() {
        return encoded;
   }
}