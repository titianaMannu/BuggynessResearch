
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Tag implements Serializable {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("zipball_url")
	@Expose
	private String zipballUrl;
	@SerializedName("tarball_url")
	@Expose
	private String tarballUrl;
	@SerializedName("commit")
	@Expose
	private Commit commit;
	@SerializedName("node_id")
	@Expose
	private String nodeId;
	private static final long serialVersionUID = -5618371499699760891L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Tag() {
	}

	/**
	 * 
	 * @param tarballUrl
	 * @param name
	 * @param commit
	 * @param zipballUrl
	 * @param nodeId
	 */
	public Tag(String name, String zipballUrl, String tarballUrl, Commit commit, String nodeId) {
		super();
		this.name = name;
		this.zipballUrl = zipballUrl;
		this.tarballUrl = tarballUrl;
		this.commit = commit;
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipballUrl() {
		return zipballUrl;
	}

	public void setZipballUrl(String zipballUrl) {
		this.zipballUrl = zipballUrl;
	}

	public String getTarballUrl() {
		return tarballUrl;
	}

	public void setTarballUrl(String tarballUrl) {
		this.tarballUrl = tarballUrl;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("zipballUrl", zipballUrl)
				.append("tarballUrl", tarballUrl).append("commit", commit).append("nodeId", nodeId).toString();
	}

}
