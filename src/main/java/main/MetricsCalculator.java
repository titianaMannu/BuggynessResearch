package main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import internal.utils.CSVBuilder;
import internal.data_scraping.CommitsInformation;
import internal.entities.FileInfo;
import internal.entities.VersionInfo;
import internal.entities.commits_entities.Committer;
import internal.entities.commits_entities.File;
import internal.entities.commits_entities.SelfCommit;

public class MetricsCalculator {

    private static final Logger LOGGER = Logger.getLogger(MetricsCalculator.class.getName());

    private static final String TIMEOUT_MSG = "timeout occurred, some pages could be lost\n";

    private final List<VersionInfo> versions;

    private final Map<String, SelfCommit> storeMap;

    public MetricsCalculator() {
        super();
        storeMap = new HashMap<>();
        VersionLinker versionLinker = VersionLinker.getInstance();
        // already ordered list of Jira versions with a valid link to github
        versions = versionLinker.getJiraVersions();
    }

    public void buildDataSet() throws InterruptedException, IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        //consider only first half to reduce class with snoring defects
        for (int i = 0; i <= versions.size() / 2; i++) {
            VersionInfo version = VersionLinker.getInstance().getJiraVersions().get(i);
            final int j = i;

            version.getCommitsUnderRelease();
            this.getMetrics(version, j);

            String msg = String.format("version: %s done!", version.getName());
            LOGGER.log(Level.INFO, msg);
            es.execute(() -> {
                try {
                    version.populateFilesSize();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Got an exception::", e);
                    Thread.currentThread().interrupt();
                }
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(10, TimeUnit.MINUTES);
        if (!finished) {
            LOGGER.log(Level.WARNING, TIMEOUT_MSG);
        }

        findDefectiveness();
        CSVBuilder.generateVersionsCSV(versions);
    }

    private void getMetrics(VersionInfo version, int i) throws IOException {
        SelfCommit[] commits = version.getCommits();
        for (int j = 0; j < commits.length; j++) {
            // iterate over revision starting from the most recent; otherwise we could have
            // problems with renaming operations
            storeMap.put(commits[j].getSelfCommitSha(), commits[j]);
            setMetrics(commits[j], version.getFilesMap());
        }
        if (i > 0) {
            updateReleaseCumulative(versions.get(i - 1), version);
        }
    }

    public void findDefectiveness() throws IOException, InterruptedException {
        DefectivenessCalculator dCalculator = new DefectivenessCalculator();
        dCalculator.findDefectiveness();
    }

    /**
     * If a file is renamed it takes the age and the number of authors corresponding to the old filename
     * @param prevVersion previous version
     * @param currVersion current version
     */
    private void updateReleaseCumulative(VersionInfo prevVersion, VersionInfo currVersion) {
        for (Map.Entry<String, FileInfo> entry : currVersion.getFilesMap().entrySet()) {
            FileInfo olderFileInfo = prevVersion.getFilesMap().get(entry.getKey());
            if (olderFileInfo == null && entry.getValue().isRenamed()) {
                olderFileInfo = prevVersion.getFilesMap().get(entry.getValue().getOldName());
            }
            if (olderFileInfo != null) {
                entry.getValue().increaseAge(prevVersion.getReleaseDate(), currVersion.getReleaseDate(),
                        olderFileInfo.getAge());
                entry.getValue().addAllAuthors(olderFileInfo.getAuthors());
                if (entry.getValue().getlOCTouched() == 0) { // case: no commits or renamed
                    entry.getValue().setSize(olderFileInfo.getSize()); // no loc_touched == same size!
                }
            }
        }
    }

    private void setMetrics(SelfCommit commit, Map<String, FileInfo> filesMap) throws IOException {
        for (File f : commit.getFiles()) {

            FileInfo file = filesMap.get(f.getFilename());
            if (file != null || (file = findOlderNameFiles(f.getFilename(), filesMap)) != null) {
                // we don't consider files added and removed before the current release and
                // after the previous one
                if (f.getStatus().equals("renamed")) {
                    file.setRenamed(true);
                    file.setOldName(f.getPreviousFilename());
                } else if (f.getStatus().equals("added")) {
                    CommitsInformation.getInstance();
                    LocalDate creationDate = CommitsInformation
                            .formatCommitDate(commit.getCommitData().getCommitter().getDate());
                    file.setCreationDate(creationDate);
                }
                f.retrieveChangesLOCView();
                // setting churn LOCadditions - LOCdeletions
                file.setChurn(f.getAdditions(), f.getDeletions());
                // setting LOC touched
                file.addLOCTouched(f.getChanges());
                // increments NR when a commit changes a file in this release
                file.incrementsRevisionNum();
                // add the size of committed files minus the current one
                file.addToChangeSet(commit.getFiles().size() - 1);
                // add the author
                Committer committer = commit.getCommitData().getCommitter();
                if (committer != null) {
                    file.addAuthor(committer.getName(), committer);
                }
            }
        }
    }

    private FileInfo findOlderNameFiles(String filename, Map<String, FileInfo> filesMap) {
        for (Map.Entry<String, FileInfo> currFile : filesMap.entrySet()) {
            if (currFile.getValue().isRenamed() && filename.equals(currFile.getValue().getOldName())) {
                return currFile.getValue(); // return the file whose previous name correspond to the current file
            }
        }
        return null;

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MetricsCalculator mCalculator = new MetricsCalculator();
        mCalculator.buildDataSet();
    }
}
