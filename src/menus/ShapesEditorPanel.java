package menus;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import shapes.Polygon;
import shapes.Polyline;
import shapes.Rectangle;
import shapes.Shape;


/**
 * @author Fernando Birra, 2014
 *
 */

public class ShapesEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// Vector to store all the Shapes in the document
	Vector<Shape> shapes;
	// Current shape under construction, if any
	Shape current;
	// Currently selected shape, if any
	Shape selected;
	
	ShapesEditorFrame frame;
	
	DetailsFrame details;
	protected boolean drag;
	protected Point scale, rotate;
	
	public ShapesEditorPanel(ShapesEditorFrame frame) {
		this.frame = frame;
		reset();
		setupUI();
	}
	
	public void startPolyline() {
		current = new Polyline(this);
		if(selected != null){
			selected.deselect();
			selected = null;
		}
		current.start();
	}
	
	public void stopPolyline() {
		current.stop();
		if(current.isComplete())
			shapes.add(current);
		current = null;
	}
	
	
	public void startPolygonline() {
		current = new Polygon(this);
		if(selected != null){
			selected.deselect();
			selected = null;
		}
		current.start();
	}

	public void stopPolygonline() {
		current.stop();
		if(current.isComplete())
			shapes.add(current);
		current = null;
	}
	
	
	public void startRectangle() {
		current = new Rectangle(this);
		if(selected != null){
			selected.deselect();
			selected = null;
		}
		current.start();		
	}

	public void stopRectangle() {
		current.stop();
		if(current.isComplete())
			shapes.add(current);
		current = null;
	}
	
	public void showDetails() {
		details.setVisible(true);
	}
	
	public void hideDetails() {
		details.setVisible(false);
	}
	
	private void setupUI() {
		
		details = new DetailsFrame(selected, this);
		
		MouseInputListener ml = new MouseInputAdapter() {
			public void mouseClicked(MouseEvent e) {
				// if there is a shape being built then route event to it
				if(current != null) {
					current.click(e);
				} else {
					// No shape is being built. Try each shape in turn to see who grabs the event
					update(getGraphics());
					Shape oldSelection = selected;
					selected = null;
					for(Shape s: shapes) {
						if(s.click(e)) {
							selected = s;
							break;
						}
					}
					if(selected!=oldSelection) {
						if(oldSelection != null){
							oldSelection.deselect();
						}
						
						if(selected != null) {
							selected.select();
						}
						details.setSelectedShape(selected);
					}
				}
			}
			
			public void mousePressed (MouseEvent e){
				if(selected != null && selected.getBoundingBox().frontierHit(e.getX(), e.getY())){
					scale = e.getPoint();
				} else if (selected != null && !selected.getBoundingBox().insideHit(e.getX(), e.getY())){
					rotate = e.getPoint();
				} else if(selected != null && selected.getBoundingBox().insideHit(e.getX(), e.getY())){
					drag = true;
				}
			}

			public void mouseReleased (MouseEvent e){
				if(selected != null){
					drag = false;
					scale = null;
					rotate = null;
					update(getGraphics());
					selected.drawBoundingBox();
				}
			}
			
			public void mouseMoved(MouseEvent e) {
				if(current != null) {
					current.move(e);
				}
			}
			
			public void mouseDragged (MouseEvent e){
				if(selected != null){
					if(scale != null){
						selected.getBoundingBox().changeScale(scale.getX(), scale.getY(), e.getX(), e.getY());
						scale = e.getPoint();
					} else if(rotate != null) {
						selected.getBoundingBox().changeRotation(rotate.getX(), rotate.getY(), e.getX(), e.getY());
						rotate = e.getPoint();
					} else if(drag){
						selected.getBoundingBox().changeLocation(e.getX(), e.getY());
					}
					repaint();
				}
			}
		};
		
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}
	
	public void reset() {
		shapes = new Vector<Shape>();
		current = null; selected = null;
		rotate = null; scale = null; drag = false;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(Shape s: shapes) {
			s.draw(g);
		}
		if(current!=null) current.draw(g);
	}

	public void desselectButtons() {
		frame.desselectAll();		
	}
}
