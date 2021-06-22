package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	//private TextPanel textPanel;
	private ToolBar toolBar;
	private FormPanel formPanel;
	private JFileChooser fileChooser;
	private Controller controller;
	private TablePanel tablePanel;
	private PrefsDialog prefsDialog;
	private Preferences prefs;

	private JSplitPane splitPane;
	private JTabbedPane tabPane;
	private MessagePanel messagePanel;
	private TreePanel treePanel;

	private DatabaseToolbar databaseToolbar;

	public MainFrame() {

		super("Java Swing Application");
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(600, 500);
		setMinimumSize(new Dimension(500, 500));
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		toolBar = new ToolBar();
		databaseToolbar = new DatabaseToolbar();
		//textPanel = new TextPanel();
		formPanel = new FormPanel();
		tablePanel = new TablePanel();
		prefsDialog = new PrefsDialog(this);
		tabPane = new JTabbedPane();

		messagePanel = new MessagePanel();
		treePanel = new TreePanel(this);

		prefs = Preferences.userRoot().node("db");
		controller = new Controller();
		tablePanel.setData(controller.getPeople());
		tablePanel.setPersonTableListener(new PersonTableListener() {
			public void rowDeleted(int row) {
				// System.out.println(row);
				controller.removePerson(row);
			}

		});

		
		prefsDialog.setPrefsListener(new PrefsListener() {
			@Override
			public void preferencesSet(String user, String password, Integer port) {
				prefs.put("User", user);
				prefs.put("Password", password);
				prefs.putInt("port", port);
				try {
					controller.configure(port, user, password);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable to re-connect to database.", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		String user = prefs.get("user", "");
		String password = prefs.get("pssword", "");
		Integer port = prefs.getInt("port", 3306);
		prefsDialog.setDefaults(user, password, port);
		
		try {
			controller.configure(port, user, password);
		} catch (Exception e1) {
			// This shouldn't happen -- database is not connected
			System.err.println("Can't connect to database");
		}

		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new PersonFileFilter());

		setJMenuBar(createMenuBar());

		toolBar.setStringListener(new StringListener() {
			public void textPerformed(String text) {
				//textPanel.appendText(text);
			}
		});

		formPanel.setFormListener(new FormListener() {
			public void formEventOccurred(FormEvent ev) {
//				String name = ev.getName();
//				String occupation = ev.getOccupation();
//				int ageCat = ev.getAgeCategory();
//				String empCat = ev.getEmpCategory();
//				String taxID = ev.getTaxID();
//				boolean usCitizen = ev.isUsCitizen();
//				String gender = ev.getGender();
//				textPanel.appendText(name +" "+occupation+" "+gender+" "+ageCat+" "+empCat+" "+usCitizen+" "+taxID+"\n");
				controller.addPerson(ev);
				tablePanel.refresh();
			}
		});

		tabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				int tabIndex = tabPane.getSelectedIndex();
				//System.out.println(tabIndex);
				if (tabIndex == 2) {
					treePanel.refresh();
				}
			}
		});

		databaseToolbar.setToolbarListener(new DatabaseToolbarListener() {
			@Override
			public void saveEventoccured() {
				try {
					controller.connect();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Can't connect To Databse",
							"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
				try {
					controller.saveToMySqlDatabase();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable To Save To Databse",
							"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void refreshEventoccured() {
				try {
					controller.connect();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Can't connect To Databse",
							"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
				try {
					controller.loadFromMySqlDatabase();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable To Load From Databse",
							"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
				tablePanel.refresh();
			}

		});

		tabPane.addTab("Person Database", tablePanel);
		//tabPane.addTab("Messages", textPanel);
		tabPane.addTab("Message Panel", messagePanel);
		tabPane.addTab("Custom Tree Panel", treePanel);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tabPane);
		// splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,formPanel,tablePanel);
		splitPane.setOneTouchExpandable(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("window closing ");
				controller.disconnect();
				dispose();
				System.gc();
			}

		});
		
		refresh();

		// add(toolBar, BorderLayout.NORTH);
		// add(databaseToolbar, BorderLayout.NORTH);
		add(databaseToolbar, BorderLayout.PAGE_START);
//		add(formPanel, BorderLayout.WEST);
//	    add(textPanel,BorderLayout.CENTER);
//		add(tablePanel, BorderLayout.CENTER);
		add(splitPane, BorderLayout.CENTER);
	}

	private void refresh() {
//		try {
//			controller.connect();
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(MainFrame.this, "Can't connect To Databse",
//					"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
//		}
//		try {
//			controller.loadFromMySqlDatabase();
//		} catch (SQLException e) {
//			JOptionPane.showMessageDialog(MainFrame.this, "Unable To Load From Databse",
//					"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
//		}
//		tablePanel.refresh();
	}

	private JMenuBar createMenuBar() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem exportDataItem = new JMenuItem("Export Data...");
		JMenuItem importDataItem = new JMenuItem("Import Data...");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.add(exitItem);
		fileMenu.addSeparator();

		JMenu windowMenu = new JMenu("Window");
		JMenu showMenu = new JMenu("Show");

		JCheckBoxMenuItem showAnotherFormItem = new JCheckBoxMenuItem("Next Person Form");
		JMenuItem showFormItem = new JMenuItem("Person Form");
		JMenuItem prefsItem = new JMenuItem("Preferences...");

		showAnotherFormItem.setSelected(true);
		showAnotherFormItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();

				if (menuItem.isSelected()) {
					splitPane.setDividerLocation((int) formPanel.getMinimumSize().getWidth());
				}
				formPanel.setVisible(menuItem.isSelected());
			}
		});

		prefsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefsDialog.setVisible(true);

			}
		});

		fileMenu.setMnemonic(KeyEvent.VK_F);
		// exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

		importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		exportDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to Exit?",
						"Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
//				String text = JOptionPane.showInputDialog(MainFrame.this, "Enter Your name :", "Enter user name",
//				JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
//		        System.out.println(text);
				if (action == JOptionPane.OK_OPTION) {
					// System.exit(0);
					WindowListener[] listeners = getWindowListeners();
					for (WindowListener Listener : listeners) {
						Listener.windowClosing(new WindowEvent(MainFrame.this, 0));
					}
				}

			}

		});
		importDataItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					// System.out.println(fileChooser.getSelectedFile());
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());
						tablePanel.refresh();
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file ", "Error",
								JOptionPane.ERROR_MESSAGE);
						// e1.printStackTrace();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file ", "Error",
								JOptionPane.ERROR_MESSAGE);
						// e1.printStackTrace();
					}
				}
			}
		});
		exportDataItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					// System.out.println(fileChooser.getSelectedFile());
					try {
						controller.saveToFile(fileChooser.getSelectedFile());
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save data to file ", "Error",
								JOptionPane.ERROR_MESSAGE);
						// e1.printStackTrace();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save data to file ", "Error",
								JOptionPane.ERROR_MESSAGE);
						// e1.printStackTrace();
					}
				}
			}
		});
		showMenu.add(showAnotherFormItem);
		showMenu.add(showFormItem);
		windowMenu.add(showMenu);
		windowMenu.add(prefsItem);

		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		return menuBar;
	}
}
