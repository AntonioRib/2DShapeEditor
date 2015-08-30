package shapes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

import menus.ShapesEditorPanel;
import boundingBox.BoundingBox;
import boundingBox.BoundingBoxClass;


/**
 * @author fpb
 *
 * ShapeClass: 
 * 		This abstract class provides the basic functionality that all shapes will inherit.
 * 		Specific functionality (for a certain type of shape) should be overridden in subclasses.
 */

public abstract class ShapeClass implements Shape {
	protected Vector<Point2D.Double> points;	// Used to store polyline's vertices
	protected Graphics g;
	
	protected Point2D.Double lastMove;
	
	protected boolean isBeingBuilt;			// is this shape still in construction?
	protected boolean selected;				// is this the selected shape?
	protected boolean isClosed;
	
	protected ShapesEditorPanel parent;		// reference to the application main panel
	
	protected Color vertexColor;
	protected Color lineColor;
	protected Color previewColor;
	protected Color fillColor;
	
	protected int lineThickness;
	
	protected BoundingBox bb;
	
	public ShapeClass(ShapesEditorPanel parent) {
		this.parent = parent;
		this.g = parent.getGraphics();
		this.lastMove = null;
		this.vertexColor = VERTEXINITIALCOLOR;
		this.lineColor = LINEINITIALCOLOR;
		this.previewColor = PREVIEWINITIALCOLOR;
		this.fillColor = null;
		this.lineThickness = INITIALLINETHICKNESS;
	}
	
	public void start() {
		isBeingBuilt = true;
	}

	public void stop() {
		isBeingBuilt = false;
	}
	
	public void select() {
		selected = true;
	}
	
	public void deselect() {
		selected = false;
	}
	
	public boolean click(MouseEvent e) {
		return false;
	}

	public boolean move(MouseEvent e) {
		return false;
	}
	
	@Override
	public void draw(Graphics g) {
		drawBoundingBox();
	}
	
	public void drawBoundingBox(){
		bb = new BoundingBoxClass(parent, getMinX(), getMinY(), getMaxX(), getMaxY(), this);
		bb.draw(g);
	}
	
	/*
	 * --- UTILITIES ---
	 */
	
	protected void drawBox(Graphics g, int x, int y){
		g.setColor(vertexColor);
		g.fillRect(x-3, y-3, 7, 7);
	}

	
	protected void drawSegment(Graphics g, double x1, double y1, double x2, double y2){
		g.setColor(lineColor);
		((Graphics2D) g).setStroke(new BasicStroke(lineThickness));
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		this.drawBox(g, (int)x2, (int)y2);
		this.drawBox(g, (int)x1, (int)y1);
		((Graphics2D) g).setStroke(new BasicStroke(Shape.INITIALLINETHICKNESS));
	}
	
	protected void previewSegment(Graphics g, double px, double py, double fx, double fy, double nx, double ny){
		g.setXORMode(previewColor);
		g.drawLine((int)px, (int)py, (int)fx, (int)fy);
		g.drawLine((int)px, (int)py, (int)nx, (int)ny);
		g.setPaintMode();
	}
	
	protected void previewSegment(Graphics g, double px, double py, double fx, double fy){
		g.setXORMode(previewColor);
		g.drawLine((int)px, (int)py, (int)fx, (int)fy);
		g.drawLine((int)px, (int)py, 0, 0);
		g.setPaintMode();
	}
	
	public int pointHit(int x, int y) {
		for(Point2D.Double p: points)
			if (p.distance(x,y) <= (Shape.RADIUS^2))
				return points.indexOf(p);
		return -1;
	}

	public static double distance(double x1, double y1, double x2, double y2){
		double dx = x2-x1;
		double dy = y2-y1;
		return Math.sqrt(dx*dx+dy*dy);
	}
	/*
	 * ----- GETTERS ------
	 */
	
	public Point2D.Double getLastMove() {
		return lastMove;
	}

	
	public boolean isBeingBuilt() {
		return isBeingBuilt;
	}

	
	public boolean isSelected() {
		return selected;
	}

	public boolean isClosed() {
		return isClosed;
	}
	
	public ShapesEditorPanel getParent() {
		return parent;
	}

	
	public Color getVertexColor() {
		return vertexColor;
	}
	
	public Color getLineColor() {
		return lineColor;
	}

	
	public Color getPreviewColor() {
		return previewColor;
	}
	
	public Color getFillColor(){
		return fillColor;
	}
	
	public int getLineThickness() {
		return lineThickness;
	}

	public int getNumberOfPoints(){
		return points.size();
	}
	
