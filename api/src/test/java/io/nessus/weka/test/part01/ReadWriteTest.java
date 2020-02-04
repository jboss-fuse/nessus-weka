/*-
 * #%L
 * Nessus :: Weka :: API
 * %%
 * Copyright (C) 2020 Nessus
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.nessus.weka.test.part01;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.core.Attribute;
import weka.core.Instances;

public class ReadWriteTest extends AbstractWekaTest {
    
    @Test
    public void readArffPath() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/iris.arff");
        Dataset dataset = Dataset.create(inpath);
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(5, instances.numAttributes());
        Assert.assertEquals(150, instances.numInstances());
    }
    
    @Test
    public void readArffURL() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/iris.arff");
        Dataset dataset = Dataset.create(inpath.toUri().toURL());   
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(5, instances.numAttributes());
        Assert.assertEquals(150, instances.numInstances());
    }
    
    @Test
    public void readArffInputStream() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/iris.arff");
        Dataset dataset = Dataset.create(inpath.toUri().toURL().openStream());   
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(5, instances.numAttributes());
        Assert.assertEquals(150, instances.numInstances());
    }
    
    @Test
    public void readCsvPath() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/sfny.csv");
        Dataset dataset = Dataset.create(inpath);
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(8, instances.numAttributes());
        Assert.assertEquals(492, instances.numInstances());
    }
    
    @Test
    public void readCsvURL() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/sfny.csv");
        Dataset dataset = Dataset.create(inpath.toUri().toURL());
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(8, instances.numAttributes());
        Assert.assertEquals(492, instances.numInstances());
    }    
    
    @Test
    public void readCsvInputStream() throws Exception {
        
        Path inpath = Paths.get("src/test/resources/data/sfny.csv");
        Dataset dataset = Dataset.create(inpath.toUri().toURL().openStream());
        Instances instances = dataset.getInstances();
        
        Assert.assertEquals(8, instances.numAttributes());
        Assert.assertEquals(492, instances.numInstances());
    }
    
    @Test
    public void readCsvFilterWriteArff() throws Exception {
        
        Instances data = Dataset.create("src/test/resources/data/sfny.csv")
                
                // Convert the 'in_sf' attribute to nominal
                .apply("NumericToNominal -R first")
                
                // Move the 'in_sf' attribute to the end
                .apply("Reorder -R 2-last,1")
                
                // Reset the relation name
                .apply("RenameRelation -modify sfny")

                // Write out the resulting dataset
                .write("src/test/resources/data/sfny.arff")
                
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
