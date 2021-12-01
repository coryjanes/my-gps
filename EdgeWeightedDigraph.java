import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class EdgeWeightedDigraph {
    private int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    private int[] indegree;

    public EdgeWeightedDigraph(FileInputStream in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            Scanner inputFile = new Scanner(in);
            boolean foundP = false;
            while (inputFile.hasNext()) {
                String[] curString = inputFile.nextLine().split(" ");
                /*or (int i = 0; i < curString.length; i++) {
                    System.out.print(curString[i] + i + " ");
                }*/
                //System.out.println();
                char type = curString[0].charAt(0);
                if ((type == 'p') && (!foundP)) {
                    foundP = true;
                    this.V = Integer.parseInt(curString[2]);
                    if (V < 0)
                        throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
                    indegree = new int[V];
                    adj = (Bag<DirectedEdge>[]) new Bag[V];
                    for (int v = 0; v < V; v++) {
                        adj[v] = new Bag<DirectedEdge>();
                    }
                    E = Integer.parseInt(curString[3]);
                    if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
                } else if (type == 'a') {
                    int v = Integer.parseInt(curString[1]) - 1;
                    int w = Integer.parseInt(curString[2]) - 1;
                    validateVertex(v);
                    validateVertex(w);
                    double weight = Double.parseDouble(curString[3]);
                    //System.out.println(v + "|" + w + "|" + weight);
                    DirectedEdge temp = new DirectedEdge(v, w, weight);
                    //System.out.println(temp.from() + "|" + temp.to() + "|" + temp.weight());
                    addEdge(temp);
                } else if ((type == 'p') && (foundP)) {
                    throw new IllegalArgumentException("Another vector and edges input found");
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            long seed = System.currentTimeMillis();
            Random random = new Random(seed);
            int v = random.nextInt(V);
            int w = random.nextInt(V);
            double weight = 0.01 * random.nextInt(100);
            ;
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
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

    public boolean validateV(int v) {
        return v >= 0 && v < V;
    }


    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }


    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }


    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }
}
