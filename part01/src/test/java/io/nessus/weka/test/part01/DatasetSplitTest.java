package io.nessus.weka.test.part01;

import static io.nessus.weka.utils.DatasetUtils.applyFilter;

import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;

public class DatasetSplitTest extends AbstractWekaTest {
    
    @Test
    public void testDataSplit() throws Exception {
        
        Dataset rndset = Dataset.create("data/sfny.arff")
                
                .filter("Randomize -S 0")
                
                .filter("RenameRelation -modify sfny-random")
                
                .write("data/sfny-random.arff");
                
        int numTotal = rndset.getInstances().numInstances();
        int firstTrainIdx = (int) Math.round(numTotal * 0.20);
        int lastTestIdx = firstTrainIdx - 1;
        
        Dataset trainset = new Dataset(rndset.getInstances())
                
                .consumeInstances((in) -> logInfo("Instances: {}", in.numInstances()))
                
                .consumeInstances((in) -> logInfo("Removing:  1-{}", lastTestIdx))
                
                .applyToInstances((in) -> applyFilter(in, "RemoveRange -R 1-" + lastTestIdx))
                
                .consumeInstances((in) -> logInfo("Training:  {}-{} ({})", firstTrainIdx, numTotal, in.numInstances()))
                
                .filter("RenameRelation -modify sfny-train")
                
                .write("data/sfny-80pct.arff");
        
        logInfo();
        
        Dataset testset = new Dataset(rndset.getInstances())
                
                .consumeInstances((in) -> logInfo("Instances: {}", in.numInstances()))
                
                .consumeInstances((in) -> logInfo("Removing:  {}-{}", firstTrainIdx, numTotal))
                
                .applyToInstances((in) -> applyFilter(in, "RemoveRange -R " + firstTrainIdx + "-" + numTotal))
                
                .consumeInstances((in) -> logInfo("Testing:   1-{} ({})", lastTestIdx, in.numInstances()))
                
                .filter("RenameRelation -modify sfny-test")
                
                .write("data/sfny-20pct.arff");
                
        Assert.assertEquals(492, rndset.getInstances().numInstances());
        Assert.assertEquals(395, trainset.getInstances().numInstances());
        Assert.assertEquals(97, testset.getInstances().numInstances());
    }

    
    @Test
    public void testStratifiedSplit() throws Exception {
        
        Dataset dataset = Dataset.create("data/sfny.arff")
                
                .push()
                
                .filter("StratifiedRemoveFolds -N 5")
                
                .filter("RenameRelation -modify sfny-test")
                
                .write("data/sfny-20pct-strat.arff")
                
                .pushTestSet()
                
                .pop()
                
                .filter("StratifiedRemoveFolds -N 5 -V")
                
                .filter("RenameRelation -modify sfny-train")
                
                .write("data/sfny-80pct-strat.arff")
                
                .pushTrainingSet();
                                
        Assert.assertEquals(393, dataset.popTrainingSet().getInstances().numInstances());
        Assert.assertEquals(99, dataset.popTestSet().getInstances().numInstances());
    }
}
