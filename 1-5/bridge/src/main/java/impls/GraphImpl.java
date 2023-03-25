package impls;

import apis.DrawingApi;

import java.util.function.Function;

public abstract class GraphImpl {

    protected int maxNode = 0;
    protected DrawingApi.Point center;
    protected double radius;
    protected double nodeRadius = 10;

    /**
     * Bridge to drawing api
     */
    protected DrawingApi drawingApi;

    public GraphImpl(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
        center = new DrawingApi.Point(drawingApi.getDrawingAreaWidth() / 2., drawingApi.getDrawingAreaHeight() / 2.);
        radius = Math.min(center.x, center.y) / 8;
    }

    public int addNode() {
        return ++maxNode;
    }

    public int addNodes(int toAdd) {
        maxNode += toAdd;
        return maxNode;
    }

    public int getMaxNode() {
        return maxNode;
    }

    protected DrawingApi.Point getNodePos(int n) {
        final double rotate = (360. / maxNode) * n;
        final double phi = Math.toRadians(rotate);
        final double x = center.x * Math.cos(phi) + center.x;
        final double y = center.y * Math.sin(phi) + center.y;
        return new DrawingApi.Point(x, y);
    }

    protected void drawNode(int n) {
        drawingApi.drawCircle(new DrawingApi.Circle(getNodePos(n), nodeRadius));
    }

    public abstract void addEdge(int left, int right);

    protected void drawEdge(int from, int to) {
        drawingApi.drawLine(getNodePos(from), getNodePos(to));
    }

    public abstract void drawGraph();
}
