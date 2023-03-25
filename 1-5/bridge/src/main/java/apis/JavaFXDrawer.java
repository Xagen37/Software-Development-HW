package apis;

import impls.GraphImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.function.Function;

public class JavaFXDrawer implements Drawer {
    public static class JavaFXApp extends Application {
        public static int height;
        public static int width;
        public static Function<DrawingApi, GraphImpl> graphCreate;
        public static GraphImpl graph;

        public static void run() {
            launch();
        }

        @Override
        public void start(Stage primaryStage) {
            DrawingApi drawingApi = new JavaFXApi(height, width, primaryStage);
            if (graph == null) {
                graph = graphCreate.apply(drawingApi);

                graph.addNodes(4);
                graph.addEdge(0, 1);
                graph.addEdge(1, 2);
                graph.addEdge(2, 3);
                graph.addEdge(3, 0);
            } else {
                graph.setApi(drawingApi);
            }
            graph.drawGraph();
        }
    }
    private final Function<DrawingApi, GraphImpl> graph;
    private final int height;
    private final int width;

    public JavaFXDrawer(Function<DrawingApi, GraphImpl> graph, int height, int width) {
        this.graph = graph;
        this.height = height;
        this.width = width;
    }

    public void run() {
        JavaFXApp.height = this.height;
        JavaFXApp.width  = this.width;
        JavaFXApp.graphCreate = this.graph;
        JavaFXApp.run();
    }
}
