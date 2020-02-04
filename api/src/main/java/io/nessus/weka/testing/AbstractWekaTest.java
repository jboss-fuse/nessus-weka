package io.nessus.weka.testing;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.nessus.weka.LogSupport;

public class AbstractWekaTest extends LogSupport {
	
    protected Path getOutPath() {
        return Paths.get("target");
    }

    protected InputStream getInputStream(String path) {
        InputStream input = getClass().getResourceAsStream(path);
        return input;
    }
}
