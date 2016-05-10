package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ESTATE_AGENT")
public class EstateAgent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME", nullable=false)
	private String name;

	@Column(name = "LOGIN", nullable=false)
	private String login;

	@Column(name = "PASSWORD", nullable=false)
	private String password;

	@Column(name = "ADDRESS", nullable=false)
	private String address;

	public EstateAgent() { }
	public EstateAgent(	String name, 
            			String address, 
            			String login, 
            			String password) 
	{ 
		this.name = name;
		this.address = address;
		this.login = login;
		this.password = password;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
