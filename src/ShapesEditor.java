
/**
 * @author Fernando Birra, 2014
 *
 */

import javax.swing.JFrame;

import menus.ShapesEditorFrame;


public class ShapesEditor {

	public static void main(String[] args) {
		JFrame frame = new ShapesEditorFrame();
		frame.setSize(800, 600);
		
		frame.setTitle("Shapes Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
