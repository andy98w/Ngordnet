package ngordnet.main;
import java.util.*;

class WordGraph {
    HashMap<Integer, Node> nodes;
    Node root;

    public static class Node {
        List<String> values;
        List<Node> neighbors;
        int id;

        public Node(Integer id, List<String> values) {
            this.values = values;
            this.neighbors = new ArrayList<>();
            this.id = id;
        }

        public void addEdge(Node newEdge) { neighbors.add(newEdge); }
        public List<String> getValues() { return values; }
        public void addToValues(String word) { values.add(word); }
        public int getId() { return id; }
    }

    public WordGraph() {
        nodes = new HashMap<>();
    }

    public Node makeNode(Integer setNum, List<String> values) {
        return new Node(setNum, values);
    }

    public void addNode(Node start, Node destination) {
        if (!nodes.containsKey(destination.id)) {
            nodes.put(destination.id, destination);
        }
        start.neighbors.add(destination);
    }

    public Node getNode(Integer s) {
        return nodes.get(s);
    }

    public ArrayList<String> bfs(Node start) {
        ArrayList<String> hyponyms = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();

        queue.add(start);
        visited.add(start.id);

        while (!queue.isEmpty()) {
            Node top = queue.remove();
            hyponyms.addAll(top.values);

            for (Node n : top.neighbors) {
                if (!visited.contains(n.id)) {
                    queue.add(n);
                    visited.add(n.id);
                }
            }
        }

        return hyponyms;
    }
}
