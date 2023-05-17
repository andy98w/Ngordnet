package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    Graph ngrams;
    NGramMap gramMap;

    public HyponymsHandler(Graph ngrams, NGramMap gramMap) {
        super();
        this.ngrams = ngrams;
        this.gramMap = gramMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        if (words.isEmpty()) {
            return "[]";
        }

        HashSet<String> hyps = ngrams.hyponyms(words.get(0));
        if (hyps.isEmpty()) {
            return "[]";
        }

        for (String word : words) {
            hyps.retainAll(ngrams.hyponyms(word));
        }

        List<String> hypsList = new ArrayList<>(hyps);

        if (q.k() != 0) {
            HashMap<String, Double> hypCounts = new HashMap<>();

            for (String word : hyps) {
                List<Double> history = gramMap.countHistory(word, q.startYear(), q.endYear()).data();
                double count = history.stream().mapToDouble(Double::doubleValue).sum();

                if (count == 0) {
                    hypsList.remove(word);
                } else {
                    hypCounts.put(word, count);
                }
            }

            if (q.k() < hypsList.size()) {
                hypsList.sort(Comparator.comparingDouble(hypCounts::get).reversed());
                hypsList = hypsList.subList(0, q.k());
            }
        }

        Collections.sort(hypsList);
        return "[" + String.join(", ", hypsList) + "]";
    }
}
