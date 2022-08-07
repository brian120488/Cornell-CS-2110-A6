
package submit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import graph.Node;

/** This class contains the solution to A7, shortest-path algorithm, <br>
 * and other methods for an undirected graph. */
public class Path {

    /** = the shortest path from node v to node end <br>
     * ---or the empty list if a path does not exist. <br>
     * Note: The empty list is a list with 0 elements ---it is not "null". */
    public static List<Node> shortestPath(Node v, Node end) {
        /** TODO: replace with your implementation from Part 1. */
        
        // Contains an entry for each node in the frontier set. The priority of
        // a node is the length of the shortest known path from v to the node
        // using only settled nodes except for the last node, which is in F.
        var F= new Heap<Node>(true);
        F.insert(v, 0);

        // Put in a declaration of the HashMap here, with a suitable name
        // for it and a suitable definition of its meaning --what it contains,
        // etc. See Section 10 point 4 of the A7 handout for help.
        // HashMap to hold nodes and their info (distance from v and backpointer) in sets S and F
        HashMap<Node, Info> SandF = new HashMap<>();
        SandF.put(v, new Info(0, null));
        
        while (F.size() != 0) {
            Node minNode = F.poll();
            if (minNode == end) return pathToEnd(SandF, end);
            for (var edge : minNode.exits()) {
                Node other = edge.other(minNode);    
                int dist = SandF.get(minNode).dist + edge.length;
                if (!SandF.containsKey(other)) {
                    F.insert(other, dist);
                    Info oInfo = new Info(dist, minNode);
                    SandF.put(other, oInfo);
                } else if (dist < SandF.get(other).dist) {     
                    SandF.get(other).dist = dist;
                    SandF.get(other).bkptr = minNode;
                    F.changePriority(other, dist);
                }
            }
        }
        // no path from v to end. Do not change this
        return new LinkedList<>();
    }

    /** An instance contains info about a node: <br>
     * the known shortest distance of this node from the start node and <br>
     * its backpointer: the previous node on a shortest path <br>
     * from the first node to this node (null for the start node). */
    private static class Info {
        /** shortest known distance from the start node to this one. */
        private int dist;
        /** backpointer on path (with shortest known distance) from <br>
         * start node to this one */
        private Node bkptr;

        /** Constructor: an instance with dist d from the start node and<br>
         * backpointer p. */
        private Info(int d, Node p) {
            dist= d;     // Distance from start node to this one.
            bkptr= p;    // Backpointer on the path (null if start node)
        }

        /** = a representation of this instance. */
        @Override
        public String toString() {
            return "dist " + dist + ", bckptr " + bkptr;
        }
    }

    /** = the path from the start node to node end.<br>
     * Precondition: SandF contains all the necessary information about<br>
     * ............. the path. */
    public static List<Node> pathToEnd(HashMap<Node, Info> SandF, Node end) {
        List<Node> path= new LinkedList<>();
        var p= end;
        // invariant: All the nodes from p's successor to node
        // . . . . . .end are in path, in reverse order.
        while (p != null) {
            path.add(0, p);
            p= SandF.get(p).bkptr;
        }
        return path;
    }

    /** = the sum of the weights of the edges on path p. <br>
     * Precondition: p contains at least 1 node. <br>
     * If 1 node, it's a path of length 0, i.e. with no edges. */
    public static int pathSum(List<Node> p) {
        synchronized (p) {
            Node w= null;
            var sum= 0;
            // invariant: if w is null, n is the start node of the path.<br>
            // .......... if w is not null, w is the predecessor of n on the path.
            // .......... sum = sum of weights on edges from first node to v
            for (Node n : p) {
                if (w != null) sum= sum + w.edge(n).length;
                w= n;
            }
            return sum;
        }
    }

}
