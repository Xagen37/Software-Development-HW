import apis.AWTDrawer;
import apis.Drawer;
import apis.DrawingApi;
import apis.JavaFXDrawer;
import impls.AdjMatrixGraph;
import impls.EdgeListGraph;
import impls.GraphImpl;

import java.util.function.Function;

public class Main {
    final static int HEIGHT = 640;
    final static int WIDTH = 480;
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            printUsage();
            return;
        }

        final String api  = args[0];
        final String impl = args[1];

        Function<DrawingApi, GraphImpl> graph;
        switch (impl) {
            case "edges":
                graph = EdgeListGraph.getFactory();
                break;
            case "matrix":
                graph = AdjMatrixGraph.getFactory();
                break;
            default:
                printUsage();
                return;
        }

        Drawer drawer;
        switch (api) {
            case "awt":
                drawer = new AWTDrawer(graph, HEIGHT, WIDTH);
                break;
            case "javafx":
                drawer = new JavaFXDrawer(graph, HEIGHT, WIDTH);
                break;
            default:
                printUsage();
                return;
        }
        drawer.run();
    }

    private static void printUsage() {
        System.out.println("Expected two arguments: {awt|javafx} {edges|matrix}");
    }
}
