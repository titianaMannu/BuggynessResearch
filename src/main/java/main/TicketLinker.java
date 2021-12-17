package main;



import internal.data_scraping.CommitsInformation;
import internal.data_scraping.JIRAInformation;
import internal.entities.commits_entities.CommitInfo;
import internal.entities.jira_entities.Issue;
import internal.entities.jira_entities.JIRAContent;
import internal.utils.CSVBuilder;
import internal.utils.CommentTokenizer;
import scala.Tuple2;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tiziana
 */
public class TicketLinker {
    private static final Logger LOGGER = Logger.getLogger(TicketLinker.class.getName());
    //ticket-key, last commit-date related to it
    private final Map<String, LocalDate> ticketMap;

    private final Map<String, List<CommitInfo>> commitsPerTicketMap;

    private final Map<YearMonth, Integer> fixedBugPerMonth;
    //list of all commits in descendent order; most recent is the first one
    private final CommitInfo[] infos;

    private static TicketLinker instance = null;

    public static TicketLinker getInstance() throws InterruptedException {
        if (instance == null) {
            instance = new TicketLinker();
        }
        return instance;

    }

    private TicketLinker() throws InterruptedException {
        super();
        this.ticketMap = new HashMap<>();
        this.commitsPerTicketMap = new HashMap<>();
        this.fixedBugPerMonth = new HashMap<>();

        JIRAInformation jiraInformation = JIRAInformation.getInstance();
        List<JIRAContent> ticketPerPageList;
        try {
            ticketPerPageList = jiraInformation.getJiraTickets();
            for (JIRAContent jiraContent : ticketPerPageList) {
                for (Issue ticket : jiraContent.getIssues()) {
                    ticketMap.put(ticket.getKey(), LocalDate.MIN);
                    commitsPerTicketMap.put(ticket.getKey(), new ArrayList<>());
                }
            }
        } catch (IOException e1) {
            LOGGER.log(Level.WARNING, "Got an exception::", e1);
        }
        CommitsInformation commitsInformation = CommitsInformation.getInstance();
        this.infos = commitsInformation.getCommitsInfo();

    }

    public void ticketLink() {
        // initialization of a map for tickets <ticketKey, lastCommitDate>
        for (int i = 0; i < this.infos.length; i++) {
            CommitInfo commitInfo = this.infos[i];
            // getting the message
            List<String> ticketKeyList = CommentTokenizer.tokenize(commitInfo.getCommit().getMessage());
            if (!ticketKeyList.isEmpty()) {
                updateTicketMaps(ticketKeyList, commitInfo, i);
            }
        }

    }

    private void updateTicketMaps(List<String> ticketKeyList, CommitInfo commitInfo, int commitIndex) {
        // retrieve commit date
        LocalDate commitDate = CommitsInformation.formatCommitDate(commitInfo.getCommit().getCommitter().getDate());
        // then iterate
        for (String key : ticketKeyList) {
            this.ticketMap.computeIfPresent(key, (k, v) -> {
                if (commitDate.isAfter(v)) {
                    return commitDate;
                }
                return v;
            });
            this.commitsPerTicketMap.computeIfPresent(key, (k, v) -> {
                v.add(this.infos[commitIndex]);
                return v;
            });

        }

    }


    /**
     * Given All commits this function counts how many commits are done per month ignoring month with 0 commits.
     * Inactivity period
     *
     * @param listOfAllCommits all commit to count
     * @return Map [month, commit]
     */
    public Map<YearMonth, Integer> countCommitPerMonth(CommitInfo[] listOfAllCommits) {
        LocalDate date;
        Map<YearMonth, Integer> commitPerMonth = new HashMap<>();
        // get first date corresponding to the last item of the list
        LocalDate firstDate = CommitsInformation
                .formatCommitDate(listOfAllCommits[listOfAllCommits.length - 1].getCommit().getCommitter().getDate());
        YearMonth month = YearMonth.from(firstDate);
        commitPerMonth.put(month, 0);
        for (CommitInfo commit : listOfAllCommits) {
            date = CommitsInformation.formatCommitDate(commit.getCommit().getCommitter().getDate());
            if (YearMonth.from(date).equals(month)) {
                //add a commit to the corresponding month
                commitPerMonth.computeIfPresent(month, (k, v) -> v + 1);
            } else {
                //remember dates are ordered so sync to the correct month
                month = YearMonth.from(date);
                // count a commit for this month
                commitPerMonth.put(month, 1);
            }
        }
        return commitPerMonth;
    }

    public Map<YearMonth, Integer> countFixedBugPerMonth() {
        YearMonth month;
        Map<YearMonth, Integer> commitPerMonth = countCommitPerMonth(this.infos);
        // first need to populate ticketMap
        ticketLink();

        // count fixed commit per month
        for (Map.Entry<String, LocalDate> entry : ticketMap.entrySet()) {
            month = YearMonth.from(entry.getValue());
            this.fixedBugPerMonth.putIfAbsent(month, 1);
            this.fixedBugPerMonth.computeIfPresent(month, (k, v) -> v + 1);
        }
        // consider month with commits but without fixed commits
        commitPerMonth.forEach((key, val) -> this.fixedBugPerMonth.putIfAbsent(key, 0));

        return fixedBugPerMonth;
    }

    public Map<YearMonth, Tuple2<Integer, Integer>> commitAndFixedBugPerMonth(Map<YearMonth, Integer> fixedBugPerMonth, Map<YearMonth, Integer> commitPerMonth){
        Map<YearMonth, Tuple2<Integer, Integer>> commitAndFixedBugPerMonth = new HashMap<>();
        fixedBugPerMonth.forEach((key, val) -> commitAndFixedBugPerMonth.put(key, new Tuple2<>(val, commitPerMonth.get(key))));

        return commitAndFixedBugPerMonth;
    }

    public void logInfoTicketMap() {
        int founded = 0;
        int counter = 0;
        StringBuilder bld = new StringBuilder();
        bld.append("\n");
        for (Map.Entry<String, LocalDate> entry : ticketMap.entrySet()) {
            if (entry.getValue().equals(LocalDate.MIN)) {
                counter++;
            } else {
                bld.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
                founded++;
            }

        }
        LOGGER.log(Level.INFO, "list of all couples <ticket, last commit date>:: {0}", bld);
        LOGGER.log(Level.INFO, "tickets without commits in this repository {0}", counter);
        LOGGER.log(Level.INFO, "tickets with a corresponding commit in thi repository {0}", founded);
    }

    public Map<String, LocalDate> getTicketMap() {
        return ticketMap;
    }

    public Map<YearMonth, Integer> getFixedBugPerMonth() {
        return fixedBugPerMonth;
    }

    public CommitInfo[] getInfos() {
        return infos;
    }

    public Map<String, List<CommitInfo>> getCommitsPerTicketMap() {
        return commitsPerTicketMap;
    }

   /* public static void main(String[] args) throws InterruptedException {

        TicketLinker linker = TicketLinker.getInstance();

        Map<YearMonth, Integer> fixedCommitPerMonth = linker.countFixedBugPerMonth();
        linker.logInfoTicketMap();
        String msgString = String.format("total tickets founded: %d", linker.ticketMap.size());
        LOGGER.log(Level.INFO, msgString);
        fixedCommitPerMonth.entrySet().removeIf(entries -> entries.getKey().equals(YearMonth.from(LocalDate.MIN)));
        CSVBuilder.generateCommitCsv(linker.commitAndFixedBugPerMonth(fixedCommitPerMonth, linker.countCommitPerMonth(linker.getInfos())));
    }*/

}
