package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {

	private List<Person> people;
	private Connection con;

	private int port;
	private String user;
	private String password;

	public Database() {
		super();
		people = new LinkedList<Person>();
	}

	public void addPerson(Person person) {
		people.add(person);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public void configure(int port, String user, String password) throws Exception {

		this.port = port;
		this.user = user;
		this.password = password;

		if (con != null) {
			disconnect();
			connect();
		}
	}

	public void saveToFile(File file) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		// Person[] persons = new Person[people.size()];
		// persons =people.toArray(persons);
		Person[] persons = people.toArray(new Person[people.size()]);
		oos.writeObject(persons);
		oos.close();
	}

	public void loadFromFile(File file) throws FileNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

		try {

			Person[] persons = (Person[]) ois.readObject();
			people.clear();
			people.addAll(Arrays.asList(persons));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ois.close();
	}

	public void removePerson(int index) {
		people.remove(index);
	}

	public void connect() throws Exception {
		if (con != null)
			return;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver not Found!");
		}
		String url = "jdbc:mysql://localhost:3306/SwingTestDatabase";
		con = DriverManager.getConnection(url, "root", "MySqlUserAccounts@321?");
		System.out.println("Connected: ");
	}

	public void disconnect() {
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Can't Close Connection");
			}

	}

	public void saveToMySqlDatabase() throws SQLException {
		String checkSql = "Select count(*) as count from people where id=?";
		PreparedStatement checkStatement = con.prepareStatement(checkSql);

		String insertSql = "insert into people (id , name , age , employment_status , tax_id ,us_Citizen, gender , occupation) values(?,?,?,?,?,?,?,?)";
		PreparedStatement insertStatement = con.prepareStatement(insertSql);

		String updateSql = "update people set name=?,age=?,employment_status=?,tax_id=?,us_Citizen=?,gender=?,occupation=? where id=?";
		PreparedStatement updateStatement = con.prepareStatement(updateSql);

		for (Person person : people) {
			int id = person.getId();
			String name = person.getName();
			String occupation = person.getOccupation();
			AgeCategory age = person.getAgeCategory();
			EmploymentCategory emp = person.getEmpCategory();
			String tax = person.getTaxID();
			boolean isUS = person.isUsCitizen();
			Gender gender = person.getGender();

			checkStatement.setInt(1, id);
			ResultSet checkResult = checkStatement.executeQuery();
			checkResult.next();
			int count = checkResult.getInt(1);
			// System.out.println("count for person with id "+id+" is "+count);
			// now if count = 0 then we need to insert that person in database else we wil
			// update that
			if (count == 0) {
				System.out.println("Inserting person with ID " + id);

				int col = 1;
				insertStatement.setInt(col++, id);
				insertStatement.setString(col++, name);
				insertStatement.setString(col++, age.name());
				insertStatement.setString(col++, emp.name());
				insertStatement.setString(col++, tax);
				insertStatement.setBoolean(col++, isUS);
				insertStatement.setString(col++, gender.name());
				insertStatement.setString(col++, occupation);

				insertStatement.executeUpdate();
			} else {
				System.out.println("Updating person with ID " + id);

				int col = 1;
				updateStatement.setString(col++, name);
				updateStatement.setString(col++, age.name());
				updateStatement.setString(col++, emp.name());
				updateStatement.setString(col++, tax);
				updateStatement.setBoolean(col++, isUS);
				updateStatement.setString(col++, gender.name());
				updateStatement.setString(col++, occupation);
				updateStatement.setInt(col++, id);

				updateStatement.executeUpdate();
			}
		}
		updateStatement.close();
		insertStatement.close();
		checkStatement.close();
	}

	public void loadFromMySqlDatabase() throws SQLException {
		// since we are retrieving data from database so we 1st car what's already in
		// people list here
		people.clear();

		String selectSql = "select id , name , age , employment_status , tax_id ,us_Citizen, gender , occupation from people order by name";
		Statement selectStatement = con.createStatement();
		ResultSet results = selectStatement.executeQuery(selectSql);

		while (results.next()) {
			int id = results.getInt("id");
			String name = results.getString("name");
			String age = results.getString("age");
			String emp = results.getString("employment_status");
			String tax = results.getString("tax_id");
			boolean isUS = results.getBoolean("us_Citizen");
			String gender = results.getString("gender");
			String occupation = results.getString("occupation");

			people.add(new Person(id, name, occupation, AgeCategory.valueOf(age), EmploymentCategory.valueOf(emp), tax,
					isUS, Gender.valueOf(gender)));
		}

		results.close();
		selectStatement.close();
	}

}
