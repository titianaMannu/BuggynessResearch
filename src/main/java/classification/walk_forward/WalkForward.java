package classification.walk_forward;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import classification.arff.ArffBuilder;
import classification.arff.CSVUtils;
import classification.arff.FileBean;
import classification.enumerations.Classificator;
import classification.enumerations.Cost;
import classification.enumerations.FeatureSelector;
import classification.enumerations.Sampling;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instances;

import static classification.enumerations.Cost.NO_COST_SENSITIVE;

public class WalkForward {


    private final Map<Integer, List<FileBean>> map;

    private final EntrySet[] array;

    private final String projectName;

    private static final Logger LOGGER = Logger.getLogger(WalkForward.class.getName());

    private int globalIndex;

    public WalkForward(String filename, String projectName) throws IOException {
        super();
        this.map = new TreeMap<>();

        this.projectName = projectName;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            //la mappa ha come chiavi id della release e come vslori una lista contenente i file ad essa associati
            CSVUtils.readCSV(reader, map);
        }
        // per 2 è per via di feature selection .... da aggiungere caso cost sensitive, credo altri 2 in pratica
        this.array = new EntrySet[Classificator.values().length * Sampling.values().length * Cost.values().length * 2
                * (map.size() - 1)];

    }

    public void generateData() throws IllegalStateException, IOException, InterruptedException {
        ArffBuilder builder = new ArffBuilder(projectName);
        this.globalIndex = 0;
        ExecutorService eService = Executors.newCachedThreadPool();
        for (int key : map.keySet()) {

            if (key < 1 ) {
                continue;
            }
            // step by step training and testing building
            builder.generateTrainingSet(key - 1, map);
            builder.generateTestSet(key, map);
            evaluation(key, eService);
        }
        eService.shutdown();
        if (!eService.awaitTermination(6, TimeUnit.HOURS)) {
            LOGGER.log(Level.WARNING, "timeout occurred");
        }
    }


    private void evaluation(int key, ExecutorService eService) {
        FeatureSelection featureSelection = new FeatureSelection("trainingSet.arff", "testSet.arff");
        for (Classificator classificator : Classificator.values()) { // for each classifier
            for (Sampling sampling : Sampling.values()) { // for each balancing technique
                for (Cost sensitivity : Cost.values()) {
                    for (FeatureSelector selection : FeatureSelector.values()) {
                        final int index = this.globalIndex;
                        final int k = key;
                        final FeatureSelection mod = featureSelection;
                        eService.execute(() ->
                                inTask(index, k, mod, classificator, sampling, sensitivity, selection));

                        this.globalIndex++;
                    }
                }
            }
        }
    }


    private void inTask(int index, int key,
                        FeatureSelection featureSelection, Classificator classificator, Sampling sampling, Cost sensitivity, FeatureSelector selection) {
        Evaluation evaluation;
        try {
            //apply feature selection, if any
            Instances training = featureSelection.getTrainingSet(selection);
            Instances test = featureSelection.getTestingSet(selection);
            //apply balancing technique, if any
            Classifier fc;
            fc = Balancing.useSampling(training, classificator, sampling);
            // apply cost sensitive technique, if any
            fc = CostSensitive.costSensitive(fc, 1, 10, sensitivity, training);

            if (sensitivity != NO_COST_SENSITIVE) {
                //need to consider cost matrix
                evaluation = CostSensitive.getCostSensitiveEvaluation((CostSensitiveClassifier) fc, test);
                //evaluate model
                evaluation.evaluateModel(fc, test);
            } else {
                // evaluate model
                evaluation = new Evaluation(test);
                evaluation.evaluateModel(fc, test);
            }


            EntrySet entry = new EntrySet(evaluation, selection, classificator, sampling, sensitivity, training, test);
            entry.setNumTrainingRelease(key);
            entry.setDataSet(projectName);
            array[index] = entry;
            //audit
            String msg = String.format("iteration: %d done:: classifier: %s sampling: %s selection: %s, cost-sensitivity: %s%n",
                    key, classificator.name(),
                    sampling.name(),
                    selection.name(),
                    sensitivity.name());
            LOGGER.info(msg);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    public List<EntrySet> getData() {
        return Arrays.asList(this.array);
    }

    public static void main(String[] args) throws Exception {
        WalkForward walk = new WalkForward("OPENJPA_metrics.csv", "OPENJPA");
        walk.generateData();
        CSVUtils.generateCsv(walk.getData());
    }

}
