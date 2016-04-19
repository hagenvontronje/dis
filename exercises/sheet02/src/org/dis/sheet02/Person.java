package org.dis.sheet02;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PERSON")
public class Person {

	@Id
	@Column(name="ID")
	private int id =-1;


	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="ADDRESS")
	private String address;
	
	
	public Person(String _firstName, String _name, String _address) {
		this.setFirstName(_firstName);
		this.setLastName(_name);
		this.address = _address;
	}

	public Person()
	{
		
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

	
}
