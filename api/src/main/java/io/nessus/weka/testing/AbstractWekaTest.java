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
package io.nessus.weka.testing;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.nessus.common.testing.AbstractTest;

public class AbstractWekaTest extends AbstractTest {
	
    protected Path getOutPath() {
        return Paths.get("target");
    }

    protected InputStream getInputStream(String path) {
        InputStream input = getClass().getResourceAsStream(path);
        return input;
    }
}
