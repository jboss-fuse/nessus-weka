package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.core.Attribute;
import weka.core.Instances;

public class ReadWriteTest extends AbstractWekaTest {
    
    @Test
    public void testArffRead() throws Exception {
        
        Dataset dataset = Dataset.create("data/iris.arff");
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(5, instances.numAttributes());
        Assert.assertEquals(150, instances.numInstances());
    }
    
    @Test
    public void testCsvRead() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.csv");   
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(8, instances.numAttributes());
        Assert.assertEquals(492, instances.numInstances());
    }
    
    @Test
    public void readCsvFilterWriteArff() throws Exception {
        
        Instances data = Dataset.create("data/sfny.csv")
                
                // Convert the 'in_sf' attribute to nominal
                .apply("NumericToNominal -R first")
                
                // Move the 'in_sf' attribute to the end
                .apply("Reorder -R 2-last,1")
                
                // Reset the relation name
                .apply("RenameRelation -modify sfny")

                // Write out the resulting dataset
                .write("data/sfny.arff")
                
                // Get the Weka instances
                .getInstances();

        Assert.assertEquals("sfny", data.relationName());
        
        Assert.assertEquals(8, data.numAttributes());
        Assert.assertEquals(492, data.numInstances());
        
        Attribute attr = data.attribute("in_sf");
        Assert.assertEquals(7, attr.index());
        Assert.assertTrue(attr.isNominal());
    }
}
