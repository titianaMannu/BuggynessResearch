
package internal.entities.jira_entities.bugs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Fields implements Serializable {

	@SerializedName("fixVersions")
	@Expose
	private List<FixVersion> fixVersions = null;
	@SerializedName("versions")
	@Expose
	private List<Version> versions = null;
	@SerializedName("resolutiondate")
	@Expose
	private String resolutiondate;
	@SerializedName("created")
	@Expose
	private String created;
	@SerializedName("updated")
	@Expose
	private String updated;
	private static final long serialVersionUID = 3815446555602532179L;

	public List<FixVersion> getFixVersions() {
		return fixVersions;
	}

	public void setFixVersions(List<FixVersion> fixVersions) {
		this.fixVersions = fixVersions;
	}

	public List<Version> getVersions() {
		return versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public String getResolutiondate() {
		return resolutiondate;
	}

	public void setResolutiondate(String resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("fixVersions", fixVersions).append("versions", versions)
				.append("resolutiondate", resolutiondate).append("created", created).append("updated", updated)
				.toString();
	}

}
