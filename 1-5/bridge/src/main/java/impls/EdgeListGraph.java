package impls;

import apis.DrawingApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EdgeListGraph extends GraphImpl implements Factorizable {
    private static class Edge {
        public final int left;
        public final int right;

        private Edge(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    private final List<Edge> edgeList;

    public EdgeListGraph(DrawingApi api) {
        super(api);
        edgeList = new ArrayList<>();
    }

    @Override
    public void addEdge(int left, int right) {
        if (right < left) {
            edgeList.add(new Edge(right, left));
        } else {
            edgeList.add(new Edge(left, right));
        }
    }

    @Override
    public void drawGraph() {
        for (int i = 0; i < maxNode; i++) {
            drawNode(i);
        }
        for (Edge e: edgeList) {
            drawEdge(e.left, e.right);
        }
        drawingApi.draw();
    }

    public static Function<DrawingApi, GraphImpl> getFactory() {
        return EdgeListGraph::new;
    }
}
