package apis;

import impls.GraphImpl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

public class AWTDrawer extends Frame {
    private final Function<DrawingApi, GraphImpl> graphCreate;
    private GraphImpl graph = null;
    private final int height;
    private final int width;

    public AWTDrawer(Function<DrawingApi, GraphImpl> graph, int height, int width) {
        this.graphCreate = graph;
        this.height = height;
        this.width = width;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, width, height);

        // It probably should not be here, but whatever
        graph = graphCreate.apply(new AWTApi(height, width, g2d));

        graph.addNodes(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 0);


        graph.drawGraph();
    }

    public void run() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        this.setSize(width, height);
        this.setVisible(true);
    }
}
