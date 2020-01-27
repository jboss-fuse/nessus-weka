package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Attribute;
import weka.core.Instances;

public class DatasetReadWriteTest extends AbstractWekaTest {
    
    @Test
    public void testDatasetRead() throws Exception {
        
        Instances dataset = DatasetUtils.read("data/iris.arff");
        Assert.assertEquals(5, dataset.numAttributes());
        Assert.assertEquals(150, dataset.numInstances());
    }
    
    @Test
    public void testCSVDatasetRead() throws Exception {
        
        Instances dataset = DatasetUtils.read("data/sfny.csv");   
        Assert.assertEquals(8, dataset.numAttributes());
        Assert.assertEquals(492, dataset.numInstances());
    }
    
    @Test
    public void testDatasetWrite() throws Exception {
        
        Instances data = Dataset.read("data/sfny.csv")
                
                // Convert the 'in_sf' attribute to nominal
                .apply("NumericToNominal -R first")
                
                // Move the 'in_sf' attribute to the end
                .apply("Reorder -R 2-last,1")
                
                // Reset the relation name
                .accept(ds -> ds.setRelationName("sfny"))

                // Write out the resulting dataset
                .write("data/sfny.arff")
                
                // Get the Weka instances
                .get();

        Assert.assertEquals("sfny", data.relationName());
        
        Assert.assertEquals(8, data.numAttributes());
        Assert.assertEquals(492, data.numInstances());
        
        Attribute attr = data.attribute("in_sf");
        Assert.assertEquals(7, attr.index());
        Assert.assertTrue(attr.isNominal());
    }
}
