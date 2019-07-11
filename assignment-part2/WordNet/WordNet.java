import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class WordNet {
    private final HashMap<String, LinkedList<Integer>> noun2ids;
    private final HashMap<Integer, String> id2synset;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        noun2ids = new HashMap<String, LinkedList<Integer>>();
        id2synset = new HashMap<Integer, String>();

        In in = new In(synsets);
        int id = 0;
        String line = in.readLine();
        for (; line != null; line = in.readLine()) {
            String[] items = line.split(",");
            id = Integer.parseInt(items[0]);
            String synset = items[1];
            String[] nouns = items[1].split(" ");

            for (int i = 0; i < nouns.length; i++) {
                LinkedList<Integer> ids;
                if (!noun2ids.containsKey(nouns[i]))
                    ids = new LinkedList<Integer>();
                else
                    ids = noun2ids.get(nouns[i]);
                ids.add(id);

                noun2ids.put(nouns[i], ids);
            }
            id2synset.put(id, synset);
        }
        in.close();

        in = new In(hypernyms);
        Digraph wordnet = new Digraph(id + 1);
        line = in.readLine();
        for (; line != null; line = in.readLine()) {
            String[] items = line.split(",");
            int v = Integer.parseInt(items[0]);
            for (int i = 1; i < items.length; i++) {
                wordnet.addEdge(v, Integer.parseInt(items[i]));
            }
        }
        in.close();

        if (!isValid(wordnet))
            throw new IllegalArgumentException();

        sap = new SAP(wordnet);
    }

    // check whether a digraph is a single rooted DAG
    private boolean isValid(Digraph G) {
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle())
            return false;

        int num = 0;
        for (int i = 0; i < G.V(); i++) {
            Iterator<Integer> iter = G.adj(i).iterator();
            if (!iter.hasNext())
                num++;
        }
        if (num != 1)
            return false;
        return true;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun2ids.keySet();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return noun2ids.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        LinkedList<Integer> idsA = noun2ids.get(nounA);
        LinkedList<Integer> idsB = noun2ids.get(nounB);
        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        LinkedList<Integer> idsA = noun2ids.get(nounA);
        LinkedList<Integer> idsB = noun2ids.get(nounB);
        int id = sap.ancestor(idsA, idsB);
        return id2synset.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test code
    }
}
