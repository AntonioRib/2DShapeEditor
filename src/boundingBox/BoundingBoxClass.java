package boundingBox;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

import menus.ShapesEditorPanel;
import shapes.Shape;

public class BoundingBoxClass implements BoundingBox {
	
	private double height, width;
	private Shape shape;
	private Point2D.Double anchorPoint;
	protected Vector<Point2D.Double> points;	// Used to store polyline's vertices
	protected Graphics g;
	
	

	public BoundingBoxClass(ShapesEditorPanel parent, double minX, double minY, double maxX, double maxY, Shape s) {
		points = new Vector<Point2D.Double>(4);
		points.add(new Point2D.Double(minX, minY));
		points.add(new Point2D.Double(maxX, minY));
		points.add(new Point2D.Double(maxX, maxY));
		points.add(new Point2D.Double(minX, maxY));
		this.height = maxY-getMinPoint().y;
		this.width = maxX-getMinPoint().x;
		this.anchorPoint = new Point2D.Double((minX+maxX)/2, (minY+maxY)/2);
		this.shape = s;
	}
	
	public boolean insideHit(int x, int y){
		if(x > getMinPoint().x && x < getMinPoint().x+width && y > getMinPoint().y && y < getMinPoint().y+height)
			return true;
		return false;
	}

	public boolean frontierHit(int x, int y){
		if(x > getMinPoint().x-Shape.RADIUS && x < getMinPoint().x+Shape.RADIUS && y >= getMinPoint().y && y <= getMinPoint().y+height)
			return true;
		if(y > getMinPoint().y-Shape.RADIUS && y < getMinPoint().y+Shape.RADIUS &&  x >= getMinPoint().x && x <= getMinPoint().x+width)
			return true;
		if(x > getMinPoint().x+width-Shape.RADIUS && x < getMinPoint().x+width+Shape.RADIUS &&  y >= getMinPoint().y && y <= getMinPoint().y+height)
			return true;
		if(y > getMinPoint().y+height-Shape.RADIUS && y < getMinPoint().y+height+Shape.RADIUS &&  x >= getMinPoint().x && x <= getMinPoint().x+width)
			return true;
		return false;
	}

	@Override
	public boolean anchorPointHit(int x, int y) {
		if (getAnchorPoint().distance(x,y) <= (Shape.RADIUS^2))
			return true;
		return false;
	}
	
	public void changeLocation(double x, double y) {
		double dx, dy;
		dx = x-anchorPoint.x;
		dy = y-anchorPoint.y;
		anchorPoint.setLocation(x, y);
		shape.changeLocation(dx, dy);
	}
	
	public void changeScale(double sx, double sy, int fx, int fy){
		Point2D.Double anchor = getAnchorPoint();
		double dAnchor = anchor.distance(fx, fy);
		double dFront = anchor.distance(sx, sy);
		double d = dAnchor/dFront;
		shape.changeScale(d);
	}
	
	public void changeRotation(double fx, double fy, int sx, int sy) {
		double sa = sx - getAnchorPoint().getX();
		double so = sy - getAnchorPoint().getY() ;
		double fa = fx - getAnchorPoint().getX();
		double fo = fy - getAnchorPoint().getY();
		if(Math.atan(so/sa)>0 && Math.atan(fo/fa)<0 || Math.atan(so/sa)<0 && Math.atan(fo/fa)>0)
			return;
		double angle = Math.atan(so/sa)-Math.atan(fo/fa);
		shape.changeRotation(angle);
	}
	
	/*
	 * --- DRAWING ---
	 */

	@Override
	public void draw(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(Shape.MINLINETHICKNESS));
		Point2D.Double p1 = points.firstElement();
		for(Point2D.Double p2: points){
			drawBoundingBoxSegment(g, p1.x, p1.y, p2.x, p2.y);
			p1 = p2;
		}
		drawBoundingBoxSegment(g, p1.x, p1.y, points.firstElement().x, points.firstElement().y);
		createRectangles(g);
	}
	
	protected void drawBoundingBoxSegment(Graphics g, double x1, double y1, double x2, double y2){
		g.setColor(Shape.BOUNDINGBOXCOLOR);
		((Graphics2D) g).setStroke(new BasicStroke(Shape.MINLINETHICKNESS));
		g.drawRect((int)getMinPoint().x, (int)getMinPoint().y, (int)getWidth(), (int)getHeight());
	}
	
	private void createRectangle(Graphics g, double x1, double y1, double x2, double y2){
		g.fillRect((int)(x1+x2-3), (int)(y1+y2-3), 7, 7);
		g.clearRect((int)(x1+x2-2), (int)(y1+y2-2), 5, 5);
	}
	
	private void createRectangles(Graphics g){
		createRectangle(g, getMinPoint().x, getMinPoint().y, 0, 0);
		createRectangle(g, getMinPoint().x, getMinPoint().y, width/2, 0);
		createRectangle(g, getMinPoint().x, getMinPoint().y, width, 0);
		createRectangle(g, getMinPoint().x, getMinPoint().y, 0, height/2);
		createRectangle(g, getMinPoint().x, getMinPoint().y, width, height/2);
		createRectangle(g, getMinPoint().x, getMinPoint().y, 0, height);
		createRectangle(g, getMinPoint().x, getMinPoint().y, width/2, height);
		createRectangle(g, getMinPoint().x, getMinPoint().y, width, height);
		createRectangle(g, 0, 0, anchorPoint.x, anchorPoint.y);
	}
	
	/*
	 * --- GETTERS ---
	 */

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public Shape getShape() {
		return shape;
	}

	public Point2D.Double getAnchorPoint() {
		return anchorPoint;
	}
	
	public double[][] getMatrix(){
		double[][] matrix = new double[points.size()][2];
		int i = 0;
		for(Point2D.Double p : points){
			matrix[i][0] = p.x;
			matrix[i++][1] = p.y;
		}
		return matrix;
	}


	/*
	 * --- SETTERS ---
	 */
	
	public void setHeight(double height) {
		this.height = height;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setAnchorPoint(Point2D.Double midPoint) {
		if(midPoint.x < this.getMinPoint().x || midPoint.x > this.getMinPoint().x+getWidth())
			return;
		if(midPoint.y < this.getMinPoint().y || midPoint.y > this.getMinPoint().y+getHeight())
			return;
		this.anchorPoint = midPoint;
	}
	
	public void setAnchorPoint(double x, double y) {
		if(x < this.getMinPoint().x || x > this.getMinPoint().x+getWidth())
			return;
		if(y < this.getMinPoint().y || y > this.getMinPoint().y+getHeight())
			return;
		this.anchorPoint = new Point2D.Double(x,y);
	}

	public Double getMinPoint() {
		return points.firstElement();
	}
}
