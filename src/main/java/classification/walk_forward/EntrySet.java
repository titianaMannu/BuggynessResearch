package classification.walk_forward;

import classification.enumerations.Classificator;
import classification.enumerations.Cost;
import classification.enumerations.FeatureSelector;
import classification.enumerations.Sampling;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

public class EntrySet {

	private String dataSet;
	 
	private int numTrainingRelease;
	
	private double percDefectiveInTraining;
	
	private double percDefectiveInTesting;
	
	private Classificator classifierType;
	
	private Sampling sampling;
	
	private FeatureSelector usingFeatureSelection;

	private Cost sensitivity;
	
	private double tp;
	
	private double fp;
	
	private double tn;
	
	private double fn;
	
	private double precision;
	
	private double recall;
	
	private double auc;
	
	private double kappa;

	public EntrySet(Evaluation eval, FeatureSelector selection, Classificator classificator, Sampling sampling, Cost sensitivity, Instances trainingSet, Instances testSet) {
		this.classifierType = classificator;
		this.sampling = sampling;
		this.usingFeatureSelection = selection;
		this.percDefectiveInTraining = calculatePerc(trainingSet);
		this.percDefectiveInTesting = calculatePerc(testSet);
		this.sensitivity = sensitivity;
		this.tp = eval.numTruePositives(1);
		this.fp = eval.numFalsePositives(1);
		this.tn = eval.numTrueNegatives(1);
		this.fn = eval.numFalseNegatives(1);
		
		this.auc = eval.areaUnderROC(1);
		this.precision = eval.precision(1);
		this.kappa = eval.kappa();
		this.recall = eval.recall(1);
		
		
	}

	public FeatureSelector getUsingFeatureSelection() {
		return usingFeatureSelection;
	}

	public Cost getSensitivity() {
		return sensitivity;
	}

	private double calculatePerc(Instances dataSet) {
		double falseClasses = 0.0;
		double trueClasses = 0.0;
		int numAttributes = dataSet.numAttributes();
		for(Instance instance: dataSet){
		    if (instance.toString(numAttributes - 1).equals("FALSE")) {
		    	falseClasses++;
		    }else {
				trueClasses++;
			}
		}

		double res = 0.0;
		
		if (trueClasses + falseClasses > 0) {
			res = trueClasses / (trueClasses + falseClasses);
		}
		
		return res;

	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public int getNumTrainingRelease() {
		return numTrainingRelease;
	}

	public void setNumTrainingRelease(int numTrainingRelease) {
		this.numTrainingRelease = numTrainingRelease;
	}

	public double getPercDefectiveInTraining() {
		return percDefectiveInTraining;
	}

	public double getPercDefectiveInTesting() {
		return percDefectiveInTesting;
	}

	public Classificator getClassifierType() {
		return classifierType;
	}

	public Sampling getSampling() {
		return sampling;
	}


	public double getTp() {
		return tp;
	}

	public double getFp() {
		return fp;
	}

	public double getTn() {
		return tn;
	}

	public double getFn() {
		return fn;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getAuc() {
		return auc;
	}

	public double getKappa() {
		return kappa;
	}
	
	
	
}
