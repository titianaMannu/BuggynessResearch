
package internal.entities.jira_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Commit implements Serializable {

	@SerializedName("sha")
	@Expose
	private String sha;
	@SerializedName("url")
	@Expose
	private String url;
	private static final long serialVersionUID = -5911242643735937352L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Commit() {
	}

	/**
	 * 
	 * @param sha
	 * @param url
	 */
	public Commit(String sha, String url) {
		super();
		this.sha = sha;
		this.url = url;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("sha", sha).append("url", url).toString();
	}

}
