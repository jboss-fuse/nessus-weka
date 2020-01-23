package io.nessus.weka.utils;

import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class DatasetUtils {

    // Hide ctor
    private DatasetUtils() {
    }
    
    public static void convertToNominal(Instances dataset, String name, List<String> nomvals) {

        // Use the same index position
        int idx = dataset.attribute(name).index();
        
        // Rename the numerical attribute
        String rename = "org" + name;
        dataset.renameAttribute(dataset.attribute(name), rename);
        
        // Insert the replacment nominal attribute
        Attribute nomatt = new Attribute(name, nomvals);
        dataset.insertAttributeAt(nomatt, idx);

        // Convert numerical to nominal values
        Attribute numatt = dataset.attribute(rename);
        for (int i = 0; i < dataset.numInstances(); i++) {
            Instance inst = dataset.instance(i);
            if (numatt.isNumeric()) {
                Integer val = (int) inst.value(numatt);
                inst.setValue(idx, val.toString());
            } else {
                String val = inst.stringValue(numatt);
                inst.setValue(idx, val);
            }
        }
        
        // Delete the numerical attribute 
        dataset.deleteAttributeAt(numatt.index());
    }
}