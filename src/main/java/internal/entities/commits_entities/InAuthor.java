
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InAuthor implements Serializable {

	@SerializedName("login")
	@Expose
	private String inAuthlogin;
	@SerializedName("id")
	@Expose
	private int inAuthId;
	@SerializedName("node_id")
	@Expose
	private String inAuthNodeId;
	@SerializedName("avatar_url")
	@Expose
	private String inAuthAvatarUrl;
	@SerializedName("gravatar_id")
	@Expose
	private String inAuthGravatarId;
	@SerializedName("url")
	@Expose
	private String inAuthUrl;
	@SerializedName("html_url")
	@Expose
	private String inAuthHtmlUrl;
	@SerializedName("followers_url")
	@Expose
	private String inAuthFollowersUrl;
	@SerializedName("following_url")
	@Expose
	private String inAuthFollowingUrl;
	@SerializedName("gists_url")
	@Expose
	private String inAuthGistsUrl;
	@SerializedName("starred_url")
	@Expose
	private String inAuthStarredUrl;
	@SerializedName("subscriptions_url")
	@Expose
	private String inAuthSubscriptionsUrl;
	@SerializedName("organizations_url")
	@Expose
	private String inAuthOrganizationsUrl;
	@SerializedName("repos_url")
	@Expose
	private String inAuthReposUrl;
	@SerializedName("events_url")
	@Expose
	private String inAuthEventsUrl;
	@SerializedName("received_events_url")
	@Expose
	private String inAuthReceivedEventsUrl;
	@SerializedName("type")
	@Expose
	private String inAuthType;
	@SerializedName("site_admin")
	@Expose
	private boolean inAuthSiteAdmin;
	private static final long serialVersionUID = -5048670319595542810L;

	public InAuthor() {
		super();
	}

	/**
	 * 
	 * @param inAuthReceivedEventsUrl
	 * @param inAuthSiteAdmin
	 * @param inAuthFollowingUrl
	 * @param inAuthGistsUrl
	 * @param inAuthAvatarUrl
	 * @param inAuthOrganizationsUrl
	 * @param inAuthReposUrl
	 * @param inAuthHtmlUrl
	 * @param inAuthSubscriptionsUrl
	 * @param inAuthlogin
	 * @param inAuthType
	 * @param inAuthUrl
	 * @param inAuthStarredUrl
	 * @param inAuthGravatarId
	 * @param inAuthFollowersUrl
	 * @param id
	 * @param inAuthEventsUrl
	 * @param inAuthNodeId
	 */

	public InAuthor withInAuthId(int id) {
		this.inAuthId = id;
		return this;
	}

	public String getInAuthNodeId() {
		return inAuthNodeId;
	}

	public void setInAuthNodeId(String nodeId) {
		this.inAuthNodeId = nodeId;
	}

	public InAuthor withInAuthNodeId(String nodeId) {
		this.inAuthNodeId = nodeId;
		return this;
	}

	public String getInAuthAvatarUrl() {
		return inAuthAvatarUrl;
	}

	public void setInAuthAvatarUrl(String avatarUrl) {
		this.inAuthAvatarUrl = avatarUrl;
	}

	public String getInAuthLogin() {
		return inAuthlogin;
	}

	public void setInAuthLogin(String login) {
		this.inAuthlogin = login;
	}

	public InAuthor withInAuthLogin(String login) {
		this.inAuthlogin = login;
		return this;
	}

	public int getInAuthId() {
		return inAuthId;
	}

	public void setInAuthId(int id) {
		this.inAuthId = id;
	}

	public InAuthor withInAuthAvatarUrl(String avatarUrl) {
		this.inAuthAvatarUrl = avatarUrl;
		return this;
	}

	public String getInAuthGravatarId() {
		return inAuthGravatarId;
	}

	public void setInAuthGravatarId(String gravatarId) {
		this.inAuthGravatarId = gravatarId;
	}

	public InAuthor withInAuthGravatarId(String gravatarId) {
		this.inAuthGravatarId = gravatarId;
		return this;
	}

	public String getInAuthUrl() {
		return inAuthUrl;
	}

	public void setInAuthUrl(String url) {
		this.inAuthUrl = url;
	}

	public InAuthor withInAuthUrl(String url) {
		this.inAuthUrl = url;
		return this;
	}

	public String getInAuthHtmlUrl() {
		return inAuthHtmlUrl;
	}

	public void setInAuthHtmlUrl(String htmlUrl) {
		this.inAuthHtmlUrl = htmlUrl;
	}

	public InAuthor withInAuthStarredUrl(String starredUrl) {
		this.inAuthStarredUrl = starredUrl;
		return this;
	}

	public String getInAuthSubscriptionsUrl() {
		return inAuthSubscriptionsUrl;
	}

	public void setInAuthSubscriptionsUrl(String subscriptionsUrl) {
		this.inAuthSubscriptionsUrl = subscriptionsUrl;
	}

	public InAuthor withInAuthSubscriptionsUrl(String subscriptionsUrl) {
		this.inAuthSubscriptionsUrl = subscriptionsUrl;
		return this;
	}

	public String getInAuthOrganizationsUrl() {
		return inAuthOrganizationsUrl;
	}

	public void setInAuthOrganizationsUrl(String organizationsUrl) {
		this.inAuthOrganizationsUrl = organizationsUrl;
	}

	public InAuthor withInAuthOrganizationsUrl(String organizationsUrl) {
		this.inAuthOrganizationsUrl = organizationsUrl;
		return this;
	}

	public String getInAuthReposUrl() {
		return inAuthReposUrl;
	}

	public void setInAuthReposUrl(String reposUrl) {
		this.inAuthReposUrl = reposUrl;
	}

	public InAuthor withInAuthReposUrl(String reposUrl) {
		this.inAuthReposUrl = reposUrl;
		return this;
	}

	public String getInAuthEventsUrl() {
		return inAuthEventsUrl;
	}

	public void setInAuthEventsUrl(String eventsUrl) {
		this.inAuthEventsUrl = eventsUrl;
	}

	public InAuthor withInAuthEventsUrl(String eventsUrl) {
		this.inAuthEventsUrl = eventsUrl;
		return this;
	}

	public String getInAuthReceivedEventsUrl() {
		return inAuthReceivedEventsUrl;
	}

	public void setInAuthReceivedEventsUrl(String receivedEventsUrl) {
		this.inAuthReceivedEventsUrl = receivedEventsUrl;
	}

	public InAuthor withInAuthReceivedEventsUrl(String receivedEventsUrl) {
		this.inAuthReceivedEventsUrl = receivedEventsUrl;
		return this;
	}

	public String getInAuthType() {
		return inAuthType;
	}

	public void setInAuthType(String type) {
		this.inAuthType = type;
	}

	public InAuthor withInAuthType(String type) {
		this.inAuthType = type;
		return this;
	}

	public boolean isInAuthSiteAdmin() {
		return inAuthSiteAdmin;
	}

	public void setInAuthSiteAdmin(boolean siteAdmin) {
		this.inAuthSiteAdmin = siteAdmin;
	}

	public InAuthor withInAuthSiteAdmin(boolean siteAdmin) {
		this.inAuthSiteAdmin = siteAdmin;
		return this;
	}

	public InAuthor withInAuthHtmlUrl(String htmlUrl) {
		this.inAuthHtmlUrl = htmlUrl;
		return this;
	}

	public String getInAuthFollowersUrl() {
		return inAuthFollowersUrl;
	}

	public void setInAuthFollowersUrl(String followersUrl) {
		this.inAuthFollowersUrl = followersUrl;
	}

	public InAuthor withInAuthFollowersUrl(String followersUrl) {
		this.inAuthFollowersUrl = followersUrl;
		return this;
	}

	public String getInAuthFollowingUrl() {
		return inAuthFollowingUrl;
	}

	public void setInAuthFollowingUrl(String followingUrl) {
		this.inAuthFollowingUrl = followingUrl;
	}

	public InAuthor withInAuthFollowingUrl(String followingUrl) {
		this.inAuthFollowingUrl = followingUrl;
		return this;
	}

	public String getInAuthGistsUrl() {
		return inAuthGistsUrl;
	}

	public void setInAuthGistsUrl(String gistsUrl) {
		this.inAuthGistsUrl = gistsUrl;
	}

	public InAuthor withInAuthGistsUrl(String gistsUrl) {
		this.inAuthGistsUrl = gistsUrl;
		return this;
	}

	public String getInAuthStarredUrl() {
		return inAuthStarredUrl;
	}

	public void setInAuthStarredUrl(String starredUrl) {
		this.inAuthStarredUrl = starredUrl;
	}

}
