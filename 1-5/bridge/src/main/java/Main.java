import apis.AWTDrawer;
import apis.DrawingApi;
import apis.JavaFXDrawer;
import impls.AdjMatrixGraph;
import impls.EdgeListGraph;
import impls.GraphImpl;

import java.util.function.Function;

public class Main {
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

        GraphImpl createdGraph;
        switch (api) {
            case "awt":
                AWTDrawer AWTdrawer = new AWTDrawer(graph, 640, 480);
                AWTdrawer.run();
                break;
            case "javafx":
                JavaFXDrawer JFXdrawer = new JavaFXDrawer(graph, 640, 480);
                JFXdrawer.run();
                break;
            default:
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Expected two arguments: {awt|javafx} {edges|matrix}");
    }
}
