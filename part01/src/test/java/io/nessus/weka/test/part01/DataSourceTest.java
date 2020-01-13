package io.nessus.weka.test.part01;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.testing.AbstractWekaTest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DataSourceTest extends AbstractWekaTest {
    
    @Test
    public void testDataRead() throws Exception {
        
        DataSource source = new DataSource("data/iris.arff");   
        Instances structure = source.getStructure();
        Instances dataset = source.getDataSet();
        
        Assert.assertEquals(5, structure.numAttributes());
        Assert.assertEquals(150, dataset.numInstances());
    }
}
