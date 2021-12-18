package main;



import internal.data_scraping.CommitsInformation;
import internal.data_scraping.JIRAInformation;
import internal.entities.commits_entities.CommitInfo;
import internal.entities.jira_entities.Issue;
import internal.entities.jira_entities.JIRAContent;
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

    public CommitInfo[] getInfos() {
        return infos;
    }

    public Map<String, List<CommitInfo>> getCommitsPerTicketMap() {
        return commitsPerTicketMap;
    }

}
