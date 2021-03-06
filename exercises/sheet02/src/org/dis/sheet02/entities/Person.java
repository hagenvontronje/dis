package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON")
public class Person {

	@Id
	@Column(name = "ID")
	private int id = -1;

	@Column(name = "FIRST_NAME", nullable=false)
	private String firstName;

	@Column(name = "LAST_NAME", nullable=false)
	private String lastName;

	@Column(name = "ADDRESS", nullable=false)
	private String address;

	public Person(String _firstName, String _name, String _address) {
		this.setFirstName(_firstName);
		this.setLastName(_name);
		this.setAddress(_address);
	}

	public Person() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Person))
			return false;
		Person otherPerson = (Person)obj;
		if (id != 0 && otherPerson.getId() != 0)
			return id == otherPerson.getId();
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return id != 0 ? id : super.hashCode();
	}
}
