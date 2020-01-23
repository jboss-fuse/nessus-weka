package io.nessus.weka.test.part01;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.testing.AbstractWekaTest;
import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

public class DatasetReadWriteTest extends AbstractWekaTest {
    
    @Test
    public void testDatasetRead() throws Exception {
        
        DataSource source = new DataSource("data/iris.arff");   
        Instances structure = source.getStructure();
        Instances dataset = source.getDataSet();
        
        Assert.assertEquals(5, structure.numAttributes());
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
        
        DataSource source = new DataSource("data/sfny.csv");   
        Instances dataset = source.getDataSet();

        DatasetUtils.convertToNominal(dataset, "in_sf", Arrays.asList("0", "1"));
        
        Path outpath = Paths.get("data/sfny.arff");
        DataSink.write(outpath.toString(), dataset);
    }
}
