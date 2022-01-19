package internal.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import internal.entities.commits_entities.SelfCommit;
import internal.entities.jira_entities.bugs.FixVersion;
import internal.entities.jira_entities.bugs.Version;

public class BugInfo {

	private final String key;

	private List<Version> affectedVersions;

	private List<Integer> avIndexList;

	private List<FixVersion> fixedVersions;

	private List<Integer> fvIndexList;

	private int openingVersion;

	private List<SelfCommit> commits;

	/** order by increasing resolution date order */
	private LocalDate resolutionDate;

	/** we need this information in order to identify the correct opening version*/
	private LocalDate creationDate;

	/**
	 * @param key ticket key
	 * @param affectedVersions affected versions list
	 * @param fixedVersions fixed versions list
	 * @param resolutionDate ticket resolution date
	 * @param creationDate ticket creation date
	 */
	public BugInfo(String key, List<Version> affectedVersions, List<FixVersion> fixedVersions, LocalDate resolutionDate,
				   LocalDate creationDate) {
		super();
		this.key = key;
		this.affectedVersions = affectedVersions;
		this.fixedVersions = fixedVersions;
		this.resolutionDate = resolutionDate;
		this.creationDate = creationDate;
		this.commits = new ArrayList<>();
		this.fvIndexList = new ArrayList<>();
		this.avIndexList = new ArrayList<>();
	}

	public List<Integer> getAvIndexList() {
		return avIndexList;
	}

	public List<Integer> getFvIndexList() {
		return fvIndexList;
	}

	public String getKey() {
		return key;
	}

	public List<Version> getAffectedVersions() {
		return affectedVersions;
	}

	public List<FixVersion> getFixedVersions() {
		return fixedVersions;
	}

	public List<SelfCommit> getCommits() {
		return commits;
	}

	public void addAllCommits(List<SelfCommit> commits) {
		this.commits.addAll(commits);
	}

	public LocalDate getResolutionDate() {
		return resolutionDate;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	/**
	 * Compute Injected version by using proportion method
	 * @param proportion proportion value
	 */
	public void predictedIV(int proportion) {
		if (fvIndexList.isEmpty())
			return;
		int fx = fvIndexList.get(0); // we keep the oldest one
		int predictedIV = fx - (fx - openingVersion) * proportion;

		if (predictedIV < 0) {
			predictedIV = 0;
		}
		for (int i = predictedIV; i < fx; i++) {
			avIndexList.add(i);
		}

		if (avIndexList.isEmpty()) { // case predictedIv == fx
			avIndexList.add(fx);
		}
	}

	@Override
	public String toString() {
		return "BugInfo [key=" + key + "]";
	}

	public int getProportion() {
		if (avIndexList.isEmpty())
			return 0;
		int fx = fvIndexList.get(0); // we keep the oldest one
		int iv = avIndexList.get(0); // we keep the oldest one
		int proportion;
		if (fx == openingVersion) {
			proportion = 0;
		} else {
			// if the result is non-integer e.g. 3,6 we take the low floor integer
			proportion = (fx - iv) / (fx - openingVersion);
		}
		return proportion;
	}

	public void retrieveAVIndex(Map<String, Integer> indexMap) {
		avIndexList.clear();
		for (Version version : affectedVersions) {
			Integer index = indexMap.get(version.getVname());
			if (index != null) {
				avIndexList.add(index);
			}
		}

		Collections.sort(avIndexList);
	}

	public void retrieveFVIndex(Map<String, Integer> indexMap) {
		fvIndexList.clear();
		for (FixVersion version : fixedVersions) {
			Integer index = indexMap.get(version.getName());
			if (index != null) {
				fvIndexList.add(index);
			}
		}
		Collections.sort(fvIndexList);
	}

	public void setOpeningVersion(List<VersionInfo> versions) {
		if (fvIndexList.isEmpty())
			return;
		int fv = fvIndexList.get(0);
		openingVersion = fv;
		for (int i = 0; i < versions.size(); i++) {
			LocalDate releaseDate = versions.get(i).getReleaseDate();
			if (releaseDate.isAfter(creationDate)) {
				// the first version whose relation date is after the creation date of the
				// ticket
				openingVersion = i;
				break;
			}
		}

		if (openingVersion > fv) {
			openingVersion = fv;
		}
	}

	public boolean isConsistent() {
		boolean consistency = false;
		if (!getFvIndexList().isEmpty()) {
			// we have a fixed version
			if (!getAvIndexList().isEmpty()
					&& getAvIndexList().get(getAvIndexList().size() - 1) > getFvIndexList().get(0)) {
				// the last affected version is after the first fixed version
				this.affectedVersions.clear();
				this.avIndexList.clear();
				// non-consistent AVList; need to calculate with proportion
			}
			// we have a fixed version and consistency is true!
			consistency = true;

		}

		return consistency;
	}

}
