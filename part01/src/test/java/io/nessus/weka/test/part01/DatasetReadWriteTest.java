package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.testing.AbstractWekaTest;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DatasetReadWriteTest extends AbstractWekaTest {
    
    @Test
    public void testDatasetRead() throws Exception {
        
        Instances dataset = DatasetUtils.readDataset("data/iris.arff");
        Assert.assertEquals(5, dataset.numAttributes());
        Assert.assertEquals(150, dataset.numInstances());
    }
    
    @Test
    public void testCSVDatasetRead() throws Exception {
        
        DataSource source = new DataSource("data/sfny.csv");   
        Instances structure = source.getStructure();
        Instances dataset = source.getDataSet();
        
        Assert.assertEquals(8, structure.numAttributes());
        Assert.assertEquals(492, dataset.numInstances());
    }
    
    @Test
    public void testDatasetWrite() throws Exception {
        
        Instances dataset = DatasetUtils.readDataset("data/sfny.csv");

        // Convert the 'in_sf' attribute to nominal
        dataset = DatasetUtils.applyFilter(dataset, "NumericToNominal", "-R first");
        
        // Move the 'in_sf' attribute to the end
        dataset = DatasetUtils.applyFilter(dataset, "Reorder", "-R 2-last,1");
        
        dataset.setRelationName("sfny");
        
        DatasetUtils.writeDataset(dataset, "target/sfny.arff");
    }
}
