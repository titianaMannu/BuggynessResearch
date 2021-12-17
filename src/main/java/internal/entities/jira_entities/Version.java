
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Version implements Serializable {

	@SerializedName("self")
	@Expose
	private String self;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("archived")
	@Expose
	private boolean archived;
	@SerializedName("released")
	@Expose
	private boolean released;
	private static final long serialVersionUID = -925574501984609670L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Version() {
	}

	/**
	 * 
	 * @param archived
	 * @param name
	 * @param self
	 * @param id
	 * @param released
	 */
	public Version(String self, String id, String name, boolean archived, boolean released) {
		super();
		this.self = self;
		this.id = id;
		this.name = name;
		this.archived = archived;
		this.released = released;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public Version withSelf(String self) {
		this.self = self;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Version withId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Version withName(String name) {
		this.name = name;
		return this;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Version withArchived(boolean archived) {
		this.archived = archived;
		return this;
	}

	public boolean isReleased() {
		return released;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}

	public Version withReleased(boolean released) {
		this.released = released;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("self", self).append("id", id).append("name", name)
				.append("archived", archived).append("released", released).toString();
	}

}
