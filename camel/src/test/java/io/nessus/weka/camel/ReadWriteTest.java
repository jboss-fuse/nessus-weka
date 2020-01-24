/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nessus.weka.camel;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.nessus.weka.utils.DatasetUtils;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ReadWriteTest {

    @Test
    public void wekaVersion() throws Exception {

        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start").to("weka:version");
                }
            });
            camelctx.start();
            
            ProducerTemplate producer = camelctx.createProducerTemplate();
            String res = producer.requestBody("direct:start", null, String.class);
            Assert.assertTrue(res.startsWith("3.8"));
        }
    }
    
    @Test
    public void readCSVFile() throws Exception {

        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("file:src/test/resources/data?fileName=sfny.csv&noop=true")
                        .to("weka:read").to("direct:end");
                }
            });
            camelctx.start();
            
            ConsumerTemplate consumer = camelctx.createConsumerTemplate();
            Instances dataset = consumer.receiveBody("direct:end", Instances.class);
            Assert.assertNotNull(dataset);
        }
    }
    
    @Test
    public void readCSVUrl() throws Exception {

        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start").to("weka:read");
                }
            });
            camelctx.start();
            
            Path absPath = Paths.get("src/test/resources/data/sfny.csv").toAbsolutePath();
            URL sourceUrl = absPath.toUri().toURL();
            
            ProducerTemplate producer = camelctx.createProducerTemplate();
            Instances dataset = producer.requestBody("direct:start", sourceUrl, Instances.class);
            Assert.assertNotNull(dataset);
        }
    }
    
    @Test
    @Ignore
    public void readArffInputStream() throws Exception {

        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start").to("weka:read");
                }
            });
            camelctx.start();
            
            Path absPath = Paths.get("src/test/resources/data/sfny.csv").toAbsolutePath();
            InputStream input = absPath.toUri().toURL().openStream();
            
            ProducerTemplate producer = camelctx.createProducerTemplate();
            Instances dataset = producer.requestBody("direct:start", input, Instances.class);
            Assert.assertNotNull(dataset);
        }
    }

    @Test
    public void writeDataset() throws Exception {

        try (CamelContext camelctx = new DefaultCamelContext()) {
            
            camelctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start")
                        .to("weka:write")
                        .to("file:target/data?fileName=sfny.arff");
                }
            });
            camelctx.start();
            
            Path path = Paths.get("src/test/resources/data/sfny.csv");
            DataSource source = new DataSource(path.toString()); 
            Instances dataset = source.getDataSet();
            
            ProducerTemplate producer = camelctx.createProducerTemplate();
            producer.sendBody("direct:start", dataset);
            
            Path inpath = Paths.get("target/data/sfny.arff");
            dataset = DatasetUtils.readDataset(inpath);
            Assert.assertNotNull(dataset);
        }
    }
    
}
