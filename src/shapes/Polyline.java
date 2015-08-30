package shapes;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

import menus.ShapesEditorPanel;
import boundingBox.BoundingBoxClass;

public class Polyline extends ShapeClass {

	public Polyline(ShapesEditorPanel p) {
		super(p);
		points = new Vector<Point2D.Double>();
		isClosed = false;
		parent = p;
	}
		
	public boolean click(MouseEvent e) {
		Point p = e.getPoint();
		if(isBeingBuilt) {
			if(points.size() == 0)
				drawBox(g, p.x, p.y);
			else 
				drawSegment(g, points.lastElement().getX(), points.lastElement().getY(), p.getX(), p.getY());
			points.add(lastMove = new Point2D.Double(p.getX(), p.getY()));
			return true;
		} else if(this.pointHit(p.x, p.y) != -1) {
			super.draw(g);
			return true;
		}
		return false;
	}
	
	public boolean move(MouseEvent e){
		Point p = e.getPoint();
		if(isBeingBuilt && lastMove != null && points.size()>0){
			Point2D.Double lastPoint = points.lastElement();
			this.previewSegment(g, lastPoint.x, lastPoint.y, p.getX(), p.getY(), lastMove.x, lastMove.y);
			lastMove = new Point2D.Double(p.getX(), p.getY());
			return true;
		}			
		return false;
	}
	
	@Override
	public boolean isComplete() {
		return points.size() >= 3;
	}

	
	private void drawPoints(Graphics g) {
		Point2D.Double p1 = points.firstElement();
		for(Point2D.Double p2: points){
			drawSegment(g, p1.x, p1.y, p2.x, p2.y);
			drawBox(g, (int)p1.getX(), (int)p1.getY());
			p1 = p2;
		}
	}

	
	@Override
	public void draw(Graphics g) {
		drawPoints(g);
	}	
	
	@Override
	public void stop() {
		if(isComplete()){
			Point2D.Double lastPoint = points.lastElement();
			previewSegment(g, lastPoint.x, lastPoint.y, lastMove.x, lastMove.y, lastPoint.x, lastPoint.y);
		}
			bb = new BoundingBoxClass(parent, getMinX(), getMinY(), getMaxX(), getMaxY(), this);
			super.stop();
	}
}
