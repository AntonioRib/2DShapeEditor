package shapes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import menus.ShapesEditorPanel;
import boundingBox.BoundingBox;


/**
 * @author Fernando Birra, 2014
 *
 * Shape: this interface contains all the methods that all shapes MUST 
 * support to be able to interact with the application panel.
 */

public interface Shape {
	
	public static final int RADIUS = 5;
	
	public static final int INITIALLINETHICKNESS = 1;
	public static final int MINLINETHICKNESS = 1;
	public static final int MAXLINETHICKNESS = 10;
	public static final int CATMULLROMMINDIVISIONS = 0;
	public static final int CATMULLROMMAXDIVISIONS = 1000;
	
	public static final Color VERTEXINITIALCOLOR = Color.GREEN;
	public static final Color LINEINITIALCOLOR = Color.BLACK;
	public static final Color PREVIEWINITIALCOLOR = Color.ORANGE;
	public static final Color BOUNDINGBOXCOLOR = Color.RED;
	public static final Color CATMULLROMCOLOR = Color.ORANGE;
	
	public void draw(Graphics g);	// Used to draw the shape on screen
	
	public void start();			// Used to start the shape creation process
	public void stop();				// Used to signal that the shape construction was interrupted
	public boolean isComplete();	// Used to query if the shape is complete
	
	public boolean click(MouseEvent e);	// Handle mouse click events. 
										// Return true if event was processed by shape. 
										// Return false to instruct the panel to try to deliver the event to other shapes.
	public boolean move(MouseEvent e);	// Handle mouse move events. 
										// Return true if event was processed by shape. 
										// Return false to instruct the panel to try to deliver the event to other shapes.
	
	public void select();			// Make the shape the current selection
	public void deselect();			// Tell the shape that it is no longer the selected shape in the application

	public void drawBoundingBox();
	public void drawCatmullRomCurves(int subdivisions);
	
	public int getNumberOfPoints();
	public double getMaxX();
	public double getMaxY();
	public double getMinX();
	public double getMinY();
	public void changeLocation(double dx, double dy);
	public void changeScale(double scale);
	public void changeRotation(double acos);
	public int[] xPointsList();
	public int[] yPointsList();
	public int pointHit(int x, int y);
	
	public Point2D.Double getLastMove();
	public boolean isBeingBuilt();
	public boolean isSelected();
	public boolean isClosed();
	public ShapesEditorPanel getParent();
	public Color getVertexColor();
	public Color getLineColor();
	public Color getPreviewColor();
	public Color getFillColor();
	public int getLineThickness();
	public BoundingBox getBoundingBox();
	public double[][] getMatrix();
	
	public void setLastMove(Point2D.Double lastMove);
	public void setVertexColor(Color vertexColor);
	public void setLineColor(Color lineColor);
	public void setPreviewColor(Color previewColor);
	public void setFillColor(Color fillColor);
	public void setLineThickness(int lineThickness);
	
}
