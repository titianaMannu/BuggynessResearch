
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Author implements Serializable {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("date")
	@Expose
	private String date;
	private static final long serialVersionUID = -2904790936648434788L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Author() {
	}

	/**
	 * 
	 * @param date
	 * @param name
	 * @param email
	 */
	public Author(String name, String email, String date) {
		super();
		this.name = name;
		this.email = email;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Author withName(String name) {
		this.name = name;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Author withEmail(String email) {
		this.email = email;
		return this;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Author withDate(String date) {
		this.date = date;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("email", email).append("date", date).toString();
	}

}
