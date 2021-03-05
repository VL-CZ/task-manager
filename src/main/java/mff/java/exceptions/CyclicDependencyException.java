package mff.java.exceptions;

/**
 * exception signaling cyclic dependency in tasks
 */
public class CyclicDependencyException extends RuntimeException {
    public CyclicDependencyException() {
        super("Cyclic dependency");
    }
}
