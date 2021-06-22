package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import gui.FormEvent;
import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class Controller {

	Database db = new Database();

	public List<Person> getPeople() {
		return db.getPeople();
	}

	public void addPerson(FormEvent ev) {

		String name = ev.getName();
		String occupation = ev.getOccupation();
		int ageCat = ev.getAgeCategory();
		String empCat = ev.getEmpCategory();
		String taxID = ev.getTaxID();
		boolean usCitizen = ev.isUsCitizen();
		String gen = ev.getGender();

		AgeCategory ageCategory = null;// if you initialize it with null then we don't have to provide default case in
										// switch else it is must
		switch (ageCat) {
		case 0:
			ageCategory = AgeCategory.child;
			break;
		case 1:
			ageCategory = AgeCategory.adult;
			break;
		case 2:
			ageCategory = AgeCategory.senior;
			break;
		}

		EmploymentCategory employmentCategory;
		if (empCat.equals("Employed"))
			employmentCategory = EmploymentCategory.employed;
		else if (empCat.equalsIgnoreCase("Self-Employed"))
			employmentCategory = EmploymentCategory.selfemployed;
		else if (empCat.equalsIgnoreCase("Unemployed"))
			employmentCategory = EmploymentCategory.unemployed;
		else
			employmentCategory = EmploymentCategory.other;

		Gender gender;
		if (gen.equals("male"))
			gender = Gender.male;
		else
			gender = Gender.female;

		Person person = new Person(name, occupation, ageCategory, employmentCategory, taxID, usCitizen, gender);
		db.addPerson(person);
	}

	public void saveToFile(File file) throws FileNotFoundException, IOException {
		db.saveToFile(file);
	}

	public void loadFromFile(File file) throws FileNotFoundException, IOException {
		db.loadFromFile(file);
	}

	public void saveToMySqlDatabase() throws SQLException {
		db.saveToMySqlDatabase();
	}

	public void loadFromMySqlDatabase() throws SQLException {
		db.loadFromMySqlDatabase();
	}

	public void connect() throws Exception {
		db.connect();
	}

	public void disconnect() {
		db.disconnect();
	}

	public void configure(int port, String user, String password) throws Exception {
		db.configure(port, user, password);
	}

	public void removePerson(int index) {
		db.removePerson(index);
	}
}
