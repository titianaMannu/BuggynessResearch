package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import internal.utils.CSVBuilder;
import internal.data_scraping.CommitsInformation;
import internal.data_scraping.JIRAInformation;
import internal.entities.BugInfo;
import internal.entities.FileInfo;
import internal.entities.VersionInfo;
import internal.entities.commits_entities.CommitInfo;
import internal.entities.commits_entities.File;
import internal.entities.commits_entities.SelfCommit;

public class DefectivenessCalculator {

	private static final Logger LOGGER = Logger.getLogger(DefectivenessCalculator.class.getName());

	private static final String TIMEOUT_MSG = "timeout occurred";

	private final List<VersionInfo> versions;

	private Map<String, Integer> indexMap; // <versionName, index>

	private List<BugInfo> bugs; // list of bugs information taken from Jira

	private final Map<String, List<CommitInfo>> commitsPerTicketMap; // <ticketKey, commitList>

	/**
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws IOException
	 * 
	 */
	public DefectivenessCalculator() throws IOException, InterruptedException {
		super();
		TicketLinker.getInstance().ticketLink(); // linkage tra commit per ticket e so anche qual è l'ultima data
		commitsPerTicketMap = TicketLinker.getInstance().getCommitsPerTicketMap();

		VersionLinker versionLinker = VersionLinker.getInstance();
		// already ordered list of versions with a valid link to GitHub
		versions = versionLinker.getJiraVersions();

		JIRAInformation jiraInformation = JIRAInformation.getInstance();
		BugInfo[] bugArray = jiraInformation.getBugs();
		bugs = new ArrayList<>(Arrays.asList(bugArray));
		bugs = JIRAInformation.sortBugs(bugs); // (increasing) order bugs by using resolution date

		initializeIndexMap();
		ExecutorService eService = Executors.newCachedThreadPool();
		for (int i = 0; i < bugs.size(); i++) {
			BugInfo bug = bugs.get(i);

			bug.retrieveAVIndex(indexMap);
			bug.retrieveFVIndex(indexMap);
			// consistency check
			if (!bug.isConsistent()) {
				// we don't have any fixed version
				bugs.remove(i);
				continue;
			}
			bug.setOpeningVersion(versions);
			final int j = i;
			eService.execute(() -> getCommitFullInfo(bugs.get(j)));

		}

		eService.shutdown();
		if (!eService.awaitTermination(3, TimeUnit.MINUTES)) {
			LOGGER.log(Level.WARNING, TIMEOUT_MSG);
		}

	}

	private void getCommitFullInfo(BugInfo bug){
		List<CommitInfo> commitPerTicket = commitsPerTicketMap.get(bug.getKey());
		SelfCommit[] bugCommits = CommitsInformation.getInstance().getCommitsBody(commitPerTicket);
		bug.addAllCommits(Arrays.asList(bugCommits));
	}

	private void initializeIndexMap() {
		this.indexMap = new HashMap<>();
		for (int i = 0; i < versions.size(); i++) {
			VersionInfo v = versions.get(i);
			indexMap.put(v.getName(), i);
		}
	}

	private int findProportion(int bugIndex) {
		int perc = (int) Math.floor(0.01 * bugIndex);
		int sum = 0;
		int tot = 0;
		int avg = 0;
		for (int i = bugIndex - perc; i < bugIndex; i++) {
			sum += bugs.get(i).getProportion();
			tot++;
		}
		// average among the last 1% of fixed defects
		if (tot != 0 && perc != 0) {
			avg = sum / perc;
		}
		return avg;
	}

	private void findDefectiveFiles(BugInfo bug, int bugIndex) {
		List<Integer> av = bug.getAvIndexList();
		if (av.isEmpty()) {// we need to use proportion (moving window with a factor of 1%)
			bug.predictedIV(findProportion(bugIndex));
		}

		VersionInfo version;
		// affected versions are now available
		for (Integer versionIndex : bug.getAvIndexList()) { // for every affected version
			version = versions.get(versionIndex);
			for (SelfCommit commit : bug.getCommits()) { // for all "fix" commits of the current bug
				for (File file : commit.getFiles()) { // every file touched by every commit
					FileInfo fileInfo = version.getFilesMap().get(file.getFilename());
					if (fileInfo != null) {
						fileInfo.setDefective(true); // set defectiveness = true
					}
				}
			}
		}
	}

	public void findDefectiveness() {
		for (int i = 0; i < bugs.size(); i++) {
			findDefectiveFiles(bugs.get(i), i);
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		DefectivenessCalculator dCalculator = new DefectivenessCalculator();
		dCalculator.findDefectiveness();
		CSVBuilder.generateDefectivenessCSV(dCalculator.versions);
	}

}
