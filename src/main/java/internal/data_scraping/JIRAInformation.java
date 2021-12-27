package internal.data_scraping;


import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import internal.utils.DownloaderAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import internal.utils.JSONConfig;
import internal.entities.BugInfo;
import internal.entities.VersionInfo;
import internal.entities.jira_entities.Issue;
import internal.entities.jira_entities.JIRAContent;
import internal.entities.jira_entities.bugs.JiraBug;

public class JIRAInformation {

	private static JIRAInformation instance = null;

	private static final Logger LOGGER = Logger.getLogger(JIRAInformation.class.getName());

	private List<JIRAContent> jiraTicketPages = new ArrayList<>();

	private final List<VersionInfo> versions = new ArrayList<>();

	private BugInfo[] bugs;

	public static JIRAInformation getInstance() {
		if (instance == null) {
			instance = new JIRAInformation();
		}
		return instance;
	}

	private JIRAInformation() {
		super();
	}

	private void retrieveTickets() throws IOException {
		// first we need to retrieve the encoded access token from the
		// configuration file
		String project = JSONConfig.getProjectName();
		String type = JSONConfig.getIssueType();
		int i = 0;
		int j;
		int total;
		Gson gson = new Gson();
		do {
			// Only gets a max of 1000 at a time, so must do this multiple times if bugs
			// >1000
			j = i + 1000;
			String url = JSONConfig.getJiraApi() + "search?jql=project=%22" + project.toLowerCase(Locale.ROOT) + "%22AND%22issueType%22=%22"
					+ type + "%22AND(%22status%22=%22closed%22OR"
					+ "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
					+ i + "&maxResults=" + j;
			String jsonStr = DownloaderAgent.readJsonFromJira(url, "cache/jira/bug/", Integer.toString(i));
			JIRAContent currJiraContent = gson.fromJson(jsonStr, JIRAContent.class);
			this.jiraTicketPages.add(currJiraContent);
			total = currJiraContent.getTotal();
			i = j;
		} while (i < total);

	}


	private void buildVersions() throws JSONException, IOException {
		String url = JSONConfig.getJiraApi() + "project/" + JSONConfig.getProjectName();
		JSONObject json = new JSONObject(DownloaderAgent.readJsonFromJira(url, "cache/jira/versions/", url));
		JSONArray jsonVersions = json.getJSONArray("versions");
		for (int i = 0; i < jsonVersions.length(); i++) {
			if (jsonVersions.getJSONObject(i).has("releaseDate")) { // keep only versions with a release date
				VersionInfo v = new VersionInfo();
				// set the release date
				v.setReleaseDate(
						formatJiraDate(jsonVersions.getJSONObject(i).get("releaseDate").toString(), "yyyy-MM-dd"));
				if (jsonVersions.getJSONObject(i).has("name")) { // set name if exists
					v.setName(jsonVersions.getJSONObject(i).get("name").toString());
				}
				if (jsonVersions.getJSONObject(i).has("id")) { // set the id
					v.setId(jsonVersions.getJSONObject(i).get("id").toString());
				}
				this.versions.add(v);
			}

		}
	}

	public List<VersionInfo> getVersions() throws JSONException, IOException {
		if (this.versions.isEmpty()) {
			buildVersions();
		}
		// serve per ordinare le versioni in modo crescente

		return sortVersions(this.versions);
	}

	public static List<VersionInfo> sortVersions(List<VersionInfo> versions) {
		Collections.sort(versions, (VersionInfo v1, VersionInfo v2) -> {
			if (v1.getReleaseDate().isBefore(v2.getReleaseDate())) {
				return -1;
			} else if (v2.getReleaseDate().isBefore(v1.getReleaseDate())) {
				return 1;
			} else {
				return 0;
			}
		});

		return versions;
	}

	public static List<BugInfo> sortBugs(List<BugInfo> bugList) {
		Collections.sort(bugList, (BugInfo b1, BugInfo b2) -> {
			if (b1.getResolutionDate().isBefore(b2.getResolutionDate())) {
				return -1;
			} else if (b2.getResolutionDate().isBefore(b1.getResolutionDate())) {
				return 1;
			} else {
				return 0;
			}
		});

		return bugList;

	}

	public List<JIRAContent> getJiraTickets() throws IOException {
		if (this.jiraTicketPages.isEmpty()) {
			retrieveTickets();
		}
		return this.jiraTicketPages;
	}

	private LocalDate formatJiraDate(String dateString, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		dateString = dateString.replace("T", " ").replace("Z", "");
		return LocalDate.parse(dateString, dateTimeFormatter);
	}

	public void retrieveBugs() throws IOException, InterruptedException {
		jiraTicketPages = getJiraTickets();
		bugs = new BugInfo[jiraTicketPages.get(0).getTotal()];
		ExecutorService es = Executors.newCachedThreadPool();
		int index = 0;
		for (JIRAContent content : jiraTicketPages) {
			for (Issue issue : content.getIssues()) {
				final int j = index;
				final Issue currIssue = issue;
				es.execute(() -> {
					String jsonString = "";
					try {
						jsonString = DownloaderAgent.readJsonFromJira(currIssue.getSelf(), "cache/jira/issues/" , currIssue.getKey());
					} catch (IOException e) {
						LOGGER.log(Level.WARNING, "Got an exception::", e);
					}
					Gson gson = new Gson();
					JiraBug jiraBug = gson.fromJson(jsonString, JiraBug.class);
					BugInfo bug = new BugInfo(jiraBug.getJiraBugKey(), jiraBug.getJiraBugFields().getVersions(),
							jiraBug.getJiraBugFields().getFixVersions(),
							formatJiraDate(jiraBug.getJiraBugFields().getResolutiondate(), "yyyy-MM-dd HH:mm:ss.SSSZ"),
							formatJiraDate(jiraBug.getJiraBugFields().getCreated(), "yyyy-MM-dd HH:mm:ss.SSSZ"));
					bugs[bugs.length - 1 - j] = bug; // insert bug following increasing order
				});

				index++;
			}
		}
		es.shutdown();
		if (!es.awaitTermination(3, TimeUnit.MINUTES)) {
			LOGGER.log(Level.WARNING, "timeout occured");
		}
	}

	public BugInfo[] getBugs() throws IOException, InterruptedException {
		if (bugs == null) {
			retrieveBugs();
		}
		return this.bugs;
	}

	public void logBugs() {
		StringBuilder bld = new StringBuilder();
		String msgString = String.format("%nBugs: %d", jiraTicketPages.get(0).getTotal());
		bld.append(msgString + "\n\n");
		for (BugInfo bugInfo : bugs) {
			bld.append(bugInfo + "\n");
		}
		String finalMsg = String.format("List of content:%n%sNumber Of Bugs: %d%n", bld, bugs.length);
		LOGGER.log(Level.INFO, finalMsg);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		JIRAInformation jiraInformation = new JIRAInformation();
		jiraInformation.getBugs();
		jiraInformation.logBugs();
	}

}
