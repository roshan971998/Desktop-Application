package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolBar extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton helloButton;
	private JButton goodbyeButton;

	private StringListener textListener;

	public ToolBar() {

		helloButton = new JButton("HELLO");
		goodbyeButton = new JButton("GOODBYE");
		helloButton.addActionListener(this);
		goodbyeButton.addActionListener(this);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(helloButton);
		add(goodbyeButton);

	}

	public void setStringListener(StringListener listener) {
		this.textListener = listener;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();
		if (clicked == helloButton && textListener != null)// textListener != null it means there is definitely someone
															// who is listening our events
			textListener.textPerformed("HELLO\n");
		else if (clicked == goodbyeButton && textListener != null)
			textListener.textPerformed("GOODBYE\n");
	}

}
