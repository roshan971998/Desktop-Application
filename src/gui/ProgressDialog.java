package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton cancelButton;
	private JProgressBar progressBar;
	private ProgressDialogListener listener;

	public ProgressDialog(Window parent, String title) {
		super(parent, title, ModalityType.APPLICATION_MODAL);
		// setSize(400,200);
		setLayout(new FlowLayout());
		setLocationRelativeTo(parent);

		cancelButton = new JButton("Cancel");
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);// in order to add some text to the bar of progressBar
		progressBar.setString("Retrieving Messages...");
		progressBar.setMaximum(10);

		// now minimum numerical and maximum numerical value of Progress bar can be set
		// here too
		// in constructor block but we are using her some method
		// add two controls to dialog they are button and progress bar
		// but first resize the progress bar to has the same height as button
		Dimension size = cancelButton.getPreferredSize();
		size.width = 400;
		progressBar.setPreferredSize(size);// so at the end we are setting the desired width but same height as button

		// in case we don't know what is the maximum value of thing running in
		// background we can set our progress bar as
		// progressBar.setIndeterminate(true);
		add(progressBar);
		add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					listener.progressDialogCancelled();
				}
			}
		});
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (listener != null) {
					listener.progressDialogCancelled();
				}
			}

		});
		pack();// after adding two controls to our frame we call it so that our window and all
				// the components in it will shrink
				// in order to take minimum size
	}

	public void setMaximum(int value) {
		progressBar.setMaximum(value);// so maximum can be 100% or 10% or anything we like to represented when
										// progress bar is complete
	}

	public void setValue(int value) {
		int progress = 100 * value / progressBar.getMaximum();
		progressBar.setString(String.format("%d %% complete", progress));
		progressBar.setValue(value);// this method will set the current value to progress bar at any point of time
									// of execution
	}

	@Override
	public void setVisible(final boolean visible) {

		// progressDialog.setVisible(true);------problem with the model dialog is that
		// when we set them visible then nothing happen
		// in our application and in any thread until it's gone away again hence we need
		// to run it in separate kind of swing thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				if (visible == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					progressBar.setValue(0);// so that it user should feel that every time bar is starting from 0
				}

				
				if (visible) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				} else {
					setCursor(Cursor.getDefaultCursor());
				}
				
				ProgressDialog.super.setVisible(visible);// now this model will not take hold of our application
			}
		});
	}

	public void setListener(ProgressDialogListener listener) {
		this.listener = listener;
	}

}
