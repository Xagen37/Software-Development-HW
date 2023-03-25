package apis;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class JavaFXApi implements DrawingApi {
    private final int height;
    private final int width;
    private final Stage stage;
    private final Canvas canvas;
    private final GraphicsContext ctx;

    public JavaFXApi(int height, int width, Stage stage) {
        this.height = height;
        this.width = width;
        this.stage = stage;
        canvas = new Canvas(width, height);
        ctx = canvas.getGraphicsContext2D();
    }

    @Override
    public long getDrawingAreaWidth() {
        return width;
    }

    @Override
    public long getDrawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(Circle circle) {
        final Point center = circle.center;
        ctx.fillOval(
                center.x - circle.r / 2,
                center.y - circle.r / 2,
                circle.r,
                circle.r
        );
    }

    @Override
    public void drawLine(Point begin, Point end) {
        ctx.strokeLine(begin.x, begin.y, end.x, end.y);
    }

    @Override
    public void draw() {
        Group group = new Group();
        group.getChildren().add(canvas);
        stage.setScene(new Scene(group));
        stage.show();
    }
}
