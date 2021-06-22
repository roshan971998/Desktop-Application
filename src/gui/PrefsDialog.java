package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PrefsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton okButton;
	private JButton cancelButton;
	private JSpinner portSpinner;
	private SpinnerNumberModel spinnerModel;

	private JTextField userField;
	private JPasswordField passField;
	private PrefsListener prefsListener;

	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);
		setSize(250, 200);
		setLocationRelativeTo(parent);

		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		spinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		portSpinner = new JSpinner(spinnerModel);
		userField = new JTextField(10);
		passField = new JPasswordField(10);
		passField.setEchoChar('*');

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer port = (Integer) portSpinner.getValue();
				String user = userField.getText();
				char[] password = passField.getPassword();
				// System.out.println(user +" : "+new String(password));
				if (prefsListener != null)
					prefsListener.preferencesSet(user, new String(password), port);

				// now as soon as we click OK button of dialog should go that is we need to make
				// it invisible
				setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		layoutControls();

	}

	public void setDefaults(String user, String password, int port) {
		userField.setText(user);
		passField.setText(password);
		portSpinner.setValue(port);
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;

	}

	private void layoutControls() {

		setLayout(new BorderLayout());
		Border titleBorder = BorderFactory.createTitledBorder("Database Preferences");
		Border spaceBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);

		JPanel controlPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(controlPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		controlPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 1;
		gc.weighty = 0.05;

		/////////// first row ///////////////
		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		controlPanel.add(new JLabel("User :"), gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		controlPanel.add(userField, gc);

		/////////// Next row ///////////////
		gc.weightx = 1;
		gc.weighty = 0.14;
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		controlPanel.add(new JLabel("Password :"), gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		controlPanel.add(passField, gc);

		/////////// Next row ///////////////
		gc.weightx = 1;
		gc.weighty = 0.95;
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		controlPanel.add(new JLabel("Port :"), gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		controlPanel.add(portSpinner, gc);

		/////////// Buttons Panels ///////////////
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton, gc);
		buttonPanel.add(cancelButton, gc);

		// in order to make both button look equal sized
		Dimension buttonSize = cancelButton.getPreferredSize();
		okButton.setPreferredSize(buttonSize);
	}

}
