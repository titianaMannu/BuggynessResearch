package classification.arff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArffBuilder {

	private final String attributes;

	public ArffBuilder(String projectName) throws IOException {
		super();
		Path path = Paths.get("scheleton.txt"); // qui già ignoro il nome del file perché non serve per fare la previsione
		String variables = Files.readString(path, Charset.defaultCharset());
		attributes = String.format("@RELATION %s%n%s%n", projectName, variables);
		PrintWriter writer = new PrintWriter(new File("trainingSet.arff"));
		writer.print(this.attributes);
		writer.close();
	}
	
	public void generateTestSet(int version,  Map<Integer, List<FileBean>> storeMap) throws IOException {
		PrintWriter writer = new PrintWriter("testSet.arff");
		writer.print(this.attributes);
		writer.close();
		
		appendData("testSet.arff", storeMap, version);
	}
	
	public void generateTrainingSet(int lastVersion,  Map<Integer, List<FileBean>> storeMap) throws IOException {
		appendData("trainingSet.arff", storeMap, lastVersion);
	}
	
	private void appendData(String arffFileName, Map<Integer, List<FileBean>> storeMap, int version) throws IOException {
		String defective;
		List<FileBean> beans = storeMap.get(version);
		try(FileWriter appender = new FileWriter(arffFileName, true)){
			for (FileBean fileBean : beans) {
			
				if (fileBean.isDefectiveness()) {
					defective = "TRUE";
				}else {
					defective = "FALSE";
				}
				String line = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s%n", fileBean.getVersionId(), 
						fileBean.getSize(),
						fileBean.getlOCTouched(), 
						fileBean.getNr(), 
						fileBean.getAuthNum(),
						fileBean.getChurn(),
						fileBean.getAvgChurn(),
						fileBean.getMaxChurn(),
						fileBean.getChangeSetSize(),
						fileBean.getMaxChangeSetSize(),
						fileBean.getAvgChangeSetSize(),
						fileBean.getAge(),
						defective);
				
				appender.write(line);
			}
		
		}
	}
	
	public static void main(String[] args) throws IOException {
		Map<Integer, List<FileBean>> map = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("OPENJPA_metrics.csv"))){
			CSVUtils.readCSV(reader, map);
		}
		
		ArffBuilder builder = new ArffBuilder("OPENJPA");
		builder.generateTrainingSet(0, map);
		builder.generateTrainingSet(1, map);
		builder.generateTestSet(2, map);
	}
}
