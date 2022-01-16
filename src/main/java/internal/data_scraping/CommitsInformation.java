package internal.data_scraping;

import com.google.gson.Gson;
import internal.entities.commits_entities.CommitInfo;
import internal.entities.commits_entities.Compare;
import internal.entities.commits_entities.SelfCommit;
import internal.utils.DownloaderAgent;
import internal.utils.JSONConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommitsInformation {

    private static final Logger LOGGER = Logger.getLogger(CommitsInformation.class.getName());

    private CommitInfo[] commitsInfo;

    private SelfCommit[] commits;
    private static final String TIMEOUT_MSG = "'timeout occurred";

    private static CommitsInformation instance = null;

    public static CommitsInformation getInstance() {
        if (instance == null) {
            instance = new CommitsInformation();
        }
        return instance;
    }

    /**
     *
     */
    private CommitsInformation() {
        super();
        commitsInfo = new CommitInfo[0];
    }

    private void downloadCommits(String url, int tokenIndex, String cachePath, int j, String[] list){
        String reString = null;
        try {
            reString = DownloaderAgent.readJsonFromGitHub(url, tokenIndex, cachePath, url);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Got an exception:", e);
        }

        fillJsonList(reString, j, list);

    }

    public CommitInfo[] retrieveCommits(String queryString) throws InterruptedException {
        /* Get project name */
        int requestPerThread = 10;
        String project = JSONConfig.getProjectName();
        int currpage = 1;
        boolean endCondition;
        Gson gson = new Gson();
        List<CommitInfo> listOfAllCommit = new ArrayList<>();
        do {
            final int tokenIndex = JSONConfig.getPseudoRandomIndex();
            String[] list = new String[requestPerThread];
            ExecutorService es = Executors.newCachedThreadPool();
            for (int i = 0; i < requestPerThread; i++) {
                String url = JSONConfig.getRepository() + project.toLowerCase(Locale.ROOT) + "/commits?page=" + currpage
                        + "&per_page=100" + queryString;
                final int j = i;
                es.execute(() -> downloadCommits(url, tokenIndex,"cache/commits/" , j, list));
                currpage++;

            }
            es.shutdown();
            if (!es.awaitTermination(1, TimeUnit.MINUTES)) {
                LOGGER.log(Level.WARNING, TIMEOUT_MSG);
            }

            endCondition = false;
            for (String jsonString : list) {
                if (!jsonString.equals("[]")) {// update commits list
                    CommitInfo[] commitList = gson.fromJson(jsonString, CommitInfo[].class);
                    listOfAllCommit.addAll(Arrays.asList(commitList));
                    if (commitList.length < 100) {
                        // the next page will be empty
                        endCondition = true;
                    }
                } else {
                    endCondition = true;
                }
            }
        } while (!endCondition);

        sortCommitInfos(listOfAllCommit);
        return listOfAllCommit.toArray(new CommitInfo[listOfAllCommit.size()]);

    }

    public CommitInfo[] getCommitsInfo() throws InterruptedException {
        if (commitsInfo.length == 0) {
            this.commitsInfo = retrieveCommits("");
        }
        return commitsInfo;
    }

    public static LocalDate formatCommitDate(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateStr = dateStr.replace("T", " ").replace("Z", "");
        return LocalDate.parse(dateStr, dateTimeFormatter);
    }

    private static void fillJsonList(String json, int index, String[] list) {
        list[index] = json;
    }


    private void sortCommitInfos(List<CommitInfo> infos) {

        Collections.sort(infos, (CommitInfo c1, CommitInfo c2) -> {
            LocalDate c1Date = formatCommitDate(c1.getCommit().getCommitter().getDate());
            LocalDate c2Date = formatCommitDate(c2.getCommit().getCommitter().getDate());
            if (c1Date.isBefore(c2Date)) {
                return 1;
            } else if (c2Date.isBefore(c1Date)) {
                return -1;
            } else {
                return 0;
            }

        });

    }


    private static void fillSelfCommitList(SelfCommit selfCommit, int index, SelfCommit[] array) {
        array[index] = selfCommit;
    }

    public SelfCommit getFullCommit(String commitUrl, String commitSha) throws IOException {
        // this method should be executed in a thread because it does multiple
        // http_requests
        List<SelfCommit> tempCommitList = new ArrayList<>();
        int page = 1;
        int tokenIndex;
        String completeQuery;
        Gson gson = new Gson();
        SelfCommit currCommit;
        do { // we consider cases with more of 300 files distributed in multiple pagesS
            tokenIndex = JSONConfig.getPseudoRandomIndex(); // update token
            completeQuery = String.format("%s?page=%d", commitUrl, page);
            String jsonString = DownloaderAgent.readJsonFromGitHub(completeQuery, tokenIndex, "cache/commits/commit/" + commitSha + "/", Integer.toString(page));
            currCommit = gson.fromJson(jsonString, SelfCommit.class);
            if (currCommit == null) {
                break;
            }
            tempCommitList.add(currCommit);
            page++; // go to the next page

            // exit condition : files are less than 300 (300/page is the limit)
        } while (currCommit.getFiles().size() == 300);

        SelfCommit resCommit = tempCommitList.get(0);
        for (int i = 1; i < tempCommitList.size(); i++) { // regroup file information in a single object
            resCommit.getFiles().addAll(tempCommitList.get(i).getFiles());
        }

        return resCommit;
    }

    public SelfCommit[] getCommitsBody(List<CommitInfo> commitInfoList) {
        SelfCommit[] fullCommits = new SelfCommit[commitInfoList.size()];

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < commitInfoList.size(); i++) {
            final int j = i;
            final String url = commitInfoList.get(i).getUrl();
            final String sha = commitInfoList.get(i).getSha();
            es.execute(() -> {
                if (url != null) {
                    try {
                        fillSelfCommitList(getFullCommit(url, sha), j, fullCommits);
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Got an exception::", e);
                    }
                }
            });
        }

        es.shutdown();
        try {
            if (!es.awaitTermination(5, TimeUnit.MINUTES)) {
                LOGGER.log(Level.WARNING, TIMEOUT_MSG);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Got an exception::", e);
            Thread.currentThread().interrupt();
        }

        sortSelfCommitInfos(Arrays.asList(fullCommits));
        return fullCommits;
    }

    public SelfCommit[] getCommits() throws InterruptedException {
        if (commits.length == 0) {
            getCommitsInfo();
            commits = getCommitsBody(Arrays.asList(this.commitsInfo));
        }
        return commits;
    }

    private static void sortSelfCommitInfos(List<SelfCommit> infos) {
        Collections.sort(infos, (SelfCommit c1, SelfCommit c2) -> {
            LocalDate c1Date = formatCommitDate(c1.getCommitData().getCommitter().getDate());
            LocalDate c2Date = formatCommitDate(c2.getCommitData().getCommitter().getDate());
            if (c1Date.isBefore(c2Date)) {
                return 1;
            } else if (c2Date.isBefore(c1Date)) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    public CommitInfo[] compare(String prevRev, String currRev, int tokenIndex) throws IOException {
        String url = JSONConfig.getRepository() + JSONConfig.getProjectName() + "/compare/" + prevRev + "..." + currRev;
        //get all commit for the current release (after the previous one)
        String json = DownloaderAgent.readJsonFromGitHub(url, tokenIndex, "cache/compare/", "compare-" + prevRev + "-" + currRev);
        Gson gson = new Gson();
        Compare compare = gson.fromJson(json, Compare.class);
        if (!compare.getCommits().contains(compare.getBaseCommit())) {
            //include base commit
            compare.getCommits().add(compare.getBaseCommit());
        }

        CommitInfo[] commitInfos = new CommitInfo[compare.getCommits().size()];
        return compare.getCommits().toArray(commitInfos);
    }

}
