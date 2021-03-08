package mff.java.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {

    /**
     * get path to resources folder
     * @return path to resources folder
     */
    private static Path getResourcesFolderPath() {
        return Paths.get("src", "main", "resources");
    }

    /**
     * get path to resources folder for mff.java package
     *
     * @return path to resources/mff/java
     */
    public static Path getPackageResourcesFolderPath() {
        return Paths.get(getResourcesFolderPath().toString(), "mff", "java");
    }
}
