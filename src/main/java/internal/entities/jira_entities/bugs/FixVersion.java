
package internal.entities.jira_entities.bugs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class FixVersion implements Serializable {

	@SerializedName("self")
	@Expose
	private String self;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("archived")
	@Expose
	private boolean archived;
	@SerializedName("released")
	@Expose
	private boolean released;
	@SerializedName("releaseDate")
	@Expose
	private String releaseDate;
	private static final long serialVersionUID = -8725170280031281529L;

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isReleased() {
		return released;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("self", self).append("id", id).append("description", description)
				.append("name", name).append("archived", archived).append("released", released)
				.append("releaseDate", releaseDate).toString();
	}

}
