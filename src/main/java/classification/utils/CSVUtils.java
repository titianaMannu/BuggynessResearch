package classification.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

import classification.walk_forward.EntrySet;
import internal.utils.JSONConfig;

public class CSVUtils {
	
	private CSVUtils() {
		super();
	}


	public static String readCSV(BufferedReader reader, Map<Integer, List<FileBean>> storeMap) throws IOException {
		String line = null;
		int index = 1;
		String headerLine = reader.readLine(); // we don't consider header line
		while ((line = reader.readLine()) != null) {
			FileBean fileBean = new FileBean();
			try(Scanner scanner =  new Scanner(line)){
				scanner.useDelimiter(",");
				while(scanner.hasNext()) {
					String data = scanner.next();
					switch (index) {
						case 1: 
							fileBean.setVersionId(Integer.parseInt(data));
							break;	
						case 2: 
							fileBean.setVersionName(data);
							break;
						case 3:
							fileBean.setFileName(data);
							break;
						case 4:
							fileBean.setSize(Long.parseLong(data));
							break;
						case 5:
							fileBean.setlOCTouched(Long.parseLong(data));
							break;
						case 6:
							fileBean.setNr(Long.parseLong(data));
							break;
						case 7:
							fileBean.setAuthNum(Long.parseLong(data));
							break;
						case 8:
							fileBean.setChurn(Long.parseLong(data));
							break;
						case 9:
							fileBean.setAvgChurn(Long.parseLong(data));
							break;
						case 10:
							fileBean.setMaxChurn(Long.parseLong(data));
							break;
						case 11: 
							fileBean.setChangeSetSize(Long.parseLong(data));
							break;
						case 12:
							fileBean.setMaxChangeSetSize(Long.parseLong(data));
							break;
						case 13:
							fileBean.setAvgChangeSetSize(Long.parseLong(data));
							break;
						case 14:
							fileBean.setAge(Long.parseLong(data));
							break;
						case 15:
							fileBean.setDefectiveness(Boolean.parseBoolean(data));
							break;
						default:
							throw new IllegalArgumentException("Unexpected value: " + index);
					}
					
					index++;
				
				}
			index= 1;
			updateMap(storeMap, fileBean);
			
			}
		}
		return headerLine;
	}
	
	
	private static void updateMap(Map<Integer, List<FileBean>> storeMap, FileBean file) {
		List<FileBean> list;
		if ((list = storeMap.get(file.getVersionId())) != null) {
			list.add(file);
		}else {
			list = new ArrayList<>();
			list.add(file);
			storeMap.put(file.getVersionId(), list);
		}
		
	}
	
	public static void generateCsv(List<EntrySet> data) {
		String projectName = JSONConfig.getProjectName();
		String header = "dataset;#TrainingRelease;%Defective in training;%Defective in testing;"
				+ "classifier;balancing;Feature Selection;cost;TP;FP;TN;FN;Precision;Recall;FMeasure;ROC Area;Kappa";
		File fout = new File(projectName + "-walkForward.csv");

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			bw.write(header);
			bw.newLine();
			for (EntrySet entry : data) {
				String str= String.format("%s;%d;%.2f;%.2f;%s;%s;%s;%s;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f",
						entry.getDataSet(),
						entry.getNumTrainingRelease(),
						entry.getPercentageDefectiveInTraining() * 100,
						entry.getPercentageDefectiveInTesting() * 100,
						entry.getClassifierType().name(),
						entry.getSampling().name(),
						entry.getUsingFeatureSelection().name(),
						entry.getSensitivity(),
						entry.getTp(),
						entry.getFp(),
						entry.getTn(),
						entry.getFn(),
						entry.getPrecision(),
						entry.getRecall(),
						entry.getfMeasure(),
						entry.getAuc(),
						entry.getKappa());
				
				bw.write(str);
				bw.newLine();

			}
		} catch (Exception e) {
			Logger.getGlobal().info(e.toString());
		}
	}

	
	public static void main(String[] args) throws  IOException {
		Map<Integer, List<FileBean>> map = new TreeMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("OPENJPA_metrics.csv"))){
			CSVUtils.readCSV(reader, map);
		}
	}
}
