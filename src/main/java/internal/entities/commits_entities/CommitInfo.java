
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class CommitInfo implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sha == null) ? 0 : sha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommitInfo other = (CommitInfo) obj;
		if (sha == null) {
			if (other.sha != null)
				return false;
		} else if (!sha.equals(other.sha)) {
			return false;
		}
		return true;
	}

	@SerializedName("sha")
	@Expose
	private String sha;
	@SerializedName("node_id")
	@Expose
	private String nodeId;
	@SerializedName("commit")
	@Expose
	private Commit commit;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("html_url")
	@Expose
	private String htmlUrl;
	@SerializedName("comments_url")
	@Expose
	private String commentsUrl;
	@SerializedName("author")
	@Expose
	private InAuthor author;
	@SerializedName("committer")
	@Expose
	private InnerCommitter committer;
	@SerializedName("parents")
	@Expose
	private List<Parent> parents = null;
	private static final long serialVersionUID = -1725482583891473399L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public CommitInfo() {
		super();
	}


	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public CommitInfo withSha(String sha) {
		this.sha = sha;
		return this;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public CommitInfo withNodeId(String nodeId) {
		this.nodeId = nodeId;
		return this;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public CommitInfo withCommit(Commit commit) {
		this.commit = commit;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CommitInfo withUrl(String url) {
		this.url = url;
		return this;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public CommitInfo withHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
		return this;
	}

	public String getCommentsUrl() {
		return commentsUrl;
	}

	public void setCommentsUrl(String commentsUrl) {
		this.commentsUrl = commentsUrl;
	}

	public CommitInfo withCommentsUrl(String commentsUrl) {
		this.commentsUrl = commentsUrl;
		return this;
	}

	public InAuthor getAuthor() {
		return author;
	}

	public void setAuthor(InAuthor author) {
		this.author = author;
	}

	public CommitInfo withAuthor(InAuthor author) {
		this.author = author;
		return this;
	}

	public InnerCommitter getCommitter() {
		return committer;
	}

	public void setCommitter(InnerCommitter committer) {
		this.committer = committer;
	}

	public CommitInfo withCommitter(InnerCommitter committer) {
		this.committer = committer;
		return this;
	}

	public List<Parent> getParents() {
		return parents;
	}

	public void setParents(List<Parent> parents) {
		this.parents = parents;
	}

	public CommitInfo withParents(List<Parent> parents) {
		this.parents = parents;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("sha", sha).append("nodeId", nodeId).append("commit", commit)
				.append("url", url).append("htmlUrl", htmlUrl).append("commentsUrl", commentsUrl)
				.append("author", author).append("committer", committer).append("parents", parents).toString();
	}

}