	public BoundingBox getBoundingBox(){
		return bb;
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

	public double getMaxX(){
		double max = Double.MIN_VALUE;
		for(Point2D.Double p: points)
			if(p.getX() > max)
				max = p.getX();
		return max;
	}

	public double getMaxY(){
		double max = Double.MIN_VALUE;
		for(Point2D.Double p: points)
			if(p.getY() > max)
				max = (int) p.getY();
		return max;
	}

	public double getMinX(){
		double min = Double.MAX_VALUE;
		for(Point2D.Double p: points)
			if(p.getX() < min)
				min = (int) p.getX();
		return min;
	}

	public double getMinY(){
		double min = Double.MAX_VALUE;
		for(Point2D.Double p: points)
			if(p.getY() < min)
				min = (int) p.getY();
		return min;
	}
	
	public void changeLocation(double x, double y) {
		for(Point2D.Double p: points){
			p.setLocation(p.x+x, p.y+y);
		}
	}
	
	public void changeScale(double scale){
		for(Point2D.Double p : points){
			p.setLocation(p.getX()*scale, p.getY()*scale);
		}
		enquadrar();
	}
	
	public void changeRotation(double angle){
		for(Point2D.Double p : points){
			double x = p.getX()*Math.cos(angle) - p.getY()*Math.sin(angle);
			double y = p.getX()*Math.sin(angle) + p.getY()*Math.cos(angle);
			p.setLocation(x, y);
		}
		enquadrar();
	}
	
	// Takes the old anchor, computes the new. 
	// Then moves shape to make the new anchor 
	// be on the same place as tha old.
	public void enquadrar(){
		Point2D.Double anchor = bb.getAnchorPoint();
		double midX = getMinX()+((getMaxX()-getMinX())/2);
		double midY = getMinY()+((getMaxY()-getMinY())/2);
		changeLocation(anchor.x - midX, anchor.y - midY);
	}
	
	public int[] xPointsList() {
		int[] v = new int[points.size()];
		int i = 0;
		for(Point2D.Double p : points){
			v[i++] = (int) p.x;
		}
		return v;
	}
	
	public int[] yPointsList() {
		int[] v = new int[points.size()];
		int i = 0;
		for(Point2D.Double p : points){
			v[i++] = (int) p.getY();
		}
		return v;
	}
	
	/*
	 * ----- SETTERS ------
	 */
	
	public void setLastMove(Point2D.Double lastMove) {
		this.lastMove = lastMove;
	}

	
	public void setVertexColor(Color vertexColor) {
		this.vertexColor = vertexColor;
	}

	
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	
	public void setPreviewColor(Color previewColor) {
		this.previewColor = previewColor;
	}

	public void setFillColor(Color fillColor){
		this.fillColor = fillColor;
	}
	
	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}
	
	/*
	 * --- CATMULL ROM --- 
	 */
	
	// Makes Point p-1 and Point size, adds them to a point vector, 
	// then calculates all values from subdivisions
	public Vector<Point2D.Double> interpolate(int subdivisions) {
		@SuppressWarnings("unchecked")
		Vector<Point2D.Double> points = (Vector<Point2D.Double>) this.points.clone();
	    if (isClosed()) {
	        points.add(0, points.get(points.size() - 2));
	        points.add(points.get(1));
	    } else {
	        double dx = points.get(1).getX() - points.firstElement().getX();
	        double dy = points.get(1).getY() - points.firstElement().getY();
	        Point2D.Double start = new Point2D.Double(points.firstElement().getX() - dx, points.firstElement().getY() - dy);
	        dx = points.lastElement().getX() - points.get(points.size() - 2).getX();
	        dy = points.lastElement().getY() - points.get(points.size() - 2).getY();
	        double x = points.lastElement().getX() + dx;
	        double y = points.lastElement().getY() + dy;
	        Point2D.Double end = new Point2D.Double(x, y);
	        points.add(0, start);
	        points.add(end);
	    }
	    Vector<Point2D.Double> result = new Vector<Point2D.Double>();
	    result.addAll(subdividePoints(points, subdivisions));
	    return result;
	}
	
	//subdivides all segments in a number of subdivisions, then return every point from that subdivision
	public Vector<Point2D.Double> subdividePoints(Vector<Point2D.Double> points, int subdivisions) {
		Vector<Point2D.Double> subdividedP = new Vector<Point2D.Double>(((points.size()-1) * subdivisions)+ 1);
        double increments = 1/(double)subdivisions;
        for (int i = 1; i < points.size()-2; i++) {
        	Point2D.Double p0 = points.get(i-1);
            Point2D.Double p1 = points.get(i);
            Point2D.Double p2 = points.get(i+1);
            Point2D.Double p3 = points.get(i+2);
            for (int j = 0; j <= subdivisions; j++) {
                subdividedP.add(((i-1)*subdivisions)+j, 
                		new Point2D.Double(catmullRomFunction(((double)j)*increments, p0.x, p1.x, p2.x, p3.x), 
                				catmullRomFunction(j*increments, p0.y, p1.y, p2.y, p3.y)));
            }
        }
        if(isClosed()){
        	Point2D.Double p0 = points.get(points.size()-3);
            Point2D.Double p1 = points.get(points.size()-2);
            Point2D.Double p2 = points.get(points.size()-1);
            Point2D.Double p3 = points.get(points.size()-1);
            for (int j = 0; j <= subdivisions; j++) {
                subdividedP.add(((points.size()-3)*subdivisions)+j, 
                		new Point2D.Double(catmullRomFunction(((double)j)*increments, p0.x, p1.x, p2.x, p3.x), 
                				catmullRomFunction(j*increments, p0.y, p1.y, p2.y, p3.y)));
            }
        }

        return subdividedP;
    }

	// calculates the point
    public double catmullRomFunction(double t, double p0, double p1, double p2, double p3) {
        return 0.5d * ((2 * p1) +
                      (p2 - p0) * t +
                      (2*p0 - 5*p1 + 4*p2 - p3) * t * t +
                      (3*p1 -p0 - 3 * p2 + p3) * t * t * t);    
    }
    
    // draws every segment from the catmull rom spline with a number x of subdivisions
	public void drawCatmullRomCurves(int subdivisions){
		Vector<Point2D.Double> p = interpolate(subdivisions);
		g.setColor(CATMULLROMCOLOR);
		Point2D.Double p1 = p.firstElement();
		for(Point2D.Double p2: p){
			g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
			p1 = p2;
		}
	}
}
