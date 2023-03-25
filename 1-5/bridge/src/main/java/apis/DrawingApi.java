package apis;

public interface DrawingApi {
    class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class Circle {
        public final Point center;
        public final double r;

        public Circle(Point center, double r) {
            this.center = center;
            this.r = r;
        }

        public Circle(double x, double y, double r) {
            this.center = new Point(x, y);
            this.r = r;
        }
    }

    long getDrawingAreaWidth();
    long getDrawingAreaHeight();

    void drawCircle(Circle circle);
    void drawLine(Point begin, Point end);

    void draw();
}