
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Parent implements Serializable {

	@SerializedName("sha")
	@Expose
	private String sha;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("html_url")
	@Expose
	private String htmlUrl;
	private static final long serialVersionUID = -4554116752738065659L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Parent() {
	}

	/**
	 * 
	 * @param htmlUrl
	 * @param sha
	 * @param url
	 */
	public Parent(String sha, String url, String htmlUrl) {
		super();
		this.sha = sha;
		this.url = url;
		this.htmlUrl = htmlUrl;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public Parent withSha(String sha) {
		this.sha = sha;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Parent withUrl(String url) {
		this.url = url;
		return this;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public Parent withHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("sha", sha).append("url", url).append("htmlUrl", htmlUrl).toString();
	}

}
