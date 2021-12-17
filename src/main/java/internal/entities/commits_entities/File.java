
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import internal.utils.CommentTokenizer;

import java.io.IOException;
import java.io.Serializable;


public class File implements Serializable {

	@SerializedName("sha")
	@Expose
	private String sha;
	@SerializedName("filename")
	@Expose
	private String filename;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("additions")
	@Expose
	private int additions;
	@SerializedName("deletions")
	@Expose
	private int deletions;
	@SerializedName("changes")
	@Expose
	private int changes;
	@SerializedName("blob_url")
	@Expose
	private String blobUrl;
	@SerializedName("raw_url")
	@Expose
	private String rawUrl;
	@SerializedName("contents_url")
	@Expose
	private String contentsUrl;
	@SerializedName("patch")
	@Expose
	private String patch;
	@SerializedName("previous_filename")
	@Expose
	private String previousFilename;
	private static final long serialVersionUID = -7977647899854129181L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public File() {
		super();
	}

	public int retrieveChangesLOCView() throws IOException {
		int commentedLines = 0;
		int deletedC = 0;
		int addedC = 0;

		String addedLines = "";
		String deletedLines = "";
		if (this.patch != null) {
			String[] array = this.patch.split("\n");
			for (String line : array) {

				if (line.startsWith("+")) {
					line = line.replaceFirst("\\+", "");
					addedLines = addedLines.concat(line + "\n");
				} else if (line.startsWith("-")) {
					line = line.replaceFirst("-", "");
					deletedLines = deletedLines.concat(line + "\n");
				}

			}
		}
		addedC = CommentTokenizer.countComments(addedLines, true);
		deletedC = CommentTokenizer.countComments(deletedLines, true);
		decrementAdditions(addedC);
		decrementDeletions(deletedC);
		decrementChanges(addedC + deletedC);

		return commentedLines;
	}

	public String getPreviousFilename() {
		return previousFilename;
	}

	public void setPreviousFilename(String previousFilename) {
		this.previousFilename = previousFilename;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public File withSha(String sha) {
		this.sha = sha;
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File withFilename(String filename) {
		this.filename = filename;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public File withStatus(String status) {
		this.status = status;
		return this;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		if (additions < 0) {
			this.additions = 0;
		} else {
			this.additions = additions;
		}
	}

	public File withAdditions(int additions) {
		this.additions = additions;
		return this;
	}

	public int getDeletions() {
		return deletions;
	}

	private void decrementChanges(int comments) {
		if (this.changes - comments >= 0) {
			this.changes -= comments;
		} else {
			this.changes = 0;
		}
	}

	private void decrementAdditions(int comments) {
		if (this.additions - comments >= 0) {
			this.additions -= comments;
		} else {
			this.additions = 0;
		}
	}

	private void decrementDeletions(int comments) {
		if (this.deletions - comments >= 0) {
			this.deletions -= comments;
		} else {
			this.deletions = 0;
		}
	}

	public void setDeletions(int deletions) {
		if (deletions < 0) {
			this.deletions = 0;
		} else {
			this.deletions = deletions;
		}
	}

	public File withDeletions(int deletions) {
		this.deletions = deletions;
		return this;
	}

	public int getChanges() {
		return changes;
	}

	public void setChanges(int changes) {
		this.changes = changes;
	}

	public File withChanges(int changes) {
		this.changes = changes;
		return this;
	}

	public String getBlobUrl() {
		return blobUrl;
	}

	public void setBlobUrl(String blobUrl) {
		this.blobUrl = blobUrl;
	}

	public File withBlobUrl(String blobUrl) {
		this.blobUrl = blobUrl;
		return this;
	}

	public String getRawUrl() {
		return rawUrl;
	}

	public void setRawUrl(String rawUrl) {
		this.rawUrl = rawUrl;
	}

	public File withRawUrl(String rawUrl) {
		this.rawUrl = rawUrl;
		return this;
	}

	public String getContentsUrl() {
		return contentsUrl;
	}

	public void setContentsUrl(String contentsUrl) {
		this.contentsUrl = contentsUrl;
	}

	public File withContentsUrl(String contentsUrl) {
		this.contentsUrl = contentsUrl;
		return this;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public File withPatch(String patch) {
		this.patch = patch;
		return this;
	}

	@Override
	public String toString() {
		return "File [sha=" + sha + ", filename=" + filename + ", additions=" + additions + ", deletions=" + deletions
				+ ", changes=" + changes + ", patch=" + patch + "]";
	}

}
