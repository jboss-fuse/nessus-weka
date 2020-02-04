/*-
 * #%L
 * Nessus :: Weka :: Part 01
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

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.core.Instances;

public class DatasetSplitTest extends AbstractWekaTest {
    
    @Test
    public void testDataSplit() throws Exception {
        
        Dataset rndset = Dataset.create("data/sfny.arff")
                
                .apply("Randomize -S 0")
                
                .apply("RenameRelation -modify sfny-random")
                
                .write("data/sfny-random.arff");
                
        int numTotal = rndset.getInstances().numInstances();
        int firstTrainIdx = (int) Math.round(numTotal * 0.20);
        int lastTestIdx = firstTrainIdx - 1;
        
        Dataset trainset = Dataset.create(rndset.getInstances())
                
                .consumeInstances((in) -> logInfo("Instances: {}", in.numInstances()))
                
                .consumeInstances((in) -> logInfo("Removing:  1-{}", lastTestIdx))
                
                .apply("RemoveRange -R 1-" + lastTestIdx)
                
                .consumeInstances((in) -> logInfo("Training:  {}-{} ({})", firstTrainIdx, numTotal, in.numInstances()))
                
                .apply("RenameRelation -modify sfny-train80")
                
                .write("data/sfny-train80.arff");
        
        logInfo();
        
        Dataset testset = Dataset.create(rndset.getInstances())
                
                .consumeInstances((in) -> logInfo("Instances: {}", in.numInstances()))
                
                .consumeInstances((in) -> logInfo("Removing:  {}-{}", firstTrainIdx, numTotal))
                
                .apply("RemoveRange -R " + firstTrainIdx + "-" + numTotal)
                
                .consumeInstances((in) -> logInfo("Testing:   1-{} ({})", lastTestIdx, in.numInstances()))
                
                .apply("RenameRelation -modify sfny-test20")
                
                .write("data/sfny-test20.arff");
                
        Assert.assertEquals(492, rndset.getInstances().numInstances());
        Assert.assertEquals(395, trainset.getInstances().numInstances());
        Assert.assertEquals(97, testset.getInstances().numInstances());
    }

    
    @Test
    public void testStratifiedSplit() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.arff")
                
                .push()
                
                .apply("StratifiedRemoveFolds -N 5")
                
                .apply("RenameRelation -modify sfny-test")
                
                .applyToInstances((Instances ins) -> { 
                    ins.setRelationName("sfny-test"); 
                    return ins;
                 })
                
                .write("data/sfny-test.arff")
                
                .pushTestSet()
                
                .pop()
                
                .apply("StratifiedRemoveFolds -N 5 -V")
                
                .apply("RenameRelation -modify sfny-train")
                
                .write("data/sfny-train.arff")
                
                .pushTrainingSet();
                                
        Assert.assertEquals(393, dataset.popTrainingSet().getInstances().numInstances());
        Assert.assertEquals(99, dataset.popTestSet().getInstances().numInstances());
    }
}
