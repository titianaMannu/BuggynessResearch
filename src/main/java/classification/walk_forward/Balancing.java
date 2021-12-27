package classification.walk_forward;

import classification.enumerations.Classificator;
import classification.enumerations.Sampling;
import scala.Tuple2;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

public class Balancing {

    private Balancing() {
    }

    private static Classifier getClassifier(Classificator classificator) {
        switch (classificator) {
            case RANDOMFOREST:
                return new RandomForest();
            case NAIVEBAYES:
                return new NaiveBayes();
            case IBK:
                return new IBk();
            default:
                throw new IllegalArgumentException("Unexpected value: " + classificator);
        }
    }

    /**
     * simple classifier
     */
    public static Classifier getSimpleEvaluation(Classificator classificator, Instances training) throws Exception {
        Classifier cf = getClassifier(classificator);
        cf.buildClassifier(training);
        return cf;
    }

    public static FilteredClassifier getUnderSamplingEvaluator(Instances training, Classificator classificator) throws Exception {
        Classifier cl = getClassifier(classificator);

        FilteredClassifier fc = new FilteredClassifier();
        // setting classifier
        fc.setClassifier(cl);
        // underSampling options
        SpreadSubsample spreadSubsample = new SpreadSubsample();
        String[] opts = new String[]{"-M", "1.0", "-X", "0.0", "-S", "1"};

        spreadSubsample.setOptions(opts);
        fc.setFilter(spreadSubsample);
        fc.buildClassifier(training);
        return fc;
    }

    public static FilteredClassifier getOverSamplingEvaluator(Instances training, Classificator classificator) throws Exception {
        Classifier cl = getClassifier(classificator);

        FilteredClassifier fc = new FilteredClassifier();
        // setting classifier
        fc.setClassifier(cl);
        Resample resample = new Resample();
        resample.setInputFormat(training);
        long optFactor = findSamplingPercentage(training)._2;
        // oversampling options
        String[] opts = new String[]{"-B", "1.0","-S","1", "-Z", String.valueOf(optFactor)};
        resample.setOptions(opts);

        fc.setFilter(resample);
        fc.buildClassifier(training);
        return fc;
    }

    public static FilteredClassifier getSmoteEvaluator(Instances training, Classificator classificator) throws Exception {
        Classifier cl = getClassifier(classificator);

        FilteredClassifier fc = new FilteredClassifier();
        // setting classifier
        fc.setClassifier(cl);


        SMOTE smote = new SMOTE();

        String[] opts = new String[]{"-P", findSamplingPercentage(training)._1.toString(),"-K","5","-C","0"};

        smote.setOptions(opts);
        smote.setInputFormat(training);

        fc.setFilter(smote);
        fc.buildClassifier(training);
        return fc;

    }

    public static Classifier useSampling(Instances training, Classificator classificator, Sampling techSampling) throws Exception {
        Classifier fc;
        switch (techSampling) {
            case NO_SAMPLING:
                fc = getSimpleEvaluation(classificator, training);
                break;

            case UNDER_SAMPLING:
                fc = getUnderSamplingEvaluator(training, classificator);
                break;

            case OVER_SAMPLING:
                fc = getOverSamplingEvaluator(training, classificator);
                break;

           case SMOTE:
                fc = getSmoteEvaluator(training, classificator);
                break;

            default:
                throw new IllegalArgumentException("Unexpected value: " + techSampling);
        }

        return fc;
    }

    private static Tuple2<Long, Long> findSamplingPercentage(Instances training) {

        int falseClasses = 0;
        int trueClasses = 0;
        for (Instance instance : training) {
            if (instance.toString(training.numAttributes() - 1).equals("FALSE")) {
                falseClasses++;
            } else {
                trueClasses++;
            }
        }
        long sampleSizePercent = 0;
        long smotePercentage = 0;
        if (trueClasses + falseClasses > 0) {
            if (falseClasses > trueClasses) {
                // usually defective classes are a majority
                if (trueClasses > 0) {
                    smotePercentage = 100L * ((falseClasses - trueClasses) / trueClasses);
                }
                sampleSizePercent = (200L * falseClasses) / (trueClasses + falseClasses);
            } else {
                if (falseClasses > 0) {
                    smotePercentage = 100L * ((trueClasses - falseClasses) / falseClasses);
                }
                sampleSizePercent = (200L * trueClasses) / (trueClasses + falseClasses);
            }
        }
        return new Tuple2<>(smotePercentage, sampleSizePercent);
    }



}