package classification.walk_forward;

import classification.enumerations.FeatureSelector;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureSelection {
    private Instances trainingSet;

    private Instances testSet;

    private Instances filteredTrainingSet;

    private Instances filteredTestSet;

    private static final String MSG_EXC = "Got an exception::";

    private static final Logger LOGGER = Logger.getLogger(FeatureSelection.class.getName());

    public FeatureSelection(String trainingFile, String testFile) {
        super();
        try {
            DataSource trainingSource = new DataSource(trainingFile);
            this.trainingSet = trainingSource.getDataSet();
            int numAttributes = trainingSet.numAttributes();
            trainingSet.setClassIndex(numAttributes - 1);

            DataSource testSource = new DataSource(testFile);
            this.testSet = testSource.getDataSet();
            testSet.setClassIndex(numAttributes - 1);

            getFilteredEvaluation();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, MSG_EXC, e);
        }

    }


    /**
     * filter (best first) case
     */
    private void getFilteredEvaluation() {
        Instances localTrainingSet = this.trainingSet;
        // create AttributeSelection object
        AttributeSelection filter = new AttributeSelection();
        // create evaluator and search algorithm objects
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        // set the algorithm to search backward
        search.setSearchBackwards(true);
        // set the filter to use the evaluator and search algorithm
        filter.setEvaluator(eval);
        filter.setSearch(search);
        try {
            // specify the dataset
            filter.setInputFormat(localTrainingSet);

            // apply
            this.filteredTrainingSet = Filter.useFilter(localTrainingSet, filter);

            int numAttrFiltered = this.filteredTrainingSet.numAttributes();

            // evaluation with filtered
            this.filteredTrainingSet.setClassIndex(numAttrFiltered - 1);

            this.filteredTestSet = Filter.useFilter(testSet, filter);

            this.filteredTestSet.setClassIndex(numAttrFiltered - 1);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, MSG_EXC, e);
        }

    }


    public Instances getTrainingSet(FeatureSelector selection){
        if (selection == FeatureSelector.BEST_FIRST) {
            return this.filteredTrainingSet;
        }
        return this.trainingSet;

    }

    public Instances getTestingSet(FeatureSelector selection){
        if (selection == FeatureSelector.BEST_FIRST) {
            return this.filteredTestSet;
        }
        return this.testSet;

    }


}
