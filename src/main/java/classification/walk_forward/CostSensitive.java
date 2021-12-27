package classification.walk_forward;

import classification.enumerations.Cost;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instances;

public class CostSensitive {

    private CostSensitive() {
    }

    private static CostMatrix createCostMatrix(double falsePositiveWeight, double falseNegativeWeight) {
        CostMatrix costMatrix = new CostMatrix(2);
        costMatrix.setCell(0, 0, 0.0);
        costMatrix.setCell(1, 0, falsePositiveWeight);
        costMatrix.setCell(0, 1, falseNegativeWeight);
        costMatrix.setCell(1, 1, 0.0);
        return costMatrix;
    }

    public static Classifier costSensitive(Classifier classifier, double falsePositiveWeight, double falseNegativeWeight, Cost sensitivity, Instances training) throws Exception {
        CostSensitiveClassifier costSensitiveCf ;
        switch (sensitivity) {
            case SENSITIVE_LEARNING:
                costSensitiveCf = getCostSensitiveClassifier(classifier, falsePositiveWeight, falseNegativeWeight, training);
                costSensitiveCf.setMinimizeExpectedCost(false);
                costSensitiveCf.buildClassifier(training);
                break;
            case SENSITIVE_THRESHOLD:
                costSensitiveCf = getCostSensitiveClassifier(classifier, falsePositiveWeight, falseNegativeWeight, training);
                costSensitiveCf.setMinimizeExpectedCost(true);
                costSensitiveCf.buildClassifier(training);
                break;
            default:
                return classifier;
        }
        return costSensitiveCf;
    }

    public static Evaluation getCostSensitiveEvaluation(CostSensitiveClassifier classifier, Instances test) throws Exception {
        // evaluate model
        return new Evaluation(test, classifier.getCostMatrix());
    }

    private static CostSensitiveClassifier getCostSensitiveClassifier(Classifier classifier, double falsePositiveWeight, double falseNegativeWeight, Instances training) throws Exception {
        CostSensitiveClassifier costSensitiveCf = new CostSensitiveClassifier();
        costSensitiveCf.setClassifier(classifier);
        costSensitiveCf.setCostMatrix(createCostMatrix(falsePositiveWeight, falseNegativeWeight));

        return costSensitiveCf;
    }
}

