
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SelfCommit implements Serializable {

	@SerializedName("sha")
	@Expose
	private String selfCommitSha;
	@SerializedName("commit")
	@Expose
	private Commit commitData;
	@SerializedName("url")
	@Expose
	private String selfCommitUrl;
	@SerializedName("stats")
	@Expose
	private Stats selfCommitStats;
	@SerializedName("files")
	@Expose
	private List<File> files = null;
	@SerializedName("author")
	@Expose
	private InAuthor author;

	private static final long serialVersionUID = 3538347832025487884L;

	public SelfCommit() {
		// No args constructor for use in serialization
	}

	public InAuthor getAuthor() {
		return author;
	}

	public void setAuthor(InAuthor author) {
		this.author = author;
	}

	public String getSelfCommitSha() {
		return selfCommitSha;
	}

	public void setSelfCommitSha(String selfCommitSha) {
		this.selfCommitSha = selfCommitSha;
	}

	public Commit getCommitData() {
		return commitData;
	}

	public void setCommitData(Commit commitData) {
		this.commitData = commitData;
	}

	public String getSelfCommitUrl() {
		return selfCommitUrl;
	}

	public void setSelfCommitUrl(String selfCommitUrl) {
		this.selfCommitUrl = selfCommitUrl;
	}

	public Stats getSelfCommitStats() {
		return selfCommitStats;
	}

	public void setSelfCommitStats(Stats selfCommitStats) {
		this.selfCommitStats = selfCommitStats;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "SelfCommit [sha=" + selfCommitSha + "]";
	}

}
