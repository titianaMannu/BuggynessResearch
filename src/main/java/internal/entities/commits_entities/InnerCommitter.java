
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InnerCommitter implements Serializable {

	@SerializedName("avatar_url")
	@Expose
	private String avatarUrl;
	@SerializedName("gravatar_id")
	@Expose
	private String gravatarId;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("html_url")
	@Expose
	private String htmlUrl;
	@SerializedName("followers_url")
	@Expose
	private String followersUrl;
	@SerializedName("following_url")
	@Expose
	private String followingUrl;
	@SerializedName("node_id")
	@Expose
	private String nodeId;
	@SerializedName("gists_url")
	@Expose
	private String gistsUrl;
	@SerializedName("starred_url")
	@Expose
	private String starredUrl;
	@SerializedName("subscriptions_url")
	@Expose
	private String subscriptionsUrl;
	@SerializedName("organizations_url")
	@Expose
	private String organizationsUrl;
	@SerializedName("repos_url")
	@Expose
	private String reposUrl;
	@SerializedName("events_url")
	@Expose
	private String eventsUrl;
	@SerializedName("received_events_url")
	@Expose
	private String receivedEventsUrl;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("site_admin")
	@Expose
	private boolean siteAdmin;
	@SerializedName("login")
	@Expose
	private String login;
	private static final long serialVersionUID = -3617904922866560538L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public InnerCommitter() {
		super();
	}

	/**
	 * 
	 * @param receivedEventsUrl
	 * @param siteAdmin
	 * @param followingUrl
	 * @param gistsUrl
	 * @param avatarUrl
	 * @param organizationsUrl
	 * @param reposUrl
	 * @param htmlUrl
	 * @param subscriptionsUrl
	 * @param login
	 * @param type
	 * @param url
	 * @param starredUrl
	 * @param gravatarId
	 * @param followersUrl
	 * @param id
	 * @param eventsUrl
	 * @param nodeId
	 */
	public InnerCommitter withLogin(String login) {
		this.login = login;
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InnerCommitter withId(int id) {
		this.id = id;
		return this;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public InnerCommitter withNodeId(String nodeId) {
		this.nodeId = nodeId;
		return this;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public InnerCommitter withAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
		return this;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public String getGravatarId() {
		return gravatarId;
	}

	public void setGravatarId(String gravatarId) {
		this.gravatarId = gravatarId;
	}

	public InnerCommitter withGravatarId(String gravatarId) {
		this.gravatarId = gravatarId;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public InnerCommitter withUrl(String url) {
		this.url = url;
		return this;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public InnerCommitter withHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
		return this;
	}

	public String getFollowersUrl() {
		return followersUrl;
	}

	public void setFollowersUrl(String followersUrl) {
		this.followersUrl = followersUrl;
	}

	public InnerCommitter withFollowersUrl(String followersUrl) {
		this.followersUrl = followersUrl;
		return this;
	}

	public String getFollowingUrl() {
		return followingUrl;
	}

	public void setFollowingUrl(String followingUrl) {
		this.followingUrl = followingUrl;
	}

	public InnerCommitter withFollowingUrl(String followingUrl) {
		this.followingUrl = followingUrl;
		return this;
	}

	public String getGistsUrl() {
		return gistsUrl;
	}

	public void setGistsUrl(String gistsUrl) {
		this.gistsUrl = gistsUrl;
	}

	public InnerCommitter withGistsUrl(String gistsUrl) {
		this.gistsUrl = gistsUrl;
		return this;
	}

	public String getStarredUrl() {
		return starredUrl;
	}

	public void setStarredUrl(String starredUrl) {
		this.starredUrl = starredUrl;
	}

	public InnerCommitter withStarredUrl(String starredUrl) {
		this.starredUrl = starredUrl;
		return this;
	}

	public String getSubscriptionsUrl() {
		return subscriptionsUrl;
	}

	public void setSubscriptionsUrl(String subscriptionsUrl) {
		this.subscriptionsUrl = subscriptionsUrl;
	}

	public InnerCommitter withSubscriptionsUrl(String subscriptionsUrl) {
		this.subscriptionsUrl = subscriptionsUrl;
		return this;
	}

	public String getOrganizationsUrl() {
		return organizationsUrl;
	}

	public void setOrganizationsUrl(String organizationsUrl) {
		this.organizationsUrl = organizationsUrl;
	}

	public InnerCommitter withOrganizationsUrl(String organizationsUrl) {
		this.organizationsUrl = organizationsUrl;
		return this;
	}

	public String getReposUrl() {
		return reposUrl;
	}

	public void setReposUrl(String reposUrl) {
		this.reposUrl = reposUrl;
	}

	public InnerCommitter withReposUrl(String reposUrl) {
		this.reposUrl = reposUrl;
		return this;
	}

	public String getEventsUrl() {
		return eventsUrl;
	}

	public void setEventsUrl(String eventsUrl) {
		this.eventsUrl = eventsUrl;
	}

	public InnerCommitter withEventsUrl(String eventsUrl) {
		this.eventsUrl = eventsUrl;
		return this;
	}

	public String getReceivedEventsUrl() {
		return receivedEventsUrl;
	}

	public void setReceivedEventsUrl(String receivedEventsUrl) {
		this.receivedEventsUrl = receivedEventsUrl;
	}

	public InnerCommitter withReceivedEventsUrl(String receivedEventsUrl) {
		this.receivedEventsUrl = receivedEventsUrl;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InnerCommitter withType(String type) {
		this.type = type;
		return this;
	}

	public boolean isSiteAdmin() {
		return siteAdmin;
	}

	public void setSiteAdmin(boolean siteAdmin) {
		this.siteAdmin = siteAdmin;
	}

	public InnerCommitter withSiteAdmin(boolean siteAdmin) {
		this.siteAdmin = siteAdmin;
		return this;
	}

}
