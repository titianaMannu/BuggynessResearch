
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class JIRAContent implements Serializable {

	@SerializedName("expand")
	@Expose
	private String expand;
	@SerializedName("startAt")
	@Expose
	private int startAt;
	@SerializedName("maxResults")
	@Expose
	private int maxResults;
	@SerializedName("total")
	@Expose
	private int total;
	@SerializedName("issues")
	@Expose
	private List<Issue> issues = null;
	private static final long serialVersionUID = 938373069736020646L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public JIRAContent() {
	}

	/**
	 * 
	 * @param expand
	 * @param total
	 * @param maxResults
	 * @param issues
	 * @param startAt
	 */
	public JIRAContent(String expand, int startAt, int maxResults, int total, List<Issue> issues) {
		super();
		this.expand = expand;
		this.startAt = startAt;
		this.maxResults = maxResults;
		this.total = total;
		this.issues = issues;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public JIRAContent withExpand(String expand) {
		this.expand = expand;
		return this;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public JIRAContent withStartAt(int startAt) {
		this.startAt = startAt;
		return this;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public JIRAContent withMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public JIRAContent withTotal(int total) {
		this.total = total;
		return this;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public JIRAContent withIssues(List<Issue> issues) {
		this.issues = issues;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("expand", expand).append("startAt", startAt)
				.append("maxResults", maxResults).append("total", total).append("issues", issues).toString();
	}

}
