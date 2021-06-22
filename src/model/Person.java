package model;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int count = 1;
	private int id;
	private String name;
	private String occupation;
	private AgeCategory ageCategory;
	private EmploymentCategory empCategory;
	private String taxID;
	private boolean usCitizen;
	private Gender gender;

	public Person(String name, String occupation, AgeCategory ageCategory, EmploymentCategory empCategory, String taxID,
			boolean usCitizen, Gender gender) {
		this.id = count;
		count++;
		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCategory;
		this.empCategory = empCategory;
		this.taxID = taxID;
		this.usCitizen = usCitizen;
		this.gender = gender;
	}

	public Person(int id, String name, String occupation, AgeCategory ageCategory, EmploymentCategory empCategory,
			String taxID, boolean usCitizen, Gender gender) {
		this.id = id;
		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCategory;
		this.empCategory = empCategory;
		this.taxID = taxID;
		this.usCitizen = usCitizen;
		this.gender = gender;
		count++;
	}

	@Override
	public String toString() {
		return id + ": " + name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}

	public EmploymentCategory getEmpCategory() {
		return empCategory;
	}

	public void setEmpCategory(EmploymentCategory empCategory) {
		this.empCategory = empCategory;
	}

	public String getTaxID() {
		return taxID;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	public boolean isUsCitizen() {
		return usCitizen;
	}

	public void setUsCitizen(boolean usCitizen) {
		this.usCitizen = usCitizen;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

}
