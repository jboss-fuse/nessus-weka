package io.nessus.weka.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Assert;
import org.junit.Test;

import io.nessus.weka.Dataset;
import io.nessus.weka.testing.AbstractWekaTest;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

public class DecisionTreeTest extends AbstractWekaTest {
    
    @Test
    public void testJ48WithCrossValidation() throws Exception {
        
        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                
                @Override
                public void configure() throws Exception {
                    
                    // Use weka to read the model training data
                    from("weka:read?path=src/test/resources/data/sfny-train.arff")
                    
                    // Build a J48 classifier using cross-validation with 10 folds
                    .to("weka:model?build=J48&xval=true&folds=10&seed=1")
                    
                    // Persist the J48 model
                    .to("weka:model?saveTo=src/test/resources/data/sfny-j48.model")
                    
                    .to("direct:end");
                }
            });
            camelctx.start();
            
            ConsumerTemplate consumer = camelctx.createConsumerTemplate();
            Dataset dataset = consumer.receiveBody("direct:end", Dataset.class);
            
            Classifier classifier = dataset.getClassifier();
            Assert.assertNotNull(classifier);
            logInfo("{}", classifier);
            
            Evaluation evaluation = dataset.getEvaluation();
            Assert.assertNotNull(evaluation);
            logInfo("{}", evaluation);
        }
    }
    
    @Test
    public void testJ48WithTrainingData() throws Exception {
        
        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                
                @Override
                public void configure() throws Exception {
                    
                    // Use weka to read the model training data
                    from("weka:read?path=src/test/resources/data/sfny-train.arff")
                    
                    // Push the current instances to the stack
                    .to("weka:push?dsname=sfny-train")
                    
                    // Build a J48 classifier with a set of named instances
                    .to("weka:model?build=J48&dsname=sfny-train")
                    
                    .to("direct:end");
                }
            });
            camelctx.start();
            
            ConsumerTemplate consumer = camelctx.createConsumerTemplate();
            Dataset dataset = consumer.receiveBody("direct:end", Dataset.class);
            
            Classifier classifier = dataset.getClassifier();
            Assert.assertNotNull(classifier);
            logInfo("{}", classifier);
            
            Evaluation evaluation = dataset.getEvaluation();
            Assert.assertNotNull(evaluation);
            logInfo("{}", evaluation);
        }
    }
    
    @Test
    public void testJ48WithCurrentInstances() throws Exception {
        
        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                
                @Override
                public void configure() throws Exception {
                    
                    // Use weka to read the model training data
                    from("weka:read?path=src/test/resources/data/sfny-train.arff")
                    
                    // Build a J48 classifier using the current dataset
                    .to("weka:model?build=J48")
                    
                    .to("direct:end");
                }
            });
            camelctx.start();
            
            ConsumerTemplate consumer = camelctx.createConsumerTemplate();
            Dataset dataset = consumer.receiveBody("direct:end", Dataset.class);
            
            Classifier classifier = dataset.getClassifier();
            Assert.assertNotNull(classifier);
            logInfo("{}", classifier);
            
            Evaluation evaluation = dataset.getEvaluation();
            Assert.assertNotNull(evaluation);
            logInfo("{}", evaluation);
        }
    }
}
