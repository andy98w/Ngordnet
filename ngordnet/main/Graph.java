package ngordnet.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import edu.princeton.cs.algs4.In;

public class Graph {
    WordGraph graph = new WordGraph();
    HashMap<String, List<Integer>> sets = new HashMap<>();

    public Graph(String synsetFile, String hyponymFile) {
        In synsets = new In(synsetFile);

        while (synsets.hasNextLine() && !synsets.isEmpty()) {
            String[] parts = synsets.readLine().split(",");
            Integer synsetId = Integer.parseInt(parts[0]);
            String[] synsetWords = parts[1].split(" ");

            track(synsetId, synsetWords);

            if (graph.nodes.containsKey(synsetId)) {
                for (String e : synsetWords) {
                    graph.getNode(synsetId).addToValues(e);
                }
            } else {
                graph.nodes.put(synsetId, graph.makeNode(synsetId, Arrays.asList(synsetWords)));
            }
        }

        In hyponyms = new In(hyponymFile);

        while (hyponyms.hasNextLine() && !hyponyms.isEmpty()) {
            String line = hyponyms.readLine();
            String[] ids = line.split(",");
            Integer synsetId = Integer.parseInt(ids[0]);

            for (int i = 1; i < ids.length; i++) {
                graph.addNode(graph.nodes.get(synsetId), graph.nodes.get(Integer.parseInt(ids[i])));
            }
        }
    }

    public HashSet<String> hyponyms(String word) {
        List<Integer> nums = sets.get(word);
        if (sets.get(word) == null) {
            nums = new ArrayList<>();
        }

        HashSet<String> hyponym = new HashSet<>();
        for (Integer num : nums) {
            hyponym.addAll(graph.bfs(graph.getNode(num)));
        }

        return hyponym;
    }

    public void track(Integer synsetId, String[] words) {
        for (String word : words) {
            if (sets.containsKey(word)) {
                sets.get(word).add(synsetId);
            } else {
                List<Integer> al = new ArrayList<>();
                al.add(synsetId);
                sets.put(word, al);
            }
        }
    }
}
