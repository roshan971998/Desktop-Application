package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class DatabaseToolbar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton saveButton;
	private JButton refreshButton;
	private DatabaseToolbarListener toolbarListener;

	public DatabaseToolbar() {

		setFloatable(false);// in order to stop the toolbar floating
		// get rid of border if you want the ToolBar DraggAble.
		setBorder(BorderFactory.createEtchedBorder());

//		saveButton = new JButton("Save");
//		refreshButton = new JButton("Refresh");
		saveButton = new JButton();
		refreshButton = new JButton();
		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);

		String savePath = "/images/Save16.gif";
		saveButton.setIcon(createIcon(savePath));
		saveButton.setToolTipText("Save");

		String refreshPath = "/images/Refresh16.gif";
		refreshButton.setIcon(createIcon(refreshPath));
		refreshButton.setToolTipText("Refresh");

		// setLayout(new FlowLayout(FlowLayout.LEFT));
		add(saveButton);
		//addSeparator();
		add(refreshButton);

	}

	public void setToolbarListener(DatabaseToolbarListener toolbarListener) {
		this.toolbarListener = toolbarListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();
		if (clicked == saveButton && toolbarListener != null) {
			toolbarListener.saveEventoccured();
		} else if (clicked == refreshButton && toolbarListener != null) {
			toolbarListener.refreshEventoccured();
		}
	}

	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if (url == null) {
			System.err.println("Unable to laod Images " + path);
		}

		ImageIcon icon = new ImageIcon(url);

		return icon;

	}

}
