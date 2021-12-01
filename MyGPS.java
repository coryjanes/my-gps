import java.io.FileInputStream;
import java.util.Scanner;
import java.util.Stack;

public class MyGPS {
    private double[] distTo;
    private DirectedEdge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public MyGPS(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    private boolean check(EdgeWeightedDigraph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 1 and " + V);
    }


    public static void main(String[] args) throws Exception {
        boolean again = true;
        Scanner in = new Scanner(System.in);
        if (args[0] == null) throw new IllegalArgumentException("file argument is null");
        FileInputStream inputFilePath = new FileInputStream(args[0]);
        if (args[1] == null) throw new IllegalArgumentException("file argument is null");
        FileInputStream inputFileCoordinates = new FileInputStream(args[1]);
        // int s = Integer.parseInt(args[1]) - 1;
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(inputFilePath);
        CoordinateRead C = new CoordinateRead(inputFileCoordinates);

        // Draw
        /*for (int i = 0; i < C.length(); i++) {
            Coordinate tempCoor = C.getLongLat(i);
            StdDraw.circle(Math.abs(tempCoor.getLatitude() / 1000000), tempCoor.getLongtitude() / 1000000, 0.1);
        }*/
        /*double xScale = 1 / (C.getMaxLat() - C.getMinLat() + 0.001);
        double yScale = 1 / (C.getMaxLong() - C.getMinLong() + 0.001);

        for (int i = 0; i < C.length(); i++) {
            double tempDrawY = C.scaleDownY(i, yScale);
            double tempDrawX = C.scaleDownX(i, xScale);
            StdDraw.circle(tempDrawX, tempDrawY, 0.0005);
        }*/

        // Interface


        while (again) {
            //Route Menu
            System.out.println("----------------------------------------------------------");
            System.out.println("|          Welcome to Route Runner 2020!!!               |");
            System.out.println("----------------------------------------------------------");
            System.out.println("| *** The current graph has vertices from " + 1 + " to " + G.V() + ". *** |");
            System.out.println("---------------------------------------------------------|");
            System.out.println("  Would you like to:");
            System.out.println("     1. Find a new route");
            System.out.println("     2. Exit");
            System.out.print(": ");
            char input = in.next().charAt(0);  //User choice
            try {
                if (input == '1') {
                    try {
                        System.out.print("Please enter source vertex: ");
                        int source = Integer.parseInt(in.next()) - 1;
                        if (!G.validateV(source)) {
                            System.out.println("vertex " + (source + 1) + " is not between 1 and " + G.V());
                            continue;
                        }
                        System.out.print("PLease enter destination vertex: ");
                        int dest = Integer.parseInt(in.next()) - 1;
                        if (!G.validateV(dest)) {
                            System.out.println("vertex " + (dest + 1) + " is not between 1 and " + G.V());
                            continue;
                        }

                        // Process
                        long start = System.currentTimeMillis();
                        MyGPS sp = new MyGPS(G, source);

                        if (sp.hasPathTo(dest)) {

                            // Draw:
/*                            StdDraw.circle(0.1, 0.1, 0.1);
                            StdDraw.line(0.2, 0.1, 0.3, 0.1);
                            StdDraw.line(0.3, 0.1, 0.25, 0.15);
                            StdDraw.line(0.3, 0.1, 0.25, 0.05);
                            StdDraw.circle(0.4, 0.1, 0.1);*/

                            // Draw Line

                            double xScale;
                            double yScale;
                            long minLat = Long.MAX_VALUE, minLong = Long.MAX_VALUE;
                            long maxLat = Long.MIN_VALUE, maxLong = Long.MIN_VALUE;

                            for (DirectedEdge e : sp.pathTo(dest)) {
                                long tempLat = C.getLongLat(e.from()).getLatitude();
                                long tempLong = C.getLongLat(e.from()).getLongtitude();

                                if (tempLat < minLat) {
                                    minLat = tempLat;
                                }
                                if (tempLat > maxLat) {
                                    maxLat = tempLat;
                                }
                                if (tempLong < minLong) {
                                    minLong = tempLong;
                                }
                                if (tempLong > maxLong) {
                                    maxLong = tempLong;
                                }
                            }

                            long tempLat = C.getLongLat(dest).getLatitude();
                            long tempLong = C.getLongLat(dest).getLongtitude();

                            if (tempLat < minLat) {
                                minLat = tempLat;
                            }
                            if (tempLat > maxLat) {
                                maxLat = tempLat;
                            }
                            if (tempLong < minLong) {
                                minLong = tempLong;
                            }
                            if (tempLong > maxLong) {
                                maxLong = tempLong;
                            }

                            xScale = 1 / (maxLat - minLat + 0.001);
                            yScale = 1 / (maxLong - minLong + 0.001);

                            for (DirectedEdge e : sp.pathTo(dest)) {
                                StdDraw.setPenColor(StdDraw.BLACK);
                                double tempDrawYfrom = C.scaleDownY(e.from(), minLong, yScale);
                                double tempDrawXfrom = C.scaleDownX(e.from(), minLat, xScale);
                                StdDraw.circle(tempDrawXfrom, tempDrawYfrom, 0.0005);
                                double tempDrawYto = C.scaleDownY(dest, minLong, yScale);
                                double tempDrawXto = C.scaleDownX(dest, minLat, xScale);
                                StdDraw.circle(tempDrawXto, tempDrawYto, 0.0005);
                                StdDraw.setPenColor(StdDraw.MAGENTA);
                                StdDraw.line(C.scaleDownX(e.from(), minLat, xScale), C.scaleDownY(e.from(), minLong, yScale), C.scaleDownX(dest, minLat, xScale), C.scaleDownY(dest, minLong, yScale));
                            }


                            // Print
                            System.out.printf("\n%d to %d (%.2f)  ", source + 1, dest + 1, sp.distTo(dest));
                            for (DirectedEdge e : sp.pathTo(dest)) {
                                System.out.print(e + "   ");
                            }
                            System.out.println();
                        } else {
                            System.out.printf("%d to %d         no path\n", source, dest);
                        }

                        long stop = System.currentTimeMillis();
                        System.out.println("Process time: " + (stop - start) / 1000 + " seconds");

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered. Returning to menu.");
                    }
                } else if (input == '2') {
                    System.out.println("~~ Goodbye! ~~");
                    again = false;
                    break;
                } else {
                    System.out.println("Please enter a '1' or '2': ");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number entered. Returning to menu.");
            }

        }



/*        MyGPS sp = new MyGPS(G, s);
        // compute shortest paths


        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {

                System.out.printf("%d to %d (%.2f)  ", s + 1, t + 1, sp.distTo(t));
                for (DirectedEdge e : sp.pathTo(t)) {
                    System.out.print(e + "   ");
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d         no path\n", s, t);
            }
        }*/


        //while (running) {

    /*        for (int t = 0; t < G.V(); t++) {
                if (sp.hasPathTo(t)) {

                    System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                    for (DirectedEdge e : sp.pathTo(t)) {
                        System.out.print(e + "   ");
                    }
                    System.out.println();
                } else {
                    System.out.printf("%d to %d         no path\n", s, t);
                }
            }*/
        //}
/*        System.out.println();
        MyGPS sp = new MyGPS(G, s);
        The current graph has vertices from 1 to 2500.
        Would you like to:
        Find a new route
        Exit*/


    }
}
