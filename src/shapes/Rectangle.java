package shapes;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import menus.ShapesEditorPanel;
import boundingBox.BoundingBoxClass;

public class Rectangle extends Polygon {

	private double lastProj;
	
	public Rectangle(ShapesEditorPanel p) {
		super(p);
	}
	
	@Override
	public boolean click(MouseEvent e) {
		Point p = e.getPoint();
		if(isBeingBuilt) {
			if(points.size() == 0){
				points.add(lastMove = new Point2D.Double(p.getX(), p.getY()));
				drawBox(g, p.x, p.y);
			} else if (points.size() == 1){
				Point2D.Double v1 = points.firstElement();
				points.add(lastMove = new Point2D.Double(p.getX(), p.getY()));
				drawSegment(g, v1.x, v1.y, p.x, p.y);
			} else if(points.lastElement().x != p.getX() || points.lastElement().y != p.getY()) {
				Point2D.Double p1 = points.firstElement();
				Point2D.Double p2 = points.lastElement();
				double proj;
				double ux = perpendicularUnitaryVectorX(p1.x, p1.y, p2.x, p2.y);
				double uy = perpendicularUnitaryVectorY(p1.x, p1.y, p2.x, p2.y);
				proj = calculateProjection(p2.x, p2.y, p.getX(), p.getY(), ux, uy, p1.distance(p2), e.isShiftDown());
				points.add(new Point2D.Double(p2.x+ux*proj,p2.y+uy*proj));
				points.add(new Point2D.Double(p1.x+ux*proj,p1.y+uy*proj));
				super.draw(g);
				this.stop();
			} 
			return true;
		} else if(this.pointHit(p.x, p.y) != -1) {
			drawBoundingBox();
			return true;
		}
		return false;
	}
	
	public boolean move(MouseEvent e) {
		Point p = e.getPoint();
		if(isBeingBuilt){
			if (points.size() == 1){
				Point2D.Double p1 = points.firstElement();
				previewSegment(g, p1.x, p1.y, p.x, p.y, lastMove.x, lastMove.y);
				lastMove = new Point2D.Double(p.x,p.y);
				return true;
			} else if(points.size() == 2) {
				Point2D.Double p1 = points.firstElement();
				Point2D.Double p2 = points.lastElement();
				double ux = perpendicularUnitaryVectorX(p1.x, p1.y, p2.x, p2.y);
				double uy = perpendicularUnitaryVectorY(p1.x, p1.y, p2.x, p2.y);
				double proj =  this.calculateProjection(p2.x, p2.y, p.x, p.y, ux, uy, distance(p1.x, p1.y, p2.x, p2.y), e.isShiftDown());
				Point2D.Double fp3 = (new Point2D.Double(p2.x+ux*lastProj, p2.y+uy*lastProj));
				Point2D.Double fp4 = (new Point2D.Double(p1.x+ux*lastProj, p1.y+uy*lastProj));
				Point2D.Double np3 = (new Point2D.Double(p2.x+ux*proj,p2.y+uy*proj));
				Point2D.Double np4 = (new Point2D.Double(p1.x+ux*proj, p1.y+uy*proj));
				previewSquare(p1, p2, fp3, fp4, np3, np4);
				lastMove = new Point2D.Double(p.x,p.y);
				lastProj = proj;
				return true;
			} 
		}
		return false;
	}
	
	private void previewSquare(Point2D.Double p1, Point2D.Double p2, Point2D.Double fp3, Point2D.Double fp4, Point2D.Double np3, Point2D.Double np4){
		drawSegment(g, p1.x, p1.y, p2.x, p2.y);
		previewSegment(g, p2.x, p2.y, fp3.x, fp3.y, np3.x, np3.y);
		previewSegment(g, p1.x, p1.y, fp4.x, fp4.y, np4.x, np4.y);
		g.setXORMode(previewColor);
		g.drawLine((int)fp3.x, (int)fp3.y, (int)fp4.x, (int)fp4.y);
		g.drawLine((int)np3.x, (int)np3.y, (int)np4.x, (int)np4.y);
		g.setPaintMode();
	}
	
	
	private double perpendicularUnitaryVectorX(double x1, double y1, double x2, double y2){
		double v1x = x1 - x2;
		double v1y = y1 - y2;
		double v1d = Math.sqrt(v1x*v1x+v1y*v1y);
		return -v1y/v1d;
	}
	
	private double perpendicularUnitaryVectorY(double x1, double y1, double x2, double y2){
		double v1x = x1 - x2;
		double v1y = y1 - y2;
		double v1d = Math.sqrt(v1x*v1x+v1y*v1y);
		return v1x/v1d;
	}
	
	private double calculateProjection(double x1, double y1, double x2, double y2, double ux, double uy, double distance, boolean square){
		double v2x = x2 - x1;
		double v2y = y2 - y1;
		double pEsc = ux*v2x+uy*v2y;
		double xM = Math.sqrt(ux*ux+uy*uy);
		double yM = Math.sqrt(v2x*v2x+v2y*v2y);
		double d = distance(x1, y1, x2, y2);
		double angle = Math.acos(pEsc/(xM*yM));
		
		if(square && Math.cos(angle)>0)
			return distance;
		else if (square && Math.cos(angle)<=0)
			return -distance;
		return d*Math.cos(angle);
		
	}
	
	@Override
	public boolean isComplete() {
		return  points.size() >= 4;
	}
	
	
	@Override
	public void stop() {
		bb = new BoundingBoxClass(parent, getMinX(), getMinY(), getMaxX(), getMaxY(), this);
		isBeingBuilt = false;
		parent.desselectButtons();
	}
	
}
