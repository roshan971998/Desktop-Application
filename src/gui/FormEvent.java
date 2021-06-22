package gui;

import java.util.EventObject;

public class FormEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private String name;
	private String occupation;
	private Integer ageCategory;
	private String empCategory;
	private String taxID;
	private boolean usCitizen;
	private String gender;

	public FormEvent(Object source) {
		super(source);
	}

	public FormEvent(Object source, String name, String occupation, int ageCat, String empcat, String taxid,
			boolean uscitizen, String gender) {
		super(source);
		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCat;
		this.empCategory = empcat;
		this.taxID = taxid;
		this.usCitizen = uscitizen;
		this.gender = gender;

	}

	public String getGender() {
		return gender;
	}

	public String getTaxID() {
		return taxID;
	}

	public boolean isUsCitizen() {
		return usCitizen;
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

	public Integer getAgeCategory() {
		return ageCategory;
	}

	public String getEmpCategory() {
		return empCategory;
	}

}
