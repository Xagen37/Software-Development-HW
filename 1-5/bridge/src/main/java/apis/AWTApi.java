package apis;

import java.awt.*;

public class AWTApi implements DrawingApi {
    private final int height;
    private final int width;
    private final Graphics2D g2d;

    public AWTApi(int height, int width, Graphics2D g2d) {
        this.height = height;
        this.width = width;
        this.g2d = g2d;
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
        g2d.setColor(Color.BLACK);
        g2d.drawOval(
                (int) (center.x - circle.r / 2),
                (int) (center.y - circle.r / 2),
                (int) (circle.r),
                (int) (circle.r)
        );
    }

    @Override
    public void drawLine(Point begin, Point end) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine((int)begin.x, (int)begin.y, (int)end.x, (int)end.y);
    }

    @Override
    public void draw() {}
}
