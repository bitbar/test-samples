package objects;

import org.opencv.core.Point;

public class ImageLocation {

    public ImageLocation(){
        this.scaleFactor=1;
        this.resizeFactor=1;
    }


    public Point getTopLeft() {
        return topLeft;
    }
    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public void setTopRight(Point topRight) {
        this.topRight = topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Point bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public void divideCoordinatesBy(int i) {
        this.scaleFactor=i;

        this.topLeft.x = this.topLeft.x/i;
        this.topLeft.y = this.topLeft.y/i;

        this.topRight.x = this.topRight.x/i;
        this.topRight.y = this.topRight.y/i;

        this.bottomLeft.x = this.bottomLeft.x/i;
        this.bottomLeft.y = this.bottomLeft.y/i;

        this.bottomRight.x = this.bottomRight.x/i;
        this.bottomRight.y = this.bottomRight.y/i;

        this.center.x = this.center.x/i;
        this.center.y = this.center.y/i;
    }

    public double getWidth(){
        return this.topRight.x-this.topLeft.x;
    }

    public double getHeight(){
        return this.bottomLeft.y-this.topLeft.y;
    }

    public int getScaleFactor(){
        return this.scaleFactor;
    }

    public void setResizeFactor(double resizeFactor) {
        this.resizeFactor=resizeFactor;
    }
    public double getResizeFactor(){
        return this.resizeFactor;
    }


    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;
    private Point center;
    private int scaleFactor;
    private double resizeFactor;
}
