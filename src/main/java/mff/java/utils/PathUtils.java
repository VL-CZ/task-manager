package mff.java.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {

    /**
     * get path to resources folder
     *
     * @return path to resources folder
     */
    public static Path getResourcesFolderPath() {
        return Paths.get("src", "main", "resources");
    }
}
