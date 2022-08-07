package submit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import graph.FindState;
import graph.Finder;
import graph.FleeState;
import graph.Node;
import graph.NodeStatus;

/** A solution with find-the-Orb optimized and flee getting out as fast as possible. */
public class Pollack extends Finder {

    /** Get to the orb in as few steps as possible. <br>
     * Once you get there, you must return from the function in order to pick it up. <br>
     * If you continue to move after finding the orb rather than returning, it will not count.<br>
     * If you return from this function while not standing on top of the orb, it will count as <br>
     * a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the orb in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the orb at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * currentLoc(), neighbors(), and distanceToOrb() in FindState.<br>
     * You know you are standing on the orb when distanceToOrb() is 0.
     *
     * Use function moveTo(long id) in FindState to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the orb, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first search. <br>
     * Some modification is necessary to make the search better, in general. */
    @Override
    public void find(FindState state) {
        // TODO 1: Walk to the orb
//        HashSet<Long> visited = new HashSet<>();
//        dfsWalk2(state, visited);
        
        dfsWalk(state);
    }
    
    public void dfsWalk(FindState state) {
        Stack<Long> s = new Stack<>();
        HashSet<Long> visited = new HashSet<>();
        s.add(state.currentLoc());
        visited.add(state.currentLoc());
        while (!s.isEmpty()) {
            if (state.distanceToOrb() == 0) return;
            Long currLoc = state.currentLoc();
            for (var n : state.neighbors()) {
                Long loc = n.getId();
                if (!visited.contains(loc)) {
                    visited.add(loc);
                    s.push(currLoc);
                    s.push(loc);
                }
            }
            state.moveTo(s.pop());           
        }
    }
    
    public void dfsWalk2(FindState state, HashSet<Long> visited) {
        if (state.distanceToOrb() == 0) return;
        visited.add(state.currentLoc());
        Long currLoc = state.currentLoc();
        for (var n : state.neighbors()) {
            Long loc = n.getId();
            if (!visited.contains(loc)) {
                state.moveTo(loc);
                dfsWalk2(state, visited);
                if (state.distanceToOrb() == 0) return;
                state.moveTo(currLoc);
            }
        }        
    }

    /** Get out the cavern before the ceiling collapses, trying to collect as <br>
     * much gold as possible along the way. Your solution must ALWAYS get out <br>
     * before steps runs out, and this should be prioritized above collecting gold.
     *
     * You now have access to the entire underlying graph, which can be accessed <br>
     * through FleeState state. <br>
     * currentNode() and exit() will return Node objects of interest, and <br>
     * allsNodes() will return a collection of all nodes on the graph.
     *
     * Note that the cavern will collapse in the number of steps given by <br>
     * stepsLeft(), and for each step this number is decremented by the <br>
     * weight of the edge taken. <br>
     * Use stepsLeft() to get the steps still remaining, and <br>
     * moveTo() to move to a destination node adjacent to your current node.
     *
     * You must return from this function while standing at the exit. <br>
     * Failing to do so before steps runs out or returning from the wrong <br>
     * location will be considered a failed run.
     *
     * You will always have enough steps to flee using the shortest path from the <br>
     * starting position to the exit, although this will not collect much gold. <br>
     * For this reason, using Dijkstra's to plot the shortest path to the exit <br>
     * is a good starting solution
     *
     * Here's another hint. Whatever you do you will need to traverse a given path. It makes sense
     * to write a method to do this, perhaps with this specification:
     *
     * // Traverse the nodes in moveOut sequentially, starting at the node<br>
     * // pertaining to state <br>
     * // public void moveAlong(FleeState state, List<Node> moveOut) */
    @Override
    public void flee(FleeState state) {
        // TODO 2. Get out of the cavern in time, picking up as much gold as possible.
        for (Node node : Path.shortestPath(state.currentNode(), state.exit())) {
            if (node != state.currentNode()) state.moveTo(node);
        }
    }

}
