package menus;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class DetailsColorChooser extends JColorChooser{

	private static final long serialVersionUID = 1L;

	public DetailsColorChooser(Color actualColor) {
		super(actualColor);
		setPreviewPanel(new JPanel());
		AbstractColorChooserPanel formerPanels[] = getChooserPanels();
		AbstractColorChooserPanel panels[] = new AbstractColorChooserPanel[1];
		for(AbstractColorChooserPanel p : formerPanels){
			if(p.getDisplayName().equals("Swatches")){
				panels[0] = p;
			}
		}
		setChooserPanels(panels);
	}
}
