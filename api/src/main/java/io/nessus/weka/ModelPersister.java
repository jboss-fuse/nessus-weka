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
package io.nessus.weka;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import weka.classifiers.Classifier;

public class ModelPersister implements Consumer<Classifier> {

    private final Path outpath;
    
    public ModelPersister(String outpath) {
        this(Paths.get(outpath));
    }

    public ModelPersister(Path outpath) {
        this.outpath = outpath;
    }

    @Override
    public void accept(Classifier cl) {
        try (OutputStream outstream = new FileOutputStream(outpath.toFile())) {
            outpath.getParent().toFile().mkdirs();
            new ObjectOutputStream(outstream).writeObject(cl);
        } catch (IOException ex) {
            throw UncheckedException.create(ex);
        }
   }
}
