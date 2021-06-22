package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FormPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel nameLabel;
	private JLabel occupationLabel;
	private JLabel ageLabel;
	private JLabel empLabel;

	private JTextField nameField;
	private JTextField occupationField;

	private JCheckBox citizenCheck;
	private JTextField taxField;
	private JLabel taxLabel;

	private JList<AgeCategory> ageList;
	private JComboBox<String> empCombo;

	private JRadioButton maleRadio;
	private JRadioButton femaleRadio;
	private ButtonGroup genderGroup;
	private JButton okbtn;
	private FormListener formListener;

	public FormPanel() {

		this.setPreferredSize(new Dimension(250, 56));// her value of height is ignored by Border Layout Manager,only
		this.setMinimumSize(new Dimension(250, 56)); // the value for width (here 250) matters

		Border innerBorder = BorderFactory.createTitledBorder("ADD PERSON");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		nameLabel = new JLabel("Name :");
		occupationLabel = new JLabel("Occupation :");
		ageLabel = new JLabel("Age :");
		empLabel = new JLabel("Employment :");
		nameField = new JTextField(10);
		occupationField = new JTextField(10);

		// set up age ListBox
		ageList = new JList<AgeCategory>();
		DefaultListModel<AgeCategory> ageModel = new DefaultListModel<>();
		ageModel.addElement(new AgeCategory(0, "under 18"));
		ageModel.addElement(new AgeCategory(1, "18 to 65"));
		ageModel.addElement(new AgeCategory(2, "65 or over"));

		ageList.setModel(ageModel);
		ageList.setPreferredSize(new Dimension(110, 70));
		ageList.setBorder(BorderFactory.createEtchedBorder());
		ageList.setSelectedIndex(1);

		// set up employment ComboBox
		empCombo = new JComboBox<String>();
		DefaultComboBoxModel<String> empModel = new DefaultComboBoxModel<String>();
		empModel.addElement("Employed");
		empModel.addElement("Unemployed");
		empModel.addElement("Self-Employed");
		empCombo.setModel(empModel);
		empCombo.setSelectedIndex(0);
		empCombo.setEditable(true);

		// set up CheckBox
		citizenCheck = new JCheckBox();
		taxLabel = new JLabel("Tax ID :");
		taxField = new JTextField(10);

		// setup Tax ID
		taxLabel.setEnabled(false);
		taxField.setEnabled(false);
		citizenCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isTicked = citizenCheck.isSelected();
				taxLabel.setEnabled(isTicked);
				taxField.setEnabled(isTicked);
			}
		});

		// set up Radio Button
		maleRadio = new JRadioButton("Male");
		femaleRadio = new JRadioButton("Female");
		genderGroup = new ButtonGroup();
		genderGroup.add(maleRadio);
		genderGroup.add(femaleRadio);
		maleRadio.setActionCommand("male");
		femaleRadio.setActionCommand("female");

		// set up Button
		okbtn = new JButton("OK");
		// okbtn.setMnemonic(KeyEvent.VK_ENTER);
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String occupation = occupationField.getText();
				AgeCategory ageCat = ageList.getSelectedValue();
				String empCat = (String) empCombo.getSelectedItem();
				String taxID = taxField.getText();
				boolean usCitizen = citizenCheck.isSelected();
				String gender = genderGroup.getSelection().getActionCommand();

				FormEvent ev = new FormEvent(this, name, occupation, ageCat.getId(), empCat, taxID, usCitizen, gender);
				if (formListener != null)
					formListener.formEventOccurred(ev);
			}

		});

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;

		//// First row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(nameField, gc);

		//// Second row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 1;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(occupationLabel, gc);

		gc.gridx = 1;
		gc.gridy = 1;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(occupationField, gc);

		//// Third row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 2;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(ageLabel, gc);

		gc.gridx = 1;
		gc.gridy = 2;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(ageList, gc);

		//// Fourth row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 3;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(empLabel, gc);// or add(new JLabel("Employment"),gc);

		gc.gridx = 1;
		gc.gridy = 3;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(empCombo, gc);

		//// Fifth row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 4;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("US Citizen"), gc);// or add(new JLabel("Employment"),gc);

		gc.gridx = 1;
		gc.gridy = 4;// or gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(citizenCheck, gc);

		/// six row///
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy++;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(taxLabel, gc);// or add(new JLabel("Employment"),gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(taxField, gc);

		/// seven row///
		gc.weightx = 1;
		gc.weighty = 0.05;

		gc.gridy++;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Gender :"), gc);// or add(new JLabel("Employment"),gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(maleRadio, gc);
//		gc.anchor = GridBagConstraints.CENTER;
//		add(femaleRadio, gc);

		gc.weightx = 1;
		gc.weighty = 0.05;

		gc.gridy++;
		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(femaleRadio, gc);

		/// last row//////
		gc.weightx = 1;
		gc.weighty = 2;

		gc.gridx = 1;
		gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(okbtn, gc);
	}

	public void setFormListener(FormListener Listener) {
		this.formListener = Listener;

	}
}

class AgeCategory {
	private int id;
	private String text;

	public AgeCategory(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public int getId() {
		return id;
	}

}
