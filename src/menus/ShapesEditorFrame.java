package menus;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;


public class ShapesEditorFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ShapesEditorPanel panel;	// reference to the working area

	private ButtonGroup brectangle;

	
	public ShapesEditorFrame() {
		setupUI();
	}
	
	/**
	 * Used to setup the interface: build components and plug the listeners
	 */
	private void setupUI() {
		// Create two toolbars
		brectangle = new ButtonGroup();
		JToolBar toolbar1 = new JToolBar(); 
		JToolBar toolbar2 = new JToolBar(); 
		
		// Create the icons with the images for the buttons
		// Create the icons with the images for the buttons
		ImageIcon exitIcon = new ImageIcon("exit.png");
		ImageIcon newIcon = new ImageIcon("file_new.png");
		ImageIcon polyIcon = new ImageIcon("polyline.png");
		ImageIcon polygonIcon = new ImageIcon("polygon.png");
		ImageIcon editIcon = new ImageIcon("edit_property.png");
		ImageIcon rectangleIcon = new ImageIcon("rectangle.png");
		
		// Create the buttons to placed in the toolbars
		JButton exitButton = new JButton(exitIcon);
		JButton newButton = new JButton(newIcon);
		JToggleButton polyButton = new JToggleButton(polyIcon);
		JToggleButton polygonButton = new JToggleButton(polygonIcon);
		JToggleButton editButton = new JToggleButton(editIcon);
		JToggleButton rectangleButton = new JToggleButton(rectangleIcon);
		
		brectangle.add(rectangleButton);
	
		// Place each button in one of the toolbars
		toolbar1.add(exitButton);
		toolbar1.add(newButton);
		
		toolbar2.add(polyButton);
		toolbar2.add(polygonButton);
		toolbar2.add(rectangleButton);
		toolbar2.add(editButton);
		
		// Create a panel to store the application's toolbars
		JPanel toolbars = new JPanel();
		// Insert the toolbars into the toolbar's panel
		toolbars.add(toolbar1);
		toolbars.add(toolbar2);

		// Setup the listener for the exit button
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Setup the listener for the NEW button
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.reset();
				panel.repaint();
			}
		});
		
		// Setup the listener for the new Polyline button
		polyButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
					panel.startPolyline();
				else panel.stopPolyline();
			}			
		});
		
		polygonButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					panel.startPolygonline();
				} else { 
					panel.stopPolygonline();
				}
			}
		});
		
		rectangleButton.addItemListener(new ItemListener() {			
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					panel.startRectangle();
				} else if((e.getStateChange() == ItemEvent.DESELECTED)) {
					panel.stopRectangle();
				}
			}
		});
		
		editButton.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					panel.showDetails();	
				} else {
					panel.hideDetails();
				}
			}
		});
		

		// Add the toolbars' panel to the top of the frame
		add(toolbars, BorderLayout.NORTH);
		
		// Create the panel that will constitute the working  area for the application
		panel= new ShapesEditorPanel(this);
		
		// Set its background color to white
		panel.setBackground(Color.WHITE);
		
		// Put the panel in the center area of the frame 
		// (it will extend to all the non-occupied areas too: left, right and bottom).
		add(panel, BorderLayout.CENTER);		
	
	}

	public void desselectAll(){
		brectangle.clearSelection();
	}
}
