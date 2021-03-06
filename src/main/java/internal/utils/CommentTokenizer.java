package internal.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentTokenizer {
	private static final String DELIMIT_STRING = JSONConfig.getProjectName() + "-";

	private static final String PROJECT_NAME = JSONConfig.getProjectName();

	private static List<String> delimiters = List.of("]", ",", "and", "[", ":", "\n", ".", ";", "ISSUE-",
			PROJECT_NAME + " " + "ISSUE #");

	private CommentTokenizer() {
		super();
	}

	public static List<String> tokenize(String comment) {
		List<String> res = new ArrayList<>();
		List<String> tokensToAnalyze = new ArrayList<>();
		for (String delimiter : delimiters) {
			if (delimiter.equals(",") || delimiter.equals("ISSUE #") || delimiter.equals("ISSUE-")
					|| delimiter.equals(PROJECT_NAME + " ")) {
				comment = comment.replace(delimiter, " " + DELIMIT_STRING).replace(":", " ");
			} else {
				comment = comment.replace(delimiter, " ");
			}

		}
		String[] tokens = comment.split(" ");
		tokensToAnalyze.add(comment);
		tokensToAnalyze.addAll(Arrays.asList(tokens));
		findIssueLink(tokensToAnalyze, res);
		return res;

	}

	private static void findIssueLink(List<String> tokensToAnalyze, List<String> res) {
		for (String token : tokensToAnalyze) {
			token = token.replace(" ", "");
			token = token.toUpperCase();
			int index = token.indexOf(DELIMIT_STRING);
			if (index == 0 && (token.length() <= DELIMIT_STRING.length() + 4) && !res.contains(token)) {
				res.add(token);
			}
		}
	}

	public static int countComments(String str) throws IOException {
		int count = 0;
		String line = "";
		Reader inputString = new StringReader(str);
		BufferedReader br = new BufferedReader(inputString);
		while ((line = br.readLine()) != null) {
			count += analyzeLine(line, br);
		}
		br.close();
		return count;
	}

	/**
	 * This method counts CL lines: everything that is not a physical line of code
	 * (LOC)
	 * 
	 * @param line
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private static int analyzeLine(String line, BufferedReader br)
			throws IOException {
			int count = 0;
			if (line.startsWith("//")) {
				count++;
			} else if (line.contains("/*") ) {
				if (line.startsWith("/*")) count++;
				while (line != null && !line.contains("*/") ) {
					count++;
					line = br.readLine();
				}
			}
		return count;

	}
}
