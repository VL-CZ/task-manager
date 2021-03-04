package mff.java;

import mff.java.utils.PathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class PathUtilsTest {

    /**
     * check that path to resources exists
     */
    @Test
    public void getResourcesFolderPath_exists() {
        Path pathToResources = PathUtils.getResourcesFolderPath();
        Assertions.assertTrue(Files.exists(pathToResources));
    }
}
