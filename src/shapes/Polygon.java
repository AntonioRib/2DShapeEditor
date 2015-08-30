package shapes;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import menus.ShapesEditorPanel;

public class Polygon extends Polyline {
	
	public Polygon(ShapesEditorPanel p) {
		super(p);
		fillColor = null;
		isClosed = true;
	}
	
	@Override
	public void draw(Graphics g) {
		fillShape(g);
		super.draw(g);
		Point2D.Double firstPoint = points.firstElement();
		Point2D.Double lastPoint = points.lastElement();
		drawSegment(g, firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y);
	}
	
	@Override
	public void stop() {
		super.stop();
		if(isComplete()){
			Point2D.Double firstPoint = points.firstElement();
			Point2D.Double lastPoint = points.lastElement();
			drawSegment(parent.getGraphics(), firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y);
		}
	}
	
	protected void fillShape(Graphics g){
		if(fillColor == null)
			return;
		g.setColor(fillColor);
		g.fillPolygon(xPointsList(), yPointsList(), getNumberOfPoints());
	}
}
