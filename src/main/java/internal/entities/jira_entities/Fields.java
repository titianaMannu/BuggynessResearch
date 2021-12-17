
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Fields implements Serializable {

	@SerializedName("versions")
	@Expose
	private List<Version> versions = null;
	@SerializedName("resolutiondate")
	@Expose
	private String resolutiondate;
	@SerializedName("created")
	@Expose
	private String created;
	private static final long serialVersionUID = 5363783232292533581L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Fields() {
	}

	/**
	 * 
	 * @param versions
	 * @param resolutiondate
	 * @param created
	 */
	public Fields(List<Version> versions, String resolutiondate, String created) {
		super();
		this.versions = versions;
		this.resolutiondate = resolutiondate;
		this.created = created;
	}

	public List<Version> getVersions() {
		return versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public Fields withVersions(List<Version> versions) {
		this.versions = versions;
		return this;
	}

	public String getResolutiondate() {
		return resolutiondate;
	}

	public void setResolutiondate(String resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	public Fields withResolutiondate(String resolutiondate) {
		this.resolutiondate = resolutiondate;
		return this;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Fields withCreated(String created) {
		this.created = created;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("versions", versions).append("resolutiondate", resolutiondate)
				.append("created", created).toString();
	}

}
