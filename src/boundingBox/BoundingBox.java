package boundingBox;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import shapes.Shape;

public interface BoundingBox {

	public boolean insideHit(int x, int y);

	public boolean frontierHit(int x, int y);
	
	public boolean anchorPointHit(int x, int y);
	
	public void draw(Graphics g);

	public void changeLocation(double x, double y);
	
	public void changeScale(double d, double e, int x, int y);
	
	public void changeRotation(double x, double y, int i, int j);
	
	public Point2D.Double getMinPoint();

	public double getHeight();

	public double getWidth();

	public Shape getShape();

	public Point2D.Double getAnchorPoint();

	public void setHeight(double height);

	public void setWidth(double width);

	public void setAnchorPoint(Point2D.Double midPoint);
	
	public void setAnchorPoint(double x, double y);

}