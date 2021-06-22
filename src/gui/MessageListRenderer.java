package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import model.Message;

/*
 *this demonstrates using an arbitrary component as list box renderer
 *Probably it is overkill in this process to use JPanel when jLabel could be used directly  
 */

public class MessageListRenderer implements ListCellRenderer {

	private JPanel panel;
	private JLabel label;

	private Color selectedColor;
	private Color normalColor;

	public MessageListRenderer() {
		panel = new JPanel();
		label = new JLabel();
		label.setFont(createFont("/fonts/CrimewaveBB.ttf").deriveFont(Font.PLAIN, 13));
		
		selectedColor = new Color(210, 210, 255);
		normalColor = Color.WHITE;

		label.setIcon(createIcon("/images/Information24.gif"));

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(label);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		Message message = (Message) value;
		label.setText(message.getTitle());

//		label.setBackground(cellHasFocus ? selectedColor :normalColor);
//		label.setOpaque(true);
		panel.setBackground(cellHasFocus ? selectedColor : normalColor);
		return panel;
	}

	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if (url == null) {
			System.err.println("Unable to laod Images " + path);
		}

		ImageIcon icon = new ImageIcon(url);

		return icon;

	}
	
	public  Font createFont(String path) {
		URL url = getClass().getResource(path);

		if (url == null) {
			System.err.println("Unable to load font: " + path);
		}

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
		} catch (FontFormatException e) {
			System.err.println("Bad format in font file: " + path);
		} catch (IOException e) {
			System.out.println("Unable to read font file: " + path);
		}

		return font;
	}
	
	

}
