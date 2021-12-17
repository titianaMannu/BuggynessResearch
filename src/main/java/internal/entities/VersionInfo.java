package internal.entities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import internal.data_scraping.CommitsInformation;
import internal.entities.commits_entities.SelfCommit;
import internal.entities.jira_entities.Tag;
import internal.utils.DownloaderAgent;
import internal.utils.JSONConfig;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionInfo {
	private static final Logger LOGGER = Logger.getLogger(VersionInfo.class.getName());

	private Map<String, FileInfo> filesMap; // <pathName, FileInfo>

	private LocalDate releaseDate;

	private SelfCommit[] commits;

	private String id;

	private String name;

	private Tag tag;

	private Tag prevTag;

	private ExecutorService eService;

	/**
	 * 
	 */
	public VersionInfo() {
		super();
		this.filesMap = new HashMap<>();
		this.eService = Executors.newCachedThreadPool();

	}

	public Map<String, FileInfo> getFilesMap() {
		return filesMap;
	}

	public void setFilesMap(Map<String, FileInfo> filesMap) {
		this.filesMap = filesMap;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public SelfCommit[] getCommits() {
		return commits;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
		try {
			retrieveFiles();
		} catch (JSONException | IOException e) {
			LOGGER.log(Level.WARNING, "Got an exception::", e);
		}
	}

	public ExecutorService geteService() {
		return eService;
	}

	public void seteService(ExecutorService eService) {
		this.eService = eService;
	}

	public Tag getPrevTag() {
		return prevTag;
	}

	public void setPrevTag(Tag prevTag) {
		this.prevTag = prevTag;
	}

	/*public void retrieveCommits() throws InterruptedException {
		CommitInfo[] commitInfos;
		// do this in async task
		if (tag != null) {
			int tokenIndex = JSONConfig.getPseudoRandomIndex();
			CommitsInformation info = CommitsInformation.getInstance();
			if (prevTag == null) {
				commitInfos = info.retrieveCommits("&sha=" + tag.getCommit().getSha());
			} else {
				commitInfos = info.compare(prevTag.getName(), tag.getName(), tokenIndex);
			}

			// could take a long time
			this.commits = info.getCommitsBody(Arrays.asList(commitInfos));
		}
	}
*/
	private void retrieveFiles() throws JSONException, IOException {
		int tokenIndex = JSONConfig.getPseudoRandomIndex();
		String jsonContent = DownloaderAgent.readJsonFromGitHub(this.tag.getCommit().getUrl(), tokenIndex, "cache/tag/" + this.tag.getCommit().getSha() + "/", "tagBaseCommit-" + this.tag.getCommit().getSha());
		Gson gson = new Gson();
		SelfCommit commitContent = gson.fromJson(jsonContent, SelfCommit.class);
		// update release date on GitHub information!
		this.releaseDate = CommitsInformation.formatCommitDate(commitContent.getCommitData().getCommitter().getDate());

		String treeUrlString = commitContent.getCommitData().getTree().getUrl(); // link to the tree of this version
		// call with recursive=0 to explore subfolders recursively
		jsonContent = DownloaderAgent.readJsonFromGitHub(treeUrlString + "?recursive=0", tokenIndex, "cache/tag/" + this.tag.getCommit().getSha() + "/files/",
				"tag-" + this.tag.getCommit().getSha());
		JSONObject json = new JSONObject(jsonContent);

		// this is a limit case with extremely low probability because the threshold is
		// 100 000 items per folder. This is the reason why it is ignored.
		if (json.getBoolean("truncated")) {
			String msg = String.format("the page %s has been truncated%n", treeUrlString + "?recursive=0");
			LOGGER.log(Level.WARNING, msg);
		}
		JSONArray files = json.getJSONArray("tree");
		JSONObject currObj;
		for (int i = 0; i < files.length(); i++) {
			currObj = files.getJSONObject(i);
			if (currObj.get("type").equals("blob") // consider only java class
					&& FilenameUtils.getExtension(currObj.getString("path")).equals("java")) {
				FileInfo fileInfo = new FileInfo(currObj.getString("path"), currObj.getString("url"), eService,
						releaseDate, this.name);
				filesMap.put(fileInfo.getPathName(), fileInfo);
			}
		}

	}

	/*public void popolateFilesSize() throws InterruptedException {
		int count = 0;
		int tokenIndex;
		for (Map.Entry<String, FileInfo> entry : filesMap.entrySet()) {
			tokenIndex = JSONConfig.getPseudoRandomIndex();
			if (entry.getValue().getRevisionNum() == 0 || entry.getValue().getlOCTouched() == 0) {
				// we are not interested in files without changes in this release
				continue;
			}

			count++;
			if (count > 500) {
				count = 0;
				tokenIndex = JSONConfig.getPseudoRandomIndex();
			}

			final int currTokenIndex = tokenIndex;
			entry.getValue().populateFileSize(currTokenIndex);
		}

		eService.shutdown();
		boolean finished = eService.awaitTermination(5, TimeUnit.MINUTES);
		if (!finished) {
			LOGGER.log(Level.WARNING, "timeout occurred, some pages could be lost\n");
		}
	}
*/
}
