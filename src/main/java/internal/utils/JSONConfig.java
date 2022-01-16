package internal.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONConfig {

	private static final Logger LOGGER = Logger.getLogger(JSONConfig.class.getName());

	private static final JSONObject jsonObject = retrieveConfig();

	private static String project = null;

	private static String issue = null;

	private static String repository = null;

	private static String jiraApi = null;

	private static final Integer MAX_TOKEN = 2;

	private static final Integer MIN_TOKEN = 0;

	private static final Random random = new SecureRandom();

	/**
	 * retrieve configuration's parameters from a JSON file
	 */
	private static JSONObject retrieveConfig() {
		// JSON parser object to parse read file
		JSONObject jsonObject = null;
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("config.json")) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			jsonObject = (JSONObject) obj;

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Got an exception::", e);
		}
		return jsonObject;
	}

	public static String getProjectName() {
		if (project == null) {
			project = (String) jsonObject.get("project"); // get a projetct name to use
		}
		return project;
	}

	public static String getIssueType() {
		if (issue == null) {
			issue = (String) jsonObject.get("issue"); // get an issue type to use
		}
		return issue;
	}

	public static String getEncodedTocken(int index) {
		// get oauth encrypted token for githubApi
		if (jsonObject.get("encodedToken_" + index) == null) {
			index = getPseudoRandomIndex();
		}
		return jsonObject.get("encodedToken_" + index).toString();
	}

	public static String getRepository() {
		if (repository == null) {
			repository = (String) jsonObject.get("repository"); // get partial url repository
		}
		return repository;
	}

	public static String getJiraApi() {
		if (jiraApi == null) {
			jiraApi = (String) jsonObject.get("jira_api"); // get partial url for jira rest api
		}
		return jiraApi;
	}

	public static void main(String[] args) {

		String infoString = String.format("jira api %s", getJiraApi());
		LOGGER.info(infoString);
	}

	public static int getPseudoRandomIndex() {
		return random.nextInt((MAX_TOKEN - MIN_TOKEN) + 1) + MIN_TOKEN;
	}
}