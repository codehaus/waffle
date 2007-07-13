package org.codehaus.waffle.example.mydvds.model;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Fabio Kung
 * @author Nico Steppat
 */
@Entity
public class Dvd implements Comparable<Dvd> {

	@Id
	@GeneratedValue
	private Long id;

	private String title;

	private String description;

	@ManyToMany
	private Set<User> users = new HashSet<User>();

	@Enumerated(EnumType.STRING)
	private DvdType type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DvdType getType() {
		return type;
	}

	public void setType(DvdType type) {
		this.type = type;
	}

	public Set<User> getUsers() {
		return users;
	}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dvd dvd = (Dvd) o;

        if (title != null ? !title.equals(dvd.title) : dvd.title != null) return false;
        if (type != dvd.type) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public String toString() {
        return String.format("[%s] %s - %s", this.type, this.title, this.description);
    }

    public int compareTo(Dvd other) {
        int comparison = this.type.compareTo(other.type);
        if (comparison == 0) {
            comparison = this.title.compareTo(other.title);
        }
        return comparison;
    }
}
