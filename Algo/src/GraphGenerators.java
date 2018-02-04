import java.util.Random;
import java.util.Vector;

class GraphGenerators {

    private static void shuffle(Vector<Integer> v) {
        Random random = new Random();
        int n = v.size();
        for(int i=0; i<n; ++i) {
            int diff = random.nextInt(n-i);
            int t = v.elementAt(i);
            v.setElementAt(v.elementAt(i+diff), i);
            v.setElementAt(t, i+diff);
        }
    }

    public static Data.Graph generateRandomConnectedGraph(int order, int size) {
        if(order<0 && size<order-1)
            throw new IllegalArgumentException("ERROR: ...invalid parameters");
        Data.Graph graph = new Data.Graph(order);
        Random random = new Random();
        for(int i=1;i<order;++i) {
            int v = random.nextInt(i);
            graph.addEdge(i,v);
        }
        while(graph.getSize() < size) {
            int v1 = random.nextInt(order);
            int v2 = random.nextInt(order);
            graph.addEdge(v1, v2);
        }

        return graph;
    }

    public static Data.Graph generateRandomHamiltonianGraph(int order, int size) {
        if(order<0 && size<order)
            throw new IllegalArgumentException("ERROR: ...invalid parameters");
        Data.Graph graph = new Data.Graph(order);
        Random random = new Random();
        Vector<Integer> v = new Vector<>();
        for(int i=0; i<order; ++i)
            v.addElement(i);
        shuffle(v);
        for(int i=0; i<order; ++i)
            graph.addEdge(v.elementAt(i), v.elementAt((i+1)%order));
        while(graph.getSize() < size) {
            int v1 = random.nextInt(order);
            int v2 = random.nextInt(order);
            graph.addEdge(v1, v2);
        }

        return graph;
    }
}