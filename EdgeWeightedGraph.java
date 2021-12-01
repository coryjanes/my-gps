import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(File file) {
        if (file == null) throw new IllegalArgumentException("file argument is null");
        try {
            Scanner inputFile = new Scanner(new FileInputStream(file));
            V = inputFile.nextInt();
            adj = (Bag<Edge>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Edge>();
            }

            int E = inputFile.nextInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = inputFile.nextInt();
                int w = inputFile.nextInt();
                validateVertex(v);
                validateVertex(w);
                double weight = inputFile.nextDouble();
                Edge e = new Edge(v, w, weight);
                addEdge(e);
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + file, ioe);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 1 and " + V);
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Bag<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }


}
