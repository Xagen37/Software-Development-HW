package impls;

import apis.DrawingApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.max;

public class AdjMatrixGraph extends GraphImpl implements Factorizable{
    private final List<List<Boolean>> adjMatrix;

    public AdjMatrixGraph(DrawingApi api) {
        super(api);
        this.adjMatrix = new ArrayList<>();
    }

    @Override
    public void addEdge(int left, int right) {
        final int ceil = max(left, right);
        adaptMatrix(ceil);
        adjMatrix.get(left).set(right, Boolean.TRUE);
        adjMatrix.get(right).set(left, Boolean.TRUE);
    }

    private void adaptMatrix(int ceil) {
        if (adjMatrix.size() <= ceil) {
            final int currSize = adjMatrix.size();
            for (int i = 0; i < ceil - currSize + 1; i++) {
                adjMatrix.add(new ArrayList<>());
            }

            for (List<Boolean> row : adjMatrix) {
                final int thisSize = row.size();
                for (int j = 0; j < ceil - thisSize + 1; j++) {
                    row.add(Boolean.FALSE);
                }
            }
        }
    }

    @Override
    public void drawGraph() {
        for (int i = 0; i < maxNode; i++) {
            drawNode(i);
        }
        for (int i = 0; i < adjMatrix.size(); i++) {
            for (int j = i; j < adjMatrix.get(i).size(); j++)
            {
                if (adjMatrix.get(i).get(j)) {
                    drawEdge(i, j);
                }
            }
        }
        drawingApi.draw();
    }

    public static Function<DrawingApi, GraphImpl> getFactory() {
        return AdjMatrixGraph::new;
    }
}
