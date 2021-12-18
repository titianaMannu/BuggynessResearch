package internal.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import internal.entities.FileInfo;
import internal.entities.VersionInfo;

public class CSVBuilder {

	/**
	 * 
	 */
	private CSVBuilder() {
		super();
	}

	/*
	 * This method generates a csv file containing for each month of project
	 * development the corresponding commits of the searched type
	 */
	public static void generateCommitCsv(Map<String, Integer> countCommit) {
		String header = "Date,No. Commits";
		File fout = new File("data.csv");

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			bw.write(header);
			bw.newLine();
			for (Map.Entry<String, Integer> entry : countCommit.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				bw.write(key + "," + value.toString());
				bw.newLine();

			}

		} catch (Exception e) {
			Logger.getGlobal().info(e.toString());
		}
	}

	public static void generateVersionsCSV(List<VersionInfo> versions) {
		String labels = "Version,Version Name,File Name,size,LOC Touched,NR,Atuthors Number,Churn,AVG Churn,MAX Churn,ChangeSetSize,MAX ChangeSetSize,AVG ChangeSetSize,Age,Defective";
		File fout = new File(JSONConfig.getProjectName() + "_metrics.csv");

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			bw.write(labels);
			bw.newLine();
			for (int i = 0; i <= versions.size() / 2; i++) {
				VersionInfo v = versions.get(i);
				for (Map.Entry<String, FileInfo> entry : v.getFilesMap().entrySet()) {
					FileInfo file = entry.getValue();
					if (file.getSize() == 0) {
						// we are not interested finding defectiveness in files without physical LOCs
						// sloc > 0 (comments) but loc == 0
						continue;
					}
					String formatted = String.format("%d,release-%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%b", i,
							v.getName(), entry.getKey(), file.getSize(), file.getlOCTouched(), file.getRevisionNum(),
							file.getAuthorNum(), file.getChurn(), file.getAvgChurn(), file.getMaxChurn(),
							file.getChangeSetSizeSum(), file.getMaxChSetSize(), file.getAvgChSetSize(), file.getAge(),
							file.isDefective());
					bw.write(formatted);
					bw.newLine();
				}
			}

		} catch (Exception e) {
			Logger.getGlobal().info(e.toString());
		}
	}

	public static void generateDefectivenessCSV(List<VersionInfo> versions) {
		String labels = "Version,VersionID,Version Name,File Name,buggy";
		File fout = new File("defectiveness.csv");

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			bw.write(labels);
			bw.newLine();
			for (int i = 0; i < versions.size(); i++) {
				VersionInfo v = versions.get(i);
				for (Map.Entry<String, FileInfo> entry : v.getFilesMap().entrySet()) {
					String formatted = String.format("%d,%s,%s,%s,%b", i, v.getId(), v.getName(), entry.getKey(),
							entry.getValue().isDefective());
					bw.write(formatted);
					bw.newLine();
				}
			}

		} catch (Exception e) {
			Logger.getGlobal().info(e.toString());
		}
	}
}
