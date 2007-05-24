package org.waffle.acceptance;

public class User {

	private Integer id;
	private String name;
	private String password;

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    this.password = password;
	}

	public User() {
	    super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
}
