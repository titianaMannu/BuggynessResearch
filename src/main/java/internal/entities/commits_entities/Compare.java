package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Compare implements Serializable {

	@SerializedName("commits")
	@Expose
	private List<CommitInfo> commits = null;
	@SerializedName("base_commit")
	@Expose
	private CommitInfo baseCommit;
	private static final long serialVersionUID = 1102972922875979418L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Compare() {
	}

	/**
	 * 
	 * @param commits
	 */
	public Compare(List<CommitInfo> commits, CommitInfo baseCommit) {
		super();
		this.commits = commits;
		this.baseCommit = baseCommit;
		commits.add(baseCommit);
	}

	public List<CommitInfo> getCommits() {
		return commits;
	}

	public void setCommits(List<CommitInfo> commits) {
		this.commits = commits;
	}

	public CommitInfo getBaseCommit() {
		return baseCommit;
	}

	public void setBaseCommit(CommitInfo baseCommit) {
		this.baseCommit = baseCommit;
	}

}