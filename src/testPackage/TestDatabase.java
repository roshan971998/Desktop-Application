package testPackage;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class TestDatabase {

	public static void main(String[] args) throws Exception {
		System.out.println("Running Database test");

		Database db = new Database();
		try {
			db.connect();

		} catch (ClassNotFoundException e) {
			// throw new Exception("Driver not Found!");
		}

		db.addPerson(new Person("Joe", "Builder", AgeCategory.adult, EmploymentCategory.employed, "1234", true,
				Gender.male));
		db.addPerson(new Person("puja", "freelancer", AgeCategory.child, EmploymentCategory.selfemployed, null, false,
				Gender.female));
		db.addPerson(new Person("Joe", "Lion Tamer", AgeCategory.adult, EmploymentCategory.employed, "1234", true,
				Gender.male));
		db.addPerson(new Person("puja", "freelancer", AgeCategory.senior, EmploymentCategory.selfemployed, null, false,
				Gender.female));
		db.addPerson(new Person("ramesh", "farmer", AgeCategory.senior, EmploymentCategory.selfemployed, null, false,
				Gender.male));

		db.saveToMySqlDatabase();
		db.loadFromMySqlDatabase();

		db.disconnect();
	}

}
