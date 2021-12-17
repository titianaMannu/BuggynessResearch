package internal.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import internal.entities.commits_entities.Committer;

public class FileInfo {

	private String pathName; // path

	private String versionIndx; // we identify an entry in the dataset with the key_couple (versionIndx,
								// pathname)

	private String oldName; // need to use when renamed is true

	private boolean renamed; // true if the file had another pathname in the previous release

	private String url; // link to the content of the file

	private boolean defective; // is the class defective ? y/n

	private long size; // lines of code LOC

	private long lOCTouched; // sum over revision of changes in terms of LOC (LOC touched)

	private long revisionNum;

	private long churn; // sum_over_Rev(addedLOC - deletedLOC)

	private List<Long> churnList;

	private long avgChurn;

	private long maxChurn;

	private List<Integer> changeSet; // list of number of files committed toghether with this one; one per revision

	private long maxChSetSize;

	private long avgChSetSize;

	private Map<String, Committer> authors; // <authorName, CommitterInfo> all authors over revision

	private long age; // age of the file in terms of weeks

	private LocalDate creationDate;

	private LocalDate releaseDate;

	private ExecutorService es;

	private static final Logger LOGGER = Logger.getLogger(FileInfo.class.getName());

	/**
	 * @param pathName
	 * @param url
	 */
	public FileInfo(String pathName, String url, ExecutorService es, LocalDate releDate, String versionIndx) {
		super();
		this.churnList = new ArrayList<>();
		this.versionIndx = versionIndx;
		this.pathName = pathName;
		this.url = url;
		this.es = es;
		this.avgChurn = 0;
		this.defective = false;
		this.lOCTouched = 0;
		this.revisionNum = 0;
		this.changeSet = new ArrayList<>();
		this.churn = 0;
		this.maxChurn = 0;
		this.maxChSetSize = 0;
		this.avgChSetSize = 0;
		this.authors = new HashMap<>();
		this.age = 0;
		this.renamed = false;
		this.oldName = "";
		this.creationDate = LocalDate.MIN;
		this.releaseDate = releDate;
	}

	public Integer getChangeSetSizeSum() {
		int res = 0;
		for (Integer integer : changeSet) {
			res += integer;
		}
		return res;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
		increaseAge(creationDate, releaseDate, 0);
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public boolean isRenamed() {
		return renamed;
	}

	public void setRenamed(boolean renamed) {
		this.renamed = renamed;
	}

	public boolean isDefective() {
		return defective;
	}

	public void setDefective(boolean defective) {
		this.defective = defective;
	}

	public long getlOCTouched() {
		return lOCTouched;
	}

	public void addLOCTouched(long lOCTouched) {
		this.lOCTouched += lOCTouched;
	}

	public String getPathName() {
		return pathName;
	}

	public String getVersionIndx() {
		return versionIndx;
	}

	public long getSize() {
		return size;
	}

	public void addAuthor(String name, Committer committer) {
		this.authors.put(name, committer);
	}

	public long getAuthorNum() {
		return this.authors.size();
	}

	public void addToChangeSet(int value) {
		this.changeSet.add(value);
		Collections.sort(this.changeSet);
		this.maxChSetSize = changeSet.get(changeSet.size() - 1); // update max value
	}

	/*private void setSize(int tokenIndex) throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject(DownloaderAgent.readJSONFromGitHub(this.url, tokenIndex));
		String encodedContent = jsonObject.getString("content");
		byte[] byteArray = Base64.getMimeDecoder().decode(encodedContent);
		String contentString = new String(byteArray);
		// don't consider empty lines; compliant with ocuntLines
		long cl = CommentTokenizer.countComments(contentString, false);
		long sloc = countLines(contentString);
		if (sloc - cl > 0) {
			this.size = sloc - cl; // LOC = SLOC - CL
		} else {
			this.size = 0;
		}

	}

	public void populateFileSize(int tokenIndex) {
		es.execute(() -> {
			try {
				setSize(tokenIndex);
			} catch (IOException | JSONException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				Thread.currentThread().interrupt();
			}
		});
	}

	public static int countLines(String str) throws IOException {
		StringReader sReader = new StringReader(str);
		BufferedReader br = new BufferedReader(sReader);
		int count = 0;
		String line;
		while ((line = br.readLine()) != null) {
			if (!line.trim().isEmpty()) {
				count++;
			}
		}
		return count;
	}*/

	public long getRevisionNum() {
		return revisionNum;
	}

	public void incrementsRevisionNum() {
		this.revisionNum++;
	}

	public long getChurn() {
		return churn;
	}

	public void setChurn(long addedLOC, long deleteLOC) {
		this.churn += (addedLOC - deleteLOC);
		this.churnList.add(addedLOC - deleteLOC);
		setMaxChurn(addedLOC - deleteLOC);
	}

	public long getMaxChurn() {
		return maxChurn;
	}

	public long getAvgCurn() {
		this.avgChurn = 0;
		if (!this.churnList.isEmpty()) {

			for (Long churnRev : this.churnList) {
				this.avgChurn += churnRev;
			}
			this.avgChurn /= this.churnList.size();
		}
		return this.avgChurn;
	}

	private void setMaxChurn(long newVal) {
		if (maxChurn < newVal) {
			this.maxChurn = newVal;
		}
	}

	public long getMaxChSetSize() {
		return maxChSetSize;
	}

	public long getAvgChSetSize() {
		avgChSetSize = 0;
		if (!this.changeSet.isEmpty()) {
			for (Integer integer : changeSet) {
				avgChSetSize += integer;
			}
			// update average; Casting to integer number because it doesn't make sense to
			// consider half file
			avgChSetSize /= changeSet.size();
		}
		return avgChSetSize;
	}

	public long getAge() {
		return age;
	}

	public void increaseAge(LocalDate prevDate, LocalDate currReleaseDate, long prevVal) {
		long weeks = ChronoUnit.WEEKS.between(prevDate, currReleaseDate);
		this.age = prevVal + weeks;
	}

	public Map<String, Committer> getAuthors() {
		return authors;
	}

	public void addAllAuthors(Map<String, Committer> others) {
		this.authors.putAll(others);
	}

	public String getUrl() {
		return url;
	}

}
