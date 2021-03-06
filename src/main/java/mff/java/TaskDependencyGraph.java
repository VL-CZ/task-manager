package mff.java;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import mff.java.exceptions.CyclicDependencyException;
import mff.java.models.TaskDependency;

import java.util.*;

/**
 * class representing graph between {@link mff.java.models.TaskDependency} instances
 */
public class TaskDependencyGraph {

    /**
     * inner class representing node of a graph
     */
    private class Node {
        /**
         * is this node already visited?
         */
        private boolean isVisited;

        /**
         * is this node already closed?
         */
        private boolean isClosed;

        /**
         * id of the node
         */
        private final int id;

        /**
         * list of neighbours of this node
         */
        private final Collection<Integer> neighbours;

        private Node(int id) {
            this.id = id;
            this.neighbours = new ArrayList<>();
            this.isVisited = false;
            this.isClosed = false;
        }
    }

    /**
     * map of graph nodes
     * <br>
     * <code>nodes[i]</code> contains {@link Node} instance with id=i
     */
    private final Map<Integer, Node> nodes;

    /**
     * bi-directional mapping between taskId and nodeId
     */
    private final BiMap<Integer, Integer> taskIdToNodeIdMap;

    /**
     * collection of all dependencies
     */
    private final Collection<TaskDependency> dependencies;

    /**
     * cache with task order
     */
    private int[] tasksOrderCache;

    public TaskDependencyGraph(Collection<TaskDependency> dependencies) {
        this.dependencies = dependencies;
        nodes = new HashMap<>();
        taskIdToNodeIdMap = HashBiMap.create();
    }

    /**
     * order tasks according to their dependencies
     * <br>
     * e.g. get order in which we should complete the tasks
     * <br>
     * if there are mutiple options, return any of them
     *
     * @return array of task IDs in incerasing order according to dependencies
     */
    public int[] getOrdering() {
        // if cached, return id
        if (tasksOrderCache != null) {
            return tasksOrderCache;
        }

        createGraph();
        var topologicalOrder = getTopologicalOrder();

        var taskOrder = getTaskIdsFromTopologicalOrder(topologicalOrder);

        // cache the result
        tasksOrderCache = taskOrder;

        return taskOrder;
    }

    /**
     * create graph vertices and edges, store it into {@link #nodes} field
     */
    private void createGraph() {
        var allTaskIds = getAllTaskIds();
        initTaskToNodeMapping(allTaskIds);

        // init nodes (vertices)
        for (int taskId : allTaskIds) {
            int nodeId = taskIdToNodeIdMap.get(taskId);
            nodes.put(nodeId, new Node(nodeId));
        }

        // init edges
        for (var dependency : dependencies) {
            int nodeId = taskIdToNodeIdMap.get(dependency.getTaskId());
            int dependsOnNodeId = taskIdToNodeIdMap.get(dependency.getDependsOnTaskId());

            nodes.get(nodeId).neighbours.add(dependsOnNodeId);
        }
    }

    /**
     * get all task IDs contained in {@link #dependencies} (either as taskId or dependsOnTaskId)
     *
     * @return set of all contained taskIDs in {@link #dependencies}
     * @throws CyclicDependencyException if cycle detected
     */
    private Set<Integer> getAllTaskIds() {
        var allTaskIds = new HashSet<Integer>();
        for (var dependency : dependencies) {
            allTaskIds.add(dependency.getTaskId());
            allTaskIds.add(dependency.getDependsOnTaskId());
        }
        return allTaskIds;
    }

    /**
     * fill {@link #taskIdToNodeIdMap} with values
     */
    private void initTaskToNodeMapping(Collection<Integer> allTaskIds) {
        int nodeId = 0;
        for (var taskId : allTaskIds) {
            taskIdToNodeIdMap.put(taskId, nodeId);
            nodeId++;
        }
    }

    /**
     * get nodes in topological order
     *
     * @return topological order of nodeIDs
     * @throws CyclicDependencyException if cycle detected
     */
    private Collection<Integer> getTopologicalOrder() {
        var topologicalOrder = new ArrayList<Integer>();

        for (Node node : nodes.values()) {
            if (!node.isVisited)
                topologicalSortFromNode(node, topologicalOrder);
        }

        return topologicalOrder;
    }

    /**
     * search for nodes from the given node, and add them to topological order
     *
     * @param node             node to start
     * @param topologicalOrder list of nodes in topological order
     * @throws CyclicDependencyException if cycle detected
     */
    private void topologicalSortFromNode(Node node, Collection<Integer> topologicalOrder) {
        node.isVisited = true;

        for (int neighbourId : node.neighbours) {
            Node neighbour = nodes.get(neighbourId);
            if (!neighbour.isVisited)
                topologicalSortFromNode(neighbour, topologicalOrder);
            else if (!neighbour.isClosed)
                throw new CyclicDependencyException();
        }

        topologicalOrder.add(node.id);
        node.isClosed = true;
    }

    /**
     * get taskIDs from topologicalOrder of nodes
     *
     * @param topologicalOrder nodeIDs in topologicalOrder
     * @return list of taskIDs in topological order
     */
    private int[] getTaskIdsFromTopologicalOrder(Collection<Integer> topologicalOrder) {
        int[] taskIds = new int[topologicalOrder.size()];

        int index = 0;
        for (int nodeId : topologicalOrder) {
            int taskId = taskIdToNodeIdMap.inverse().get(nodeId);
            taskIds[index] = taskId;
            index++;
        }

        return taskIds;
    }
}
