package main;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import internal.utils.DownloaderAgent;
import org.json.JSONException;

import com.google.gson.Gson;

import internal.utils.JSONConfig;
import internal.data_scraping.JIRAInformation;
import internal.entities.VersionInfo;
import internal.entities.jira_entities.Tag;

public class VersionLinker {

	private static final Logger LOGGER = Logger.getLogger(VersionLinker.class.getName());

	private List<VersionInfo> jiraVersions;

	private List<Tag> githubTags;

	private static VersionLinker instance = null;

	public static VersionLinker getInstance() {
		if (instance == null) {
			instance = new VersionLinker();
		}
		return instance;
	}

	private VersionLinker() {
		super();
		this.githubTags = new ArrayList<>();
		try {
			this.jiraVersions = JIRAInformation.getInstance().getVersions(); // retrieve versions from jira
			retrieveTagsFromGithub(); // initialize tagsMap
			versionLink(); // link version in jira with versions in GitHub
		} catch (IOException | JSONException e) {
			LOGGER.log(Level.WARNING, "Got an exception:: {0}", e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	private void retrieveTagsFromGithub() throws IOException {
		String jsonString;
		/* Get project name */
		String project = JSONConfig.getProjectName();
		int page = 1;
		Gson gson = new Gson();
		Tag[] tagArray;
		do {
			int tokenIndex = JSONConfig.getPseudoRandomIndex();
			String url = JSONConfig.getRepository() + project.toLowerCase(Locale.ROOT) + "/tags?page=" + page
					+ "&per_page=100";
			jsonString = DownloaderAgent.readJsonFromGitHub(url, tokenIndex, "cache/tag/", Integer.toString(page));
			tagArray = gson.fromJson(jsonString, Tag[].class);
			this.githubTags.addAll(Arrays.asList(tagArray));
			page++;
		} while (tagArray.length == 100); // Stop condition: list's length is smaller than 100

	}

	private void versionLink() {
		List<VersionInfo> newVersionList = new ArrayList<>();
		VersionInfo v;
		Collections.reverse(this.githubTags);
		for (int j = 0; j < this.githubTags.size(); j++) { // iterate over tags
			Tag tag = this.githubTags.get(j);
			for (int i = 0; i < this.jiraVersions.size(); i++) {
				v = this.jiraVersions.get(i);
				if (compareVNames(v.getName(), tag.getName())) {
					// found a link on GitHub
					v.setTag(tag);
					if (j > 0) { // prevent IndexOutOfBound
						v.setPrevTag(this.githubTags.get(j - 1));
					}
					newVersionList.add(v);
					break;
				}
			}

		}

		this.jiraVersions = JIRAInformation.sortVersions(newVersionList);
	}

	private boolean compareVNames(String jiraName, String tagName) {
		boolean res = false;

		if (jiraName.equals(tagName) || tagName.equals("release-" + jiraName)
				|| tagName.equals(JSONConfig.getProjectName().toLowerCase() + "-" + jiraName)
				|| tagName.equals(jiraName + "-incubating")) {
			res = true;
		}

		return res;
	}

	public List<VersionInfo> getJiraVersions() {
		this.jiraVersions = JIRAInformation.sortVersions(this.jiraVersions);
		return jiraVersions;
	}

	public List<Tag> getGithubTags() {
		return githubTags;
	}

}
