package mff.java;

import mff.java.utils.PathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class PathUtilsTest {

    /**
     * check that path to resources/mff/java exists
     */
    @Test
    public void getPackageResourcesFolderPath_exists() {
        Path pathToResources = PathUtils.getPackageResourcesFolderPath();
        Assertions.assertTrue(Files.exists(pathToResources));
    }
}
