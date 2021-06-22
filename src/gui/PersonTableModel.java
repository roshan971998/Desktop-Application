package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.AgeCategory;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class PersonTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Person> db;

	private String[] colNames = { "ID", "Name", "Occupation", "Age Category", "Employment Category", "Us Citizen ",
			"Tax ID", "Gender", };

	public PersonTableModel() {
		super();
	}

	public void setData(List<Person> db) {
		this.db = db;
	}

	@Override
	public boolean isCellEditable(int row, int col) {

		switch (col) {
		case 1:
			return true;	
		case 4:
			return true;	
		case 5:
			return true;	
		default:
			return false;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {

		if (db == null)
			return;

		Person person = db.get(row);

		switch (col) {
		case 1:
			person.setName((String) value);
			break;
		case 4:
			person.setEmpCategory((EmploymentCategory)value);
			break;
		case 5:
			person.setUsCitizen((Boolean)value);
			break;
		default:
			return;
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return AgeCategory.class;
		case 4:
			return EmploymentCategory.class;
		case 5:
			return Boolean.class;
		case 6:
			return String.class;
		case 7:
			return Gender.class;
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		// return super.getColumnName(column);
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		return 8;// this integer represents no of columns we want to be displayed
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = db.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return person.getId();
		case 1:
			return person.getName();
		case 2:
			return person.getOccupation();
		case 3:
			return person.getAgeCategory();
		case 4:
			return person.getEmpCategory();
		case 5:
			return person.isUsCitizen();
		case 6:
			return person.getTaxID();
		case 7:
			return person.getGender();
		}
		return null;
	}

}
