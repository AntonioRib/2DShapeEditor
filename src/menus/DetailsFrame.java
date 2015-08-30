package menus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shapes.Polygon;
import shapes.Shape;

public class DetailsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Shape selectedShape;
	
	private ShapesEditorPanel parent;
	
	private Color c;

	public DetailsFrame(Shape s, ShapesEditorPanel parent) {
		super("Shape Details");
		this.parent = parent;
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.setSize(225, 325);
		this.setAlwaysOnTop(true);
		this.selectedShape = s;
		if(selectedShape == null)
			setupMessage();
		else
			setupUI();
	}

	private void setupMessage() {
		JLabel warning = new JLabel("No figure selected");
		this.getContentPane().removeAll();
		this.add(warning);
	}

	private void setupUI() {
		this.getContentPane().removeAll();
		final JLabel lineColorLabel = new JLabel("LineColor (RGB): " + selectedShape.getLineColor().getRed() + ", " 
				+ selectedShape.getLineColor().getGreen() + ", "
				 + selectedShape.getLineColor().getBlue());
		
		final JLabel fillColorLabel =  new JLabel("FillColor (RGB): None");
		
		final JLabel vertexColorLabel = new JLabel("VertexColor (RGB): " + selectedShape.getVertexColor().getRed() + ", " 
				+ selectedShape.getVertexColor().getGreen() + ", "
				 + selectedShape.getVertexColor().getBlue());
		
		final JLabel thicknessLabel = new JLabel("Line Thickness");
		final JLabel catmullromLabel = new JLabel("Catmull-Rom Subdivisions");
		
		final JSlider thicknessSlider = new JSlider(Shape.MINLINETHICKNESS, Shape.MAXLINETHICKNESS, selectedShape.getLineThickness());
		
		JButton changeLineColor = new JButton("Choose line color");
		JButton changeVertexColor = new JButton("Choose vertex colors");
		JButton changeFillColor = new JButton("Choose fill color");
		
		final JSlider catmullRomDivisionsSlider = new JSlider(0, 10, 0);
		this.add(changeLineColor);
		this.add(changeVertexColor);

		if(selectedShape.getFillColor() != null){
			fillColorLabel.setText("FillColor (RGB): " + selectedShape.getFillColor().getRed() + ", " 
					+ selectedShape.getFillColor().getGreen() + ", "
					+ selectedShape.getFillColor().getBlue());
		}
		
		final JCheckBox fillVisiblityCheckBox = new JCheckBox("Filled?", false);
		
		if(selectedShape instanceof Polygon){
			this.add(fillVisiblityCheckBox);
			this.add(changeFillColor);
			
		}
		
		this.add(thicknessLabel);
		this.add(thicknessSlider);
		this.add(catmullromLabel);
		this.add(catmullRomDivisionsSlider);
		this.add(lineColorLabel);
		this.add(vertexColorLabel);
		this.add(fillColorLabel);
		
		final JColorChooser jc =new DetailsColorChooser(selectedShape.getLineColor());
		final JDialog colorChooser = 	DetailsColorChooser.createDialog(getParent(), "Select Line Color", true, jc, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c = jc.getColor();
			}
		}, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		changeLineColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorChooser.setVisible(true);
						
				if (c != null)
					selectedShape.setLineColor(c);
				selectedShape.draw(parent.getGraphics());
			}
		});
		
		changeLineColor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e){ 
				lineColorLabel.setText("LineColor (RGB): " + selectedShape.getLineColor().getRed() + ", " 
						+ selectedShape.getLineColor().getGreen() + ", "
						 + selectedShape.getLineColor().getBlue());	
			}
		});
		
		changeVertexColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorChooser.setVisible(true);
				if (c != null)
					selectedShape.setVertexColor(c);
				selectedShape.draw(parent.getGraphics());
			}
		});
		
		changeVertexColor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e){ 
				vertexColorLabel.setText("VertexColor (RGB): " + selectedShape.getVertexColor().getRed() + ", " 
						+ selectedShape.getVertexColor().getGreen() + ", "
						 + selectedShape.getVertexColor().getBlue());
			}
		});
		
		changeFillColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorChooser.setVisible(true);
				if(fillVisiblityCheckBox.isSelected())
					selectedShape.setFillColor(c);
				else
					selectedShape.setFillColor(null);
				selectedShape.draw(parent.getGraphics());
			}
		});
		
		changeFillColor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e){ 
				if(selectedShape.getFillColor() != null)
					fillColorLabel.setText("FillColor (RGB): " + selectedShape.getFillColor().getRed() + ", " 
						+ selectedShape.getFillColor().getGreen() + ", "
						+ selectedShape.getFillColor().getBlue());
			}
		});
		
		fillVisiblityCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fillVisiblityCheckBox.isSelected()){
					selectedShape.setFillColor(selectedShape.getFillColor());
					selectedShape.draw(parent.getGraphics());
				} else {
				 	selectedShape.setFillColor(null);
				 	parent.repaint();
				}
			}
		});
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer(1), new JLabel("1") );
		labelTable.put( new Integer(5), new JLabel("5") );
		labelTable.put( new Integer(10), new JLabel("10") );
		
		thicknessSlider.setLabelTable(labelTable);
		thicknessSlider.setPaintLabels(true);
		thicknessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e){ 
				selectedShape.setLineThickness(thicknessSlider.getValue());
				parent.update(parent.getGraphics());
				selectedShape.drawBoundingBox();
			}
		});
		
		catmullRomDivisionsSlider.setLabelTable(labelTable);
		catmullRomDivisionsSlider.setPaintLabels(true);
		catmullRomDivisionsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e){ 
				parent.update(parent.getGraphics());
				selectedShape.drawCatmullRomCurves(catmullRomDivisionsSlider.getValue());
				selectedShape.drawBoundingBox();
				selectedShape.draw(parent.getGraphics());		
			}
		});
	}

	/*
	 * --- GETTERS ---
	 */
	
	public Shape getSelectedShape() {
		return selectedShape;
	}

	/*
	 * --- SETTERS ---
	 */
	
	public void setSelectedShape(Shape selectedShape) {
		this.selectedShape = selectedShape;
		if(selectedShape == null)
			setupMessage();
		else
			setupUI();
		refresh();
	}
	
	public void refresh() {
		this.invalidate();
		this.validate();
		this.repaint();
	}
}
